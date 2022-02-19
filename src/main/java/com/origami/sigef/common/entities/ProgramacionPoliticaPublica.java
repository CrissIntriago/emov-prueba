/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Dairon Freddy
 */
@Entity
@Table(name = "programacion_politica_publica")
@NamedQueries({
    @NamedQuery(name = "ProgramacionPoliticaPublica.findAll", query = "SELECT p FROM ProgramacionPoliticaPublica p"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findById", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.id = :id"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findByActividadOperativa", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.actividadOperativa = :actividadOperativa"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findByMetaFinanciera", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.metaFinanciera = :metaFinanciera"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findByDescripcionMeta", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.descripcionMeta = :descripcionMeta"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findByCodigoUnidadAdministrativaResponsaable", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.codigoUnidadAdministrativaResponsaable = :codigoUnidadAdministrativaResponsaable"),
    @NamedQuery(name = "ProgramacionPoliticaPublica.findByPresupuestoReformado", query = "SELECT p FROM ProgramacionPoliticaPublica p WHERE p.presupuestoReformado = :presupuestoReformado")})
public class ProgramacionPoliticaPublica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "actividad_operativa")
    private String actividadOperativa;
    @Column(name = "meta_financiera")
    private Short metaFinanciera;
    @Size(max = 2147483647)
    @Column(name = "descripcion_meta")
    private String descripcionMeta;
    @Column(name = "codigo_unidad_administrativa_responsaable")
    private BigInteger codigoUnidadAdministrativaResponsaable;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_reformado")
    private BigDecimal presupuestoReformado;
    @OneToMany(mappedBy = "programacionPolitica")
    private List<NumeroMetaLogrado> metasLogradas;

    public ProgramacionPoliticaPublica() {
    }

    public ProgramacionPoliticaPublica(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActividadOperativa() {
        return actividadOperativa;
    }

    public void setActividadOperativa(String actividadOperativa) {
        this.actividadOperativa = actividadOperativa;
    }

    public Short getMetaFinanciera() {
        return metaFinanciera;
    }

    public void setMetaFinanciera(Short metaFinanciera) {
        this.metaFinanciera = metaFinanciera;
    }

    public String getDescripcionMeta() {
        return descripcionMeta;
    }

    public void setDescripcionMeta(String descripcionMeta) {
        this.descripcionMeta = descripcionMeta;
    }

    public BigInteger getCodigoUnidadAdministrativaResponsaable() {
        return codigoUnidadAdministrativaResponsaable;
    }

    public void setCodigoUnidadAdministrativaResponsaable(BigInteger codigoUnidadAdministrativaResponsaable) {
        this.codigoUnidadAdministrativaResponsaable = codigoUnidadAdministrativaResponsaable;
    }

    public BigDecimal getPresupuestoReformado() {
        return presupuestoReformado;
    }

    public void setPresupuestoReformado(BigDecimal presupuestoReformado) {
        this.presupuestoReformado = presupuestoReformado;
    }

    public List<NumeroMetaLogrado> getMetasLogradas() {
        return metasLogradas;
    }

    public void setMetasLogradas(List<NumeroMetaLogrado> metasLogradas) {
        this.metasLogradas = metasLogradas;
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
        if (!(object instanceof ProgramacionPoliticaPublica)) {
            return false;
        }
        ProgramacionPoliticaPublica other = (ProgramacionPoliticaPublica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ProgramacionPoliticaPublica[ id=" + id + " ]";
    }

}
