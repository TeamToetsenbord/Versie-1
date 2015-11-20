/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author School
 */
@Embeddable
public class HsdpaConnectionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "unit_id")
    private String unitId;
    @Basic(optional = false)
    @Column(name = "date_time_start_minute")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeStartMinute;
 

    public HsdpaConnectionPK() {
    }

    public HsdpaConnectionPK(String unitId, Date date) {
        this.unitId = unitId;
        this.dateTimeStartMinute = date;
        
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitId != null ? unitId.hashCode() : 0);
          return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HsdpaConnectionPK)) {
            return false;
        }
        HsdpaConnectionPK other = (HsdpaConnectionPK) object;
        if ((this.unitId == null && other.unitId != null) || (this.unitId != null && !this.unitId.equals(other.unitId))) {
            return false;
        }
        
        if ((this.dateTimeStartMinute == null && other.dateTimeStartMinute != null) || (this.dateTimeStartMinute != null && !this.dateTimeStartMinute.equals(other.dateTimeStartMinute))) {
            return false;
        }
        
    
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.HsdpaConnectionPK[ unitId=" + unitId + "]";
    }
    
}
