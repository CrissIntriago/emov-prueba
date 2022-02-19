package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.CatCiudadela;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Entities.CoaAbogado;
import com.gestionTributaria.Services.CoaAbogadoServices;
import com.gestionTributaria.Services.CoactivaService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatZonaSector;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class ReporteCoactivaMB implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private UserSession userSession;

    @Inject
    private CoactivaService coactivaService;
    @Inject
    private CoaAbogadoServices abogadoService;

    private Integer tipoReporte;

    private Integer tipoReporteEstadistico;

    private Date desde, hasta;

    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    private List<Short> mz_inicial;

    private List<Short> mz_final;

    private List<Short> sectores;

    /*Listado de sectores para reporte de locales comerciales*/
    private List<Short> sectoresLocales;

    private CatPredio catPredio;

    private Short manzanaIni;

    private Short manzanaFin;

    private List<Short> zonas;

    private List<CatCiudadela> ciudadelas;

    /*Captura la naturaleza del propietario para reporte de local comercial*/
    private Long naturaleza_propietario;

    private LazyModel<CatZonaSector> lazyCatpredio;

    private CatZonaSector predioSeleccionado;

    private String claveCatastralBuscar = "";

    private Date fechaDesde;

    private Date fechaHasta;

    private Date fechaDesde2;

    //private SimpleDateFormat formato;
    private Date fechaHasta2;

    private CoaAbogado abogado;
    private List<CoaAbogado> abogados;
    private Boolean rangoFechas;
    private Boolean rangoFechas2;

    public Boolean getRangoFechas2() {
        return rangoFechas2;
    }

    public void setRangoFechas2(Boolean rangoFechas2) {
        this.rangoFechas2 = rangoFechas2;
    }

    @PostConstruct
    public void init() {
        try {
            abogado = new CoaAbogado();
            abogados = abogadoService.getAllabogados();
//            zonas = coactivaService.findAllQuery("SELECT DISTINCT cp.zona FROM CatPredio cp where cp.zona IS NOT null ORDER BY cp.zona", null);
            ciudadelas = coactivaService.findAllQuery("SELECT ciu FROM CatCiudadela ciu ORDER BY ciu.nombre", null);
            catPredio = new CatPredio();
//            sectoresLocales = coactivaService.findAllQuery("SELECT DISTINCT cp.sector FROM CatPredio cp ORDER BY cp.sector", null);
            this.lazyCatpredio = new LazyModel<>(CatZonaSector.class);
            fechaDesde = new Date();
            fechaHasta = new Date();
            rangoFechas = false;
            rangoFechas2 = false;
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM");
            SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
            Calendar fecha = Calendar.getInstance();
            fechaDesde2 = new Date();
            fechaHasta2 = new Date();
            int anio = fecha.get(Calendar.YEAR);
            String fe1 = formato.format(fechaDesde2);
            String fec1 = "01/01/" + anio;
            String fe2 = formato.format(fechaHasta2);
            String fec2 = "31/12/" + anio;
            fechaDesde2 = formato2.parse(fec1);
            fechaHasta2 = formato2.parse(fec2);
            //Date fecha = formato.parse("23/11/2015");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*Carga las zonas por el sector para 
    Reporte de cartera por sector y manzana*/
    public void getSectoresByZona() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("zona", catPredio.getZona());
        sectores = coactivaService.findAllQuery("SELECT DISTINCT cp.sector FROM CatPredio cp where cp.zona=:zona ORDER BY cp.sector", params);
        System.out.println("sectores:" + sectores.size());
    }

    /*Carga las manzanas por el sector para 
    Reporte de cartera por sector y manzana*/
    public void manzanaBySector() {
        HashMap<String, Object> params = new HashMap<>();
//        params.put("sector", catPredio.getSector() );
        params.put("sector", predioSeleccionado.getSector());
        mz_inicial = coactivaService.findAllQuery("SELECT DISTINCT cp.mz FROM CatPredio cp where cp.sector=:sector ORDER BY mz", params);

        mz_final = coactivaService.findAllQuery("SELECT DISTINCT cp.mz FROM CatPredio cp where cp.sector=:sector ORDER BY mz DESC", params);
    }

    public void abrirDialogCatPredio() {
        JsfUtil.executeJS("PF('dlgCatPredio').show();");
        JsfUtil.update("formNuevoRequisito");
    }

    public void seleccionarCatPredio(CatZonaSector catPredio) {
        predioSeleccionado = catPredio;
        System.out.println("predioSeleccionado:" + predioSeleccionado.toString());
        manzanaBySector();
        JsfUtil.executeJS("PF('dlgCatPredio').hide();");
        JsfUtil.update("formMain");
    }

    public void limpiarCatPredioDatos() {
        predioSeleccionado = new CatZonaSector();
        mz_inicial = new ArrayList<>();
        mz_final = new ArrayList<>();
        JsfUtil.update("formMain");
    }

    public void imprimirReporte(boolean isEstadistico, boolean excel) {
        if (excel) {
            servletSession.setOnePagePerSheet(false);
            servletSession.setContentType("xlsx");
        }
        System.out.println("el estadistico tiene un valor de ----> " + isEstadistico);
        System.out.println("se imprime excel ----> " + excel);

        if (!isEstadistico && tipoReporte != null) {
//            System.out.println("entro aqui el sout---> "+isEstadistico);
            int a;
            switch (tipoReporte.intValue()) {
                case 1:

                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {
                        System.out.println("SIN RANGO DE FECHA");
                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                        if (predioSeleccionado == null) {
                            JsfUtil.addWarningMessage("ADVERTENCIA", "Debe ingresar la zona y el sector");
                            return;
                        }
                        if (manzanaIni == null) {
                            JsfUtil.addWarningMessage("MANZANA INICIAL", "Seleccione la manzana Inicial");
                            return;
                        }
                        if (manzanaFin == null) {
                            JsfUtil.addWarningMessage("MANZANA FINAL", "Seleccione la Manzana Final");
                            return;
                        }
                        servletSession.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                        servletSession.addParametro("sector_id", predioSeleccionado.getSector());
                        servletSession.addParametro("manzana_ini", manzanaIni);
                        servletSession.addParametro("manzana_fin", manzanaFin);

                    }
                    servletSession.setNombreReporte("deudaPorSector");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactiva");
                    limpiarCatPredioDatos();
                    break;
                case 2:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                        servletSession.addParametro("ciudadela_id", 0L);
                    } else {
                        System.out.println("SIN RANGO DE FECHA");
                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                        servletSession.addParametro("ciudadela_id", catPredio.getCiudadela().getId());
                        if (catPredio.getCiudadela() == null || catPredio.getCiudadela().getId() == null) {
                            JsfUtil.addWarningMessage("Ciudadela", "Seleccione la ciudadela");
                            return;
                        }
                    }

                    servletSession.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));

                    servletSession.setNombreReporte("deudaPorCiudadela");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactiva");

                    break;
                case 3:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {
                        int i = 0;
                        claveCatastralBuscar = "";
                        //parroquia
                        claveCatastralBuscar = "".equals(catPredio.getParroquia().toString()) ? "1." : catPredio.getParroquia().toString() + ".";
                        for (i = 0; i < 9; i++) {
                            //sector
                            if (i == 0) {
                                if ("".equals(catPredio.getSector().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getSector().toString() + ".";
                                }
                            }
                            //manzana
                            if (i == 1) {
                                if ("".equals(catPredio.getMz().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getMz().toString() + ".";
                                }
                            }
                            //solar
                            if (i == 2) {
                                if ("".equals(catPredio.getSolar().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getSolar().toString() + ".";
                                }
                            }
                            //div 1
                            if (i == 3) {
                                if ("".equals(catPredio.getDiv1().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getDiv1().toString() + ".";
                                }
                            }
                            //div 2
                            if (i == 4) {
                                if ("".equals(catPredio.getDiv2().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getDiv2().toString() + ".";
                                }
                            }
                            //div 3
                            if (i == 5) {
                                if ("".equals(catPredio.getDiv3().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getDiv3().toString() + ".";
                                }
                            }
                            //div 4
                            if (i == 6) {
                                if ("".equals(catPredio.getDiv4().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getDiv4().toString() + ".";
                                }
                            }
                            //div phv
                            if (i == 7) {
                                if ("".equals(catPredio.getPhv().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getPhv().toString() + ".";
                                }
                            }
                            //div phh
                            if (i == 8) {
                                if ("".equals(catPredio.getPhh().toString())) {
                                    break;
                                } else {
                                    claveCatastralBuscar = claveCatastralBuscar + catPredio.getPhh().toString();
                                }
                            }
                        }
                    }
                    servletSession.setNombreReporte("carteraVencidadXContribuyente");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    servletSession.addParametro("clave_cat", claveCatastralBuscar);
                    break;
                case 4:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {
                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                    }
                    servletSession.setNombreReporte("reporteCarteraTotal");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    break;
                case 5:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("desde", (fechaDesde));
                        servletSession.addParametro("hasta", (fechaHasta));
                    } else {
                        servletSession.addParametro("desde", null);
                        servletSession.addParametro("hasta", null);

                    }

                    servletSession.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                    servletSession.setNombreReporte("coactivaPorRangoFecha");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactiva");
                    break;
                case 7:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {
                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                    }
                    servletSession.setNombreReporte("reporteCarteraTotalRural");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    break;

                case 8:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {

                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                        if (predioSeleccionado.getSector() == null) {
                            JsfUtil.addWarningMessage("Sector", "Debe ingresar el sector");
                            return;
                        }

                        if (naturaleza_propietario == null) {
                            JsfUtil.addWarningMessage("NATURALEZA", "Debe ingresar la naturaleza del propietario");
                            return;
                        }
                        servletSession.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                        servletSession.addParametro("sector_id", predioSeleccionado.getSector());
                        servletSession.addParametro("naturaleza_id", naturaleza_propietario);
                    }
                    servletSession.setNombreReporte("localesComerciales");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactiva");
                    limpiarCatPredioDatos();
                    break;

                case 9:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("DESDE", (fechaDesde));
                        servletSession.addParametro("HASTA", (fechaHasta));
                    } else {
                        servletSession.addParametro("DESDE", null);
                        servletSession.addParametro("HASTA", null);
                    }
                    servletSession.addParametro("IMG_URL", JsfUtil.getRealPath("resources/images/escudo_duran.png"));
                    servletSession.setNombreReporte("exFuncionarios");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactiva");
                    break;
                case 10:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("FECHADESDE", Utils.getAnio(fechaDesde));
                        servletSession.addParametro("FECHAHASTA", Utils.getAnio(fechaHasta));
                    } else {
                        servletSession.addParametro("FECHADESDE", null);
                        servletSession.addParametro("FECHAHASTA", null);
                    }
                    servletSession.setNombreReporte("juiciosPendientesPago");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/recuperacionCarteraJuicio");

                    break;
                case 11:
                    if (rangoFechas == true) {
                        System.out.println("CON RANGO DE FECHA");
                        servletSession.addParametro("FECHADESDE", fechaDesde);
                        servletSession.addParametro("FECHAHASTA", fechaHasta);
                    } else {
                        servletSession.addParametro("FECHADESDE", null);
                        servletSession.addParametro("FECHAHASTA", null);

                    }
                    servletSession.setNombreReporte("recuperacionCartera");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/recuperacionCarteraJuicio");
                    servletSession.addParametro("idAbogado", abogado == null ? null : abogado.getId());
                    break;
            }
        } else {
//            System.out.println("entro aqui el sout---QUE ES> "+isEstadistico);
            int a;
            if (tipoReporteEstadistico == null) {
                JsfUtil.addWarningMessage("TIPO REPORTE", "Seleccione un tipo de reporte");
                return;
            }

            switch (tipoReporteEstadistico) {
                case 1:
                    if (rangoFechas2 == null) {
                        JsfUtil.addWarningMessage("ATENCION", "Debe seleccionar Rango de fechas");
                        return;

                    }
                    System.out.println("la fecha desde es:---> " + fechaDesde2);
                    System.out.println("la fecha hasta es:---> " + fechaHasta2);
                    servletSession.addParametro("DESDE", fechaDesde2);
                    servletSession.addParametro("HASTA", fechaHasta2);
                    servletSession.setNombreReporte("ESTADISTICAXANIO");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    break;
                case 2:
                    if (rangoFechas2 == null) {
                        JsfUtil.addWarningMessage("ATENCION", "Debe seleccionar Rango de fechas");
                        return;

                    }
                    System.out.println("la fecha desde es:---> " + fechaDesde2);
                    System.out.println("la fecha hasta es:---> " + fechaHasta2);
                    servletSession.addParametro("DESDE", fechaDesde2);
                    servletSession.addParametro("HASTA", fechaHasta2);
                    servletSession.setNombreReporte("ESTADISTICASXSECTOR");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    break;
                case 3:
                    if (rangoFechas2 == null) {
                        JsfUtil.addWarningMessage("ATENCION", "Debe seleccionar Rango de fechas");
                        return;

                    }
                    System.out.println("la fecha desde es:---> " + fechaDesde2);
                    System.out.println("la fecha hasta es:---> " + fechaHasta2);
                    servletSession.addParametro("DESDE", fechaDesde2);
                    servletSession.addParametro("HASTA", fechaHasta2);
                    servletSession.setNombreReporte("ESTADISTICASXCIUDADELAS");
                    servletSession.setNombreSubCarpeta("GestionTributatia/Coactiva/reportesCoactivaEstadisticos");
                    break;
            }
        }

        servletSession.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public Integer getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(Integer tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public Integer getTipoReporteEstadistico() {
        return tipoReporteEstadistico;
    }

    public void setTipoReporteEstadistico(Integer tipoReporteEstadistico) {
        this.tipoReporteEstadistico = tipoReporteEstadistico;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public List<Short> getMz_inicial() {
        return mz_inicial;
    }

    public void setMz_inicial(List<Short> mz_inicial) {
        this.mz_inicial = mz_inicial;
    }

    public List<Short> getMz_final() {
        return mz_final;
    }

    public void setMz_final(List<Short> mz_final) {
        this.mz_final = mz_final;
    }

    public List<Short> getZonas() {
        return zonas;
    }

    public void setZonas(List<Short> zonas) {
        this.zonas = zonas;
    }

    public List<Short> getSectores() {
        return sectores;
    }

    public void setSectores(List<Short> sectores) {
        this.sectores = sectores;
    }

    public CatPredio getCatPredio() {
        return catPredio;
    }

    public void setCatPredio(CatPredio catPredio) {
        this.catPredio = catPredio;
    }

    public Short getManzanaIni() {
        return manzanaIni;
    }

    public void setManzanaIni(Short manzanaIni) {
        this.manzanaIni = manzanaIni;
    }

    public Short getManzanaFin() {
        return manzanaFin;
    }

    public void setManzanaFin(Short manzanaFin) {
        this.manzanaFin = manzanaFin;
    }

    public List<CatCiudadela> getCiudadelas() {
        return ciudadelas;
    }

    public void setCiudadelas(List<CatCiudadela> ciudadelas) {
        this.ciudadelas = ciudadelas;
    }

    public Long getNaturaleza_propietario() {
        return naturaleza_propietario;
    }

    public void setNaturaleza_propietario(Long naturaleza_propietario) {
        this.naturaleza_propietario = naturaleza_propietario;
    }

    public List<Short> getSectoresLocales() {
        return sectoresLocales;
    }

    public void setSectoresLocales(List<Short> sectoresLocales) {
        this.sectoresLocales = sectoresLocales;
    }

    public LazyModel<CatZonaSector> getLazyCatpredio() {
        return lazyCatpredio;
    }

    public void setLazyCatpredio(LazyModel<CatZonaSector> lazyCatpredio) {
        this.lazyCatpredio = lazyCatpredio;
    }

    public CatZonaSector getPredioSeleccionado() {
        return predioSeleccionado;
    }

    public void setPredioSeleccionado(CatZonaSector predioSeleccionado) {
        this.predioSeleccionado = predioSeleccionado;
    }

    public CoaAbogado getAbogado() {
        return abogado;
    }

    public void setAbogado(CoaAbogado abogado) {
        this.abogado = abogado;
    }

    public List<CoaAbogado> getAbogados() {
        return abogados;
    }

    public void setAbogados(List<CoaAbogado> abogados) {
        this.abogados = abogados;
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

    public Boolean getRangoFechas() {
        return rangoFechas;
    }

    public void setRangoFechas(Boolean rangoFechas) {
        this.rangoFechas = rangoFechas;
    }

    public Date getFechaDesde2() {
        return fechaDesde2;
    }

    public void setFechaDesde2(Date fechaDesde2) {
        this.fechaDesde2 = fechaDesde2;
    }

    public Date getFechaHasta2() {
        return fechaHasta2;
    }

    public void setFechaHasta2(Date fechaHasta2) {
        this.fechaHasta2 = fechaHasta2;
    }
}
