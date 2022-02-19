/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.audit.ShowName;
import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.config.LogEntityListener;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANGEL NAVARRO
 */
@Table(name = "usuarios", schema = "auth")
@Entity
@NamedQueries({
    @NamedQuery(name = "Usuario.findByFuncionarioIdentifivacion", query = "SELECT ur FROM Usuarios ur INNER JOIN ur.funcionario f INNER JOIN f.persona p WHERE p.identificacion = ?1"),
    @NamedQuery(name = "Usuario.findByFuncionario", query = "SELECT ur FROM Usuarios ur WHERE ur.funcionario = ?1 AND ur.estado = TRUE"),
    @NamedQuery(name = "Usuario.findByUsuarioActivo", query = "SELECT ur FROM Usuarios ur WHERE ur.funcionario is not null and ur.estado = true ORDER BY ur.usuario ASC"),
    @NamedQuery(name = "Usuario.findByUsuarioActivoParametre", query = "SELECT ur FROM Usuarios ur WHERE ur.funcionario is not null and ur.estado = ?1 ORDER BY ur.usuario ASC"),
    @NamedQuery(name = "Usuario.findEmpresaByUsuario", query = "SELECT ur.empresaId FROM Usuarios ur WHERE ur.funcionario is not null and ur.estado = true AND ur.id = ?1"),
    @NamedQuery(name = "Usuario.findByEstado", query = "SELECT ur FROM Usuarios ur WHERE ur.estado = true")})
@EntityListeners(LogEntityListener.class)
@ShowName(name = "Usuario")
@XmlRootElement
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    @ShowName(name = "Identificador")
    private Long id;
    @Size(max = 255)
    @Column(name = "usuario", length = 50, nullable = false)
    @ShowName(name = "Nombre de usuario")
    private String usuario;
    @Column(name = "contrasenia", length = 1000, nullable = false)
    @ShowName(name = "Clave")
    private String contrasenia;
    @Basic(optional = false)
    @Column(nullable = false)
    @ShowName(name = "Habiblidato")
    private Boolean estado;
    @JoinColumn(name = "funcionario")
    @OneToOne
    @ShowName(name = "Datos de Funcionario")
    @GsonExcludeField
    private Servidor funcionario;
    @Column(name = "code")
    private String code;
    @Column(name = "fecha_caducidad")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCaducidad;
    @Column(name = "fecha_ingreso")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Column(name = "caducar_clave")
    @ShowName(name = "Caduca clave")
    private Boolean caducarClave = true;
    @Column(name = "dias_caducidad")
    @ShowName(name = "Dias Caduca clave")
    private Integer diasCaducidad = 90;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ShowName(name = "Roles de accesos")
    @GsonExcludeField
    private List<UsuarioRol> roles;
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    @ShowName(name = "Empresa")
    @GsonExcludeField
    private DatosGeneralesEntidad empresaId;
    @Column(name = "sis_enabled")
    private Boolean sisEnabled;
    @Transient
    private String claveTemp;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<UsuarioRol> usuariosRolesList;

    @JoinColumn(name = "ente")
    @OneToOne
    private Cliente ente;

    public Usuarios() {
    }

    public Usuarios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNameUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Servidor getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Servidor funcionario) {
        this.funcionario = funcionario;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public Boolean getCaducarClave() {
        return caducarClave;
    }

    public void setCaducarClave(Boolean caducarClave) {
        this.caducarClave = caducarClave;
    }

    public Integer getDiasCaducidad() {
        return diasCaducidad;
    }

    public void setDiasCaducidad(Integer diasCaducidad) {
        this.diasCaducidad = diasCaducidad;
    }

    
    public List<UsuarioRol> getRoles() {
        return roles;
    }

    public void setRoles(List<UsuarioRol> roles) {
        this.roles = roles;
    }

    public DatosGeneralesEntidad getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(DatosGeneralesEntidad empresaId) {
        this.empresaId = empresaId;
    }

    public String getClaveTemp() {
        return claveTemp;
    }

    public void setClaveTemp(String claveTemp) {
        this.claveTemp = claveTemp;
    }

    public List<UsuarioRol> getUsuariosRolesList() {
        return usuariosRolesList;
    }

    public void setUsuariosRolesList(List<UsuarioRol> usuariosRolesList) {
        this.usuariosRolesList = usuariosRolesList;
    }

    public Cliente getEnte() {
        return ente;
    }

    public void setEnte(Cliente ente) {
        this.ente = ente;
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
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuarios{" + "id=" + id + ", usuario=" + usuario + ", contrasenia=" + contrasenia + ", estado=" + estado + ", code=" + code + ", fechaCaducidad=" + fechaCaducidad + ", fechaIngreso=" + fechaIngreso + ", usuarioIngreso=" + usuarioIngreso + ", caducarClave=" + caducarClave + ", diasCaducidad=" + diasCaducidad + '}';
    }

}
