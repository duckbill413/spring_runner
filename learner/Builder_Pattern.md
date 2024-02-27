# Builder Pattern
Builder 패턴은 Java 객체를 쉽게 생성할 수 있도록 도와준다.
Builder 패턴을 이용하면 모든 경우의 생성자를 만들어 놓은 것과 같다고 볼 수 있다.

> Builder 구현하기
> Builder에 대해 쉽게 이해해 보기 위해서 직접 구현해 보겠습니다.
>
> Builder interface
> ```java
> public interface StudentBuilder {
>   StudentBuilder name(String name);
>   StudentBuilder age(int grade);
>   Student build();
> }
> ```
>
> Builder interface 구현체
> ```java
> public class DefaultStudentBuilder implements StudentBuilder{
>   private String name;
>   private int grade;
>   @Override
>   public StudentBuilder name(String name) {
>   this.name = name;
>   return this;
>   }
> 
>     @Override
>     public StudentBuilder age(int grade) {
>         this.grade = grade;
>         return this;
>     }
> 
>     @Override
>     public Student build() {
>         // 기본 생성자가 반드시 필요한 것을 확인할 수 있다.
>         Student student = new Student();
>         student.setName(this.name);
>         student.setGrade(this.grade);
>         return student;
>     }
> }
> ```

### Lombok builder
lombok의 Builder을 이용하면 앞선 과정을 어노테이션(`@Builder`)을 이용해 간단히 할 수 있다.

클래스 상단에 `@Builder` 어노테이션 사용
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private String name;
    private int grade;
}

@Data
public class Animal {
  private String name;
  private int age;

  @Builder
  public Animal(String name, int age) {
    this.name = Objects.nonNull(name) ? name : "초코";
    this.age = age <= 0 ? 1 : age;
  }

  @Builder(builderMethodName = "fromAge", buildMethodName = "newAnimal")
  public Animal(int age) {
    this.name = "new!!!";
    this.age = age;
  }
}
```

Student 객체 생성
```java
Student.builder()
    .name("홍길동")
    .grade(1)
    .build();
```

생성자에 `@Builder` 어노테이션 사용 및 하나의 클래스에 여러 `@Builder`의 사용
- 생성자에 `@Builder`를 사용해서 필요한 인자에 대해서만 객체를 생성하거나 유효성 검증등의 비지니스 로직을 더할 수 있다.
  ```java
    @Builder
    public Animal(String name, int age) {
        this.name = Objects.nonNull(name) ? name : "홍길동";
        this.age = age <= 0 ? 1 : age;
    }
  ```

- 한 클래스에서 여러개의 Builder을 사용하기 위해서는 `builderMethodName`와 `buildMethodName`를 커스텀하여 사용할 수 있다.
  ```java
    @Builder(builderMethodName = "fromAge", buildMethodName = "newAnimal")
    public Animal(int age) {
        this.name = "new!!!";
        this.age = age; 
    }
  ```

- 객체 생성
  ```java
  @Test
  public void animalBuilderTest1() {
      Animal dog = Animal.builder()
              .name(null) // null이 입력되었을때 확인
              .age(3)
              .build();
      log.info(dog);
  }

  @Test
  public void animalBuilderTest2() {
      Animal newAnimal = Animal.fromAge() // name이 입력되지 않음을 볼 수 있음
              .age(5)
              .newAnimal();
      log.info(newAnimal); // builder Method의 이름이 다른 것 확인 가능
  }
  ```

### AccessLevel의 설정
- @Builder(access = AccessLevel.PRIVATE) 의 형태로 사용 가능

```java
public class Vehicle {
    private final String name;
    private final int number;
    
    @Builder(access = AccessLevel.PRIVATE)
    private Vehicle(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public static Vehicle getVehicle(String name, int number) {
        return Vehicle.builder()
                .name(name)
                .number(number)
                .build();
    }
}
```
