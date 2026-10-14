package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.domain.RevisionInfEvaJustecHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio RevisionInfEvaJustecHist
 *
 * @author quental
 */
@Repository("revisionInfEvaJustecHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class RevisionInfEvaJustecHistDAOImpl extends BaseDAOImpl<RevisionInfEvaJustecHist, Integer>
        implements RevisionInfEvaJustecHistDAO {

    @Override
    @Transactional(readOnly = false)
    public void crearHistorico(RevisionInfEvaJustec revisionInfEvaJustec) {
        RevisionInfEvaJustecHist revisionInfEvaJustecHist = new RevisionInfEvaJustecHist();
        revisionInfEvaJustecHist.setRevisionInfEvaJustec(revisionInfEvaJustec);
        revisionInfEvaJustecHist.setExpediente(revisionInfEvaJustec.getExpediente());
        revisionInfEvaJustecHist.setExpedienteJusTec(revisionInfEvaJustec.getExpedienteJusTec());
        revisionInfEvaJustecHist.setExpedienteDoc(revisionInfEvaJustec.getExpedienteDoc());
        revisionInfEvaJustecHist.setIdEstado(revisionInfEvaJustec.getIdEstado());
        revisionInfEvaJustecHist.setTlObservaciones(revisionInfEvaJustec.getTlObservaciones());
        
        this.insert(revisionInfEvaJustecHist);
    }

}
