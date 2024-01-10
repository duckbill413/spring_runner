# 트랜잭션 (Transaction)

## 트랜잭션 특징

- 원자성(Atomicity)

  트랜잭션이 DB에 모두 반영되거나, 혹은 전혀 반영되지 않아야 한다.(All or nothing)
  
  > 트랜잭션 원자성 테스트
  > ```java
  > @Test // MEMO: Transaction 의 all or nothing 원자성 test
  > void transactionErrorTest(){
  >   try {
  >   bookService.putBookAndAuthorError();
  >   }
  >   catch (RuntimeException e){
  >   System.out.println(">>> " + e.getMessage());
  >   }
  >   System.out.println("books : " + bookRepository.findAll());
  >   System.out.println("authors : " + authorRepository.findAll());
  > }
  > ```
  > 만약 트랜잭션 수행 중 RuntimeError과 같은 에러가 발생한다면 트랜잭션은 rollback을 통해 트랜잭션 이전 상태로 되돌아간다.

- 일관성(Consistency)

  트랜잭션의 작업 처리 결과는 항상 일관성이 있어야 한다.

- 독립성(Isolation)

  하나의 트랜잭션이 실행되고 있을 때 다른 트랜잭션이 중간에 끼어들수 없도록 격리성을 가져야 한다.

- 지속성(Durability)

  트랜잭션이 성공적으로 수행되면 결과는 영구적으로 반영되어야 한다.

### Commit
하나의 트랜잭션이 성공적으로 끝났고, DB가 일관성 있는 상태일 때 이를 알려주기 위해 사용되는 연산

```java
@Transactional
public void putBookAndAuthor(){
    Book book = new Book();
    book.setName("JPA 시작하기");
    bookRepository.save(book);

    Author author = new Author();
    author.setName("martin");
    authorRepository.save(author);
}
```

다음과 같은 코드가 실행 되었을때 save 메서드가 실행 되더라도 즉시 실행되는 것이 아니라 메서드가 모두 종료되는 마지막에서 트랜잭션이 이루어지게 된다.  
즉, 중간에서 데이터를 조회해도 출력되지 않는다.

### Rollback
하나의 트랜잭션 처리가 비정상적으로 종료되어 트랜잭션의 원자성이 깨진 경우 rollback을 통해 처음 상태로 되돌아가는 것

### AOP를 이용한 트랜잭션 분리
트랜잭션 코드와 비지니스 로직 코드가 복잡하게 얽혀있는 코드가 있을때 트랜잭션 코드와 비지니스 로직 코드를 분리하기 위하여 해당 로직만을 클래스 밖으로 빼내서 별도의 모듈로 만드는 AOP를 고안 및 적용하게 되었다. 이를 Spring에서 `@Transactional`을 이용한다.
- Spring boot에서는 `@Transaction` 어노테이션을 이용해서 트랜잭션을 걸 수 있다.
  - 다되는 것은 아니고 RDBMS 방식의 DB를 사용할 때 걸 수 있다.
  - `@Transaction`은 두가지 방식을 지원한다. 하나는 일반적인 `@Transcation` 다른 하나는 `@Transactional(readOnly = true)` 이 있다.
    - `@Transaction`: 모든 경우 (CRUD) 활용할 수 있다.
    - `@Transactional(readOnly = true)`: READ에 대한 경우에만 활용이 가능하다. Spring에 해당 로직이 Read에 대해서만 트랜잭션을 발생시키면 된다는 것을 알려주기 때문에 `@Transaction`에 비해서 빠르다.


## 격리 수준 (Isolation)

동시에 여러 트랜잭션이 진행될 때 트랜잭션의 작업 결과를 여타 트랜잭션에게 어떻게 노출할 것인지를 결정한다.

### Isolation 5가지 수준

1. DEFAULT
   - 사용하는 데이터 엑세스 기술 또는 DB 드라이버의 디폴트 설정에 따름
   - 일반적으로 드라이버 격리 수준은 DB의 격리 수준을 따르며, 대부분의 DB는 READ_COMMITED를 기본 격리 수준으로 가짐
2. READ_UNCOMMITTED
   - 가장 낮은 격리수준으로써 하나의 트랜잭션이 커밋되기 전에 그 변화가 다른 트랜잭션에 그대로 노출되는 문제를 가진다.
   - 가장 빠른 속도를 가지므로 데이터의 일관성이 떨어지더라도 의도적으로 사용될 수 있다.
   - Dirty Read, Phantom Read, Non-Repeatable Read 문제
3. READ_COMMITTED
   - Spring은 기본 속성이 DEFAULT이며, DB는 일반적으로 READ_COMMITED가 기본 속성으로 사용된다.
   - 가장 많이 사용되는 속성이다.
   - READ_UNCOMMITED와 달리 다른 트랜잭션이 커밋하지 않은 정보는 읽을 수 없다.
   - 하나의 트랜잭션이 읽은 로우를 다른 트랜잭션이 수정할 수 있어서 처음 트랜잭션이 같은 로우를 다시 읽을 때 다른 내용이 발견딜 수 있음
   - Phantom Read, Non-Repeatable Read 문제
4. REPEATABLE_READ
   - 하나의 트랜잭션이 읽은 로우를 다른 트랜잭션이 수정할 수 없도록 막아주지만 새로운 로우를 추가하는 것은 막지 않는다.
   - 따라서 SELECT로 조건에 맞는 로우를 전부 가져오는 경우 트랜잭션이 끝나기 전에 추가된 로우를 발견할 수 없다.
   - Phantom Read 문제
5. SERIALIZABLE
   - 가장 강력한 트랜잭션 격리 수준으로, 이름 그대로 트랜잭션을 순차적으로 진행시켜준다.
   - SERIALIZABLE 은 여러 트랜잭션이 동시에 같은 테이블의 정보를 엑세스 할 수 없다.
   - SERIALIZABLE 은 가장 안전하지만 가장 성능이 떨어지므로 극단적으로 안전한 작업이 필요한 경우가 아니라면 사용해서는 안된다.


---

# JDBC Template
> 프로젝트 내 참조 클래스  
> `MenuJdbcRepository`

## 유용한 JDBC 활용
- `RowMapper` : RowMapper 인터페이스는 데이터베이스의 결과 행을 객체로 매핑하는 방법을 정의
  ```java
  RowMapper<Menu> MENU_ROW_MAPPER = (rs, rowNum) -> Menu.builder()
              .id(rs.getLong("id"))
              .name(rs.getString("name"))
              .price(rs.getLong("price"))
              .stock(rs.getLong("stock"))
              .build();
  ```
  
- `NamedParameterJdbcTemplate`
  JDBC를 사용하여 SQL 쿼리를 실행하는 데 사용되며, 이름 기반의 파라미터 바인딩을 지원  
  `?` 대신에 `:name`과 같은 형태로 파라미터를 바인딩할 수 있으며, 이를 통해 코드의 가독성을 높이고 오류 가능성을 줄일 수 있다.
  - `queryForObject()`: 단일 행을 반환하는 SQL 쿼리를 실행하는데 사용
    > namedParameterJdbcTemplate.queryForObject(sql, params, MENU_ROW_MAPPER);
  - `query()`: 복수 행을 반환하는 쿼리를 실행하는데 사용
    > namedParameterJdbcTemplate.query(sql, MENU_ROW_MAPPER)
  - `batchUpdate()`: insert 및 update를 batch 쿼리를 이용해서 동작 성능 향상을 노릴 수 있음
    > namedParameterJdbcTemplate.batchUpdate(sql, params)
    
    > non-batch  
    > `insert into Person (name, age) value ("김싸피", 21);`  
    > `insert into Person (name, age) value ("정싸피", 22);`  
    > `insert into Person (name, age) value ("홍싸피", 23);`  
    > ---
    > batch  
    > `insert into Person (name, age) values ("김싸피", 21), ("정싸피", 22), ("홍싸피", 23);`
- `SqlParamterSource`: SQL 쿼리 파라미터 값을 제공하는 방법을 정의 이름 기반의 파라미터를 사용하는 경우 `NamedParameterJdbcTemplate`와 같이 사용
  ```java
  SqlParameterSource[] params = menus
    .stream().map(BeanPropertySqlParameterSource::new)
    .toArray(SqlParameterSource[]::new);
  ```
- `BeanPropertySqlParameterSource`: `SqlParameterSource` 인터페이스를 구현한 것으로, Java Bean의 프로퍼티 값을 SQL 쿼리의 파라미터로 사용하는데 사용, `Bean`의 `getter`메소드를 통해 파라미터 값을 얻는다. 단, `getter`을 자주 사용하게 되므로 약간의 오버헤드가 발생할 수 있다.
- `SimpleJdbcInsert`: 데이터에 새로운 행을 삽입하는데 사용되는 JDBC 코드를 단순화 하기 위해 사용. 테이블 이름과 데이터 만으로 간단하게 `INSERT` 쿼리를 생성하고 실행
  ```java
  SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
    .withTableName("Menu")
    .usingGeneratedKeyColumns("id"); // @GeneratedValue(strategy = GenerationType.IDENTITY) Key
  ```
## INSERT와 Bulk INSERT
### INSERT
저장 한번에 하나의 쿼리가 발생
```java
private Menu insert(Menu menu) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
            .withTableName("Menu")
            .usingGeneratedKeyColumns("id"); // @GeneratedValue(strategy = GenerationType.IDENTITY) Key

    SqlParameterSource params = new BeanPropertySqlParameterSource(menu);
    var id = jdbcInsert.executeAndReturnKey(params).longValue();

    return Menu.builder()
            .id(id)
            .name(menu.getName())
            .price(menu.getPrice())
            .stock(menu.getStock())
            .build();
}
```

### Bulk INSERT
여러 건의 저장에도 쿼리는 한번만 발생
```java
@Override
public int saveAll(List<Menu> menus) {
    // JDBC의 bulk insert를 이용해 보겠다.

    var sql = """
            INSERT INTO Menu (name, price, stock)
            VALUES (:name, :price, :stock)
            """;

    SqlParameterSource[] params = menus
            .stream().map(BeanPropertySqlParameterSource::new)
            .toArray(SqlParameterSource[]::new);

    var result = namedParameterJdbcTemplate.batchUpdate(sql, params);
    return Arrays.stream(result).sum();
}
```

>시간 차이 분석 테스트 결과  
![jdbc_insert_test](assets/jdbc_insert_test.png)

---
# 개발에 유용한 라이브러리
## ModelMapper
- DTO에서 Entity로의 매핑을 편리하게 도와주는 라이브러리
- 일반적으로 객체를 생성하여 사용할 수도 있으나, 스프링의 `@Bean`을 이용하여 싱글톤 객체를 이용해서 사용하는 것이 권장
- 객체 매핑은 `type`과 `변수명`을 이용해서 매핑하며 `getter`, `setter`을 활용하는 방식을 취한다. 즉, `Record` 클래스에 대한 매핑은 어렵다.
- ModelMapper의 매핑은 AccessLevel을 이용해서 접근 레벨을 지정할 수 있다.
- `MatchingStrategy`를 활용해서 매핑 전략을 어느 정도로 할지 선택할 수 있다. `type`과 `name`을 엄격하게 검사하는 `STRICT` 권장

```java
@Bean
public ModelMapper getMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setMatchingStrategy(MatchingStrategies.STRICT);

    return modelMapper;
}
```
## Gson
- 구글에서 개발한 `json` 파싱을 도와주는 라이브러리
```java
Gson gson = new Gson();
```
을 이용해서 `gson` 객체를 생성하고 활용 가능하다.

## Jackson
- `ModelMapper`이 DTO에서 Entity로 매핑하는데 유용한 라이브러리 였다면, Jackson은 json에서 dto혹은 entity로 매핑하는데 도움을 주는 라이브러리이다. 반대도 가능하다.
- `ObjectMapper` 클래스를 이용해서 매핑을 진행하며, 변경할 `class`를 지정하여 매핑을 진행하게 된다.

## Springdoc
- 기존 Swagger API 페이지를 생성하기 위해서 많이 사용되던 라이브리러는 `springfox`이다.
- `springfox`는 개발이 진행되지 않은지 오래 되어, spring 3.x.x 버전에서는 원활하게 동작하지 않는다.
- `springdoc`은 계속해서 개발이 진행되어 최근 `springfox`의 점유율을 거의 따라잡았으며, Spring 3.x.x와 호환도 우수하다.
- `Springdoc`을 활용하며 `DTO`에 대한 정보 및 `Request`, `Response`에 대한 정보 확인이 가능
- 간단한 API 테스트도 `Springdoc`을 이용해 볼 수 있다. JWT Test도 지원한다.

`.yml` 파일과 Springdoc에 대한 `Config`를 이용해서 SpringDoc에 대한 설정을 진행할 수 있다.
```yaml
springdoc:
  api-docs:
    path: /api-docs
    groups:
      enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    groups-order: asc
    tags-sorter: alpha
    operations-sorter: alpha
    display-request-duration: true
    doc-expansion: none
  cache:
    disabled: true
  override-with-generic-response: false
  model-and-view-allowed: false
  default-consumes-media-type: application/json
  default-produces-media-type: application/json
  group-configs: # API 문서내에서 그룹 지정
    - group: all-api
      paths-to-match:
        - /** # 모든 경로에 대한 그룹
      paths-to-exclude:
        - /favicon.ico
        - /health
    - group: jwt-api
      paths-to-match:
        - /api/** # JWT를 사용한 경로에 대한 그룹
  show-actuator: true
```