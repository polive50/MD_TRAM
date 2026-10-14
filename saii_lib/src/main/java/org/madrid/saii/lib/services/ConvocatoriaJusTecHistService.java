package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusTecHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaJusTecHist
 *
 * @author nttdata
 */
public interface ConvocatoriaJusTecHistService
        extends BaseService<ConvocatoriaJusTecHist, Integer, ConvocatoriaJusTecHistDAO> {

    /**
     * Buscar paginadamente ConvocatoriaJusTecHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaJusTecHistDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaJusTecHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaJusTecHist.
     *
     * @param ConvocatoriaJusTecHistDTO the ConvocatoriaJusTecHist DTO
     * @return ConvocatoriaJusTecHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaJusTecHistDTO guardar(ConvocatoriaJusTecHistDTO convocatoriaJusTecHistDTO)
            throws ServiceException;

}
