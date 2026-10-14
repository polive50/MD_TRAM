package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionTipoDocDAO;
import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionTipoDoc
 *
 * @author quental
 */
public interface EvaluacionTipoDocService
        extends BaseService<EvaluacionTipoDoc, Integer, EvaluacionTipoDocDAO> {

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
    List<EvaluacionTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     *
     */
    List<EvaluacionTipoDocDTO> obtenerListaEvalTipoDocPorInforme(Integer idInforme,
            Integer idConvocatoria) throws ServiceException;

    /**
     * Método para guardar EvaluacionTipoDocDTO
     * @param idConvocatoria
     * @param idInforme
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    EvaluacionTipoDocDTO agregar(Integer idConvocatoria, Integer idInforme,
            Integer idTipoDocumento) throws ServiceException;
    
    /**
     * 
     * @param EvaluacionTipoDocDTO
     * @return
     * @throws ServiceException
     */
    EvaluacionTipoDocDTO guardar(final EvaluacionTipoDocDTO evaluacionTipoDocDTO)
            throws ServiceException;
    
    /**
     * 
     * @param idEvaluacionTipoDoc
     * @return
     * @throws ServiceException
     */
    EvaluacionTipoDocDTO buscarId(final Integer idEvaluacionTipoDoc) throws ServiceException;

    void borradoLogicoEvalTipoDoc(EvaluacionTipoDocDTO evaluacionTipoDocDTO) throws ServiceException;

   
   

   
}
