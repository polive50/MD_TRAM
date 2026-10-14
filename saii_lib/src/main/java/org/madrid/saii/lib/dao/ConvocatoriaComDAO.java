package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaCom
 *
 * @author nttdata
 */
public interface ConvocatoriaComDAO extends BaseDAO<ConvocatoriaCom, Integer> {
    
    /**
     * @param idConvocatoria
     * @return
     * @throws ServiceException 
     */
    List<ConvocatoriaComDTO> buscarAsc(Integer idConvocatoria) ;
    
    /**
     * @param idConvocatoria
     * @return
     * @throws ServiceException 
     */
    List<ConvocatoriaComDTO> buscarDesc(Integer idConvocatoria) ;
    
    /**
     * @param idConvocatoria
     * @return
     * @throws ServiceException 
     */
    List<ConvocatoriaComDTO> buscarActCorreo(Integer idConvocatoria, Integer idTramite);

}
