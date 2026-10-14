package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaCauEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCauEval
 *
 * @author nttdata
 */
public interface ConvocatoriaCauEvalService
        extends BaseService<ConvocatoriaCauEval, Integer, ConvocatoriaCauEvalDAO> {

    /**
     * Buscar por id ConvocatoriaCauEval.
     *
     * @param idConvocatoriaCauEval the id ConvocatoriaCauEval
     * @return ConvocatoriaCauEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauEvalDTO buscarId(Integer idConvocatoriaCauEval) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCauEval.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaCauEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCauEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCauEval.
     *
     * @param convocatoriaCauEvalDTO the convocatoriaCauEval DTO
     * @return convocatoriaCauEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauEvalDTO agregar(Integer idConvocatoria, Integer idCausaEval)
            throws ServiceException;
    
    
    ConvocatoriaCauEvalDTO buscarIdConvocatoriaCauDTO(Integer idConvocatoria, Integer idCausaEval)
            throws ServiceException;
    
    

}
