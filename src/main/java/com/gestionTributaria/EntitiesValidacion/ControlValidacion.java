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
 * @author DEVELOPER
 */
@Entity
@Table(name = "control_validacion",schema = Utils.SCHEMA_MIGRACION)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControlValidacion.findAll", query = "SELECT c FROM ControlValidacion c"),
    @NamedQuery(name = "ControlValidacion.findById", query = "SELECT c FROM ControlValidacion c WHERE c.id = :id"),
    @NamedQuery(name = "ControlValidacion.findByNombreCompleto", query = "SELECT c FROM ControlValidacion c WHERE c.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "ControlValidacion.findByIdUsuario", query = "SELECT c FROM ControlValidacion c WHERE c.idUsuario = :idUsuario"),
    @NamedQuery(name = "ControlValidacion.findByUsuario", query = "SELECT c FROM ControlValidacion c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "ControlValidacion.findByFecha", query = "SELECT c FROM ControlValidacion c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ControlValidacion.findByRegistrosConteo", query = "SELECT c FROM ControlValidacion c WHERE c.registrosConteo = :registrosConteo")})
public class ControlValidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Size(max = 2147483647)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Column(name = "id_usuario")
    private BigInteger idUsuario;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "registros_conteo")
    private BigInteger registrosConteo;

    public ControlValidacion() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getRegistrosConteo() {
        return registrosConteo;
    }

    public void setRegistrosConteo(BigInteger registrosConteo) {
        this.registrosConteo = registrosConteo;
    }
    
}
