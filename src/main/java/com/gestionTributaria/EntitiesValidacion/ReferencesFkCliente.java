/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "references_fk_cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReferencesFkCliente.findAll", query = "SELECT r FROM ReferencesFkCliente r"),
    @NamedQuery(name = "ReferencesFkCliente.findById", query = "SELECT r FROM ReferencesFkCliente r WHERE r.id = :id"),
    @NamedQuery(name = "ReferencesFkCliente.findByTableSchema", query = "SELECT r FROM ReferencesFkCliente r WHERE r.tableSchema = :tableSchema"),
    @NamedQuery(name = "ReferencesFkCliente.findByTableName", query = "SELECT r FROM ReferencesFkCliente r WHERE r.tableName = :tableName"),
    @NamedQuery(name = "ReferencesFkCliente.findByColumnOrigen", query = "SELECT r FROM ReferencesFkCliente r WHERE r.columnOrigen = :columnOrigen"),
    @NamedQuery(name = "ReferencesFkCliente.findByColumnCondictional", query = "SELECT r FROM ReferencesFkCliente r WHERE r.columnCondictional = :columnCondictional")})
public class ReferencesFkCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private BigInteger id;
    @Column(name = "table_schema")
    private String tableSchema;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "column_origen")
    private String columnOrigen;
    @Column(name = "column_condictional")
    private String columnCondictional;

    public ReferencesFkCliente() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnOrigen() {
        return columnOrigen;
    }

    public void setColumnOrigen(String columnOrigen) {
        this.columnOrigen = columnOrigen;
    }

    public String getColumnCondictional() {
        return columnCondictional;
    }

    public void setColumnCondictional(String columnCondictional) {
        this.columnCondictional = columnCondictional;
    }

}
