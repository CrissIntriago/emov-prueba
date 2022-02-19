/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "papeleta_recaudacion", schema = "asgard")
@SqlResultSetMapping(name = "papeletaRecaudacionMaping",
        classes = @ConstructorResult(targetClass = PapeletaRecaudacion.class,
                columns = {
                    @ColumnResult(name = "usuario", type = String.class),
                    @ColumnResult(name = "efectivo", type = BigDecimal.class),
                    @ColumnResult(name = "cheque", type = BigDecimal.class),
                    @ColumnResult(name = "total", type = BigDecimal.class),})
)
public class PapeletaRecaudacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 2147483647)
    @Column(length = 2147483647)
    private String papeleta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 19, scale = 2)
    private BigDecimal efectivo;
    @Column(precision = 19, scale = 2)
    private BigDecimal cheque;
    @JoinColumn(name = "caja", referencedColumnName = "id")
    @ManyToOne
    private Cajero caja;
    @Column
    private String usuario;
    @Column(precision = 19, scale = 2)
    private BigDecimal total;
    @Column(name = "fecha_registro")
    private Date fecharegistro;    

    public PapeletaRecaudacion() {
    }

    public PapeletaRecaudacion(String usuario, BigDecimal efectivo, BigDecimal cheque, BigDecimal total) {
        this.usuario = usuario;
        this.efectivo = efectivo;
        this.cheque = cheque;
        this.total = total;
    }

    public PapeletaRecaudacion(Long id, String papeleta, BigDecimal efectivo, BigDecimal cheque, Cajero caja, Date fecharegistro, String usuario, BigDecimal total) {
        this.id = id;
        this.papeleta = papeleta;
        this.efectivo = efectivo;
        this.cheque = cheque;
        this.caja = caja;
        this.fecharegistro = fecharegistro;
        this.usuario = usuario;
        this.total = total;
    }

    public PapeletaRecaudacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPapeleta() {
        return papeleta;
    }

    public void setPapeleta(String papeleta) {
        this.papeleta = papeleta;
    }

    public BigDecimal getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(BigDecimal efectivo) {
        this.efectivo = efectivo;
    }

    public BigDecimal getCheque() {
        return cheque;
    }

    public void setCheque(BigDecimal cheque) {
        this.cheque = cheque;
    }

    public Cajero getCaja() {
        return caja;
    }

    public void setCaja(Cajero caja) {
        this.caja = caja;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fechaRegistro) {
        this.fecharegistro = fechaRegistro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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
        if (!(object instanceof PapeletaRecaudacion)) {
            return false;
        }
        PapeletaRecaudacion other = (PapeletaRecaudacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gestionTributaria.Entities.PapeletaRecaudacion[ id=" + id + " ]";
    }

}
