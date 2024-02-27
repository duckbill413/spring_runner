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

    