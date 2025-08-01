# 스프링 부트 TDD

## API 목록

### 판매자 회원 가입

- 요청
  - 메서드: POST
  - 경로: /seller/signUp
  - 헤더
    ```
    Content-Type: application/json
    ```
  - 본문
    ```
    CreateSellerCommand {
        email: string,
        username: string,
        password: string
    }
    ``` 
- curl 명령 예시
    ```bash
    curl -i -X POST 'http://localhost:8080/seller/signUp' \
    -H 'Content-Type: application/json' \
    -d '{
      "email" :"seller1@example.com",
      "username": "seller1",
      "password": "seller1-password"
    }'
    ```


- 성공 응답
  - 상태코드: 204 No Content

- 정책
  - 이메일 주소는 유일해야 한다
  - 사용자 이름은 유일해야 한다
  - 사용자 이름은 3자 이상의 영문자, 숫자, 하이픈, 밑줄 문자로 구성되어야 한다
  - 비밀번호는 8자 이상의 문자로 구성되어야 한다

- 테스트
  - [x] 올바르게 요청하면 204 No Content 상태코드를 반환한다.
  - [x] email 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] email 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] username 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] username 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] username 속성이 올바른 형식을 따르면 204 No Content 상태코드를 반환한다
  - [x] password 속성이 지정되지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] password 속성이 올바른 형식을 따르지 않으면 400 Bad Request 상태코드를 반환한다
  - [x] email 속성에 이미 존재하는 이메일 주소가 지정되면 400 Bad Request 상태코드를 반환한다
  - [x] username 속성에 이미 존재하는 이메일 주소가 지정되면 400 Bad Request 상태코드를 반환한다
  - [x] 비밀번호를 올바르게 암호화한다
  - [ ] 접근 토큰은 JWT 형식을 따른다
  - [ ] 존재하지 않는 이메일 주소가 사용되면 400 Bad Request 상태코드를 반환한다
  - [ ] 잘못된 비밀번호가 사용되면 400 Bad Request 상태코드를 반환한다

### 판매자 접근 토큰 발행

요청

- 메서드: POST
- 경로: /seller/issueToken
- 본문
  ```
  IssueSellerToken {
    email: string,
    password: string
  }
  ```
- curl 명령 예시
  ```bash
  curl -i -X POST 'http://localhost:8080/seller/issueToken' \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "seller1@example.com",
    "password": "seller1-password"
  }'
  ```

성공 응답

- 상태코드: 200 OK

  본문
  ```
  AccessTokenCarrier {
    accessToken: string
  }
  ```

- 테스트
  - [x] 올바르게 요청하면 200 OK 상태코드를 반환한다
  - [x] 올바르게 요청하면 접근 토큰을 반환한다
  - [x] 접근 토큰은 JWT 형식을 따른다
  - [x] 존재하지 않는 이메일 주소가 사용되면 400 Bad Request 상태코드를 반환한다
  - [x] 잘못된 비밀번호가 사용되면 400 Bad Request 상태코드를 반환한다
