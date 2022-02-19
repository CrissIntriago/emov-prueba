/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuentaContablecatalogoPresupuesto;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.CuentaContableLazy;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.CuentaContablecatalogoPresupuestoService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author Dairon Freddy
 */
@Named(value = "cuentaContableView")
@ViewScoped
public class CuentaContableController implements Serializable {

    /**
     *
     */
    @Inject
    private UserSession userSession;

    private static final long serialVersionUID = 1L;
    @Inject
    private CuentaContableService cuentaContableService;
    @Inject
    private CuentaContablecatalogoPresupuestoService cuentaCatalogoService;
    @Inject
    private NivelService nivelService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ServletSession ss;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;

    private OpcionBusqueda opcionBusqueda;
    private CuentaContableLazy lazy;
    private CuentaContable cuenta;
    private CuentaContable cuentaSeleccionada;
    private List<Nivel> niveles;
    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> clasificaciones;
    private List<CatalogoPresupuesto> credito;
    private List<CatalogoPresupuesto> debito;
    private List<CatalogoPresupuesto> listCobradoDevengado;
    private List<CatalogoPresupuesto> listHijosCobadoDevengado;
    private List<CatalogoPresupuesto> catalogosSelecionandos;
    private List<CuentaContablecatalogoPresupuesto> list2;
    private List<CuentaContablecatalogoPresupuesto> list3;
    private CuentaContablecatalogoPresupuesto cuenta_Catalogo;
    private Boolean invertir;
    private int cantColumnas;
    private String columnClass;
    private String impr;
    private List<CuentaContablecatalogoPresupuesto> listaAsociadasPresupuesto;
    private boolean invertir2;
    private boolean btnDisable;
    private CuentaContable cuentaVista;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        this.invertir = false;
        catalogosSelecionandos = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        listHijosCobadoDevengado = new ArrayList<>();
        niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"tipo_cuenta", "CC"});
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        lazy = new CuentaContableLazy(opcionBusqueda);
        lazy.getFilterss().put("estado", true);
        cuenta = new CuentaContable();
        clasificaciones = catalogoService.getItemsByCatalogo("clasificacion_cuenta");
        cantColumnas = 4;
        columnClass = "ui-grid-col-3";
        listaAsociadasPresupuesto = new ArrayList<>();
        cuentaVista = new CuentaContable();
    }

    public void buscar() {
        lazy = new CuentaContableLazy(opcionBusqueda);
    }

    public void cancelar() {
        lazy = new CuentaContableLazy(opcionBusqueda);
    }

    public void form(CuentaContable c, boolean edit) {
        if (c != null) {
            if (c.getMovimiento() && !edit) {
                JsfUtil.addWarningMessage("Aviso!!!", "No se puede agregar una cuenta porque es de tipo movimiento");
                return;
            }
        }
        this.cuenta = new CuentaContable();
        this.list3 = new ArrayList<>();
        if (edit) {
            editListCobradoDevengado(c);
            if (c.getCobradoDevengado() != null) {
                this.invertir = true;
            }
            cuenta = c;
        } else {
            cuenta.setPadre(c);
            cuenta.setClasificacion(c.getClasificacion());
            cuenta.setNivel(nivelService.getProximoNivel(c.getNivel()));
            cuenta.setPeriodo(c.getPeriodo());
            invertirCuenta();
            btnDisable = c.getMovimiento();
            switch (c.getNivel().getOrden()) {
                case 1:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 2:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(c.getGrupo());
                    cuenta.setSubGrupo(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 3:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(c.getGrupo());
                    cuenta.setSubGrupo(c.getSubGrupo());
                    cuenta.setCuentaNivel1(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 4:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(c.getGrupo());
                    cuenta.setSubGrupo(c.getSubGrupo());
                    cuenta.setCuentaNivel1(c.getCuentaNivel1());
                    cuenta.setCuentaNivel2(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 5:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(c.getGrupo());
                    cuenta.setSubGrupo(c.getSubGrupo());
                    cuenta.setCuentaNivel1(c.getCuentaNivel1());
                    cuenta.setCuentaNivel2(c.getCuentaNivel2());
                    cuenta.setCuentaNivel3(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 6:
                    cuenta.setTitulo(c.getTitulo());
                    cuenta.setGrupo(c.getGrupo());
                    cuenta.setSubGrupo(c.getSubGrupo());
                    cuenta.setCuentaNivel1(c.getCuentaNivel1());
                    cuenta.setCuentaNivel2(c.getCuentaNivel2());
                    cuenta.setCuentaNivel3(c.getCuentaNivel3());
                    cuenta.setCuentaNivel4(cuentaContableService.getMaxValueForChild(c, false));
                    break;
            }
        }
        actulizarInfoGridFom();
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update("formCuenta");
    }

    public void formAdd() {
        cuenta = new CuentaContable();
        cuenta.setNivel(nivelService.getFirstNivel("tipo_cuenta", "CC"));
        cuenta.setPeriodo(opcionBusqueda.getAnio());
        cuenta.setTitulo(cuentaContableService.getMaxValueForChild(cuenta, true));

        actulizarInfoGridFom();
        PrimeFaces.current().executeScript("PF('cuentaDialog').show()");
        PrimeFaces.current().ajax().update("formCuenta");
    }

    public void guardar() {
        boolean edit = cuenta.getId() != null;
        cuenta.setCodigo(generarCodigo(cuenta));
        CuentaContable existeCuenta = cuentaContableService.existeCuenta(cuenta);
        if (cuenta.getId() == null && existeCuenta != null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Cuenta Contable", cuenta.getCodigo() + " se ecuentra registrada en el sistema.");
            return;
        }

        if (cuenta.getPadre() != null && cuenta.getPadre().getMovimiento() == true) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Cuenta Contable", cuenta.getCodigo() + "No puede Agregar SubCuenta en Cuentas de Movimiento");
            return;
        }

        if (cuenta.getMovimiento() == false) {
            String toUpperCase = cuenta.getNombre().toUpperCase();
            cuenta.setNombre(toUpperCase);
        }

        if (cuenta.getId() == null) {
            cuenta.setUsuarioCreacion(userSession.getNameUser());
            cuenta.setFechaCreacion(new Date());
            cuentaSeleccionada = cuentaContableService.create(cuenta);

        } else {
            cuenta.setUsuarioModifica(userSession.getNameUser());
            cuenta.setFechaModificacion(new Date());
            cuentaContableService.edit(cuenta);
        }
        cuentaCatalogo(edit);
        actualizarLists();
        PrimeFaces.current().executeScript("PF('cuentaDialog').hide()");
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("formMain");
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addSuccessMessage("Cuenta Contable", cuenta.getCodigo() + (edit ? " editada" : " registrada") + " con éxito.");
    }

    public void eliminar(CuentaContable c) {
        List<CuentaContable> hijos = cuentaContableService.getHijosByPadre(c);
        list3 = cuentaCatalogoService.getCatalogoPresupuesto(c.getId());
        if (hijos != null) {
            if (!hijos.isEmpty()) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addErrorMessage("Cuenta Contable", c.getCodigo() + " tiene cuentas de movimientos asociadas.");
                return;
            }
        }
        if (c.getCredito() != null || c.getDebito() != null || c.getCobradoDevengado() != null) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Cuenta Contable", c.getCodigo() + " tiene Item Presupuestario asociado.");
            return;
        }
        if (!list3.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Cuenta Contable", c.getCodigo() + " tiene Item Presupuestario asociado.");
            return;
        }
        if (cuentaContableService.getDiarioGeneral(c)) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Cuenta Contable", c.getCodigo() + " esta siendo utilizado");
            return;
        }
        JsfUtil.addSuccessMessage("Cuenta Contable", c.getCodigo() + " eliminada con éxito");
        c.setEstado(Boolean.FALSE);
        cuentaContableService.edit(c);
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("messages");
    }

    public void handleCloseForm(CloseEvent event) {
        actualizarLists();

        PrimeFaces.current().ajax().update("mostrarColumnas");
        PrimeFaces.current().ajax().update("cuentas");
        PrimeFaces.current().ajax().update("formCuenta");
    }

    private void actulizarInfoGridFom() {
        actualizarAsociacionPresupuestaria();
        listCobradoDevengado = catalogoPresupuestoService.cobradoDevengado(opcionBusqueda.getAnio());
        switch (cuenta.getNivel().getOrden()) {
            case 1:
            case 4:
            case 6:
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                break;
            case 2:
            case 3:
            case 5:
            case 7:
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                break;
        }

    }

    private void actulizarInfoGridFomVista() {
        actualizarAsociacionPresupuestariaVista();
        listCobradoDevengado = catalogoPresupuestoService.cobradoDevengado(opcionBusqueda.getAnio());
        switch (cuentaVista.getNivel().getOrden()) {
            case 1:
            case 4:
            case 6:
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                break;
            case 2:
            case 3:
            case 5:
            case 7:
                cantColumnas = 4;
                columnClass = "ui-grid-col-3";
                break;
        }

    }

    public void visualizarCuentas(CuentaContable c, boolean edit) {
        listaAsociadasPresupuesto = new ArrayList<>();
        cuentaVista = new CuentaContable();
        if (edit) {
            editListCobradoDevengado(c);
            if (c.getCobradoDevengado() != null) {
            }
            cuentaVista = c;
        } else {
            cuentaVista.setPadre(c);
            cuentaVista.setClasificacion(c.getClasificacion());
            cuentaVista.setNivel(nivelService.getProximoNivel(c.getNivel()));
            cuentaVista.setPeriodo(c.getPeriodo());
            invertirCuentaVista();
            switch (c.getNivel().getOrden()) {
                case 1:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 2:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(c.getGrupo());
                    cuentaVista.setSubGrupo(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 3:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(c.getGrupo());
                    cuentaVista.setSubGrupo(c.getSubGrupo());
                    cuentaVista.setCuentaNivel1(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 4:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(c.getGrupo());
                    cuentaVista.setSubGrupo(c.getSubGrupo());
                    cuentaVista.setCuentaNivel1(c.getCuentaNivel1());
                    cuentaVista.setCuentaNivel2(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 5:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(c.getGrupo());
                    cuentaVista.setSubGrupo(c.getSubGrupo());
                    cuentaVista.setCuentaNivel1(c.getCuentaNivel1());
                    cuentaVista.setCuentaNivel2(c.getCuentaNivel2());
                    cuentaVista.setCuentaNivel3(cuentaContableService.getMaxValueForChild(c, false));
                    break;
                case 6:
                    cuentaVista.setTitulo(c.getTitulo());
                    cuentaVista.setGrupo(c.getGrupo());
                    cuentaVista.setSubGrupo(c.getSubGrupo());
                    cuentaVista.setCuentaNivel1(c.getCuentaNivel1());
                    cuentaVista.setCuentaNivel2(c.getCuentaNivel2());
                    cuentaVista.setCuentaNivel3(c.getCuentaNivel3());
                    cuentaVista.setCuentaNivel4(cuentaContableService.getMaxValueForChild(c, false));
                    break;
            }
        }
        listaAsociadasPresupuesto = cuentaContableService.getListaPresupuestoAsociado(c);
        actulizarInfoGridFomVista();
        PrimeFaces.current().executeScript("PF('cuentaDialogVisualizacion').show()");
        PrimeFaces.current().ajax().update(":formCuentaView");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    private String generarCodigo(CuentaContable c) {

        if (c.getPadre() != null) {
            return c.getPadre().getCodigo() + getSubCodigo(c);
        }

        return getSubCodigo(c);
    }

    private String getSubCodigo(CuentaContable c) {

        String format = "%0" + c.getNivel().getLongitud() + "d";
        switch (c.getNivel().getOrden()) {

            case 1: {
                return String.format(format, c.getTitulo());
            }
            case 2: {
                return String.format(format, c.getGrupo());
            }
            case 3: {
                return String.format(format, c.getSubGrupo());
            }
            case 4: {
                return String.format(format, c.getCuentaNivel1());
            }
            case 5: {
                return String.format(format, c.getCuentaNivel2());
            }
            case 6: {
                return String.format(format, c.getCuentaNivel3());
            }
            case 7: {
                return String.format(format, c.getCuentaNivel4());
            }
        }
        return "";
    }

    public boolean renderElementForm(CuentaContable cc, int position) {
        try {
            if (cc.getNivel() != null) {

                return position <= cc.getNivel().getOrden();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean readOnlyCampo() {
        boolean edit = cuenta.getId() != null;
        if (edit) {
            return true;
        }
        return false;
    }

    public String styleNegrilla(CuentaContable cuenta) {
        if (cuenta.getMovimiento().equals(Boolean.FALSE)) {
            return "font-weight: bold";
        }
        return "";
    }

    public boolean readOnlyElementForm(CuentaContable cc, int position) {
        try {
            if (cc != null && cc.getNivel() != null) {
                return position != cc.getNivel().getOrden();
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public String etiquetas(CuentaContable c) {
        String var = "";
        int codigo;
        CuentaContable padre = cuentaContableService.getPadre(c, opcionBusqueda.getAnio());
        if (c.getId() != null && c.getNivel().getOrden() > 3) {
            if (padre.getCobradoDevengado() != null) {
                codigo = Integer.parseInt(padre.getCobradoDevengado().getCodigo());
                return condicion_etiquetas(codigo);
            }
            CuentaContable padrePadre = cuentaContableService.getPadre(padre, opcionBusqueda.getAnio());
            if (padrePadre.getCobradoDevengado() != null) {
                codigo = Integer.parseInt(padrePadre.getCobradoDevengado().getCodigo());
                return condicion_etiquetas(codigo);
            }
            CuentaContable padrePadrePadre = cuentaContableService.getPadre(padrePadre, opcionBusqueda.getAnio());
            if (padrePadrePadre.getCobradoDevengado() != null) {
                codigo = Integer.parseInt(padrePadrePadre.getCobradoDevengado().getCodigo());
                return condicion_etiquetas(codigo);
            }
        }
        return "";
    }

    public String condicion_etiquetas(int codigo) {
        if (codigo > 0 && codigo < 50) {
            return "Cobrado / Devengado";
        } else {
            return "Pagado / Devengado";
        }
    }

    public boolean renderCobradoPagado(CuentaContable c, Boolean activarMensaje) {
        if (c.getId() != null || (c.getId() == null && c.getNivel().getOrden() > 3)) {
            if (c.getPadre() != null) {
                CuentaContable padre = cuentaContableService.getPadre(c, opcionBusqueda.getAnio());
                if (padre.getCobradoDevengado() != null) {
                    c.setCtaPagarCobrar(Boolean.TRUE);
                    asociacionCobradoD(activarMensaje);
                    return true;
                }
                CuentaContable padrePadre = cuentaContableService.getPadre(padre, opcionBusqueda.getAnio());
                if (padrePadre.getCobradoDevengado() != null) {
                    c.setCtaPagarCobrar(Boolean.TRUE);
                    asociacionCobradoD(activarMensaje);
                    return true;
                }
                CuentaContable padrePadrePadre = cuentaContableService.getPadre(padrePadre, opcionBusqueda.getAnio());
                if (padrePadrePadre.getCobradoDevengado() != null) {
                    c.setCtaPagarCobrar(Boolean.TRUE);
                    asociacionCobradoD(activarMensaje);
                    return true;
                }
            }
        }
        PrimeFaces.current().ajax().update("asoc");
        return false;
    }

    private void editListCobradoDevengado(CuentaContable c) {
        if (c.getPadre() != null) {
            if (c.getId() != null && c.getNivel().getOrden() > 3) {
                int aux = (int) c.getNivel().getOrden();
                CuentaContable temp = c;
                System.out.println("INICIAL: " + temp.getCodigo());
                for (int i = aux; i > 1; i--) {
                    System.out.println("CONTADOR : " + i);
                    System.out.println("IN: " + temp.getCodigo());
                    temp = cuentaContableService.getPadre(temp, temp.getPeriodo());
                    System.out.println("OUT: " + temp.getCodigo());
                    if (temp.getCobradoDevengado() != null) {
                        list3 = cuentaCatalogoService.getCatalogoPresupuesto(c.getId());
                        listHijosCobadoDevengado = catalogoPresupuestoService.getListCatalogo(temp.getCobradoDevengado().getCodigo(), temp.getPeriodo());
                    }
                }
            }
            if (!list3.isEmpty()) {
                for (CatalogoPresupuesto catpre : listHijosCobadoDevengado) {
                    for (CuentaContablecatalogoPresupuesto cat : list3) {
                        if (catpre.equals(cat.getCatalogoPresupuesto())) {
                            catalogosSelecionandos.add(cat.getCatalogoPresupuesto());
                        }
                    }
                }
            }
        }
    }

    public void actualizarLists() {
        invertir = false;
        catalogosSelecionandos = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        listHijosCobadoDevengado = new ArrayList<>();
    }

    public Boolean listEquals() {
        boolean var = false;
        if (catalogosSelecionandos.size() == list3.size()) {
            for (CuentaContablecatalogoPresupuesto cc : list3) {
                for (CatalogoPresupuesto cp : catalogosSelecionandos) {
                    if (Objects.equals(cc.getCatalogoPresupuesto().getId(), cp.getId())) {
                        var = true;
                        break;
                    } else {
                        var = false;
                    }
                }
                if (var == false) {
                    return var;
                }
            }
        }
        return var;
    }

    private void cuentaCatalogo(boolean edit) {
        Boolean listEquals = listEquals();
        if (cuenta.getId() != null) {
            if (!catalogosSelecionandos.isEmpty() && listEquals.equals(false)) {
                for (CatalogoPresupuesto catPre : catalogosSelecionandos) {
                    cuenta_Catalogo = new CuentaContablecatalogoPresupuesto();
                    cuenta_Catalogo.setCatalogoPresupuesto(catPre);
                    list2.add(cuenta_Catalogo);
                }
                if (edit && listEquals.equals(false)) {
                    for (CuentaContablecatalogoPresupuesto cta : list3) {
                        cuentaCatalogoService.remove(cta);
                    }
                }
                if (!list2.isEmpty()) {
                    for (CuentaContablecatalogoPresupuesto cc : list2) {
                        cuenta_Catalogo = new CuentaContablecatalogoPresupuesto();
                        cuenta_Catalogo.setCatalogoPresupuesto(cc.getCatalogoPresupuesto());
                        if (edit) {
                            cuenta_Catalogo.setCuentaContable(cuenta);
                        } else {
                            cuenta_Catalogo.setCuentaContable(cuentaSeleccionada);
                        }
                        cuenta_Catalogo = cuentaCatalogoService.create(cuenta_Catalogo);
                    }
                }
            } else if (!list3.isEmpty() && listEquals.equals(false)) {
                for (CuentaContablecatalogoPresupuesto cta : list3) {
                    cuentaCatalogoService.remove(cta);
                }
            }
        }
    }

    public void invertirCuenta() {
        if (cuenta.getId() != null || cuenta.getId() == null) {
            if (this.invertir.equals(true)) {
                credito = catalogoPresupuestoService.asociacionPresupuestaria(false, opcionBusqueda.getAnio());
                debito = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
            } else {
                credito = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
                debito = catalogoPresupuestoService.asociacionPresupuestaria(false, opcionBusqueda.getAnio());
            }
        }
    }

    public void invertirCuentaVista() {
        if (cuentaVista.getId() != null || cuentaVista.getId() == null) {
            if (this.invertir2 == true) {
                credito = catalogoPresupuestoService.asociacionPresupuestaria(false, opcionBusqueda.getAnio());
                debito = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
            } else {
                credito = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
                debito = catalogoPresupuestoService.asociacionPresupuestaria(false, opcionBusqueda.getAnio());
            }
        }
    }

    public boolean disabledAgrupacion() {
        List<CuentaContable> hijos = cuentaContableService.getHijosByPadre(cuenta);
        if (cuenta != null) {
            if (cuenta.getCredito() != null || cuenta.getDebito() != null) {
                return true;
            }
            if (!hijos.isEmpty() && cuenta.getId() != null) {
                return true;
            }
        }
        return false;
    }

    public void asociacionCobradoD(Boolean activarMensaje) {
        if (cuenta.getId() != null) {
            list3 = cuentaCatalogoService.getCatalogoPresupuesto(cuenta.getId());
        }
        if (cuenta.getCredito() != null || cuenta.getDebito() != null) {
            cuenta.setCtaPagarCobrar(Boolean.FALSE);
            PrimeFaces.current().ajax().update("messages");
            if (activarMensaje) {
                JsfUtil.addWarningMessage("Cuenta Contable", cuenta.getCodigo() + " Ya Registra Asocicación Presupuestaria Credito/Debito.");
            }
            return;
        } else if (!list3.isEmpty()) {
            cuenta.setCtaPagarCobrar(Boolean.TRUE);
            PrimeFaces.current().ajax().update("messages");
            if (activarMensaje) {
                JsfUtil.addWarningMessage("Cuenta Contable", cuenta.getCodigo() + " Ya Registra Asocicación Presupuestaria Cobrado/Devengado.");
            }
            return;
        }
        if (cuenta.getPadre().getCobradoDevengado() != null) {
            listHijosCobadoDevengado = catalogoPresupuestoService.getListCatalogo(cuenta.getPadre().getCobradoDevengado().getCodigo(), opcionBusqueda.getAnio());
        } else if (cuenta.getPadre().getPadre().getCobradoDevengado() != null) {
            listHijosCobadoDevengado = catalogoPresupuestoService.getListCatalogo(cuenta.getPadre().getPadre().getCobradoDevengado().getCodigo(), opcionBusqueda.getAnio());
        } else if (cuenta.getPadre().getPadre().getPadre().getCobradoDevengado() != null) {
            listHijosCobadoDevengado = catalogoPresupuestoService.getListCatalogo(cuenta.getPadre().getPadre().getPadre().getCobradoDevengado().getCodigo(), opcionBusqueda.getAnio());
        } else {
            cuenta.setCtaPagarCobrar(Boolean.FALSE);
            PrimeFaces.current().ajax().update("messages");
            if (activarMensaje) {
                JsfUtil.addWarningMessage("Cuenta Contable", "Verifique que la cuenta PADRE este asociada a un DEVENGADO/COBRADO");
            }
            return;
        }
    }

    public void cobradoDevengado() {
        listCobradoDevengado = catalogoPresupuestoService.cobradoDevengado(opcionBusqueda.getAnio());
    }

    public void actualizarAsociacionPresupuestaria() {
        if (cuenta.getId() != null) {
            if (cuenta.getDebito() != null || cuenta.getCredito() != null) {
                List<CatalogoPresupuesto> aux = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
                for (CatalogoPresupuesto cp : aux) {
                    if (cuenta.getDebito() != null) {
                        if (cuenta.getDebito().getCodigo().equals(cp.getCodigo())) {
                            this.invertir = true;
                        }
                    } else {
                        break;
                    }
                }
                invertirCuenta();
            } else {
                invertirCuenta();
            }
        }
    }

    public void actualizarAsociacionPresupuestariaVista() {
        if (cuentaVista.getId() != null) {
            if (cuentaVista.getDebito() != null || cuentaVista.getCredito() != null) {
                List<CatalogoPresupuesto> aux = catalogoPresupuestoService.asociacionPresupuestaria(true, opcionBusqueda.getAnio());
                for (CatalogoPresupuesto cp : aux) {
                    if (cuentaVista.getDebito() != null) {
                        if (cuentaVista.getDebito().getCodigo().equals(cp.getCodigo())) {
                            invertir2 = true;
                        }
                    } else {
                        break;
                    }
                }
                invertirCuentaVista();
            } else {
                invertirCuentaVista();
            }
        }
    }

    public void abrirDialogImprimir(boolean excel) {
        if (lazy.getRowCount() == 0) {
            JsfUtil.addWarningMessage("Información", "No existen cuentas que mostrar");
            return;
        } else {
            if (excel) {
                PrimeFaces.current().executeScript("PF('dlgImp2').show()");
            } else {
                PrimeFaces.current().executeScript("PF('dlgImp').show()");
            }

        }
    }

    public void imprimirRep(boolean tipoDoc) {
        if ((impr != null)) {
            if (opcionBusqueda.getTitulo() != null) {
                ss.addParametro("titulo", opcionBusqueda.getTitulo());
            }
            if (opcionBusqueda.getGrupo() != null) {
                ss.addParametro("grupo", opcionBusqueda.getGrupo());
            }
            if (opcionBusqueda.getSubGrupo() != null) {
                ss.addParametro("subGrupo", opcionBusqueda.getSubGrupo());
            }
            if (opcionBusqueda.getNivel1() != null) {
                ss.addParametro("nivel1", opcionBusqueda.getNivel1());
            }
            if (opcionBusqueda.getNivel2() != null) {
                ss.addParametro("nivel2", opcionBusqueda.getNivel2());
            }
            if (opcionBusqueda.getNivel3() != null) {
                ss.addParametro("nivel3", opcionBusqueda.getNivel3());
            }
            if (opcionBusqueda.getNivel4() != null) {
                ss.addParametro("nivel4", opcionBusqueda.getNivel4());
            }
            ss.addParametro("anio", opcionBusqueda.getAnio());
            if (!tipoDoc) {
                ss.setContentType("xlsx");
            }
            if (impr.equals("plan")) {
                ss.setNombreReporte("planDeCuentas");
            }
            if (impr.equals("relacion")) {
                ss.setNombreReporte("relacionContablePresupuesto");
            }
            ss.setNombreSubCarpeta("contabilidad");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            JsfUtil.addWarningMessage("Información", "escoja una de las opciones para proceder");
            return;
        }
        PrimeFaces.current().executeScript("PF('dlgImp').hide()");
        PrimeFaces.current().executeScript("PF('dlgImp2').hide()");
    }

//<editor-fold defaultstate="collapsed" desc="GETTERs AND SETTERs">
    public boolean isBtnDisable() {
        return btnDisable;
    }

    public void setBtnDisable(boolean btnDisable) {
        this.btnDisable = btnDisable;
    }

    public List<CatalogoPresupuesto> getListCobradoDevengado() {
        return listCobradoDevengado;
    }

    public void setListCobradoDevengado(List<CatalogoPresupuesto> listCobradoDevengado) {
        this.listCobradoDevengado = listCobradoDevengado;
    }

    public CuentaContable getCuentaVista() {
        return cuentaVista;
    }

    public void setCuentaVista(CuentaContable cuentaVista) {
        this.cuentaVista = cuentaVista;
    }

    public boolean isInvertir2() {
        return invertir2;
    }

    public void setInvertir2(boolean invertir2) {
        this.invertir2 = invertir2;
    }

    public List<CatalogoPresupuesto> getCredito() {
        return credito;
    }

    public void setCredito(List<CatalogoPresupuesto> credito) {
        this.credito = credito;
    }

    public List<CatalogoPresupuesto> getDebito() {
        return debito;
    }

    public void setDebito(List<CatalogoPresupuesto> debito) {
        this.debito = debito;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public CuentaContableLazy getLazy() {
        return lazy;
    }

    public void setLazy(CuentaContableLazy lazy) {
        this.lazy = lazy;
    }

    public String getImpr() {
        return impr;
    }

    public void setImpr(String impr) {
        this.impr = impr;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public CuentaContable getCuenta() {
        return cuenta;
    }

    public void setCuenta(CuentaContable cuenta) {
        this.cuenta = cuenta;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public CuentaContableService getCuentaContableService() {
        return cuentaContableService;
    }

    public void setCuentaContableService(CuentaContableService cuentaContableService) {
        this.cuentaContableService = cuentaContableService;
    }

    public CuentaContable getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(CuentaContable cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public List<CatalogoItem> getClasificaciones() {
        return clasificaciones;
    }

    public void setClasificaciones(List<CatalogoItem> clasificaciones) {
        this.clasificaciones = clasificaciones;
    }

    public int getCantColumnas() {
        return cantColumnas;
    }

    public void setCantColumnas(int cantColumnas) {
        this.cantColumnas = cantColumnas;
    }

    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public Boolean getInvertir() {
        return invertir;
    }

    public void setInvertir(Boolean invertir) {
        this.invertir = invertir;
    }

    public List<CatalogoPresupuesto> getListHijosCobadoDevengado() {
        return listHijosCobadoDevengado;
    }

    public void setListHijosCobadoDevengado(List<CatalogoPresupuesto> listHijosCobadoDevengado) {
        this.listHijosCobadoDevengado = listHijosCobadoDevengado;
    }

    public List<CuentaContablecatalogoPresupuesto> getListaAsociadasPresupuesto() {
        return listaAsociadasPresupuesto;
    }

    public void setListaAsociadasPresupuesto(List<CuentaContablecatalogoPresupuesto> listaAsociadasPresupuesto) {
        this.listaAsociadasPresupuesto = listaAsociadasPresupuesto;
    }

    public List<CatalogoPresupuesto> getCatalogosSelecionandos() {
        return catalogosSelecionandos;
    }

    public void setCatalogosSelecionandos(List<CatalogoPresupuesto> catalogosSelecionandos) {
        this.catalogosSelecionandos = catalogosSelecionandos;
    }
//</editor-fold>

}
