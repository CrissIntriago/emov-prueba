/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_proteccion_alimentos", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppProteccionAlimentos.findAll", query = "SELECT a FROM AppProteccionAlimentos a"),
    @NamedQuery(name = "AppProteccionAlimentos.findById", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.id = :id"),
    @NamedQuery(name = "AppProteccionAlimentos.findByProteccion", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.proteccion = :proteccion"),
    @NamedQuery(name = "AppProteccionAlimentos.findByProteccionVitrina", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.proteccionVitrina = :proteccionVitrina"),
    @NamedQuery(name = "AppProteccionAlimentos.findByAlmacenamineto", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.almacenamineto = :almacenamineto"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformeGorra", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformeGorra = :uniformeGorra"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformeGuantes", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformeGuantes = :uniformeGuantes"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformesMascarilla", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformesMascarilla = :uniformesMascarilla"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformeCamiseta", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformeCamiseta = :uniformeCamiseta"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformeMandil", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformeMandil = :uniformeMandil"),
    @NamedQuery(name = "AppProteccionAlimentos.findByUniformeBotas", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.uniformeBotas = :uniformeBotas"),
    @NamedQuery(name = "AppProteccionAlimentos.findByRecipienteCuarentaBasuraVerde", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.recipienteCuarentaBasuraVerde = :recipienteCuarentaBasuraVerde"),
    @NamedQuery(name = "AppProteccionAlimentos.findByRecipienteCientoVeinteBasuraVerde", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.recipienteCientoVeinteBasuraVerde = :recipienteCientoVeinteBasuraVerde"),
    @NamedQuery(name = "AppProteccionAlimentos.findByRecipienteContenedor", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.recipienteContenedor = :recipienteContenedor"),
    @NamedQuery(name = "AppProteccionAlimentos.findByDesechosBiopeligrosos", query = "SELECT a FROM AppProteccionAlimentos a WHERE a.desechosBiopeligrosos = :desechosBiopeligrosos")})
public class AppProteccionAlimentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "proteccion")
    private Boolean proteccion;
    @Column(name = "proteccion_vitrina")
    private Boolean proteccionVitrina;
    @Column(name = "almacenamineto")
    private BigInteger almacenamineto;
    @Column(name = "uniforme_gorra")
    private Boolean uniformeGorra;
    @Column(name = "uniforme_guantes")
    private Boolean uniformeGuantes;
    @Column(name = "uniformes_mascarilla")
    private Boolean uniformesMascarilla;
    @Column(name = "uniforme_camiseta")
    private Boolean uniformeCamiseta;
    @Column(name = "uniforme_mandil")
    private Boolean uniformeMandil;
    @Column(name = "uniforme_botas")
    private Boolean uniformeBotas;
    @Column(name = "recipiente_cuarenta_basura_verde")
    private Boolean recipienteCuarentaBasuraVerde;
    @Column(name = "recipiente_ciento_veinte_basura_verde")
    private Boolean recipienteCientoVeinteBasuraVerde;
    @Column(name = "recipiente_contenedor")
    private Boolean recipienteContenedor;
    @Column(name = "desechos_biopeligrosos")
    private Boolean desechosBiopeligrosos;
    @JoinColumn(name = "servicio_sanitario", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosSanitarios servicioSanitario;
    @JoinColumn(name = "servicios_sanitario", referencedColumnName = "id")
    @ManyToOne
    private AppServiciosSanitarios serviciosSanitario;
    @OneToMany(mappedBy = "proteccionAlimento")
    private List<AppCtrlSanitarioMejoras> appCtrlSanitarioMejorasList;

    public AppProteccionAlimentos() {
    }

    public AppProteccionAlimentos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getProteccion() {
        return proteccion;
    }

    public void setProteccion(Boolean proteccion) {
        this.proteccion = proteccion;
    }

    public Boolean getProteccionVitrina() {
        return proteccionVitrina;
    }

    public void setProteccionVitrina(Boolean proteccionVitrina) {
        this.proteccionVitrina = proteccionVitrina;
    }

    public BigInteger getAlmacenamineto() {
        return almacenamineto;
    }

    public void setAlmacenamineto(BigInteger almacenamineto) {
        this.almacenamineto = almacenamineto;
    }

    public Boolean getUniformeGorra() {
        return uniformeGorra;
    }

    public void setUniformeGorra(Boolean uniformeGorra) {
        this.uniformeGorra = uniformeGorra;
    }

    public Boolean getUniformeGuantes() {
        return uniformeGuantes;
    }

    public void setUniformeGuantes(Boolean uniformeGuantes) {
        this.uniformeGuantes = uniformeGuantes;
    }

    public Boolean getUniformesMascarilla() {
        return uniformesMascarilla;
    }

    public void setUniformesMascarilla(Boolean uniformesMascarilla) {
        this.uniformesMascarilla = uniformesMascarilla;
    }

    public Boolean getUniformeCamiseta() {
        return uniformeCamiseta;
    }

    public void setUniformeCamiseta(Boolean uniformeCamiseta) {
        this.uniformeCamiseta = uniformeCamiseta;
    }

    public Boolean getUniformeMandil() {
        return uniformeMandil;
    }

    public void setUniformeMandil(Boolean uniformeMandil) {
        this.uniformeMandil = uniformeMandil;
    }

    public Boolean getUniformeBotas() {
        return uniformeBotas;
    }

    public void setUniformeBotas(Boolean uniformeBotas) {
        this.uniformeBotas = uniformeBotas;
    }

    public Boolean getRecipienteCuarentaBasuraVerde() {
        return recipienteCuarentaBasuraVerde;
    }

    public void setRecipienteCuarentaBasuraVerde(Boolean recipienteCuarentaBasuraVerde) {
        this.recipienteCuarentaBasuraVerde = recipienteCuarentaBasuraVerde;
    }

    public Boolean getRecipienteCientoVeinteBasuraVerde() {
        return recipienteCientoVeinteBasuraVerde;
    }

    public void setRecipienteCientoVeinteBasuraVerde(Boolean recipienteCientoVeinteBasuraVerde) {
        this.recipienteCientoVeinteBasuraVerde = recipienteCientoVeinteBasuraVerde;
    }

    public Boolean getRecipienteContenedor() {
        return recipienteContenedor;
    }

    public void setRecipienteContenedor(Boolean recipienteContenedor) {
        this.recipienteContenedor = recipienteContenedor;
    }

    public Boolean getDesechosBiopeligrosos() {
        return desechosBiopeligrosos;
    }

    public void setDesechosBiopeligrosos(Boolean desechosBiopeligrosos) {
        this.desechosBiopeligrosos = desechosBiopeligrosos;
    }

    public AppServiciosSanitarios getServicioSanitario() {
        return servicioSanitario;
    }

    public void setServicioSanitario(AppServiciosSanitarios servicioSanitario) {
        this.servicioSanitario = servicioSanitario;
    }

    public AppServiciosSanitarios getServiciosSanitario() {
        return serviciosSanitario;
    }

    public void setServiciosSanitario(AppServiciosSanitarios serviciosSanitario) {
        this.serviciosSanitario = serviciosSanitario;
    }

    
    public List<AppCtrlSanitarioMejoras> getAppCtrlSanitarioMejorasList() {
        return appCtrlSanitarioMejorasList;
    }

    public void setAppCtrlSanitarioMejorasList(List<AppCtrlSanitarioMejoras> appCtrlSanitarioMejorasList) {
        this.appCtrlSanitarioMejorasList = appCtrlSanitarioMejorasList;
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
        if (!(object instanceof AppProteccionAlimentos)) {
            return false;
        }
        AppProteccionAlimentos other = (AppProteccionAlimentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppProteccionAlimentos[ id=" + id + " ]";
    }
    
}
