/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Formula;
import com.origami.sigef.resource.talento_humano.entities.Servidor;

/**
 *
 * @author OrigamiEC
 */
@Entity
@Table(name = "distributivo", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Distributivo.findAll", query = "SELECT d FROM Distributivo d"),
    @NamedQuery(name = "Distributivo.findById", query = "SELECT d FROM Distributivo d WHERE d.id = :id"),
    @NamedQuery(name = "Distributivo.findByUnidadAdministrativa", query = "SELECT d FROM Distributivo d WHERE d.unidadAdministrativa = :unidadAdministrativa"),
    @NamedQuery(name = "Distributivo.findByEstado", query = "SELECT d FROM Distributivo d WHERE d.estado = :estado"),
    @NamedQuery(name = "Distributivo.findByCargosNull", query = "SELECT d FROM Distributivo d WHERE d.estado = true and d.servidorPublico is Null ")})

@XmlRootElement
public class Distributivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "unidad_administrativa", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadAdministrativa;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "estado")
    private boolean estado;
    @OneToMany(mappedBy = "distributivo")
    private List<Servidor> servidorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distributivo")
    private List<DetalleRegistro> detalleRegistroList;
    @JoinColumn(name = "cargo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cargo cargo;
    @JoinColumn(name = "regimen", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RegimenLaboral regimen;
    @JoinColumn(name = "tipo_contrato", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private RegimenLaboral tipoContrato;
    @JoinColumn(name = "servidor_publico", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Servidor servidorPublico;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distributivo")
    private List<ValoresDistributivo> valoresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distributivo")
    private List<DistributivoEscala> distributivoEscalaCollection;
    @Basic(optional = true)
    @Column(name = "vigencia_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaDesde;
    @Basic(optional = true)
    @Column(name = "vigencia_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaHasta;
    @Basic(optional = true)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = true)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = true)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Basic(optional = true)
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Formula("(SELECT CASE WHEN \n"
            + "		(SELECT pd.id FROM partidas_distributivo pd \n"
            + "                INNER JOIN talento_humano.valores_distributivo vd ON vd.id = pd.distributivo\n"
            + "                INNER JOIN conf.parametros_talento_humano pt ON pt.id = vd.valores_parametrizados\n"
            + "                INNER JOIN catalogo_item ca ON ca.id = pt.valores\n"
            + "                WHERE vd.distributivo = id AND pd.partida_ap IS NOT NULL AND ca.codigo = 'RAU' AND pd.reforma_codificado > 0\n"
            + "                AND pd.codigo_reforma_traspaso IS NULL AND pd.codigo_reforma IS NULL AND vd.estado = true\n"
            + "                AND vd.anio= EXTRACT(YEAR FROM now()) AND pd.periodo = vd.anio) is not null\n"
            + " THEN true\n"
            + " ELSE false\n"
            + " END)")
    private boolean estadoActivo;

    @Formula("(select concat('C-', right(('C-'||'000000'||d.id),6) ) from talento_humano.distributivo d WHERE d.id=id)")
    private String codigoSQL;

    @OneToMany(mappedBy = "distributivo")
    private List<AnticipoRemuneracion> anticipoRemuneracionList;

    @OneToMany(mappedBy = "distributivo")
    private List<DiasLaborado> listDiasLaborado;

    @OneToMany(mappedBy = "distributivo")
    private List<HoraExtraSuplementaria> listHoraExtraSupl;
    @OneToMany(mappedBy = "distributivo")
    private List<PrestamoIess> listPrestamoIess;
    @OneToMany(mappedBy = "distributivo")
    private List<CaucionServidores> caucionServidoresList;

    public Distributivo() {
        this.estadoActivo = false;
    }

    public Distributivo(Long id) {
        this.id = id;
    }

    public Distributivo(Long id, boolean estado, Date vigenciaDesde, Date vigenciaHasta,
            Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.estado = estado;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }

    public Distributivo(Servidor servidorPublico) {
        this.servidorPublico = servidorPublico;

    }

    public Distributivo(Servidor servidorPublico, Cargo cargo) {
        this.servidorPublico = servidorPublico;
        this.cargo = cargo;

    }

    public Distributivo(Servidor servidorPublico, Cargo cargo, UnidadAdministrativa unidad) {
        this.servidorPublico = servidorPublico;
        this.cargo = cargo;
        this.unidadAdministrativa = unidad;

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

    public boolean getEstado() {
        return estado;
    }

    public List<Servidor> getServidorList() {
        return servidorList;
    }

    public void setServidorList(List<Servidor> servidorList) {
        this.servidorList = servidorList;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<DetalleRegistro> getDetalleRegistroList() {
        return detalleRegistroList;
    }

    public void setDetalleRegistroList(List<DetalleRegistro> detalleRegistroList) {
        this.detalleRegistroList = detalleRegistroList;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public List<AnticipoRemuneracion> getAnticipoRemuneracionList() {
        return anticipoRemuneracionList;
    }

    public void setAnticipoRemuneracionList(List<AnticipoRemuneracion> anticipoRemuneracionList) {
        this.anticipoRemuneracionList = anticipoRemuneracionList;
    }

//    public EscalaSalarial getEscalaSalarial() {
//        return escalaSalarial;
//    }
//
//    public void setEscalaSalarial(EscalaSalarial escalaSalarial) {
//        this.escalaSalarial = escalaSalarial;
//    }
    public RegimenLaboral getRegimen() {
        return regimen;
    }

    public void setRegimen(RegimenLaboral regimen) {
        this.regimen = regimen;
    }

    public Servidor getServidorPublico() {
        return servidorPublico;
    }

    public void setServidorPublico(Servidor servidorPublico) {
        this.servidorPublico = servidorPublico;
    }

    public RegimenLaboral getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(RegimenLaboral tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
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

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public boolean getEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public List<ValoresDistributivo> getValoresList() {
        return valoresList;
    }

    public void setValoresList(List<ValoresDistributivo> valoresList) {
        this.valoresList = valoresList;
    }

    public List<DistributivoEscala> getDistributivoEscalaCollection() {
        return distributivoEscalaCollection;
    }

    public void setDistributivoEscalaCollection(List<DistributivoEscala> distributivoEscalaCollection) {
        this.distributivoEscalaCollection = distributivoEscalaCollection;
    }

    public List<DiasLaborado> getListDiasLaborado() {
        return listDiasLaborado;
    }

    public void setListDiasLaborado(List<DiasLaborado> listDiasLaborado) {
        this.listDiasLaborado = listDiasLaborado;
    }

    public List<HoraExtraSuplementaria> getListHoraExtraSupl() {
        return listHoraExtraSupl;
    }

    public void setListHoraExtraSupl(List<HoraExtraSuplementaria> listHoraExtraSupl) {
        this.listHoraExtraSupl = listHoraExtraSupl;
    }

    public List<PrestamoIess> getListPrestamoIess() {
        return listPrestamoIess;
    }

    public void setListPrestamoIess(List<PrestamoIess> listPrestamoIess) {
        this.listPrestamoIess = listPrestamoIess;
    }

    public List<CaucionServidores> getCaucionServidoresList() {
        return caucionServidoresList;
    }

    public void setCaucionServidoresList(List<CaucionServidores> caucionServidoresList) {
        this.caucionServidoresList = caucionServidoresList;
    }

    public String getCodigoSQL() {
        return codigoSQL;
    }

    public void setCodigoSQL(String codigoSQL) {
        this.codigoSQL = codigoSQL;
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
        if (!(object instanceof Distributivo)) {
            return false;
        }
        Distributivo other = (Distributivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Distributivo{" + "id=" + id + ", unidadAdministrativa=" + unidadAdministrativa + ", estado=" + estado + ", servidorList=" + servidorList + ", detalleRegistroList=" + detalleRegistroList + ", cargo=" + cargo + ", regimen=" + regimen + ", tipoContrato=" + tipoContrato + ", servidorPublico=" + servidorPublico + ", valoresList=" + valoresList + ", distributivoEscalaCollection=" + distributivoEscalaCollection + ", vigenciaDesde=" + vigenciaDesde + ", vigenciaHasta=" + vigenciaHasta + ", fechaCreacion=" + fechaCreacion + ", usuarioCreacion=" + usuarioCreacion + ", fechaModificacion=" + fechaModificacion + ", usuarioModifica=" + usuarioModifica + ", estadoActivo=" + estadoActivo + ", codigoSQL=" + codigoSQL + ", anticipoRemuneracionList=" + anticipoRemuneracionList + ", listDiasLaborado=" + listDiasLaborado + ", listHoraExtraSupl=" + listHoraExtraSupl + ", listPrestamoIess=" + listPrestamoIess + ", caucionServidoresList=" + caucionServidoresList + '}';
    }
    
}
