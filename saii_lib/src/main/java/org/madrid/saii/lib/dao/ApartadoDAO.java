package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Apartado;

/**
 * Interfaz para el DAO de la clase de dominio Apartado
 *
 * @author nttdata
 */
public interface ApartadoDAO extends BaseDAO<Apartado, Integer> {

    /**
     * 
     * @param idEvaluacion
     * @return
     */
    List<Apartado> buscarPorEvaluacion(Integer idEvaluacion);
}
