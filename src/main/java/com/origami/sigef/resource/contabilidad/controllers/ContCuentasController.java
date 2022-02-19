/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.conf.services.PlanCuentasService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaPartida;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contCuentasView")
@ViewScoped
public class ContCuentasController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PlanCuentasService confCuentasService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ContCuentaPartidaService contCuentaPartidaService;
    @Inject
    private PresCatalogoPresupuestarioService catalogoPresupuestoService;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;

    private ContCuentas contCuentas;
    private ContCuentaPartida contCuentaPartida;

    private List<PlanCuentas> confCuentasList;
    private List<CatalogoItem> clasificacionList;
    private List<PresCatalogoPresupuestario> partidasList;
    private List<PresCatalogoPresupuestario> partidasIngreso;
    private List<PresCatalogoPresupuestario> partidasEgreso;
    private List<PresCatalogoPresupuestario> partidasPresupuestoNivel;
    private List<PresCatalogoPresupuestario> partidasMovimiento;
    private List<ContSaldoInicial> contSaldoInicialList;

    private LazyModel<ContCuentas> contCuentasLazy;

    private boolean editView;

    // Parametros de reporte //
    private int pActivo;
    private int pMovimiento;
    private Boolean bNivel;
    private PlanCuentas pNivel;
    private Boolean bCodigo;
    private String pCodigo;
    //////////////////////////

    @PostConstruct
    public void init() {
        this.contCuentasLazy = new LazyModel<>(ContCuentas.class);
        this.contCuentasLazy.getSorteds().put("codigo", "ASC");
        this.contCuentasLazy.getFilterss().put("estado", true);
        confCuentasList = confCuentasService.getNivelesList(CONFIG.PLAN_CUENTA_CONTABLE, false);
        cleanForm();
        cargarPresupuesto();
    }

    public void cleanForm() {
        contCuentas = new ContCuentas();
        contCuentaPartida = new ContCuentaPartida();
        partidasList = new ArrayList<>();
        partidasMovimiento = new ArrayList<>();
        partidasMovimiento = new ArrayList<>();
        clasificacionList = catalogoService.getItemsByCatalogo("clasificacion_cuenta");
        editView = false;
    }

    private void cargarPresupuesto() {
        partidasIngreso = catalogoPresupuestoService.findTipoPresupuesto(true);
        partidasEgreso = catalogoPresupuestoService.findTipoPresupuesto(false);
        partidasPresupuestoNivel = catalogoPresupuestoService.findNivel(2);
    }

    public void invertirCuenta() {
        contCuentaPartida = new ContCuentaPartida();
        List<PresCatalogoPresupuestario> temp = partidasEgreso;
        partidasEgreso = partidasIngreso;
        partidasIngreso = temp;
        PrimeFaces.current().ajax().update("gridDebitoCredito");
    }

    public void form(ContCuentas cuenta, boolean accion) {
        cleanForm();
        editView = accion;
        if (cuenta != null) {
            this.contCuentas = cuenta;
            loadPartidasCuentas(this.contCuentas);
            if (cuenta.getPadre() != null && cuenta.getPagadoDevengado()) {
                recursividad(cuenta.getPadre());
            }
            JsfUtil.executeJS("PF('cuentaDlg').show()");
            PrimeFaces.current().ajax().update("cuentaForm");
        } else {
            PlanCuentas aux = confCuentasService.getFindNext(CONFIG.PLAN_CUENTA_CONTABLE, 1, false);
            if (aux == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
                return;
            }
            this.contCuentas = new ContCuentas();
            this.contCuentas.setConfId(aux);
            generarCodigo(null);
        }
    }

    public void insertPadre(ContCuentas padre) {
        cleanForm();
        contCuentaPartida = new ContCuentaPartida();
        PlanCuentas aux = confCuentasService.getFindNext(CONFIG.PLAN_CUENTA_CONTABLE, (padre.getConfId().getNivel() + 1), false);
        if (aux == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe agregar una nueva configuracion de nivel para el registro de una nueva cuenta contable");
            return;
        }
        this.contCuentas = new ContCuentas();
        this.contCuentas.setPadre(padre);
        this.contCuentas.setClasificacion(padre.getClasificacion());
        this.contCuentas.setConfId(aux);
        recursividad(padre);
        generarCodigo(padre);
    }

    private void recursividad(ContCuentas padre) {
        if (padre.getConfId().getNivel() >= 4) {
            if (padre.getCtaPagarCobrar()) {
                String codigo = contCuentaPartidaService.findCuentaPartidaCtaCobraPagar(padre);
                if (!codigo.equals("")) {
                    partidasMovimiento = catalogoPresupuestoService.findLikeCodigoPresupuesto(codigo);
                    if (!partidasMovimiento.isEmpty()) {
                        contCuentas.setPagadoDevengado(true);
                    }
                }
            } else {
                recursividad(padre.getPadre());
            }
        }
    }

    private void loadPartidasCuentas(ContCuentas cuenta) {
        if (cuenta.getConfId().getNivel() > 4 && cuenta.getMovimiento()) {
            recursividad(cuenta.getPadre());
        }
        List<ContCuentaPartida> aux = contCuentaPartidaService.findRelacionCuentaList(cuenta);
        partidasList = new ArrayList<>();
        if (!aux.isEmpty()) {
            if (aux.size() > 1) {
                for (ContCuentaPartida item : aux) {
                    partidasList.add(item.getIdPartida1());
                }
            } else {
                ContCuentaPartida temp = aux.get(0);
                if (temp.getIdCuenta().getPagadoDevengado()) {
                    partidasList.add(temp.getIdPartida1());
                } else {
                    contCuentaPartida = aux.get(0);
                }
            }
        }
        if (cuenta.getCtaInvertida()) {
            invertirCuenta();
        }
    }

    private void generarCodigo(ContCuentas padre) {
        String code = contCuentasService.getNextCode(contCuentas.getConfId(), padre);
        if (code.equals("1")) {
            if (contCuentas.getConfId().getNumDigito() > 1) {
                code = Utils.completarCadenaConCeros(code, contCuentas.getConfId().getNumDigito());
            }
            contCuentas.setCodIngreso(code);
            if (padre != null) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, contCuentas.getConfId().getNumDigito());
            }
            contCuentas.setCodigo(code);
        } else {
            contCuentas.setCodIngreso(code);
            if (contCuentas.getConfId().getNumDigito() > 1 && code.length() == 1) {
                code = padre.getCodigo() + Utils.completarCadenaConCeros(code, contCuentas.getConfId().getNumDigito());
            }
            this.contCuentas.setCodigo(code);
        }
        JsfUtil.executeJS("PF('cuentaDlg').show()");
        PrimeFaces.current().ajax().update("cuentaForm");
    }

    public void generarCodigo() {
        if (contCuentas.getPadre() != null) {
            if (contCuentas.getConfId().getNumDigito() > 1) {
                contCuentas.setCodIngreso(Utils.completarCadenaConCeros(contCuentas.returnIngreso(), contCuentas.getConfId().getNumDigito()));
            }
            contCuentas.setCodigo(contCuentas.getCodPadre().concat(contCuentas.returnIngreso()));
            if (contCuentas.getConfId().getSeparador()) {
                contCuentas.setCodigo(contCuentas.getCodigo().concat(contCuentas.getConfId().getCaracter()));
            }
        } else {
            contCuentas.setCodigo(contCuentas.getCodIngreso());
        }
    }

    public void save() {
        boolean edit = contCuentas.getId() != null;
        if (contCuentas.getConfId() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un nivel");
            return;
        }
        if (contCuentas.getClasificacion() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione una clasificacion");
            return;
        }
        if (contCuentas.getDescripcion() == null || contCuentas.getDescripcion().equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una descripcion");
            return;
        }
        if (contCuentas.getMovimiento()) {
            contCuentas.setDescripcion(contCuentas.getDescripcion().toUpperCase());
        }
        if (contCuentas.getConfId().getNumDigito() > contCuentas.returnIngreso().length()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No la cantidad de digitos ingresado en el codigo es menor las requeridas en el nivel no." + contCuentas.getConfId().getNivel());
            return;
        }
        if (contCuentas.getCodigo() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el codigo de la cuenta");
            return;
        }
        determinarValoresBoolean();
        if (edit) {
            if (!contCuentas.getActivo()) {
                contCuentas.setUserDesactivar(userSession.getNameUser());
                contCuentas.setDateDesactivar(new Date());
            }
            contCuentas.setUserModificacion(userSession.getNameUser());
            contCuentas.setDateModificacion(new Date());
            contCuentasService.edit(contCuentas);
            if (contCuentaPartida != null) {
                if (contCuentaPartida.getId() != null) {
                    if (contCuentaPartida.getIdPartida1() != null || contCuentaPartida.getIdPartida2() != null) {
                        contCuentaPartidaService.edit(contCuentaPartida);
                    } else {
                        contCuentaPartidaService.remove(contCuentaPartida);
                    }
                } else {
                    if (contCuentaPartida.getIdPartida1() != null || contCuentaPartida.getIdPartida2() != null) {
                        contCuentaPartida.setIdCuenta(contCuentas);
                        contCuentaPartidaService.create(contCuentaPartida);
                    }
                }
            }
            if (contCuentas.getPagadoDevengado()) {
                int cont = contCuentaPartidaService.deleteList(contCuentas);
                saveListPartidas();
            }
        } else {
            if (contCuentasService.findExiste(contCuentas.getCodigo())) {
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede registrat porque ya exite una cuenta contable con el codigo: " + contCuentas.getCodigo());
                return;
            }
            contCuentas.setUserCreacion(userSession.getNameUser());
            contCuentas.setDateCreacion(new Date());
            contCuentas = contCuentasService.create(contCuentas);
            //guardar relacion presupuestaria
            saveListPartidas();
            if (contCuentaPartida != null) {
                if (contCuentaPartida.getIdPartida1() != null || contCuentaPartida.getIdPartida2() != null) {
                    contCuentaPartida.setIdCuenta(contCuentas);
                    contCuentaPartidaService.create(contCuentaPartida);
                }
            }
        }
        JsfUtil.addSuccessMessage("INFO!!!", (edit ? "Editado" : " Registrado") + " con éxito");
        PrimeFaces.current().ajax().update("cuentaTable");
        closeForm();
    }

    private void saveListPartidas() {
        if (!partidasList.isEmpty()) {
            for (PresCatalogoPresupuestario item : partidasList) {
                ContCuentaPartida temp = new ContCuentaPartida(contCuentas, item);
                contCuentaPartidaService.create(temp);
            }
        }
    }

    public void determinarValoresBoolean() {
        if (contCuentas.getMovimiento()) {
            contCuentas.setCtaPagarCobrar(false);
            if (contCuentas.getPagadoDevengado()) {
                contCuentas.setCtaInvertida(false);
            } else {
                contCuentas.setPagadoDevengado(false);
            }
        } else {
            if (!contCuentas.getCtaPagarCobrar()) {
                contCuentas.setCtaInvertida(false);
                contCuentas.setPagadoDevengado(false);
            }
        }
    }

    public void closeForm() {
        cleanForm();
        JsfUtil.executeJS("PF('cuentaDlg').hide()");
    }

    public void openDlgReporte() {
        pActivo = 1;
        pMovimiento = 1;
        bNivel = true;
        bCodigo = true;
        pCodigo = "";
        pNivel = new PlanCuentas();
        JsfUtil.executeJS("PF('reporteDlg').show()");
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public void printReporte(String type) {
        if (!bCodigo) {
            if (pCodigo.equals("") || pCodigo == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar el codigo de busqueda");
                return;
            }
        }
        if (!bNivel) {
            if (pNivel == null || pNivel.getId() == null) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un nivel");
                return;
            }
            servletSession.addParametro("pNivel", pNivel.getId());
        } else {
            servletSession.addParametro("pNivel", null);
        }
        servletSession.addParametro("pActivo", pActivo);
        servletSession.addParametro("pMovimiento", pMovimiento);
        servletSession.addParametro("bNivel", bNivel);
        servletSession.addParametro("bCodigo", bCodigo);
        servletSession.addParametro("pCodigo", pCodigo + "%");
        servletSession.setNombreReporte("plan_cuentas");
        servletSession.setNombreSubCarpeta("_contabilidad");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        JsfUtil.executeJS("PF('reporteDlg').hide()");
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public void deleteCuenta(ContCuentas contCuentas) {
        if (contCuentasService.findHijos(contCuentas)) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se puede eliminar la cuenta contable, debido a las relaciones que tiene");
            return;
        }
        contCuentas.setEstado(Boolean.FALSE);
        contCuentasService.edit(contCuentas);
        PrimeFaces.current().ajax().update("cuentaTable");
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public void openDlgSaldoInicial(ContCuentas contCuentas) {
        OpcionBusqueda busqueda = new OpcionBusqueda();
        ContSaldoInicial temp = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(contCuentas, busqueda.getAnio());
        if (temp == null) {
            ContSaldoInicial aux = new ContSaldoInicial();
            aux.setIdCuenta(contCuentas);
            aux.setPeriodo(busqueda.getAnio());
            contSaldoInicialService.create(aux);
        }
        contSaldoInicialList = contSaldoInicialService.findByNamedQuery("ContSaldoInicial.findByIdCuenta", contCuentas);
        JsfUtil.executeJS("PF('saldoInicialDlg').show()");
        PrimeFaces.current().ajax().update("saldoInicialTable");
    }

    public void openDlgSaldoInicial() {
        if (!contSaldoInicialList.isEmpty()) {
            for (ContSaldoInicial temp : contSaldoInicialList) {
                contSaldoInicialService.edit(temp);
            }
        }
        JsfUtil.executeJS("PF('saldoInicialDlg').hide()");
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha registrado correctamente");
    }

    public ContCuentas getContCuentas() {
        return contCuentas;
    }

    public void setContCuentas(ContCuentas contCuentas) {
        this.contCuentas = contCuentas;
    }

    public List<PlanCuentas> getConfCuentasList() {
        return confCuentasList;
    }

    public void setConfCuentasList(List<PlanCuentas> confCuentasList) {
        this.confCuentasList = confCuentasList;
    }

    public LazyModel<ContCuentas> getContCuentasLazy() {
        return contCuentasLazy;
    }

    public void setContCuentasLazy(LazyModel<ContCuentas> contCuentasLazy) {
        this.contCuentasLazy = contCuentasLazy;
    }

    public List<CatalogoItem> getClasificacionList() {
        return clasificacionList;
    }

    public void setClasificacionList(List<CatalogoItem> clasificacionList) {
        this.clasificacionList = clasificacionList;
    }

    public ContCuentaPartida getContCuentaPartida() {
        return contCuentaPartida;
    }

    public void setContCuentaPartida(ContCuentaPartida contCuentaPartida) {
        this.contCuentaPartida = contCuentaPartida;
    }

    public List<PresCatalogoPresupuestario> getPartidasIngreso() {
        return partidasIngreso;
    }

    public void setPartidasIngreso(List<PresCatalogoPresupuestario> partidasIngreso) {
        this.partidasIngreso = partidasIngreso;
    }

    public List<PresCatalogoPresupuestario> getPartidasEgreso() {
        return partidasEgreso;
    }

    public void setPartidasEgreso(List<PresCatalogoPresupuestario> partidasEgreso) {
        this.partidasEgreso = partidasEgreso;
    }

    public boolean isEditView() {
        return editView;
    }

    public void setEditView(boolean editView) {
        this.editView = editView;
    }

    public List<PresCatalogoPresupuestario> getPartidasMovimiento() {
        return partidasMovimiento;
    }

    public void setPartidasMovimiento(List<PresCatalogoPresupuestario> partidasMovimiento) {
        this.partidasMovimiento = partidasMovimiento;
    }

    public List<PresCatalogoPresupuestario> getPartidasPresupuestoNivel() {
        return partidasPresupuestoNivel;
    }

    public void setPartidasPresupuestoNivel(List<PresCatalogoPresupuestario> partidasPresupuestoNivel) {
        this.partidasPresupuestoNivel = partidasPresupuestoNivel;
    }

    public List<PresCatalogoPresupuestario> getPartidasList() {
        return partidasList;
    }

    public void setPartidasList(List<PresCatalogoPresupuestario> partidasList) {
        this.partidasList = partidasList;
    }

    public int getpActivo() {
        return pActivo;
    }

    public void setpActivo(int pActivo) {
        this.pActivo = pActivo;
    }

    public int getpMovimiento() {
        return pMovimiento;
    }

    public void setpMovimiento(int pMovimiento) {
        this.pMovimiento = pMovimiento;
    }

    public Boolean getbNivel() {
        return bNivel;
    }

    public void setbNivel(Boolean bNivel) {
        this.bNivel = bNivel;
    }

    public PlanCuentas getpNivel() {
        return pNivel;
    }

    public void setpNivel(PlanCuentas pNivel) {
        this.pNivel = pNivel;
    }

    public Boolean getbCodigo() {
        return bCodigo;
    }

    public void setbCodigo(Boolean bCodigo) {
        this.bCodigo = bCodigo;
    }

    public String getpCodigo() {
        return pCodigo;
    }

    public void setpCodigo(String pCodigo) {
        this.pCodigo = pCodigo;
    }

    public List<ContSaldoInicial> getContSaldoInicialList() {
        return contSaldoInicialList;
    }

    public void setContSaldoInicialList(List<ContSaldoInicial> contSaldoInicialList) {
        this.contSaldoInicialList = contSaldoInicialList;
    }

}
