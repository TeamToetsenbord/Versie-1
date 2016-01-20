/*Script should be assigned to the .csv file upload page
 */

/**
 * @type WebSocket: the websocket for uploading files
 */
var csvFileWebSocket;
initUploadFileSocket();
/**
 * Intanciates the reportWebSocket.
 * @returns {undefined}
 */
function initUploadFileSocket(){
    csvFileWebSocket = new WebSocket("ws://"+ document.location.host+ document.location.pathname +"fileUploadEndpoint");
    csvFileWebSocket.onmessage = onMessage;
    csvFileWebSocket.onerror = onError;
        
    
}

function onError(event){
    console.log("Error: " + event.data);
}

//Call when a new file is uploaded.
function sendNewCSVFileMessage(path, CSVFileType){
    
    var json = {};
    json.messageType = "newCSVFile"
    json.CSVFileType = CSVFileType;
    json.path = path;
    csvFileWebSocket.send(json);
}


function onMessage(event) {
   
    console.log(event.data);
    //TODO return message to user
}


