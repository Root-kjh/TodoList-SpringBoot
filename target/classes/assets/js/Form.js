function createForm(action, method="POST"){
    var newForm = document.createElement("form");

    newTodo.setAttribute("method",method);
    newTodo.setAttribute("action",action);
    return newForm;
}

function createTextInput(name,value="",placeholder=""){
    var newTextInput = document.createElement("input");
    newTextInput.setAttribute("type","text");
    newTextInput.setAttribute("name",name);
    newTextInput.setAttribute("value",value);
    newTextInput.setAttribute("placeholder",placeholder);

    return newTextInput;
}

function createTextarea(name,cols,rows){
    var newTextArea = document.createElement("textarea");
    newTextArea.setAttribute("name",name);
    newTextArea.setAttribute("cols",cols);
    newTextArea.setAttribute("rows",rows);

    return newTextArea;
}

function createSubmitButton(){
    var newSubmit = document.createElement("button");
    newSubmit.setAttribute("type","submit");

    return newSubmit;
}

export {createForm, createTextInput, createTextarea};