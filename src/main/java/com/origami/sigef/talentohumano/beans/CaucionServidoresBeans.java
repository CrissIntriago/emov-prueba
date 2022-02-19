/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CaucionServidores;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.services.CaucionServidoresService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Origami
 */
@Named(value = "CaucionServidoresBeans")
@ViewScoped
public class CaucionServidoresBeans implements Serializable {

    private LazyModel<CaucionServidores> lazy;
    private List<MasterCatalogo> periodos;
    private CaucionServidores caucionservidores;
    private OpcionBusqueda busqueda;
    private short periodo;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private TipoRol rolCopia;
    private TipoRol rolSeleccionado;

    @Inject
    private MasterCatalogoService catalogoService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CaucionServidoresService caucionservidoresService;
    @Inject
    private ParametrizableService parametroService;
    private BigDecimal valorCuota;
    private int contador;
    private ParametrosTalentoHumano valoPrametro;
    private LazyModel<DiasLaborado> listaServidores;

    @PostConstruct
    public void inicializate() {
        contador = 0;
        rolCopia = new TipoRol();
        valorCuota = BigDecimal.ZERO;
        caucionservidores = new CaucionServidores();
        caucionservidores.setPorcentaje(new BigDecimal(40));
        caucionservidores.setServidor(new Servidor());
        caucionservidores.setServidor(new Servidor());
        caucionservidores.setValorParametrizado(new ParametrosTalentoHumano());
        caucionservidores.getServidor().setPersona(new Cliente());
        caucionservidores.getServidor().setDistributivo(new Distributivo());
        caucionservidores.setDistributivo(new Distributivo());
        caucionservidores.setTipoRol(new TipoRol());
        busqueda = new OpcionBusqueda();
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        valoPrametro = parametroService.valorParametroTipo("CAUCIONES");
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
    }
    
    public void actualizarRoles() {
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }
    
    public void buscar() {
        rolSeleccionado = caucionservidores.getTipoRol();
        lazy = new LazyModel<>(CaucionServidores.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("periodo", periodo);
        lazy.getFilterss().put("tipoRol", caucionservidores.getTipoRol());
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
    }

    public void buscarServidor() {
        if (rolSeleccionado == null) {
            JsfUtil.addErrorMessage("Rol", "Debe seleccionar un rol");
            return;
        }
        listaServidores = new LazyModel<>(DiasLaborado.class);
        listaServidores.getFilterss().put("estado", true);
        listaServidores.getSorteds().put("servidor.persona.apellido", "ASC");
        listaServidores.getFilterss().put("tipoRol", rolSeleccionado);
        listaServidores.setDistinct(false);
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
        PrimeFaces.current().ajax().update("formServidorPublico");
        PrimeFaces.current().ajax().update(":servidorPublicoDialog");
    }

    public void selectData(SelectEvent evt) {
        caucionservidores.setServidor((Servidor) evt.getObject());
        if (caucionservidores.getServidor() != null) {
            caucionservidores.setDistributivo(caucionservidores.getServidor().getDistributivo());
        }
    }

    public void selectDataServidor(DiasLaborado dia) {
        caucionservidores.setServidor(dia.getServidor());
        if (caucionservidores.getServidor() != null) {
            caucionservidores.setDistributivo(caucionservidores.getServidor().getDistributivo());
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public void cancelar() {
        resetValues();
    }

    public void guardar() {
        boolean edit = caucionservidores.getId() != null;
        if (rolSeleccionado == null) {
            JsfUtil.addErrorMessage("El valor del porventaje minimo debe ser de 10%", "");
            return;
        }
        List<CaucionServidores> lista = caucionservidoresService.listPrestamoIESS(rolSeleccionado);
        try {
            if (edit) {
                caucionservidores.setFechaModificacion(new Date());
                caucionservidores.setUsuarioModifica(userSession.getNameUser());
                if (this.caucionservidores.getPorcentaje().doubleValue() < 10) {
                    JsfUtil.addWarningMessage("El valor del porventaje minimo debe ser de 10%", "");
                    return;
                }
                caucionservidoresService.edit(caucionservidores);
            } else {
                if (buscarServidor(lista, caucionservidores)) {
                    JsfUtil.addWarningMessage("Registro de Servidor", "El Servidor ya se encuentra registrado");
                    return;
                }
                caucionservidores.setFechaCreacion(new Date());
                caucionservidores.setUsuarioCreacion(userSession.getNameUser());
                caucionservidores.setPeriodo(periodo);
                caucionservidores.setBaseImponible(calculoBaseImponible());
                caucionservidores.setValorParametrizado(valoPrametro);
                caucionservidores.setCuotaPropocional(calculoCuotaPropocional(caucionservidores.getBaseImponible()));
                if (this.caucionservidores.getPorcentaje().doubleValue() < 10) {
                    JsfUtil.addWarningMessage("El valor del porventaje minimo debe ser de 10%", "");
                    return;
                }
                caucionservidores = caucionservidoresService.create(caucionservidores);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        actualizarValores();
        resetValues();
        JsfUtil.addInformationMessage("Caucion Servidor", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
    }

    public void handleCloseForm(CloseEvent event) {
        PrimeFaces.current().ajax().update("formservidor");
    }

    public void generarCopia() {
        int executeUpdate;
        if (this.rolCopia.getId() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        if (this.rolCopia.equals(caucionservidores.getTipoRol())) {
            JsfUtil.addWarningMessage("Error", "No puede realizar Copia del mismo rol");
            return;
        }
        Date fechaCreacion = new Date();
        executeUpdate = caucionservidoresService.getcopia(caucionservidores.getTipoRol(), fechaCreacion, userSession.getNameUser(), rolCopia);
        executeUpdate = 0;
        if (executeUpdate > 0) {
            JsfUtil.addSuccessMessage("Datos", "Los Datos del Rol Anterior fueron Traidos con éxito");
        }
        List<CaucionServidores> lista = caucionservidoresService.listPrestamoIESS(caucionservidores.getTipoRol());
        for (CaucionServidores cau : lista) {
            caucionservidores = cau;
            caucionservidores.setPeriodo(periodo);
            caucionservidores.setBaseImponible(calculoBaseImponible());
            caucionservidores.setCuotaPropocional(calculoCuotaPropocional(caucionservidores.getBaseImponible()));
            caucionservidoresService.edit(caucionservidores);
        }
        caucionservidores = new CaucionServidores();
        PrimeFaces.current().executeScript("PF('dialogQuirografario').hide()");
        PrimeFaces.current().ajax().update(":formservidor");
    }

    public void form() {
        if (caucionservidores.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        List<CaucionServidores> lista = caucionservidoresService.listPrestamoIESS(caucionservidores.getTipoRol());
        if (lista != null) {
            if (!lista.isEmpty()) {
                JsfUtil.addWarningMessage("Error", "Ya cuenta con datos registrados");
                return;
            }
        }
        if (caucionservidores.getTipoRol().getId() == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar una Cabecera de Rol");
            return;
        }
        PrimeFaces.current().executeScript("PF('dialogQuirografario').show()");
        PrimeFaces.current().ajax().update(":formservidor");
    }

    public void resetValues() {
        valorCuota = BigDecimal.ZERO;
        caucionservidores = new CaucionServidores();
        caucionservidores.setPorcentaje(new BigDecimal(40));
        caucionservidores.setServidor(new Servidor());
        caucionservidores.setValorParametrizado(new ParametrosTalentoHumano());
        caucionservidores.getServidor().setPersona(new Cliente());
        caucionservidores.getServidor().setDistributivo(new Distributivo());
        caucionservidores.setDistributivo(new Distributivo());
        caucionservidores.setTipoRol(new TipoRol());
    }

    public void eliminar(CaucionServidores ca) {
        if (ca.getTipoRol().getEstadoAprobacion().equals(registrado)) {
            ca.setEstado(Boolean.FALSE);
            caucionservidoresService.edit(ca);
            actualizarValores();
        } else {
            JsfUtil.addWarningMessage("Eliminar", "No puede Eliminar con Roles Aprobados o Pagados");
        }
    }

    public void editar(CaucionServidores ca) {
        this.caucionservidores = ca;
        this.valorCuota = ca.getCuotaPropocional();
    }

    public void actualizarValores() {
        List<CaucionServidores> lista = caucionservidoresService.listPrestamoIESS(rolSeleccionado);
        if (!lista.isEmpty()) {
            for (CaucionServidores c : lista) {
                c.setCuotaPropocional(calculoCuotaPropocional(c.getBaseImponible()));
                caucionservidoresService.edit(c);
            }
        }
    }

    public BigDecimal calculoBaseImponible() {
        double total;
        total = ((this.caucionservidores.getValorPrimaNeta().doubleValue() * this.caucionservidores.getPorcentaje().doubleValue()) / 100);
        return new BigDecimal(total);
    }

    public BigDecimal calculoCuotaPropocional(BigDecimal valor) {
        double total;
        if (rolSeleccionado.getId() != null) {
            Long cantidad = caucionservidoresService.numeroServidores(rolSeleccionado);
            if (cantidad.intValue() == 0 || cantidad == null) {
                total = (valor.doubleValue() / 1) / 12;
            } else {
                total = (valor.doubleValue() / cantidad.intValue()) / 12;
            }
            return new BigDecimal(total);
        }
        return new BigDecimal(BigInteger.ZERO);
    }

    public void valorMinimo() {
        if (this.caucionservidores.getPorcentaje().doubleValue() < 10) {
            JsfUtil.addWarningMessage("El valor del porventaje minimo debe ser de 10%", "");
        }
        this.valorCuota = calculoCuotaPropocional(calculoBaseImponible());
    }

    public boolean buscarServidor(List<CaucionServidores> lista, CaucionServidores servidor) {
        return lista.stream().anyMatch((p) -> (servidor.getServidor().equals(p.getServidor())));
    }

    public LazyModel<CaucionServidores> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<CaucionServidores> lazy) {
        this.lazy = lazy;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public CaucionServidores getCaucionservidores() {
        return caucionservidores;
    }

    public void setCaucionservidores(CaucionServidores caucionservidores) {
        this.caucionservidores = caucionservidores;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public CatalogoItem getRegistrado() {
        return registrado;
    }

    public void setRegistrado(CatalogoItem registrado) {
        this.registrado = registrado;
    }

    public TipoRol getRolCopia() {
        return rolCopia;
    }

    public void setRolCopia(TipoRol rolCopia) {
        this.rolCopia = rolCopia;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }

    public LazyModel<DiasLaborado> getListaServidores() {
        return listaServidores;
    }

    public void setListaServidores(LazyModel<DiasLaborado> listaServidores) {
        this.listaServidores = listaServidores;
    }

}
