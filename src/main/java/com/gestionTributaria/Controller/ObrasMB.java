/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.*;
import com.gestionTributaria.Entities.*;
import com.gestionTributaria.Services.CatUbicacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.PredioCemCiudadelaManzanaDTO;
import com.gestionTributaria.models.PredioCemDTO;
import com.gestionTributaria.models.predioCemClaveNumPredioDTO;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "obras")
@ViewScoped
public class ObrasMB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ManagerService manager;
    @Inject
    private ServletSession servletSession;
    @Inject
    private UserSession uSession;
    @Inject
    private CatUbicacionService ubicacionesService;
    private LazyModel<Obra> obras;
    private Obra obra;
    private List<TipoObra> tiposObra;
    private List<CatUbicacion> ubicaciones;
    private List<CatUbicacion> ubicacionesSeleccionadas;
    private Integer anioReporte;
    private List<ContCuentas> cuentas;
    private Boolean nextPage = Boolean.FALSE;
    private String tipoDefinicion;
    private List<PredioCemDTO> sectoresPredios;
    private List<PredioCemDTO> prediosDTOSeleccionados;
    private List<PredioCemCiudadelaManzanaDTO> ciudadelaSector;
    private List<PredioCemCiudadelaManzanaDTO> ciudadelaSectorSeleccionados;
    private List<predioCemClaveNumPredioDTO> predioClaveNumPredio;
    private List<predioCemClaveNumPredioDTO> predioClaveNumPredioSeleccionados;
    private LazyModel<CatPredio> predios;
    private List<CatPredio> catPredios;
    private List<CatCiudadela> catCiudadelas;
    private List<CatCiudadela> catCiudadelasSeleccionadas;
    private CatCiudadela ciudadela;
    private String formaCalculo;
    private Integer valorCalculo1;
    private Integer valorCalculo2;
    private List<FinaRenRubrosLiquidacion> rubrosList;
    private FinaRenRubrosLiquidacion rubroSelected;
    private Map<String, Object> parametros = new HashMap<>();
    private CatUbicacion ubicacion;
    private List<CatParroquia> parroquias;
    private CatParroquia parroquia;
    private List<CatParroquia> parroquiaSeleccionadas;

    @PostConstruct
    public void initView() {
        try {
            obras = new LazyModel<>(Obra.class);
            parametros.put("estado", Boolean.TRUE);
            tiposObra = manager.findObjectByParameterList(TipoObra.class, parametros);
            obra = new Obra();
            tipoDefinicion = "Parroquia";
            ubicacion = new CatUbicacion();
            ubicacionesSeleccionadas = new ArrayList<>();
            prediosDTOSeleccionados = new ArrayList<>();
            ciudadelaSectorSeleccionados = new ArrayList<>();
            predioClaveNumPredioSeleccionados = new ArrayList();
            parroquias = new ArrayList<>();
            ubicaciones = new ArrayList<>();
            parroquiaSeleccionadas = new ArrayList<>();
            loadRegistersObras();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRegistersObras() {
        parametros = new HashMap<>();
        sectoresPredios = ubicacionesService.getUbicaciones();
        ciudadelaSector = ubicacionesService.getUbicacionesCiudadelaMz();
        predioClaveNumPredio = ubicacionesService.getUbicacionesPredio();
        parametros.put("tipoLiq", Arrays.asList(2L, 3L));
        this.rubrosList = manager.findAllQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id in (:tipoLiq) AND r.estado = true AND r.codigoRubro IS NOT NULL ORDER BY r.codigoRubro ASC", parametros);
    }

    public SelectItem[] getListTipoObras() {
        int cantRegis = tiposObra.size();
        SelectItem[] options = new SelectItem[cantRegis + 1];
        options[0] = new SelectItem("", "Seleccione");
        for (int i = 0; i < cantRegis; i++) {
            options[i + 1] = new SelectItem(tiposObra.get(i).getDescripcion(), tiposObra.get(i).getDescripcion());
        }
        return options;
    }

//    public SelectItem[] getListUbicaciones() {
//        int cantRegis = ubicaciones.size();
//        SelectItem[] options = new SelectItem[cantRegis + 1];
//        options[0] = new SelectItem("", "Seleccione");
//        for (int i = 0; i < cantRegis; i++) {
//            options[i + 1] = new SelectItem(ubicaciones.get(i).getNombre(), ubicaciones.get(i).getNombre());
//        }
//        return options;
//    }
    public void eliminarParroquiaSeleccion(CatParroquia parroquia) {
        parroquias.remove(parroquia);
    }

    public void eliminarCiudadelaSeleccion(CatUbicacion ciudadela) {
        ubicacionesSeleccionadas.remove(ciudadela);
    }

    public void registerObraPage() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "moduloGestionTributario/Mejoras/_registroObras.xhtml");
        nextPage = Boolean.TRUE;
    }

    public void redirectToCreateNewRubros(FinaRenTipoLiquidacion tipoLiquidacion) {
        servletSession.instanciarParametros();
        servletSession.addParametro("idTipoLiquidacion", 13);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "moduloGestionTributario/mantenimientos/_asignacionrubros.xhtml");

    }

    public String onFlowProcess(FlowEvent event) {
        CatCiudadela ciudadela;
        if (event.getNewStep().equals("ubicacion")) {
            if (obra != null) {
                if (obra.getTipoObra() == null || obra.getAnio() == null || obra.getCostoTotal() == null
                        || obra.getSubsidio() == null || obra.getPlazo() == null) {
                    JsfUtil.addWarningMessage("Debe registrar todos los  campos Obligatorios", "");
                    JsfUtil.update("growl");
                    return event.getOldStep();
                }
            }
        }
        if (event.getNewStep().equals("rubrosSelection")) {
            CatUbicacion ubicacion;
            if (this.tipoDefinicion.equals("Parroquia")) {
                if (parroquia == null) {
                    JsfUtil.addWarningMessage("", "Debe Seleccionar al menos una parroquia");
                    return event.getOldStep();
                }
            }
            if (this.tipoDefinicion.equals("Sector")) {
                if (prediosDTOSeleccionados == null || prediosDTOSeleccionados.isEmpty()) {
                    JsfUtil.addWarningMessage("", "Debe Seleccionar al menos un sector");
                }
            }
            if (this.tipoDefinicion.equals("Ciudadelas")) {
                if (ciudadelaSectorSeleccionados == null || ciudadelaSectorSeleccionados.isEmpty()) {
                    JsfUtil.addWarningMessage("", "Debe Seleccionar una ciudadela al menos");
                    return event.getOldStep();
                }
            }
            if (this.tipoDefinicion.equals("Predios")) {
                if (predioClaveNumPredioSeleccionados == null || predioClaveNumPredioSeleccionados.isEmpty()) {
                    JsfUtil.addWarningMessage("", "Debe Seleccionar al menos un predio");
                    return event.getOldStep();
                }
            }
            if (!parroquiaSeleccionadas.isEmpty() || !prediosDTOSeleccionados.isEmpty() || !ciudadelaSectorSeleccionados.isEmpty() || !predioClaveNumPredioSeleccionados.isEmpty()) {
                //parroquia 
                ubicacionesSeleccionadas.clear();
                if (parroquiaSeleccionadas.size() > 0) {
                    for (CatParroquia item : parroquiaSeleccionadas) {
                        ubicacion = new CatUbicacion();
                        ubicacion.setParroquia(item);
                        ubicacionesSeleccionadas.add(ubicacion);
                    }
                }
                //sector
                if (prediosDTOSeleccionados.size() > 0) {
                    for (PredioCemDTO item : prediosDTOSeleccionados) {
                        ciudadela = new CatCiudadela();
                        ubicacion = new CatUbicacion();
                        ciudadela.setId(item.getId_ciudadela().longValue());
                        ubicacion.setCiudadela(ciudadela);
                        ubicacion.setSector(item.getSector());
                        ubicacionesSeleccionadas.add(ubicacion);
                    }
                }
                //ciudadela
                if (ciudadelaSectorSeleccionados.size() > 0) {
                    for (PredioCemCiudadelaManzanaDTO item : ciudadelaSectorSeleccionados) {
                        ciudadela = new CatCiudadela();
                        ubicacion = new CatUbicacion();
                        ciudadela.setId(item.getId_ciudadela().longValue());
                        ubicacion.setCiudadela(ciudadela);
                        ubicacion.setMz(item.getManzana());
                        ubicacionesSeleccionadas.add(ubicacion);
                    }
                }
                //predio
                if (predioClaveNumPredioSeleccionados.size() > 0) {
                    for (predioCemClaveNumPredioDTO item : predioClaveNumPredioSeleccionados) {
                        ubicacion = new CatUbicacion();
                        ubicacion.setPredio(BigInteger.valueOf(item.getId_predio()));
                        ubicacionesSeleccionadas.add(ubicacion);
                    }
                }
            }
        }
        if (event.getNewStep().equals("confirmar")) {
            if (rubroSelected == null) {
                JsfUtil.addWarningMessage("Debe Asignar Rubros al tipo de Obra Escogida", "");
                JsfUtil.update("growl");
                return event.getOldStep();
            }
        }
        return event.getNewStep();
    }

    public void llenarUbicacionParroquia() {
        if (this.parroquia != null) {
            parroquiaSeleccionadas.add(this.parroquia);
        }
    }

//    public void llenarUbicacionCiudadelas() {
//        if (this.ciudadela != null) {
//            CatUbicacion ubicacion = new CatUbicacion();
//            ubicacion.setCiudadela(this.ciudadela);
//            ubicacion.setNombre(this.ciudadela.getNombre());
//            ubicacionesSeleccionadas.add(ubicacion);
//        }
//    }
    public void cleanVarPrediosDTO() {
        this.catPredios = new ArrayList();
        this.prediosDTOSeleccionados = new ArrayList();
        this.catPredios.clear();
        this.prediosDTOSeleccionados.clear();
    }

    public void seleccionarObra(Obra obra) {
        if (obra.getId() == null) {
            this.obra = new Obra();
        } else {
            this.obra = obra;
        }
    }

    public void calcularValores() {
        if (obra != null && obra.getCostoTotal() != null && obra.getSubsidio() != null) {
            obra.setValorSubcidiado(obra.getCostoTotal().multiply(obra.getSubsidio()).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
            obra.setValorRecuperar(obra.getCostoTotal().subtract(obra.getValorSubcidiado()));
        }
        if (obra != null && obra.getPlazo() != null && obra.getValorRecuperar() != null) {
            obra.setValorEmisionAnual(obra.getValorRecuperar().divide(new BigDecimal(obra.getPlazo()), 2, BigDecimal.ROUND_HALF_UP));
        }
    }

    public void seleccionUbicacion() {
        ValoresObraUbicacion ubicacion;
//        obra.setValoresObraUbicacionsCollection(new ArrayList<ValoresObraUbicacion>());
        for (CatUbicacion u : ubicacionesSeleccionadas) {
            ubicacion = new ValoresObraUbicacion();
            u.setEstado(true);
            u.setFechaIngreso(new Date());
            u.setUsuarioIngreso(uSession.getUsuario());
            ubicacion.setUbicacion(u);
            ubicacion.setPorcentaje(new BigDecimal("100.00"));
            ubicacion.setValorRecuperar(obra.getValorRecuperar());
//            obra.getValoresObraUbicacionsCollection().add(ubicacion);
        }
    }

    public void calcularValoresUbicacion(ValoresObraUbicacion obraUbicacion) {
        if (obra.getValorEmisionAnual() == null) {
            obra.setValorEmisionAnual(BigDecimal.ONE);
        }

        obraUbicacion.setValorRecuperar(obra.getValorEmisionAnual().multiply(obraUbicacion.getPorcentaje()).divide(new BigDecimal("100")));
        manager.save(obraUbicacion);
    }

    public void guardarObra() {
        try {
            System.out.println("OBRA" + this.obra.getConcepto());
            System.out.println("tamaño de la lista ubicaciones: " + ubicacionesSeleccionadas.toString());
            if (ubicacionesSeleccionadas.size() > 0) {
                this.obra=(Obra)manager.save(obra);
                for (CatUbicacion item : ubicacionesSeleccionadas) {
                    ubicacion = new CatUbicacion();
                    ubicacion.setNombre(item.getCiudadela().getNombre());
                    ubicacion.setEstado(true);
                    ubicacion.setFechaIngreso(new Date());
                    ubicacion.setObra(this.obra);
                    ubicacion.setParroquia(item.getParroquia());
                    ubicacion.setSector(item.getSector());
                    ubicacion.setCiudadela(item.getCiudadela());
                    ubicacion.setMz(item.getMz());
                    ubicacion.setPredio(item.getPredio());
                    ubicacionesService.create(ubicacion);
                }
            }
            obra.setEstado(Boolean.TRUE);
            JsfUtil.addInformationMessage("Mensaje.", "Guardado Exitoso.");
            JsfUtil.redirect(CONFIG.URL_APP + "mejoras/obras");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activarInactivarObra(Obra o) {
        Obra tem = o;
        String mensaje = "";
        if (o.getEstado() != null) {
            if (o.getEstado()) {
                tem.setEstado(Boolean.FALSE);
                mensaje = "Inactivo";
            } else {
                tem.setEstado(Boolean.TRUE);
                mensaje = "Activo";
            }
        } else {
            tem.setEstado(Boolean.FALSE);
            mensaje = "Inactivo";
        }
        manager.update(tem);
        JsfUtil.addInformationMessage("", "La obra " + tem.getConcepto().toUpperCase() + " se " + mensaje + " con exitó");
    }

    public void generarReporteObras() {

        servletSession.borrarDatos();
        servletSession.instanciarParametros();
        servletSession.addParametro("ANIO", anioReporte);
        servletSession.setNombreReporte("obras");
        servletSession.setNombreSubCarpeta("GestionTributatia/mejoras");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

    }

    public List<CatParroquia> getParroquias() {
        Map<String, Object> paramt = new HashMap<>();
        paramt.put("idCanton", 81L);
        return manager.findAll(CatParroquia.class,
                paramt);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public String getFormaCalculo() {
        return formaCalculo;
    }

    public void setFormaCalculo(String formaCalculo) {
        this.formaCalculo = formaCalculo;
    }

    public CatCiudadela getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(CatCiudadela ciudadela) {
        this.ciudadela = ciudadela;
    }

    public List<CatCiudadela> getCatCiudadelas() {
        return catCiudadelas;
    }

    public void setCatCiudadelas(List<CatCiudadela> catCiudadelas) {
        this.catCiudadelas = catCiudadelas;
    }

    public ServletSession getServletSession() {
        return servletSession;
    }

    public void setServletSession(ServletSession servletSession) {
        this.servletSession = servletSession;
    }

    public LazyModel<Obra> getObras() {
        return obras;
    }

    public void setObras(LazyModel<Obra> obras) {
        this.obras = obras;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public List<TipoObra> getTiposObra() {
        return tiposObra;
    }

    public void setTiposObra(List<TipoObra> tiposObra) {
        this.tiposObra = tiposObra;
    }

    public List<CatUbicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<CatUbicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public Integer getAnioReporte() {
        return anioReporte;
    }

    public void setAnioReporte(Integer anioReporte) {
        this.anioReporte = anioReporte;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public String getTipoDefinicion() {
        return tipoDefinicion;
    }

    public void setTipoDefinicion(String tipoDefinicion) {
        this.tipoDefinicion = tipoDefinicion;
    }

    public List<PredioCemDTO> getSectoresPredios() {
        return sectoresPredios;
    }

    public void setSectoresPredios(List<PredioCemDTO> sectoresPredios) {
        this.sectoresPredios = sectoresPredios;
    }

    public List<PredioCemDTO> getPrediosDTOSeleccionados() {
        return prediosDTOSeleccionados;
    }

    public void setPrediosDTOSeleccionados(List<PredioCemDTO> prediosDTOSeleccionados) {
        this.prediosDTOSeleccionados = prediosDTOSeleccionados;
    }

    public LazyModel<CatPredio> getPredios() {
        return predios;
    }

    public void setPredios(LazyModel<CatPredio> predios) {
        this.predios = predios;
    }

    public List<CatPredio> getCatPredios() {
        return catPredios;
    }

    public void setCatPredios(List<CatPredio> catPredios) {
        this.catPredios = catPredios;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosList() {
        return rubrosList;
    }

    public void setRubrosList(List<FinaRenRubrosLiquidacion> rubrosList) {
        this.rubrosList = rubrosList;
    }

    public FinaRenRubrosLiquidacion getRubroSelected() {
        return rubroSelected;
    }

    public void setRubroSelected(FinaRenRubrosLiquidacion rubroSelected) {
        this.rubroSelected = rubroSelected;
    }

    public List<ContCuentas> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<ContCuentas> cuentas) {
        this.cuentas = cuentas;
    }

    public List<CatUbicacion> getUbicacionesSeleccionadas() {
        return ubicacionesSeleccionadas;
    }

    public void setUbicacionesSeleccionadas(List<CatUbicacion> ubicacionesSeleccionadas) {
        this.ubicacionesSeleccionadas = ubicacionesSeleccionadas;
    }

    public List<CatCiudadela> getCatCiudadelasSeleccionadas() {
        return catCiudadelasSeleccionadas;
    }

    public void setCatCiudadelasSeleccionadas(List<CatCiudadela> catCiudadelasSeleccionadas) {
        this.catCiudadelasSeleccionadas = catCiudadelasSeleccionadas;
    }

    public Integer getValorCalculo1() {
        return valorCalculo1;
    }

    public void setValorCalculo1(Integer valorCalculo1) {
        this.valorCalculo1 = valorCalculo1;
    }

    public Integer getValorCalculo2() {
        return valorCalculo2;
    }

    public void setValorCalculo2(Integer valorCalculo2) {
        this.valorCalculo2 = valorCalculo2;
    }

    public List<PredioCemCiudadelaManzanaDTO> getCiudadelaSector() {
        return ciudadelaSector;
    }

    public void setCiudadelaSector(List<PredioCemCiudadelaManzanaDTO> ciudadelaSector) {
        this.ciudadelaSector = ciudadelaSector;
    }

    public List<PredioCemCiudadelaManzanaDTO> getCiudadelaSectorSeleccionados() {
        return ciudadelaSectorSeleccionados;
    }

    public void setCiudadelaSectorSeleccionados(List<PredioCemCiudadelaManzanaDTO> ciudadelaSectorSeleccionados) {
        this.ciudadelaSectorSeleccionados = ciudadelaSectorSeleccionados;
    }

    public List<predioCemClaveNumPredioDTO> getPredioClaveNumPredio() {
        return predioClaveNumPredio;
    }

    public void setPredioClaveNumPredio(List<predioCemClaveNumPredioDTO> predioClaveNumPredio) {
        this.predioClaveNumPredio = predioClaveNumPredio;
    }

    public List<predioCemClaveNumPredioDTO> getPredioClaveNumPredioSeleccionados() {
        return predioClaveNumPredioSeleccionados;
    }

    public void setPredioClaveNumPredioSeleccionados(List<predioCemClaveNumPredioDTO> predioClaveNumPredioSeleccionados) {
        this.predioClaveNumPredioSeleccionados = predioClaveNumPredioSeleccionados;
    }

    public List<CatParroquia> getParroquiaSeleccionadas() {
        return parroquiaSeleccionadas;
    }

    public void setParroquiaSeleccionadas(List<CatParroquia> parroquiaSeleccionadas) {
        this.parroquiaSeleccionadas = parroquiaSeleccionadas;
    }
//</editor-fold>

}
