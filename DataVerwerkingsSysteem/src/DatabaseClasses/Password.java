/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Elize
 */
@Entity
@Table(name = "passwords")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Password.findAll", query = "SELECT p FROM Password p"),
    @NamedQuery(name = "Password.findByUserName", query = "SELECT p FROM Password p WHERE p.userName = :userName"),
    @NamedQuery(name = "Password.findByPassword", query = "SELECT p FROM Password p WHERE p.password = :password")})
public class Password implements Serializable, EntityClass{
    @Basic(optional = false)
    @Column(name = "password")
    private String password = null;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName = null;
    @JoinColumn(name = "user_name", referencedColumnName = "user_name", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private User user = null;

    public Password() {
    }

    public Password(String userName) {
        this.userName = userName;
    }

    public Password(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Password)) {
            return false;
        }
        Password other = (Password) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.Password[ userName=" + userName + " ]";
    }

    @Override
    public Object getPK() {
        return this.getUserName();
    }

    @Override
    public Car getCar() {
        return null;
    }

    @Override
    public EntityClass mergeWithObjectFromDatabase(EntityClass ec) {
        
        Password dbPassword = (Password) ec;
        
        if(this.password != null){
            if (dbPassword.password == null || !this.password.equals(dbPassword.password)) {
                dbPassword.password = this.password;   
            }
        }
        
        if(this.userName != null){
            if (dbPassword.userName == null || !this.userName.equals(dbPassword.userName)) {
                dbPassword.userName = this.userName;   
            }
        }
        
        if(this.user != null){
            if (dbPassword.user == null || !this.user.equals(dbPassword.user)) {   
                    dbPassword.user = this.user;
                }
        }

        return dbPassword;
    }

 
}
