# JPA

## ORM

## Entity 생성

### ID 생성 전략

### Enum type

`@CreatedDate`
`@LastModifiedDate`
`Column`

### `varchar`과 `text`에 대해서

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

### BaseEntity

`@MappedSuperclass`
`EntityListeners(AuditingEntityListener.class)`


---

## JPA

### JPA 연관관계

> @JoinColumn
>
>

- 1 : 1 단방향
- 1 : 1 양방향
- N : 1 단방향
- N : 1 양방향
- N : N 양방향