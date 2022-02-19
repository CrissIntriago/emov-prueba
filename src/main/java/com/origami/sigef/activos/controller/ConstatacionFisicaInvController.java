/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ConstatacionFisicaInvService;
import com.origami.sigef.activos.service.DetalleConstFisicaService;
import com.origami.sigef.activos.service.DetalleItemService;
import com.origami.sigef.activos.service.GrupoNivelesService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ConstatacionFisicaInventario;
import com.origami.sigef.common.entities.DetalleConstFisicaInventario;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.GrupoNiveles;
import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "constatacionFisicaInvView")
@ViewScoped
public class ConstatacionFisicaInvController implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ProcesoIngresoInvController.class.getName());
    private String banderaConsulta;
    private String banderaEstado = "no";
    private ConstatacionFisicaInventario constatacionFisicaInventario;
    private DetalleConstFisicaInventario detalleConstFisicaInventario;
    private ContCuentas cuentaContable;
    private ContCuentas tipoGasto;
    private GrupoNiveles area;
    private List<GrupoNiveles> listArea;
    private GrupoNiveles grupo;
    private List<GrupoNiveles> listGrupo;
    private List<GrupoNiveles> listGrupoItems;
    private GrupoNiveles subGrupo;
    private List<GrupoNiveles> listSubGrupo;
    private List<GrupoNiveles> listSubGrupoItems;
    private DetalleItem detalleItem;
    private ArrayList<DetalleItem> listDetalle, listDetalleSelect;
    private List<CatalogoItem> listEstados;
    private List<DetalleItem> listDetalleMuchItem;
    private List<DetalleConstFisicaInventario> listDetalleConstFis;
    private List<ContCuentas> listTiposGastos;
    private List<ContCuentas> listCuentaContable;
    private List<ConstatacionFisicaInventario> listconst;  ///borrar pilas
    private LazyModel<ConstatacionFisicaInventario> lazyModel;
    private Boolean bandera = Boolean.TRUE;
    private Boolean banderaDos = Boolean.TRUE;
    private Boolean banderaTres = Boolean.TRUE;
    private Boolean banderaBotonG = Boolean.TRUE;
    private Boolean banderaBotonSelection = Boolean.TRUE;
    private Boolean bolGrupo = Boolean.TRUE;
    private Boolean bolImprimir = Boolean.TRUE;
    private Boolean bolSubGrupo = Boolean.TRUE;
    private ConstatacionFisicaInventario impresion;
    private Short anio;
    private List<DetalleItem> listItemsSeleccionados;
    private LazyModel<DetalleItem> lazyItem;
    
    @Inject
    private ContCuentasService contCuentasService;
    
    @Inject
    private ConstatacionFisicaInvService cfis;
    @Inject
    private DetalleItemService dis;
    @Inject
    private DetalleConstFisicaService detalleConstFisicaService;
    @Inject
    private GrupoNivelesService grupoNivelesService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession userSession;
    
    private Date fechaDesde;
    private Date fechaHasta;
    
    @Inject
    private ValoresService valoresService;
    
    private Valores valores;
    
    private DetalleItem item;
    
    @PostConstruct
    public void initView() {
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        detalleConstFisicaInventario = new DetalleConstFisicaInventario();
        item = new DetalleItem();
        listArea = grupoNivelesService.findByPadres();
        cuentaContable = new ContCuentas();
        tipoGasto = new ContCuentas();
        area = new GrupoNiveles();
        grupo = new GrupoNiveles();
        subGrupo = new GrupoNiveles();
        detalleItem = new DetalleItem();
        listDetalle = new ArrayList<>();
        lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
        lazyModel.getFilterss().put("estadoCons", true);
        String cod2 = "estados_constatacion_fisica";
        listEstados = catalogoItemService.findAllEstados(cod2);
        listconst = cfis.getAllConstataciones();
        listDetalleMuchItem = new ArrayList<>();
        listDetalleConstFis = new ArrayList<>();
        anio = Short.valueOf("2019");   /// borrar
        listTiposGastos = dis.getTipoGastos("13", Arrays.asList("15138", "15238"), "911");
        valores = valoresService.findByNamedQuery1("Valores.findCodeLike", "RUTA_IMAGEN_SERVICE");
        listItemsSeleccionados = new ArrayList<>();
    }
    
    public void loadLazy() {
        lazyItem = null;
        lazyItem = new LazyModel<>(DetalleItem.class);
        lazyItem.getFilterss().put("estado", true);
        lazyItem.getFilterss().put("cuentaContable:noEqual", null);
        lazyItem.getSorteds().put("orden", "DESC");
    }
    
    public void openDialogSeleccion() {
        if (listDetalle.isEmpty()) {
            banderaDos = Boolean.TRUE;
            limpiarSelecciones();
            PrimeFaces.current().executeScript("PF('dlgItem').show()");
        } else {
            JsfUtil.addWarningMessage("Información", "Tiene un proceso generado, por favor CANCELAR para generar uno nuevo");
            banderaDos = Boolean.FALSE;
        }
    }
    
    public void actualizarCtaCbe() {
        listCuentaContable = contCuentasService.findCtaContableByTipo(tipoGasto);
    }

//<editor-fold defaultstate="collapsed" desc="botones busqueda en el dialogo Consultar">
    public void busqueda() {
        if (fechaDesde == null && fechaHasta == null) {
            JsfUtil.addWarningMessage("Advertencia", "Ingrese un rango de fecha.");
            return;
        }
        if (fechaDesde != null && fechaHasta != null) {
            if (fechaDesde.compareTo(fechaHasta) > 0) {
                JsfUtil.addWarningMessage("Información", "La fecha DESDE no puede ser mayor a la fecha HASTA");
                return;
            }
            lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
            lazyModel.getFilterss().put("fechaConstatacion:between", Arrays.asList(fechaDesde, fechaHasta));
        }
        if (fechaDesde != null && fechaHasta == null) {
            lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
            lazyModel.getFilterss().put("fechaConstatacion:gte", fechaDesde);
        }
        if (fechaDesde == null && fechaHasta != null) {
            lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
            lazyModel.getFilterss().put("fechaConstatacion:lte", fechaHasta);
        }
        PrimeFaces.current().ajax().update("frmMain:dtConsulta");
//        System.out.println("row " + lazyModel.getRowCount());
//        System.out.println("row " + lazyModel.getRowIndex());
        if (lazyModel.getRowCount() == 0) {
            JsfUtil.addInformationMessage("Información", "No hay constataciones en ese rango de fecha.");
        }
        
    }
    
    public void busquedaLimpiar() {
        fechaDesde = null;
        fechaHasta = null;
        banderaConsulta = null;
        lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
        PrimeFaces.current().ajax().update("frmMain");
    }
//</editor-fold>

    public void nuevaConst() {
        bolImprimir = Boolean.TRUE;
        if (bandera) {
            banderaBotonSelection = Boolean.FALSE;
            String cod = "NEW-CF";
            String cod2 = "estados_constatacion_fisica";
            constatacionFisicaInventario.setEstado(catalogoItemService.getEstadoConstatacion(cod, cod2));
            Long orden = cfis.getOrderConstatacion();
            constatacionFisicaInventario.setFechaConstatacion(new Date());
            constatacionFisicaInventario.setFechaCreacion(new Date());
            constatacionFisicaInventario.setOrden(orden);
            constatacionFisicaInventario.setEstadoCons(true);
            String codigoGenerado = Utils.completarCadenaConCeros(constatacionFisicaInventario.getOrden() + "", 4);
            constatacionFisicaInventario.setCodigo("CF-" + codigoGenerado);
            constatacionFisicaInventario.setUsuarioCreacion(userSession.getNameUser());
            bandera = Boolean.FALSE;
        } else {
            JsfUtil.addWarningMessage("Información", "Tiene un proceso generado, por favor CANCELAR para generar uno nuevo");
        }
    }
    
    public void consultar() {
        bolImprimir = Boolean.TRUE;
        if ((bandera == true) && (constatacionFisicaInventario.getId() == null)) {
            if (banderaTres) {
                busquedaLimpiar();
                lazyModel = new LazyModel(ConstatacionFisicaInventario.class);
                PrimeFaces.current().ajax().update("frmConsulta:dtConsulta");
                PrimeFaces.current().executeScript("PF('dlgConsulta').show()");
            } else {
                JsfUtil.addWarningMessage("Información", "Tiene un proceso generado, por favor CANCELAR para generar uno nuevo");
            }
        } else {
            JsfUtil.addWarningMessage("Información", "Tiene un proceso generado, por favor CANCELAR para generar uno nuevo consultandolo");
        }
    }
    
    public void borrarElementoLista(int can) {
        listDetalle.remove(can);
    }
    
    public void limpiarSelecciones() {
        tipoGasto = new ContCuentas();
        cuentaContable = new ContCuentas();
        area = new GrupoNiveles();
        grupo = new GrupoNiveles();
        subGrupo = new GrupoNiveles();
        detalleItem = new DetalleItem();
    }
    
    public void buscarCuenta() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("CODIGO", Arrays.asList(tipoGasto.getCodigo()));
        Utils.openDialog("/facelet/activos/inventario/Dialog/dialogCuentaContable", params);
    }
    
    public void selectDatosCta(SelectEvent evt) {
        this.cuentaContable = (ContCuentas) evt.getObject();
    }
    
    public void buscarItem() {
        loadLazy();
        PrimeFaces.current().executeScript("PF('dialogItems').show()");
        PrimeFaces.current().ajax().update("formItemsDialog");
    }
    
    public void addItemsSelect() {
        if (listItemsSeleccionados.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "Seleccione Items para Agregarle Tipo de Inv., Cuenta y Contra Cuenta Contable.");
            return;
        }
        for (DetalleItem d : listItemsSeleccionados) {
            if (!listDetalle.contains(d)) {
                listDetalle.add(d);
            }
        }
        banderaBotonG = Boolean.FALSE;
        JsfUtil.executeJS("PF('dialogItems').hide()");
        PrimeFaces.current().ajax().update("frmConsulta");
    }
    
    public void guardar() {
        int ban = 0;
        for (int i = 0; i < listDetalle.size(); i++) {
            if ((listDetalle.get(i).getCantMas() < 1) && (listDetalle.get(i).getCantidadExistente() > 0)
                    && (!constatacionFisicaInventario.getEstado().toString().equals("NUEVA"))) {
                ban = ban + 1;
            }
        }
        if (ban > 0) {
            JsfUtil.addWarningMessage("Información", "Uno de los item no ha sido constatado. Ingrese valor");
            return;
        }
        if (listDetalle.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "No existen datos de items que guardar");
            return;
        }
        if ((constatacionFisicaInventario.getEstado().toString().equals("NUEVA"))) {
            String cod = "EMI-CF";
            String cod2 = "estados_constatacion_fisica";
            constatacionFisicaInventario.setEstado(catalogoItemService.getEstadoConstatacion(cod, cod2));
            cfis.create(constatacionFisicaInventario);
            for (int i = 0; i < listDetalle.size(); i++) {
                detalleConstFisicaInventario.setConstatacionFisicaId(constatacionFisicaInventario);
                detalleConstFisicaInventario.setDetalleItem(listDetalle.get(i));
                detalleConstFisicaInventario.setExistenciaMovimiento(listDetalle.get(i).getCantidadExistente());
                detalleConstFisicaInventario.setConstatado(listDetalle.get(i).getCantMas());
                detalleConstFisicaInventario.setDiferencia(listDetalle.get(i).getCantMen());
                detalleConstFisicaInventario.setObservacion(listDetalle.get(i).getObservacionTemp());
                detalleConstFisicaService.create(detalleConstFisicaInventario);
                detalleConstFisicaInventario = new DetalleConstFisicaInventario();
            }
        } else {
            String cod = "REG-CF";
            String cod2 = "estados_constatacion_fisica";
            String ing = "no";
            String egr = "no";
            constatacionFisicaInventario.setEstado(catalogoItemService.getEstadoConstatacion(cod, cod2));
            cfis.edit(constatacionFisicaInventario);
            for (int i = 0; i < listDetalle.size(); i++) {
                listDetalleConstFis.get(i).setConstatado(listDetalle.get(i).getCantMas());
                listDetalleConstFis.get(i).setDiferencia(listDetalle.get(i).getCantMen());
                if (listDetalleConstFis.get(i).getDiferencia() > 0) {
                    ing = "INGRESO";
                } else if (listDetalleConstFis.get(i).getDiferencia() < 0) {
                    egr = "EGRESO";
                }
                listDetalleConstFis.get(i).setObservacion(listDetalle.get(i).getObsAdicional());
                listDetalleConstFis.get(i).setRevisado(Boolean.TRUE);
                detalleConstFisicaService.edit(listDetalleConstFis.get(i));
            }
            if ((ing.equals("INGRESO")) && (egr.equals("EGRESO"))) {
                constatacionFisicaInventario.setRazon("AMBOS");
            } else if ((ing.equals("INGRESO")) && (!egr.equals("EGRESO"))) {
                constatacionFisicaInventario.setRazon("INGRESO");
            } else if ((!ing.equals("INGRESO")) && (egr.equals("EGRESO"))) {
                constatacionFisicaInventario.setRazon("EGRESO");
            }
            constatacionFisicaInventario.setAjustado(Boolean.FALSE);
            cfis.edit(constatacionFisicaInventario);
        }
        lazyModel = new LazyModel<>(ConstatacionFisicaInventario.class);
        listconst = cfis.getAllConstataciones();
        cancelar();
        bolImprimir = Boolean.FALSE;
        JsfUtil.addSuccessMessage("Información", "La constatación se ha guardado correctamente");
        PrimeFaces.current().executeScript("PF('dlgConsulta').hide()");
        PrimeFaces.current().ajax().update("frmConsulta");
        PrimeFaces.current().ajax().update("frmMain");
    }
    
    public void onCellEdit(CellEditEvent event) {
        int nn = event.getRowIndex();
        int diff = listDetalle.get(nn).getCantMas() - listDetalle.get(nn).getCantidadExistente();
        listDetalle.get(nn).setCantMen(diff);

        /*
        Editando y guardando automaticamente los datos de la constatación
         */
        listDetalleConstFis.get(nn).setConstatado(listDetalle.get(nn).getCantMas());
        listDetalleConstFis.get(nn).setDiferencia(listDetalle.get(nn).getCantMen());
        listDetalleConstFis.get(nn).setObservacion(listDetalle.get(nn).getObsAdicional());
        listDetalleConstFis.get(nn).setRevisado(Boolean.TRUE);
        detalleConstFisicaService.edit(listDetalleConstFis.get(nn));
        
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Valor Modificado", " valor cambió ");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void cancelar() {
        limpiarSelecciones();
        this.impresion = new ConstatacionFisicaInventario();
        this.impresion = Utils.clone(this.constatacionFisicaInventario);
        constatacionFisicaInventario = new ConstatacionFisicaInventario();
        detalleConstFisicaInventario = new DetalleConstFisicaInventario();
        bolGrupo = Boolean.TRUE;
        bolImprimir = Boolean.TRUE;
        bolSubGrupo = Boolean.TRUE;
        cuentaContable = new ContCuentas();
        area = new GrupoNiveles();
        grupo = new GrupoNiveles();
        subGrupo = new GrupoNiveles();
        detalleItem = new DetalleItem();
        listDetalle = new ArrayList<>();
        listDetalleConstFis = new ArrayList<>();
        bandera = Boolean.TRUE;
        banderaDos = Boolean.TRUE;
        banderaTres = Boolean.TRUE;
        banderaBotonG = Boolean.TRUE;
        banderaBotonSelection = Boolean.TRUE;
        PrimeFaces.current().executeScript("PF('dlgConsulta').hide()");
        PrimeFaces.current().ajax().update("frmConsulta", "frmMain");
    }
    
    public void cerrar(ConstatacionFisicaInventario cons) {
        if (cons == null) {
            nuevaConst();
        } else {
            if ((cons.getGrupo() == null) && (cons.getSubGrupo() == null)) {
                bolGrupo = Boolean.FALSE;
                bolSubGrupo = Boolean.FALSE;
            } else if ((cons.getGrupo() != null) && (cons.getSubGrupo() == null)) {
                bolSubGrupo = Boolean.FALSE;
            }
            fechaDesde = null;
            fechaHasta = null;
            this.constatacionFisicaInventario = cons;
            area = constatacionFisicaInventario.getArea();
            try {
                grupo = constatacionFisicaInventario.getGrupo();
                subGrupo = constatacionFisicaInventario.getSubGrupo();
                listGrupo = grupoNivelesService.findGrupoByPadreEscogido(area);
                listSubGrupo = grupoNivelesService.findSubGrupoByGrupoEscogido(grupo);
            } catch (Exception e) {
            }
            listDetalleConstFis = detalleConstFisicaService.getListDetalleItemCons(constatacionFisicaInventario);
            if (constatacionFisicaInventario.getEstado().getTexto().equals("REGISTRADA")) {
                
                banderaBotonG = Boolean.TRUE;
            } else {
                banderaBotonG = Boolean.FALSE;
            }
            for (int i = 0; i < listDetalleConstFis.size(); i++) {
                detalleItem.setTipoGasto(listDetalleConstFis.get(i).getDetalleItem().getTipoGasto());
                detalleItem.setAsignarGrupo(listDetalleConstFis.get(i).getDetalleItem().getAsignarGrupo());
                detalleItem.setCuentaContable(listDetalleConstFis.get(i).getDetalleItem().getCuentaContable());
                detalleItem.setCodigoAgrupado(listDetalleConstFis.get(i).getDetalleItem().getCodigoAgrupado());
                detalleItem.setDescripcion(listDetalleConstFis.get(i).getDetalleItem().getDescripcion());
                detalleItem.setObservacionTemp(listDetalleConstFis.get(i).getObservacion());
                detalleItem.setCantidadExistente(listDetalleConstFis.get(i).getExistenciaMovimiento());
                detalleItem.setCantMas(listDetalleConstFis.get(i).getConstatado());
                detalleItem.setCantMen(listDetalleConstFis.get(i).getDiferencia());
                detalleItem.setUrlImagenConstatacion(valores.getValorString() + "CONSTATACIONINV/id/" + listDetalleConstFis.get(i).getId());
                listDetalle.add(detalleItem);
                detalleItem = new DetalleItem();
            }
        }
        PrimeFaces.current().executeScript("PF('dlgConsulta').show()");
        PrimeFaces.current().ajax().update("frmConsulta");
    }
    
    public ContCuentas obtenerPadreOfCuentaTGastos(ContCuentas c) {
        if (c == null) {
            return new ContCuentas();
        }
        return dis.findByPadreOfTGastos(c);
    }
    
    public void mostrarImgDialog(DetalleItem itemImagen) {
        if (itemImagen != null) {
            item = itemImagen;
        } else {
            item = new DetalleItem();
        }
        PrimeFaces.current().ajax().update("formImg");
        PrimeFaces.current().executeScript("PF('imagenDialog').show()");
    }
    
    public void imprimirConstatacion() {
        ss.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        if (impresion.getEstado().getTexto().equals("EMITIDA")) {
            ss.addParametro("cont_id", impresion.getId());
            ss.setNombreReporte("constatacionFisicaEmitida");
            ss.setNombreSubCarpeta("activos");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            ss.addParametro("id_const", impresion.getId());
            ss.setNombreReporte("constatacionFisicaRegistrada");
            ss.setNombreSubCarpeta("activos");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }
    
    public void ReimprimirConstatacion(ConstatacionFisicaInventario cons, Boolean tipo) {
        ss.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        if (!tipo) {
            ss.setContentType("xlsx");
            ss.setOnePagePerSheet(true);
        }
        if (cons.getEstado().getTexto().equals("EMITIDA")) {
            ss.addParametro("cont_id", cons.getId());
            ss.setNombreReporte("constatacionFisicaEmitida");
            ss.setNombreSubCarpeta("activos");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            ss.addParametro("id_const", cons.getId());
            ss.setNombreReporte("constatacionFisicaRegistrada");
            ss.setNombreSubCarpeta("activos");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }
    
    public void eliminarConst(ConstatacionFisicaInventario c) {
        constatacionFisicaInventario = c;
        if (constatacionFisicaInventario.getEstado().getCodigo().equals("EMI-CF")) {
            constatacionFisicaInventario.setEstadoCons(false);
            cfis.edit(constatacionFisicaInventario);
            JsfUtil.addSuccessMessage("Info", "Constatación eliminada correctamente");
            PrimeFaces.current().ajax().update("frmMain");
        } else if (constatacionFisicaInventario.getEstado().getCodigo().equals("REG-CF")) {
            JsfUtil.addErrorMessage("Error", "La Constatación está Registrada, no puede ser eliminada");
        }
        PrimeFaces.current().ajax().update("frmMain");
    }

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public String getBanderaConsulta() {
        return banderaConsulta;
    }
    
    public void setBanderaConsulta(String banderaConsulta) {
        this.banderaConsulta = banderaConsulta;
    }
    
    public Boolean getBanderaBotonSelection() {
        return banderaBotonSelection;
    }
    
    public List<GrupoNiveles> getListArea() {
        return listArea;
    }
    
    public void setListArea(List<GrupoNiveles> listArea) {
        this.listArea = listArea;
    }
    
    public List<GrupoNiveles> getListGrupo() {
        return listGrupo;
    }
    
    public void setListGrupo(List<GrupoNiveles> listGrupo) {
        this.listGrupo = listGrupo;
    }
    
    public List<GrupoNiveles> getListSubGrupo() {
        return listSubGrupo;
    }
    
    public Boolean getBolGrupo() {
        return bolGrupo;
    }
    
    public void setBolGrupo(Boolean bolGrupo) {
        this.bolGrupo = bolGrupo;
    }
    
    public void setListSubGrupo(List<GrupoNiveles> listSubGrupo) {
        this.listSubGrupo = listSubGrupo;
    }
    
    public void setBanderaBotonSelection(Boolean banderaBotonSelection) {
        this.banderaBotonSelection = banderaBotonSelection;
    }
    
    public Boolean getBanderaBotonG() {
        return banderaBotonG;
    }
    
    public void setBanderaBotonG(Boolean banderaBotonG) {
        this.banderaBotonG = banderaBotonG;
    }
    
    public DetalleConstFisicaInventario getDetalleConstFisicaInventario() {
        return detalleConstFisicaInventario;
    }
    
    public void setDetalleConstFisicaInventario(DetalleConstFisicaInventario detalleConstFisicaInventario) {
        this.detalleConstFisicaInventario = detalleConstFisicaInventario;
    }
    
    public String getBanderaEstado() {
        return banderaEstado;
    }
    
    public void setBanderaEstado(String banderaEstado) {
        this.banderaEstado = banderaEstado;
    }
    
    public List<DetalleItem> getListDetalleMuchItem() {
        return listDetalleMuchItem;
    }
    
    public void setListDetalleMuchItem(List<DetalleItem> listDetalleMuchItem) {
        this.listDetalleMuchItem = listDetalleMuchItem;
    }
    
    public ConstatacionFisicaInventario getConstatacionFisicaInventario() {
        return constatacionFisicaInventario;
    }
    
    public ContCuentas getTipoGasto() {
        return tipoGasto;
    }
    
    public void setTipoGasto(ContCuentas tipoGasto) {
        this.tipoGasto = tipoGasto;
    }
    
    public void setConstatacionFisicaInventario(ConstatacionFisicaInventario constatacionFisicaInventario) {
        this.constatacionFisicaInventario = constatacionFisicaInventario;
    }
    
    public Boolean getBandera() {
        return bandera;
    }
    
    public Boolean getBolImprimir() {
        return bolImprimir;
    }
    
    public void setBolImprimir(Boolean bolImprimir) {
        this.bolImprimir = bolImprimir;
    }
    
    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }
    
    public ArrayList<DetalleItem> getListDetalle() {
        return listDetalle;
    }
    
    public void setListDetalle(ArrayList<DetalleItem> listDetalle) {
        this.listDetalle = listDetalle;
    }
    
    public DetalleItem getDetalleItem() {
        return detalleItem;
    }
    
    public void setDetalleItem(DetalleItem detalleItem) {
        this.detalleItem = detalleItem;
    }
    
    public List<ContCuentas> getListTiposGastos() {
        return listTiposGastos;
    }
    
    public void setListTiposGastos(List<ContCuentas> listTiposGastos) {
        this.listTiposGastos = listTiposGastos;
    }
    
    public List<CatalogoItem> getListEstados() {
        return listEstados;
    }
    
    public void setListEstados(List<CatalogoItem> listEstados) {
        this.listEstados = listEstados;
    }
    
    public ContCuentas getCuentaContable() {
        return cuentaContable;
    }
    
    public void setCuentaContable(ContCuentas cuentaContable) {
        this.cuentaContable = cuentaContable;
    }
    
    public GrupoNiveles getArea() {
        return area;
    }
    
    public List<ContCuentas> getListCuentaContable() {
        return listCuentaContable;
    }
    
    public void setListCuentaContable(List<ContCuentas> listCuentaContable) {
        this.listCuentaContable = listCuentaContable;
    }
    
    public void setArea(GrupoNiveles area) {
        this.area = area;
    }
    
    public GrupoNiveles getGrupo() {
        return grupo;
    }
    
    public void setGrupo(GrupoNiveles grupo) {
        this.grupo = grupo;
    }
    
    public GrupoNiveles getSubGrupo() {
        return subGrupo;
    }
    
    public void setSubGrupo(GrupoNiveles subGrupo) {
        this.subGrupo = subGrupo;
    }

//    public DetalleItem getItemNuevo() {
//        return itemNuevo;
//    }
//
//    public void setItemNuevo(DetalleItem itemNuevo) {
//        this.itemNuevo = itemNuevo;
//    }
    public List<ConstatacionFisicaInventario> getListconst() {
        return listconst;
    }
    
    public List<DetalleConstFisicaInventario> getListDetalleConstFis() {
        return listDetalleConstFis;
    }
    
    public void setListDetalleConstFis(List<DetalleConstFisicaInventario> listDetalleConstFis) {
        this.listDetalleConstFis = listDetalleConstFis;
    }
    
    public void setListconst(List<ConstatacionFisicaInventario> listconst) {
        this.listconst = listconst;
    }
    
    public LazyModel<ConstatacionFisicaInventario> getLazyModel() {
        return lazyModel;
    }
    
    public void setLazyModel(LazyModel<ConstatacionFisicaInventario> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public Boolean getBolSubGrupo() {
        return bolSubGrupo;
    }
    
    public void setBolSubGrupo(Boolean bolSubGrupo) {
        this.bolSubGrupo = bolSubGrupo;
    }
    
    public List<GrupoNiveles> getListGrupoItems() {
        return listGrupoItems;
    }
    
    public void setListGrupoItems(List<GrupoNiveles> listGrupoItems) {
        this.listGrupoItems = listGrupoItems;
    }
    
    public List<GrupoNiveles> getListSubGrupoItems() {
        return listSubGrupoItems;
    }
    
    public void setListSubGrupoItems(List<GrupoNiveles> listSubGrupoItems) {
        this.listSubGrupoItems = listSubGrupoItems;
    }

//    public Boolean getBoleanoBtnBuscar() {
//        return boleanoBtnBuscar;
//    }
//
//    public void setBoleanoBtnBuscar(Boolean boleanoBtnBuscar) {
//        this.boleanoBtnBuscar = boleanoBtnBuscar;
//    }
    public Boolean getBanderaDos() {
        return banderaDos;
    }
    
    public void setBanderaDos(Boolean banderaDos) {
        this.banderaDos = banderaDos;
    }
    
    public Boolean getBanderaTres() {
        return banderaTres;
    }
    
    public void setBanderaTres(Boolean banderaTres) {
        this.banderaTres = banderaTres;
    }
    
    public Short getAnio() {
        return anio;
    }
    
    public void setAnio(Short anio) {
        this.anio = anio;
    }
    
    public Date getFechaDesde() {
        return fechaDesde;
    }
    
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
    
    public Date getFechaHasta() {
        return fechaHasta;
    }
    
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    
    public DetalleItem getItem() {
        return item;
    }
    
    public void setItem(DetalleItem item) {
        this.item = item;
    }
    
    public ArrayList<DetalleItem> getListDetalleSelect() {
        return listDetalleSelect;
    }
    
    public void setListDetalleSelect(ArrayList<DetalleItem> listDetalleSelect) {
        this.listDetalleSelect = listDetalleSelect;
    }
    
    public List<DetalleItem> getListItemsSeleccionados() {
        return listItemsSeleccionados;
    }
    
    public void setListItemsSeleccionados(List<DetalleItem> listItemsSeleccionados) {
        this.listItemsSeleccionados = listItemsSeleccionados;
    }
    
    public LazyModel<DetalleItem> getLazyItem() {
        return lazyItem;
    }
    
    public void setLazyItem(LazyModel<DetalleItem> lazyItem) {
        this.lazyItem = lazyItem;
    }
//</editor-fold>

}
