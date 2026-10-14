package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.ExpedienteAnualidadHist;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteAnualidadHist
 *
 * @author Quental
 */
public interface ExpedienteAnualidadHistDAO extends BaseDAO<ExpedienteAnualidadHist, Integer> {

    /**
     * Metodo que guarda el historico de expedienteAnualidad
     *
     * @param expedienteAnualidad
     * @return
     */
    void crearHistorico(ExpedienteAnualidad expedienteAnualidad);
}
