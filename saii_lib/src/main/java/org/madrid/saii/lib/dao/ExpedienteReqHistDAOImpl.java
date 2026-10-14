
package org.madrid.saii.lib.dao;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteReq
 *
 * @author everis
 */
@Repository("expedienteReqHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteReqHistDAOImpl extends BaseDAOImpl<ExpedienteReqHist, Integer>
        implements ExpedienteReqHistDAO {

    @Override
    public void crearHistorico(ExpedienteReq expedienteReq) {
        ExpedienteReqHist expedienteReqHist = new ExpedienteReqHist();
        expedienteReqHist.setDocumentoPtfr(expedienteReq.getDocumentoPtfr());
        expedienteReqHist.setExpediente(expedienteReq.getExpediente());
        expedienteReqHist.setIdExpedienteDoc(expedienteReq.getIdExpedienteDoc());
        expedienteReqHist.setItCumple(expedienteReq.getItCumple());
        expedienteReqHist.setRequisito(expedienteReq.getRequisito());
        expedienteReqHist.setTlObservaciones(expedienteReq.getTlObservaciones());
        expedienteReqHist.setTlObservacionesCiudadano(expedienteReq.getTlObservacionesCiudadano());
        if (Objects.nonNull(expedienteReq.getFcModifFila())) {
            expedienteReqHist.setFcAltaFila(expedienteReq.getFcModifFila());
        } else {
            expedienteReqHist.setFcAltaFila(expedienteReq.getFcAltaFila());
        }
        this.insert(expedienteReqHist);
    }

}
