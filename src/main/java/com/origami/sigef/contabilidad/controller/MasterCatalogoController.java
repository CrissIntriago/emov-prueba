/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import com.origami.sigef.contabilidad.service.ControlPresupuestarioService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.CuentaContablecatalogoPresupuestoService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.tesoreria.service.ItemTarifarioService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "masterCatalogoView")
@ViewScoped
public class MasterCatalogoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CuentaContablecatalogoPresupuestoService contable_Presupuesto_Service;
    @Inject
    private DetalleItemService detalleItemService;
    @Inject
    private ControlPresupuestarioService controlPresupuestarioService;
    @Inject
    private ControlCuentaContableService controlCuentaService;
    @Inject
    private ItemTarifarioService itemTarifarioService;

    private CuentaContablecatalogoPresupuesto contable_Presupuesto;
    private MasterCatalogo masterCatalogoSeleccionado;
    private List<MasterCatalogo> periodos;

    private LazyModel<MasterCatalogo> lazy;
    private List<Integer> cbAnio = new ArrayList<>();
    private MasterCatalogo masterCatalogo;
    List<CatalogoItem> listadoCuentas;
    private short delPeriodo = 0;
    private Boolean copiar;
    private Date vigenciaDesde;
    private Date vigenciaHasta;

    @PostConstruct
    public void inicializar() {
        contable_Presupuesto = new CuentaContablecatalogoPresupuesto();
        this.masterCatalogo = new MasterCatalogo();
        this.masterCatalogoSeleccionado = new MasterCatalogo();
        this.lazy = new LazyModel<>(MasterCatalogo.class);
        lazy.getSorteds().put("anio", "DESC");
        this.listadoCuentas = catalogoService.getMuestraTipo("tipo_cuenta", Arrays.asList("CC", "CP", "PA", "D"));
        anioActual();
        copiar = Boolean.FALSE;
    }

    /**
     * setear el anio actual a un atributi de mater catalogo
     */
    public void anioActual() {
        Calendar fecha = new GregorianCalendar();
        int anioActual = fecha.get(Calendar.YEAR);
        String s = String.valueOf(anioActual);
        masterCatalogo.setAnio(Short.parseShort(s));
    }

    /**
     * guardar en un cabecera de fechas para el distributivo, PAPP, cuenta
     * contable, cuenta presupuestaria
     *
     */
    public void guardar() {

        boolean edit = masterCatalogo.getId() != null;

        if (!edit) {
            if (copiar && delPeriodo != 0) {
                copiarCatalogo(masterCatalogo);
            } else {
                guardarMasterCatalogo(masterCatalogo);
            }
        } else {
            masterCatalogo.setUsuarioModifica(userSession.getNameUser());
            masterCatalogo.setFechaModificacion(new Date());
            masterCatalogoService.edit(masterCatalogo);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Información", masterCatalogo.getNombre() + " editado con éxito.");
            resetValues();
        }
    }

    public void editar(MasterCatalogo c) {
        masterCatalogo = c;
    }

    public void eliminar(MasterCatalogo c) {
        if (c.getTipo().getCodigo().equals("CC")) {
            List<CuentaContable> resultCuenta = masterCatalogoService.getExisteCatalogoCuenta(c.getAnio());
            if (!resultCuenta.isEmpty()) {
                JsfUtil.addInformationMessage("Información", "No se puede eliminar " + c.getTipo().getTexto() + " por que tiene cuentas registradas.");
            } else {
                deleteCatalogo(c);
            }
        } else {
            List<CatalogoPresupuesto> resultPresupuesto = masterCatalogoService.getExisteCatalogoPresupuesto(c.getAnio());
            if (!resultPresupuesto.isEmpty()) {
                JsfUtil.addInformationMessage("Información", "No se puede eliminar " + c.getTipo().getTexto() + " por que tiene cuentas registradas.");
            } else {
                deleteCatalogo(c);
            }
        }
        PrimeFaces.current().ajax().update("messages");
    }

    public void deleteCatalogo(MasterCatalogo c) {
        masterCatalogoService.remove(c);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Eliminado con éxito.");
    }

    public void updatePeriodo() {

        if (masterCatalogo != null) {
            if (masterCatalogo.getTipo() != null) {
                periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", masterCatalogo.getTipo().getCodigo()});
            }
        } else {
            resetValues();
        }
    }

    public void guardarMasterCatalogo(MasterCatalogo masterCatalago) {
        List<MasterCatalogo> resultMaster = masterCatalogoService.getMasterCatalogoExiste(masterCatalago);
        if (resultMaster.isEmpty()) {
            masterCatalago.setFechaCreacion(new Date());
            masterCatalago.setUsuarioCreacion(userSession.getNameUser());
            this.masterCatalogo = masterCatalogoService.create(masterCatalago);
            JsfUtil.addSuccessMessage("Master Catálogo", masterCatalogo.getTipo().getTexto() + " guardado con éxito.");
            resetValues();
        } else {
            executeMessagesExisteCatalogo(masterCatalago);
        }
        PrimeFaces.current().ajax().update("messages");
    }

    /**
     * copia los datos de un anio anterio al anio que se requiera
     *
     * @param masterCatalogo
     */
    public void copiarCatalogo(MasterCatalogo masterCatalogo) {
        try {
            int executeUpdate;
            int executeUpdateConrolCta;
            if (masterCatalogo.getTipo().getCodigo().equals("CC")) {
                List<CuentaContable> resultCuenta = masterCatalogoService.getExisteCatalogoCuenta(masterCatalogo.getAnio());
                List<CatalogoPresupuesto> resultPresupuesto = masterCatalogoService.getExisteCatalogoPresupuesto(masterCatalogo.getAnio());
                if (resultPresupuesto.isEmpty()) {
                    JsfUtil.addSuccessMessage("Master Catálogo", "Necesita Crear Items Presupuestario Para generar una Copia de " + masterCatalogo.getTipo().getTexto());
                    PrimeFaces.current().ajax().update("messages");
                    return;
                }
                if (!resultCuenta.isEmpty()) {
                    executeMessagesExisteCatalogo(masterCatalogo);
                } else {
                    if (controlCuentaService.getvalidarCopiaContable(delPeriodo)) {
                        JsfUtil.addErrorMessage("Master Catálogo", "Cuenta Contable del período " + delPeriodo + " faltan por cerrar periodos.");
                        return;
                    }
                    executeUpdate = masterCatalogoService.getCopiaCuenta(masterCatalogo, delPeriodo);
                    executeUpdateConrolCta = masterCatalogoService.getCopiaControlCuenta(masterCatalogo, delPeriodo);

                    if (executeUpdate > 0 && executeUpdateConrolCta > 0) {
                        buscarPadreCuenta();
                        buscarDevengadoCobrado();
                        buscarCuentaContableCatalogoPresupuesto();
                        CopiarDetalleItem();
                        JsfUtil.addSuccessMessage("Master Catálogo", " Copia de " + masterCatalogo.getTipo().getTexto() + " del período " + delPeriodo + " Al período " + masterCatalogo.getAnio() + " generado con éxito.");
                        guardarMasterCatalogo(masterCatalogo);
                    } else {
                        JsfUtil.addErrorMessage("Master Catálogo", " No existe Cuentas Registradas del Período " + delPeriodo + " En " + masterCatalogo.getTipo().getTexto());
                        return;
                    }
                    PrimeFaces.current().ajax().update("messages");
                }
            } else if (masterCatalogo.getTipo().getCodigo().equals("CP")) {
                List<CatalogoPresupuesto> resultPresupuesto = masterCatalogoService.getExisteCatalogoPresupuesto(masterCatalogo.getAnio());
                if (!resultPresupuesto.isEmpty()) {
                    executeMessagesExisteCatalogo(masterCatalogo);
                } else {
//                    if(controlPresupuestarioService.getvalidarCopiaPersupuestaria(delPeriodo)){
//                        JsfUtil.addErrorMessage("Master Catálogo", "Catalogo del período "+delPeriodo+" faltan por cerrar periodos.");
//                        return;
//                    }
                    executeUpdate = masterCatalogoService.getCopiaCuenta(masterCatalogo, delPeriodo);
                    executeUpdateConrolCta = masterCatalogoService.getCopiaControlPresupuestario(masterCatalogo, delPeriodo);
                    if (executeUpdate > 0 && executeUpdateConrolCta > 0) {
                        buscarPadrePresupuesto();
                        JsfUtil.addSuccessMessage("Master Catálogo", " Copia de " + masterCatalogo.getTipo().getTexto() + " del período " + delPeriodo + " Al período " + masterCatalogo.getAnio() + " generado con éxito.");
                        guardarMasterCatalogo(masterCatalogo);
                    } else {
                        JsfUtil.addErrorMessage("Master Catálogo", " No existe Cuentas Registradas del Período " + delPeriodo + " En " + masterCatalogo.getTipo().getTexto());
                        PrimeFaces.current().ajax().update("messages");
                    }
                }
            } else {
                List<ValoresDistributivo> resultRubros = masterCatalogoService.getExistenciaDistributivosRubros(masterCatalogo.getAnio());
                if (!resultRubros.isEmpty()) {
                    executeMessagesExisteCatalogo(masterCatalogo);

                } else {
                    executeUpdate = masterCatalogoService.getCopiaCuenta(masterCatalogo, delPeriodo);
                    if (executeUpdate > 0) {
                        JsfUtil.addSuccessMessage("Master Catálogo", " Copia de " + masterCatalogo.getTipo().getTexto() + " del período " + delPeriodo + " Al período " + masterCatalogo.getAnio() + " generado con éxito.");
                        int copiaDistributivoEscala = masterCatalogoService.getCopiaDistributivoEscala(masterCatalogo, delPeriodo);
                        if (copiaDistributivoEscala > 0) {
                            JsfUtil.addSuccessMessage("Datos Escala Salarial Acualizado con Exito...", "");
                        }
                        guardarMasterCatalogo(masterCatalogo);
                    } else {
                        JsfUtil.addErrorMessage("Master Catálogo", " No existe Rubros Registradas del Período " + delPeriodo + " En " + masterCatalogo.getTipo().getTexto());
                        PrimeFaces.current().ajax().update("messages");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearDistributivoEscala() {

    }

    public void CopiarDetalleItem() {
        int executeUpdate = masterCatalogoService.copiaDetalleItem(masterCatalogo.getAnio(), delPeriodo);
        if (executeUpdate > 0) {
            List<DetalleItem> detallesOld = masterCatalogoService.getDetallesItemByPeriodo(delPeriodo);
            List<DetalleItem> detallesNew = masterCatalogoService.getDetallesItemByPeriodo(masterCatalogo.getAnio());
            editDetalleItem(detallesOld, detallesNew);
        } else {
            return;
        }
        JsfUtil.addSuccessMessage("Master Catálogo", " Copia de Detalle Item generado con éxito.");
    }

    public void editDetalleItem(List<DetalleItem> detallesOld, List<DetalleItem> detallesNew) {
        for (DetalleItem dNew : detallesNew) {
            for (DetalleItem dOld : detallesOld) {
                if (dNew.getCodigo().equals(dOld.getCodigo())) {
                    if (dOld.getTipoGasto() != null) {
                        ContCuentas cuenta_tipoGastos = masterCatalogoService.getContCuentas(dOld.getTipoGasto().getCodigo());
                        dNew.setTipoGasto(cuenta_tipoGastos);
                    }
                    if (dOld.getCuentaContable() != null) {
                        ContCuentas cuenta = masterCatalogoService.getContCuentas(dOld.getCuentaContable().getCodigo());
                        dNew.setCuentaContable(cuenta);
                    }
                    if (dNew.getCuentaContable() != null || dNew.getTipoGasto() != null) {
                        detalleItemService.edit(dNew);
                    }
                }
            }
        }
    }

    public void buscarDevengadoCobrado() {
        List<CuentaContable> resultDevengado = masterCatalogoService.getDebitoCreditoCobrado(delPeriodo);
        List<CuentaContable> resultCuenta = masterCatalogoService.getExisteCatalogoCuenta(masterCatalogo.getAnio());
        if (!resultDevengado.isEmpty() && !resultCuenta.isEmpty()) {
            for (CuentaContable cc : resultCuenta) {
                for (CuentaContable dc : resultDevengado) {
                    if (cc.getCodigo().equals(dc.getCodigo())) {
                        cc = buscarCatalogoPresupuesto(dc, cc);
                    }
                }
                cuentaContableService.edit(cc);
            }
        }
    }

    public CuentaContable buscarCatalogoPresupuesto(CuentaContable pdt, CuentaContable cc) {
        if (pdt.getDebito() != null) {
            CatalogoPresupuesto debito = masterCatalogoService.getPresupuesto(masterCatalogo.getAnio(), pdt.getDebito().getCodigo());
            cc.setDebito(debito);
        }
        if (pdt.getCredito() != null) {
            CatalogoPresupuesto credito = masterCatalogoService.getPresupuesto(masterCatalogo.getAnio(), pdt.getCredito().getCodigo());
            cc.setCredito(credito);
        }
        if (pdt.getCobradoDevengado() != null) {
            CatalogoPresupuesto cobrado_devengado = masterCatalogoService.getPresupuesto(masterCatalogo.getAnio(), pdt.getCobradoDevengado().getCodigo());
            cc.setCobradoDevengado(cobrado_devengado);
        }
        return cc;
    }

    public void buscarCuentaContableCatalogoPresupuesto() {
        List<CuentaContablecatalogoPresupuesto> resultCuentaPresupuesto = masterCatalogoService.getCuentaContableCatalogoPresupuesto(delPeriodo);
        if (!resultCuentaPresupuesto.isEmpty()) {
            for (CuentaContablecatalogoPresupuesto ccp : resultCuentaPresupuesto) {
                CuentaContable c = masterCatalogoService.getCuentaContable(masterCatalogo.getAnio(), ccp.getCuentaContable().getCodigo());
                CatalogoPresupuesto cp = masterCatalogoService.getPresupuesto(masterCatalogo.getAnio(), ccp.getCatalogoPresupuesto().getCodigo());
                if (c != null && cp != null) {
                    c.setCtaPagarCobrar(Boolean.TRUE);
                    cuentaContableService.edit(c);
                    contable_Presupuesto.setCuentaContable(c);
                    contable_Presupuesto.setCatalogoPresupuesto(cp);
                    contable_Presupuesto = contable_Presupuesto_Service.create(contable_Presupuesto);
                    contable_Presupuesto = new CuentaContablecatalogoPresupuesto();
                }
            }
        }
    }

    public void buscarPadreCuenta() {
        List<CuentaContable> resultCuenta = masterCatalogoService.getExisteCatalogoCuenta(masterCatalogo.getAnio());
        if (!resultCuenta.isEmpty()) {
            for (CuentaContable cuenta : resultCuenta) {
                String cadena;
                if (cuenta.getNivel().getOrden() == 2) {
                    cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    setPadre(cuenta, cadena);
                }
                if (cuenta.getNivel().getOrden() == 3) {
                    cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    setPadre(cuenta, cadena);
                }
                if (cuenta.getNivel().getOrden() == 4) {
                    cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    setPadre(cuenta, cadena);
                }
                if (cuenta.getNivel().getOrden() == 5) {
                    if (cuenta.getCuentaNivel1().toString().length() == 1) {
                        cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    } else {
                        cadena = Short.toString(cuenta.getTitulo());
                        cadena = cadena + Short.toString(cuenta.getGrupo());
                        cadena = cadena + Short.toString(cuenta.getSubGrupo());
                        cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                    }
                    setPadre(cuenta, cadena);
                }
                if (cuenta.getNivel().getOrden() == 6) {
                    if (cuenta.getCuentaNivel2().toString().length() == 1) {
                        cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    } else {
                        cadena = Short.toString(cuenta.getTitulo());
                        cadena = cadena + Short.toString(cuenta.getGrupo());
                        cadena = cadena + Short.toString(cuenta.getSubGrupo());
                        cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                        cadena = cadena + Short.toString(cuenta.getCuentaNivel2());
                    }
                    setPadre(cuenta, cadena);
                }
                if (cuenta.getNivel().getOrden() == 7) {
                    cadena = getCadenaCuenta(cuenta, cuenta.getNivel().getOrden());
                    setPadre(cuenta, cadena);
                }
            }
        }
    }

    public void setPadre(CuentaContable cuenta, String cadena) {
        CuentaContable padre = masterCatalogoService.getCuentaContable(cuenta.getPeriodo(), cadena);
        if (padre != null) {
            cuenta.setPadre(padre);
            cuentaContableService.edit(cuenta);
        }
    }

    public String getCadenaCuenta(CuentaContable cuenta, short nivel) {
        String cadena = "";
        switch (nivel) {
            case 2:
                cadena = Short.toString(cuenta.getTitulo());
                break;
            case 3:
                cadena = Short.toString(cuenta.getTitulo());
                cadena = cadena + Short.toString(cuenta.getGrupo());
                break;
            case 4:
                cadena = Short.toString(cuenta.getTitulo());
                cadena = cadena + Short.toString(cuenta.getGrupo());
                cadena = cadena + Short.toString(cuenta.getSubGrupo());
                break;
            case 5:
                cadena = Short.toString(cuenta.getTitulo());
                cadena = cadena + Short.toString(cuenta.getGrupo());
                cadena = cadena + Short.toString(cuenta.getSubGrupo());
                cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel1());
                break;
            case 6:
                cadena = Short.toString(cuenta.getTitulo());
                cadena = cadena + Short.toString(cuenta.getGrupo());
                cadena = cadena + Short.toString(cuenta.getSubGrupo());
                if (cuenta.getCuentaNivel1().toString().length() == 1) {
                    cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel1());
                } else {
                    cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                }
                cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel2());
                break;
            case 7:
                switch (cuenta.getCuentaNivel3().toString().length()) {
                    case 2:
                        cadena = Short.toString(cuenta.getTitulo());
                        cadena = cadena + Short.toString(cuenta.getGrupo());
                        cadena = cadena + Short.toString(cuenta.getSubGrupo());
                        if (cuenta.getCuentaNivel1().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel1());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                        }
                        if (cuenta.getCuentaNivel2().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel2());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel2());
                        }
                        cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel3());
                        break;
                    case 1:
                        cadena = Short.toString(cuenta.getTitulo());
                        cadena = cadena + Short.toString(cuenta.getGrupo());
                        cadena = cadena + Short.toString(cuenta.getSubGrupo());
                        if (cuenta.getCuentaNivel1().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel1());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                        }
                        if (cuenta.getCuentaNivel2().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel2());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel2());
                        }
                        cadena = cadena + "00" + Short.toString(cuenta.getCuentaNivel3());
                        break;
                    default:
                        cadena = Short.toString(cuenta.getTitulo());
                        cadena = cadena + Short.toString(cuenta.getGrupo());
                        cadena = cadena + Short.toString(cuenta.getSubGrupo());
                        if (cuenta.getCuentaNivel1().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel1());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel1());
                        }
                        if (cuenta.getCuentaNivel2().toString().length() == 1) {
                            cadena = cadena + "0" + Short.toString(cuenta.getCuentaNivel2());
                        } else {
                            cadena = cadena + Short.toString(cuenta.getCuentaNivel2());
                        }
                        cadena = cadena + Short.toString(cuenta.getCuentaNivel3());
                        break;
                }
                break;
        }
        return cadena;
    }

    public void buscarPadrePresupuesto() {
        List<CatalogoPresupuesto> resultPresupuesto = masterCatalogoService.getExisteCatalogoPresupuesto(masterCatalogo.getAnio());
        if (!resultPresupuesto.isEmpty()) {
            for (CatalogoPresupuesto catalogo : resultPresupuesto) {
                String cadena;
                if (catalogo.getNivel().getOrden() == 2) {
                    cadena = getCadenaPresupuesto(catalogo, catalogo.getNivel().getOrden());
                    setPadrePresupuesto(catalogo, cadena);
                }
                if (catalogo.getNivel().getOrden() == 3) {
                    cadena = getCadenaPresupuesto(catalogo, catalogo.getNivel().getOrden());
                    setPadrePresupuesto(catalogo, cadena);
                }
                if (catalogo.getNivel().getOrden() == 4) {
                    if (catalogo.getSubGrupo().toString().length() == 1) {
                        cadena = getCadenaPresupuesto(catalogo, catalogo.getNivel().getOrden());
                    } else {
                        cadena = Short.toString(catalogo.getTitulo());
                        cadena = cadena + Short.toString(catalogo.getNaturaleza());
                        cadena = cadena + Short.toString(catalogo.getSubGrupo());
                    }
                    setPadrePresupuesto(catalogo, cadena);
                }
            }
        }
    }

    public String getCadenaPresupuesto(CatalogoPresupuesto catalogo, short nivel) {
        String cadena = "";
        switch (nivel) {
            case 2:
                cadena = Short.toString(catalogo.getTitulo());
                break;
            case 3:
                cadena = Short.toString(catalogo.getTitulo());
                cadena = cadena + Short.toString(catalogo.getNaturaleza());
                break;
            case 4:
                cadena = Short.toString(catalogo.getTitulo());
                cadena = cadena + Short.toString(catalogo.getNaturaleza());
                cadena = cadena + "0" + Short.toString(catalogo.getSubGrupo());
                break;
        }
        return cadena;
    }

    public void setPadrePresupuesto(CatalogoPresupuesto catalogo, String cadena) {
        CatalogoPresupuesto padre = masterCatalogoService.getPresupuesto(catalogo.getAnio(), cadena);
        if (padre != null) {
            catalogo.setPadre(padre);
            catalogoPresupuestoService.edit(catalogo);
        }
    }

    public void executeMessagesExisteCatalogo(MasterCatalogo masterCatalogo) {
        JsfUtil.addErrorMessage("Master Catálogo", " Ya existe " + masterCatalogo.getTipo().getTexto() + " del período " + masterCatalogo.getAnio() + " registrados.");
        PrimeFaces.current().ajax().update("messages");
    }

    public void resetValues() {
        masterCatalogo = new MasterCatalogo();
        periodos = new ArrayList<>();
        delPeriodo = 0;
        setCopiar(Boolean.FALSE);
        anioActual();
    }

    public boolean renderElementForm() {
        if (masterCatalogo != null) {
            Boolean edit = masterCatalogo.getId() != null;
            if (edit) {
                return false;
            }
            if (masterCatalogo.getTipo() != null) {
                if (masterCatalogo.getTipo().getCodigo().equals("CC") || masterCatalogo.getTipo().getCodigo().equals("CP") || masterCatalogo.getTipo().getCodigo().equals("D")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String redireccionar(MasterCatalogo master) {
        switch (master.getTipo().getCodigo()) {
            case "CC":
                return "/facelet/contabilidad/cuentaContable/cuenta.xhtml?faces-redirect=true";
            case "CP":
                return "/facelet/contabilidad/planPresupuesto/catalogoPresupuesto.xhtml?faces-redirect=true";
            case "D":
                return "/facelet/talentoHumano/distributivo.xhtml?faces-redirect=true";
            default:
                return "/facelet/contabilidad/planAnual/actividadOperativa.xhtml?faces-redirect=true";
        }
    }

    public Boolean readOnly() {
        Boolean edit = masterCatalogo.getId() != null;
        return edit;
    }

    public Boolean disableCopy() {
        return copiar;
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public LazyModel<MasterCatalogo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<MasterCatalogo> lazy) {
        this.lazy = lazy;
    }

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public List<Integer> getCbAnio() {
        return cbAnio;
    }

    public void setCbAnio(List<Integer> cbAnio) {
        this.cbAnio = cbAnio;
    }

    public MasterCatalogo getMasterCatalogo() {
        return masterCatalogo;
    }

    public void setMasterCatalogo(MasterCatalogo masterCatalogo) {
        this.masterCatalogo = masterCatalogo;
    }

    public List<CatalogoItem> getListadoCuentas() {
        return listadoCuentas;
    }

    public void setListadoCuentas(List<CatalogoItem> listadoCuentas) {
        this.listadoCuentas = listadoCuentas;
    }

    public MasterCatalogo getMasterCatalogoSeleccionado() {
        return masterCatalogoSeleccionado;
    }

    public void setMasterCatalogoSeleccionado(MasterCatalogo masterCatalogoSeleccionado) {
        this.masterCatalogoSeleccionado = masterCatalogoSeleccionado;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public short getDelPeriodo() {
        return delPeriodo;
    }

    public void setDelPeriodo(short delPeriodo) {
        this.delPeriodo = delPeriodo;
    }

    public Boolean getCopiar() {
        return copiar;
    }

    public void setCopiar(Boolean copiar) {
        this.copiar = copiar;
    }

    public CuentaContablecatalogoPresupuesto getContable_Presupuesto() {
        return contable_Presupuesto;
    }

    public void setContable_Presupuesto(CuentaContablecatalogoPresupuesto contable_Presupuesto) {
        this.contable_Presupuesto = contable_Presupuesto;
    }

//</editor-fold>
}
