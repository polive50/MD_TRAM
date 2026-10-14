
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaAct
 *
 * @author everis
 */
public interface ConvocatoriaActDAO extends BaseDAO<ConvocatoriaAct, Integer> {
    
    /**
     * 
     * @param idConvocatoriaAct
     * @return
     */
    public List<ConvocatoriaActDTO> buscarActoresAsociados(ConvocatoriaActDTO convocatoriaActDTO);
    
}
