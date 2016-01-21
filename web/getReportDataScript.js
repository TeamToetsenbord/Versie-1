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
 
        /**
         * Method for recieving messages.
         * Should be updated every minute.
         * @param {type} event: the message
         * @returns {undefined}
         */  
        ws.onmessage = function (event) {

        console.log('message: ', event.data);

        var jsonResponse;
        //try to get the json object from the server
        try {
            jsonResponse = angular.fromJson(event.data);
            // types: report, error, allUnitIs
            // report types: {Control_Room, Connections, Authority, CityGis, } 
            console.log("jsonMessage: ", jsonResponse );
        } catch (e) {
            console.log('error: ', e);

            jsonResponse = {'error': e};

            }
        };
        
        ws.onerror = function (event) {
            console.log('connection Error', event);
        };

        ws.onclose = function (event) {
            console.log('connection closed', event);
        };

        /**
         * Set the unit id of this socket to the user unit id
         * @returns {undefined}
         */
        ws.onopen = function () {
            console.log("opening");
            setUnitId();
        };

    })

function sendDataRequest(){
    
}

/**
 * Get all unit id's from the database.
 * @returns {undefined}
 */
function getUnitIdsFromDB(){
    var json = {};
    json.type = "getUnitIds";
    ws.send(JSON.stringify(json));
}

function setUnitId(){
     //Todo send current UnitId
            var json = {};
            json.unitId = "999";
            ws.send(JSON.stringify(json));
    
    }

