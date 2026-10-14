
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.dto.BloqueDTO;

/**
 * Interfaz para el DAO de la clase de dominio Bloque
 *
 * @author everis
 */
public interface BloqueDAO extends BaseDAO<Bloque, Integer> {
    
    /**
     * @param idBloquePadre
     * @return
     */
    Bloque buscarIdBloquePadre(Integer idBloquePadre);

    List<Bloque> listBloquesMultiplesPorBloquePadre(Integer idBloquePadre);
    
    List<BloqueDTO> lstBloqueMultipleByIdBloquePadre(Integer idBloquePadre);

}
