/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Primary key class
 * @author School
 */
@Embeddable
public class CarStatusEventPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId;
    @Basic(optional = false)
    @Column(name = "event_date")
    private String eventDate;

    public CarStatusEventPK() {
    }

    public CarStatusEventPK(String unitId, String eventDate) {
        this.unitId = unitId;
        this.eventDate = eventDate;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitId != null ? unitId.hashCode() : 0);
        hash += (eventDate != null ? eventDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarStatusEventPK)) {
            return false;
        }
        CarStatusEventPK other = (CarStatusEventPK) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        if ((this.eventDate == null && other.eventDate != null) || (this.eventDate != null && !this.eventDate.equals(other.eventDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.CarStatusEventsPK[ unitId=" + unitId + ", eventDate=" + eventDate + " ]";
    }
    
}
