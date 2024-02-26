# REST API 설계 규칙

1. 슬래시 구분자(`/`)는 계층 관계를 나타내는데 사용한다. 
   - ex) http://restapi.example.com/houses/apartments
2. 동사는 사용하지 않으며 명사를 대신 사용한다. 또한, 명사는 복수형을 사용한다.
   - ex) `running` -> `run`
   - ex) `book` -> `books`
3. URI 마지막 문자로 슬래시(`/`)를 포함하지 않는다.
   - URI에 포함되는 모든 글자는 리소스의 유일한 식별자로 사용되어야 하며 URI가 다르다는 것은 리소스가 다르다는 것이고, 역으로 리소스가 다르면 URI도 달라져야 한다.
   - REST API는 분명한 URI를 만들어 통신을 해야 하기 때문에 혼동을 주지 않도록 URI 경로의 마지막에는 슬래시(/)를 사용하지 않는다.ex) http://restapi.example.com/houses/apartments/ (X)
4. 하이픈(`-`)은 URI 가독성을 높이는데 사용
   - 불가피하게 긴 URI경로를 사용하게 된다면 하이픈을 사용해 가독성을 높인다.
5. 밑줄(`_`)은 URI에 사용하지 않는다.
   - 밑줄은 보기 어렵거나 밑줄 때문에 문자가 가려지기도 하므로 가독성을 위해 밑줄은 사용하지 않는다.
6. URI 경로에는 소문자가 적합하다.
   - URI 경로에 대문자 사용은 피하도록 한다.
   - RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정하기 때문
7. 파일확장자는 URI에 포함하지 않는다.
   - REST API에서는 메시지 바디 내용의 포맷을 나타내기 위한 파일 확장자를 URI 안에 포함시키지 않는다.
   - Accept header를 사용한다.ex) http://restapi.example.com/members/soccer/345/photo.jpg (X)
     - ex) GET / members/soccer/345/photo HTTP/1.1 Host: restapi.example.com Accept: image/jpg (O)
8. 리소스 간에는 연관 관계가 있는 경우 (계층형 구조를 지키자)
   - /리소스명/리소스 ID/관계가 있는 다른 리소스명
     - ex) GET : /users/{userid}/devices (일반적으로 소유 ‘has’의 관계를 표현할 때)
---
# RequestMapping (CRUD)
### POST (C)
- data 등록
- `@RequestBody`를 통해 body를 전달할 수 있다.
### GET (R)
- data 조회
- `@PathVaiable`, `@QueryParam`을 통해 Path Value 및 Parameter을 전달 받을 수 있다.
- body를 전달할 수 없다.
### PATCH (U)
- 일부 데이터 수정
### PUT (U)
- 전체 데이터 수정
### DELETE (D)
- 데이터 삭제

> REST-API 적용 예시  
> 도서 등록 (Post)  
> - POST http://localhost:8080/books  
> 
> 전체 도서 조회 (GET)  
> - GET http://localhost:8080/books  
> 
> 도서 번호를 이용한 조회 (GET)  
> - GET http://localhost:8080/books/{book-isbn}  
> 
> 제목에 `코딩`을 포함한 도서의 조회 (GET)  
> - GET http://localhost:8080/books?key=코딩  
> 
> 도서 번호에 해당하는 도서의 가격 변경 (PATCH)
> - PATCH http://localhost:8080/books/{book-isbn}  
> 
> 도서 번호에 해당하는 도서의 전체 데이터 변경 (PUT)
> - PUT http://localhost:8080/books/{book-isbn} 
> 
> 도서 번호에 해당하는 도서의 삭제 (DELETE)
> - DELETE http://localhost:8080/books/{book-isbn} 

---

# Response
## ResponseEntity, BaseResponse, SuccessCode
API를 비지니스 로직에 알맞게 정의 하여 가독성과 생산성을 향상
```java
@GetMapping("/{id}")
public ResponseEntity<BaseResponse<MenuInfo>> findMenu(
        @PathVariable Long menuId
) {
    var menuDto = menuService.findMenu(menuId);
    return BaseResponse.success(
            SuccessCode.SELECT_SUCCESS,
            menuDto
    );
}
```

## BaseExceptionHandler, ErrorCode
비지니스 중 발생할 수 있는 오류들에 대하여 커스텀 Exception을 생성하고 ErrorCode를 통해 관리
```java
@Getter
public class BaseExceptionHandler extends RuntimeException {
private final ErrorCode errorCode;

    public BaseExceptionHandler(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseExceptionHandler(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
```

물론, 비니지스 규칙에 따라 유연하게 변경될 수 있음

- `BaseResponse`를 활용한 실제 배달의 민족 API
  - ResponseEntity, BaseResopnse가 활용된 것을 확인해볼 수 있다.
```json
{
  "status":"SUCCESS",
  "message":"성공",
  "serverDatetime":"2022-07-12 22:40:08",
  "data":
    {
      "code":"BAEMIN_DELIVERY_HOME",
      "title":"배달 홈",
      "titleIconUrl":"",
      "titleImageUrl":"",
      "bannerInventory":"",
      "displayCategories":
        [
          {
            "code":"BAEMIN_DELIVERY_HOME_ALL",
            "text":"전체",
            "hasCuration":false,
            "banner":false}
        ],
      "operationInfoAvailable":false,
      "sorts":
        {
          "title":"정렬",
          "options":
            [
              {
                "text":"배달팁 낮은 순",
                "selectedText":"배달팁 낮은 순",
                "code":"SORT__DELIVERYTIP",
                "hasShortCut":true,
                "initialSelected":false,
                "imageType":"URL",
                "imageUrl":"http://bm-cdn.baemin.com/shoplist/list_sort_filter/delivery_tip_icon@3x.png"
              },
              {
                "text":"기본순",
                "selectedText":"기본순",
                "code":"SORT__DEFAULT_RECOMMEND",
                "hasShortCut":true,
                "initialSelected":true,
                "imageType":"",
                "imageUrl":""
              }
            ]
        },
      "filters":
        [
          {
            "type":"MINIMUM_ORDER_PRICE",
            "title":"최소주문금액",
            "options":
              [
                {
                  "text":"전체",
                  "selectedText":"최소주문금액",
                  "code":"",
                  "initialSelected":true,
                  "imageType":"","imageUrl":""
                },
                {
                  "text":"5,000원 이하",
                  "selectedText":"최소주문 5,000원 이하",
                  "code":"MINIMUM_ORDER_PRICE__LOWER_THAN_5000",
                  "initialSelected":false,
                  "imageType":"",
                  "imageUrl":""
                }
              ]
          }
        ]
    }
}
```
---
# Lombok
```java
public class Person {
    private String name;
    private String age;
}
```
### @Getter, @Setter
```java
public class Person {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
```

```java
@Getter
@Setter
public class Person {
    private String name;
    private String age;
}
```

### @ToString
```java
public class Person {
    private String name;
    private String age;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
```

```java
@ToString
public class Person {
    private String name;
    private String age;
}
```

### @Data
```java
/**
 * @see Getter
 * @see Setter
 * @see RequiredArgsConstructor
 * @see ToString
 * @see EqualsAndHashCode
 * @see lombok.Value
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Data {
	String staticConstructor() default "";
}
```

### @AllArgsConstructor
```java
public class Person {
    private String name;
    private String age;

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }
}
```

```java
@AllArgsConstructor
public class Person {
    private String name;
    private String age;
}
```

### @NoArgsConstructor
```java
public class Person {
    private String name;
    private String age;

    public Person() {
    }
}
``` 

```java
@NoArgsConstructor
public class Person {
    private String name;
    private String age;

}
```

### @EqualsAndHashCode
```java
public class Person {
    private String name;
    private String age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### 
```java
@EqualsAndHashCode
public class Person {
    private String name;
    private String age;
}
```

---
# 생성자 할당, @Autowired, @RequiredArgsConstructor
스프링 개발을 하면서 주의 할 점은 계층적인 개발을 진행해야 한다는 점이다.
`Controller` -> `Service` -> `Repository`
                         ㄴ-> `Componet`

위의 형태로 주로 개발을 진행하게 될 것이다. 루프가 발생되지 않게 주의하자.
> ex) `Controller` -> `Service` -> `Repository` -> `Controller`


- 생성자 할당
    ```java
    @Log4j2
    @Service
    public class MenuServiceImpl implements MenuService {
        private final MenuRepository menuRepository;
        private final ModelMapper modelMapper;
    
        public MenuServiceImpl(MenuRepository menuRepository, ModelMapper modelMapper) {
            this.menuRepository = menuRepository;
            this.modelMapper = modelMapper;
        }
    }
    ```
- 👍 @RequiredArgsConstructor
    ```java
    @Log4j2
    @Service
    @RequiredArgsConstructor
    public class MenuServiceImpl implements MenuService {
        private final MenuRepository menuRepository;
        private final ModelMapper modelMapper;
    }
    ```
  - 생성자 할당 방식에 비해 가독성이 좋다.

- @Autowired
    ```java
    @Log4j2
    @Service
    public class MenuServiceImpl implements MenuService {
        @Autowired
        private MenuRepository menuRepository;
        @Autowired
        private ModelMapper modelMapper;
    }
    ```
  - `@Autowired`의 경우 `final`로 할당 받는 것이 아니다 보니 비지니스 로직내에서 개발자의 실수로 객체가 변경될 가능성이 존재

    
---
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

---
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