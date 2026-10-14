
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.GcatusuarsDTO;

/**
 * Interfaz para el DAO Gcatusuar
 *
 * @author everis
 */
public interface GcatusuarsDAO extends BaseDAO<UnidadAdmin, Integer> {

    /**
     * Recupera la Unidad a la que pertenece el login
     *
     * @param cdUsuario
     * @return GcatusuarsDTO
     */
    GcatusuarsDTO buscarCdUsuario(String cdUsuario);

    /**
     * Devuelve la descripcion de la unidad
     * 
     * @param cdUnidad
     * @return
     */
    String descripcionUnidad(String cdUnidad);

}
