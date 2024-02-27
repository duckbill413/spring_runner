# Enum class
서로 연관된 상수의 집합을 나타내기 위해서 사용된다.
추가적인 메서드나 필드를 가질 수 있습니다.  
enum의 인스턴스는 기본적으로 Thread Safe가 보장

```java
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}
```

```java
public enum Day {
    SUNDAY("일요일"), 
    MONDAY("월요일"), 
    TUESDAY("화요일"), 
    WEDNESDAY("수요일"), 
    THURSDAY("목요일"), 
    FRIDAY("금요일"), 
    SATURDAY("토요일");
    
    final String korean;
    
    Day(String korean) {
        this.korean = korean;
    }
}