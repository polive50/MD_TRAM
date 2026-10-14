
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteReqHist
 *
 * @author everis
 */
public interface ExpedienteReqHistDAO extends BaseDAO<ExpedienteReqHist, Integer> {
    /**
     * 
     * @param expedienteReq
     */
    public void crearHistorico(ExpedienteReq expedienteReq);
}
