/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.EspCementerioBoveda;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_departamento_detalle_titulos", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppDepartamentoDetalleTitulos.findAll", query = "SELECT a FROM AppDepartamentoDetalleTitulos a")})
public class AppDepartamentoDetalleTitulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "categoria_establecimiento")
    private String categoriaEstablecimiento;
    @Size(max = 255)
    @Column(name = "de")
    private String de;
    @Size(max = 255)
    @Column(name = "departamento")
    private String departamento;
    @JoinColumn(name = "ente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente ente;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 255)
    @Column(name = "fecha_letra")
    private String fechaLetra;
    @Column(name = "num_recibo")
    private BigInteger numRecibo;
    @Size(max = 255)
    @Column(name = "para")
    private String para;
    @Size(max = 500)
    @Column(name = "tipo_establecimiento")
    private String tipoEstablecimiento;
    @Column(name = "total_personal")
    private Integer totalPersonal;
    @Column(name = "total_personal_administrativo")
    private Integer totalPersonalAdministrativo;
    @Column(name = "total_personal_operativo")
    private Integer totalPersonalOperativo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_pago")
    private BigDecimal totalPago;
    @Size(max = 255)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Size(max = 255)
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "veces")
    private Integer veces;
    @Column(name = "ruedas")
    private Integer ruedas;
    @Column(name = "toneladas")
    private BigDecimal toneladas;
    @JoinColumn(name = "forma_pago", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogoItem formaPago;
    @Column(name = "clave_predio")
    private String clavePredio;
    @Column(name = "predio")
    private Long predio;
    @JoinColumn(name = "cementerio_boveda", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EspCementerioBoveda cementerioBoveda;

    @OneToMany(mappedBy = "departamentoDetalleTitulos", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLiquidacion> finaRenLiquidacionList;
    @JoinColumn(name = "tipo_liquidacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLiquidacion tipoLiquidacion;

    public AppDepartamentoDetalleTitulos() {
    }

    public AppDepartamentoDetalleTitulos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoriaEstablecimiento() {
        return categoriaEstablecimiento;
    }

    public void setCategoriaEstablecimiento(String categoriaEstablecimiento) {
        this.categoriaEstablecimiento = categoriaEstablecimiento;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaLetra() {
        return fechaLetra;
    }

    public void setFechaLetra(String fechaLetra) {
        this.fechaLetra = fechaLetra;
    }

    public BigInteger getNumRecibo() {
        return numRecibo;
    }

    public void setNumRecibo(BigInteger numRecibo) {
        this.numRecibo = numRecibo;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getTipoEstablecimiento() {
        return tipoEstablecimiento;
    }

    public void setTipoEstablecimiento(String tipoEstablecimiento) {
        this.tipoEstablecimiento = tipoEstablecimiento;
    }

    public Integer getTotalPersonal() {
        return totalPersonal;
    }

    public void setTotalPersonal(Integer totalPersonal) {
        this.totalPersonal = totalPersonal;
    }

    public Integer getTotalPersonalAdministrativo() {
        return totalPersonalAdministrativo;
    }

    public void setTotalPersonalAdministrativo(Integer totalPersonalAdministrativo) {
        this.totalPersonalAdministrativo = totalPersonalAdministrativo;
    }

    public Integer getTotalPersonalOperativo() {
        return totalPersonalOperativo;
    }

    public void setTotalPersonalOperativo(Integer totalPersonalOperativo) {
        this.totalPersonalOperativo = totalPersonalOperativo;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(BigDecimal totalPago) {
        this.totalPago = totalPago;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    
    public List<FinaRenLiquidacion> getFinaRenLiquidacionList() {
        return finaRenLiquidacionList;
    }

    public void setFinaRenLiquidacionList(List<FinaRenLiquidacion> finaRenLiquidacionList) {
        this.finaRenLiquidacionList = finaRenLiquidacionList;
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public EspCementerioBoveda getCementerioBoveda() {
        return cementerioBoveda;
    }

    public void setCementerioBoveda(EspCementerioBoveda cementerioBoveda) {
        this.cementerioBoveda = cementerioBoveda;
    }

    public String getClavePredio() {
        return clavePredio;
    }

    public void setClavePredio(String clavePredio) {
        this.clavePredio = clavePredio;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public Integer getVeces() {
        return veces;
    }

    public void setVeces(Integer veces) {
        this.veces = veces;
    }

    public Integer getRuedas() {
        return ruedas;
    }

    public void setRuedas(Integer ruedas) {
        this.ruedas = ruedas;
    }

    public BigDecimal getToneladas() {
        return toneladas;
    }

    public void setToneladas(BigDecimal toneladas) {
        this.toneladas = toneladas;
    }

    public CatalogoItem getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(CatalogoItem formaPago) {
        this.formaPago = formaPago;
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
        if (!(object instanceof AppDepartamentoDetalleTitulos)) {
            return false;
        }
        AppDepartamentoDetalleTitulos other = (AppDepartamentoDetalleTitulos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppDepartamentoDetalleTitulos[ id=" + id + " ]";
    }

}
