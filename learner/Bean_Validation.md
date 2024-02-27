# Bean Validation

사용자가 입력한 데이터가 비지니스 로직에 적합한지 검증해야할 필요성이 있다.  
Bean Validation을 사용하면 간단한 애너테이션으로 쉽게 검증할 수 있다.

- 의존성 주입

> implementation 'org.springframework.boot:spring-boot-starter-validation'

## Hibernate Validation Annotation 종류

| Annotation                | 용도                                                                                |
|---------------------------|-----------------------------------------------------------------------------------|
| `@NotBlank`               | CharSequence 타입 필드에 사용되어 문자열이 null이 아니고, 앞뒤 공백 문자를 제거한 후 문자열 길이가 0보다 크다는 것을 검사한다. |
| `@NotEmpty`               | CharSequence, Collection, Map 타입과 배열에 사용되어 `null`이 아니고 비어 있지 않음을 검사한다.            |
| `@NotNull`                | 모든 타입에 사용할 수 있으며 `null`이 아님을 검사한다.                                                |
| `@Min(value=)`            | 최솟값을 지정해서 이 값보다 크거나 같은지 검사한다.                                                     |
| `@Max(value=)`            | 최댓값을 지정해서 이 값보다 작거나 같은지 검사한다.                                                     |
| `@Pattern(regex=, flags)` | regex로 지정한 정규 표현식을 준수하는지 검사한다. 정규 표현식의 `flag`도 사용 가능                              |
| `@Size(min=, max=)`       | 개수의 최솟값, 최댓값을 준수하는지 검사한다.                                                         |
| `@Email`                  | 문자열이 유요한 이메일 주소를 나타내는지 검사한다.                                                      |
|                           |                                                                                   |

## 커스텀 Bean Validation Annotation
비밀번호에 대하여 검증하는 어노테이션을 만들어 보겠습니다.  

비밀번호 검증에는 비밀번호 규칙을 강제하는 `Passay` 라이브러리도 같이 활용해 보겠습니다.  
> implementation 'org.passay:passay:1.6.0'

**Password Annotation**
```java
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordRuleValidator.class)
public @interface Password {
    String message() default "Password do not adhere to the specified rule";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

```
- `@Target` annotation은 `@Password` 애너테이션을 적용할 타입을 지정한다. 예제에서는 메서드와 필드에 적용할 수 있다.
- `@Retention` 애너테이션은 `@Password` 애너테이션이 언제까지 효력을 유지할 것인지 지정할 수 있다.
- `@Constraint` 애너테이션은 `@Password` 애너테이션이 빈 밸리데이션 제약 사항을 포함하는 애너테이션임을 의미하며, `validatedBy`속성을 사용해서 제약 사항이 구현된 클래스를 지정할 수 있다.
- `message()` 메서드는 유효성 검증에 실패할 때 표시해야 하는 문자열을 지정한다.
- `Class<?>[] groups()` 메서드를 사용해서 그룹을 지정하면 밸리데이션을 그룹별로 구분해서 적용할 수 있다.
- `Class<? extends PayLoad>[] payload()`는 밸리데이션 클라이언트가 사용하는 메타데이터를 전달하기 위해 사용된다.

**PasswordRuleValidator 클래스**
```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.ArrayList;
import java.util.List;

public class PasswordRuleValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_COMPLEX_RULES = 2;
    private static final int MAX_REPETITIVE_CHARS = 3;
    private static final int MIN_SPECIAL_CASE_CHARS = 1;
    private static final int MIN_UPPER_CASE_CHARS = 1;
    private static final int MIN_LOWER_CASE_CHARS = 1;
    private static final int MIN_DIGIT_CASE_CHARS = 1;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<Rule> passwordRules = new ArrayList<>();
        passwordRules.add(new LengthRule(8, 30));
        CharacterCharacteristicsRule characterCharacteristicsRule =
                new CharacterCharacteristicsRule(
                        MIN_COMPLEX_RULES,
                        new CharacterRule(EnglishCharacterData.Special, MIN_SPECIAL_CASE_CHARS),
                        new CharacterRule(EnglishCharacterData.UpperCase, MIN_UPPER_CASE_CHARS),
                        new CharacterRule(EnglishCharacterData.LowerCase, MIN_LOWER_CASE_CHARS),
                        new CharacterRule(EnglishCharacterData.Digit, MIN_DIGIT_CASE_CHARS)
                );
        passwordRules.add(characterCharacteristicsRule);
        passwordRules.add(new RepeatCharacterRegexRule(MAX_REPETITIVE_CHARS));

        PasswordValidator passwordValidator = new PasswordValidator(passwordRules);
        PasswordData passwordData = new PasswordData(password);
        RuleResult ruleResult = passwordValidator.validate(passwordData);
        return ruleResult.isValid();
    }
}

```
- `PasswordRuleValidator` 클래스는 ConstraintValidator 인터페이스를 구현하므로 `isValid`메서드를 구현해야하고, 이 메서드 안에 커스텀 비밀번호 유효성 검증 로직을 추가한다.
