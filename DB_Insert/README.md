## DB Insert 비교

### 대량의 데이터를 Database에 Insert 할 시 방법에 따라 처리 속도를 비교합니다.
### 비교 내용
1. JPA를 통한 단순 Insert를 사용하는 방법
2. JDBC Bulk Insert를 사용하는 방법
3. Spring Batch를 사용하는 방법
4. Spring Data JDBC를 사용하는 방법

필요한 의존성
- Spring Data JPA
- Spring Data JDBC
- Spring Batch
- MySQL
- Lombok

application.yml

```yaml
spring:
  application:
    name: DB_Insert

  datasource:
    url: jdbc:mysql://localhost:3306/db-insert-mysql?rewriteBatchedStatements=true
    rewriteBatchedStatements=true = (MySQL JDBC 드라이버가 배치 삽입 쿼리를 단일 쿼리로 재작성하여 DB로 전송하도록 지시)
    username: test
    password: test
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
```
Local 실행 조건

docker 설치

실행 방법
1. 터미널에서 docker-compose up을 통하여 db 컨테이너 실행
2. Application 실행
3. Swagger를 통하여 데이터 넣을 양을 정하고 데이터 생성
4. 처리 속도 비교

중복 검증이 필요한 대량 데이터 삽입의 경우는 해당 방법은 효과가 없음.
-> 하나씩 검증하고 삽입해야하기 때문.

따라서 검증이 필요하지 않은 대량 데이터 생성에 적합