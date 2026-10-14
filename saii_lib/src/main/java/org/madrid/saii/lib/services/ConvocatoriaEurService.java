package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEurDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.dto.ConvocatoriaEurDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaEur
 *
 * @author everis
 */
public interface ConvocatoriaEurService
        extends BaseService<ConvocatoriaEur, Integer, ConvocatoriaEurDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaEur.
     *
     * @return List<ConvocatoriaEurDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEurDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaEur.
     *
     * @param idConvocatoriaEur the id ConvocatoriaEur
     * @return ConvocatoriaEurDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEurDTO buscarId(Integer idConvocatoriaEur) throws ServiceException;

    /**
     * Buscar por idCatalogoValores
     */
    public ConvocatoriaEurDTO buscarIdCatalogoValores(Integer idCatalogoValor,
            Integer idConvocatoria) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaEur.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaEurDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaEurDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar por idCatalogoValores
     */
    public List<ConvocatoriaEurDTO> buscarIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Guarda un/a convocatoriaEur.
     *
     * @param convocatoriaEurDTO the convocatoriaEur DTO
     * @return convocatoriaEurDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaEurDTO guardar(ConvocatoriaEurDTO convocatoriaEurDTO) throws ServiceException;

    /**
     * Borrado logico ConvocatoriaEur
     *
     * @param convocatoriaEurDTO
     * @return ConvocatoriaEurDTO
     * @throws ServiceException
     */
    ConvocatoriaEurDTO borradoLogico(final ConvocatoriaEurDTO convocatoriaEurDTO)
            throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriaeurDTO
     * @return
     * @throws ServiceException
     */
    ConvocatoriaEurDTO altaLogica(final ConvocatoriaEurDTO convocatoriaeurDTO)
            throws ServiceException;

}
