# ORM
- 객체와 관계형 데이터베이스의 데이터를 자동으로 매핑해주는 것을 말한다.
    - 객체 지향 프로그래밍은 클래스를 사용하고, 관계형 데이터베이스는 테이블을 사용
    - 객체 모델과 관계형 모델 간에 불일치가 존재
    - ORM을 통해 객체 간의 관계를 바탕으로 SQL을 자동으로 생성하여 불일치를 해결

### 장점
- 객체 지향적인 코드로 인해 더 직관적으고 비즈니스 로직에 더 집중할 수 있다.
- 재사용 및 유지보수의 편리성이 증가한다.
- DBMS에 대한 종속성이 줄어든다.
### 단점
- 완벽한 ORM으로만 서비스를 구현하기가 어렵다.
- 잘못 구현된 경우 속도 저하 및 일관성이 무너질 수 있다.

---
# JPA Part 1
[JPA 사용하기](https://velog.io/@duckbill/JPA-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0)
- 어플리케이션과 데이터베이스는 ORM이라는 개념이다.
- JPA는 자바 ORM의 표준 스택으로 인터페이스 형태로 제공하여 준다.
- JPA의 실제 구현 클래스를 모아 놓은 것이 Hibernate 그중 스프링에서 자주 사용되는 것을 모아놓은 것이 Spring data jpa이다.

## JPA 사용하기
- `dependency` 추가하기
> `implementation 'org.springframework.boot:spring-boot-starter-data-jpa'`

- `yaml` 수정하기
    ```yaml
    spring:
      jpa:
        show-sql: true # SQL 사용 로그 출력
        properties:
            hibernate:
        format_sql: true # SQL 로그 출력을 이쁘게
        generate-ddl: false 
        hibernate:
            ddl-auto: create # Spring 실행시 TABLE 생성 규칙
        defer-datasource-initialization: true # Spring 2.5 부터 resource의 SQL을 Spring 생성시 초기화 하지 않기 때문에 초기화 하기 위해서 `true`로 설정
    ```
  - `show-sql`을 `true`로 설정하면 속도가 느려질 수 있으나, JPA에서 사용하는 쿼리를 확인하기 쉽다.
    - `ddl-auto`는 실제 운영시에는 `none`, `validate` 옵션으로만 사용해야 한다.
      - create : 기존 테이블 삭제 후 다시 생성 (drop + create)
      - create-drop : create와 같으나 종료 시점에 테이블 drop
      - update : 변경분만 반영
      - validate : 엔티티와 테이블이 정상 매핑 되었는지만 확인
      - none : 사용하지 않음
      - 주의 사항
        - 운영 장비에서는 create, create-drop, update 를 사용해서는 않된다.
        - 개발 초기 : create, create-drop
        - 테스트 서버 : update, validate
        - 스테이징과 운영 서버 : validate, none
        - 로컬 환경을 제외하고는 직접 쿼리 명령을 하는 것이 좋다.
      

- JpaRepository 생성하기
Spring에서 JPA는 Entity를 생성하고 JpaRepository 인터페이스를 생성하는 것으로 사용할 수 있다.
    ```java
    public interface MemberRepository extends JpaRepository<Member, Long> {
    } 
    ```
    - `JpaRepository<T, ID>`: T는 Entity class, ID에는 Entity 클래스의 ID Type을 지정하면 된다.


## JPA Query Method
### Return Type
![](https://velog.velcdn.com/images/duckbill/post/566b2d68-e452-41eb-ae2c-6ea8ba611a33/image.png)

### Query Subject Keywords
![](https://velog.velcdn.com/images/duckbill/post/defe5920-1635-4082-ad56-52c56d629eb7/image.png)

### Query Predicate Keywords
![](https://velog.velcdn.com/images/duckbill/post/9c708efc-b19b-4a36-bb2d-341e484bc824/image.png)

### Query Predicate Modifier Keywords
![](https://velog.velcdn.com/images/duckbill/post/a64f7808-cc3a-4bd4-912c-7ba1b5b893f9/image.png)


## Entity 생성
### ID 생성 전략
AUTO 전략 / 기본키 매핑 정리
매핑 전략GenerationType.AUTO는 선택한 데이터베이스 방언에 따라 IDENTITY, SEQUENCE, TABLE 전략 중 하나를 자동으로 선택한다.

- SEQUENCE: 데이터베이스 시퀸스에서 식별자 값을 획득한 수 영속성 컨텍스트에 저장 (Oracle)
- IDENTITY: 데이터베이스에 엔티티를 저장해서 식별자 값을 획득한 후 영속성 컨텍스트에 저장한다. (IDENTITY 전략은 테이블에 데이터를 저장해야 식별자 값을 획득할 수 있다.) (MySQL)
- TABLE: 데이터베이스 시퀸스 생성용 테이블에서 식별자 값을 획득한 후 영속성 컨텍스트에 저장

```java
@GeneratedValue(strategy = GenerationType.UUID) // 사용 (springboot 3.0 & hibernate6 에서만 지원)
private UUID id; // uuid로 생성

@GeneratedValue(strategy = GenerationType.UUID)
private String id; // varchar로 생성

@Id
@GeneratedValue(generator = "uuid2")
@GenericGenerator(name = "uuid2", strategy = "uuid2")
private String id
```

### @Column
- name: 데이터베이스의 Column과 Object name을 별도로 매핑하기 위해서 사용
- nullable: default는 null 이지만 false로 두면 not null로 테이블을 생성하게 된다.
- unique: column 하나만 unique 키로 둘때 사용
- length: default=255 Varchar 등의 크기를 결정
- insertable / updatable: DDL 뿐만 아니라 DML에도 영향을 끼친다. 값이 false이면 insert / update 시에 반영되지 않는다.
- columnDefinition(DDL): 데이터베이스 컬럼 정보를 직접 줄 수 있다

### Enum type
Enum type을 컬럼으로 생성함으로써 에러를 방지할 수 있다.

- Entity에서 컬럼시에 Enum column을 생성하기 위해서는 `@Enumerated` 어노테이션을 이용할 수 있다.
- `@Enumerated` 어노테이션을 이용해서 컬럼생성시 `EnumType`은 `String`으로 해주는 것이 좋다.
  - `EnumType`을 `String`으로 하지 않았을때의 문제점
    - `EnumType`을 `String`으로 하지 않았을때 컬럼은 Enum 파라미터의 index 번호 ex)0, 1, 2...으로 생성된다.
    - 만약 중간에 Enum의 순서가 변경되거나 새로운 값이 추가되었을때 저장된 데이터베이스 값의 의미가 달라지는 일이 발생할 수 있다.
```java
@Enumerated(EnumType.STRING)
private Category category;
```

### Embedded
- 임베디드 타입은 새로운 값 타입을 직접 정의해서 사용한는 JPA의 방법이다.
- 임베디드 타입을 사용하여 더욱 객체지향적인 코드를 만들 수 있다.
- 코드의 재사용성을 향상 시킬 수 있다.

> Member Entity의 Address Embedded class
> ```java
> @Data
> @AllArgsConstructor
> @NoArgsConstructor
> @Embeddable
> public class Address {
>   private String city; // 시
>   private String district; // 구
>   @Column(name = "address_detail")
>   private String detail; // 상세주소
>   private String zipcode; // 우편번호
> }
> ```
> - `@Embeddable` 어노테이션을 붙인다.
> 
> Member Entity Column
> ```java
> @Embedded
> private Address address // @Embedded 어노테이션 사용
> ```

- Embedded 클래스의 재사용성을 높이기 위해서 `@AttributeOverride` 어노테이션을 사용할 수도 있다.
```java
@AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "district", column = @Column(name = "home_district")),
        @AttributeOverride(name = "detail", column = @Column(name = "home_address_detail")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip_code"))
})
@Embedded
private Address address;
```


### `@Transient`
해당 어노테이션을 붙인 Column은 영속성 처리에서 제외되기 때문에 DB에 반영되지 않는다.

```java
@Transient
private String column;
```

### Entity Listener
- `@PrePersist`: `Insert`메소드가 호출되기 전에 실행  
    ```java
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
    ```
    - `@CreatedDate`
    - `@LastModifiedDate`


### BaseEntity
`Entity`클래스가 상속 받을 `BaseEntity`클래스를 생성하여 공통된 컬럼에 대한 작업을 쉽게 처리할 수 있다.
- `@MappedSuperclass`: 해당 클래스의 필드를 상속받는 클래스의 필드로 추가  
- `EntityListeners(AuditingEntityListener.class)`: `Entity`의 변화를 `Listener`를 `BaseEntity`에 부착

### JPA 연관관계
### 1 : 1 단방향
```java
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    
    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;
}
@Getter
@Setter
@Entity
public class Locker {
    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;
    private String name;
}
```

### 1 : 1 양방향
```java
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;
}
@Getter
@Setter
@Entity
public class Locker {
    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long id;
    private String name;
    @OneToOne(mappedBy = "locker")
    private Member member;
}
```

### N : 1 단방향
![](https://velog.velcdn.com/images/duckbill/post/3a2c9c83-0d72-4130-8e23-4d753529a9ae/image.jpg)
```java
@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;
}
```
회원은 으로 팀 엔티티를 참조할 수 있지만 반대로 팀에는 회원을 참조하는 필드가 없다.  
Member.team 필드로 회원 테이블의 TEAM_ID 외래키를 관리한다.

### N : 1 양방향
![](https://velog.velcdn.com/images/duckbill/post/8a08b10e-ed12-410e-8bb4-0244274902c9/image.jpg)
```java
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public void setTeam(Team team) {
        this.team = team;

        // 무한 루프 방지
        if (!team.getMembers().contains(this)) {
            team.getMembers().add(this);
        }
    }
}
@Getter
@Setter
@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        this.members.add(member);
        if (member.getTeam() != this) { // 무한 루프 방지
            member.setTeam(this);
        }
    }
}
```

1:N 연관관계 단점
- 연관관계 관리를 위해 추가로 UPDATE SQL을 실행
- 연관 관계의 주인이 FK를 관리
- 본인 테이블에 FK가 있으면 엔티티의 저장과 연관관계 처리를 INSERT SQL 한 번으로 끝낼 수 있지만, 다른 테이블에 외래 키가 있으면 연관관계 처리를 위한 UPDATE SQL을 추가로 실행해야 한다.
위와 같은 이유로 잘 사용되지 않는다.

### N : N 양방향
`@ManyToMany`는 실무에서 사용되지 않는다. 대신 `1:N N:1`의 관계를 이용해서 N:N 관계를 구현한다.

```java
@Getter
@Setter
@Entity
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int orderAmount;
}
```

```java
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
@Getter
@Setter
@Entity
public class Product {
    @Id @Column(name = "product_id")
    private String id;
    private String name;

    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();
}
```