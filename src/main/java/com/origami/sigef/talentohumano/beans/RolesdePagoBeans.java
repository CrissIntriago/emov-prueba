package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoAnexoService;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Luis Pozo G
 */
@Named(value = "rolesdePagoBeans")
@ViewScoped
public class RolesdePagoBeans implements Serializable {

    //trabajar con roles
    @Inject
    private RolesDePagoService rolesDePagoService;
    private RolesDePago rolesDePago;
    private RolesDePago rolesDePagoSeleccionado;
    private RolesDePago auxRol;
//    private List<RolesDePago> listRolesPago;

    @Inject
    private DistributivoEscalaService disEscalaService;
    private DistributivoEscala escala;

    @Inject
    private UserSession userSession;
    private OpcionBusqueda busqueda;
    //SERVIDOR
    @Inject
    private ServidorService servidorService;
    private Servidor servidorDialog;
    private List<PartidasDistributivo> partidaDistributivo;
    private List<PartidasDistributivoAnexo> listAnexo;
    @Inject
    private PartidaDistributivoService partidaService;
    @Inject
    private ParametrizableService parametrizableService;

    //trabajar con valores roles
    private List<ValoresRoles> listaValoresRoles;
    @Inject
    private ValoresRolesService valorRolesService;
    private ValoresRoles valoresRol;

    @Inject
    private MasterCatalogoService masterCatalogoService;
    private List<MasterCatalogo> periodos;
    private String mesR;

    @Inject
    private CuentaContableService cuentaService;
    private List<CuentaContable> listCuenta;

    @Inject
    private PartidaDistributivoAnexoService partidaAnexoService;
    private LazyModel<RolesDePago> lazyRoles;

    private List<ParametrosTalentoHumano> valoresMostrar;
    private List<ParametrosTalentoHumano> valoresSeleccionando;

    @PostConstruct
    public void init() {
        valoresMostrar = new ArrayList<>();
        valoresSeleccionando = new ArrayList<>();
        busqueda = new OpcionBusqueda();
        rolesDePago = new RolesDePago();
        rolesDePagoSeleccionado = new RolesDePago();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
    }

    public void btnBuscar() {
        if (busqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar un Periodo a buscar.");
            return;
        }
        listAnexo = partidaAnexoService.getDistributivoAnexoOriginal(busqueda.getAnio());
        List<Servidor> auxListSer = new ArrayList<>();
        auxListSer = servidorService.findServidorNoRolesAndPe(busqueda.getAnio());
        if (auxListSer != null && !auxListSer.isEmpty()) {
            for (Servidor s : auxListSer) {
                rolesDePago.setServidor(s);
                rolesDePago.setEstado(Boolean.TRUE);
                rolesDePago.setFechaCreacion(new Date());
                rolesDePago.setPeriodo(busqueda.getAnio());
                rolesDePago.setFechaModificacion(new Date());
                rolesDePago.setUsuarioCreacion(userSession.getName());
                rolesDePago.setUsuarioModificacion(userSession.getName());
                rolesDePago = rolesDePagoService.create(rolesDePago);
                setValoresRolesPartida(rolesDePago);
                rolesDePago = new RolesDePago();
            }
        }
        lazyRoles = new LazyModel<>(RolesDePago.class);
        lazyRoles.getFilterss().put("estado", true);
        lazyRoles.setDistinct(false);
        lazyRoles.getFilterss().put("periodo", busqueda.getAnio());
        lazyRoles.getSorteds().put("servidor.persona.apellido", "ASC");
        JsfUtil.addSuccessMessage("Información", " Datos del periodo " + busqueda.getAnio() + " Cargados Correctamente.");
    }

    public void selectRol(RolesDePago rol) {
        auxRol = new RolesDePago();
        auxRol = rol;
        servidorDialog = new Servidor();
        servidorDialog = auxRol.getServidor();
        escala = new DistributivoEscala();
        this.escala = disEscalaService.getEscalaDistributivoAnio(servidorDialog.getDistributivo(), busqueda);
        List<PartidasDistributivo> lisNewPartidas = partidaService.showAllPartidaXDistributiv(servidorDialog.getDistributivo(), busqueda.getAnio());
        List<ValoresRoles> listValoresIngresos = valorRolesService.FindValoresIngresoRolesXRol(auxRol);
        List<ValoresRoles> listValoresEliminar = new ArrayList<>();
        for (ValoresRoles vr : listValoresIngresos) {
            boolean comprobar = false;
            for (PartidasDistributivo pd : lisNewPartidas) {
                if (vr.getValorParametrizable().getTipo().getCodigo().equals(pd.getDistributivo().getValoresParametrizados().getTipo().getCodigo())) {
                    if (vr.getPartidaAp().equals(pd.getPartidaAp())) {
                        comprobar = true;
                        break;
                    }
                }
            }
            if (!comprobar) {
                listValoresEliminar.add(vr);
            }
        }
        if (!listValoresEliminar.isEmpty()) {
            for (ValoresRoles ValorEliminar : listValoresEliminar) {
                ValorEliminar.setEstado(Boolean.FALSE);
                valorRolesService.edit(ValorEliminar);
            }
        }
        partidaDistributivo = new ArrayList<>();
        partidaDistributivo = partidaService.showPartidaxDistributivoAndPeriodo(servidorDialog.getDistributivo(), busqueda.getAnio());
        if (partidaDistributivo != null) {
            for (PartidasDistributivo item : partidaDistributivo) {
                guardarValores(item);
            }
        }
        listCuenta = new ArrayList<>();
        listCuenta = cuentaService.getCuentasContablesXPeriodo(busqueda.getAnio());
        listaValoresRoles = new ArrayList<>();
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        if (!listaValoresRoles.isEmpty()) {
            for (ValoresRoles item : listaValoresRoles) {
                if (item.getCuentaContable() == null && item.getItemApR() != null) {
                    item.setCuentaContable(getCuenta(item.getItemApR()));
                    valorRolesService.edit(item);
                }
            }
        }
        PrimeFaces.current().executeScript("PF('dlgServidor').show()");
        PrimeFaces.current().ajax().update(":panelServidor");
    }

    private void guardarValores(PartidasDistributivo item) {
        valoresRol = new ValoresRoles();
        valoresRol.setPeriodo(busqueda.getAnio());
        valoresRol.setEstado(Boolean.TRUE);
        valoresRol.setValorParametrizable(item.getDistributivo().getValoresParametrizados());
        valoresRol.setEstructuraApR(item.getEstructuraAp());
        valoresRol.setFuenteApR(item.getFuenteAp());
        valoresRol.setFuenteDirectaR(item.getFuenteDirecta());
        valoresRol.setItemApR(item.getItemAp());
        valoresRol.setPartidaAp(item.getPartidaAp());
        valoresRol.setRolPago(auxRol);
        valoresRol.setCuentaContable(getCuenta(valoresRol.getItemApR()));
        valoresRol = valorRolesService.create(valoresRol);
    }

    public CuentaContable getCuenta(CatalogoPresupuesto c) {
        CuentaContable cuenta;
        cuenta = cuentaService.getCuentaXItem(c);
        return cuenta;
    }

    public void openDlgValoresParametrizables() {
        valoresMostrar = parametrizableService.findByOnlyClasificacion("E");
        PrimeFaces.current().executeScript("PF('dlgvaloresEgreso').show()");
        PrimeFaces.current().ajax().update(":formValoresEgreso");
    }

    public void openDlgValoresParametrizablesAnexo() {
//        Map<String, List<String>> params = new HashMap<>();
//        params.put("TIPO", Arrays.asList("EGRESO-ANE"));
//        params.put("anio", Arrays.asList(busqueda.getAnio() + ""));
//        Utils.openDialog("/facelet/talentoHumano/dialogValores", "850", "400", params);
        PrimeFaces.current().executeScript("PF('dlgvaloresAnexo').show()");
        PrimeFaces.current().ajax().update(":formValoresAnexo");
    }

    public void guardarValoresSeleccioandosEgreso() {
        System.out.println("lista de seccionados " + valoresSeleccionando.size());
        boolean saber = false;
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        if (Utils.isNotEmpty(valoresSeleccionando)) {
            for (ParametrosTalentoHumano parametroTem : valoresSeleccionando) {
                saber = false;
                if (!listaValoresRoles.isEmpty()) {
                    for (ValoresRoles item : listaValoresRoles) {
                        if ("OTROS_E".equals(parametroTem.getTipo().getCodigo())) {
                            saber = false;
                            break;
                        } else if (Objects.equals(parametroTem.getTipo(), item.getValorParametrizable().getTipo())) {
                            saber = true;
                            break;
                        }
                    }
                }
                if (saber) {
                    JsfUtil.addWarningMessage("Este Rubro de Egreso ya esta Registrado.", parametroTem.getNombre());
                } else {
                    valoresRol = new ValoresRoles();
                    valoresRol.setEstado(Boolean.TRUE);
                    valoresRol.setValorParametrizable(parametroTem);
                    valoresRol.setPeriodo(busqueda.getAnio());
                    valoresRol.setRolPago(auxRol);
                    valoresRol = valorRolesService.create(valoresRol);
                    valoresRol = new ValoresRoles();
                    listaValoresRoles = new ArrayList<>();
                    listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
                }
            }
            JsfUtil.addSuccessMessage("Información", "Rubro Registrado Correctamente.");
        }
        valoresSeleccionando = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('dlgvaloresEgreso').hide()");
        PrimeFaces.current().ajax().update(":formValoresEgreso");
        PrimeFaces.current().ajax().update(":frmServidor");
        PrimeFaces.current().ajax().update(":dtvaloresRubro");
    }

    public void selectDataRubrosAnexo(PartidasDistributivoAnexo da) {
        boolean saber = false;
        if (!listaValoresRoles.isEmpty()) {
            for (ValoresRoles item : listaValoresRoles) {
                if (Objects.equals(da.getDistributivoAnexo().getValorParametrizado().getTipo(), item.getValorParametrizable().getTipo())) {
                    saber = true;
                    break;
                }
            }
        }
        if (saber == true) {
            JsfUtil.addWarningMessage("Información", "Este Rubro de Egreso ya esta Registrado.");
            return;
        }
        valoresRol = new ValoresRoles();
        valoresRol.setEstado(Boolean.TRUE);
        valoresRol.setValorParametrizable(da.getDistributivoAnexo().getValorParametrizado());
        valoresRol.setEstructuraApR(da.getEstructuraApA());
        valoresRol.setFuenteApR(da.getFuenteApA());
        valoresRol.setItemApR(da.getItemApA());
        valoresRol.setFuenteDirectaR(da.getFuenteDirectaA());
        valoresRol.setPartidaAp(da.getPartidaAp());
        valoresRol.setCuentaContable(getCuenta(da.getItemApA()));
        valoresRol.setPeriodo(busqueda.getAnio());
        valoresRol.setRolPago(auxRol);
        valoresRol = valorRolesService.create(valoresRol);
        valoresRol = new ValoresRoles();
        listaValoresRoles = new ArrayList<>();
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        PrimeFaces.current().executeScript("PF('dlgvaloresAnexo').hide()");
        PrimeFaces.current().ajax().update(":panelServidor");
        JsfUtil.addSuccessMessage("Información", "Rubro Registrado Correctamente.");
    }

    public void selectDataRubros(SelectEvent evt) {
        boolean saber = false;
        ParametrosTalentoHumano parametroTem;
        parametroTem = (ParametrosTalentoHumano) evt.getObject();
        if (!listaValoresRoles.isEmpty()) {
            for (ValoresRoles item : listaValoresRoles) {
                if ("OTROS_E".equals(parametroTem.getTipo().getCodigo())) {
                    saber = false;
                    break;
                } else if (Objects.equals(parametroTem.getTipo(), item.getValorParametrizable().getTipo())) {
                    saber = true;
                    break;
                }
            }
        }
        if (saber == true) {
            JsfUtil.addWarningMessage("Información", "Este Rubro de Egreso ya esta Registrado.");
            return;
        }
        valoresRol = new ValoresRoles();
        valoresRol.setEstado(Boolean.TRUE);
        valoresRol.setValorParametrizable(parametroTem);
        valoresRol.setPeriodo(busqueda.getAnio());
        valoresRol.setRolPago(auxRol);
        valoresRol = valorRolesService.create(valoresRol);
        valoresRol = new ValoresRoles();
        listaValoresRoles = new ArrayList<>();
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        JsfUtil.addSuccessMessage("Información", "Rubro Registrado Correctamente.");

    }

    public void cellEdit(ValoresRoles v) {
        valorRolesService.edit(v);
    }

    public void EliminarValor(ValoresRoles v) {
        v.setEstado(Boolean.FALSE);
        valorRolesService.edit(v);
        listaValoresRoles = new ArrayList<>();
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);

    }

    public void cerrarDlg() {
        boolean cerrar = false;
        if (!listaValoresRoles.isEmpty()) {
            for (ValoresRoles item : listaValoresRoles) {
                if ("E".equals(item.getValorParametrizable().getClasificacion().getCodigo())) {
                    if (item.getCuentaContable() == null) {
                        cerrar = true;
                        break;
                    }

                }
            }
        }
        if (cerrar == true) {
            JsfUtil.addErrorMessage("Información", "Debe Asignarle Cuenta Contable a todos los Rubros de Egreso.");
        } else {
            PrimeFaces.current().executeScript("PF('dlgServidor').hide()");
            JsfUtil.addSuccessMessage("Información", "Rubros Asignados Correctamente.");
        }
    }

    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void setValoresRolesPartida(RolesDePago rol) {
        System.out.println("ENTRO vsetValoresRolesPartida");
        RolesDePago auxRol = new RolesDePago();
        auxRol = rol;
        Servidor servidorDialog = new Servidor();
        servidorDialog = auxRol.getServidor();
        List<PartidasDistributivo> lisNewPartidas = partidaService.showAllPartidaXDistributiv(servidorDialog.getDistributivo(), busqueda.getAnio());
        List<ValoresRoles> listValoresIngresos = valorRolesService.FindValoresIngresoRolesXRol(auxRol);
        List<ValoresRoles> listValoresEliminar = new ArrayList<>();
        for (ValoresRoles vr : listValoresIngresos) {
            boolean comprobar = false;
            for (PartidasDistributivo pd : lisNewPartidas) {
                if (vr.getValorParametrizable().getTipo().getCodigo().equals(pd.getDistributivo().getValoresParametrizados().getTipo().getCodigo())) {
                    if (vr.getPartidaAp().equals(pd.getPartidaAp())) {
                        comprobar = true;
                        break;
                    }
                }
            }
            if (comprobar == false) {
                listValoresEliminar.add(vr);
            }
        }
        if (!listValoresEliminar.isEmpty()) {
            for (ValoresRoles ValorEliminar : listValoresEliminar) {
                ValorEliminar.setEstado(Boolean.FALSE);
                valorRolesService.edit(ValorEliminar);
            }
        }
        List<PartidasDistributivo> partidaDistributivo = new ArrayList<>();
        partidaDistributivo = partidaService.showPartidaxDistributivoAndPeriodo(servidorDialog.getDistributivo(), busqueda.getAnio());
        if (partidaDistributivo != null) {
            for (PartidasDistributivo item : partidaDistributivo) {
                guardarValores(item);
            }
        }
        List<ValoresRoles> listaValoresRoles = valorRolesService.FindValoresRolesXRol(auxRol);
        if (!listaValoresRoles.isEmpty()) {
            for (ValoresRoles item : listaValoresRoles) {
                if (item.getCuentaContable() == null && item.getItemApR() != null) {
                    item.setCuentaContable(getCuenta(item.getItemApR()));
                    valorRolesService.edit(item);
                }
            }

        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public RolesDePago getRolesDePago() {
        return rolesDePago;
    }

    public void setRolesDePago(RolesDePago rolesDePago) {
        this.rolesDePago = rolesDePago;
    }

    public RolesDePago getRolesDePagoSeleccionado() {
        return rolesDePagoSeleccionado;
    }

    public void setRolesDePagoSeleccionado(RolesDePago rolesDePagoSeleccionado) {
        this.rolesDePagoSeleccionado = rolesDePagoSeleccionado;
    }

    public DistributivoEscala getEscala() {
        return escala;
    }

    public void setEscala(DistributivoEscala escala) {
        this.escala = escala;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public String getMesR() {
        return mesR;
    }

    public void setMesR(String mesR) {
        this.mesR = mesR;
    }

    public Servidor getServidorDialog() {
        return servidorDialog;
    }

    public void setServidorDialog(Servidor servidorDialog) {
        this.servidorDialog = servidorDialog;
    }

    public List<PartidasDistributivo> getPartidaDistributivo() {
        return partidaDistributivo;
    }

    public void setPartidaDistributivo(List<PartidasDistributivo> partidaDistributivo) {
        this.partidaDistributivo = partidaDistributivo;
    }

    public List<ValoresRoles> getListaValoresRoles() {
        return listaValoresRoles;
    }

    public void setListaValoresRoles(List<ValoresRoles> listaValoresRoles) {
        this.listaValoresRoles = listaValoresRoles;
    }

    public List<CuentaContable> getListCuenta() {
        return listCuenta;
    }

    public void setListCuenta(List<CuentaContable> listCuenta) {
        this.listCuenta = listCuenta;
    }

    public LazyModel<RolesDePago> getLazyRoles() {
        return lazyRoles;
    }

    public void setLazyRoles(LazyModel<RolesDePago> lazyRoles) {
        this.lazyRoles = lazyRoles;
    }
//</editor-fold>

    public List<PartidasDistributivoAnexo> getListAnexo() {
        return listAnexo;
    }

    public void setListAnexo(List<PartidasDistributivoAnexo> listAnexo) {
        this.listAnexo = listAnexo;
    }

    public List<ParametrosTalentoHumano> getValoresMostrar() {
        return valoresMostrar;
    }

    public void setValoresMostrar(List<ParametrosTalentoHumano> valoresMostrar) {
        this.valoresMostrar = valoresMostrar;
    }

    public List<ParametrosTalentoHumano> getValoresSeleccionando() {
        return valoresSeleccionando;
    }

    public void setValoresSeleccionando(List<ParametrosTalentoHumano> valoresSeleccionando) {
        this.valoresSeleccionando = valoresSeleccionando;
    }

}
