/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var loginWebsocket;
createLoginWebsocket();

function createLoginWebsocket(){
    loginWebsocket = 
            new WebSocket("ws://"+ document.location.host + document.location.pathname + "login");
    
    loginWebsocket.onmessage = onMessage;
    loginWebsocket.onclose = onClose;
    loginWebsocket.onopen = onOpen;
    
}

function onMessage(message){
    console.log(message);
}

function onClose(data){
    console.log(data);
    console.log("closed");
}

function onOpen(){
    
}

function logIn(){
    var json = {};
    json.type = "logIn";
    json.username = "test";
    json.password = "test";
    loginWebsocket.send(JSON.stringify(json));
}
