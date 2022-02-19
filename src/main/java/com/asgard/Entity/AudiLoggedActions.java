/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "audi_logged_actions", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AudiLoggedActions.findAll", query = "SELECT a FROM AudiLoggedActions a"),
    @NamedQuery(name = "AudiLoggedActions.findByEventId", query = "SELECT a FROM AudiLoggedActions a WHERE a.eventId = :eventId"),
    @NamedQuery(name = "AudiLoggedActions.findBySchemaName", query = "SELECT a FROM AudiLoggedActions a WHERE a.schemaName = :schemaName"),
    @NamedQuery(name = "AudiLoggedActions.findByTableName", query = "SELECT a FROM AudiLoggedActions a WHERE a.tableName = :tableName"),
    @NamedQuery(name = "AudiLoggedActions.findByRelid", query = "SELECT a FROM AudiLoggedActions a WHERE a.relid = :relid"),
    @NamedQuery(name = "AudiLoggedActions.findBySessionUserName", query = "SELECT a FROM AudiLoggedActions a WHERE a.sessionUserName = :sessionUserName"),
    @NamedQuery(name = "AudiLoggedActions.findByActionTstampTx", query = "SELECT a FROM AudiLoggedActions a WHERE a.actionTstampTx = :actionTstampTx"),
    @NamedQuery(name = "AudiLoggedActions.findByActionTstampStm", query = "SELECT a FROM AudiLoggedActions a WHERE a.actionTstampStm = :actionTstampStm"),
    @NamedQuery(name = "AudiLoggedActions.findByActionTstampClk", query = "SELECT a FROM AudiLoggedActions a WHERE a.actionTstampClk = :actionTstampClk"),
    @NamedQuery(name = "AudiLoggedActions.findByTransactionId", query = "SELECT a FROM AudiLoggedActions a WHERE a.transactionId = :transactionId"),
    @NamedQuery(name = "AudiLoggedActions.findByApplicationName", query = "SELECT a FROM AudiLoggedActions a WHERE a.applicationName = :applicationName"),
    @NamedQuery(name = "AudiLoggedActions.findByClientPort", query = "SELECT a FROM AudiLoggedActions a WHERE a.clientPort = :clientPort"),
    @NamedQuery(name = "AudiLoggedActions.findByClientQuery", query = "SELECT a FROM AudiLoggedActions a WHERE a.clientQuery = :clientQuery"),
    @NamedQuery(name = "AudiLoggedActions.findByAction", query = "SELECT a FROM AudiLoggedActions a WHERE a.action = :action"),
    @NamedQuery(name = "AudiLoggedActions.findByStatementOnly", query = "SELECT a FROM AudiLoggedActions a WHERE a.statementOnly = :statementOnly"),
    @NamedQuery(name = "AudiLoggedActions.findByAppUser", query = "SELECT a FROM AudiLoggedActions a WHERE a.appUser = :appUser")})
public class AudiLoggedActions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "event_id")
    private Long eventId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "schema_name")
    private String schemaName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "table_name")
    private String tableName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "relid")
    private long relid;
    @Size(max = 2147483647)
    @Column(name = "session_user_name")
    private String sessionUserName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_tstamp_tx")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionTstampTx;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_tstamp_stm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionTstampStm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "action_tstamp_clk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionTstampClk;
    @Column(name = "transaction_id")
    private BigInteger transactionId;
    @Size(max = 2147483647)
    @Column(name = "application_name")
    private String applicationName;
    @Lob
    @Column(name = "client_addr")
    private Object clientAddr;
    @Column(name = "client_port")
    private Integer clientPort;
    @Size(max = 2147483647)
    @Column(name = "client_query")
    private String clientQuery;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "action")
    private String action;
    @Lob
    @Column(name = "row_data")
    private Object rowData;
    @Lob
    @Column(name = "changed_fields")
    private Object changedFields;
    @Basic(optional = false)
    @NotNull
    @Column(name = "statement_only")
    private boolean statementOnly;
    @Size(max = 2147483647)
    @Column(name = "app_user")
    private String appUser;

    public AudiLoggedActions() {
    }

    public AudiLoggedActions(Long eventId) {
        this.eventId = eventId;
    }

    public AudiLoggedActions(Long eventId, String schemaName, String tableName, long relid, Date actionTstampTx, Date actionTstampStm, Date actionTstampClk, String action, boolean statementOnly) {
        this.eventId = eventId;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.relid = relid;
        this.actionTstampTx = actionTstampTx;
        this.actionTstampStm = actionTstampStm;
        this.actionTstampClk = actionTstampClk;
        this.action = action;
        this.statementOnly = statementOnly;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getRelid() {
        return relid;
    }

    public void setRelid(long relid) {
        this.relid = relid;
    }

    public String getSessionUserName() {
        return sessionUserName;
    }

    public void setSessionUserName(String sessionUserName) {
        this.sessionUserName = sessionUserName;
    }

    public Date getActionTstampTx() {
        return actionTstampTx;
    }

    public void setActionTstampTx(Date actionTstampTx) {
        this.actionTstampTx = actionTstampTx;
    }

    public Date getActionTstampStm() {
        return actionTstampStm;
    }

    public void setActionTstampStm(Date actionTstampStm) {
        this.actionTstampStm = actionTstampStm;
    }

    public Date getActionTstampClk() {
        return actionTstampClk;
    }

    public void setActionTstampClk(Date actionTstampClk) {
        this.actionTstampClk = actionTstampClk;
    }

    public BigInteger getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Object getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(Object clientAddr) {
        this.clientAddr = clientAddr;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getClientQuery() {
        return clientQuery;
    }

    public void setClientQuery(String clientQuery) {
        this.clientQuery = clientQuery;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getRowData() {
        return rowData;
    }

    public void setRowData(Object rowData) {
        this.rowData = rowData;
    }

    public Object getChangedFields() {
        return changedFields;
    }

    public void setChangedFields(Object changedFields) {
        this.changedFields = changedFields;
    }

    public boolean getStatementOnly() {
        return statementOnly;
    }

    public void setStatementOnly(boolean statementOnly) {
        this.statementOnly = statementOnly;
    }

    public String getAppUser() {
        return appUser;
    }

    public void setAppUser(String appUser) {
        this.appUser = appUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AudiLoggedActions)) {
            return false;
        }
        AudiLoggedActions other = (AudiLoggedActions) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AudiLoggedActions[ eventId=" + eventId + " ]";
    }
    
}
