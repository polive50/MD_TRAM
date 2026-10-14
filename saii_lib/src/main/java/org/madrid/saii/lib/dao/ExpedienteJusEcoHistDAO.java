package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteJusEcoHist
 *
 * @author quental
 */
public interface ExpedienteJusEcoHistDAO extends BaseDAO<ExpedienteJusEcoHist, Integer> {

    /**
     * Metodo que guarda el historico de expedienteJusEco
     *
     * @param expedienteJusEco
     * @return
     */
    void crearHistorico(ExpedienteJusEco expedienteJusEco);
}
