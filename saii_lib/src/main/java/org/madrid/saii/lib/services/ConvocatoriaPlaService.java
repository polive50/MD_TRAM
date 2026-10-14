package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaDAO;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaPla
 *
 * @author everis
 */
public interface ConvocatoriaPlaService
        extends BaseService<ConvocatoriaPla, Integer, ConvocatoriaPlaDAO> {

    /**
     * Buscar por id ConvocatoriaPla.
     *
     * @param idConvocatoriaPla the id ConvocatoriaPla
     * @return ConvocatoriaPlaDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaDTO buscarId(Integer idConvocatoriaPla) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaPla.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaPlaDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaPla.
     *
     * @param convocatoriaPlaDTO the convocatoriaPla DTO
     * @return convocatoriaPlaDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaDTO guardar(ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException;
    
    /**
     * Borrado logico ConvocatoriaPla
     *
     * @param ConvocatoriaPlaDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param ConvocatoriaPlaDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException;

    /**
     * Permite obtener las plantillas por id de convocatoria
     *
     * @param idConvocatoria Identificador de convocatoria
     * @return Lista de areas cientificas asociadas a la convocatoria
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    List<ConvocatoriaPlaDTO> obtenerPlantillasPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Carga las plantillas de una nueva convocatoria a partir de su id
     *
     * @param idConvocatoria
     * @throws ServiceException
     */
    void nuevaConvocatoria(Convocatoria convocatoria) throws ServiceException;

    /**
     * listar los tramites relacionados con la convocatoria
     *
     * @param idConvocatoria
     * @return List<ConvocatoriaPlaDTO>
     * @throws ServiceException
     */
    public List<ConvocatoriaPlaDTO> tramiteConvocatoria(Integer idConvocatoria)
            throws ServiceException;
}
