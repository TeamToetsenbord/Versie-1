/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Elize
 */
@Embeddable
public class CarPositionDataPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId;
    @Basic(optional = false)
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Basic(optional = false)
    @Column(name = "connection_type")
    private String connectionType;

    public CarPositionDataPK() {
    }

    public CarPositionDataPK(String unitId, Date eventDate, String connectionType) {
        this.unitId = unitId;
        this.eventDate = eventDate;
        this.connectionType = connectionType;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitId != null ? unitId.hashCode() : 0);
        hash += (eventDate != null ? eventDate.hashCode() : 0);
        hash += (connectionType != null ? connectionType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarPositionDataPK)) {
            return false;
        }
        CarPositionDataPK other = (CarPositionDataPK) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        if ((this.eventDate == null && other.eventDate != null) || (this.eventDate != null && !this.eventDate.equals(other.eventDate))) {
            return false;
        }
        if ((this.connectionType == null && other.connectionType != null) || (this.connectionType != null && !this.connectionType.equals(other.connectionType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.CarPositionDataPK[ unitId=" + unitId + ", eventDate=" + eventDate + ", connectionType=" + connectionType + " ]";
    }
    
}
