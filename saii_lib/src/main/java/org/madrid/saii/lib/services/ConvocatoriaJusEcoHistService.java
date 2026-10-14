package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusEcoHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusEcoHist
 *
 * @author quental
 */
public interface ConvocatoriaJusEcoHistService
        extends BaseService<ConvocatoriaJusEcoHist, Integer, ConvocatoriaJusEcoHistDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusEcoHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusEcoHistDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaJusEcoHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaJusEcoHist.
     *
     * @param ConvocatoriaJusEcoHistDTO the ConvocatoriaJusEcoHist DTO
     * @return ConvocatoriaJusEcoHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaJusEcoHistDTO guardar(ConvocatoriaJusEcoHistDTO convocatoriaJusEcoHistDTO)
            throws ServiceException;

}
