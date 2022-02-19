/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dairon Freddy C
 */
@Entity
@Table(name = "catalogo_item", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CatalogoItem.findAll", query = "SELECT c FROM CatalogoItem c"),
    @NamedQuery(name = "CatalogoItem.findAllEstados", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo NOT IN ('NEW-CF')"),  //erwin
    @NamedQuery(name = "CatalogoItem.findByTipoPlan", query = "SELECT c FROM CatalogoItem c WHERE c.codigo= ?1"),
    @NamedQuery(name = "CatalogoItem.findById", query = "SELECT c FROM CatalogoItem c WHERE c.id = :id"),
    @NamedQuery(name = "CatalogoItem.findByCatalogo", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 ORDER BY c.orden"),
    @NamedQuery(name = "CatalogoItem.findCatalogoClasificacion1", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 ORDER BY c.texto"),
    @NamedQuery(name = "CatalogoItem.getEstadoConstatacion", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?2 AND c.codigo =?1"),
    @NamedQuery(name = "CatalogoItem.findCatalogotipo1", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo = ?2  ORDER BY c.orden"),
    @NamedQuery(name = "CatalogoItem.findCatalogotipoLike", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo LIKE ?2  ORDER BY c.orden"),
    @NamedQuery(name = "CatalogoItem.findByC", query = "SELECT c FROM CatalogoItem c JOIN c.catalogo cc WHERE cc.codigo = ?1 AND c.codigo = ?2"),
    @NamedQuery(name = "CatalogoItem.findByOrden", query = "SELECT c FROM CatalogoItem c WHERE c.orden = :orden"),
    @NamedQuery(name = "CatalogoItem.findByTexto", query = "SELECT c FROM CatalogoItem c WHERE c.texto = :texto"),
    @NamedQuery(name = "CatalogoItem.findByDescripcion", query = "SELECT c FROM CatalogoItem c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "CatalogoItem.findByDefaultValue", query = "SELECT c FROM CatalogoItem c WHERE c.defaultValue = :defaultValue"),
    @NamedQuery(name = "CatalogoItem.TipoCatalogo", query = "SELECT cc FROM CatalogoItem cc where cc.codigo IN ('CC','CP')"),
    @NamedQuery(name = "CatalogoItem.findTipoIdentificacion", query = "SELECT cc FROM CatalogoItem cc where cc.codigo = ?1"),
    @NamedQuery(name = "CatalogoItem.findByMeses", query = "SELECT cc FROM CatalogoItem cc INNER JOIN cc.catalogo ca WHERE ca.codigo = ?1 ORDER BY cc.orden ASC"),
    @NamedQuery(name = "CatalogoItem.findByCedulaRuc", query = "SELECT c FROM CatalogoItem c join c.catalogo cc  WHERE c.codigo= ?1 AND cc.codigo = ?2"),
    @NamedQuery(name = "CatalogoItem.findByPadre", query = "SELECT c FROM CatalogoItem c WHERE c.padre = ?1"),
    @NamedQuery(name = "CatalogoItem.findByCodigo", query = "SELECT c FROM CatalogoItem c WHERE c.codigo = ?1")})
@XmlRootElement
public class CatalogoItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "orden")
    private Short orden;
    @Size(max = 250)
    @Column(name = "texto")
    private String texto;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "default_value")
    private Boolean defaultValue;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "valor")
    private BigDecimal valor = BigDecimal.ZERO;
    @JoinColumn(name = "catalogo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @GsonExcludeField
    private Catalogo catalogo;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem padre;
    @Transient
    private String nameCatalogo;

    public CatalogoItem() {
    }

    public CatalogoItem(Long id) {
        this.id = id;
    }

    public CatalogoItem(String codigo) {
        this.codigo = codigo;
    }

    public CatalogoItem(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CatalogoItem(Long tipo_registro, String cod_tipo) {
        this.id = tipo_registro;
        this.texto = cod_tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public CatalogoItem getPadre() {
        return padre;
    }

    public void setPadre(CatalogoItem padre) {
        this.padre = padre;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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
        if (!(object instanceof CatalogoItem)) {
            return false;
        }
        CatalogoItem other = (CatalogoItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return texto;
    }

    public String getNameCatalogo() {
        if (nameCatalogo == null) {
            nameCatalogo = catalogo.getNombre();
        }
        return nameCatalogo;
    }

    public void setNameCatalogo(String nameCatalogo) {
        this.nameCatalogo = nameCatalogo;
    }

}
