package capstone.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    public final TokenProvider tokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // HTTP 요청에서 토큰을 추출합니다.
        String jwt = resolveToken(request);

        // 로그 추가
        System.out.println("Received Token: " + jwt);

        // 추출된 토큰이 유효하고, 검증이 성공하면 해당 토큰으로부터 Authentication 객체를 생성하여 SecurityContextHolder에 설정합니다.
        if (!(request.getRequestURI().equals("/signup") || request.getRequestURI().equals("/login"))) {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 토큰이 유효하지 않은 경우 로그를 남기고 적절한 응답을 설정하세요.
                logger.warn("유효하지 않은 토큰입니다: " + jwt);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"유효하지 않은 토큰입니다.\"}");
                return;
            }
        }

        // 다음 필터로 전달합니다.
        filterChain.doFilter(request, response);
    }

    // HTTP 요청에서 Authorization 헤더로부터 토큰을 추출하는 메소드입니다.
    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 토큰을 추출하는 메서드
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // Authorization 헤더에서 토큰 값을 가져옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // 토큰 값이 비어있지 않고 Bearer 접두사로 시작하는 경우
            return bearerToken.substring(7);
            // Bearer 접두사를 제외한 실제 토큰 값을 반환
        }
        return null;
        // 토큰이 없는 경우 null을 반환
    }
}
