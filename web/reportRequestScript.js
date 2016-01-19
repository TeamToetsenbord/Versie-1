/*Script should be assigned to the report download page
 */

/**
 * @type WebSocket: the websocket for dowloading reports
 */
var reportWebSocket;
window.onload = initReportRequestSocket;
/**
 * Intanciates the reportWebSocket.
 * @returns {undefined}
 */
function initReportRequestSocket(){
    console.log("creating..");
    reportWebSocket = new WebSocket("ws://"+ document.location.host+"/CityGisWebApplication/reportEndpoint");
    reportWebSocket.onmessage = onMessage;
}

/**
 * @param {type} reportType: the type of report: 
 *                           Authority, 
 *                           CityGis Report, 
 *                           Connections or 
 *                           Authority report
 * @param {type} unitId: the unitId, if neccesery (only it het report is a control room report)
 * @returns {undefined}
 */
function sendReportRequest(reportType, unitId){
    if(unitId === undefined){
        unitId = null;
    }
    var jsonRequest = {};
    jsonRequest.reportType = reportType;
    jsonRequest.unitId = unitId; 
    reportWebSocket.send(jsonRequest);
} 
/**
 * This method should return the file URL
 * @param {type} event: the message that was send
 * @returns {undefined}
 */
function onMessage(event) {
    
    var jsonMessage = JSON.parse(event.data);
    if(jsonMessage.hasOwnProperty(error)){
        var error = jsonMessage.error;
    }else{
        var reportType = jsonMessage.reportType;
        var path = jsonMessage.path;
    }
    
    console.log(event.data);
    //TODO get file from server!
}