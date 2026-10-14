package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaVerDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.dto.ConvocatoriaPlaVerDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaPlaVer
 *
 * @author everis
 */
public interface ConvocatoriaPlaVerService
        extends BaseService<ConvocatoriaPlaVer, Integer, ConvocatoriaPlaVerDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaPlaVer.
     *
     * @return List<ConvocatoriaPlaVerDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaVerDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaPlaVer.
     *
     * @param idConvocatoriaPlaVer the id ConvocatoriaPlaVer
     * @return ConvocatoriaPlaVerDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaVerDTO buscarId(Integer idConvocatoriaPlaVer) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaPlaVer.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaPlaVerDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaVerDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaPlaVer.
     *
     * @param convocatoriaPlaVerDTO the convocatoriaPlaVer DTO
     * @return convocatoriaPlaVerDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaVerDTO guardar(ConvocatoriaPlaVerDTO convocatoriaPlaVerDTO)
            throws ServiceException;

    /**
     * Borrado logico ConvocatoriaPlaVer
     *
     * @param ConvocatoriaPlaVerDTO
     * @return ConvocatoriaPlaVerDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaPlaVerDTO convocatoriaplaVerDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriaplaVerDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaPlaVerDTO convocatoriaplaVerDTO) throws ServiceException;

}
