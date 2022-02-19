/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.config.LogEntityListener;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author XndySxnchez
 */
@Entity
@Table(name = "acl_login", schema = "auth")
public class AclLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "ip_user_session")
    private String ipUserSession;
    @Column(name = "mac_client")
    private String macClient;
    @Column(name = "fecha_do_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoLogin;
    @Column(name = "fecha_do_logout") 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoLogout;
    @Column(name = "user_session_name")
    private String userSessionName;
    @Column(name = "user_session_id")
    private BigInteger userSessionId;
    @Column(name = "evento")
    private String evento;

    public AclLogin() {
    }

    public AclLogin(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpUserSession() {
        return ipUserSession;
    }

    public void setIpUserSession(String ipUserSession) {
        this.ipUserSession = ipUserSession;
    }

    public String getMacClient() {
        return macClient;
    }

    public void setMacClient(String macClient) {
        this.macClient = macClient;
    }

    public Date getFechaDoLogin() {
        return fechaDoLogin;
    }

    public void setFechaDoLogin(Date fechaDoLogin) {
        this.fechaDoLogin = fechaDoLogin;
    }

    public Date getFechaDoLogout() {
        return fechaDoLogout;
    }

    public void setFechaDoLogout(Date fechaDoLogout) {
        this.fechaDoLogout = fechaDoLogout;
    }

    public String getUserSessionName() {
        return userSessionName;
    }

    public void setUserSessionName(String userSessionName) {
        this.userSessionName = userSessionName;
    }

    public BigInteger getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(BigInteger userSessionId) {
        this.userSessionId = userSessionId;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AclLogin)) {
            return false;
        }
        AclLogin other = (AclLogin) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "AclLogin{" + "id=" + id + ", ipUserSession=" + ipUserSession + ", macClient=" + macClient + ", fechaDoLogin=" + fechaDoLogin + ", fechaDoLogout=" + fechaDoLogout + ", userSessionName=" + userSessionName + ", userSessionId=" + userSessionId + ", evento=" + evento + '}';
    }

}
