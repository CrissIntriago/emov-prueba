/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.RecActasEspeciesDet;
import com.gestionTributaria.Entities.RecEspecies;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_rubros_liquidacion", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenRubrosLiquidacion.findAll", query = "SELECT f FROM FinaRenRubrosLiquidacion f")})
public class FinaRenRubrosLiquidacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_rubro")
    private Long codigoRubro;
    @Column(name = "cuenta_orden")
    private String cuentaOrden;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "porcentaje_servicio")
    private Boolean porcentajeServicio;
    @Column(name = "tipo_acto")
    private String tipoActo;
    @Column(name = "prioridad")
    private BigInteger prioridad;
    @Column(name = "rubro_del_municipio")
    private Boolean rubroDelMunicipio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;
    @Column(name = "aplica_exoneracion")
    private Boolean aplicaExoneracion;
    @OneToMany(mappedBy = "rubro", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenDetLiquidacion> finaRenDetLiquidacionList;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLiquidacion tipoLiquidacion;
    @JoinColumn(name = "tipo_valor", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoValor tipoValor;
    @Transient
    private BigDecimal valorTotal;
    @Transient
    ///ESTE PPUEDE SER EL AVALUO DE LA CONSTRUCCION
    //PARA LA APROBACION DE LOS PLANOS O EL AREA DEL METRO CUADRADO PARA EL CALCULO DEL ESTACIONAIENTO DE VEHICULOS
    private BigDecimal valorCalculo;
    @Transient
    private Integer cantidad;
    @Transient
    private RecActasEspeciesDet acta;
    @Transient
    private BigDecimal valorPorcentualTemp;

    @Transient
    private Boolean cobrar = false;
    @Transient
    private Integer anio;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "rubro", fetch = FetchType.LAZY)
    private RecEspecies recEspecies;
    @JoinColumn(name = "tipo_rubro", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tipoRubro;

    @JoinColumn(name = "cuenta_presupuesto", referencedColumnName = "id")
    @ManyToOne
    private CatalogoPresupuesto cuentaPresupuesto;
    @JoinColumn(name = "cont_cc", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas contCc;
    @JoinColumn(name = "cont_cp", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas contCp;

    @JoinColumn(name = "cta_debe_cart", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas ctaDebeCart;
    @JoinColumn(name = "cta_haber_cart", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas ctaHaberCart;
    @JoinColumn(name = "cta_orden_deudor", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas ctaOrdenDeudor;
    @JoinColumn(name = "cta_orden_acreedor", referencedColumnName = "id")
    @ManyToOne
    private ContCuentas ctaOrdenAcreedor;
    @JoinColumn(name = "partida", referencedColumnName = "id")
    @ManyToOne
    private PresCatalogoPresupuestario partida;
    @JoinColumn(name = "partida_cart", referencedColumnName = "id")
    @ManyToOne
    private PresCatalogoPresupuestario partidacart;
    @Column(name = "anio_avaluo")
    private Integer anioAvaluo;

    public FinaRenRubrosLiquidacion() {
        this.rubroDelMunicipio = true;
        anioAvaluo = 0;
    }

    public FinaRenRubrosLiquidacion(Long id) {
        this.id = id;
        anioAvaluo = 0;
    }

    public FinaRenRubrosLiquidacion(String descripcion, Integer cantidad, BigDecimal valorTotal) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.valorTotal = valorTotal;
        anioAvaluo = 0;
    }

    public FinaRenRubrosLiquidacion(Long id, boolean estado) {
        this.id = id;
        this.estado = estado;
        anioAvaluo = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogoItem getTipoRubro() {
        return tipoRubro;
    }

    public void setTipoRubro(CatalogoItem tipoRubro) {
        this.tipoRubro = tipoRubro;
    }

    public ContCuentas getCtaOrdenDeudor() {
        return ctaOrdenDeudor;
    }

    public void setCtaOrdenDeudor(ContCuentas ctaOrdenDeudor) {
        this.ctaOrdenDeudor = ctaOrdenDeudor;
    }

    public ContCuentas getCtaOrdenAcreedor() {
        return ctaOrdenAcreedor;
    }

    public void setCtaOrdenAcreedor(ContCuentas ctaOrdenAcreedor) {
        this.ctaOrdenAcreedor = ctaOrdenAcreedor;
    }

    public Long getCodigoRubro() {
        return codigoRubro;
    }

    public void setCodigoRubro(Long codigoRubro) {
        this.codigoRubro = codigoRubro;
    }

    public CatalogoPresupuesto getCuentaPresupuesto() {
        return cuentaPresupuesto;
    }

    public void setCuentaPresupuesto(CatalogoPresupuesto cuentaPresupuesto) {
        this.cuentaPresupuesto = cuentaPresupuesto;
    }

    public String getCuentaOrden() {
        return cuentaOrden;
    }

    public void setCuentaOrden(String cuentaOrden) {
        this.cuentaOrden = cuentaOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(BigInteger prioridad) {
        this.prioridad = prioridad;
    }

    public Boolean getRubroDelMunicipio() {
        return rubroDelMunicipio;
    }

    public void setRubroDelMunicipio(Boolean rubroDelMunicipio) {
        this.rubroDelMunicipio = rubroDelMunicipio;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<FinaRenDetLiquidacion> getFinaRenDetLiquidacionList() {
        return finaRenDetLiquidacionList;
    }

    public void setFinaRenDetLiquidacionList(List<FinaRenDetLiquidacion> finaRenDetLiquidacionList) {
        this.finaRenDetLiquidacionList = finaRenDetLiquidacionList;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public FinaRenTipoValor getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(FinaRenTipoValor tipoValor) {
        this.tipoValor = tipoValor;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getAplicaExoneracion() {
        return aplicaExoneracion;
    }

    public void setAplicaExoneracion(Boolean aplicaExoneracion) {
        this.aplicaExoneracion = aplicaExoneracion;
    }

    public BigDecimal getValorCalculo() {
        return valorCalculo;
    }

    public void setValorCalculo(BigDecimal valorCalculo) {
        this.valorCalculo = valorCalculo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public RecActasEspeciesDet getActa() {
        return acta;
    }

    public void setActa(RecActasEspeciesDet acta) {
        this.acta = acta;
    }

    public BigDecimal getValorPorcentualTemp() {
        return valorPorcentualTemp;
    }

    public void setValorPorcentualTemp(BigDecimal valorPorcentualTemp) {
        this.valorPorcentualTemp = valorPorcentualTemp;
    }

    public Boolean getCobrar() {
        return cobrar;
    }

    public void setCobrar(Boolean cobrar) {
        this.cobrar = cobrar;
    }

    public RecEspecies getRecEspecies() {
        return recEspecies;
    }

    public void setRecEspecies(RecEspecies recEspecies) {
        this.recEspecies = recEspecies;
    }

    public String getTipoActo() {
        return tipoActo;
    }

    public void setTipoActo(String tipoActo) {
        this.tipoActo = tipoActo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public ContCuentas getContCc() {
        return contCc;
    }

    public void setContCc(ContCuentas contCc) {
        this.contCc = contCc;
    }

    public ContCuentas getContCp() {
        return contCp;
    }

    public void setContCp(ContCuentas contCp) {
        this.contCp = contCp;
    }

    public ContCuentas getCtaDebeCart() {
        return ctaDebeCart;
    }

    public void setCtaDebeCart(ContCuentas ctaDebeCart) {
        this.ctaDebeCart = ctaDebeCart;
    }

    public ContCuentas getCtaHaberCart() {
        return ctaHaberCart;
    }

    public void setCtaHaberCart(ContCuentas ctaHaberCart) {
        this.ctaHaberCart = ctaHaberCart;
    }

    public PresCatalogoPresupuestario getPartida() {
        return partida;
    }

    public void setPartida(PresCatalogoPresupuestario partida) {
        this.partida = partida;
    }

    public PresCatalogoPresupuestario getPartidacart() {
        return partidacart;
    }

    public void setPartidacart(PresCatalogoPresupuestario partidacart) {
        this.partidacart = partidacart;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getPorcentajeServicio() {
        return porcentajeServicio;
    }

    public void setPorcentajeServicio(Boolean porcentajeServicio) {
        this.porcentajeServicio = porcentajeServicio;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getAnioAvaluo() {
        return anioAvaluo;
    }

    public void setAnioAvaluo(Integer anioAvaluo) {
        this.anioAvaluo = anioAvaluo;
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
        if (!(object instanceof FinaRenRubrosLiquidacion)) {
            return false;
        }
        FinaRenRubrosLiquidacion other = (FinaRenRubrosLiquidacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FinaRenRubrosLiquidacion{" + "id=" + id + ", contCc=" + contCc + ", contCp=" + contCp + ", ctaDebeCart=" + ctaDebeCart + ", ctaHaberCart=" + ctaHaberCart + ", ctaOrdenDeudor=" + ctaOrdenDeudor + ", ctaOrdenAcreedor=" + ctaOrdenAcreedor + ", partida=" + partida + ", partidacart=" + partidacart + '}';
    }

}
