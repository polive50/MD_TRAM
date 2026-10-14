package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Evaluacion;

/**
 * Interfaz para el DAO de la clase de dominio Evaluacion
 *
 * @author nttdata
 */
public interface EvaluacionDAO extends BaseDAO<Evaluacion, Integer> {
    /**
     * 
     * @param idInforme
     * @return
     */
    List<Evaluacion> buscarPorInforme(Integer idInforme);
}
