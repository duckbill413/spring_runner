# JPA Part 2

## 영속성 컨텍스트

![](https://velog.velcdn.com/images/duckbill/post/8403b438-0338-4a4a-bd35-f26e18b1d2bc/image.webp)

- 비영속 : 영속성 컨텍스트와 관계가 없는 새로운 상태 (DB에 저장되지 않는 상태)
    - Entity를 관리하고 있는 상태 Duty check, 조회 쓰기 지연 등을 지원
- 영속(Managed) : 엔티티 매니저를 통해 엔티티가 영속성 컨텍스트에 저장되어 관리되고 있는 상태
- 준영속(Detached) : 영속성 컨텍스틍에서 관리되다가 분리된 상태
- 삭제(Removed) : 영속성 컨텍스트에서 삭제된 상태

### 영속성 컨텍스트의 이점

- 1차 캐시 👀👀
    - 영속성 컨텍스트는 1차 캐시가 존재한다. 엔티티를 영속성 컨텍스트에 저장하는 순간 1차 캐시에 객체가 key, value 값으로 저장된다.
    - 엔티티 매니저가 조회를 할 때 먼저 영속성 컨텍스트에 있는 1차 캐시에서 해당 엔티티를 찾고 엔티티가 존재할 경우 DB에 접근 하지 않고 반환한다.
      > ```java
      > @Test
      > void findCache() {
      > // FEAT: Entity Cache 에 의하여 한 번만 select 문이 실행된다. 1차 캐시를 활용해 성능이 상승한다.
      >   System.out.println(memberRepository.findById(1L).get());
      >   System.out.println(memberRepository.findById(1L).get());
      >   System.out.println(memberRepository.findById(1L).get());
      > }
      > ```
      > - Entity Cache에 의하여 findById의 쿼리는 한번만 수행되게 된다.
      > - 쿼리 수행을 줄임으로써 성능을 향상 시킬 수 있다.
      > - 단, 키 값이 아닌 값에 대하여 조회시 entity cache를 유효하게 얻기 어렵다.

- 동일성 보장
    - 영속성 컨텍스트에서 꺼내온 객체는 동일성이 보장된다.
    - 1차 캐시로 반복 가능한 읽기 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공한다.

- 쓰기 지연 👍👍
    - 트랜잭션 내부에서 persist()가 일어날때, 엔티티를 1차 캐시에 저장하고, 쓰기 지연 SQL 저장소라는 곳에 INSERT QUERY를 생성해서 쌓아 놓는다. 이후 commit(), flush() 를
      할 때 쓰기지연된 SQL QUERY를 DB에 보낸다.

- 변경 감지 👍👍👍
    - JPA에서는 엔티티를 업데이트 할때 update(), persist() 와 같은 메소드로 영속성 컨텍스트에 알려주지 않아도 된다. 이것이 가능한 이유는 변경감지(Dirty Checking) 덕분이다.

- 플러시 👍👍
    - 플러시는 영속성 컨텍스트의 변경내용을 데이터베이스에 반영한다.
    - 트랜잭션 커밋 시점에서 플러시가 발생하는데 이때 쓰기 지연 저장소에 쌓여 있는 SQL문을 데이터베이스에 전송한다.
    - 영속성 캐시가 `flush`되는 시점
      > 1. `flush` 메소드를 명시적으로 호출한 시점
      > 2. 트랜잭션이 끝나고 `commit`되는 시점
      > 3. 복잡한 조회의 조건에서 JPA 쿼리가 실행되는 시점 (JPQL)

## 영속성 전이 (Cascade)

영속성 전이를 통해 하나의 Entity가 생성, 업데이트 될 떄 `하위 Entity`도 같이 `생성`, `업데이트` 해주는 방법

- PERSIST, REMOVE
    - 상위 엔티티가 영속 처리될 때 하위 엔티티들도 같이 영속 처리
- MERGE, REFRESH, DETACH
    - 상위 엔티티의 상태가 변경될 때 하위 엔티티들도 같이 상태 변경
- All
    - 상위 엔티티의 모든 상태 변경이 하위 엔티티에 적용

### In Spring 👍👍👍👍

- **CascadeType.ALL: 모든 Cascade를 적용**
- **CascadeType.PERSIST: 엔티티를 영속화할 때, 연관된 엔티티도 함께 유지**
- **CascadeType.MERGE: 엔티티 상태를 병합(Merge)할 때, 연관된 엔티티도 모두 병합**
- **CascadeType.REMOVE: 엔티티를 제거할 때, 연관된 엔티티도 모두 제거**
- **CascadeType.DETACH: 부모 엔티티를 detach() 수행하면, 연관 엔티티도 detach()상태가 되어 변경 사항 반영 X**
- **CascadeType.REFRESH: 상위 엔티티를 새로고침(Refresh)할 때, 연관된 엔티티도 모두 새로고침**

## Orphan Removal (고아 제거)

연관 관계가 없어진 `Entity`를 제거

### Cascade REMOVE vs Orphan Removal

- Cascade remove
    - setOrders(null)를 호출하면 관련 Order 엔티티가 DB에서 자동으로 제거되지 않습니다.
- Orphan Removal
    - setOrders(null)를 호출하면 관련 Order 엔티티가 DB에서 자동으로 제거 됩니다.

## 데이터 로딩 (LAZY, EAGER)

### EAGER

> @OneToMany(fetch = FetchType.EAGER)

부모 엔터티을 로딩하면 자식 엔터티도 같이 로딩

### LAZY

> @OneToMany(fetch = FetchType.LAZY)

- 부모 엔터티을 로딩하여도 자식 엔터티는 로딩되지 않음
- LAZY Fetch 타입은 실제 엔티티 조회시에 바로 가지고 오지 않고, 연관 관계에 있는 엔티티를 참조할때 가져오게 된다.
- 하지만, 동일한 트랜잭션 내에서 주인 객체를 로딩하는 방법 존재

## JPA N+1

JPA에서 `UPDATE`, `DELETE` 명령을 사용할 때 JPA는 DML 명령을 실행할 레코드가 실제로 존재하는지 확인하기 위해서 `SELECT`명령을 실행하게 된다.  
또 다른 케이스로는 LAZY 로딩을 사용할때 EAGER 방식에서는 join을 이용해서 한번에 데이터를 불러오는 반면 한번 더 쿼리를 사용하는 경우가 있다.  
물론, 에러를 방지할 수 있다는 측면에서는 좋으나, 불필요한 쿼리가 한번 더 실행되는 케이스가 있을 수 있다.

## JPQL

JPA는 ORM으로써 다양한 쿼리를 쉽고 빠르게 설계하여 개발자로 하여금 비지니스 로직에 좀 더 집중할 수 있도록하는 장점이 있다.  
하지만, 복잡한 쿼리를 작성하는데 있어서는 약한 모습을 보인다.  
이것을 극복하기 위해서 사용되는 것이 JPQL이다. JPQL을 사용하면 개발자가 직접 쿼리를 작성하여 쿼리를 실행할 수 있다.

JPQL은 `JpaRepository`의 메소드에 `@Query` 어노테이션을 붙여 사용할 수 있다.

- `nativeQuery` 옵션을 사용하면 JPQL 대신 일반적인 sql문을 사용할 수도 있다.

```java
@Query(value = """
        SELECT m
        FROM Menu m
        WHERE m.restaurant.id = :id
        ORDER BY m.name
        """,
        countQuery = """
                SELECT count(m)
                FROM Menu m
                WHERE m.restaurant.id = :id
                """)
List<Menu> findByRestaurantId(@Param("id") UUID id);
```

- `countQuery`: 페이지네이션을 도입한 경우 조회될 수 있는 전체 데이터의 개수를 예상하는 과정이 필요하다.

### JPQL mapping

```java
@Query(value = """
        SELECT new com.example.learner.domain.menu.dto.response.MenuDetailRes(
        m.id, m.name, m.price, m.stock)
        FROM Menu m
        WHERE m.id = :id
        """)
MenuDetailRes findByIdToMenuDetailRes(@Param("id") UUID id);
```

JDBC의 ROW_MAPPER와 같이 JPQL을 사용하여 DTO로 직접 매핑하는 방식도 가능하다.

### Fetch Query

- Join, Fetch Join 차이점
    - 일반 Join
        - 오직 JPQL에서 조회되는 주체가 되는 Entity만 조회하여 영속화
        - 조회의 주체가 되는 Entity만 Select 해서 영속화하기 때문에 데이터는 필요하지 않지만 연관 Entity가 검색 조건에는 필요한 경우에 사용
    - Fetch Join
        - 조회의 주체가 되는 Entity 이외에 Fetch Join이 걸린 연관 Entity도 함께 SELECT 하여 영속화
        - Fetch Join이 걸린 Entity를 모두 영속화하기 때문에 FetchType이 Lazy인 Entity를 참조하더라도 이미 영속성 컨텍스트에 들어가있어 N+1 문제 해결

- Fetch Join 사용법
  ```java
  @Query(value = "SELECT r FROM Restaurant r JOIN FETCH r.menus")
  List<Restaurant> findAllWithMenu();
  ```

## 총평

이론과 이러이러한 것들이 있다 위주로 이야기를 했지만 결국 스스로 실습을 진행해 보지 않는다면 의미가 없다.

---

## Software Delete

실제 운용환경에서 delete 방식은 위험성이 있으므로 자주 사용되는 방식은 아니다. 따라서, 소프트웨어적으로 delete 되었다로만 처리하는 경우가 많다.

따라서, DB에서는 값이 존재하지만 Logic 상에서는 값이 존재하지 않는 것으로 처리하는 것이다.

- 예제

```java

@Entity
@Where(clause = "deleted = false")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Long authorId;
    @ToString.Exclude
    private boolean deleted; // true : deleted, false : not deleted
}
```

1. Book entity에 대하여 deleted flag를 생성한다.
2. Book entity에 `@Where` Annotation을 붙이고 deleted=false 옵션을 사용하면 JPA 쿼리문에 항상 `where deleted = false` 문이 붙게 된다.