/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.AppDepartamentoDetalleTitulos;
import com.asgard.Entity.FinaRenDetLiquidacion;
import com.gestionTributaria.Entities.EspCementerio;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.Entities.EspCementerioBoveda;
import com.gestionTributaria.Entities.EspCementerioBovedaEnte;
import com.gestionTributaria.Entities.EspFotoBovedas;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author DEVELOPER
 */
@Named(value = "espaciosPublicos")
@ViewScoped
public class EspaciosPublicos implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Inject
    private ManagerService manager;

    @Inject
    private CatalogoService catalogoService;
    @Inject
    private UserSession session;
    @Inject
    protected ServletSession ss;

    private LazyModel<EspCementerio> cementerioLazy;
    private LazyModel<EspCementerioBoveda> bovedasLazy;
    private EspCementerio cementerio, cementerioNuevo;
    private EspCementerioBoveda boveda, bovedaNueva;

    private List<EspCementerioBovedaEnte> bovedaEntesListPropietarios, bovedaEntesListSepultados, bovedaEntesListPropietarioAnterior;

    private Boolean esNuevaBoveda;

    private List<Cliente> propietario, sepultados;
    private LazyModel<Cliente> contribuyentes;

    private List<EspFotoBovedas> fotosBovedas;

    private EspFotoBovedas espFotoBovedaSeleccionada;
    private Integer index;
    private List<CatalogoItem> tipoConstruccionBoveda;
    protected Documentos documento;

    protected List<Documentos> listaDocumentos;

    private List<FinaRenLiquidacion> liquidacionesBoveda;

    protected Long tReporte;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private Date desdeA;
    private Date hastaA;
    private Map<String, Object> param;

    @PostConstruct
    public void init() {
        listaDocumentos = new ArrayList<>();
        bovedaEntesListPropietarioAnterior = new ArrayList();
        cementerioLazy = new LazyModel<>(EspCementerio.class);
        cementerio = new EspCementerio();
        cementerioNuevo = new EspCementerio();
        esNuevaBoveda = Boolean.FALSE;
        bovedaNueva = new EspCementerioBoveda();
        bovedaEntesListPropietarios = new ArrayList();
        bovedaEntesListSepultados = new ArrayList();
        fotosBovedas = new ArrayList();
        desdeA = new Date();
        hastaA = new Date();
        tipoConstruccionBoveda = catalogoService.getItemsByCatalogo("GT_bodeva-tipo_construccion");
    }

    public void calAreaBoveda() {
        if (bovedaNueva.getAlto() != null && bovedaNueva.getAncho() != null) {
            bovedaNueva.setArea(bovedaNueva.getAlto().multiply(bovedaNueva.getAncho()));
        } else {
            bovedaNueva.setArea(BigDecimal.ZERO);
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("bovedas")) {
            if (cementerio.getId() != null) {
                bovedasLazy = new LazyModel(EspCementerioBoveda.class);
                bovedasLazy.getFilterss().put("cementerio", cementerio);
            } else {
                JsfUtil.addErrorMessage("Seleccione un Cementerio para Continuar", null);
                return event.getOldStep();
            }
        }

        if (event.getNewStep().equals("bovedas_informacion")) {
            if (boveda != null && boveda.getId() != null) {
                loadFotosBovedas();
                for (EspCementerioBovedaEnte bovedaEnte : boveda.getEspCementerioBovedaEnteList()) {
                    ///PROPIETARIOS
                    if ("propietario".equals(bovedaEnte.getTipo().getCodigo()) && bovedaEnte.getEstado()) {
                        this.bovedaEntesListPropietarios.add(bovedaEnte);
                    }
                    ///sepultados
                    if ("sepultados".equals(bovedaEnte.getTipo().getCodigo()) && bovedaEnte.getEstado()) {
                        this.bovedaEntesListSepultados.add(bovedaEnte);
                    }
                    //ANTERIORES
                    if ("anteriores".equals(bovedaEnte.getTipo().getCodigo())) {
                        this.bovedaEntesListPropietarioAnterior.add(bovedaEnte);
                    }
                    liquidacionesBoveda = new ArrayList();
                    if (boveda.getDepartamentoDetalleTitulosList() != null && !boveda.getDepartamentoDetalleTitulosList().isEmpty()) {
                        for (AppDepartamentoDetalleTitulos tit : boveda.getDepartamentoDetalleTitulosList()) {
                            if (tit.getFinaRenLiquidacionList() != null && !tit.getFinaRenLiquidacionList().isEmpty()) {
                                for (FinaRenLiquidacion rl : tit.getFinaRenLiquidacionList()) {
                                    if (rl.getEstadoLiquidacion().getId().equals(2L) || rl.getEstadoLiquidacion().getId().equals(1L)) {
                                        liquidacionesBoveda.add(rl);
                                    }
                                }
                            }
                        }
                    }
                }
                cargarDocumentos();
            } else {
                JsfUtil.addErrorMessage("Seleccione una Boveda para Continuar", null);
                return event.getOldStep();
            }
        }

        return event.getNewStep();
    }

    public void loadFotosBovedas() {
        if (boveda != null && boveda.getId() != null) {
            param = new HashMap<>();
            param.put("cementerioBoveda", boveda);
            param.put("sisEnabled", true);
            this.fotosBovedas = manager.findAllQuery("select e from EspFotoBovedas e where e.cementerioBoveda = :cementerioBoveda and e.sisEnabled=:sisEnabled", param);
        }
    }

    public void imprimirFichaCementerio() {
        ss.borrarDatos();
        ss.instanciarParametros();
        //  ss.setTieneDatasource(Boolean.TRUE);
        ss.setNombreSubCarpeta("EspaciosPublicos/cementerio/");
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        param = new HashMap<>();
        param.put("cementerioBoveda", boveda.getId());
        this.fotosBovedas = manager.findAllQuery("select e from EspFotoBovedas e where e.cementerioBoveda = :cementerioBoveda", param);
        if (Utils.isNotEmpty(fotosBovedas)) {
            //   ss.addParametro("FOTO", fserv.streamFile(fotosBovedas.get(0).getFileOid()));
        }
        ss.addParametro("Fondo", path + SisVars.sisMarcaAgua);
        ss.addParametro("LOGO", path + SisVars.sisLogo);
        ss.addParametro("ID", boveda.getId());
        ss.setNombreReporte("sFichaCementerio");
        //JsfUtil.redirectNewTab("/sgmEE/Documento");
    }

    public void eliminarBoveda() {
        boveda.setEstado(Boolean.FALSE);
        manager.update(boveda);
    }

    public void nuevaBoveda(EspCementerioBoveda boveda) {

        if (boveda == null) {
            bovedaNueva = new EspCementerioBoveda();
            bovedaNueva.setCementerio(cementerio);
            this.esNuevaBoveda = Boolean.TRUE;

        } else {
            bovedaNueva = boveda;
        }

        JsfUtil.redirectNewTab(CONFIG.URL_APP + "cementerio/nuevas/boveda");

    }

    public void saveCementerio() {
        try {

            if (cementerioNuevo.getId() == null) {
                cementerioNuevo.setEstado(Boolean.TRUE);
                cementerioNuevo.setFechaCreacion(new Date());
                cementerioNuevo.setUsuario(session.getNameUser());

            }
            if (cementerioNuevo.getId() != null) {
                manager.update(cementerioNuevo);
            } else {
                cementerioNuevo = (EspCementerio) manager.updateEntity(cementerioNuevo);
            }
            if (cementerioNuevo.getId() != null) {
                JsfUtil.addInformationMessage("Info", "Datos Grabados Correctamente: " + cementerioNuevo.getNombreCementerio());
            } else {
                JsfUtil.addErrorMessage("Info", "Hubo un error al registrar el cementerio. Inténtelo nuevamente");
            }
            cementerioNuevo = new EspCementerio();
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
            JsfUtil.addErrorMessage("Info", "Hubo un error al registrar el cementerio. Inténtelo nuevamente");
        }

    }

    public void nuevoCliente() {
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "cliente-adm");
    }

    public void seleccionarPropietarios() {
        EspCementerioBovedaEnte bovedaEnte = null;
        for (Cliente catEnte : propietario) {

            bovedaEnte = new EspCementerioBovedaEnte();
            bovedaEnte.setEnte(catEnte);
            bovedaEnte.setEstado(Boolean.TRUE);
            bovedaEnte.setFechaCreacion(new Date());
            //PROPIETARIOS
            bovedaEnte.setTipo(catalogoService.getItemByCatalogoAndCodigo("GT_TIPO_CEMENTERIO_ESTADOS", "propietario"));
            bovedaEnte.setUsuario(session.getNameUser());
            if (boveda != null && boveda.getId() != null) {
                bovedaEnte.setCementerioBoveda(boveda);
                manager.save(bovedaEnte);
            }
            bovedaEntesListPropietarios.add(bovedaEnte);
        }
    }

    public void removePropietario(int index) {
        EspCementerioBovedaEnte bovedaEnte = bovedaEntesListPropietarios.get(index);
        if (bovedaEnte.getId() != null) {
            bovedaEnte.setTipo(catalogoService.getItemByCatalogoAndCodigo("GT_TIPO_CEMENTERIO_ESTADOS", "anteriores"));
            bovedaEnte.setEstado(Boolean.FALSE);
            manager.update(bovedaEnte);
            bovedaEntesListPropietarioAnterior.add(bovedaEnte);
        }
        bovedaEntesListPropietarios.remove(index);
    }

    public void handleFileDocumentBySave(FileUploadEvent event) {
//        try {
//            //Long documentoId = fserv.uploadFile(FilesUtil.copyFileServer1(event), event.getFile().getFileName(), event.getFile().getContentType());
//            documentoBean.setFechaCreacion(new Date());
//            documentoBean.setNombre(event.getFile().getFileName());
//            //documentoBean.setRaiz(predio.getId());
//            documentoBean.setContentType(event.getFile().getContentType());
//            documentoBean.setDocumentoId(documentoId);
//            documentoBean.setIdentificacion("Datos Bovedas");
//            documentoBean.setBoveda(boveda.getId());
//            this.setDocumento(documentoBean.saveDocumentoBoveda());
//            cargarDocumentos();
//            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
//        } catch (IOException e) {
//            Logger.getLogger(FichaPredial.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    public void removeSepultados(int index) {
        EspCementerioBovedaEnte bovedaEnte = bovedaEntesListSepultados.get(index);
        if (bovedaEnte.getId() != null) {
            bovedaEnte.setEstado(Boolean.FALSE);
            manager.update(bovedaEnte);
        }
        bovedaEntesListSepultados.remove(index);
    }

    public void loadContribyentes() {
        contribuyentes = new LazyModel(Cliente.class);
        contribuyentes.getFilterss().put("estado", true);
        contribuyentes.getFilterss().put("validado", true);
    }

    public void loadValuesProp() {
        propietario = new ArrayList();
        loadContribyentes();
    }

    public void loadValuesSepultados() {
        sepultados = new ArrayList();
        loadContribyentes();
    }

    public String descripcionRubro(List<FinaRenDetLiquidacion> liquidacions) {
        String rubro = " ";
        if (liquidacions != null && !liquidacions.isEmpty()) {

            for (FinaRenDetLiquidacion detLiquidacion : liquidacions) {
                rubro = rubro + (String) manager.find("SELECT r.descripcion FROM FinaRenRubrosLiquidacion r WHERE r.id=:idRubro", new String[]{"idRubro"}, new Object[]{detLiquidacion.getRubro().getId()});
            }

        }
        return rubro;
    }

    public void seleccionarSepultados() {

        EspCementerioBovedaEnte bovedaEnte = null;
        for (Cliente catEnte : sepultados) {
            bovedaEnte = new EspCementerioBovedaEnte();
            bovedaEnte.setEnte(catEnte);
            bovedaEnte.setEstado(Boolean.TRUE);
            bovedaEnte.setFechaCreacion(new Date());
            bovedaEnte.setTipo(catalogoService.getItemByCatalogoAndCodigo("GT_TIPO_CEMENTERIO_ESTADOS", "sepultados"));
            //sepultados
            // bovedaEnte.setTipo(new CtlgItem(764L));
            bovedaEnte.setUsuario(session.getNameUser());
            if (boveda != null && boveda.getId() != null) {
                bovedaEnte.setCementerioBoveda(boveda);
                manager.save(bovedaEnte);
            }
            bovedaEntesListSepultados.add(bovedaEnte);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {

        try {
            String ruta = SisVars.RUTA_GESTION_TRIBUTARIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();

            System.out.println("ruta " + ruta);
            espFotoBovedaSeleccionada = new EspFotoBovedas();
            espFotoBovedaSeleccionada.setNombre(event.getFile().getFileName());
            espFotoBovedaSeleccionada.setContentType(event.getFile().getContentType());
            espFotoBovedaSeleccionada.setRuta(ruta);
            espFotoBovedaSeleccionada.setSisEnabled(Boolean.TRUE);
            espFotoBovedaSeleccionada.setUsuario(session.getNameUser());
            espFotoBovedaSeleccionada.setInputStream(event.getFile().getInputstream());
            Utils.copyFileServerOneBoveda(espFotoBovedaSeleccionada);
            fotosBovedas.add(espFotoBovedaSeleccionada);
            espFotoBovedaSeleccionada = new EspFotoBovedas();
            JsfUtil.addInformationMessage("Nota1", "Foto guardada satisfactoriamente");

        } catch (Exception e) {
            Logger.getLogger(FotosLocalComercial.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void searchBovedas(Long idFoto) {
        EspFotoBovedas espFotoBovedas = manager.find(EspFotoBovedas.class, idFoto);
        this.fotosBovedas.add(espFotoBovedas);
    }

    public void cargarFotos() {

    }

    public void cargarDocumentos() {
        if (boveda == null && boveda.getId() == null) {
            return;
        }
        // geDocumentosList = manager.findAll(Querys.getGeDocumentosIdBoveda, new String[]{"boveda"}, new Object[]{boveda.getId()});
    }

    public void descargarDocumento(Long fileOid) {
        try {
            String url = fileOid.toString();
            if (url != null && url.trim().length() > 0) {
                JsfUtil.redirectNewTab(SisVars.urlbase + "DescargarDocsRepositorio?idDoc=" + url + "&type=pdf");
            } else {
                JsfUtil.addWarningMessage("No Existen Documentos para el Predio Seleccionado", "");
            }
        } catch (Exception e) {
            Logger.getLogger(EspaciosPublicos.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void inactivarDocumento(EspCementerio documentos) {
//        try {
//            documentos = catas.saveInactivarDocumentos(documentos);
//            if (!documentos.getEstado()) {
//                JsfUtil.addInformationMessage("Exito!", "Datos Registrados Correctamente");
//            } else {
//                JsfUtil.addErrorMessage("", "Error al Eliminar los Archivos");
//            }
//            cargarDocumentos();
//        } catch (Exception e) {
//            Logger.getLogger(EspaciosPublicos.class.getName()).log(Level.SEVERE, null, e);
//        }
    }

    public void guardarEditarBoveda() {
        bovedaNueva.setFechaCreacion(new Date());
        bovedaNueva.setUsuario(session.getNameUser());
        if (bovedaNueva.getId() == null) {
            manager.persist(bovedaNueva);
        } else {
            manager.update(bovedaNueva);
        }
        JsfUtil.addInformationMessage("", "Registro con exitó");
    }

    public void saveBoveda() {
        try {
            if (cementerio == null || cementerio.getId() == null) {
                JsfUtil.addErrorMessage("Debe seleccionar un Cementeriio Asociado", null);
                return;
            }
            if (bovedaEntesListPropietarios == null || bovedaEntesListPropietarios.isEmpty()) {
                JsfUtil.addErrorMessage("Debe seleccionar Propietaro(s)", null);
                return;
            }
            bovedaNueva.setManzana(bovedaNueva.getManzana().toUpperCase());
            bovedaNueva.setEstado(Boolean.TRUE);
            bovedaNueva.setUsuario(session.getNameUser());
            bovedaNueva.setCementerio(cementerio);
            bovedaNueva.setPropietario(bovedaEntesListPropietarios.get(0).getEnte());
            if (existeBoveda(bovedaNueva)) {
                JsfUtil.addErrorMessage("Boveda " + bovedaNueva.getLote() + "Registrada Anteriormente", null);
                return;
            }
            bovedaNueva.setFechaCreacion(new Date());
//            boveda.setFotosBovedas(fotosBovedas);
//            boveda.setEspCementerioBovedaEnteList(bovedaEntesListPropietarios);

            System.out.println("grabando");
            EspCementerioBoveda cementerioBoveda = (EspCementerioBoveda) manager.save(bovedaNueva);
            System.out.println("grabado");
            if (cementerioBoveda != null && cementerioBoveda.getId() != null) {

                for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListPropietarios) {
                    bovedaEnte.setCementerioBoveda(cementerioBoveda);
                    manager.persist(bovedaEnte);
                }
                for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListSepultados) {
                    bovedaEnte.setCementerioBoveda(cementerioBoveda);
                    manager.persist(bovedaEnte);
                }

                for (EspFotoBovedas it : fotosBovedas) {
                    it.setCementerioBoveda(cementerioBoveda);
                    manager.persist(it);
                }

                JsfUtil.addInformationMessage("Datos Grabados Correctamente", null);
                cleanValues();
                JsfUtil.redirect(CONFIG.URL_APP + "cementerio/nuevas/boveda");
            } else {
                JsfUtil.addErrorMessage("Ocurrio Un Error Verifique los datos o recargue la pagina", null);
            }
        } catch (Exception e) {
        }
    }

    public void cleanValues() {
        fotosBovedas = new ArrayList();
        bovedaEntesListSepultados = new ArrayList();
        bovedaEntesListPropietarios = new ArrayList();
        bovedaNueva = new EspCementerioBoveda();
        cementerio = new EspCementerio();

    }

    public void calculateArea() {
        if (bovedaNueva.getAlto() != null && bovedaNueva.getAncho() != null) {
            bovedaNueva.setArea(bovedaNueva.getAlto().multiply(bovedaNueva.getAncho()).setScale(2, RoundingMode.HALF_UP));
        }
    }

    public void updateBoveda() {
        EspCementerioBoveda c = (EspCementerioBoveda) manager.find(EspCementerioBoveda.class, boveda.getId());
        boveda.setCementerio(cementerio);
        boveda.setFotosBovedas(fotosBovedas);

        if (c.getManzana() == null) {
            if (existeBoveda(boveda)) {
                JsfUtil.addErrorMessage("Boveda " + bovedaNueva.getLote() + "Registrada Anteriormente", null);
                return;
            } else {

                manager.update(boveda);

                for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListPropietarios) {
                    bovedaEnte.setCementerioBoveda(boveda);
                    if (bovedaEnte.getId() == null) {
                        System.out.println("grabando 1 ");
                        manager.persist(bovedaEnte);
                    } else {
                        System.out.println("editando 1 ");
                        manager.update(bovedaEnte);
                    }

                }
                for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListSepultados) {
                    bovedaEnte.setCementerioBoveda(boveda);

                    if (bovedaEnte.getId() == null) {
                        System.out.println("grabando 2 ");
                        manager.persist(bovedaEnte);
                    } else {
                        System.out.println("editando 3 ");
                        manager.update(bovedaEnte);
                    }
                }

                for (EspFotoBovedas it : fotosBovedas) {
                    it.setCementerioBoveda(boveda);

                    if (it.getId() == null) {
                        System.out.println("grabando 3 ");
                        manager.persist(it);
                    } else {
                        System.out.println("editando 3 ");
                        manager.update(it);
                    }

                }

                JsfUtil.addInformationMessage("Datos Actualizados Correctamente", null);
            }
        } else {
            manager.update(boveda);
            for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListPropietarios) {
                bovedaEnte.setCementerioBoveda(boveda);
                if (bovedaEnte.getId() == null) {
                    System.out.println("grabando 1 ");
                    manager.persist(bovedaEnte);
                } else {
                    System.out.println("editando 1 ");
                    manager.update(bovedaEnte);
                }

            }
            for (EspCementerioBovedaEnte bovedaEnte : bovedaEntesListSepultados) {
                bovedaEnte.setCementerioBoveda(boveda);

                if (bovedaEnte.getId() == null) {
                    System.out.println("grabando 2 ");
                    manager.persist(bovedaEnte);
                } else {
                    System.out.println("editando 3 ");
                    manager.update(bovedaEnte);
                }
            }

            for (EspFotoBovedas it : fotosBovedas) {
                it.setCementerioBoveda(boveda);

                if (it.getId() == null) {
                    System.out.println("grabando 3 ");
                    manager.persist(it);
                } else {
                    System.out.println("editando 3 ");
                    manager.update(it);
                }

            }

            JsfUtil.addInformationMessage("Datos Actualizados Correctamente", null);
        }
        ss.getBorrarDatos();
        ss.instanciarParametros();;
        JsfUtil.redirect(CONFIG.URL_APP + "cementerio/mantenimieto");
    }

    public Boolean existeBoveda(EspCementerioBoveda b) {
        if (!b.getManzana().equals("")) {
            if (b.getLote() != null) {
                EspCementerioBoveda c = (EspCementerioBoveda) manager.find(
                        "select e from EspCementerioBoveda e where e.manzana = :manzana and e.loteAlfanumerico = :lote and e.cementerio.id = :cementerio AND e.estado = true",
                        new String[]{"manzana", "lote", "cementerio"},
                        new Object[]{b.getManzana(), b.getLoteAlfanumerico(), b.getCementerio().getId()});
                if (c == null) {
                    return Boolean.FALSE;
                } else {
                    return Boolean.TRUE;
                }
            } else {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }

    public void loadValueFotoBoveda(EspFotoBovedas espFotoBovedas, Integer index) {
        this.index = index;
        this.espFotoBovedaSeleccionada = new EspFotoBovedas();
        this.espFotoBovedaSeleccionada = espFotoBovedas;
//        JsfUtil.executeJS("PF('dlgFoto').show()");
//        JsfUtil.update("frmFotos");

    }

    public void eliminarFoto() {
//        if (catas.quitarFotoBoveda(espFotoBovedaSeleccionada)) {
//            fotosBovedas.remove(index);
//            JsfUtil.update("registroCementerioForm");
//            JsfUtil.addInformationMessage("Nota!", "Foto eliminada satisfactoriamente.");
//        } else {
//            JsfUtil.addWarningMessage("Advertencia!", "No se pudo eliminar la foto seleccionada");
//        }
    }

    public void eliminarFoto(int index) {
        EspFotoBovedas tem = fotosBovedas.get(index);
        if (tem.getId() != null) {
            tem.setSisEnabled(Boolean.FALSE);
            manager.update(tem);
        }

        fotosBovedas.remove(index);
        System.out.println("fotosBovedas " + fotosBovedas.size());
    }

    /*REPORTE*/
    public void generarReporteCementerio(Boolean esPDFCementerio) {

        try {
            //  String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarDatos();

            ss.addParametro("desde", desdeA);
            ss.addParametro("hasta", hastaA);

            if (!esPDFCementerio) {
                ss.setContentType("xlsx");
            } else {
                ss.setContentType("pdf");
            }

            ss.setNombreReporte("SReporteCementerio");
            ss.setNombreSubCarpeta("GestionTributatia/cementerio");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");

        } catch (Exception e) {
            // LOG.log(Level.OFF, null, e);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    public ManagerService getManager() {
        return manager;
    }

    public void setManager(ManagerService manager) {
        this.manager = manager;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public LazyModel<EspCementerio> getCementerioLazy() {
        return cementerioLazy;
    }

    public void setCementerioLazy(LazyModel<EspCementerio> cementerioLazy) {
        this.cementerioLazy = cementerioLazy;
    }

    public LazyModel<EspCementerioBoveda> getBovedasLazy() {
        return bovedasLazy;
    }

    public void setBovedasLazy(LazyModel<EspCementerioBoveda> bovedasLazy) {
        this.bovedasLazy = bovedasLazy;
    }

    public EspCementerio getCementerio() {
        return cementerio;
    }

    public void setCementerio(EspCementerio cementerio) {
        this.cementerio = cementerio;
    }

    public EspCementerio getCementerioNuevo() {
        return cementerioNuevo;
    }

    public void setCementerioNuevo(EspCementerio cementerioNuevo) {
        this.cementerioNuevo = cementerioNuevo;
    }

    public EspCementerioBoveda getBoveda() {
        return boveda;
    }

    public void setBoveda(EspCementerioBoveda boveda) {
        this.boveda = boveda;
    }

    public EspCementerioBoveda getBovedaNueva() {
        return bovedaNueva;
    }

    public void setBovedaNueva(EspCementerioBoveda bovedaNueva) {
        this.bovedaNueva = bovedaNueva;
    }

    public List<EspCementerioBovedaEnte> getBovedaEntesListPropietarios() {
        return bovedaEntesListPropietarios;
    }

    public void setBovedaEntesListPropietarios(List<EspCementerioBovedaEnte> bovedaEntesListPropietarios) {
        this.bovedaEntesListPropietarios = bovedaEntesListPropietarios;
    }

    public List<EspCementerioBovedaEnte> getBovedaEntesListSepultados() {
        return bovedaEntesListSepultados;
    }

    public void setBovedaEntesListSepultados(List<EspCementerioBovedaEnte> bovedaEntesListSepultados) {
        this.bovedaEntesListSepultados = bovedaEntesListSepultados;
    }

    public List<EspCementerioBovedaEnte> getBovedaEntesListPropietarioAnterior() {
        return bovedaEntesListPropietarioAnterior;
    }

    public void setBovedaEntesListPropietarioAnterior(List<EspCementerioBovedaEnte> bovedaEntesListPropietarioAnterior) {
        this.bovedaEntesListPropietarioAnterior = bovedaEntesListPropietarioAnterior;
    }

    public Boolean getEsNuevaBoveda() {
        return esNuevaBoveda;
    }

    public void setEsNuevaBoveda(Boolean esNuevaBoveda) {
        this.esNuevaBoveda = esNuevaBoveda;
    }

    public List<Cliente> getPropietario() {
        return propietario;
    }

    public void setPropietario(List<Cliente> propietario) {
        this.propietario = propietario;
    }

    public List<Cliente> getSepultados() {
        return sepultados;
    }

    public void setSepultados(List<Cliente> sepultados) {
        this.sepultados = sepultados;
    }

    public LazyModel<Cliente> getContribuyentes() {
        return contribuyentes;
    }

    public void setContribuyentes(LazyModel<Cliente> contribuyentes) {
        this.contribuyentes = contribuyentes;
    }

    public List<EspFotoBovedas> getFotosBovedas() {
        return fotosBovedas;
    }

    public void setFotosBovedas(List<EspFotoBovedas> fotosBovedas) {
        this.fotosBovedas = fotosBovedas;
    }

    public EspFotoBovedas getEspFotoBovedaSeleccionada() {
        return espFotoBovedaSeleccionada;
    }

    public void setEspFotoBovedaSeleccionada(EspFotoBovedas espFotoBovedaSeleccionada) {
        this.espFotoBovedaSeleccionada = espFotoBovedaSeleccionada;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public List<FinaRenLiquidacion> getLiquidacionesBoveda() {
        return liquidacionesBoveda;
    }

    public void setLiquidacionesBoveda(List<FinaRenLiquidacion> liquidacionesBoveda) {
        this.liquidacionesBoveda = liquidacionesBoveda;
    }

    public Long gettReporte() {
        return tReporte;
    }

    public void settReporte(Long tReporte) {
        this.tReporte = tReporte;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Date getDesdeA() {
        return desdeA;
    }

    public void setDesdeA(Date desdeA) {
        this.desdeA = desdeA;
    }

    public Date getHastaA() {
        return hastaA;
    }

    public void setHastaA(Date hastaA) {
        this.hastaA = hastaA;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
//</editor-fold>

    public CatalogoService getCatalogoService() {
        return catalogoService;
    }

    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public List<CatalogoItem> getTipoConstruccionBoveda() {
        return tipoConstruccionBoveda;
    }

    public void setTipoConstruccionBoveda(List<CatalogoItem> tipoConstruccionBoveda) {
        this.tipoConstruccionBoveda = tipoConstruccionBoveda;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

}
