package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.ExpedienteAnualidadHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteAnualidad
 *
 * @author nttdata
 */
@Repository("expedienteAnualidadHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteAnualidadHistDAOImp extends BaseDAOImpl<ExpedienteAnualidadHist, Integer>
        implements ExpedienteAnualidadHistDAO {

    @Override
    @Transactional(readOnly = false)
    public void crearHistorico(ExpedienteAnualidad expedienteAnualidad) {

        ExpedienteAnualidadHist expedienteAnualidadHist = new ExpedienteAnualidadHist();
        expedienteAnualidadHist.setIdExpedienteAnualidad(expedienteAnualidad.getIdExpedienteAnualidad());
        expedienteAnualidadHist.setIdExpediente(expedienteAnualidad.getIdExpediente());
        expedienteAnualidadHist.setTipoExpedienteAnualidad(expedienteAnualidad.getTipoExpedienteAnualidad());
        expedienteAnualidadHist.setNmAnualidad(expedienteAnualidad.getNmAnualidad());
        expedienteAnualidadHist.setFcIniEjecucion(expedienteAnualidad.getFcIniEjecucion());
        expedienteAnualidadHist.setFcFinEjecucion(expedienteAnualidad.getFcFinEjecucion());
        expedienteAnualidadHist.setTlObservacion(expedienteAnualidad.getTlObservacion());

        this.insert(expedienteAnualidadHist);
    }

}
