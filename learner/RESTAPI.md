# REST API 설계 규칙

1. 슬래시 구분자(`/`)는 계층 관계를 나타내는데 사용한다.
    - ex) http://restapi.example.com/houses/apartments
2. 동사는 사용하지 않으며 명사를 대신 사용한다. 또한, 명사는 복수형을 사용한다.
    - ex) `running` -> `run`
    - ex) `book` -> `books`
3. URI 마지막 문자로 슬래시(`/`)를 포함하지 않는다.
    - URI에 포함되는 모든 글자는 리소스의 유일한 식별자로 사용되어야 하며 URI가 다르다는 것은 리소스가 다르다는 것이고, 역으로 리소스가 다르면 URI도 달라져야 한다.
    - REST API는 분명한 URI를 만들어 통신을 해야 하기 때문에 혼동을 주지 않도록 URI 경로의 마지막에는 슬래시(/)를 사용하지 않는다.ex) http://restapi.example.com/houses/apartments/ (X)
4. 하이픈(`-`)은 URI 가독성을 높이는데 사용
    - 불가피하게 긴 URI경로를 사용하게 된다면 하이픈을 사용해 가독성을 높인다.
5. 밑줄(`_`)은 URI에 사용하지 않는다.
    - 밑줄은 보기 어렵거나 밑줄 때문에 문자가 가려지기도 하므로 가독성을 위해 밑줄은 사용하지 않는다.
6. URI 경로에는 소문자가 적합하다.
    - URI 경로에 대문자 사용은 피하도록 한다.
    - RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정하기 때문
7. 파일확장자는 URI에 포함하지 않는다.
    - REST API에서는 메시지 바디 내용의 포맷을 나타내기 위한 파일 확장자를 URI 안에 포함시키지 않는다.
    - Accept header를 사용한다.ex) http://restapi.example.com/members/soccer/345/photo.jpg (X)
        - ex) GET / members/soccer/345/photo HTTP/1.1 Host: restapi.example.com Accept: image/jpg (O)
8. 리소스 간에는 연관 관계가 있는 경우 (계층형 구조를 지키자)
    - /리소스명/리소스 ID/관계가 있는 다른 리소스명
        - ex) GET : /users/{userid}/devices (일반적으로 소유 ‘has’의 관계를 표현할 때)
---
# RequestMapping (CRUD)
### POST (C)
- data 등록
- `@RequestBody`를 통해 body를 전달할 수 있다.
### GET (R)
- data 조회
- `@PathVaiable`, `@QueryParam`을 통해 Path Value 및 Parameter을 전달 받을 수 있다.
- body를 전달할 수 없다.
### PATCH (U)
- 일부 데이터 수정
### PUT (U)
- 전체 데이터 수정
### DELETE (D)
- 데이터 삭제

> REST-API 적용 예시  
> 도서 등록 (Post)
> - POST http://localhost:8080/books
>
> 전체 도서 조회 (GET)
> - GET http://localhost:8080/books
>
> 도서 번호를 이용한 조회 (GET)
> - GET http://localhost:8080/books/{book-isbn}
>
> 제목에 `코딩`을 포함한 도서의 조회 (GET)
> - GET http://localhost:8080/books?key=코딩
>
> 도서 번호에 해당하는 도서의 가격 변경 (PATCH)
> - PATCH http://localhost:8080/books/{book-isbn}
>
> 도서 번호에 해당하는 도서의 전체 데이터 변경 (PUT)
> - PUT http://localhost:8080/books/{book-isbn}
>
> 도서 번호에 해당하는 도서의 삭제 (DELETE)
> - DELETE http://localhost:8080/books/{book-isbn}

---

# Response
## ResponseEntity, BaseResponse, SuccessCode
API를 비지니스 로직에 알맞게 정의 하여 가독성과 생산성을 향상
```java
@GetMapping("/{id}")
public ResponseEntity<BaseResponse<MenuInfo>> findMenu(
        @PathVariable Long menuId
) {
    var menuDto = menuService.findMenu(menuId);
    return BaseResponse.success(
            SuccessCode.SELECT_SUCCESS,
            menuDto
    );
}
```

## BaseExceptionHandler, ErrorCode
비지니스 중 발생할 수 있는 오류들에 대하여 커스텀 Exception을 생성하고 ErrorCode를 통해 관리
```java
@Getter
public class BaseExceptionHandler extends RuntimeException {
private final ErrorCode errorCode;

    public BaseExceptionHandler(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseExceptionHandler(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
```

물론, 비니지스 규칙에 따라 유연하게 변경될 수 있음

- `BaseResponse`를 활용한 실제 배달의 민족 API
    - ResponseEntity, BaseResopnse가 활용된 것을 확인해볼 수 있다.
```json
{
  "status":"SUCCESS",
  "message":"성공",
  "serverDatetime":"2022-07-12 22:40:08",
  "data":
    {
      "code":"BAEMIN_DELIVERY_HOME",
      "title":"배달 홈",
      "titleIconUrl":"",
      "titleImageUrl":"",
      "bannerInventory":"",
      "displayCategories":
        [
          {
            "code":"BAEMIN_DELIVERY_HOME_ALL",
            "text":"전체",
            "hasCuration":false,
            "banner":false}
        ],
      "operationInfoAvailable":false,
      "sorts":
        {
          "title":"정렬",
          "options":
            [
              {
                "text":"배달팁 낮은 순",
                "selectedText":"배달팁 낮은 순",
                "code":"SORT__DELIVERYTIP",
                "hasShortCut":true,
                "initialSelected":false,
                "imageType":"URL",
                "imageUrl":"http://bm-cdn.baemin.com/shoplist/list_sort_filter/delivery_tip_icon@3x.png"
              },
              {
                "text":"기본순",
                "selectedText":"기본순",
                "code":"SORT__DEFAULT_RECOMMEND",
                "hasShortCut":true,
                "initialSelected":true,
                "imageType":"",
                "imageUrl":""
              }
            ]
        },
      "filters":
        [
          {
            "type":"MINIMUM_ORDER_PRICE",
            "title":"최소주문금액",
            "options":
              [
                {
                  "text":"전체",
                  "selectedText":"최소주문금액",
                  "code":"",
                  "initialSelected":true,
                  "imageType":"","imageUrl":""
                },
                {
                  "text":"5,000원 이하",
                  "selectedText":"최소주문 5,000원 이하",
                  "code":"MINIMUM_ORDER_PRICE__LOWER_THAN_5000",
                  "initialSelected":false,
                  "imageType":"",
                  "imageUrl":""
                }
              ]
          }
        ]
    }
}
```