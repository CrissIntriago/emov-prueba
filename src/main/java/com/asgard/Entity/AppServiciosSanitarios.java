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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_servicios_sanitarios", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppServiciosSanitarios.findAll", query = "SELECT a FROM AppServiciosSanitarios a"),
    @NamedQuery(name = "AppServiciosSanitarios.findById", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.id = :id"),
    @NamedQuery(name = "AppServiciosSanitarios.findByAbastecimientoAgua", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.abastecimientoAgua = :abastecimientoAgua"),
    @NamedQuery(name = "AppServiciosSanitarios.findByDispensadores", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.dispensadores = :dispensadores"),
    @NamedQuery(name = "AppServiciosSanitarios.findByDispPapelHigienico", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.dispPapelHigienico = :dispPapelHigienico"),
    @NamedQuery(name = "AppServiciosSanitarios.findByDispToallasDesechables", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.dispToallasDesechables = :dispToallasDesechables"),
    @NamedQuery(name = "AppServiciosSanitarios.findByDispJabonLiquido", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.dispJabonLiquido = :dispJabonLiquido"),
    @NamedQuery(name = "AppServiciosSanitarios.findByDispGelAntibacterial", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.dispGelAntibacterial = :dispGelAntibacterial"),
    @NamedQuery(name = "AppServiciosSanitarios.findByServicionHigienico", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.servicionHigienico = :servicionHigienico"),
    @NamedQuery(name = "AppServiciosSanitarios.findByServNo", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.servNo = :servNo"),
    @NamedQuery(name = "AppServiciosSanitarios.findByServPapelHigienico", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.servPapelHigienico = :servPapelHigienico"),
    @NamedQuery(name = "AppServiciosSanitarios.findByServToalla", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.servToalla = :servToalla"),
    @NamedQuery(name = "AppServiciosSanitarios.findByServSecadoManoElect", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.servSecadoManoElect = :servSecadoManoElect"),
    @NamedQuery(name = "AppServiciosSanitarios.findByLavamanos", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.lavamanos = :lavamanos"),
    @NamedQuery(name = "AppServiciosSanitarios.findByUrinario", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.urinario = :urinario"),
    @NamedQuery(name = "AppServiciosSanitarios.findByUrinarioNo", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.urinarioNo = :urinarioNo"),
    @NamedQuery(name = "AppServiciosSanitarios.findByJabon", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.jabon = :jabon"),
    @NamedQuery(name = "AppServiciosSanitarios.findByLavamanosNo", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.lavamanosNo = :lavamanosNo"),
    @NamedQuery(name = "AppServiciosSanitarios.findByAlcantarillado", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.alcantarillado = :alcantarillado"),
    @NamedQuery(name = "AppServiciosSanitarios.findByPozoSeptico", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.pozoSeptico = :pozoSeptico"),
    @NamedQuery(name = "AppServiciosSanitarios.findByCtrlInsectosTiempo", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.ctrlInsectosTiempo = :ctrlInsectosTiempo"),
    @NamedQuery(name = "AppServiciosSanitarios.findByCtrlInsectosProducto", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.ctrlInsectosProducto = :ctrlInsectosProducto"),
    @NamedQuery(name = "AppServiciosSanitarios.findByCtrlPlagaEmpresa", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.ctrlPlagaEmpresa = :ctrlPlagaEmpresa"),
    @NamedQuery(name = "AppServiciosSanitarios.findByCtrlPlagaEmpresa1", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.ctrlPlagaEmpresa1 = :ctrlPlagaEmpresa1"),
    @NamedQuery(name = "AppServiciosSanitarios.findByAlmaceQuimicosProductos", query = "SELECT a FROM AppServiciosSanitarios a WHERE a.almaceQuimicosProductos = :almaceQuimicosProductos")})
public class AppServiciosSanitarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "abastecimiento_agua")
    private BigInteger abastecimientoAgua;
    @Column(name = "dispensadores")
    private Boolean dispensadores;
    @Column(name = "disp_papel_higienico")
    private Boolean dispPapelHigienico;
    @Column(name = "disp_toallas_desechables")
    private Boolean dispToallasDesechables;
    @Column(name = "disp_jabon_liquido")
    private Boolean dispJabonLiquido;
    @Column(name = "disp_gel_antibacterial")
    private Boolean dispGelAntibacterial;
    @Column(name = "servicion_higienico")
    private Boolean servicionHigienico;
    @Column(name = "serv_no")
    private Integer servNo;
    @Column(name = "serv_papel_higienico")
    private Boolean servPapelHigienico;
    @Column(name = "serv_toalla")
    private Boolean servToalla;
    @Column(name = "serv_secado_mano_elect")
    private Boolean servSecadoManoElect;
    @Column(name = "lavamanos")
    private Boolean lavamanos;
    @Column(name = "urinario")
    private Boolean urinario;
    @Column(name = "urinario_no")
    private Integer urinarioNo;
    @Column(name = "jabon")
    private Boolean jabon;
    @Column(name = "lavamanos_no")
    private Integer lavamanosNo;
    @Column(name = "alcantarillado")
    private Boolean alcantarillado;
    @Column(name = "pozo_septico")
    private Boolean pozoSeptico;
    @Column(name = "ctrl_insectos_tiempo")
    private Integer ctrlInsectosTiempo;
    @Size(max = 100)
    @Column(name = "ctrl_insectos_producto")
    private String ctrlInsectosProducto;
    @Size(max = 100)
    @Column(name = "ctrl_plaga_empresa_")
    private String ctrlPlagaEmpresa;
    @Size(max = 255)
    @Column(name = "ctrl_plaga_empresa")
    private String ctrlPlagaEmpresa1;
    @Column(name = "almace_quimicos_productos")
    private Boolean almaceQuimicosProductos;
    @OneToMany(mappedBy = "servicioSanitario")
    private List<AppProteccionAlimentos> appProteccionAlimentosList;
    @OneToMany(mappedBy = "serviciosSanitario")
    private List<AppProteccionAlimentos> appProteccionAlimentosList1;
    @JoinColumn(name = "ctrl_disposiciones", referencedColumnName = "id")
    @ManyToOne
    private AppCtrlDisposiciones ctrlDisposiciones;

    public AppServiciosSanitarios() {
    }

    public AppServiciosSanitarios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getAbastecimientoAgua() {
        return abastecimientoAgua;
    }

    public void setAbastecimientoAgua(BigInteger abastecimientoAgua) {
        this.abastecimientoAgua = abastecimientoAgua;
    }

    public Boolean getDispensadores() {
        return dispensadores;
    }

    public void setDispensadores(Boolean dispensadores) {
        this.dispensadores = dispensadores;
    }

    public Boolean getDispPapelHigienico() {
        return dispPapelHigienico;
    }

    public void setDispPapelHigienico(Boolean dispPapelHigienico) {
        this.dispPapelHigienico = dispPapelHigienico;
    }

    public Boolean getDispToallasDesechables() {
        return dispToallasDesechables;
    }

    public void setDispToallasDesechables(Boolean dispToallasDesechables) {
        this.dispToallasDesechables = dispToallasDesechables;
    }

    public Boolean getDispJabonLiquido() {
        return dispJabonLiquido;
    }

    public void setDispJabonLiquido(Boolean dispJabonLiquido) {
        this.dispJabonLiquido = dispJabonLiquido;
    }

    public Boolean getDispGelAntibacterial() {
        return dispGelAntibacterial;
    }

    public void setDispGelAntibacterial(Boolean dispGelAntibacterial) {
        this.dispGelAntibacterial = dispGelAntibacterial;
    }

    public Boolean getServicionHigienico() {
        return servicionHigienico;
    }

    public void setServicionHigienico(Boolean servicionHigienico) {
        this.servicionHigienico = servicionHigienico;
    }

    public Integer getServNo() {
        return servNo;
    }

    public void setServNo(Integer servNo) {
        this.servNo = servNo;
    }

    public Boolean getServPapelHigienico() {
        return servPapelHigienico;
    }

    public void setServPapelHigienico(Boolean servPapelHigienico) {
        this.servPapelHigienico = servPapelHigienico;
    }

    public Boolean getServToalla() {
        return servToalla;
    }

    public void setServToalla(Boolean servToalla) {
        this.servToalla = servToalla;
    }

    public Boolean getServSecadoManoElect() {
        return servSecadoManoElect;
    }

    public void setServSecadoManoElect(Boolean servSecadoManoElect) {
        this.servSecadoManoElect = servSecadoManoElect;
    }

    public Boolean getLavamanos() {
        return lavamanos;
    }

    public void setLavamanos(Boolean lavamanos) {
        this.lavamanos = lavamanos;
    }

    public Boolean getUrinario() {
        return urinario;
    }

    public void setUrinario(Boolean urinario) {
        this.urinario = urinario;
    }

    public Integer getUrinarioNo() {
        return urinarioNo;
    }

    public void setUrinarioNo(Integer urinarioNo) {
        this.urinarioNo = urinarioNo;
    }

    public Boolean getJabon() {
        return jabon;
    }

    public void setJabon(Boolean jabon) {
        this.jabon = jabon;
    }

    public Integer getLavamanosNo() {
        return lavamanosNo;
    }

    public void setLavamanosNo(Integer lavamanosNo) {
        this.lavamanosNo = lavamanosNo;
    }

    public Boolean getAlcantarillado() {
        return alcantarillado;
    }

    public void setAlcantarillado(Boolean alcantarillado) {
        this.alcantarillado = alcantarillado;
    }

    public Boolean getPozoSeptico() {
        return pozoSeptico;
    }

    public void setPozoSeptico(Boolean pozoSeptico) {
        this.pozoSeptico = pozoSeptico;
    }

    public Integer getCtrlInsectosTiempo() {
        return ctrlInsectosTiempo;
    }

    public void setCtrlInsectosTiempo(Integer ctrlInsectosTiempo) {
        this.ctrlInsectosTiempo = ctrlInsectosTiempo;
    }

    public String getCtrlInsectosProducto() {
        return ctrlInsectosProducto;
    }

    public void setCtrlInsectosProducto(String ctrlInsectosProducto) {
        this.ctrlInsectosProducto = ctrlInsectosProducto;
    }

    public String getCtrlPlagaEmpresa() {
        return ctrlPlagaEmpresa;
    }

    public void setCtrlPlagaEmpresa(String ctrlPlagaEmpresa) {
        this.ctrlPlagaEmpresa = ctrlPlagaEmpresa;
    }

    public String getCtrlPlagaEmpresa1() {
        return ctrlPlagaEmpresa1;
    }

    public void setCtrlPlagaEmpresa1(String ctrlPlagaEmpresa1) {
        this.ctrlPlagaEmpresa1 = ctrlPlagaEmpresa1;
    }

    public Boolean getAlmaceQuimicosProductos() {
        return almaceQuimicosProductos;
    }

    public void setAlmaceQuimicosProductos(Boolean almaceQuimicosProductos) {
        this.almaceQuimicosProductos = almaceQuimicosProductos;
    }

    
    public List<AppProteccionAlimentos> getAppProteccionAlimentosList() {
        return appProteccionAlimentosList;
    }

    public void setAppProteccionAlimentosList(List<AppProteccionAlimentos> appProteccionAlimentosList) {
        this.appProteccionAlimentosList = appProteccionAlimentosList;
    }

    
    public List<AppProteccionAlimentos> getAppProteccionAlimentosList1() {
        return appProteccionAlimentosList1;
    }

    public void setAppProteccionAlimentosList1(List<AppProteccionAlimentos> appProteccionAlimentosList1) {
        this.appProteccionAlimentosList1 = appProteccionAlimentosList1;
    }

    public AppCtrlDisposiciones getCtrlDisposiciones() {
        return ctrlDisposiciones;
    }

    public void setCtrlDisposiciones(AppCtrlDisposiciones ctrlDisposiciones) {
        this.ctrlDisposiciones = ctrlDisposiciones;
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
        if (!(object instanceof AppServiciosSanitarios)) {
            return false;
        }
        AppServiciosSanitarios other = (AppServiciosSanitarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppServiciosSanitarios[ id=" + id + " ]";
    }
    
}
