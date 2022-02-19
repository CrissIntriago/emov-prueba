/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Sandra Arroba
 */
@Entity
@Table(name = "grupo_niveles", schema = "activos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoNiveles.findAll", query = "SELECT g FROM GrupoNiveles g"),
    @NamedQuery(name = "GrupoNiveles.findByPadres", query = "SELECT g FROM GrupoNiveles g where g.padre is NULL ORDER BY g.codigo, g.descripcion ASC"),

    @NamedQuery(name = "GrupoNiveles.getItemsParameter", query = "SELECT g FROM GrupoNiveles g JOIN g.padre p where p.id = ?1"),

    @NamedQuery(name = "GrupoNiveles.findGrupoByPadreEscogido", query = "SELECT g FROM GrupoNiveles g where g.padre = ?1 ORDER BY g.codigo asc "),

    @NamedQuery(name = "GrupoNiveles.findSubGrupoByGrupoEscogido", query = "SELECT g FROM GrupoNiveles g where g.padre = ?1 "),

    @NamedQuery(name = "GrupoNiveles.findById", query = "SELECT g FROM GrupoNiveles g WHERE g.id = :id"),
    @NamedQuery(name = "GrupoNiveles.findByCodigo", query = "SELECT g FROM GrupoNiveles g WHERE g.codigo = :codigo"),
    @NamedQuery(name = "GrupoNiveles.findByOrden", query = "SELECT g FROM GrupoNiveles g WHERE g.orden = :orden"),
    @NamedQuery(name = "GrupoNiveles.findByDescripcion", query = "SELECT g FROM GrupoNiveles g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GrupoNiveles.findByD", query = "SELECT g.padre FROM GrupoNiveles g WHERE g.id = ?1"),
    @NamedQuery(name = "GrupoNiveles.findByMaxOrden", query = "SELECT MAX(g.orden)+1 FROM GrupoNiveles g JOIN g.nivel n JOIN n.tipo t JOIN t.catalogo c WHERE g.estado = true AND c.codigo = ?1 AND t.codigo = ?2 AND n.orden = ?3"),
    @NamedQuery(name = "GrupoNiveles.findByMaxOrdenSub", query = "SELECT MAX(g.orden)+1 FROM GrupoNiveles g JOIN g.nivel n JOIN n.tipo t JOIN t.catalogo c WHERE g.estado = true AND c.codigo = ?1 AND t.codigo = ?2 AND n.orden = ?3 AND g.padre= ?4"),
    @NamedQuery(name = "GrupoNiveles.findByEstado", query = "SELECT g FROM GrupoNiveles g WHERE g.estado = :estado")})
public class GrupoNiveles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private long orden;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "nivel", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Nivel nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupoNiveles")
//    private List<Inventario> inventarioList;
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY)
    private List<GrupoNiveles> grupoNivelesList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private GrupoNiveles padre;
    @OneToMany(mappedBy = "asignarGrupo")
    private List<DetalleItem> detalleItemList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "area")
    private List<ConstatacionFisicaInventario> listConstArea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<ConstatacionFisicaInventario> listConstGrupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subGrupo")
    private List<ConstatacionFisicaInventario> listConstSubGrupo;

    @Transient
    private TreeNode nodeMenus;

    public GrupoNiveles() {
    }

    public GrupoNiveles(Long id) {
        this.id = id;
    }

    public GrupoNiveles(Long id, String codigo, long orden, String nombre, String descripcion, boolean estado) {
        this.id = id;
        this.codigo = codigo;
        this.orden = orden;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ConstatacionFisicaInventario> getListConstArea() {
        return listConstArea;
    }

    public void setListConstArea(List<ConstatacionFisicaInventario> listConstArea) {
        this.listConstArea = listConstArea;
    }

    public List<ConstatacionFisicaInventario> getListConstGrupo() {
        return listConstGrupo;
    }

    public void setListConstGrupo(List<ConstatacionFisicaInventario> listConstGrupo) {
        this.listConstGrupo = listConstGrupo;
    }

    public List<ConstatacionFisicaInventario> getListConstSubGrupo() {
        return listConstSubGrupo;
    }

    public void setListConstSubGrupo(List<ConstatacionFisicaInventario> listConstSubGrupo) {
        this.listConstSubGrupo = listConstSubGrupo;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

//    
//    public List<Inventario> getInventarioList() {
//        return inventarioList;
//    }
//
//    public void setInventarioList(List<Inventario> inventarioList) {
//        this.inventarioList = inventarioList;
//    }
    
    public List<GrupoNiveles> getGrupoNivelesList() {
        return grupoNivelesList;
    }

    public void setGrupoNivelesList(List<GrupoNiveles> grupoNivelesList) {
        this.grupoNivelesList = grupoNivelesList;
    }

    public GrupoNiveles getPadre() {
        return padre;
    }

    public void setPadre(GrupoNiveles padre) {
        this.padre = padre;
    }

    public List<DetalleItem> getDetalleItemList() {
        return detalleItemList;
    }

    public void setDetalleItemList(List<DetalleItem> detalleItemList) {
        this.detalleItemList = detalleItemList;
    }

    public TreeNode getNodeMenus() {
        if (nodeMenus == null) {
            createNode();
        }
        return nodeMenus;
    }

    public void setNodeMenus(TreeNode nodeMenus) {
        this.nodeMenus = nodeMenus;
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
        if (!(object instanceof GrupoNiveles)) {
            return false;
        }
        GrupoNiveles other = (GrupoNiveles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GrupoNiveles{" + "codigo=" + codigo + ", orden=" + orden + ", nombre=" + nombre + ", descripcion=" + descripcion + ", nivel=" + nivel + ", estado=" + estado + ", padre=" + padre + ", nodeMenus=" + nodeMenus + '}';
    }

    public void createNode() {
        GrupoNiveles gnivel = new GrupoNiveles();
        gnivel.setId(this.getId());
        gnivel.setNombre(getNombre());

        gnivel.setCodigo("00");
        nodeMenus = new DefaultTreeNode("Area " + getNombre(), gnivel, null);
        if (Utils.isNotEmpty(this.getGrupoNivelesList())) {
            for (GrupoNiveles grupo : this.getGrupoNivelesList()) {
                createItemsMenu(nodeMenus, grupo);
            }
        }
    }

    private TreeNode createItemsMenu(TreeNode node, GrupoNiveles grupo) {
        TreeNode menus = new DefaultTreeNode(grupo, node);
        if (Utils.isNotEmpty(grupo.getGrupoNivelesList())) {
            for (GrupoNiveles item : grupo.getGrupoNivelesList()) {
                createItemsMenu(menus, item);
            }
        }
        return menus;
    }
}
