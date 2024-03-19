# 스프링 시큐리티
스프링 시큐리티 모듈은 스프링 애플리케이션에 초점을 맞춘 모듈이다.

> 스프링 시큐리티 기본 보안 기능
> - 애플리케이션 사용자 인증
> - 별도의 로그인 페이지가 없을 때 사용할 수 있는 기본적인 로그인 페이지
> - 폼 기반 로그인에 사용할 수 있는 기본 계정
> - 패스워드 암호화에 사용할 수 있는 여러 가지 인코더
> - 사용자 인증 성공 후 세션 ID를 교체해서 세션 고정 공격(session fixation attack) 방지
> - HTTP 응답 코드에 랜덤 문자열 토큰을 포함해서 사이트 간 요청 위조(cross-site request forgery. CSRF) 공격 방지
> - 자주 발생하는 보안 공격을 방어할 수 있는 여러 가지 HTTP 응답 헤더 제공
> > Cache-Control: no-cache, no-store, max-age=0, must-revalidate  
> > Pragma: no-cache  
> > Expires: 0  
> > X-Content-Type-Options: nosniff  
> > Strict-Transport-Security: max-age=31536000 ; includeSubDomains  
> > X-Frame-Options: DENY
> > X-XSS-Protection: 1; mode=block

> 스프링 시큐리티의 CSRF 방어
> ![](./assets/session-csrf.jpg)

- `Cache-Control`: 브라우저 캐시를 완전하게 비활성화
- `X-Content-Type-Options`: 브라우저의 콘텐츠 타입 추측을 비활성화하고 `Content-Type` 헤더로 지정된 콘텐츠 타입으로만 사용하도록 강제
~~~~~~~~~~~~~~~~~~~~~~~~~~~