/*Script should be assigned to the .csv file upload page
 */

/**
 * @type WebSocket: the websocket for uploading files
 */
var csvFileWebSocket;
window.onload = initUploadFileSocket;
/**
 * Intanciates the reportWebSocket.
 * @returns {undefined}
 */
function initUploadFileSocket(){
    csvFileWebSocket = new WebSocket("ws://"+ document.location.host+"/CityGisWebApplication/fileUploadEndpoint");
    csvFileWebSocket.onmessage = onMessage;
    csvFileWebSocket.onerror = onError;
        
    
}

function onError(event){
    console.log("Error: " + event.data);
}

function sendLine(){
    csvFileWebSocket.send("start");
    var json = '{' +
            '"type":"Connections", '
            + '"line":"2015-03-10 00:47:24;357566040024266;Connection;1"'
            + '}';
    csvFileWebSocket.send(json);
    csvFileWebSocket.send("end");
}


function onMessage(event) {
   
    console.log(event.data);
    //TODO 
}


