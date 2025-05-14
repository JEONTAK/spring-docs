## DB Insert 비교

### 대량의 데이터를 Database에서 조회 할 시 방법에 따라 처리 속도를 비교합니다.
### 비교 내용
**유저**
1. 기본 유저(이메일) 조회
2. 인덱싱 활용 이메일 조회
3. 기본 유저(이름) 조회
4. 필요한 컬럼만 조회
5. Redis Cache
6. Caffeine Cache
7. Async
8. 페이징 조회
9. 기본 방식의 다건 조회
10. 배치 조회(IN)

**주문**
1. 기본 주문 조회
2. 조인 최적화 조회

필요한 의존성
- Spring Data JPA
- Spring Web
- Spring Cache
- Spring Data Redis
- Caffeine Cache
- MySQL
- Lombok
- Swagger
- Jackson DataType

application.yml

```yaml
spring:
  application:
    name: DB_Search
  #MySQL
  datasource:
    url: jdbc:mysql://localhost:3306/db-search-mysql?rewriteBatchedStatements=true
    username: test
    password: test
    driver-class-name: com.mysql.cj.jdbc.Driver

    hikari:
      maximum-pool-size: 100
      minimum-idle: 10

  #JPA
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

  #Redis
  data:
    redis:
      host: localhost
      port: 6379

  #Cache
  cache:
    type: redis
    cache-names:
      - redis-users
      - caffeine-users

  #Async
  task:
    execution:
      pool:
        max-size: 10
        core-size: 5

springdoc:
  swagger-ui:
    tags-sorter: alpha
```

Local 실행 조건

```
docker 설치
```

실행 방법
```
1. 터미널에서 docker-compose up을 통하여 db 컨테이너 실행
2. Application 실행
3. Swagger를 통하여 테스트
4. 처리 속도 비교
```

### 자세한 내용은 [블로그](https://velog.io/@tak980418/%EB%8C%80%EC%9A%A9%EB%9F%89-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A1%B0%ED%9A%8C-%EC%86%8D%EB%8F%84%EB%A5%BC-%EB%86%92%EC%9D%B4%EB%8A%94-%EC%B5%9C%EC%A0%81%ED%99%94-%ED%95%B4%EB%B3%B4%EA%B8%B0)에서 확인하실 수 있습니다.
