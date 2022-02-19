/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.asgard.Entity.AppDepartamentoDetalleTitulos;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "esp_cementerio_boveda", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspCementerioBoveda.findAll", query = "SELECT e FROM EspCementerioBoveda e")})
public class EspCementerioBoveda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private BigDecimal area;
    @Column(name = "alto")
    private BigDecimal alto;
    @Column(name = "ancho")
    private BigDecimal ancho;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "cantidad_bovedas")
    private Integer cantidadBovedas;
    @Column(name = "cantidad_sepultados")
    private Integer cantidadSepultados;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "lote")
    private Integer lote;
    @Size(max = 10)
    @Column(name = "lote_alfanumerico")
    private String loteAlfanumerico;
    @Size(max = 10)
    @Column(name = "manzana")
    private String manzana;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne
    private Cliente propietario;
    @JoinColumn(name = "tipo_construccion", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem tipoConstruccion;
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @JoinColumn(name = "cementerio", referencedColumnName = "id")
    @ManyToOne
    private EspCementerio cementerio;
    @OneToMany(mappedBy = "cementerioBoveda")
    private List<EspCementerioBovedaEnte> espCementerioBovedaEnteList;

    @OneToMany(mappedBy = "cementerioBoveda")
    private List<AppDepartamentoDetalleTitulos> departamentoDetalleTitulosList;
    @OneToMany(mappedBy = "cementerioBoveda")
    private List<EspFotoBovedas> fotosBovedas;

    public EspCementerioBoveda() {
    }

    public EspCementerioBoveda(Long id) {
        this.id = id;
    }

    public EspCementerioBoveda(Long id, Date fechaCreacion) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public BigDecimal getAlto() {
        return alto;
    }

    public void setAlto(BigDecimal alto) {
        this.alto = alto;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidadBovedas() {
        return cantidadBovedas;
    }

    public void setCantidadBovedas(Integer cantidadBovedas) {
        this.cantidadBovedas = cantidadBovedas;
    }

    public Integer getCantidadSepultados() {
        return cantidadSepultados;
    }

    public void setCantidadSepultados(Integer cantidadSepultados) {
        this.cantidadSepultados = cantidadSepultados;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public String getLoteAlfanumerico() {
        return loteAlfanumerico;
    }

    public void setLoteAlfanumerico(String loteAlfanumerico) {
        this.loteAlfanumerico = loteAlfanumerico;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public CatalogoItem getTipoConstruccion() {
        return tipoConstruccion;
    }

    public void setTipoConstruccion(CatalogoItem tipoConstruccion) {
        this.tipoConstruccion = tipoConstruccion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public EspCementerio getCementerio() {
        return cementerio;
    }

    public void setCementerio(EspCementerio cementerio) {
        this.cementerio = cementerio;
    }

    public List<AppDepartamentoDetalleTitulos> getDepartamentoDetalleTitulosList() {
        return departamentoDetalleTitulosList;
    }

    public void setDepartamentoDetalleTitulosList(List<AppDepartamentoDetalleTitulos> departamentoDetalleTitulosList) {
        this.departamentoDetalleTitulosList = departamentoDetalleTitulosList;
    }

    public List<EspFotoBovedas> getFotosBovedas() {
        return fotosBovedas;
    }

    public void setFotosBovedas(List<EspFotoBovedas> fotosBovedas) {
        this.fotosBovedas = fotosBovedas;
    }

    public String getCedulasPropietarios() {
        String nombres = "";
        StringBuilder sb = new StringBuilder();
        if (this.espCementerioBovedaEnteList != null && !this.espCementerioBovedaEnteList.isEmpty()) {
            for (EspCementerioBovedaEnte bovEnte : espCementerioBovedaEnteList) {
                if (bovEnte.getTipo() != null) {
                    if (bovEnte.getTipo().getCodigo().equals("propietario")) {
                        nombres = bovEnte.getEnte().getIdentificacion();
                        sb.append(nombres).append(" - ");
                    }
                }
            }
        }
        if (sb.length() >= 3) {
            sb.delete(sb.length() - 3, sb.length() - 1);
        }
        return sb.toString().replaceAll("\\s{2,}", " ").trim().toUpperCase();
    }

    public String getNombresPropietarios() {
        String nombres = "";
        StringBuilder sb = new StringBuilder();
        if (this.espCementerioBovedaEnteList != null && !this.espCementerioBovedaEnteList.isEmpty()) {
            for (EspCementerioBovedaEnte bovEnte : espCementerioBovedaEnteList) {
//                if (bovEnte.getTipo().getId().equals(763L)) {
//                    if ("PER_NAT".equals(bovEnte.getEnte().getTipoProv().getCodigo())) {
                nombres = bovEnte.getEnte().getApellido() + " " + bovEnte.getEnte().getNombre();
//                    } else {
                nombres = " " + bovEnte.getEnte().getRazonSocial();
//                    }
                sb.append(nombres).append(" - ");
//                }
            }
        }
        if (sb.length() >= 3) {
            sb.delete(sb.length() - 3, sb.length() - 1);
        }
        return sb.toString().replaceAll("\\s{2,}", " ").trim().toUpperCase();
    }

    @XmlTransient
    public List<EspCementerioBovedaEnte> getEspCementerioBovedaEnteList() {
        return espCementerioBovedaEnteList;
    }

    public void setEspCementerioBovedaEnteList(List<EspCementerioBovedaEnte> espCementerioBovedaEnteList) {
        this.espCementerioBovedaEnteList = espCementerioBovedaEnteList;
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
        if (!(object instanceof EspCementerioBoveda)) {
            return false;
        }
        EspCementerioBoveda other = (EspCementerioBoveda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.EspCementerioBoveda[ id=" + id + " ]";
    }

}
