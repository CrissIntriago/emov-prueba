/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.interfaces;

import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContTransferencias;
import com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Local
public interface ContRegistroTransferencia {

    public ContTransferencias findById(Long idTransferencia);

    public List<ContTransferenciasDetalle> findByIdTransferencia(ContTransferencias contTransferencias);

    public String validaciones(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList);

    public ContComprobantePago findNumComprobante(Long idComprobante, Short periodo);

    public List<ContBeneficiarioComprobantePago> findByBeneficiario(ContComprobantePago comprobante);

    public ContTransferencias initObject(ContComprobantePago comprobante, ContTransferencias contTransferencias);

    public List<ContTransferenciasDetalle> detalleTransferencia(ContComprobantePago comprobante, List<ContTransferenciasDetalle> contTransferenciasDetalleList);

    public ContTransferencias edit(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList);

    public ContTransferencias create(ContTransferencias contTransferencias, List<ContTransferenciasDetalle> contTransferenciasDetalleList);

    public void generarArchivosDescargas(int accion, ContTransferencias contTransferencias) throws FileNotFoundException;

    public void generarReporte(ContTransferencias contTransferencias, String tipoArchivo);

    public void anularGeneral(ContTransferencias contTransferencias);

    public void upload(ContTransferencias contTransferencias, List<UploadedFile> files,Date fechaAcreditacion);

    public ThServidorCargo getCargoServidor(Servidor servidor);
}
