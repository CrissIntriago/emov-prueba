/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.PagoRubroMejora;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_pago_rubro", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenPagoRubro.findAll", query = "SELECT f FROM FinaRenPagoRubro f"),
    @NamedQuery(name = "FinaRenPagoRubro.findById", query = "SELECT f FROM FinaRenPagoRubro f WHERE f.id = :id"),
    @NamedQuery(name = "FinaRenPagoRubro.findByRubro", query = "SELECT f FROM FinaRenPagoRubro f WHERE f.rubro = :rubro"),
    @NamedQuery(name = "FinaRenPagoRubro.findByValor", query = "SELECT f FROM FinaRenPagoRubro f WHERE f.valor = :valor")})
public class FinaRenPagoRubro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne
    private FinaRenRubrosLiquidacion rubro;
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "pago", referencedColumnName = "id")
    @ManyToOne
    private FinaRenPago pago;
    @Column(name = "tramite")
    private Boolean tramite;
    @Column(name = "num_tramite")
    private String numTramite;
    @Column(name = "usuario_tramite")
    private String usuarioTramite;
    @OneToMany(mappedBy = "rubroMejoraPago", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<PagoRubroMejora> mejPagoRubroMejoras;

    public FinaRenPagoRubro() {
    }

    public FinaRenPagoRubro(Long id) {
        this.id = id;
    }

    public Boolean getTramite() {
        return tramite;
    }

    public void setTramite(Boolean tramite) {
        this.tramite = tramite;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public String getUsuarioTramite() {
        return usuarioTramite;
    }

    public void setUsuarioTramite(String usuarioTramite) {
        this.usuarioTramite = usuarioTramite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public FinaRenPago getPago() {
        return pago;
    }

    public void setPago(FinaRenPago pago) {
        this.pago = pago;
    }

    public List<PagoRubroMejora> getMejPagoRubroMejoras() {
        return mejPagoRubroMejoras;
    }

    public void setMejPagoRubroMejoras(List<PagoRubroMejora> mejPagoRubroMejoras) {
        this.mejPagoRubroMejoras = mejPagoRubroMejoras;
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
        if (!(object instanceof FinaRenPagoRubro)) {
            return false;
        }
        FinaRenPagoRubro other = (FinaRenPagoRubro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenPagoRubro[ id=" + id + " ]";
    }

}
