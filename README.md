### Framework 의 시작
* 일반적인 Web Framework 를 흉내내보자
* 모든 요청을 받아주는 공통 Servlet 을 통해 각 업무 Action Class 로 흐름을 연결 해보자(해당 정보는 Properties 활용)
* 위 과정에서 Interface 를 활용해 본다.
* SQL Injection 공격을 회피하기 위한 공통 Filter 를 구현해보자(request parameter 를 조작? 해보자)


### 디렉토리 구조(배포 구조)
* /index.jsp
* /WEB-INF/web.xml
* /WEB-INF/classes/uriMapping.properties
* /WEB-INF/classes/filter/InjectionFilter.class
* /WEB-INF/classes/servlet/ActionServlet.class
* /WEB-INF/classes/action/Action.class
* /WEB-INF/classes/action/MainAction.class
* /WEB-INF/classes/action/DetailAction.class
* /WEB-INF/jsp/main.jsp
* /WEB-INF/jsp/detail.jsp
    
### /index.jsp 
* /main.do 로 Redirect 한다. 

### /WEB-INF/web.xml
* Servlet 정의
* Filter 정의

### uriMapping.properties
* /main.do:action.MainAction
* /detail.do:action.DetailAction

위와 같이 선언

### ActionServlet
* uriMapping.properties 정보를 바탕으로 호출된 URI 를 통해서 설정된 클래스의 execute() 함수 호출

### Action Interface
* 아래 두 클래스의 인터페이스를 생성
* MainAction
* DetailAction

```
public interface Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
```

### MainAction Class
* Action Interface 를 상속받아 execute 함수 구현
* /WEB-INF/jsp/main.jsp 페이지로 Forword

### DetailAction Class
* Action Interface 를 상속받아 execute 함수 구현
* /WEB-INF/jsp/detail.jsp 페이지로 Forword

### InjectionFilter Class
* SQL Injection 공격을 회피하기 위하여 넘어온 파라미터 중 특정 문자열은 자동 치환한다.
* 예)
  - /main.do?test=abc'efg 로 호출할 경우
  - request.getParameter("test") 로 출력할 경우 ' 문자열이 &apos; 문자열로 출력되도록 한다.
  - abc&apos;efg
  
### 기존 JSP 게시판 전환
* 위 방법으로 기존 JSP로 된 게시판을 만들어본다.