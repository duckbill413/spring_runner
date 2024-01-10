# JPA
## ORM

## Entity의 생성
@GeneratedValue(strategy = GenerationType.UUID) // 사용 (springboot 3.0 & hibernate6 에서만 지원)
private UUID id; // uuid로 생성

@GeneratedValue(strategy = GenerationType.UUID)
private String id; // varchar로 생성

@Id
@GeneratedValue(generator = "uuid2")
@GenericGenerator(name = "uuid2", strategy = "uuid2")
private String id

## JPA 연관 관계

## 