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
        'http_req_duration{endpoint:price_default}': ['p(95)<1000'],
        'http_req_duration{endpoint:join_fetch}': ['p(95)<1000'],
    },
};

export default function () {
    const endpoints = [
        {
            name: 'price_default',
            url: 'http://localhost:8080/orders/price/default?price=99000',
            checkName: 'price default',
        },
        {
            name: 'join_fetch',
            url: 'http://localhost:8080/orders/price/joinfetch?price=99000',
            checkName: 'join fetch',
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