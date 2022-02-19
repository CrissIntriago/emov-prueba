/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.contabilidad.models.MovimientoCuentasModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cont_cuentas", schema = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "ContCuentas.findAll", query = "SELECT c FROM ContCuentas c order by c.codigo asc"),
    @NamedQuery(name = "ContCuentas.findById", query = "SELECT c FROM ContCuentas c WHERE c.id = :id"),
    @NamedQuery(name = "ContCuentas.findByConfId", query = "SELECT c FROM ContCuentas c WHERE c.confId = :confId"),
    @NamedQuery(name = "ContCuentas.findByCodigo", query = "SELECT c FROM ContCuentas c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "ContCuentas.findByDescripcion", query = "SELECT c FROM ContCuentas c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "ContCuentas.findByActivo", query = "SELECT c FROM ContCuentas c WHERE c.activo = :activo"),
    @NamedQuery(name = "ContCuentas.findByGobierno", query = "SELECT c FROM ContCuentas c WHERE c.gobierno = :gobierno"),
    @NamedQuery(name = "ContCuentas.findByMovimiento", query = "SELECT c FROM ContCuentas c WHERE c.movimiento = :movimiento"),
    @NamedQuery(name = "ContCuentas.findByClasificacion", query = "SELECT c FROM ContCuentas c WHERE c.clasificacion = :clasificacion"),
    @NamedQuery(name = "ContCuentas.findByUserCreacion", query = "SELECT c FROM ContCuentas c WHERE c.userCreacion = :userCreacion"),
    @NamedQuery(name = "ContCuentas.findByDateCreacion", query = "SELECT c FROM ContCuentas c WHERE c.dateCreacion = :dateCreacion"),
    @NamedQuery(name = "ContCuentas.findByUserModificacion", query = "SELECT c FROM ContCuentas c WHERE c.userModificacion = :userModificacion"),
    @NamedQuery(name = "ContCuentas.findByDateModificacion", query = "SELECT c FROM ContCuentas c WHERE c.dateModificacion = :dateModificacion"),
    @NamedQuery(name = "ContCuentas.findByUserDesactivar", query = "SELECT c FROM ContCuentas c WHERE c.userDesactivar = :userDesactivar"),
    @NamedQuery(name = "ContCuentas.findByDateDesactivar", query = "SELECT c FROM ContCuentas c WHERE c.dateDesactivar = :dateDesactivar")})
    @SqlResultSetMapping(name = "MovimientoCuentasModelMapping",
        classes = @ConstructorResult(targetClass = MovimientoCuentasModel.class,
        columns = {
            @ColumnResult(name = "sum_debe", type = BigDecimal.class),
            @ColumnResult(name = "sum_haber", type = BigDecimal.class),
            @ColumnResult(name = "num_diario", type = Integer.class),
            @ColumnResult(name = "anulado", type = Boolean.class),
            @ColumnResult(name = "tipo", type = String.class),
            @ColumnResult(name = "fecha_registro", type = Date.class),
            @ColumnResult(name = "num_comprobante", type = Integer.class),
            @ColumnResult(name = "beneficiario", type = String.class),
            @ColumnResult(name = "descripcion", type = String.class),
            @ColumnResult(name = "codigo", type = String.class),
            @ColumnResult(name = "debe", type = BigDecimal.class),
            @ColumnResult(name = "haber", type = BigDecimal.class),
            @ColumnResult(name = "nombre_cuenta", type = String.class),
        })
    )
public class ContCuentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "conf_id", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanCuentas confId;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "gobierno")
    private Boolean gobierno;
    @Column(name = "movimiento")
    private Boolean movimiento;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacion;
    @Size(max = 100)
    @Column(name = "user_creacion")
    private String userCreacion;
    @Column(name = "date_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreacion;
    @Size(max = 100)
    @Column(name = "user_modificacion")
    private String userModificacion;
    @Column(name = "date_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModificacion;
    @Size(max = 100)
    @Column(name = "user_desactivar")
    private String userDesactivar;
    @Column(name = "date_desactivar")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDesactivar;
    @OneToMany(mappedBy = "padre")
    private List<ContCuentas> contCuentasList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private ContCuentas padre;
    @OneToMany(mappedBy = "idCuenta")
    private List<ContSaldoInicial> contSaldoInicialList;
    @Column(name = "cta_pagar_cobrar")
    private Boolean ctaPagarCobrar;
    @Column(name = "tipo_asociacion")
    private Boolean tipoAsociacion;
    @Column(name = "cta_invertida")
    private Boolean ctaInvertida;
    @Column(name = "pagado_devengado")
    private Boolean pagadoDevengado;
    @Column(name = "estado")
    private Boolean estado;

    @Transient
    private String codPadre;
    @Transient
    private String codIngreso;
    @Transient
    private Boolean isHija;

    public ContCuentas() {
        activo = true;
        gobierno = true;
        movimiento = false;
        ctaPagarCobrar = false;
        estado = true;
        ctaInvertida = false;
        pagadoDevengado = false;
    }

    public ContCuentas(Long id) {
        this.id = id;
    }

    public ContCuentas(Long idcuenta, String codcuenta, String nomcuenta) {
        this.id = idcuenta;
        this.codigo = codcuenta;
        this.descripcion = nomcuenta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanCuentas getConfId() {
        return confId;
    }

    public void setConfId(PlanCuentas confId) {
        this.confId = confId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getGobierno() {
        return gobierno;
    }

    public void setGobierno(Boolean gobierno) {
        this.gobierno = gobierno;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public CatalogoItem getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(CatalogoItem clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getUserCreacion() {
        return userCreacion;
    }

    public void setUserCreacion(String userCreacion) {
        this.userCreacion = userCreacion;
    }

    public Date getDateCreacion() {
        return dateCreacion;
    }

    public void setDateCreacion(Date dateCreacion) {
        this.dateCreacion = dateCreacion;
    }

    public String getUserModificacion() {
        return userModificacion;
    }

    public void setUserModificacion(String userModificacion) {
        this.userModificacion = userModificacion;
    }

    public Date getDateModificacion() {
        return dateModificacion;
    }

    public void setDateModificacion(Date dateModificacion) {
        this.dateModificacion = dateModificacion;
    }

    public String getUserDesactivar() {
        return userDesactivar;
    }

    public void setUserDesactivar(String userDesactivar) {
        this.userDesactivar = userDesactivar;
    }

    public Date getDateDesactivar() {
        return dateDesactivar;
    }

    public void setDateDesactivar(Date dateDesactivar) {
        this.dateDesactivar = dateDesactivar;
    }

    public List<ContCuentas> getContCuentasList() {
        return contCuentasList;
    }

    public void setContCuentasList(List<ContCuentas> contCuentasList) {
        this.contCuentasList = contCuentasList;
    }

    public ContCuentas getPadre() {
        return padre;
    }

    public void setPadre(ContCuentas padre) {
        this.padre = padre;
    }

    public List<ContSaldoInicial> getContSaldoInicialList() {
        return contSaldoInicialList;
    }

    public void setContSaldoInicialList(List<ContSaldoInicial> contSaldoInicialList) {
        this.contSaldoInicialList = contSaldoInicialList;
    }

    public String getCodPadre() {
        if (padre != null) {
            codPadre = padre.getCodigo();
        }
        return codPadre;
    }

    public void setCodPadre(String codPadre) {
        this.codPadre = codPadre;
    }

    public String getCodIngreso() {
        if (padre != null) {
            int aux = codigo.length() - padre.getCodigo().length();
            if (aux > 0) {
                codIngreso = codigo.substring(padre.getCodigo().length()).replace(confId.getCaracter(), "");
            }
        } else {
            codIngreso = codigo;
        }
        return codIngreso;
    }

    public String returnIngreso() {
        return codIngreso;
    }

    public void setCodIngreso(String codIngreso) {
        this.codIngreso = codIngreso;
    }

    public Boolean getCtaPagarCobrar() {
        return ctaPagarCobrar;
    }

    public void setCtaPagarCobrar(Boolean ctaPagarCobrar) {
        this.ctaPagarCobrar = ctaPagarCobrar;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getCtaInvertida() {
        return ctaInvertida;
    }

    public void setCtaInvertida(Boolean ctaInvertida) {
        this.ctaInvertida = ctaInvertida;
    }

    public Boolean getPagadoDevengado() {
        return pagadoDevengado;
    }

    public void setPagadoDevengado(Boolean pagadoDevengado) {
        this.pagadoDevengado = pagadoDevengado;
    }

    public Boolean getTipoAsociacion() {
        return tipoAsociacion;
    }

    public void setTipoAsociacion(Boolean tipoAsociacion) {
        this.tipoAsociacion = tipoAsociacion;
    }

    public Boolean getIsHija() {
        return isHija;
    }

    public void setIsHija(Boolean isHija) {
        this.isHija = isHija;
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
        if (!(object instanceof ContCuentas)) {
            return false;
        }
        ContCuentas other = (ContCuentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.contabilidad.entities.ContCuentas[ id=" + id + " ]";
    }

}
