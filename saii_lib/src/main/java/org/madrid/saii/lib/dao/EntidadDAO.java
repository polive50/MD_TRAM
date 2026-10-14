package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.Entidad;

/**
 * Interfaz para el DAO de la clase de dominio Entidad
 *
 * @author everis
 */
public interface EntidadDAO extends BaseDAO<Entidad, Integer> {
    
    /**
     * 
     * @return
     */
    List<Entidad> buscarTodos();

}
