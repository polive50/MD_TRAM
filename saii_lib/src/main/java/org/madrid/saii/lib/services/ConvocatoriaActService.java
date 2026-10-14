package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaActDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaAct
 *
 * @author everis
 */
public interface ConvocatoriaActService
        extends BaseService<ConvocatoriaAct, Integer, ConvocatoriaActDAO> {

    /**
     * Buscar paginadamente las ConvocatoriaAct.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<CatalogosDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaActDTO.
     *
     * @param ConvocatoriaActDTO the ConvocatoriaActDTO
     * @return ConvocatoriaActDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaActDTO guardar(final ConvocatoriaActDTO convocatoriaActDTO) throws ServiceException;

    /**
     * Borra de forma logica un/a ConvocatoriaActDTO.
     *
     * Si es actor es solicitante tambien borra su impreso.
     *
     * @param idActor the idActor
     * @return ConvocatoriaActDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaActDTO borrarActor(final Integer idActor) throws ServiceException;

    /**
     * Buscar por el tipo de persona para una convocatoria en concreto
     *
     * @param tipoPersona
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaActDTO> buscarPorTipoPersona(Integer tipoPersona, Integer idConvocatoria)
            throws ServiceException;

    /**
     * Buscar por id actor.
     *
     * @param idActor the id idActor
     * @return ConvocatoriaActDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaActDTO buscarId(final Integer idActor) throws ServiceException;

    /**
     * Buscar por id convocatoria.
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Buscar por id convocatoria los tipos de Actores Participantes
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> buscarTipoActorParticipante(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Buscar por id convocatoria los tipos de Actores Solicitantes
     *
     * @param idConvocatoria the idConvocatoria
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> buscarTipoActorSolicitante(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Obtiene los actores participantes para el actor solicitante
     *
     * @param idActor
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> obtenerParticipantesSolicitante(Integer idActor)
            throws ServiceException;

    /**
     * Obtiene los actores participantes para el actor solicitante
     *
     * @param idActor
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> obtenerParticipantesCoordinador(Integer idActor)
            throws ServiceException;

    /**
     * Mediante el id de la convocatoria, se obtiene los actores del tipo
     * coordinador
     *
     * @param idConvocatoria
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException
     */
    List<ConvocatoriaActDTO> buscarTipoActorCoordinador(Integer idConvocatoria)
            throws ServiceException;

    /**
     * Verifica si existe al menos un actor con el indicador de contrato
     * habilitado
     *
     * @param idConvocatoria
     * @return Boolean
     * @throws ServiceException
     */
    Boolean validarConvocatoriaContratoActor(Integer idConvocatoria) throws ServiceException;

    /**
     * Recuperar los coordinadores asociados al solicitante
     *
     * @param idActor
     * @return List<ConvocatoriaActDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaActDTO> obtenerCoordinadores(Integer idActor) throws ServiceException;

    List<ConvocatoriaActDTO> buscarHijosConContrato(Integer idConvocatoria, Integer idActor, String tipoActorPadre) throws ServiceException;
    
    /**
     * Buscar paginadamente las ConvocatoriaAct.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<Catalogos>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaAct> buscarDatosActoresCorreo(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    
    List<ConvocatoriaAct> retornarPaginado(List<ConvocatoriaAct> listaConvocatoriaAct, int index,
            int pageSize);
    
    /**
     * Permite obtener los actores por id de actor
     *
     * @param idActor Identificador del actor
     * @return Lista de actores 
     * @throws ServiceException Excepción ocurrida en el servicio
     */
    List<ConvocatoriaActDTO> obtenerActoresPorIdActor(Integer idActor)
            throws ServiceException;
    
    /**
     * 
     * @param convocatoriaActDTO
     * @return
     * @throws ServiceException
     */
    List<ConvocatoriaActDTO> obtenerActoresAsociados(ConvocatoriaActDTO convocatoriaActDTO)
            throws ServiceException;
    
}
