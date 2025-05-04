## DB Insert 비교

### 대량의 데이터를 Database에 Insert 할 시 방법에 따라 처리 속도를 비교합니다.
### 비교 내용
1. JPA를 통한 단순 Insert를 사용하는 방법
2. JDBC Bulk Insert를 사용하는 방법
3. SimpleJdbcInsert를 사용하는 방법
4. Spring Data JDBC를 사용하는 방법

필요한 의존성
- Spring Data Web
- Spring Data JPA
- Spring Data JDBC
- MySQL
- Lombok
- Swagger

application.yml

```yaml
spring:
  application:
    name: DB_Insert

  datasource:
    url: jdbc:mysql://localhost:3306/db-insert-mysql?rewriteBatchedStatements=true
    username: test
    password: test
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
        jdbc:
          batch_size: 1000
```

Local 실행 조건

```
docker 설치
```

실행 방법
```
1. 터미널에서 docker-compose up을 통하여 db 컨테이너 실행
2. Application 실행
3. Swagger를 통하여 데이터 넣을 양을 정하고 데이터 생성
4. 처리 속도 비교
```

### 자세한 내용은 [블로그](https://velog.io/@tak980418/Spring-BulkInsert%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%84%B1%EB%8A%A5-%EC%B5%9C%EC%A0%81%ED%99%94)에서 확인하실 수 있습니다.
