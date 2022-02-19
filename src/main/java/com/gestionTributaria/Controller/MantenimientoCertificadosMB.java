package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.CertificadosServices;
import com.gestionTributaria.Services.DetalleCertificadoServices;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class MantenimientoCertificadosMB implements Serializable {

    @Inject
    private ManagerService manager;
    @Inject
    private DetalleCertificadoServices detalleCertificadoServices;
    @Inject
    private CertificadosServices certificadosServices;
    private TreeNode root2;
    private Map<String, Object> paramt;
    private TreeNode selectedNode;
    private FinaRenLiquidacion cobrosGenerales = new FinaRenLiquidacion();
    private String texto;
    private CabeceraCertificado cabeceraCertificado;
    private DetalleCertificado detalleCertificado;
    private String textoRemplazar;
    private String nuevoTexto;

    @PostConstruct
    public void initView() {
        llenarArbol();
    }

    public void llenarArbol() {
        try {
            cabeceraCertificado = new CabeceraCertificado();
            detalleCertificado = new DetalleCertificado();
            root2 = new DefaultTreeNode("tipos_liquidaciones", null);
            List<FinaRenTipoLiquidacion> raices;
            paramt = new HashMap<>();
            paramt.put("prefijo", Arrays.asList("PCN"));
            paramt.put("estado", true);
            raices = manager.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE  r.nombreTransaccion IS NOT NULL AND "
                    + "r.estado=:estado AND  r.prefijo in (:prefijo)  ORDER BY r.transaccionPadre, r.nombreTransaccion ASC", paramt);
            System.out.println("raices " + raices.size());
            for (FinaRenTipoLiquidacion temp : raices) {
                if (!temp.getTomado()) {
                    temp.setTomado(true);
                    TreeNode node = new DefaultTreeNode(temp, root2);
                    node.setExpanded(true);
                    llenarHijosArbol(temp, node);
                }
            }
            System.out.println("asd: " + root2);
        } catch (Exception e) {
            System.out.println("Error en llenar arbol certificados" + e);
        }
    }

    public void nuevoCertificado() {
        cabeceraCertificado = new CabeceraCertificado();
        cabeceraCertificado.setFecha(new Date());
        cabeceraCertificado.setTipoLiquidacion((FinaRenTipoLiquidacion) selectedNode.getData());
        cabeceraCertificado = (CabeceraCertificado) certificadosServices.create(cabeceraCertificado);
        detalleCertificado = new DetalleCertificado();
        detalleCertificado.setCabeceraCertificado(cabeceraCertificado);
        detalleCertificado.setTexto(texto);
        detalleCertificado = detalleCertificadoServices.create(detalleCertificado);
        JsfUtil.update("mainForm");
        cabeceraCertificado = new CabeceraCertificado();
    }

    public void actualizarDatos() {
        texto = texto.replace(textoRemplazar, (" " + (nuevoTexto.trim()) + " ").toUpperCase());
        JsfUtil.update("mainForm");
    }

    public void añadirTabla() {
        texto = texto + "<table>"
                + "  <tr>"
                + "    <td>Emil</td>"
                + "    <td>Tobias</td>"
                + "    <td>Linus</td>"
                + "  </tr>"
                + "</table>";
        JsfUtil.update("mainForm");
    }

    public void editarCertificado() {
        certificadosServices.edit(cabeceraCertificado);
        detalleCertificado.setCabeceraCertificado(cabeceraCertificado);
        detalleCertificado.setTexto(texto);
        detalleCertificadoServices.edit(detalleCertificado);
        JsfUtil.update("mainForm");
    }

    public void onNodeSelect() {
        paramt = new HashMap<>();
        paramt.put("tipoLiquidacion", (FinaRenTipoLiquidacion) selectedNode.getData());
        cabeceraCertificado = manager.findByParameter(CabeceraCertificado.class, paramt);
        if (cabeceraCertificado != null) {
            paramt = new HashMap<>();
            paramt.put("cabeceraCertificado", cabeceraCertificado);
            detalleCertificado = manager.findByParameter(DetalleCertificado.class, paramt);
            texto = detalleCertificado.getTexto();
        } else {
            paramt = new HashMap<>();
            paramt.put("id", 2);
            cabeceraCertificado = manager.findByParameter(CabeceraCertificado.class, paramt);
            paramt = new HashMap<>();
            paramt.put("cabeceraCertificado", 2);
            detalleCertificado = manager.findByParameter(DetalleCertificado.class, paramt);
            texto = "Con fecha de " + cabeceraCertificado.getFecha() + "";
            JsfUtil.addInformationMessage("Info", "No se encontrarón resultados.");
        }
        JsfUtil.update("mainForm");
    }

    public void llenarHijosArbol(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            List<FinaRenTipoLiquidacion> hijos;
            String sql = "SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre=:idPadre AND r.nombreTransaccion IS NOT NULL and (r.nombreReporte like '%PERMISOS%' or r.nombreReporte like '%CERTIFICADO%')"
                    + "ORDER BY r.transaccionPadre, r.nombreTransaccion ASC";
            paramt = new HashMap<>();
            paramt.put("idPadre", hoja.getId());

            hijos = manager.findAllQuery(sql, paramt);

            if (hijos == null || hijos.isEmpty()) {
                return;
            }

            for (FinaRenTipoLiquidacion temp2 : hijos) {
                if (!temp2.getTomado()) {
                    TreeNode node = new DefaultTreeNode(temp2, padre);
                    temp2.setTomado(true);
                    llenarHijosArbol(temp2, node);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TreeNode getRoot2() {
        return root2;
    }

    public void setRoot2(TreeNode root2) {
        this.root2 = root2;
    }

    public Map<String, Object> getParamt() {
        return paramt;
    }

    public void setParamt(Map<String, Object> paramt) {
        this.paramt = paramt;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public FinaRenLiquidacion getCobrosGenerales() {
        return cobrosGenerales;
    }

    public void setCobrosGenerales(FinaRenLiquidacion cobrosGenerales) {
        this.cobrosGenerales = cobrosGenerales;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public CabeceraCertificado getCabeceraCertificado() {
        return cabeceraCertificado;
    }

    public void setCabeceraCertificado(CabeceraCertificado cabeceraCertificado) {
        this.cabeceraCertificado = cabeceraCertificado;
    }

    public DetalleCertificado getDetalleCertificado() {
        return detalleCertificado;
    }

    public void setDetalleCertificado(DetalleCertificado detalleCertificado) {
        this.detalleCertificado = detalleCertificado;
    }

    public String getTextoRemplazar() {
        return textoRemplazar;
    }

    public void setTextoRemplazar(String textoRemplazar) {
        this.textoRemplazar = textoRemplazar;
    }

    public String getNuevoTexto() {
        return nuevoTexto;
    }

    public void setNuevoTexto(String nuevoTexto) {
        this.nuevoTexto = nuevoTexto;
    }

}
