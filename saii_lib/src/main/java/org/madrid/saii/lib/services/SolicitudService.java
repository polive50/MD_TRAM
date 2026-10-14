package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SolicitudFiltrosDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Solicitud
 *
 * @author everis
 */
public interface SolicitudService extends BaseService<Solicitud, Integer, SolicitudDAO> {

    /**
     * Buscar por id Solicitud.
     *
     * @param idSolicitud the id solicitud
     * @return SolicitudDTO
     * @throws ServiceException the service exception
     */
    SolicitudDTO buscarId(final Integer idSolicitud) throws ServiceException;

    /**
     * Busca una solicitud según sus filtros.
     *
     * @param solicitudFiltros
     * @return List<SolicitudDTO>
     * @throws ServiceException the service exception
     */
    List<SolicitudDTO> filtrar(SolicitudFiltrosDTO solicitudFiltros) throws ServiceException;

    /**
     * Crea una Solicitud.
     *
     * @param SolicitudDTO the Solicitud DTO
     * @param interesadoFisico the Interesado DTO
     * @return SolicitudDTO
     * @throws ServiceException the service exception
     */
    SolicitudDTO crearSolicitud(final SolicitudDTO solicitudDTO,
            final InteresadoDTO interesadoFisico) throws ServiceException;

    /**
     * Presenta la solicitud. Genera el pdf para registro.
     *
     * @param solicitudDTO the solicitud DTO
     * @return RegistroEregDTO
     * @throws ServiceException the service exception
     */
    RegistroEregDTO presentar(final SolicitudDTO solicitudDTO) throws ServiceException;

    /**
     * Obtiene el catálogo de valores a partir del tipo de persona
     *
     * @param String tipo de persona
     * @return List<CatalogosValoresDTO> lista de catálogo valores
     */
    List<CatalogosValoresDTO> obtenerListaValoresActor(String tipoPersona) throws ServiceException;

    /**
     * Obtiene los valores de área científica
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     */
    List<CatalogosValoresDTO> obtenerListaAreasCientificas() throws ServiceException;

    /**
     * Obtiene los estados de las solicitudes
     *
     * @return List<CatalogosValoresDTO> lista de catálogo de valores
     */
    List<CatalogosValoresDTO> obtenerListaEstadosSolicitud() throws ServiceException;

    /**
     * Busca y devuelve una solicitud que coincida con los filtros del dto
     *
     * @param SolicitudDTO solicitudDTO
     * @return SolicitudDTO solicitud encontrada
     */
    SolicitudDTO buscarSolicitudIniciada(SolicitudDTO solicitudDTO) throws ServiceException;

    /**
     * Busca soliditud por idExpediente
     *
     * @param Integer idExpediente
     * @return SolicitudDTO solicitud encontrada
     */
    SolicitudDTO buscarPorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    public List<SolicitudDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar Solicitudes para Tramitacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    List<SolicitudDTO> buscarSolicitudesTramitacion(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            final Integer idConvocatoria, final Boolean mostrarIniciadas, Integer nmDiasSubsanacion)
            throws ServiceException;

    /**
     * método para cambiar el estado de la solicitud
     *
     * @param IdSolicitud
     * @param idEstadoSolictud
     * @return
     * @throws ServiceException
     */
    SolicitudDTO cambiarEstado(Integer idSolicitud, Integer idEstadoSolictud)
            throws ServiceException;
    
    /**
     * Busca las solicitudes por id convocatoria que le pasamos por parametro.
     *
     * @param idConvocatoria the idConvocatoria
     * @throws ServiceException the service exception
     */
    List<SolicitudDTO> buscarPorIdConvocatoria(Integer idConvocatoria) throws ServiceException;

    SolicitudDTO buscarSolicitudOrganismoIniciada(SolicitudDTO solicitudDTO)
            throws ServiceException;

    SolicitudDTO guardarSolicitud(SolicitudDTO solicitudDTO) throws ServiceException;

}
