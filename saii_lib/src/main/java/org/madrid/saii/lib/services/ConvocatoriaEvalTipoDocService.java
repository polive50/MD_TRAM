package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEvalTipoDocDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
public interface ConvocatoriaEvalTipoDocService
        extends BaseService<ConvocatoriaEvalTipoDoc, Integer, ConvocatoriaEvalTipoDocDAO> {

    /**
     * Buscar paginadamente Documentacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaDocEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEvalTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     *
     */
    List<ConvocatoriaEvalTipoDocDTO> obtenerListaConvocatoriaEvalTipoDocPorNivel(Integer nivel,
            Integer idConvocatoria) throws ServiceException;

    /**
     * Método para guardar ConvocatoriaEvalTipoDocDTO
     * @param idConvocatoria
     * @param idNivelAsociacion
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    ConvocatoriaEvalTipoDocDTO agregar(Integer idConvocatoria, Integer idNivelAsociacion,
            Integer idTipoDocumento) throws ServiceException;
    
    /**
     * 
     * @param convocatoriaEvalTipoDocDTO
     * @return
     * @throws ServiceException
     */
    ConvocatoriaEvalTipoDocDTO guardar(final ConvocatoriaEvalTipoDocDTO convocatoriaEvalTipoDocDTO)
            throws ServiceException;
    
    /**
     * 
     * @param convocatoriaEvalTipoDocId
     * @return
     * @throws ServiceException
     */
    ConvocatoriaEvalTipoDocDTO buscarId(final Integer convocatoriaEvalTipoDocId) throws ServiceException;

   
   

   
}
