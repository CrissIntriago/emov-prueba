/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.entities;

import com.origami.sigef.common.entities.TipoTramiteRequisito;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

/**
 *
 * @author gutya
 */
@Entity
@Table(name = "tipo_tramite", schema = "procesos")
@NamedQueries({
    @NamedQuery(name = "TipoTramite.unidadAdministrativa", query = "SELECT tt FROM TipoTramite tt WHERE tt.unidadAdministrativa = ?1 ORDER BY tt.descripcion ASC"),
    @NamedQuery(name = "TipoTramite.abreviatura", query = "SELECT tt FROM TipoTramite tt WHERE tt.abreviatura = ?1"),
    @NamedQuery(name = "TipoTramite.subTipoTramite", query = "SELECT tt FROM TipoTramite tt WHERE tt.padre = ?1"),
    @NamedQuery(name = "TipoTramite.rolesAcceso", query = "SELECT tt FROM TipoTramite tt WHERE tt.estado=TRUE AND tt.rolesAcceso LIKE ?1 ORDER BY tt.descripcion ASC")})
public class TipoTramite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activitykey")
    private String activitykey;
    @Column(name = "carpeta")
    private String carpeta;
    @Column(name = "user_direccion")
    private String userDireccion;
    @Column(name = "estado")
    private Boolean estado = true;
    @Column(name = "archivo_bpmn")
    private String archivoBpmn;
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "dlg_referencia")
    private String dlgReferencia;   
    @Column(name = "tiene_digitalizacion")
    private Boolean tieneDigitalizacion = false;
    @Column(name = "roles_acceso")
    private String rolesAcceso;
    @OneToMany(mappedBy = "tipoTramite")
    private List<TipoTramiteRequisito> historicoTramiteRequisitoList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private TipoTramite padre;
    @JoinColumn(name = "tramite", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Tramite tramite;
    

    public TipoTramite() {
    }

    public TipoTramite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnidadAdministrativa getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(UnidadAdministrativa unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getActivitykey() {
        return activitykey;
    }

    public void setActivitykey(String activitykey) {
        this.activitykey = activitykey;
    }

    public String getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public String getUserDireccion() {
        return userDireccion;
    }

    public void setUserDireccion(String userDireccion) {
        this.userDireccion = userDireccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getArchivoBpmn() {
        return archivoBpmn;
    }

    public void setArchivoBpmn(String archivoBpmn) {
        this.archivoBpmn = archivoBpmn;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Boolean getTieneDigitalizacion() {
        return tieneDigitalizacion;
    }

    public void setTieneDigitalizacion(Boolean tieneDigitalizacion) {
        this.tieneDigitalizacion = tieneDigitalizacion;
    }

    public List<TipoTramiteRequisito> getHistoricoTramiteRequisitoList() {
        return historicoTramiteRequisitoList;
    }

    public void setHistoricoTramiteRequisitoList(List<TipoTramiteRequisito> historicoTramiteRequisitoList) {
        this.historicoTramiteRequisitoList = historicoTramiteRequisitoList;
    }

    public String getDlgReferencia() {
        return dlgReferencia;
    }

    public void setDlgReferencia(String dlgReferencia) {
        this.dlgReferencia = dlgReferencia;
    }

    public String getRolesAcceso() {
        return rolesAcceso;
    }

    public void setRolesAcceso(String rolesAcceso) {
        this.rolesAcceso = rolesAcceso;
    }

    public TipoTramite getPadre() {
        return padre;
    }

    public void setPadre(TipoTramite padre) {
        this.padre = padre;
    }

    public Tramite getTramite() {
        return tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }
    
    @Override
    public String toString() {
        return "TipoTramite{" + "id=" + id + ", descripcion=" + descripcion + ", activitykey=" + activitykey + ", abreviatura=" + abreviatura + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoTramite other = (TipoTramite) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
