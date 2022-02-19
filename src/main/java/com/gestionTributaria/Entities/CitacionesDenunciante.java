/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author ORIGAMIEC
 */
@Entity
@Table(name = "reportecomisariacitacionesdenunciante", schema = Utils.SCHEMA_COMISARIA)
public class CitacionesDenunciante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "denunciante")
    private BigInteger denunciante;
    @Column(name = "cedula_denunciante")
    private String cedulaDenunciante;
    @Column(name = "nombre_completo_denunciante")
    private String nombreCompletoDenunciante;
    @Column(name = "comisaria")
    private BigInteger comisaria;
    @Column(name = "email")
    private String email;
    @Column(name = "descripcion")
    private String comisariaTexto;
    @Column(name = "usuario_crea")
    private String usuarioCreacion;

    public CitacionesDenunciante() {
    }

    public CitacionesDenunciante(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(BigInteger denunciante) {
        this.denunciante = denunciante;
    }

    public String getCedulaDenunciante() {
        return cedulaDenunciante;
    }

    public void setCedulaDenunciante(String cedulaDenunciante) {
        this.cedulaDenunciante = cedulaDenunciante;
    }

    public String getNombreCompletoDenunciante() {
        return nombreCompletoDenunciante;
    }

    public void setNombreCompletoDenunciante(String nombreCompletoDenunciante) {
        this.nombreCompletoDenunciante = nombreCompletoDenunciante;
    }

    public BigInteger getComisaria() {
        return comisaria;
    }

    public void setComisaria(BigInteger comisaria) {
        this.comisaria = comisaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComisariaTexto() {
        return comisariaTexto;
    }

    public void setComisariaTexto(String comisariaTexto) {
        this.comisariaTexto = comisariaTexto;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String toString() {
        return "id " + id;
    }

}
