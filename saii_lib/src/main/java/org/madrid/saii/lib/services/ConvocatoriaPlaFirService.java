package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaFirDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaPlaFir
 *
 * @author everis
 */
public interface ConvocatoriaPlaFirService
        extends BaseService<ConvocatoriaPlaFir, Integer, ConvocatoriaPlaFirDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaPlaFir.
     *
     * @return List<ConvocatoriaPlaFirDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaFirDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaPlaFir.
     *
     * @param idConvocatoriaPlaFir the id ConvocatoriaPlaFir
     * @return ConvocatoriaPlaFirDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaFirDTO buscarId(Integer idConvocatoriaPlaFir) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaPlaFir.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaPlaFirDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaFirDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaPlaFir.
     *
     * @param convocatoriaPlaFirDTO the convocatoriaPlaFir DTO
     * @return convocatoriaPlaFirDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaFirDTO guardar(ConvocatoriaPlaFirDTO convocatoriaPlaFirDTO)
            throws ServiceException;

    /**
     * Borrado logico ConvocatoriaPlaFir
     *
     * @param ConvocatoriaPlaFirDTO
     * @return ConvocatoriaPlaFirDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaPlaFirDTO convocatoriaplafirDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriaplafirDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaPlaFirDTO convocatoriaplafirDTO) throws ServiceException;

}
