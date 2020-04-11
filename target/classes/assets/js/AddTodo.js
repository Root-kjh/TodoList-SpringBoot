import createForm from 'Form.js';

function AddTodo(){
    var newTodo = createForm("/todo/insert");
    var title = document.createElement("input");
    var context = document.createElement("textarea");

    newTodo.setAttribute("class","NewTodo");
    newTodo.style.background="yellow";
    newTodo.style.width="300px";
    newTodo.style.height="300px";
}