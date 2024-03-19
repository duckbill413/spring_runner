# 트랜잭션 (Transaction)

## 트랜잭션 특징 Part 1 -> Day7

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

---