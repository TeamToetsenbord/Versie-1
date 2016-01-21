/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.websocket.Session;

/**
 *
 * @author Elize
 */
public class DataRecieverSession {
    private String unitID = null;
    private Session session = null;

    public DataRecieverSession(Session session){
        this.session = session;
    }
    
    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    
}
