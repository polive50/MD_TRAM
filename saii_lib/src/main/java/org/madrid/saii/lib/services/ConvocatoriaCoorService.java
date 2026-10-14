package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaCoorDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorFiltrosDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorNavegacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCoor
 *
 * @author nttdata
 */
public interface ConvocatoriaCoorService
extends BaseService<ConvocatoriaCoor, Integer, ConvocatoriaCoorDAO> {

    /**
     * Buscar por id ConvocatoriaCoor.
     *
     * @param idConvocatoriaCoor the id ConvocatoriaCoor
     * @return ConvocatoriaCoorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorDTO buscarId(Integer idConvocatoriaCoor) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCoor.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaCoorDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCoorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCoor.
     *
     * @param convocatoriaCoorDTO the convocatoriaCoor DTO
     * @return convocatoriaCoorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorDTO guardar(ConvocatoriaCoorDTO convocatoriaCoorDTO) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaCoorDTO> obtenerCoordinadoresPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param idArea
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    Boolean existeAreaPersonaConvocatoriaCoor(Integer idConvocatoria, Integer idArea,
            Integer idInteresado) throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param idArea
     * @param IdTipoInforme
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    Boolean existeAreaPersonaConvocatoriaCoor(Integer idConvocatoria, Integer idArea,
            Integer IdTipoInforme, Integer idInteresado) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param idArea
     * @return
     * @throws ServiceException
     */
    Boolean existeConvocatoriaCoorArea(Integer idConvocatoria, Integer idArea)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param idTipoInforme
     * @return
     * @throws ServiceException
     */
    Boolean existeConvocatoriaCoorTipoInforme(Integer idConvocatoria, Integer idTipoInforme) throws ServiceException;

    /**
     * Obtener convocatorias Coordinador.
     *
     * @return ConvocatoriaCoorNavegacionDTO
     * @throws ServiceException
     */
    ConvocatoriaCoorNavegacionDTO obtenerConvocatoriaCoor(ConvocatoriaCoorFiltrosDTO filtros)
            throws ServiceException;

    /**
     * Obtener el coordinador de convocatoria por idConvocatoria y idArea.
     *
     * @param idConvocatoria el Convocatoria
     * @param idArea el Area
     * @return List<ConvocatoriaCoorDTO>
     * @throws ServiceException
     */
    List<ConvocatoriaCoorDTO> obtenerCoordinadorConvocatoriaArea(Integer idConvocatoria,
            Integer idArea) throws ServiceException;

    /**
     * Eliminar el coordinador de convocatoria por idConvocatoriaCoor.
     *
     * @param idConvocatoriaCoor
     * @throws ServiceException
     */
    void eliminarCoordinadorEvaluador(Integer idConvocatoriaCoor) throws ServiceException;
    
    /**
     * Buscar por id Convocatoria.
     *
     * @param idConvocatoria the id Convocatoria
     * @return ConvocatoriaCoorDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCoorDTO buscarByIdConvocatoria(Integer idConvocatoria) throws ServiceException;

    
    /**
     * Buscar por id Convocatoria.
     * Buscar por id Area.
     * @param idConvocatoria the id Convocatoria
     * @return ConvocatoriaCoorDTO
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCoorDTO> buscarByIdConvocatoriaAndIdArea(Integer idConvocatoria,
            Integer idArea) throws ServiceException;


}
