package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaDoc
 *
 * @author everis
 */
public interface ConvocatoriaDocEvalService
        extends BaseService<ConvocatoriaDocEval, Integer, ConvocatoriaDocEvalDAO> {

    /**
     * Buscar paginadamente Documentacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaDocEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda una documentacion.
     *
     * @param ConvocatoriaDocEvalDTO the ConvocatoriaDocEvalDTO
     * @return ConvocatoriaDocEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalDTO guardar(final ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException;

    /**
     * Buscar por id enlace.
     *
     * @param idDocumentacion the id Enlace
     * @return ConvocatoriaDocEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalDTO buscarId(final Integer idDocumentacion) throws ServiceException;

    /**
     * Devuelve el documento por cdFica.
     *
     * @param cdFica the cdFica
     * @return the ConvocatoriaDocEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalDTO descargar(Integer cdFica) throws ServiceException;

    /**
     * Obtiene la lista de documentos que tiene una convocatoria.
     *
     * @param idSolicitud the id solicitud
     * @para bFica true si accede a fica / false no accede
     * @return the list
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalDTO> obtenerDocumentosConvocatoriaEval(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException;

    /**
     * Subir documento fica.
     *
     * @param convocatoriaDocEvalDTO the ConvocatoriaDocEvalDTO
     * @return the ConvocatoriaDocEvalDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalDTO subirDocumentoFica(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException;

    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaDocEvalDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
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
    
    void guardarConvocatoriaDocEvalHist(final ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException;
}
