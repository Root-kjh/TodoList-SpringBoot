# ToDOList 웹 제작

간단한 웹이라도, 간결하고 깨끗한 코드로 짜보기를 실천하기 위해 간단한 ToDoList 웹을 제작하기로 했다.

## 기능 나열

ToDoList웹을 제작하기 위해 먼저 해당 웹의 모든 기능을 나열한다.

1. 로그인
2. 회원가입
3. 로그아웃
4. 회원정보 수정
5. 회원탈퇴
6. 로그인된 회원의 TodoList 출력
7. Todo 추가
8. Todo 삭제
9. Todo 수정

SpringBoot를 이용해 해당 웹을 구현할 것이다.

## 데이터베이스 설계

    데이터베이스는 MySQL을 이용할것이다.

1. user Table
    * idx
    * nick_name
    * user_name
    * password(sha512 hash)

2. ToDo Table
    * idx
    * UserTableIdx(회원정보 Table -> idx)
    * Title
    * Context

## URL 설계

* / : 메인 페이지 출력

1. /User/
    * 로그인(signin/)
        * form : 로그인 폼 출력
        * process : 로그인 처리(POST : User->UserName, User->Password(sha512 hashed))
    * 회원가입(signup/)
        * form : 회원가입 폼 출력
        * process : 회원가입 처리(POST : User->UserName, User->Password(sha512 hashed), User->NickName)
    * 로그아웃(logout/) : 로그아웃 처리
    * 회원정보 수정(edit_user_info/)
        * form : 회원정보 수정 폼 출력
        * process :  회원정보 수정 처리(POST : User->UserName, User->Password(sha512 hashed), User->NickName)
    * 회원탈퇴(withdraw/)
        * form : 회원탈퇴 전 비밀번호 입력 폼 출력
        * process : 회원탈퇴 처리

2.  /Todo/
    * TodoList 출력(/) : ToDoList 출력
    * Todo 추가(/insert) : Tdoo 추가(POST : todo->title, todo->context)
    * Todo 삭제(/delete) : Todo 삭제(GET : todo->idx)
    * Todo 수정(/update) : Todo 수정(GET : todo->idx)
    (POST : todo->title, todo->context)

## 보안 설계
> 기능 구현 중 발생될 수 있는 취약점 나열, 보안방안 추가

> XSS, SQL Injection 관련 취약점 제외(Spring에서 자동 보안)

로그인 기능 계정 BruteForce 취약점 -> recaptcha 사용, 동일 ip의 5회 이상 로그인 시도 시 10분간 ip Block

1. 로그인
    * 계정 BruteForce 취약점 -> recaptcha 사용 or 동일 ip의 5회 이상 로그인 시도 시 10분간 ip Block
    * packet sniffing을 통한 user password 노출 취약점 -> SSL 암호화 사용 or pw를 front에서 hash 한채로 전달 

2. 회원가입
    * 중복 계정 회원가입 -> 회원가입 시 id 중복 체크
    * packet sniffing을 통한 user password 노출 취약점 -> SSL 암호화 사용 or pw를 front에서 hash 한채로 전달 

3. 로그아웃

4. 회원정보 수정

5. 회원탈퇴

6. 로그인된 회원의 ToDoList 출력

7. Todo 추가

8. Todo 삭제
    * ToDoList->idx 조작 -> 전달된 ToDoList->idx에 레코드의 UserIdx와 세션 id로 검색된 user->idx 비교

9. Todo 수정
    * ToDoList->idx 조작 -> 전달된 ToDoList->idx에 레코드의 UserIdx와 세션 id로 검색된 user->idx 비교

## 테스트 구현

### Repository
    
1. User Repository
    * select
    * insert
    * delete
    * update


2. ToDoList Repository
    * select
    * insert
    * delete
    * update

### Service

1. signin
2. signup
3. logout
4. userinfoUpdate
5. userinfoDelete
6. selectTodoList
7. insertTodo
8. deleteTodo
9. updateeTodo

### Controller

1. /User/
    * 로그인(signin/)
        * form : 로그인 폼 출력
        * process : 로그인 처리(POST : User->UserName, User->Password(sha512 hashed))
    * 회원가입(signup/)
        * form : 회원가입 폼 출력
        * process : 회원가입 처리(POST : User->UserName, User->Password(sha512 hashed), User->NickName)
    * 로그아웃(logout/) : 로그아웃 처리
    * 회원정보 수정(edit_user_info/)
        * form : 회원정보 수정 폼 출력
        * process :  회원정보 수정 처리(POST : User->UserName, User->Password(sha512 hashed), User->NickName)
    * 회원탈퇴(withdraw/)
        * form : 회원탈퇴 전 비밀번호 입력 폼 출력
        * process : 회원탈퇴 처리

2.  /Todo/
    * TodoList 출력(/) : ToDoList 출력
    * Todo 추가(/insert) : Tdoo 추가(POST : todo->title, todo->context)
    * Todo 삭제(/delete) : Todo 삭제(GET : todo->idx)
    * Todo 수정(/update) : Todo 수정(GET : todo->idx)
    (POST : todo->title, todo->context)

## view 설계

1. /User/
    * 로그인(signin/) -> Login(UserName, Password) Form, signup button 출력
    * 회원가입(signup/) -> Signup Form(UserName, NickName, Password, Password retry), signin button 출력
    * 회원정보 수정(edit_user_info/) -> (UserName, NickName, Password, Password retry) 폼 출력
    * 회원탈퇴(withdraw/)회원탈퇴 전 비밀번호 입력 폼(Password, Password retry) 출력

2.  /Todo/
    * TodoList 출력(/) : ToDoList(Title, Content), Todo Add button, Todo remove button 출력

        Todo add button click : New Todo 출력 -> Ctrl+S 저장

        Todo remove button click : 해당 Todo remove -> javascript로 todo view remove

        Todo update : 해당 Todo update
