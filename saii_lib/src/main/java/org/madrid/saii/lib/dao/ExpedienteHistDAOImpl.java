
package org.madrid.saii.lib.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteDocHist
 *
 * @author everis
 */
@Repository("expedienteHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteHistDAOImpl extends BaseDAOImpl<ExpedienteHist, Integer>
        implements ExpedienteHistDAO {

    /**
     *
     */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteHist crearHistorico(Expediente expediente, String cdUsuAlta) {

        ExpedienteHist expedienteHist = new ExpedienteHist();

        expedienteHist.setCdExpediente(expediente.getCdExpediente());
        expedienteHist.setIdExpediente(expediente.getIdExpediente());
        expedienteHist.setIdSolicitud(expediente.getSolicitud().getIdSolicitud());
        expedienteHist.setInteresado(expediente.getInteresado());
        expedienteHist.setEstado(expediente.getEstado());
        expedienteHist.setEstadoBenef(expediente.getEstadoBenef());
       
        expedienteHist.setIdConvocatoriaAct(expediente.getConvocatoriaAct().getIdActor());
        expedienteHist.setCdEreg(expediente.getCdEreg());
        expedienteHist.setDsDestinoEreg(expediente.getDsDestinoEreg());
        expedienteHist.setFcFirma(expediente.getFcFirma());
        expedienteHist.setNmPaso(expediente.getNmPaso());
        expedienteHist.setNmNota(expediente.getNmNota());
        expedienteHist.setCdSiex(expediente.getCdSiex());
        expedienteHist.setEstadoRevAdmin(expediente.getEstadoRevAdmin());
        expedienteHist.setEstadoRevRequi(expediente.getEstadoRevRequi());
        expedienteHist.setEstadoRevEval(expediente.getEstadoRevEval());
        expedienteHist.setEstadoRevFinan(expediente.getEstadoRevFinan());
        expedienteHist.setEstadoRevContrato(expediente.getEstadoRevContrato());
        expedienteHist.setEstadoProvisional(expediente.getEstadoProvisional());
        expedienteHist.setItAlegaciones(expediente.getItAlegaciones());
        expedienteHist.setCdFicaValoracionAleg(expediente.getCdFicaValoracionAleg());
        expedienteHist.setCdEregSubsanaReq(expediente.getCdEregSubsanaReq());
        expedienteHist.setFcFirmaSubsanaReq(expediente.getFcFirmaSubsanaReq());
        
        //GIM MD-1889 22/28/2025 AÑADIENDO LOS NUEVOS ATRIBUTOS
        expedienteHist.setMotivoRechazo(expediente.getMotivoRechazo());
        expedienteHist.setConvocatoriaArea(expediente.getConvocatoriaArea());
        expedienteHist.setConvocatoriaAreaSubarea(expediente.getConvocatoriaAreaSubarea());
        expedienteHist.setConvocatoriaSubmod(expediente.getConvocatoriaSubmod());
        expedienteHist.setIdMedioNotificacion(expediente.getIdMedioNotificacion());
        expedienteHist.setEstadoRevFinan(expediente.getEstadoRevFinan());
        expedienteHist.setEstadoRevJusTec(expediente.getEstadoRevJusTec());
        expedienteHist.setIdExpedientePadre(expediente.getIdExpedientePadre());
        expedienteHist.setItAlegacionRevisada(expediente.getItAlegacionRevisada());
        expedienteHist.setCdEregSubsanaAdm(expediente.getCdEregSubsanaAdm());
        expedienteHist.setFcFirmaSubsanaAdm(expediente.getFcFirmaSubsanaAdm());
        expedienteHist.setEstadoRevJusEco(expediente.getEstadoRevJusEco());
        expedienteHist.setRepresentanteLegal(expediente.getRepresentanteLegal());
        expedienteHist.setIdPersonaSolicitante(expediente.getIdPersonaSolicitante());
        expedienteHist.setCdEregAlegaciones(expediente.getCdEregAlegaciones());  
        expedienteHist.setFcFirmaAlegaciones(expediente.getFcFirmaAlegaciones());
        expedienteHist.setItAceptaAyuda(expediente.getItAceptaAyuda());
        expedienteHist.setAutorizado(expediente.getAutorizado());
        
        // Auditoria
        expedienteHist.setFcAltaFila(new Date());
        if (StringUtils.isNotBlank(cdUsuAlta)) {
            expedienteHist.setCdUsuAlta(cdUsuAlta);
        }
        expedienteHist = this.insert(expedienteHist);

        return expedienteHist;
    }
}
