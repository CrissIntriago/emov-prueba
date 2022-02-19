/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.tesoreria.modelTarifario.BancoOrigenModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "banco", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b"),
    @NamedQuery(name = "Banco.findById", query = "SELECT b FROM Banco b WHERE b.id = :id"),
    @NamedQuery(name = "Banco.findByNombreBanco", query = "SELECT b FROM Banco b WHERE b.nombreBanco = :nombreBanco"),
    @NamedQuery(name = "Banco.findByCuentaCorriente", query = "SELECT b FROM Banco b WHERE b.cuentaCorriente = :cuentaCorriente"),
    @NamedQuery(name = "Banco.findByFechaCreacion", query = "SELECT b FROM Banco b WHERE b.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Banco.findByUsuarioCreacion", query = "SELECT b FROM Banco b WHERE b.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Banco.findByFechaModificacion", query = "SELECT b FROM Banco b WHERE b.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Banco.findByUsuarioModifica", query = "SELECT b FROM Banco b WHERE b.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "Banco.findByEstado", query = "SELECT b FROM Banco b WHERE b.estado = :estado")})
@SqlResultSetMapping(name = "BancoOrigenValueMapping",
        classes = @ConstructorResult(targetClass = BancoOrigenModel.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "nombre", type = String.class),})
)
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre_banco")
    private String nombreBanco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cuenta_corriente")
    private String cuentaCorriente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "banco")
    private List<DetalleBanco> detalleBancoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nombreBanco")
    private List<CuentaBancaria> cuentaBancariaList;
//        @OneToMany(cascade = CascadeType.ALL, mappedBy = "nombreBanco")
//    private List<Servidor> servidorList;

    public Banco() {
    }

    public Banco(Long id) {
        this.id = id;
    }

    public Banco(Long id, String nombreBanco, String cuentaCorriente, Date fechaCreacion, String usuarioCreacion, boolean estado) {
        this.id = id;
        this.nombreBanco = nombreBanco;
        this.cuentaCorriente = cuentaCorriente;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getCuentaCorriente() {
        return cuentaCorriente;
    }

    public void setCuentaCorriente(String cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<DetalleBanco> getDetalleBancoList() {
        return detalleBancoList;
    }

    public void setDetalleBancoList(List<DetalleBanco> detalleBancoList) {
        this.detalleBancoList = detalleBancoList;
    }

    public List<CuentaBancaria> getCuentaBancariaList() {
        return cuentaBancariaList;
    }

    public void setCuentaBancariaList(List<CuentaBancaria> cuentaBancariaList) {
        this.cuentaBancariaList = cuentaBancariaList;
    }

//    public List<Servidor> getServidorList() {
//        return servidorList;
//    }
//
//    public void setServidorList(List<Servidor> servidorList) {
//        this.servidorList = servidorList;
//    }
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.Banco[ id=" + id + " ]";
    }

}
