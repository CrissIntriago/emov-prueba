/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.interfaces;

import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Criss Intriago
 */
@Local
public interface ContRegistroPago {

    public String validaciones(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago);

    public ContComprobantePago create(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles,
            List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago);

    public ContComprobantePago edit(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles,
            List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete, List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago,
            List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagoDelete);

    public ContDiarioGeneral anular(ContComprobantePago contComprobantePago);

    public DetalleBanco beneficiarioBanco(Cliente cliente);

    public List<ContDiarioGeneralDetalle> detalleComprobantePago(ContDiarioGeneral diarioGeneral);

    public ContComprobantePago findById(Long idComprobante);

    public List<ContDiarioGeneralDetalle> findByIdComprobantePago(ContComprobantePago contComprobantePago);

    public List<ContBeneficiarioComprobantePago> findByIdBeneficiaciosPagos(ContComprobantePago contComprobantePago);

    public void ComprobantPagoImprimirReporte(ContComprobantePago contComprobantePago, String tipoDocumento);

    public ContComprobantePago findByNumRegistro(Integer idComprobante, Short periodo);

    public void editTransferencia(ContComprobantePago idContComprobantePago);

    public ThTipoRol findThTipoRol(ContDiarioGeneral diario);

    public List<ThLiquidacionRol> getThLiquidacionRol(ThTipoRol thTipoRol);

}
