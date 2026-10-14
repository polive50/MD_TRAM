package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaDoc
 *
 * @author everis
 */
public interface ConvocatoriaDocService
        extends BaseService<ConvocatoriaDoc, Integer, ConvocatoriaDocDAO> {

    /**
     * Buscar paginadamente Documentacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaDocDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda una documentacion.
     *
     * @param ConvocatoriaDocDTO the ConvocatoriaDocDTO
     * @return ConvocatoriaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocDTO guardar(final ConvocatoriaDocDTO convocatoriaDocDTO) throws ServiceException;

    /**
     * Buscar por id enlace.
     *
     * @param idDocumentacion the id Enlace
     * @return ConvocatoriaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocDTO buscarId(final Integer idDocumentacion) throws ServiceException;

    /**
     * Devuelve el documento por cdFica.
     *
     * @param cdFica the cdFica
     * @return the ConvocatoriaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocDTO descargar(Integer cdFica) throws ServiceException;

    /**
     * Obtiene la lista de documentos que tiene una convocatoria.
     *
     * @param idSolicitud the id solicitud
     * @para bFica true si accede a fica / false no accede
     * @return the list
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocDTO> obtenerDocumentosConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Subir documento fica.
     *
     * @param convocatoriaDocDTO the ConvocatoriaDocDTO
     * @return the ConvocatoriaDocDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocDTO subirDocumentoFica(ConvocatoriaDocDTO convocatoriaDocDTO)
            throws ServiceException;

    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaDocDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     * valida si el documento existe
     * 
     * @param idConvocatoria
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    Boolean existeTipoDocumento(Integer idConvocatoria, Integer idTipoDocumento)
            throws ServiceException;
}
