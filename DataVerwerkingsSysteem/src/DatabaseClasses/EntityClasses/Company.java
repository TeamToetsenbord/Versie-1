/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses.EntityClasses;

import DatabaseClasses.EntityClasses.EntityClass;
import DatabaseClasses.EntityClasses.User;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Elize
 */
@Entity
@Table(name = "companies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
    @NamedQuery(name = "Company.findByCompanyId", query = "SELECT c FROM Company c WHERE c.companyId = :companyId"),
    @NamedQuery(name = "Company.findByName", query = "SELECT c FROM Company c WHERE c.name = :name")})
public class Company implements Serializable, EntityClass {
    @Column(name = "name")
    private String name = null;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "company_id")
    private Integer companyId = null;
    @OneToMany(mappedBy = "company")
    private Collection<User> userCollection = null;

    public Company() {
    }

    public Company(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Company(String name, Integer companyId) {
        this.name = name;
        this.companyId = companyId;
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
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
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
        return "DatabaseClasses.Company[ companyId=" + companyId + " ]";
    }

    @Override
    public Object getPK() {
        return this.getCompanyId();
    }

    @Override
    public Car getCar() {
        return null;
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        
        Company dbCompany = (Company) ec;
        
        if(this.name != null){
            if (dbCompany.name == null || !this.name.equals(dbCompany.name)) {
                dbCompany.setName(this.name);
            }
        }
        
        if(this.companyId != null){
            if (dbCompany.companyId == null || !this.companyId.equals(dbCompany.companyId)) {
                dbCompany.setCompanyId(this.companyId);
            }
        }
        
        if(this.userCollection != null){
            if(dbCompany.userCollection == null){
                dbCompany.userCollection = dbCompany.userCollection;
            }else if (!this.userCollection.containsAll(dbCompany.userCollection)) {
                for(User user: this.userCollection){
                    if(!dbCompany.userCollection.contains(user)){
                        dbCompany.userCollection.add(user);
                    }
                }
            }
        }
        
        return dbCompany;

    }

}
