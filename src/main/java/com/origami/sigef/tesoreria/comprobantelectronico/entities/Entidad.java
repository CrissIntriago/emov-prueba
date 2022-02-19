/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.comprobantelectronico.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

/**
 *
 * @author gutya
 */
@Entity
@Table(schema = "comprobantes_electronicos", name = "entidad")
public class Entidad implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ruc_entidad")
    private String rucEntidad;
    @Column(name = "nombre_entidad")
    private String nombreEntidad;
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    private String establecimiento;
    @Where(clause = "estado = true")
    private Boolean estado;
    private String telefono;
    private String mail;
    private String direccion;
    @Column(name = "direccion_sucursal")
    private String direccionSucursal;
    private String logo;
    @Column(name = "contribuyente_especial")
    private String contribuyenteEspecial;
    private String contabilidad;
    @Column(name = "valor_max")
    private Integer valorMax;
    @Column(name = "valor_min")
    private Integer valorMin;

    public Entidad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRucEntidad() {
        return rucEntidad;
    }

    public void setRucEntidad(String rucEntidad) {
        this.rucEntidad = rucEntidad;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(String contabilidad) {
        this.contabilidad = contabilidad;
    }

    public Integer getValorMax() {
        return valorMax;
    }

    public void setValorMax(Integer valorMax) {
        this.valorMax = valorMax;
    }

    public Integer getValorMin() {
        return valorMin;
    }

    public void setValorMin(Integer valorMin) {
        this.valorMin = valorMin;
    }

    @Override
    public String toString() {
        return "Entidad{" + "id=" + id + ", rucEntidad=" + rucEntidad + ", nombreEntidad=" + nombreEntidad + ", nombreComercial=" + nombreComercial + ", establecimiento=" + establecimiento + ", estado=" + estado + ", telefono=" + telefono + ", mail=" + mail + ", direccion=" + direccion + ", direccionSucursal=" + direccionSucursal + ", logo=" + logo + ", contribuyenteEspecial=" + contribuyenteEspecial + ", contabilidad=" + contabilidad + ", valorMax=" + valorMax + ", valorMin=" + valorMin + '}';
    }

}
