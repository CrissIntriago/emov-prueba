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
@Table(name = "reportecomisariacitacionesdenunciado", schema = Utils.SCHEMA_COMISARIA)
public class CitacionesDenunciado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "denunciado")
    private BigInteger denunciado;
    @Column(name = "cedula_denunciado")
    private String cedulaDenunciado;
    @Column(name = "nombre_compelto_dennunciado")
    private String nombreCompletoDenunciado;
    @Column(name = "comisaria")
    private BigInteger comisaria;
    @Column(name = "email")
    private String email;
    @Column(name = "descripcion")
    private String comisariaTexto;
    @Column(name = "usuario_crea")
    private String usuarioCreacion;

    public CitacionesDenunciado(Long id) {
        this.id = id;
    }

    public CitacionesDenunciado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(BigInteger denunciado) {
        this.denunciado = denunciado;
    }

    public String getCedulaDenunciado() {
        return cedulaDenunciado;
    }

    public void setCedulaDenunciado(String cedulaDenunciado) {
        this.cedulaDenunciado = cedulaDenunciado;
    }

    public String getNombreCompletoDenunciado() {
        return nombreCompletoDenunciado;
    }

    public void setNombreCompletoDenunciado(String nombreCompletoDenunciado) {
        this.nombreCompletoDenunciado = nombreCompletoDenunciado;
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

    public String toStirng() {
        return "id" + id;
    }

    

}
