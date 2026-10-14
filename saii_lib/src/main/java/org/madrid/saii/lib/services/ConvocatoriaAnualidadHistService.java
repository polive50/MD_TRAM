package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAnualidadHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidadHist;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaAnualidadHist
 *
 * @author Quental
 */
public interface ConvocatoriaAnualidadHistService
        extends BaseService<ConvocatoriaAnualidadHist, Integer, ConvocatoriaAnualidadHistDAO> {

    /**
     * Buscar paginadamente ConvocatoriaAnualidadHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaAnualidadHistDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaAnualidadHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaAnualidadHist.
     *
     * @param ConvocatoriaAnualidadHistDTO the ConvocatoriaAnualidadHist DTO
     * @return ConvocatoriaAnualidadHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaAnualidadHistDTO guardar(ConvocatoriaAnualidadHistDTO convocatoriaAnualidadHistDTO)
            throws ServiceException;

}
