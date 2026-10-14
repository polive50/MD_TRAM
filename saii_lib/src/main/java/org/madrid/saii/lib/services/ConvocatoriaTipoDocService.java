package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaTip
 *
 * @author everis
 */
public interface ConvocatoriaTipoDocService
        extends BaseService<ConvocatoriaTipoDoc, Integer, ConvocatoriaTipoDocDAO> {

    /**
     * Buscar paginadamente ConvocatoriaTip.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaTipDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;
    
    /**
     * Buscar por id.
     *
     * @param idValor the id ConvocatoriaTipoDocDTO
     * @return ConvocatoriaTipoDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaTipoDocDTO buscarId(final Integer idConvocatoriTipoDoc) throws ServiceException;

    /**
     * Agregar un/a convocatoriaTip.
     *
     * @param idConvocatoria Id de convocatoria
     * @param idTipoDocumento Id de tipo de documento
     * @return convocatoriaTipDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaTipoDocDTO agregar(final Integer idConvocatoria, final Integer idTipoDocumento,
            final Integer idTramite) throws ServiceException;

    /**
     * Buscar por id convocatoria
     *
     * @param idConvocatoria Id convocatoria
     * @return List<ConvocatoriaTipDTO>
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> buscarPorConvocatoria(final Integer idConvocatoria)
            throws ServiceException;

    /**
     * Guarda una ConvocatoriaTipoDocDTO
     *
     * @param convocatoriaTipoDocDTO the convocatoria Tipo Doc DTO
     * @return convocatoriaTipoDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaTipoDocDTO guardar(final ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO)
            throws ServiceException;

    /**
     * Método que busca el tipo de documento por convocatoria
     * 
     * @param idConvocatoria
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaTipoDocumento(final Integer idConvocatoria,
            final Integer idTipoDocumento) throws ServiceException;
    /**
     * 
     * @param idConvocatoria
     * @param idTipoDocumentos
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaTiposDocumentos(final Integer idConvocatoria,
            final List<Integer> idTipoDocumentos) throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @return
     */
    List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaNoEnImpresos(final Integer idConvocatoria)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> buscarPorIdConvocatoria(final Integer idConvocatoria)
            throws ServiceException;
    
    /**
     * Buscar por id convocatoria
     *
     * @param idConvocatoria Id convocatoria
     * @return List<ConvocatoriaTipDTO>
     * @throws ServiceException
     */
    List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaNoFiltrado(final Integer idConvocatoria)
            throws ServiceException;

}
