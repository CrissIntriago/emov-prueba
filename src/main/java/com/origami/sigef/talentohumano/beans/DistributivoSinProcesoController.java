/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cargo;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.RegimenLaboral;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.PartidaDistributivoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.talentohumano.services.CargoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.RegimenLaboralService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "distributivoSinProcesoView")
@ViewScoped
public class DistributivoSinProcesoController implements Serializable {

    private LazyModel<Distributivo> distributivoLazy;
    private Distributivo distributivo;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private CargoService cargoService;
    private List<Cargo> listCargo;
    @Inject
    private UnidadAdministrativaService unidadService;
    private List<UnidadAdministrativa> listUnidad;
    private boolean editar = false;
    private boolean editar1 = false;
    private String observaciones;
//    private boolean mostrar = false;
    //listas de regimen
    private List<RegimenLaboral> listRegimen;
    private List<RegimenLaboral> regimen;
    @Inject
    private RegimenLaboralService regimenService;
    //para traer los rubros
    private ValoresDistributivo valores;
    @Inject
    private ValoresDistributivoService valoresDistributivoService;
    private List<ValoresDistributivo> valoresDistributivoList;
    private ParametrosTalentoHumano parametrosTalento;
    //listas auxiliares
    private List<ValoresDistributivo> valorList;
    private List<ValoresDistributivo> listaSeleccionada;
    private String tipoReporte;

    //para guardar distributivo_Escala
    @Inject
    private DistributivoEscalaService distributivoEscalaService;
    private DistributivoEscala distributivoEscala;
    private List<DistributivoEscala> distributivoEscalaList;//paraq borrar todo

    @Inject
    private UserSession userSession;

    //catalogo master
    private OpcionBusqueda busqueda;
    private List<MasterCatalogo> periodos;
    @Inject
    private MasterCatalogoService masterService;
    //    Si el año esta aprobado estas desactivan todo
    private boolean bloqueo;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;

    @Inject
    private ServletSession ss;
    private long anio;

    @Inject
    private PartidaDistributivoService partidaService;

    @PostConstruct
    public void inicializate() {

        distributivo = new Distributivo();
        valores = new ValoresDistributivo();
        listCargo = cargoService.findByNamedQuery("Cargo.findByEstado");
        listUnidad = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");
        listRegimen = regimenService.findByNamedQuery("RegimenLaboral.findByFiltro");
        valoresDistributivoList = new ArrayList<>();
        distributivoLazy = new LazyModel<>(Distributivo.class);
        distributivoLazy.setDistinct(false);
        distributivoLazy.getFilterss().put("estado", true);
        distributivoLazy.getSorteds().put("servidorPublico.persona.apellido", "ASC");
        parametrosTalento = new ParametrosTalentoHumano();
        valorList = new ArrayList<>();
        listaSeleccionada = new ArrayList<>();
        busqueda = new OpcionBusqueda();
        periodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
        distributivoEscala = new DistributivoEscala();

    }

    public void form(Distributivo distributiv) {
        busqueda = new OpcionBusqueda();
        distributivoEscala = new DistributivoEscala();
        if (distributiv != null) {
            distributivo = distributiv;
            editar = Boolean.TRUE;
            //poniendo para guardar
            distributivoEscala = distributivoEscalaService.getEscalaDistributivoAnio(distributivo, busqueda);
            regimen = regimenService.findByNamedQuery("RegimenLaboral.findByPadre", distributivo.getRegimen());
            if ("NP".equals(distributivo.getTipoContrato().getCodigo())) {
                regimen = regimenService.findByNamedQuery("RegimenLaboral.findByCodigoNombramiento");
                editar1 = Boolean.FALSE;
            } else {
                editar1 = Boolean.TRUE;
            }
            valoresDistributivoList = new ArrayList<>();
            valorList = new ArrayList<>();
            valorList = valoresDistributivoService.findvaloresDistributivo(distributiv, busqueda);
            if (valoresDistributivoList.isEmpty() && valorList != null) {
                for (ValoresDistributivo vd : valorList) {
                    valoresDistributivoList.add(vd);
                }
            }
        } else {
            editar1 = Boolean.FALSE;
            editar = Boolean.FALSE;
            distributivo = new Distributivo();
            valores = new ValoresDistributivo();
            valoresDistributivoList = new ArrayList<>();
        }
        if (distributivoEscala != null) {
            if (distributivoEscala.getEscalaSalarial() == null) {
                distributivoEscala.setEscalaSalarial(new EscalaSalarial());
            }
        }
        habilitarEscala(distributivo, busqueda.getAnio());
        distributivo.setUsuarioModifica(userSession.getNameUser());
        distributivo.setFechaModificacion(new Date());
        PrimeFaces.current().executeScript("PF('distributivoDialogo').show()");
        PrimeFaces.current().ajax().update(":panelDistributivo");
    }

    public void filtroActivosInactivos(int var) {
        distributivoLazy = new LazyModel<>(Distributivo.class);
        distributivoLazy.getFilterss().put("estado", true);
        switch (var) {
            case 1://Activos sin Vacantes
                distributivoLazy.getFilterss().put("estadoActivo", true);
                distributivoLazy.getFilterss().put("servidorPublico:notEqual", null);
                distributivoLazy.getFilterss().put("estado", true);
                break;
            case 2://Vacantes solo los activos
                distributivoLazy.getFilterss().put("servidorPublico:equal", null);
                distributivoLazy.getFilterss().put("estadoActivo", true);
                distributivoLazy.getFilterss().put("estado", true);
                break;
            case 3://Inactivos
                distributivoLazy.getFilterss().put("estadoActivo", false);
                distributivoLazy.getFilterss().put("estado", true);
                //distributivoLazy.getFilterss().put("estado", false);
                break;
        }
        JsfUtil.update("formMain:dtDistributivo");
    }

    public void buscarP() {
        valoresDistributivoList = new ArrayList<>();
        valorList = valoresDistributivoService.findvaloresDistributivo(distributivo, busqueda);
        if (valoresDistributivoList.isEmpty() && valorList != null) {
            for (ValoresDistributivo vd : valorList) {
                valoresDistributivoList.add(vd);
            }
        }
        //volver a buscar escala
        distributivoEscala = new DistributivoEscala();
        distributivoEscala = distributivoEscalaService.getEscalaDistributivoAnio(distributivo, busqueda);
        if (distributivoEscala == null) {
            distributivoEscala = new DistributivoEscala();
        }
        habilitarEscala(distributivo, busqueda.getAnio());
    }

    public void guardar() {
        boolean edit = distributivo.getId() != null;
        if (distributivo.getCargo() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar Cargo.");
            return;
        }
        if (distributivo.getUnidadAdministrativa() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la unidad administrativa.");
            return;
        }
        if (distributivo.getRegimen() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el Regimen Laboral.");
            return;
        }
        if (distributivo.getTipoContrato() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar el tipo de Contrato.");
            return;
        }
        if (distributivo.getVigenciaDesde() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la fecha de Vigencia Desde.");
            return;
        }
        if (distributivoEscala.getRemuneracionDolares() == null || distributivoEscala.getEscalaSalarial().getRemuneracionDolares() == null) {
            JsfUtil.addWarningMessage("Información", " Debe ingresar la Escala Salarial.");
            return;
        }
        if (!edit) {
            distributivo.setEstado(Boolean.TRUE);
            distributivo.setUsuarioCreacion(userSession.getNameUser());
            distributivo.setFechaCreacion(new Date());
            distributivo = distributivoService.create(distributivo);
            for (ValoresDistributivo vde : valoresDistributivoList) {
                if (vde.getProyeccion() == null) {
                    JsfUtil.addWarningMessage("Información", "Debe Agregar la proyección a los valores");
                    return;
                }
            }
            for (ValoresDistributivo vd : valoresDistributivoList) {
                vd = valoresDistributivoService.create(vd);
            }
            distributivoEscala.setEstado(true);
            distributivoEscala.setDistributivo(distributivo);
            distributivoEscala.setAnio(busqueda.getAnio());
            distributivoEscala = distributivoEscalaService.create(distributivoEscala);
        } else {
            distributivoService.edit(distributivo);
            if (distributivoEscala.getId() == null) {
                distributivoEscala.setEstado(true);
                distributivoEscala.setDistributivo(distributivo);
                distributivoEscala.setAnio(busqueda.getAnio());
                distributivoEscala = distributivoEscalaService.create(distributivoEscala);
            } else {
                distributivoEscala.setEstado(true);
                distributivoEscala.setDistributivo(distributivo);
                distributivoEscala.setAnio(busqueda.getAnio());
                distributivoEscalaService.edit(distributivoEscala);
            }
            for (ValoresDistributivo vde : valoresDistributivoList) {
                if (vde.getProyeccion() == null) {
                    JsfUtil.addWarningMessage("Información", "Debe Agregar la proyección a los valores");
                    return;
                }
            }
            for (ValoresDistributivo vd : valoresDistributivoList) {
                valoresDistributivoService.edit(vd);
            }
            cambioEstadoList();
        }
        PrimeFaces.current().executeScript("PF('distributivoDialogo').hide()");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addSuccessMessage("Información", distributivo.getCargo().getNombreCargo() + (edit ? " editada" : " registrada") + " con éxito.");
        distributivo = new Distributivo();
        valoresDistributivoList = new ArrayList<>();
    }

    public Boolean estadoDistributivo(Distributivo dis) {
        return distributivoService.distributivoReformado(dis, "RAU");
    }

    public void eliminar(Distributivo distributiv) {
        if (distributiv.getEstadoActivo()) {
            JsfUtil.addWarningMessage("Verificar", "Verificar que Rubros No esten Presupuesdos o con saldos en CERO");
            return;
        }
        distributiv.setEstado(Boolean.FALSE);
        valorList = new ArrayList<>();
        valorList = valoresDistributivoService.findAllValoresDistributivo(distributiv);
        if (!valorList.isEmpty()) {
            for (ValoresDistributivo vd : valorList) {
                vd.setEstado(false);
                valoresDistributivoService.edit(vd);
            }
        }
        distributivoEscalaList = new ArrayList<>();
        distributivoEscalaList = distributivoEscalaService.getAllEscalaDistributivoAnio(distributiv);
        System.out.println("distributivoEscalaList: " + distributivoEscalaList);
        if (!distributivoEscalaList.isEmpty()) {
            for (DistributivoEscala de : distributivoEscalaList) {
                de.setEstado(false);
                distributivoEscalaService.edit(de);
            }
        }
        distributivoService.edit(distributiv);
        JsfUtil.addSuccessMessage("Información", distributiv.getCargo().getNombreCargo() + "eliminada con éxito");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("messages");
    }

    public void update() {
        if (distributivo.getRegimen() != null) {
            regimen = regimenService.findByNamedQuery("RegimenLaboral.findByPadre", distributivo.getRegimen());
        } else {
            regimen = new ArrayList<>();
        }
    }

    public void openDlgEscala() {
        Utils.openDialog("/facelet/talentoHumano/dialogEscala", "850", "400");
    }

    public void openDlgValoresParametrizables() {
        if (distributivoEscala.getRemuneracionDolares() != null) {
            Map<String, List<String>> params = new HashMap<>();
            params.put("TIPO", Arrays.asList("GEN"));
            Utils.openDialog("/facelet/talentoHumano/dialogValores", "850", "260", params);
        } else {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("Información", "Debe ingresar la Escala Salarial para tomar su RMU y generar la Proyección.");
        }
    }

    public void selectDataEscala(SelectEvent evt) {
        distributivoEscala.setEscalaSalarial((EscalaSalarial) evt.getObject());
        distributivoEscala.setRemuneracionDolares(distributivoEscala.getEscalaSalarial().getRemuneracionDolares());
        if (!valoresDistributivoList.isEmpty()) {
            for (int i = 0; i < valoresDistributivoList.size(); i++) {
                onCellEdit(valoresDistributivoList.get(i));
            }
            PrimeFaces.current().ajax().update("dtValores");
        }
    }

    public void selectDataRubros(SelectEvent evt) {
        Boolean saber = false;
        parametrosTalento = (ParametrosTalentoHumano) evt.getObject();
        valores = new ValoresDistributivo();
        valores.setDistributivo(distributivo);
        valores.setAnio(busqueda.getAnio());
        if (!valoresDistributivoList.isEmpty()) {
            for (int i = 0; i < valoresDistributivoList.size(); i++) {
                if (Objects.equals(parametrosTalento.getTipo(), valoresDistributivoList.get(i).getValoresParametrizados().getTipo())) {
                    saber = true;
                    break;
                }
            }
        }
        if ("ALOSEP".equals(parametrosTalento.getTipo().getCodigo()) || "ACT".equals(parametrosTalento.getTipo().getCodigo())) {
            for (int i = 0; i < valoresDistributivoList.size(); i++) {
                if ("ACT".equals(valoresDistributivoList.get(i).getValoresParametrizados().getTipo().getCodigo()) || "ALOSEP".equals(valoresDistributivoList.get(i).getValoresParametrizados().getTipo().getCodigo())) {
                    saber = true;
                    break;
                }
            }
        }
        if (saber == true) {
            JsfUtil.addWarningMessage("Información", " Este Rubro ya Esta Registrado.");
            return;
        }
        valores.setValoresParametrizados(parametrosTalento);
        valores.setEstado(Boolean.TRUE);
        valores.setValorRubro(parametrosTalento.getValor());
        valoresDistributivoList.add(this.valores);
    }

    public void onCellEdit(ValoresDistributivo rubro) {
        double valorRubro;
        double valorRMU;
        valorRMU = distributivoEscala.getEscalaSalarial().getRemuneracionDolares().doubleValue();
        switch (rubro.getValoresParametrizados().getTipo().getCodigo()) {
            case "RAU":
                valorRubro = (valorRMU * rubro.getProyeccion());
                rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                break;
            case "DC":
                if (rubro.getValorRubro() != null && rubro.getProyeccion() != null) {
                    valorRubro = (rubro.getValorRubro().doubleValue() / 12) * rubro.getProyeccion();
                    rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                break;
            case "DT":
                valorRubro = (valorRMU / 12) * rubro.getProyeccion();
                rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                break;
            case "SBU":
                if (rubro.getValoresParametrizados().getMedicionPorcentaje() == true) {
                    valorRubro = Math.round(rubro.getProyeccion() * rubro.getValorRubro().doubleValue()) / 100d;
                    rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                } else {
                    valorRubro = (rubro.getProyeccion() * rubro.getValorRubro().doubleValue());
                    rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                break;
            /**
             * por default entra para el calculo respectivo de los encargos,
             * otros ingresos, horas extras etc.
             */
            default:
                if (rubro.getValoresParametrizados().getMedicionPorcentaje() == true) {
                    valorRubro = Math.round(valorRMU * rubro.getProyeccion() * rubro.getValorRubro().doubleValue()) / 100d;
                    rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                } else {
                    valorRubro = (valorRMU * rubro.getProyeccion() * rubro.getValorRubro().doubleValue());
                    rubro.setValorResultante(BigDecimal.valueOf(valorRubro).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                }
                break;
        }
    }

    public void cambioEstadoList() {
        if (!listaSeleccionada.isEmpty()) {
            for (ValoresDistributivo vd : listaSeleccionada) {
                vd.setEstado(Boolean.FALSE);
                vd.setAnio(busqueda.getAnio());
                valoresDistributivoService.edit(vd);
                if (vd.getId() != null) {
                    valoresDistributivoService.getDelePartidaValorDistributivo(vd);
                }
            }
        }
    }

    public void borrarRubroListaTest(int pos) {
        Boolean resultado = valoresDistributivoService.getCount(valoresDistributivoList.get(pos));
        if (resultado) {
            JsfUtil.addErrorMessage("ERROR", "No se puede eliminar porque tiene una partida asignada");
        } else {
            if (valoresDistributivoList.get(pos).getDistributivo() != null) {
                listaSeleccionada.add(valoresDistributivoList.get(pos));
            }
            valoresDistributivoList.remove(pos);
            if (valoresDistributivoList.isEmpty()) {
                bloqueo = false;
            }
        }
    }

    public void opendlgPrint() {
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
    }

    public void printReport() {
        if (anio == 0) {
            JsfUtil.addWarningMessage("Información", " Ingrese un año para generar Reporte.");
            return;
        }
        boolean blockPrint = false;
        blockPrint = proformaPresupuestoPlanificadoService.BloquearSiEsAprobado((short) anio, false, true);
        ss.addParametro("anio", anio);
        ss.addParametro("bloqueo", blockPrint);
        if (tipoReporte.equals("all")) {
            ss.setNombreReporte("distributivoGeneral");
        }
        if (tipoReporte.equals("vacantes")) {
            ss.setNombreReporte("distributivoGeneralVacante");
        }
        if (tipoReporte.equals("asignados")) {
            ss.setNombreReporte("distributivoGeneralAsignados");
        }
        ss.setNombreSubCarpeta("Distributivos");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('dlgPrint').hide()");

    }

    public boolean habilitar(ValoresDistributivo v) {
        boolean result = false;
        List<PartidasDistributivo> listPartida;
        if (v.getId() != null) {
            listPartida = partidaService.getPartidasAllXValorRubro(v, busqueda.getAnio());
            if (!listPartida.isEmpty()) {
                for (PartidasDistributivo item : listPartida) {
                    if (item.getEstadoPartida() != null) {
                        if ("AD".equals(item.getEstadoPartida().getCodigo()) || "ARD".equals(item.getEstadoPartida().getCodigo()) || "ATD".equals(item.getEstadoPartida().getCodigo())) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public void habilitarEscala(Distributivo d, Short anio) {
        setBloqueo(Boolean.FALSE);
        List<ValoresDistributivo> valores;
        valores = valoresDistributivoService.getAllValoresDis(d, anio);
        if (valores != null) {
            if (!valores.isEmpty()) {
                setBloqueo(Boolean.TRUE);
            }
        }
    }

    public String loadCodigo(Long id) {
        String cod = Utils.completarCadenaConCeros(id.toString(), 6);
        return "C-" + cod;
    }

    public void limpiarFiltro() {
        distributivoLazy = new LazyModel<>(Distributivo.class);
        distributivoLazy.setDistinct(false);
        distributivoLazy.getSorteds().put("servidorPublico.persona.apellido", "ASC");
    }

    //<editor-fold defaultstate="collapsed" desc="metodo de eliminar recorriendo">
    //    public void borrarRubroLista(ValoresDistributivo valor) {
    //        if (valor.getId() != null) {
    //            valoresDistributivoList.remove(valor);
    //        } else {
    //            int postDelete = -1;
    //            for (int i = 0; i < valoresDistributivoList.size(); i++) {
    //                if (valoresDistributivoList.get(i).getCodigo().equals(valor.getCodigo())) {
    //                    postDelete = i;
    //                    break;
    //                }
    //            }
    //            if (postDelete != -1) {
    //                valoresDistributivoList.remove(postDelete);
    //            }
    //        }
    //    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="get and set">
    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public List<Cargo> getListCargo() {
        return listCargo;
    }

    public void setListCargo(List<Cargo> listCargo) {
        this.listCargo = listCargo;
    }

    public List<UnidadAdministrativa> getListUnidad() {
        return listUnidad;
    }

    public void setListUnidad(List<UnidadAdministrativa> listUnidad) {
        this.listUnidad = listUnidad;
    }

    public List<RegimenLaboral> getListRegimen() {
        return listRegimen;
    }

    public void setListRegimen(List<RegimenLaboral> listRegimen) {
        this.listRegimen = listRegimen;
    }

    public List<RegimenLaboral> getRegimen() {
        return regimen;
    }

    public void setRegimen(List<RegimenLaboral> regimen) {
        this.regimen = regimen;
    }

    public boolean getEditar() {
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public boolean getEditar1() {
        return editar1;
    }

    public void setEditar1(boolean editar1) {
        this.editar1 = editar1;
    }

    public LazyModel<Distributivo> getDistributivoLazy() {
        return distributivoLazy;
    }

    public void setDistributivoLazy(LazyModel<Distributivo> distributivoLazy) {
        this.distributivoLazy = distributivoLazy;
    }

    public ValoresDistributivo getValores() {
        return valores;
    }

    public void setValores(ValoresDistributivo valores) {
        this.valores = valores;
    }

    public List<ValoresDistributivo> getValoresDistributivoList() {
        return valoresDistributivoList;
    }

    public void setValoresDistributivoList(List<ValoresDistributivo> valoresDistributivoList) {
        this.valoresDistributivoList = valoresDistributivoList;
    }

    public ParametrosTalentoHumano getParametrosTalento() {
        return parametrosTalento;
    }

    public void setParametrosTalento(ParametrosTalentoHumano parametrosTalento) {
        this.parametrosTalento = parametrosTalento;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public DistributivoEscala getDistributivoEscala() {
        return distributivoEscala;
    }

    public void setDistributivoEscala(DistributivoEscala distributivoEscala) {
        this.distributivoEscala = distributivoEscala;
    }

    public boolean getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }
//</editor-fold>

    public long getAnio() {
        return anio;
    }

    public void setAnio(long anio) {
        this.anio = anio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

}
