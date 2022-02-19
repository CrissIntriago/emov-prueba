/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ren_detalle_plusvalia", schema = Utils.SCHEMA_SGM)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RenDetallePlusvalia.findAll", query = "SELECT r FROM RenDetallePlusvalia r")})
public class RenDetallePlusvalia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "predios_asociados", referencedColumnName = "id")
    @ManyToOne
    private CatPredio prediosAsociados;
    @JoinColumn(name = "valores_plusvalia", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RenValoresPlusvalia valoresPlusvalia;
 
    public RenDetallePlusvalia() {
    }

    public RenDetallePlusvalia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatPredio getPrediosAsociados() {
        return prediosAsociados;
    }

    public void setPrediosAsociados(CatPredio prediosAsociados) {
        this.prediosAsociados = prediosAsociados;
    }

   

    public RenValoresPlusvalia getValoresPlusvalia() {
        return valoresPlusvalia;
    }

    public void setValoresPlusvalia(RenValoresPlusvalia valoresPlusvalia) {
        this.valoresPlusvalia = valoresPlusvalia;
    }


    
}
