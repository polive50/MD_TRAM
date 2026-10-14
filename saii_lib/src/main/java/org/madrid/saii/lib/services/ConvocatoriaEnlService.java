package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEnlDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.madrid.saii.lib.dto.ConvocatoriaEnlDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaEnl
 *
 * @author everis
 */
public interface ConvocatoriaEnlService
        extends BaseService<ConvocatoriaEnl, Integer, ConvocatoriaEnlDAO> {

    /**
     * Buscar paginadamente enlaces.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaEnlDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEnlDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un enlace.
     *
     * @param ConvocatoriaEnlDTO the ConvocatoriaEnlDTO
     * @return ConvocatoriaEnlDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEnlDTO guardar(final ConvocatoriaEnlDTO convocatoriaEnlDTO) throws ServiceException;

    /**
     * Buscar por id enlace.
     *
     * @param idEnlace the id Enlace
     * @return ConvocatoriaEnlDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEnlDTO buscarId(final Integer idEnlace) throws ServiceException;

    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaEnlDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEnlDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;
}
