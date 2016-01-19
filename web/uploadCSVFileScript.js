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

//Call when a new file is uploaded.
function sendNewCSVFileMessage(path, reportType){
    
    var json = {};
    json.messageType = "newCSVFile"
    json.reportType = reportType;
    json.path = path;
    console.log(json);
    //csvFileWebSocket.send(json);
    //csvFileWebSocket.send("end");
}


function onMessage(event) {
   
    console.log(event.data);
    //TODO 
}


