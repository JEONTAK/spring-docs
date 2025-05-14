import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 50 },  // 50명
        { duration: '20s', target: 50 },  // 유지
        { duration: '10s', target: 100 }, // 100명
        { duration: '20s', target: 100 }, // 유지
        { duration: '10s', target: 0 },   // 종료
    ],
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)'],
    thresholds: {
        'http_req_duration{endpoint:email_default}': ['p(95)<1000'],
        'http_req_duration{endpoint:email_indexing}': ['p(95)<1000'],
        'http_req_duration{endpoint:name_default}': ['p(95)<1000'],
        'http_req_duration{endpoint:query_optimize}': ['p(95)<1000'],
        'http_req_duration{endpoint:paging}': ['p(95)<1000'],
        'http_req_duration{endpoint:user_list_default}': ['p(95)<1000'],
        'http_req_duration{endpoint:batch_in}': ['p(95)<1000'],
        'http_req_duration{endpoint:redis_cache}': ['p(95)<1000'],
        'http_req_duration{endpoint:caffeine_cache}': ['p(95)<1000'],
        'http_req_duration{endpoint:async}': ['p(95)<1000'],
    },
};

export default function () {
    // 엔드포인트별 요청 정의
    const endpoints = [
        {
            name: 'email_default',
            url: 'http://localhost:8080/users/email/default?email=user_000',
            checkName: 'email default',
        },
        {
            name: 'email_indexing',
            url: 'http://localhost:8080/users/email/indexing?email=user_000',
            checkName: 'email indexing',
        },
        {
            name: 'name_default',
            url: 'http://localhost:8080/users/name/default?name=Name_0',
            checkName: 'name default',
        },
        {
            name: 'query_optimize',
            url: 'http://localhost:8080/users/name/query?name=Name_0',
            checkName: 'query optimize',
        },
        {
            name: 'paging',
            url: 'http://localhost:8080/users/name/paging?page=0&size=10',
            checkName: 'paging',
        },
        {
            name: 'user_list_default',
            url: 'http://localhost:8080/users/name/usersList?name=Name_0,Name_1',
            checkName: 'user list default',
        },
        {
            name: 'batch_in',
            url: 'http://localhost:8080/users/name/batchWithIn?name=Name_0,Name_1',
            checkName: 'batch with IN',
        },
        {
            name: 'redis_cache',
            url: 'http://localhost:8080/users/name/rediscache?name=Name_0',
            checkName: 'redis cache',
        },
        {
            name: 'caffeine_cache',
            url: 'http://localhost:8080/users/name/caffeine?name=Name_0',
            checkName: 'caffeine cache',
        },
        {
            name: 'async',
            url: 'http://localhost:8080/users/name/async?name=Name_0',
            checkName: 'async',
        },
    ];

    // VU ID에 따라 하나의 엔드포인트 선택
    const vuId = __VU; // 현재 VU의 ID (0부터 시작)
    const endpointIndex = vuId % endpoints.length; // VU ID를 엔드포인트 수로 모듈러 연산
    const selectedEndpoint = endpoints[endpointIndex];

    // 선택된 엔드포인트 요청 실행
    let res = http.get(selectedEndpoint.url, {
        tags: { endpoint: selectedEndpoint.name },
    });
    check(res, {
        [`${selectedEndpoint.checkName} status is 200`]: (r) => r.status === 200,
        [`${selectedEndpoint.checkName} response time < 1000ms`]: (r) => r.timings.duration < 1000,
    });

    sleep(2); // 요청 간 2초 대기
}