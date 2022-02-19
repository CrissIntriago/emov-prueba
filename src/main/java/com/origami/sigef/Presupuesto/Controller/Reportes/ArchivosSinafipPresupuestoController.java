/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.Presupuesto.Model.EstructuraArchivosPlanosPresupuesto;
import com.origami.sigef.Presupuesto.Service.EjecucionPresupuestariaService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ControlPresupuestarioService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "archivosSinafipPresupuestoView")
@ViewScoped
public class ArchivosSinafipPresupuestoController implements Serializable {

    private OpcionBusqueda busqueda;
    private Presupuesto presupuesto;

    private String tipoArchivo;
    private String fechaDesde;
    private String fechaHasta;
    private Integer mes;
    private boolean bolMeses;
    private boolean mostrarTablaPresupuesto;
    private boolean mostrarDatosCedula;
    private boolean mostrarDatosIniciales;

    private final String CODIGO_PRESUPUESTO_INICIAL = "PI";
    private final String CODIGO_CEDULA_PRESUPUESTARIA = "CP";

    private List<MasterCatalogo> periodos;
    private List<CatalogoItem> mesesAnio;
    private List<PresCatalogoPresupuestario> listItem;
    private List<EstructuraArchivosPlanosPresupuesto> listEstructuraArchivos;
    private List<Presupuesto> listPresupuesto;

    private final DecimalFormat formatInt = new DecimalFormat("00");

    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private CatalogoItemService catalogoItemService;

    @Inject
    private ValoresService valoresService;

    @Inject
    private PresCatalogoPresupuestarioService presCatalogoPresupuestarioService;

    @Inject
    private EjecucionPresupuestariaService ejecucionService;

    @Inject
    private ServletSession servletSession;

    @Inject
    private ControlPresupuestarioService controlPresupuestarioService;

    @PostConstruct
    public void initView() {
        loadView();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        if (periodos.isEmpty() && periodos.size() < 1) {
            busqueda = null;
        }
        mesesAnio = catalogoItemService.findByNamedQuery("CatalogoItem.findByCatalogo", "meses_anio");
    }

    public void loadView() {
        busqueda = new OpcionBusqueda();
        listEstructuraArchivos = new ArrayList<>();
        listItem = new ArrayList<>();
        tipoArchivo = "";
        fechaDesde = "";
        fechaHasta = "";
        mes = 0;
        bolMeses = Boolean.FALSE;
        mostrarTablaPresupuesto = Boolean.FALSE;
        mostrarDatosCedula = Boolean.FALSE;
        mostrarDatosIniciales = Boolean.FALSE;
    }

    public void actualizarDatosView() {
        listItem = new ArrayList<>();
//        diarioApertura = new DiarioGeneral();
//        claseDiarioApertura = Boolean.FALSE;
//        mensajeCuentaRendered = Boolean.FALSE;
        bolMeses = Boolean.FALSE;
        mostrarTablaPresupuesto = Boolean.FALSE;
        mostrarDatosCedula = Boolean.FALSE;
        mostrarDatosIniciales = Boolean.FALSE;
        if (tipoArchivo != null) {
            Long niv = 4L;
            if (tipoArchivo.equals(CODIGO_PRESUPUESTO_INICIAL)) {
                listItem = presCatalogoPresupuestarioService.getListPresupuestoForArchivoPlano(busqueda.getAnio(), niv.shortValue());
                mostrarTablaPresupuesto = Boolean.TRUE;
                mostrarDatosIniciales = Boolean.TRUE;
                for (PresCatalogoPresupuestario catalogoPresupuesto : listItem) {
                    if (!catalogoPresupuesto.getIngreso()) {
                        catalogoPresupuesto.setPresupuestoInicial(getPresupuestoInicial(catalogoPresupuesto.getCodigo()));
                    } else {
                        catalogoPresupuesto.setPresupuestoInicial(getPresupuestoInicialIngresos(catalogoPresupuesto.getCodigo()));
                    }
                    if (catalogoPresupuesto.getCodigo().startsWith("51") || catalogoPresupuesto.getCodigo().startsWith("71")) {
                        catalogoPresupuesto.setOrientacionGastos("99999999");
                    } else {
                        catalogoPresupuesto.setOrientacionGastos("01011100");
                    }
                }
            }
            if (tipoArchivo.equals(CODIGO_CEDULA_PRESUPUESTARIA)) {
                bolMeses = Boolean.TRUE;
            }
        }
    }

    public void formatFecha() {
        listItem = new ArrayList<>();
        mostrarTablaPresupuesto = Boolean.FALSE;
        mostrarDatosCedula = Boolean.FALSE;
        mostrarDatosIniciales = Boolean.FALSE;
        if (busqueda == null) {
            JsfUtil.addErrorMessage("", "Seleccione un período para generar los datos");
            return;
        }
        if (mes != null && busqueda.getAnio() != null) {
            Integer mesControl = 0;
            mesControl = mes - 1;
            if (!controlPresupuestarioService.getControlPresupuestarioByMes(busqueda.getAnio(), mesControl)) {
                String mesString;
                if (mes.toString().length() == 1) {
                    mesString = "0" + mes;
                } else {
                    mesString = "" + mes;
                }
                fechaDesde = busqueda.getAnio() + "-" + mesString;
                fechaHasta = busqueda.getAnio() + "-01";

                generarCedulaPresupuestariaForEditar();
            } else {
                JsfUtil.addErrorMessage("CONTROL PRESUPUESTARIO", "Cuentas Presupuestarias del mes ABIERTA \n No se puede Generar el Archivo.");
            }
        } else {
            JsfUtil.addErrorMessage("", "Seleccione un mes para generar los datos");
        }
    }

    public void generarCedulaPresupuestariaForEditar() {
        Long niv = 4L;
        listItem = presCatalogoPresupuestarioService.getListPresupuestoForArchivoPlano(busqueda.getAnio(), niv.shortValue());
        if (!listItem.isEmpty()) {

            for (PresCatalogoPresupuestario catalogoPresupuesto : listItem) {
                BigDecimal presupuestoInicial = BigDecimal.ZERO;
                BigDecimal reforma = BigDecimal.ZERO;
                BigDecimal codificado = BigDecimal.ZERO;
                BigDecimal devengado = BigDecimal.ZERO;
                BigDecimal comprometido = BigDecimal.ZERO;
                if (catalogoPresupuesto.getIngreso()) {
                    presupuestoInicial = getPresupuestoInicialIngresos(catalogoPresupuesto.getCodigo());
//                    presupuestoInicial = catalogoPresupuesto.getPresupuestoInicial();
//                    if(presupuestoInicial == null){
//                        presupuestoInicial = BigDecimal.ZERO;
//                    }
                    reforma = ejecucionService.getReformaByPartidaFechaDesdeHastaIngresos(busqueda.getAnio(), new Date(), new Date(), catalogoPresupuesto.getCodigo(), "AP", fechaHasta, fechaDesde);

                } else {
                    presupuestoInicial = getPresupuestoInicial(catalogoPresupuesto.getCodigo());
                    reforma = ejecucionService.getReformaEgresos(busqueda.getAnio(), new Date(), new Date(), catalogoPresupuesto.getCodigo(), "AP", fechaHasta, fechaDesde, catalogoPresupuesto.getMovimiento());
//                    System.out.println("PresupuestoInicial: " + presupuestoInicial);
//                    System.out.println("Reforma: " + reforma);
                }

                codificado = presupuestoInicial.add(reforma);
//                System.out.println("Codificado: " + codificado);
                devengado = ejecucionService.getDevengadoByCodigoItemPresupuestario(busqueda.getAnio(), fechaHasta, fechaDesde, catalogoPresupuesto.getCodigo());
                comprometido = ejecucionService.getComprometidoEgresosByCodigoItemPresupuestario(busqueda.getAnio(), fechaHasta, fechaDesde, catalogoPresupuesto.getCodigo());

                catalogoPresupuesto.setPresupuestoInicial(presupuestoInicial);
                catalogoPresupuesto.setReformasByFechas(reforma);
                catalogoPresupuesto.setCodificado(codificado);
                catalogoPresupuesto.setDevengado(devengado);
                catalogoPresupuesto.setCompromiso(comprometido);
                if (catalogoPresupuesto.getCodigo().startsWith("51") || catalogoPresupuesto.getCodigo().startsWith("71")) {
                    catalogoPresupuesto.setOrientacionGastos("99999999");
                } else {
                    catalogoPresupuesto.setOrientacionGastos("01011100");
                }
            }
            mostrarTablaPresupuesto = Boolean.TRUE;
            mostrarDatosCedula = Boolean.TRUE;
            mostrarDatosIniciales = Boolean.FALSE;
        }
    }

    public void reforma(String codigo) {

//        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
//        Date desde = null;
//        Date hasta = null;
//        try {
//            desde = formato.parse(fechaHasta+"/01");
//            hasta = formato.parse(fechaDesde+"/01");
//        } catch (ParseException ex) {
//            Logger.getLogger(ArchivosSinafipPresupuestoController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public BigDecimal getPresupuestoInicial(String codigo) {
        BigDecimal valor = ejecucionService.getPresupuestoInicialEgresosByCatalogoPresupuesto(busqueda.getAnio(), "", codigo);
        return valor;
    }

    public BigDecimal getPresupuestoInicialIngresos(String codigo) {
        BigDecimal valor = ejecucionService.getPresupuestoInicialIngresosByCatalogoPresupuesto(busqueda.getAnio(), "", codigo);
        return valor;
    }

    public void generarArchivoPlano() {
        String ruta = "";
        if (tipoArchivo != null) {
            switch (tipoArchivo) {
                case CODIGO_PRESUPUESTO_INICIAL:
                    ruta = valoresService.findByCodigo("RUTA_ARCHIVO_PRESUPUESTO_INICIAL") + "PresupuestoInicial_" + busqueda.getAnio() + ".txt";
                    if (!listItem.isEmpty()) {
                        for (PresCatalogoPresupuestario cp : listItem) {
                            if (!cp.getIngreso()) {
                                if (cp.getOrientacionGastos() == null) {
                                    JsfUtil.addErrorMessage("", "Todas los registros de Tipo Egresos debe tener asignado una Orientación de Gastos");
                                    return;
                                }
                            }
                        }
                        for (PresCatalogoPresupuestario catalogoPresupuesto : listItem) {
                            if (catalogoPresupuesto.getPresupuestoInicial().doubleValue() != 0) {
                                if (catalogoPresupuesto.getIngreso()) {
                                    listEstructuraArchivos.add(setEstructuraPresupuestoInicialIngresos(catalogoPresupuesto));
                                } else {

                                    listEstructuraArchivos.add(setEstructuraPresupuestoInicialEgresos(catalogoPresupuesto));
                                }
                            }
                        }
                    }
                    break;
                case CODIGO_CEDULA_PRESUPUESTARIA:
                    ruta = valoresService.findByCodigo("RUTA_ARCHIVO_CEDULA_PRESUPUESTARIA") + "CedulaPresupuestaria_" + fechaDesde + ".txt";
                    if (!listItem.isEmpty()) {
                        for (PresCatalogoPresupuestario cp : listItem) {
                            if (!cp.getIngreso()) {
                                if (cp.getOrientacionGastos() == null) {
                                    JsfUtil.addErrorMessage("", "Todas los registros de Tipo Egresos debe tener asignado una Orientación de Gastos");
                                    return;
                                }
                            }
                        }
                        for (PresCatalogoPresupuestario catalogoPresupuesto : listItem) {
                            if ((catalogoPresupuesto.getPresupuestoInicial().doubleValue() != 0 && catalogoPresupuesto.getReformasByFechas().doubleValue() == 0)
                                    || (catalogoPresupuesto.getPresupuestoInicial().doubleValue() == 0 && catalogoPresupuesto.getReformasByFechas().doubleValue() != 0)
                                    || (catalogoPresupuesto.getPresupuestoInicial().doubleValue() != 0 && catalogoPresupuesto.getReformasByFechas().doubleValue() != 0)) {
                                if (catalogoPresupuesto.getIngreso()) {
                                    BigDecimal recaudado = BigDecimal.ZERO;
                                    BigDecimal saldoxDevengar = BigDecimal.ZERO;
                                    recaudado = ejecucionService.getRecaudadoIngresosByCodigoItemPresupuestario(busqueda.getAnio(), fechaHasta, fechaDesde, catalogoPresupuesto.getCodigo());
                                    saldoxDevengar = catalogoPresupuesto.getCodificado().subtract(catalogoPresupuesto.getDevengado());
                                    listEstructuraArchivos.add(setEstructuraCedulaPresupuestariaIngresos(catalogoPresupuesto, mes, recaudado.doubleValue(), saldoxDevengar.doubleValue()));
                                } else {
                                    BigDecimal pagado = BigDecimal.ZERO;
                                    BigDecimal saldoxComprometer = BigDecimal.ZERO;
                                    BigDecimal saldoxDevengar = BigDecimal.ZERO;

                                    pagado = ejecucionService.getPagadoEgresosByCodigoItemPresupuestario(busqueda.getAnio(), fechaHasta, fechaDesde, catalogoPresupuesto.getCodigo());
                                    saldoxComprometer = catalogoPresupuesto.getCodificado().subtract(catalogoPresupuesto.getCompromiso());
                                    saldoxDevengar = catalogoPresupuesto.getCodificado().subtract(catalogoPresupuesto.getDevengado());
                                    listEstructuraArchivos.add(setEstructuraCedulaPresupuestariaEgresos(catalogoPresupuesto, mes, pagado.doubleValue(), saldoxComprometer.doubleValue(), saldoxDevengar.doubleValue()));
                                }
                            } else {
                            }

                        }
                    }
                    break;
            }
            if (createFileArchivoText(ruta, listEstructuraArchivos, tipoArchivo)) {
                //aqui viene la descarga
                downloadFile(ruta);
                JsfUtil.addSuccessMessage("", "Archivo Generado Correctamente");
            }
        }
    }

    public void cancelar() {
//        claseDiarioApertura = Boolean.FALSE;
        busqueda = new OpcionBusqueda();
        tipoArchivo = "";
        mes = 0;
        bolMeses = Boolean.FALSE;
        mostrarTablaPresupuesto = Boolean.FALSE;
        mostrarDatosCedula = Boolean.FALSE;
        mostrarDatosIniciales = Boolean.FALSE;
//        estructuraArchivos = new ArrayList<>();
        fechaDesde = "";
        fechaHasta = "";
//        tipoDiario = "";
        listItem = new ArrayList<>();
//        mostrarDatatableTransacccionesR = Boolean.FALSE;
//        aggProveedores = new ArrayList<>();
    }

    private Boolean createFileArchivoText(String ruta, List<EstructuraArchivosPlanosPresupuesto> estructuras, String tipo) {
        try {
            DecimalFormat format = new DecimalFormat("#0.00");
            if (!estructuras.isEmpty()) {
                if (tipo.equals(CODIGO_PRESUPUESTO_INICIAL)) {
                    try ( PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                        estructuras.forEach((e) -> {
                            if (e.getTipoPresupuesto().equals("I")) {
                                writer.println(e.getPeriodo() + "|" + e.getTipoPresupuesto() + "|" + e.getGrupo() + "|" + e.getSubgrupo() + "|" + e.getItem()
                                        + "|" + format.format(e.getValor()).replace(",", "."));
                            } else if (e.getTipoPresupuesto().equals("G")) {
                                writer.println(e.getPeriodo() + "|" + e.getTipoPresupuesto() + "|" + e.getGrupo() + "|" + e.getSubgrupo() + "|" + e.getItem()
                                        + "|" + e.getFuncion() + "|" + e.getOrientacionGasto() + "|" + format.format(e.getValor()).replace(",", "."));
                            }
                        });
                    }
                    return true;
                }
                if (tipo.equals(CODIGO_CEDULA_PRESUPUESTARIA)) {
                    try ( PrintWriter writer = new PrintWriter(ruta, "UTF-8")) {
                        estructuras.forEach((e) -> {
                            if (e.getTipoPresupuesto().equals("I")) {
                                writer.println(e.getPeriodo() + "|" + e.getTipoPresupuesto() + "|" + e.getGrupo() + "|" + e.getSubgrupo() + "|" + e.getItem()
                                        + "|" + format.format(e.getValor()).replace(",", ".") + "|" + format.format(e.getReforma()).replace(",", ".") + "|" + format.format(e.getCodificado()).replace(",", ".")
                                        + "|" + format.format(e.getDevengado()).replace(",", ".") + "|" + format.format(e.getRecaudadoPagado()).replace(",", ".") + "|" + format.format(e.getSaldoPorDevengar()).replace(",", "."));
                            } else if (e.getTipoPresupuesto().equals("G")) {
                                writer.println(e.getPeriodo() + "|" + e.getTipoPresupuesto() + "|" + e.getGrupo() + "|" + e.getSubgrupo() + "|" + e.getItem()
                                        + "|" + e.getFuncion() + "|" + e.getOrientacionGasto() + "|" + format.format(e.getValor()).replace(",", ".") + "|" + format.format(e.getReforma()).replace(",", ".")
                                        + "|" + format.format(e.getCodificado()).replace(",", ".") + "|" + format.format(e.getCompromiso()).replace(",", ".") + "|" + format.format(e.getDevengado()).replace(",", ".") + "|"
                                        + format.format(e.getRecaudadoPagado()).replace(",", ".") + "|" + format.format(e.getSaldoPorComprometer()).replace(",", ".") + "|" + format.format(e.getSaldoPorDevengar()).replace(",", "."));
                            }
                        });
                    }
                    return true;
                }
            } else {
                try ( PrintWriter writer = new PrintWriter(ruta, "UTF8")) {
                    writer.print("");
                }
                return true;
            }
            JsfUtil.addInformationMessage("", "No existen datos que generar");
            return false;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            return false;
        }
    }

    private void downloadFile(String ruta) {
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        servletSession.setNombreDocumento(ruta);
        servletSession.setContentType("text/plain");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/ViewSystemDocs");
        loadView();
    }

    /*ESTRUCTURAS PARA OBTENER LA DATA Y SETEARLA A LA ESTRUCTURA DEL ARCHIVO PLANO CORRESPONDIENTE*/
    public EstructuraArchivosPlanosPresupuesto setEstructuraPresupuestoInicialIngresos(PresCatalogoPresupuestario pre) {
        System.out.println("Presupuesto: " + pre.getCodigo());
        System.out.println("Presupuesto: " + pre.getPadre().getPadre().getCodigo());
        System.out.println("Presupuesto: substring " + pre.getPadre().getCodigo().substring(2, 4));
        System.out.println("Presupuesto: substring " + pre.getCodigo().substring(4, 6));
        EstructuraArchivosPlanosPresupuesto plano = new EstructuraArchivosPlanosPresupuesto("01", "I",
                pre.getPadre().getPadre().getCodigo(),
                Utils.completarCadenaConCeros(pre.getPadre().getCodigo().substring(2, 4), 2), Utils.completarCadenaConCeros(pre.getCodigo().substring(4, 6), 2),
                pre.getPresupuestoInicial().doubleValue());
        return plano;
    }

    public EstructuraArchivosPlanosPresupuesto setEstructuraPresupuestoInicialEgresos(PresCatalogoPresupuestario pre) {
        System.out.println("Eg Presupuesto: " + pre.getCodigo());
        System.out.println("EgPresupuesto: " + pre.getPadre().getPadre().getCodigo());
        System.out.println("EgPresupuesto: substring " + pre.getPadre().getCodigo().substring(2, 4));
        System.out.println("EgPresupuesto: substring 2: " + pre.getCodigo().substring(4, 6));
        if (pre.getCodigo().startsWith("51") || pre.getCodigo().startsWith("71")) {
            pre.setOrientacionGastos("99999999");
        } else {
            pre.setOrientacionGastos("01011100");
        }
        EstructuraArchivosPlanosPresupuesto plano = new EstructuraArchivosPlanosPresupuesto("01", "G",
                pre.getPadre().getPadre().getCodigo(),
                Utils.completarCadenaConCeros(pre.getPadre().getCodigo().substring(2, 4), 2), Utils.completarCadenaConCeros(pre.getCodigo().substring(4, 6), 2),
                "000", pre.getOrientacionGastos(), pre.getPresupuestoInicial().doubleValue());
        return plano;
    }

    public EstructuraArchivosPlanosPresupuesto setEstructuraCedulaPresupuestariaIngresos(PresCatalogoPresupuestario pre, Integer meses, Double recaudado, Double saldoPorDevengar) {
        System.out.println("IPresupuesto: " + pre.getCodigo());
        System.out.println("IPresupuesto: " + pre.getPadre().getPadre().getCodigo());
        System.out.println("IPresupuesto: substring " + pre.getPadre().getCodigo().substring(2, 4));
        System.out.println("IPresupuesto: substring 2" + pre.getCodigo().substring(4, 6));
        EstructuraArchivosPlanosPresupuesto plano = new EstructuraArchivosPlanosPresupuesto(formatInt.format(meses), "I",
                pre.getPadre().getPadre().getCodigo(),
                Utils.completarCadenaConCeros(pre.getPadre().getCodigo().substring(2, 4), 2), Utils.completarCadenaConCeros(pre.getCodigo().substring(4, 6), 2),
                pre.getPresupuestoInicial().doubleValue(), pre.getReformasByFechas().doubleValue(), pre.getCodificado().doubleValue(), pre.getDevengado().doubleValue(),
                recaudado, saldoPorDevengar);
        return plano;
    }

    public EstructuraArchivosPlanosPresupuesto setEstructuraCedulaPresupuestariaEgresos(PresCatalogoPresupuestario pre, Integer meses, Double pagado, Double saldoPorComprometer, Double saldoPorDevengar) {
        EstructuraArchivosPlanosPresupuesto plano = new EstructuraArchivosPlanosPresupuesto(formatInt.format(meses), "G",
                pre.getPadre().getPadre().getCodigo(),
                Utils.completarCadenaConCeros(pre.getPadre().getCodigo().substring(2, 4), 2), Utils.completarCadenaConCeros(pre.getCodigo().substring(4, 6), 2),
                "000", pre.getOrientacionGastos(), pre.getPresupuestoInicial().doubleValue(), pre.getReformasByFechas().doubleValue(),
                pre.getCodificado().doubleValue(), pre.getCompromiso().doubleValue(), pre.getDevengado().doubleValue(),
                pagado, saldoPorComprometer, saldoPorDevengar);
        return plano;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public boolean isBolMeses() {
        return bolMeses;
    }

    public void setBolMeses(boolean bolMeses) {
        this.bolMeses = bolMeses;
    }

    public boolean isMostrarTablaPresupuesto() {
        return mostrarTablaPresupuesto;
    }

    public void setMostrarTablaPresupuesto(boolean mostrarTablaPresupuesto) {
        this.mostrarTablaPresupuesto = mostrarTablaPresupuesto;
    }

    public boolean isMostrarDatosCedula() {
        return mostrarDatosCedula;
    }

    public void setMostrarDatosCedula(boolean mostrarDatosCedula) {
        this.mostrarDatosCedula = mostrarDatosCedula;
    }

    public boolean isMostrarDatosIniciales() {
        return mostrarDatosIniciales;
    }

    public void setMostrarDatosIniciales(boolean mostrarDatosIniciales) {
        this.mostrarDatosIniciales = mostrarDatosIniciales;
    }

    public List<EstructuraArchivosPlanosPresupuesto> getListEstructuraArchivos() {
        return listEstructuraArchivos;
    }

    public void setListEstructuraArchivos(List<EstructuraArchivosPlanosPresupuesto> listEstructuraArchivos) {
        this.listEstructuraArchivos = listEstructuraArchivos;
    }

    public List<Presupuesto> getListPresupuesto() {
        return listPresupuesto;
    }

    public void setListPresupuesto(List<Presupuesto> listPresupuesto) {
        this.listPresupuesto = listPresupuesto;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<CatalogoItem> getMesesAnio() {
        return mesesAnio;
    }

    public void setMesesAnio(List<CatalogoItem> mesesAnio) {
        this.mesesAnio = mesesAnio;
    }

    public List<PresCatalogoPresupuestario> getListItem() {
        return listItem;
    }

    public void setListItem(List<PresCatalogoPresupuestario> listItem) {
        this.listItem = listItem;
    }

}
