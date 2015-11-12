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
 * Entity class for the company database table.
 * Not used for CSV files.
 * Can be created by users with certain rights.
 * @author School
 */
@Entity
@Table(catalog = "CityGis Data", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companies.findAll", query = "SELECT c FROM Companies c"),
    @NamedQuery(name = "Companies.findByCompanyId", query = "SELECT c FROM Companies c WHERE c.companyId = :companyId"),
    @NamedQuery(name = "Companies.findByName", query = "SELECT c FROM Companies c WHERE c.name = :name")})
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "company_id")
    private Integer companyId;
    private String name;
    @OneToMany(mappedBy = "company")
    private Collection<User> usersCollection;

    public Company() {
    }

    public Company(Integer companyId, String name, Collection<User> usersCollection) {
        this.companyId = companyId;
        this.name = name;
        this.usersCollection = usersCollection;
    }

    public Company(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<User> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<User> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyId != null ? companyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.companyId == null && other.companyId != null) || (this.companyId != null && !this.companyId.equals(other.companyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.Companies[ companyId=" + companyId + " ]";
    }
    
}
