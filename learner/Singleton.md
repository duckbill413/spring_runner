# Singleton Pattern
싱글톤 패턴은 하나의 클래스에 오직 하나의 인스턴스만 가지는 패턴  
인스턴스 생성에 많은 코스트가 드는 데이터베이스 연결 모듈에 많이 사용되며 인스턴스 생성을 효율적으로 할 수 있음  
의존성이 높아지고 TDD를 할 때 불편한 단점

### 1. 단순 메서드 호출
```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```
> 멀티스레드 환경에서 원자성이 결여됨.

### 2. synchronized
인스턴스를 만환하기 전까지 격리 공간에 놓기 위해서 `synchronized` 키워드로 잠금  
`getInstance()` 메서드를 호출할 때마다 lock이 걸려 성능저하 발생

```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 3. 정적 멤버
- 정적(static) 멤버나 블록은 런타임이 아니라 최초 JVM 클래스 로딩 때 모든 클래스를 로드할 때 미리 인스턴스를 생성하는데 이를 이용.
- 클래스 로딩과 동시에 `싱글톤 인스턴스` 생성
- 싱글톤 인스턴스가 필요 없는 경우도 무조건 싱글톤 클래스를 호출해 인스턴스를 만드는 단점.
- 싸피에서 1학기에 알려준 방식

```java
public class Singleton {
    private final static Singleton instance = new Singleton();
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        return instance;
    }
}
```

### 4. 정적 블록
정적(static) 블록을 이용해도 같다.
```java
public class Singleton {
    private static Singleton instance = null;
    
    static {
        instance = new Singleton();
    }
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        return instance;
    }
}
```

### 👍 5. 정적 멤버와 Lazy Holder (중첩 클래스)
singleInstanceHolder 라는 내부 클래스를 하나 더 만듬으로써, Singleton 클래스가 최초에 로딩 되더라도 함께 초기화가 되지 않고, `getInstance()` 가 호출될 때 singleInstanceHolder 클래스가 로딩되어 인스턴스를 생성

```java
public class Singleton {
    private static class singleInstanceHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return singleInstanceHolder.INSTANCE;
    }
}
```

### 6. 이중 확인 잠금(DCL)
인스턴스 생성 여부를 싱글톤 패턴 잠금 전에 한번, 객체를 생성하기 전에 한 번 총 2번 체크하여 인스턴스가 존재하지 않을 때만 잠금을 걸 수 있게 하여 앞선 문제 해결

```java
public class Singleton {
    private volatile Singleton instance;
    
    private Singleton() {}
    
    public Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
} 
```
> `volatile` : 스레드가 2개 이상 열리게 되면 각각의 캐시메모리로 부터 싱글톤 객체를 가져오게 되는데 volatile을 사용하면 캐시 메모리가 아닌 메인 메모리를 기반으로 싱글톤 객체를 가져오게 되므로 thread safe 하다.

### 👍 7. Enum
enum의 인스턴스는 기본적으로 Thread Safe가 보장

```java
public enum SingletonEnum {
    INSTANCE;
    public void oortCloud() {}
}
```

### 결론
- 정적 멤버와 Lazy Holder (5번)과 Enum (7번)을 추천
- 5번은 현재 가장 널리 사용되는 방식
- 7번은 이펙티브 자바의 저자(조슈아 블로크)의 추천 방식