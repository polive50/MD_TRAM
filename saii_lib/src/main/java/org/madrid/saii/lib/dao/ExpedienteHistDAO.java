package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteHist;

/**
 * Interfaz para el DAO de la clase de dominio Expediente
 *
 * @author everis
 */
public interface ExpedienteHistDAO extends BaseDAO<ExpedienteHist, Integer> {

    /**
     * Crea el historico
     *
     * @param expediente
     * @param cdUsuAlta. Se informa si es distinto del login web
     * @return
     */
    ExpedienteHist crearHistorico(Expediente expediente, String cdUsuAlta);
}
