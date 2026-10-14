package org.madrid.saii.lib.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.dao.ImpresoDAO;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Impreso
 *
 * @author everis
 */
public interface ImpresoService extends BaseService<Impreso, Integer, ImpresoDAO> {

    /**
     * Crea un informe
     *
     * @param impresoDTO
     * @return
     */
    byte[] crearInforme(ImpresoDTO impresoDTO, Map<String, String> mapaDatos, ExpedienteDTO dto)
            throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaActDTO.
     *
     * @param ImpresoDTO the ImpresoDTO
     * @return ImpresoDTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO guardar(final ImpresoDTO impresoDTO) throws ServiceException;

    /**
     * Buscar paginadamente los Impresos.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ImpresoDTO>
     * @throws ServiceException the service exception
     */
    List<ImpresoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

    /**
     * Busca el impreso de una convocatoria y tipo de actor
     *
     * @param idConvocatoria Id Convocatoria
     * @param idActor tipo de actor
     * @param idTipoFormulario
     * @return ImpresoDTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO buscarPorConvActor(final Integer idConvocatoria, final Integer idActor,
            final List<Integer> lstTipoImpreso) throws ServiceException;

    /**
     * Buscar por id impreso.
     *
     * @param idImpreso the id idImpreso
     * @return ConvocatoriaActDTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO buscarId(final Integer idImpreso) throws ServiceException;

    /**
     * Descargar.
     *
     * @param cdFica the cd fica
     * @return the documento DTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO descargar(Integer cdFica) throws ServiceException;

    /**
     * Subir documento fica.
     *
     * @param ImpresoDTO the ImpresoDTO
     * @return the ImpresoDTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO subirDocumentoFica(ImpresoDTO impresoDTO, Integer tipoDocImpreso)
            throws ServiceException;

    /**
     * Busca los impresos por id convocatoria que le pasamos por parametro.
     *
     * @param idActor the idActor
     * @return the ConvocatoriaDocDTO
     * @throws ServiceException the service exception
     */
    List<ImpresoDTO> buscarPorIdConvocatoria(Integer idConvocatoria) throws ServiceException;

    /**
     *
     * @param convImpDTO
     * @throws ServiceException
     */
    void eliminarDependenciaImpreso(ImpresoDTO convImpDTO) throws ServiceException;
    
    /**
     * Buscar formulario por convocatoria y actor
     * @param idConvocatoria
     * @param idActor
     * @return List<ImpresoDTO>
     * @throws ServiceException the service exception
     */
    List<ImpresoDTO> buscarFormulariosPorIdConvocatoriaIdActor(Integer idConvocatoria, Integer idActor) throws ServiceException;

    /**
    *
    */
    Boolean validarConvocatoriaActasGestion(Integer idConvocatoria) throws ServiceException;
    
    /**
    * Metodo que busca los impresos de tipo Justificación Cientifico tecnica Intermedia y Final de un actor
    * en una convocatoria
    */
    public List<ImpresoDTO> buscarPorConvActorJustiCT(final Integer idConvocatoria, final Integer idActor) 
            throws ServiceException;

    byte[] crearInformeContrato(ExpedienteDTO dto,ImpresoDTO impresoDTO, Map<String, String> mapaDatos, List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException;
    
    /**
     * Busca el impreso de una convocatoria y tipo de actor para actores con contrato
     *
     * @param idConvocatoria Id Convocatoria
     * @param idActor tipo de actor
     * @param idTipoFormulario
     * @return ImpresoDTO
     * @throws ServiceException the service exception
     */
    ImpresoDTO buscarPorConvActorContrato(final Integer idConvocatoria, final Integer idActor,
            final List<Integer> lstTipoImpreso) throws ServiceException;

    
    byte[] crearInformePresentacionContrato(ExpedienteDTO dto, ImpresoDTO impresoDTO,
            Map<String, String> mapaDatos, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException;

	/**
    *
    * @param idExpediente expediente
    */
    List<ImpresoDTO> buscarImpresosGenerarDatos(Integer idExpediente)
            throws ServiceException;

    List<Impreso> buscarPorExpedienteTipo(final Integer idExpediente, final List<Integer> lstTipoImpreso);

    byte[] crearInformeSubsanacionReq(ExpedienteDTO dto,ImpresoDTO impresoDTO, Map<String, String> mapaDatos, List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException;

    List<ImpresoDTO> buscarPorConvActorJustiEco(Integer idConvocatoria, Integer idActor)
            throws ServiceException;
    
    /**
     * 
     * @param idActor
     * @throws ServiceException
     */
    public void borradoLogico(ImpresoDTO impresoDTO) throws ServiceException ;

    byte[] crearInformeJusEco(ImpresoDTO impresoDTO, HashMap<String, String> hamCampos,
            ExpedienteDTO expedienteDTO, List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO) throws ServiceException;
    
    /**
     * Método para recuperar los formularios por convocatoria y por tipo de informe
     * @return
     * @throws ServiceException
     */
    public List<Impreso> buscarPorConvocatoriaTipoImpreso(Integer idConvocatoria,
            Integer idTipoImpreso, Integer idActor) throws ServiceException;
    
    /**
     * Método para recuperar las formularios por convocatoeria y tipo de informe
     * @param idTipoImpreso
     * @param isTipoImpreso
     * @return
     * @throws ServiceException
     */
    public List<ImpresoDTO> buscarPorConvTipoImpreso(Integer idConvocatoria,
            List<Integer> lstTipoImpreso) throws ServiceException;
    
}
