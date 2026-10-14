package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaFechaTramDAO;
import org.madrid.saii.lib.domain.ConvocatoriaFechaTram;
import org.madrid.saii.lib.dto.ConvocatoriaFechaTramDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaFechaTram
 *
 * @author everis
 */
public interface ConvocatoriaFechaTramService
        extends BaseService<ConvocatoriaFechaTram, Integer, ConvocatoriaFechaTramDAO> {

    /**
     * Buscar paginadamente ConvocatoriaFechaTram.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaFechaTram>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaFechaTramDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda una ConvocatoriaFechaTram.
     *
     * @param convocatoriaFechaTramDTO the ConvocatoriaFechaTramDTO
     * @return ConvocatoriaFechaTramDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaFechaTramDTO guardar(final ConvocatoriaFechaTramDTO convocatoriaFechaTramDTO) throws ServiceException;

    /**
     * Buscar por idConvocatoriaFechaTram.
     *
     * @param idConvocatoriaFechaTram the idConvocatoriaFechaTram
     * @return ConvocatoriaFechaTramDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaFechaTramDTO buscarId(final Integer idConvocatoriaFechaTram) throws ServiceException;

    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaFechaTramDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaFechaTramDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;
}
