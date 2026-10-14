package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.madrid.saii.lib.domain.RevisionInfEvaJustecHist;

/**
 * Interfaz para el DAO de la clase de dominio RevisionInfEvaJustecHist
 *
 * @author quental
 */
public interface RevisionInfEvaJustecHistDAO extends BaseDAO<RevisionInfEvaJustecHist, Integer> {

    /**
     * Metodo que guarda el historico de expedienteJusTec
     *
     * @param expedienteJusTec
     * @return
     */
    void crearHistorico(RevisionInfEvaJustec revisionInfEvaJustec);

}
