package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaCoorEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.ConvocatoriaCoorEvalDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCoorEval
 *
 * @author nttdata
 */
public interface ConvocatoriaCoorEvalService
        extends BaseService<ConvocatoriaCoorEval, Integer, ConvocatoriaCoorEvalDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaCoorEval.
     *
     * @return List<ConvocatoriaCoorEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCoorEvalDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaCoorEval.
     *
     * @param idConvocatoriaCoorEval the id ConvocatoriaCoorEval
     * @return ConvocatoriaCoorEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorEvalDTO buscarId(Integer idConvocatoriaCoorEval) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCoorEval.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaCoorEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCoorEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCoorEval.
     *
     * @param convocatoriaCoorEvalDTO the convocatoriaCoorEval DTO
     * @return convocatoriaCoorEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorEvalDTO guardar(ConvocatoriaCoorEvalDTO convocatoriaCoorEvalDTO)
            throws ServiceException;

    /**
     * Borrado logico ConvocatoriaCoorEval
     *
     * @param ConvocatoriaCoorEvalDTO
     * @return ConvocatoriaCoorEvalDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaCoorEvalDTO convocatoriacoorevalDTO)
            throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriacoorevalDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaCoorEvalDTO convocatoriacoorevalDTO) throws ServiceException;
    
    /**
     * Buscar por id ConvocatoriaCoor.
     *
     * @param idConvocatoriaCoor the id ConvocatoriaCoor
     * @return ConvocatoriaCoorEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorEvalDTO buscarByIdConvocatoriaCoor(Integer idConvocatoriaCoor)
            throws ServiceException;

    /**
     * Buscar si existe area persona convocatoria coor eval
     * 
     * @param idConvocatoria
     * @param idTipoInforme
     * @param idArea
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    Boolean existeAreaPersonaConvocatoriaCoorEval(Integer idConvocatoria, Integer idTipoInforme,
            Integer idArea, Integer idInteresado) throws ServiceException;

    List<ConvocatoriaCoorEvalDTO> buscarByIdConvocatoriaCoorLista(Integer idConvocatoriaCoor)
            throws ServiceException;
    
}
