# Git Convention

참조) https://velog.io/@duckbill/Git-Flow
![](https://velog.velcdn.com/images/duckbill/post/50a3faac-c1d5-4d79-ab25-42d9e7b48856/image.png)

Git을 사용하여 개발하는 개발 환경에서 Branch 간의 문제 없이 배포까지 안정적으로 할 수 있도록 Branch를 관리하는 전략  
release가 자주 발생하는 프로젝트에 최적화  

## 브랜치 전략

1. `Main(= Master)`

   실제 운영 환경에 나가 있는 코드만 갖고 있는 브랜치

2. `Develop`

   메인 브랜치를 베이스로 생성한 브랜치. 다음 배포에 나갈 feature 을 머지하는 공간

3. `Feature`

   개발을 할 때 브랜치를 새로 만들고 해당 브랜치에 커밋을 진행한 뒤 작업이 종료되면 Develop 브랜치에 커밋하게 된다.
   - `feature/1-login-member` 멤버의 로그인 구현 브랜치

4. `Release`

   정말로 배포를 하는 단계에서 Develop 브랜치를 베이스로 Release 브랜치를 생성.

   Release 브랜치 생성후 부터는 Develop 브랜치에 작업을 하지 않고 Release 브랜치에 직접 작업을 진행.

   Release 브랜치 배포 이후 QA 및 개발 테스트가 완료된다면 Release 브랜치를 Main 브랜치에 머지

5. `Hotfix`

   의도치 않은 장애가 발생하였을때 메인 브랜치에서 브랜치를 생성한 이후 당장 수정이 필요한 최소한의 부분만 수정을 하여 메인 브랜치에 머지를 진행

## Commit Message 전략
- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `docs` : 문서 수정
- `style` : 코드 formatting, 세미콜론(;) 누락, 코드 변경이 없는 경우
- `refactor` : 코드 리팩터링
- `test` : 테스트 코드, 리팩터링 테스트 코드 추가(프로덕션 코드 변경 X)
- `chore` : 빌드 업무 수정, 패키지 매니저 수정(프로덕션 코드 변경 X)!HOTFIX : 급하게 치명적인 버그를 고쳐야 하는 경우

### Commit Message

1. 제목과 본문을 한 줄 띄워 분리하기
2. 제목은 영문 기준 50자 이내로
3. 제목 첫글자를 대문자로
4. 제목 끝에`.`금지
5. 제목은 `명령조`로
6. Github - 제목(이나 본문)에 이슈 번호 붙이기
7. 본문은 영문 기준 72자마다 줄 바꾸기
8. 본문은 `어떻게`보다 `무엇을`, `왜`에 맞춰 작성하기

## Issue Convention

- 제목은 `ISSUE` 성격에 맞게 자유롭게 합니다.
- 내용은 `ISSUE_TEMPLATE`에 맞춰서 작성합니다. 만약 템플릿과 작성해야할 이슈가 맞지 않는다면 원하는 형태로 변경합니다.
- 담당자(Assignees)를 명시 할 것
- Task list 기능을 적극 활용할 것
- 기능에 관련된 Issue라면 Github Project와 PR과 연동하여 진행상황을 공유할 것

## Pull Request Convention

- Issue와 연동하여 pull Request 생성!!!
- PR의 목적을 한문장으로 요약하기
- PR을 생성하게된 맥락이 있는데 이를 리뷰어가 알아야 한다면 함께 명시
- 피드백 받기를 원하는 시점을 명시
- 요청한 PR이 작업중이라면 리뷰어들이 알 수 있도록 '작업중' 혹은 'WIP(Work In Progress)' 라고 기재
- 원하는 피드백의 방향과 내용을 리뷰어가 알 수 있도록 명시
- 짧은 답변이라도 어조를 명확히 하기 위해 이모지 사용


---
# Spring package 구조

## 계층형 디렉터리 구조

```
com
 ㄴ example
     ㄴ duckbill
         ㄴ config
         ㄴ controller
         ㄴ domain
         ㄴ repository
         ㄴ service
         ㄴ security
         ㄴ exception
```

## 도메인형 디렉터리 구조

스프링의 웹 계층 보다 도메인에 주목하여 패키지를 구조화.  
각각의 도메인들은 서로를 의존하는 코드가 없도록 설계하는 것이 핵심.  
`JPA`와 `MSA`를 사용, 고려하는 경우 특히 유용할 수 있는 패키지 구조.  
물론, 비지니스 로직에 있어서 유연함은 필요하고 명확한 정답은 없다.

최상위 디렉터리로는 주로 `global`과 `domain`이 사용된다.

```
com
 ㄴ example
     ㄴ duckbill
         ㄴ domain
         |   ㄴ member
         |   |   ㄴ api
         |   |   ㄴ application
         |   |   ㄴ dao
         |   |   ㄴ domain
         |   |   ㄴ dto
         |   |   ㄴ exception
         |   ㄴ restaurant
         |   |   ㄴ api
         |   |   ㄴ application
         |   |   ㄴ dao
         |   |   ㄴ domain
         |   |   ㄴ dto
         |   |   ㄴ exception
         |   ...
         ㄴ global
         |   ㄴ auth
         |   ㄴ common
         |   ㄴ config
         |   ㄴ error
         |   ㄴ infra
         |   ㄴ util
         |   ...
         ㄴ application
         |   ㄴ api
         |   ㄴ usecase
```

## Global

특정 domain에 종속되지 않고, 프로젝트 전방위적으로 사용 가능한 클래스의 집합

- auth: `인증`, `인가`와 관련된 패키지
- common: `공통 클래스` 혹은 공통 `value`클래스로 구성
- config: 각종 `configuration` class
- error: `exception`, `error`와 관련된 클래스
- infra: `외부 모듈`, `api`등을 사용하는 클래스
- util: 공통 `util`성 클래스의 집합(ex: JWT, S3)

## Domain

domain entity 별로 패키지를 구성

- api: `controller` 클래스
- service: `service` 클래스 (비지니스 로직 담당)
- dao: `dao`, `repository` 클래스
- domain: `entity` 클래스
- dto: `dto` 클래스
    - `request` dto
    - `response` dto
- exception: `exception` 클래스

### Application

domain간 강한 결합을 방지하기 위해서 사용되는 패키지 구조

- api: `공통 controller` 클래스
- usecase: `여러 domain의 service를 사용하는 usecase` 클래스
    - ex) 어떤 메뉴를 가진 음식점 정보를 호출하는 경우
        - `GetRestaurantInMenuUsecase.java`

## Class와 Method의 명명
- `class`와 `method`는 해당 클래스 및 메소드의 이름만을 가지고 기능을 유추할 수 있도록 이름을 지어야 한다.
- 이름이 길어지는 것에 대해 무서워 하지 말자.
- `javadoc`을 활용해서 기능에 대해 설명해주면 더욱 best!!❤️



---
# JDK

우리가 활용할 만한 변경점 확인

## JDK 17

1. switch 문의 변경
2. record class
3. var type (java 10)
4. multiline String
5. String method
   - `isBlank`: 문자열이 비어 있거나 공백이면 `true`반환
   - `strip`: `strip()` 문자열 앞뒤 공백 제거
       - trim(): ASCII 범위 내의 공백 문자(공백, 탭, 개행 등)만을 제거합니다.
       - strip(): trim()과 달리 유니코드의 공백 문자(예: 중간 공백, 비틀린 공백)도 제거합니다. Java 11부터 도입되었습니다.
   - `stripLeading`: 문자열 앞의 공백 제거
   - `stripTrailing`: 문자열 뒤의 공백 제거
   - `repeat`: 문자열을 주어진 수 만큼 반복

### 새로운 JDK LTS 버전 JDK21

- 안타깝지만 lombok 지원 논란이 존재
- 덩달아서 JUnit도...
- https://github.com/projectlombok/lombok/issues/3393

---
# Record class
- DTO 클래스로 주로 사용

일반적인 DTO 클래스
```java
public class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

Record class로 변경된 DTO 클래스
```java
public record Person(String name, int age) {
    // 자동으로 생성된 생성자, 게터, equals(), hashCode(), toString() 메서드가 포함됨
}
```

```java
public record Person(String name, int age) {
    // 자동으로 생성된 생성자, 게터, equals(), hashCode(), toString() 메서드가 포함됨
    public Person {
        if (name == null && age == 0) {
            name = "duckbill";
            age = 29;
        }
        
        // Record 는 생성자의 끝에서 변수 할당이 발생한다.
        // this.name = name; // 생략됨
        // this.age = age; // 생략됨
    }
}
```
# Enum class
서로 연관된 상수의 집합을 나타내기 위해서 사용된다.
추가적인 메서드나 필드를 가질 수 있습니다.

```java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}
```

```java
public enum Day {
    SUNDAY("일요일"), 
    MONDAY("월요일"), 
    TUESDAY("화요일"), 
    WEDNESDAY("수요일"), 
    THURSDAY("목요일"), 
    FRIDAY("금요일"), 
    SATURDAY("토요일");
    
    final String korean;
    
    Day(String korean) {
        this.korean = korean;
    }
}
```

---
# Singleton Pattern
싱글톤 패턴은 하나의 클래스에 오직 하나의 인스턴스만 가지는 패턴  
인스턴스 생성에 많은 코스트가 드는 데이터베이스 연결 모듈에 많이 사용되며 인스턴스 생성을 효율적으로 할 수 있음  
의존성이 높아지고 TDD를 할 때 불편한 단점  

### 1. 단순 메서드 호출
```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```
> 멀티스레드 환경에서 원자성이 결여됨.

### 2. synchronized
인스턴스를 만환하기 전까지 격리 공간에 놓기 위해서 `synchronized` 키워드로 잠금  
`getInstance()` 메서드를 호출할 때마다 lock이 걸려 성능저하 발생  

```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 3. 정적 멤버
- 정적(static) 멤버나 블록은 런타임이 아니라 최초 JVM 클래스 로딩 때 모든 클래스를 로드할 때 미리 인스턴스를 생성하는데 이를 이용.  
- 클래스 로딩과 동시에 `싱글톤 인스턴스` 생성  
- 싱글톤 인스턴스가 필요 없는 경우도 무조건 싱글톤 클래스를 호출해 인스턴스를 만드는 단점.  
- 싸피에서 1학기에 알려준 방식  

```java
public class Singleton {
    private final static Singleton instance = new Singleton();
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        return instance;
    }
}
```

### 4. 정적 블록
정적(static) 블록을 이용해도 같다.
```java
public class Singleton {
    private static Singleton instance = null;
    
    static {
        instance = new Singleton();
    }
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        return instance;
    }
}
```

### 👍 5. 정적 멤버와 Lazy Holder (중첩 클래스)
singleInstanceHolder 라는 내부 클래스를 하나 더 만듬으로써, Singleton 클래스가 최초에 로딩 되더라도 함께 초기화가 되지 않고, `getInstance()` 가 호출될 때 singleInstanceHolder 클래스가 로딩되어 인스턴스를 생성

```java
public class Singleton {
    private static class singleInstanceHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return singleInstanceHolder.INSTANCE;
    }
}
```

### 6. 이중 확인 잠금(DCL)
인스턴스 생성 여부를 싱글톤 패턴 잠금 전에 한번, 객체를 생성하기 전에 한 번 총 2번 체크하여 인스턴스가 존재하지 않을 때만 잠금을 걸 수 있게 하여 앞선 문제 해결  

```java
public class Singleton {
    private volatile Singleton instance;
    
    private Singleton() {}
    
    public Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
} 
```
> `volatile` : 스레드가 2개 이상 열리게 되면 각각의 캐시메모리로 부터 싱글톤 객체를 가져오게 되는데 volatile을 사용하면 캐시 메모리가 아닌 메인 메모리를 기반으로 싱글톤 객체를 가져오게 되므로 thread safe 하다. 

### 👍 7. Enum
enum의 인스턴스는 기본적으로 Thread Safe가 보장

```java
public enum SingletonEnum {
    INSTANCE;
    public void oortCloud() {}
}
```

### 결론
- 정적 멤버와 Lazy Holder (5번)과 Enum (7번)을 추천
- 5번은 현재 가장 널리 사용되는 방식
- 7번은 이펙티브 자바의 저자(조슈아 블로크)의 추천 방식