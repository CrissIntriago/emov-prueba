/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CoaAbogado;
import com.gestionTributaria.Services.CoaAbogadoServices;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.authc.credential.DefaultPasswordService;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AbogadosCoactivaMB implements Serializable {

    DefaultPasswordService passService = new DefaultPasswordService();
    @Inject
    private UserSession session;
    @Inject
    private ManagerService services;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CoaAbogadoServices abogadoServices;
    @Inject
    private UsuarioService usuarioService;
    private LazyModel<CoaAbogado> abogados;
    private CoaAbogado abogado;
    private List<Usuarios> listUser;
    private Cliente cliente;
    private Usuarios usuario;
    private String nombreUsuario;

    @PostConstruct
    public void initView() {
        try {
            usuario = new Usuarios();
            cliente = new Cliente();
            abogados = new LazyModel<>(CoaAbogado.class);
            abogado = new CoaAbogado();
            abogado.setAbogadoInterno(Boolean.TRUE);
            listUser = usuarioService.findAllUser();
        } catch (Exception e) {
            Logger.getLogger(AbogadosCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void consultarExistenciaUsuario() {
        try {
            String identificacion = cliente.getIdentificacion();
            usuario = new Usuarios();
            if (!identificacion.isEmpty() && identificacion.length() == 10) {
                cliente = clienteService.getFindCliente(identificacion);
                if (cliente == null) {
                    cliente = clienteService.buscarCliente(identificacion);
                    abogado.setAbogadoInterno(Boolean.FALSE);
                }
                JsfUtil.update("formAbogado:tadForAbogado:abogadoExt");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void seleccionarAbogado(CoaAbogado ab) {
        try {
            if (ab == null) {
                abogado = new CoaAbogado();
            } else {
                abogado = ab;
            }
        } catch (Exception e) {
            Logger.getLogger(AbogadosCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void setAbogadoUser() {
        if (usuario != null) {
            abogado.setAbogadoInterno(Boolean.TRUE);
            abogado.setAbreviatura(usuario.getUsuario());
            abogado.setDetalle("Abg." + usuario.getFuncionario().getPersona().getNombre() + " " + usuario.getFuncionario().getPersona().getApellido());
//            --persona
        } else {
            abogado.setAbreviatura(null);
            abogado.setDetalle(null);
        }
    }

    public void limpiarDatos() {
        cliente = new Cliente();
        JsfUtil.update("formAbogado:tadForAbogado:abogadoExt");
    }

    public void llenarAbogado() {
        abogado.setEstado(Boolean.TRUE);
        abogado.setFechaIngreso(new Date());
        abogado.setUsuarioIngreso(session.getNameUser());
        abogado.setAuthUsuarios(usuario);
    }

    public void llenarDatosUsuario() {
        usuario = new Usuarios();
        usuario.setEstado(Boolean.TRUE);
        usuario.setFuncionario(null);
        usuario.setFechaIngreso(new Date());
        usuario.setCaducarClave(Boolean.TRUE);
        usuario.setDiasCaducidad(Integer.parseInt("90"));
        usuario.setUsuario(nombreUsuario);
        usuario.setEnte(cliente);
        usuario.setUsuarioIngreso(session.getNameUser());
        usuario.setContrasenia(passService.encryptPassword(usuario.getEnte().getIdentificacion()));
        abogado.setAbreviatura(usuario.getNameUsuario());
        abogado.setDetalle(usuario.getEnte().getNombreCompleto());
    }

    public void guardarAbogado() {
        try {
            if (validarAbogado(abogado)) {
                if (abogado.getId() == null) {
                    abogado.setEstado(Boolean.TRUE);
                    llenarAbogado();
                    if (Objects.equals(abogado.getAbogadoInterno(), Boolean.TRUE)) {
                        abogado = abogadoServices.create(abogado);
                    } else {
                        usuario = usuarioService.findByIdentificacionEnte(cliente.getIdentificacion());
                        if (usuario == null) {
                            cliente = clienteService.create(cliente);
                            llenarDatosUsuario();
                            usuario = usuarioService.create(usuario);
                            llenarAbogado();
                            abogado = abogadoServices.create(abogado);
                        } else {
                            llenarAbogado();
                            if (abogado.getId() == null) {
                                abogado = abogadoServices.create(abogado);
                                usuario = new Usuarios();
                                JsfUtil.update(":formAbogado");
                            } else {
                                abogadoServices.edit(abogado);
                                JsfUtil.addInformationMessage("Información", "Se editóExitosamente");
                            }
                        }
                    }
                    if (abogado != null) {
                        JsfUtil.addInformationMessage("Información", "Registro Grabado Exitosamente");
                    } else {
                        JsfUtil.addErrorMessage("Error", "No se pudo grabar el Registro");
                    }
                } else {
                    abogadoServices.edit(abogado);
                }
            } else {
                JsfUtil.addInformationMessage("Información", "Los campos son Obligatorios");
            }
            JsfUtil.update(":mainForm:dtAb");
        } catch (Exception e) {
            Logger.getLogger(AbogadosCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void inactivarActivarAbogado() {
        try {
            if (validarAbogado(abogado)) {
                abogado.setEstado(!abogado.getEstado());
                abogado = (CoaAbogado) services.save(abogado);
                if (abogado != null) {
                    JsfUtil.addInformationMessage("Información", "Registro Grabado Exitosamente");
                } else {
                    JsfUtil.addErrorMessage("Error", "No se pudo grabar el Registro");
                }
            } else {
                JsfUtil.addInformationMessage("Información", "Los campos son Obligatorios");
            }
        } catch (Exception e) {
            Logger.getLogger(AbogadosCoactivaMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get an set">
    public boolean validarAbogado(CoaAbogado ab) {
        if (ab.getAbogadoInterno() == true) {
            return !(ab.getDetalle() == null || ab.getDetalle().length() == 0);
        } else {
            return true;
        }
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public LazyModel<CoaAbogado> getAbogados() {
        return abogados;
    }

    public void setAbogados(LazyModel<CoaAbogado> abogados) {
        this.abogados = abogados;
    }

    public CoaAbogado getAbogado() {
        return abogado;
    }

    public void setAbogado(CoaAbogado abogado) {
        this.abogado = abogado;
    }

    public List<Usuarios> getListUser() {
        return listUser;
    }

    public void setListUser(List<Usuarios> listUser) {
        this.listUser = listUser;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public CoaAbogadoServices getAbogadoServices() {
        return abogadoServices;
    }

    public void setAbogadoServices(CoaAbogadoServices abogadoServices) {
        this.abogadoServices = abogadoServices;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
//</editor-fold>

}
