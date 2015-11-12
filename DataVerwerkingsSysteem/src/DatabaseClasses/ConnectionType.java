/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entitiy class for the connections table in the database.
 * It should not be used by the user!
 * @author Elize
 */
@Entity
@Table(name = "connection_types", catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConnectionTypes.findAll", query = "SELECT c FROM ConnectionTypes c"),
    @NamedQuery(name = "ConnectionTypes.findByConnectionTypeId", query = "SELECT c FROM ConnectionTypes c WHERE c.connectionTypeId = :connectionTypeId"),
    @NamedQuery(name = "ConnectionTypes.findByConnectionName", query = "SELECT c FROM ConnectionTypes c WHERE c.connectionName = :connectionName")})
public class ConnectionType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "connection_type_id")
    private Integer connectionTypeId;
    @Column(name = "connection_name")
    private String connectionName;
    @OneToMany(mappedBy = "connectionTypeId")
    private Collection<CarPositionData> carPositionDataCollection;

    public ConnectionType() {
    }

    public ConnectionType(Integer connectionTypeId, String connectionName, Collection<CarPositionData> carPositionDataCollection) {
        this.connectionTypeId = connectionTypeId;
        this.connectionName = connectionName;
        this.carPositionDataCollection = carPositionDataCollection;
    }

    public ConnectionType(Integer connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public Integer getConnectionTypeId() {
        return connectionTypeId;
    }

    public void setConnectionTypeId(Integer connectionTypeId) {
        this.connectionTypeId = connectionTypeId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    @XmlTransient
    public Collection<CarPositionData> getCarPositionDataCollection() {
        return carPositionDataCollection;
    }

    public void setCarPositionDataCollection(Collection<CarPositionData> carPositionDataCollection) {
        this.carPositionDataCollection = carPositionDataCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (connectionTypeId != null ? connectionTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConnectionType)) {
            return false;
        }
        ConnectionType other = (ConnectionType) object;
        if ((this.connectionTypeId == null && other.connectionTypeId != null) || (this.connectionTypeId != null && !this.connectionTypeId.equals(other.connectionTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.ConnectionTypes[ connectionTypeId=" + connectionTypeId + " ]";
    }
    
}
