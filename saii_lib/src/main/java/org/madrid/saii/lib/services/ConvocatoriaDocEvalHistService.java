package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaDocHist
 *
 * @author everis
 */
public interface ConvocatoriaDocEvalHistService
        extends BaseService<ConvocatoriaDocEvalHist, Integer, ConvocatoriaDocEvalHistDAO> {

    /**
     * Buscar paginadamente Documentacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaDocEvalHistDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda una documentacion.
     *
     * @param ConvocatoriaDocEvalHistDTO the ConvocatoriaDocEvalHistDTO
     * @return ConvocatoriaDocEvalHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalHistDTO guardar(final ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO) throws ServiceException;

    /**
     * Buscar por id enlace.
     *
     * @param idDocumentacion the id Enlace
     * @return ConvocatoriaDocEvalHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalHistDTO buscarId(final Integer idDocumentacion) throws ServiceException;

    /**
     * Devuelve el documento por cdFica.
     *
     * @param cdFica the cdFica
     * @return the ConvocatoriaDocEvalHistDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaDocEvalHistDTO descargar(Integer cdFica) throws ServiceException;

    /**
     * Obtiene la lista de documentos que tiene una convocatoria.
     *
     * @param idSolicitud the id solicitud
     * @para bFica true si accede a fica / false no accede
     * @return the list
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalHistDTO> obtenerDocumentosConvocatoriaEvalHist(ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO)
            throws ServiceException;

  
    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaDocEvalHistDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaDocEvalHistDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
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
