# Cookie And Session
HTTP는 기본적으로 stateless 한 프로토콜이다. 즉, 서버는 클라이언트가 누구였는지 기억하지 않는다.
## Cookie
쿠키는 정보를 유지할 수 없는 Connectionless, Stateless의 성격을 가진 HTTP의 단점을 보완하기 위해서 도입된 개념
- `Expire`: 만료일을 가진다.
- `Domain`: 쿠키가 사용되는 도메인을 지정할 수 있다.
  - 쿠키가 사용되는 도메인이 결정되거나, 도메인을 따로 지정하지 않는 경우에 HTTP API 연결 요청시에 쿠키의 값은 자동으로 Header에 담겨져 전달 되게된다.

### Cookie의 종류
`Session Cookie`: 만료 시간(Expire date)를 설정하고 메모리에만 저장되고 브라우저 종료시 만료된다.
`Persistent Cookie` 장기간 유지되는 쿠키, 파일로 저장되어 브라우저 종료에 상관없이 저장된다.
`Secure Cookie`: HTTPS 연결에서만 사용되는 쿠키

### Cookie의 단점
- 쿠키에 대한 정보들을 매번 Header에 담아 전달하기 떄문에 트래픽 발생 및 보안 문제를 야기할 수 있다.

## Session
- HTTP Session id를 식별자로 구별하여 데이터를 사용자의 브라우저에 쿠키 형태가 아닌 접속한 서버 DB에 저장
- 메모리에 저장하기 때문에 브라우저가 종료되면 사라지게 된다.

### Connection Sequence
1. 클라이언트가 서버에 Resource를 요청
2. 서버에서 HTTP Request를 통해 쿠키에서 Session id를 확인한 후 없으면 Set-Cookie를 통해 새로 발행한 `Session-id`, 전송
3. 클라이언트는 HTTP Request 헤더에 Session id를 포함하여 원하는 Resource를 요청
4. 서버는 Session id를 통해 해당 세션을 찾아 클라이언트 상태 정보를 유지하며 응답