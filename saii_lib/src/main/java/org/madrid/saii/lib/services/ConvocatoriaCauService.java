package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaCauDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCau
 *
 * @author everis
 */
public interface ConvocatoriaCauService
        extends BaseService<ConvocatoriaCau, Integer, ConvocatoriaCauDAO> {

    /**
     * Buscar por id ConvocatoriaCau.
     *
     * @param idConvocatoriaCau the id ConvocatoriaCau
     * @return ConvocatoriaCauDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauDTO buscarId(Integer idConvocatoriaCau) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCau.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaCauDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCauDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Agregar un/a convocatoriaCau.
     *
     * @param idConvocatoria Id Convocatoria
     * @param IdCausa Id Causa de denegación
     * @return convocatoriaCauDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauDTO agregar(Integer idConvocatoria, Integer idCausa) throws ServiceException;

    /**
     * Borrado logico ConvocatoriaCau
     *
     * @param ConvocatoriaCauDTO
     * @return ConvocatoriaCauDTO
     * @throws ServiceException
     */
    ConvocatoriaCauDTO borradoLogico(final ConvocatoriaCauDTO convocatoriaCauDTO)
            throws ServiceException;

    ConvocatoriaCauDTO actualizarNormativa(Integer idConvocatoria, Integer idCausa,
            String tlNormativa) throws ServiceException;
    
    ConvocatoriaCauDTO buscarIdConvocatoriaCauDTO(Integer idConvocatoria, Integer idCausa)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param idCausa
     * @param idTipoCausa
     * @return
     * @throws ServiceException
     */
    ConvocatoriaCauDTO agregar(Integer idConvocatoria, Integer idCausa, Integer idTipoCausa)
            throws ServiceException;

}
