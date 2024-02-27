# Health Check
- 서버의 생존을 확인하는 것
- 서버를 여러개 사용하고 라운드로빈 방식을 사용해서 API를 컨트롤 한다면, Health Check를 이용해서 서버가 살아있는지 확인할 수 있다.
- AWS에서는 `로드밸런서`와 `대상 그룹`을 이용해서 여러 서버에 대한 Health Check를 진행한다.
  ![aws-health-check](assets/aws-health-check.png)
- Spring Actuator 라이브러리를 이용할 수도 있다.
```java
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

정상적으로 동작하는 스프링 부트 웹 애플리케이션의 `http://localhost:8080/actuator/health`에 접속하면 애플리케이션의 상태가 `UP`으로 표시된다.