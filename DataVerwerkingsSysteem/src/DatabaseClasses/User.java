/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseClasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserName", query = "SELECT u FROM User u WHERE u.userName = :userName"),
    @NamedQuery(name = "User.findByEmailAddress", query = "SELECT u FROM User u WHERE u.emailAddress = :emailAddress"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName")})
public class User implements Serializable, EntityClass {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName = null;
    @Column(name = "email_address")
    private String emailAddress = null;
    @Column(name = "first_name")
    private String firstName = null;
    @Column(name = "last_name")
    private String lastName = null;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Password password = null;
    @JoinColumn(name = "company", referencedColumnName = "company_id")
    @ManyToOne
    private Company company = null;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DatabaseClasses.User[ userName=" + userName + " ]";
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
        
        User dbUser = (User) ec;
        
        if(this.userName != null){
            if (dbUser.userName == null || !this.userName.equals(dbUser.userName)) {
                dbUser.userName = this.userName;
            }
        }
        
        if(this.emailAddress != null){
            if (dbUser.emailAddress == null || !this.emailAddress.equals(dbUser.emailAddress)) {
                dbUser.emailAddress = this.emailAddress;
            }
        }
        
        if(this.firstName !=  null){
            if (dbUser.firstName == null || !this.firstName.equals(dbUser.firstName)) {
                dbUser.firstName = this.firstName;
            }
        }

        if(this.password !=  null ){
            if (dbUser.password == null || !this.password.equals(dbUser.password)) {
                dbUser.password = this.password;
            }
        }
        
        if(this.lastName !=  null){
            if (dbUser.lastName == null || !this.lastName.equals(dbUser.lastName)) {
                dbUser.lastName = this.lastName;
            }
        }
                        
        if(this.company != null){
            if (dbUser.company == null || !this.company.equals(dbUser.company)) {
                dbUser.company = this.company;
            }
        }

        return dbUser;
    }

}
