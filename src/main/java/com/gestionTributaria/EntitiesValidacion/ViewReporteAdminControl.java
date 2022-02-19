/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "view_reporte_admin_control", schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewReporteAdminControl.findAll", query = "SELECT v FROM ViewReporteAdminControl v")})
public class ViewReporteAdminControl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Column(name = "id_user")
    private BigInteger idUser;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Size(max = 2147483647)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "registro_validados")
    private BigInteger registroValidados;
    @Column(name = "registros_asgnados")
    private Integer registrosAsgnados;

    public ViewReporteAdminControl() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getIdUser() {
        return idUser;
    }

    public void setIdUser(BigInteger idUser) {
        this.idUser = idUser;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getRegistroValidados() {
        return registroValidados;
    }

    public void setRegistroValidados(BigInteger registroValidados) {
        this.registroValidados = registroValidados;
    }

    public Integer getRegistrosAsgnados() {
        return registrosAsgnados;
    }

    public void setRegistrosAsgnados(Integer registrosAsgnados) {
        this.registrosAsgnados = registrosAsgnados;
    }

}
