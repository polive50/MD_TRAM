package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Interesado
 *
 * @author everis
 */
public interface InteresadoService extends BaseService<Interesado, Integer, InteresadoDAO> {

    /**
     * Buscar por id Interesado.
     *
     * @param idInteresado the id interesado
     * @return InteresadoDTO
     * @throws ServiceException the service exception
     */
    InteresadoDTO buscarId(final Integer idInteresado) throws ServiceException;

    /**
     * Buscar paginadamente Interesado.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<InteresadoDTO>
     * @throws ServiceException the service exception
     */
    List<InteresadoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar paginadamente Interesado de la persona.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<InteresadoDTO>
     * @throws ServiceException the service exception
     */

    List<InteresadoDTO> buscarPersona(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea)
            throws ServiceException;

    /**
     * Busca un interesado a partir de su cdNumIden
     *
     * @param cdNumIden the cd num iden
     * @return the interesado DTO
     * @throws ServiceException the service exception
     */
    InteresadoDTO obtenerPorIdentificacion(String cdNumIden) throws ServiceException;

    /**
     * Busca un interesado a partir de su cdUsuario
     *
     * @param cdUsuario
     * @return the interesado DTO
     * @throws ServiceException the service exception
     */
    InteresadoDTO obtenerPorCdUsuario(String cdUsuario) throws ServiceException;

    /**
     * Metodo para obtener los datos de la persona por identificador y/o codigo
     * de usuario
     *
     * @param identificador
     * @param cdUsuario
     * @return
     * @throws ServiceException
     */
    InteresadoDTO obtenerPorIdentificadorCdUsuario(String identificador, String cdUsuario)
            throws ServiceException;

    /**
     * Busca la lista de tipos de identificación
     *
     * @return List<CatalogosValoresDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogosValoresDTO> obtenerTiposIdentificacion() throws ServiceException;

    /**
     * Metodo que se encarga de borrar del interesadoDTO.
     *
     * @param interesadoDTO the interesado DTO
     * @throws ServiceException the service exception
     */
    void borradoLogico(final InteresadoDTO interesadoDTO) throws ServiceException;

    /**
     * Guarda un Interesado de la persona.
     *
     * @param interesadoDTO the interesado DTO
     * @return InteresadoDTO
     * @throws ServiceException the service exception
     */
    InteresadoDTO guardarPersona(final InteresadoDTO interesadoDTO, final String cdUsuario,
            Boolean grabarDatosAcademicos) throws ServiceException;

    /**
     * Método para verificar si el interesado es coordinador o evaluador,
     * verificando si el campo itCoorEval es "S"
     *
     * @param cdNumIden Identificador del interesado
     * @return Devuelve TRUE si es coordinado/evaluador o FALSE si no lo es
     * @throws ServiceException
     */
    Boolean verificarInteresadoCoorEval(String cdNumIden) throws ServiceException;

    /**
     * Metodo para dar de baja una persona coordinador/evaluador.
     *
     * @param interesadoDTO the interesado DTO
     * @return InteresadoDTO
     * @throws ServiceException the service exception
     */
    InteresadoDTO darBajaPersonaCoor(InteresadoDTO interesadoDTO) throws ServiceException;

    /**
     * Obtiene lista de evaluadores con excepcion del usuario que se loguea.
     *
     * @param identificadorCoordinador
     * @param cdUsuario
     * @param itCoorEval
     * @return List<InteresadoDTO>
     * @throws ServiceException
     */
    List<InteresadoDTO> obtenerEvaluadores(String identificadorCoordinador, String cdUsuario)
            throws ServiceException;

    /**
     * Obtiene lista de evaluadores por coordinador.
     *
     * @param identificadorCoordinador
     * @return List<InteresadoDTO>
     * @throws ServiceException
     */

    List<InteresadoDTO> obtenerEvaluadoresxCoordinador(String identificadorCoordinador,
            Integer idConvocatoria, String cdExpediente) throws ServiceException;

    List<InteresadoDTO> retornarPaginado(List<InteresadoDTO> listaInteresadosFiltrada, int index,
            int pageSize);

    /**
     * Buscar paginadamente Evaluadores de la justificacion cientifico técnica.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<InteresadoDTO>
     * @throws ServiceException the service exception
     */
    List<InteresadoDTO> obtenerEvaluadoresJusTec(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden, int idExpediente) throws ServiceException;
    
    
    List<Interesado> obtenerInteresadosByCdNumIdem(String cdNumIden) throws ServiceException;

    List<CatalogosValoresDTO> obtenerMedioNotificaciones() throws ServiceException;

    List<CatalogosValoresDTO> obtenerPersonaSolicitante() throws ServiceException;

    List<CatalogosValoresDTO> obtenerTiposActor() throws ServiceException;

    List<InteresadoDTO> obtenerInteresados(String cdNumIden) throws ServiceException;
    
    List<InteresadoDTO> buscarPersonaNormalizado(String nombre, String apellido1, String apellido2,
            String cdNumIden, String cdUsuario, Integer idArea) throws ServiceException;
}
