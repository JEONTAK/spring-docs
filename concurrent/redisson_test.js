import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    scenarios: {
        coupon_issuance: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 200 },
                { duration: '30s', target: 400 },
                { duration: '30s', target: 600 },
                { duration: '30s', target: 800 },
                { duration: '30s', target: 0 },
            ],
            gracefulRampDown: '30s',
        },
    },
    thresholds: {
        'http_req_duration': ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
    },
};

export default function () {
    const url = 'http://localhost:8080/api/v1/issueCoupons/redisson';
    const payload = JSON.stringify({
        userId: randomIntBetween(1, 1000),
        couponId: 1,
    });
    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post(url, payload, params);
    check(res, {
        'is status 200': (r) => r.status === 200,
        'is status 400': (r) => r.status === 400,
        'is status 429': (r) => r.status === 429,
        'is status 500': (r) => r.status === 500,
    });
    sleep(0.1);
}