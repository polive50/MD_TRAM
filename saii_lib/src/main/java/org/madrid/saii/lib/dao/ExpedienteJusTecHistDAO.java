package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteJusTecHist;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteJusTecHist
 *
 * @author nttdata
 */
public interface ExpedienteJusTecHistDAO extends BaseDAO<ExpedienteJusTecHist, Integer> {

    /**
     * Metodo que guarda el historico de expedienteJusTec
     *
     * @param expedienteJusTec
     * @return
     */
    void crearHistorico(ExpedienteJusTec expedienteJusTec);
}
