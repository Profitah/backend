api문서 : https://www.notion.so/API-3f5d751821ca409481db811e0e5e6940

실행오류 없애기 : 
1. jdbc 서버 등등 필요해 보이는 프로그램을 때려박는다.<br>
2. 2024-04-16T15:56:56.224+09:00 ERROR 45559 --- [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is java.lang.RuntimeException: Driver com.mysql.cj.jdbc.Driver claims to not accept jdbcUrl, ${DB_JDBC_URL}<br>
application.yml 에서 ${DB_JDBC_URL}을 밀어버린다. 나중에 서버가 잘 작동하지 못할것 같긴한데... 일단 돌리고 나서 생각하기로 했다. <Br>
3. 2024-04-16T19:46:58.058+09:00 ERROR 57511 --- [  restartedMain] o.s.b.d.LoggingFailureAnalysisReporter  : 모르겠다. 
