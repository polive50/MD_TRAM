package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaDocDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaPlaDoc
 *
 * @author quental
 */
public interface ConvocatoriaPlaDocService
        extends BaseService<ConvocatoriaPlaDoc, Integer, ConvocatoriaPlaDocDAO> {

    /**
     * Buscar todos los registros de ConvocatoriaPlaDoc.
     *
     * @return List<ConvocatoriaPlaDocDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaDocDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ConvocatoriaPlaDoc.
     *
     * @param idConvocatoriaPlaDoc the id ConvocatoriaPlaDoc
     * @return ConvocatoriaPlaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaDocDTO buscarId(Integer idConvocatoriaPlaDoc) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaPlaDoc.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaPlaDocDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaPlaDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaPlaDoc.
     *
     * @param convocatoriaPlaDocDTO the convocatoriaPlaDoc DTO
     * @return convocatoriaPlaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaPlaDocDTO guardar(ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO)
            throws ServiceException;

    /**
     * Borrado logico ConvocatoriaPlaDoc
     *
     * @param ConvocatoriaPlaDocDTO
     * @return ConvocatoriaPlaDocDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriaPlaDocDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO) throws ServiceException;

    List<ConvocatoriaPlaDocDTO> consultarDocuSubsanaPlantilla(Integer idConvocatoriaPla)
            throws ServiceException;

}
