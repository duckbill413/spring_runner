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
6. javax -> jakarta

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