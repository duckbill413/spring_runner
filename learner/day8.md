# 커스텀 프로퍼티 정의
`@ConfigurationProperties`를 이용하여 커스텀 프로퍼티를 정의하는 방법을 알아보자  

`@Configuration` 애너테이션을 사용해 프로퍼티 정보를 담는 클래스를 만들어서 타입 안정성을 보장하고 유효성을 검증할 수 있다.  
이렇게 하면 `@Value` 애너테이션이나 `Environment` 객체를 사용하지 않고도 프로퍼티 값을 읽어서 사용할 수 있다.

의존 관계 추가
```java
implementation 'org.springframework.boot:spring-boot-configuration-processor'
```

커스텀 애플리케이션 프로퍼티
```
app.sbip.ct.name=CourseTracker
app.sbip.ct.ip=127.0.0.1
app.sbip.ct.port=9090
app.sbip.ct.security.enabled=true
app.sbip.ct.security.token=POISFosfnwf01fn12ionipaofinPNOEINFn1foIFNEPO125
app.sbip.ct.security.roles=USER,ADMIN
```

`AppProperties` 클래스

```java
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
// @ConstructorBinding => springboot 3.0.0 이후 클래스 생성자가 여러개인 경우를 제외하고 불필요
@ConstructorProperties("app.sbip.ct")
public class AppProperties {
    private final String name;
    private final String ip;
    private final int port;
    private final Security security;

    @Data
    private static class Security {
        private boolean enables;
        private final String token;
        private final List<String> roles;
    }
}
```

위의 클래스를 사용해서 프로퍼티를 읽는 다른 클래스 작성
`AppService` 클래스

```java

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppProperties appProperties;

    public AppProperties getAppProperties() {
        return this.appProperties;
    }
}
```
`AppService` 클래스에는 `@Service` 애너테이션이 붙어 있다. 이 애너테이션에 붙어 있는 클래슨느 서비스로 사용되며 스프링 부트가 탐색해서 빈으로 등록한다.  
`AppProperties` 객체를 주입 받는 부분에서 프로퍼티를 읽어서 유효성 검증을 수행하고 `AppProperties` 객체에 프로퍼티 값을 넣어준다.  

```java
@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class SpringLearnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(SpringLearnerApplication.class, args);
        
        AppService appService = applicationContext.getBean(AppService.class);
        log.info(appService.getAppProperties.toString());
    }
}
```
`@EnableConfigurationProperties(AppProperties.class)` 애너테이션을 확일 할 수 있다. 즉, Spring에서 프로퍼티를 자동으로 탐색해 주는 것이 아니라 직접 명시해 주어야 한다는 것이다.  

`@DefaultValue` 애너테이션을 사용하면 프로퍼티 기본값을 지정할 수 있다.  
ex) `@DefaultValue("8080") int port`