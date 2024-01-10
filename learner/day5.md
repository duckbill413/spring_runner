# WebSocket
기존의 HTTP 연결은 stateless 한 성격을 지닌다.  
즉, 서버는 클라이언트가 누구인지 알지 못한다.  
HTTP 연결을 사용할 경우 클라이언트에서 서버로 요청을 보내고 응답을 받는 것은 가능하나, 클라이언트의 요청이 없음에도 서버에서 클라이언트로 데이터를 보내주는 것이 불가능 하였다.  
이것을 개선하고 클라이언트와 서버간의 지속적인 연결을 지원하기 위해서 사용되는 것이 `WebSocket`이다.  

## WebSocket 연결 과정
WebSocket이라고 해서 HTTP 연결과 크게 다르지 않다.  
1. HTTP 연결과 마찬가지로 3-way handshake 방식을 이용해서 서버와 연결을 수행한다.
2. HTTP의 경우 요청에 대한 응답이 완료가 되면 4-way handshake를 이용해서 연결을 종료하지만, 웹 소켓의 경우 연결을 종료하지 않고 기존의 HTTP 연결을 WebSocket 연결로 업그레이드 한다.
3. 연결된 커넥션을 통해서 메시지를 전송/수신 받는다.
4. 4-way handshake 방식을 이용해서 연결을 종료한다.

## Stomp
WebSocket 연결을 편리하게 도와주는 WebSocket 라이브러리를 활용하면 WebSocket을 편리하게 연결할 수 있다.  
여기에 더해서 Stomp 웹 라이브러리를 사용하면 다음과 같은 장점을 더 가질 수 있다.
1. `publisher`, `subscriber` 비지니스 로직 분리
    - `publisher`: 메시지를 전송하는 주체 (전송만 가능)
    - `subscriber`: 메시지를 수신받는 주체 (수신만 가능)
2. Header의 사용  
   기존의 WebSocket 웹 라이브러리의 단점은 Header의 전송이 불가능 하다는 점이다. Stomp를 사용하면 소켓 연결을 수행하면서도 Header의 전송이 가능해서 보다 다양한 정보를 Header에 담아 전송할 수 있다.

> ### Spring-Stomp 설정
> build.gradle 설정
> ```text
> // WebSocket
> implementation 'org.springframework.boot:spring-boot-starter-websocket'
> // sockjs
> // https://mvnrepository.com/artifact/org.webjars/sockjs-client
> implementation 'org.webjars:sockjs-client:1.5.1'
> // stomp
> implementation 'org.webjars:stomp-websocket:2.3.4'
> ```
> 
> Configuration
> ```java
> @Configuration
> @EnableWebSocketMessageBroker
> public class SocketConfig implements WebSocketMessageBrokerConfigurer {
>
>     // 웹 소켓 연결을 위한 엔드포인트 설정 및 stomp sub/pub 엔드포인트 설정
>     @Override
>     public void registerStompEndpoints(StompEndpointRegistry registry) {
>         // stomp 접속 주소 url => /ws-stomp
>         registry.addEndpoint("/ws-stomp")
>                 .setAllowedOriginPatterns("*")// 연결될 엔드포인트
>                 .withSockJS(); // SocketJS 를 연결한다는 설정
>     }
>
>     @Override
>     public void configureMessageBroker(MessageBrokerRegistry registry) {
>         // 메시지를 구독하는 요청 url => 즉 메시지 받을 때
>         registry.enableSimpleBroker("/sub");
>
>         // 메시지를 발행하는 요청 url => 즉 메시지 보낼 때
>         registry.setApplicationDestinationPrefixes("/pub");
>     }
> }
> ```
> - `context-path/ws-stomp` 로 들어온 HTTP 연결을 WebSocket 연결로 업그레이드
> - `ws://context-path/pub` 로 들어온 소켓 연결은 `publiser`로 설정하고
> - `ws://context-path/sub` 로 들어온 소켓 연결은 `subscriber`로 설정한 것을 확인할 수 있다.

> ### Vue-Stomp
> Stomp를 이용한 소켓 연결 수행
> 
> 소켓 연결
> ```js
> socket.value = new SockJS(`${serverURL}/ws-stomp`);
> stompClient.value = Stomp.over(socket.value);
> 
> stompClient.value.connect(
>   {}, // 헤더 정보
>   () => onConnected(props.chatRoom.roomId), // 연결 성공시 로직
>   () => console.log("Could not WebSocket server. Retry!"); // 연결 실패시 로직
> );
```
> 
> - 메시지 수신
> ```js
> const onConnected = (roomId) => {
>   stompClient.value.subscribe("/sub/chat/rooms/" + roomId, onMessageReceived);
> };
> ```
> 
> - 메시지 발신
> ```js
> const sendMessage = async (message) => {
>   let chatMessage = {
>       roomId: props.chatRoom.roomId,
>       type: "TALK",
>       sender: me.value.nickname,
>       senderId: me.value.id,
>       message: message.data.text,
>       time: dayjs().format("YY-MM-DD HH:mm"),
>   };
> 
>   stompClient.value.send("/pub/chat/message", {}, JSON.stringify(chatMessage));
> };
> ```


## DM 설계
### WebSocket
1. 방법1. 채팅방을 구독하는 방식
> 1. 유저 1과 2는 `/pub/{채팅방ID}`, `/sub/{채팅방ID}` 에 대해 연결을 수행
> 2. 발신자는 `/pub/{채팅방ID}`를 통해 메시지 전송
> 3. 수신자는 `/sub/{채팅방ID}`를 통해 메시지를 수신
  
2. 방법2. 나 자신을 구독하는 방식
> 유저 1, 2, 3이 그룹 채팅을 한다고 할때
> 1. 유저 1, 2, 3이 각자 소켓 연결을 진행
>    - 발신: `/pub/{채팅방ID}`
>    - 수신: `/sub/{채팅방ID}/{유저ID}`  
> 2. 유저가 메시지를 전송하면 `/pub/{채팅방ID}`로 메시지를 전송
> 3. 메시지를 전송 받은 Server은 채팅방ID를 이용하여 채팅방에 들어 있는 유저 리스트를 조회
> 4. 조회한 유저 ID중 메시지를 전송한 유저를 제외한 모든 유저에게 `/sub/{유저ID}`로 메시지 전송
> 5. `/sub/{채팅방ID}/{유저ID}`를 통해 나에게 온 메시지를 확인


### Database (Pull 방식과 동일)
1. 방법1. Pull 방식 (읽기 부하)
2. 방법2. Push 방식 (쓰기 부하)

---
## CAP 이론
- Consistency, 일관성 일관성은 사용자가 분산 데이터베이스 상의 어떤 노드와 통신하는지 상관없이 같은 데이터를 조회할 수 있는 것을 의미한다. 일관성은 금융이나 개인정보와 같이 모든 사용자가 일관성 있는 데이터를 조회해야할 때 중요하다.
- Availableity, 가용성 가용성은 모든 요청이 응답을 받을 수 있어야 한다는 것을 의미한다. 즉, 시스템이 중단되는 일 없이 언제든지 사용 가능한 상태여야 한다.
- Partition Tolerance, 분할 허용성 분할이란 노드 간 통신이 끊어지는 것을 의미한다. 분할 허용성은 시스템 내 분할이 생겼을 때 시스템이 계속 작동하는 것을 의미한다. 즉, 한 노드가 다른 노드와 통신할 수 없을 때, 다른 복제 노드가 사용자 요청에 응답할 수 있어야 한다.
CAP 이론이란

분산 데이터베이스 시스템은 분할이 생겼을 때 일관성과 가용성 중 하나를 희생하야 한다는 뜻이다. 분산 데이터베이스 시스템은 네트워크 장애나 다른 이슈들에 대처하기 위해서 분할 허용성이 반드시 필요하다. 이로인해 일관성과 가용성 중 하나를 포기해야 할 수 있다.
