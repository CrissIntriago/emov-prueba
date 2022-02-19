/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "th_beneficios_sindicales", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "ThBeneficiosSindicales.findAll", query = "SELECT t FROM ThBeneficiosSindicales t"),
    @NamedQuery(name = "ThBeneficiosSindicales.findById", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.id = :id"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByIdEstructura", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.idEstructura = :idEstructura"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByIdCatalogo", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.idCatalogo = :idCatalogo"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByIdFuente", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.idFuente = :idFuente"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByIdCuentas", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.idCuentas = :idCuentas"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByPartidaPresupuestaria", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.partidaPresupuestaria = :partidaPresupuestaria"),
    @NamedQuery(name = "ThBeneficiosSindicales.findByEstado", query = "SELECT t FROM ThBeneficiosSindicales t WHERE t.estado = :estado")})
public class ThBeneficiosSindicales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "partida_presupuestaria")
    private String partidaPresupuestaria;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_servidor", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Servidor idServidor;
    @JoinColumn(name = "id_rubro", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThRubro idRubro;
    @JoinColumn(name = "id_tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ThTipoRol idTipoRol;
    @JoinColumn(name = "id_cuentas", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private ContCuentas idCuentas;
    @JoinColumn(name = "id_estructura", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresPlanProgramatico idEstructura;
    @JoinColumn(name = "id_catalogo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresCatalogoPresupuestario idCatalogo;
    @JoinColumn(name = "id_fuente", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PresFuenteFinanciamiento idFuente;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_creacion", length = 2147483647)
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @Size(max = 2147483647)
    @Column(name = "usuario_modificacion", length = 2147483647)
    private String usuarioModificacion;
    @Column(name = "valor", precision = 12, scale = 2)
    private BigDecimal valor;

    public ThBeneficiosSindicales() {
        this.estado = Boolean.TRUE;
        this.valor = BigDecimal.ZERO;
    }

    public ThBeneficiosSindicales(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContCuentas getIdCuentas() {
        return idCuentas;
    }

    public void setIdCuentas(ContCuentas idCuentas) {
        this.idCuentas = idCuentas;
    }

    public PresPlanProgramatico getIdEstructura() {
        return idEstructura;
    }

    public void setIdEstructura(PresPlanProgramatico idEstructura) {
        this.idEstructura = idEstructura;
    }

    public PresCatalogoPresupuestario getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(PresCatalogoPresupuestario idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public PresFuenteFinanciamiento getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(PresFuenteFinanciamiento idFuente) {
        this.idFuente = idFuente;
    }

    public String getPartidaPresupuestaria() {
        return partidaPresupuestaria;
    }

    public void setPartidaPresupuestaria(String partidaPresupuestaria) {
        this.partidaPresupuestaria = partidaPresupuestaria;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getIdServidor() {
        return idServidor;
    }

    public void setIdServidor(Servidor idServidor) {
        this.idServidor = idServidor;
    }

    public ThRubro getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(ThRubro idRubro) {
        this.idRubro = idRubro;
    }

    public ThTipoRol getIdTipoRol() {
        return idTipoRol;
    }

    public void setIdTipoRol(ThTipoRol idTipoRol) {
        this.idTipoRol = idTipoRol;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
        if (!(object instanceof ThBeneficiosSindicales)) {
            return false;
        }
        ThBeneficiosSindicales other = (ThBeneficiosSindicales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSindicales[ id=" + id + " ]";
    }

}
