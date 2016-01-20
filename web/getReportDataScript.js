/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var app = angular.module("reportDataGetter", []);
var ws;

    app.controller("getData", function(){
        
        ws = new WebSocket("ws://" + document.location.host + 
          document.location.pathname + "reportdataendpoint");
 
        ws.onmessage = function (event) {

        console.log('message: ', event.data);

        var response;
        //try to get the json object from the server
        try {
            response = angular.fromJson(event.data);
            console.log("jsonMessage: ", response );
        } catch (e) {
            console.log('error: ', e);

            response = {'error': e};

            }
        };
        
        ws.onerror = function (event) {
            console.log('connection Error', event);
        };

        ws.onclose = function (event) {
            console.log('connection closed', event);
        };

        ws.onopen = function () {
            console.log('connection open');
            ws.send('HELLO SERVER');
        };

    })

function sendDataRequest(){
    
}
