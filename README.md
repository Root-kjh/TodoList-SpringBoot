# API Docs

SpringBoot을 이용한 ToDoList웹

* UserInfo
    * userId
    * userName
    * nickName
    * jwt

* Todo
    * title
    * context

### Auth

* Signup(/auth/signup)
    * Request(POST)
        * Json: userName, nickName, password
    * Response: Success/ Fail

* Signin(/auth/signin)
    * Request(POST)
        * Json: userName, password
    * Response: UserInfo

### User

* EditUserInfo(/user/{userId}):
    * Request(PUT)
        * Auth: jwt
        * Json: nickName
    * Response: UserInfo

* PasswordModify(/user/{userId}):
    * Request(PATCH)
        * Auth: jwt
        * Json: password
    * Response: Success/ Fail

* Withdraw(/user/{userId}):
    * Request(DELETE)
        * Auth: jwt
    * Response: Success/ Fail

### Todo

* GetTodoList(/todo)
    * Request(GET)
        * Auth: jwt
    * Response: TodoList

* InsertTodo(/todo)
    * Request(POST)
        * Auth: jwt
        * Json: title, context
    * Response: Success/ Fail

* UpdateTodo(/todo/{todoId})
    * Request(PUT)
        * Auth: jwt
        * Json: title, context
    * Response: Todo

* DeleteTodo(/todo/{todoId})
    * Request(DELETE)
        * Auth: jwt
    * Response: Success/ Fail