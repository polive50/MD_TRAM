package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteEvalJusTecDAO;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteEvalJusTec
 *
 * @author quental
 */
public interface ExpedienteEvalJusTecService
extends BaseService<ExpedienteEvalJusTec, Integer, ExpedienteEvalJusTecDAO> {
    
    /**
     * busca el expedienteEvalJusTec
     *
     * @param expedienteEvalJusTec
     * @return ExpedienteDocDTO
     */
    List<ExpedienteEvalJusTecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec) throws ServiceException;
    
    /**
     * busca el todos expedienteEvalJusTec
     *
     * @param expedienteEvalJusTec
     * @return ExpedienteDocDTO
     */
    List<ExpedienteEvalJusTecDTO> buscarAll(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec) throws ServiceException;
    
    
    /**
     * Guarda el expedienteEvalJusTec
     *
     * @param expedienteEvalJusTec
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecDTO guardar(ExpedienteEvalJusTecDTO expedienteEvalJusTec) throws ServiceException;
    
    /**
     * Actualiza el expediente
     *
     * @param expedienteEvalJusTec
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecDTO actualizar(ExpedienteEvalJusTecDTO expedienteEvalJusTec) throws ServiceException;
    
    
    /**
     * Borrado logico RepresentanteJur
     *
     * @param ExpedienteEvalJusTecDTO
     * @return ExpedienteEvalJusTecDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecDTO borradoLogico(ExpedienteEvalJusTecDTO expedienteEvalJusTec)
            throws ServiceException;
    
    /**
     * 
     */
    List<ExpedienteEvalJusTecDTO> buscarEvaluadoresJCT(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJusTecDTO)
            throws ServiceException;
    
    /**
     * 
     */
    List<ExpedienteEvalJusTecDTO> retornarPaginado(
            List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJusTec, int index, int pageSize);

    /**
     * @throws ServiceException 
     * 
     */
    List<ExpedienteEvalJusTecDTO> buscarByIdExpJustec(List<ExpedienteJusTecDTO> listExpedienteJusTecDTO) throws ServiceException;

    /**
     * 
     */
    ExpedienteEvalJusTecDTO buscarByIdExpedienteJustec(Integer idExpedienteJusTec) throws ServiceException;
    
    /**
     * Guarda un/a ExpedienteEvalJusTec.
     *
     * @param expedienteEvalJusTecDTO the expedienteEvalJusTec DTO
     * @return expedienteEvalJusTecDTO
     * @throws ServiceException the service exception
     */
    ExpedienteEvalJusTecDTO guardarExpEvalJusTecCt(ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO)
            throws ServiceException;
    
    /**
    *
    * @param idInteresado
     * @param nmPerido 
    * @return
    * @throws ServiceException
    */
    Boolean existePersonaExpedienteEvalJusTec(Integer idInteresado) throws ServiceException;
    
    /**
     * Guarda el expedienteEvalJusTec
     *
     * @param expedienteEvalJusTec
     * @return ExpedienteDoc
     * @throws ServiceException
     */
    ExpedienteEvalJusTec guardarCambiosRegistro(ExpedienteEvalJusTec registroEditadoGrabar) throws ServiceException;
    
    /**
     * Buscar paginadamente ConvocatoriaEvalJustec.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @param nmPeriodoAux 
     * @param idExpedienteJusTec2 
     * @return List<ExpedienteEvalJusTecDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteEvalJusTecDTO> buscarEvaluadoresPorPeriodo(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec) throws ServiceException;

    
    /**
    *
    * @param idInteresado
    * @param listPeriodosExpdienteEval 
    * @return
    * @throws ServiceException
    */
    Boolean existePersonaExpedienteEvalJusTecPorExpedienteJustec(Integer idInteresado, List<ExpedienteJusTecDTO> listPeriodosExpdienteEval) throws ServiceException;
    
    /**
     * 
     */
    ExpedienteEvalJusTecDTO buscarId(Integer idExpedienteEvalJusTec) throws ServiceException;
    
    /**
     * Metodo para obtener el informe de evaluación de evaluador o coordinador
     *
     * @param idExpedienteEval
     * @param itCoorEval
     * @return
     * @throws ServiceException the service exception
     */
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacionJustec(Integer idExpedienteEvalJustec) throws ServiceException;

    /**
     * Metodo para obtener los apartados, comentarios y notas
     *
     * @param idExpedienteEval
     * @param idEvaluacion
     * @return
     * @throws ServiceException the service exception
     */
    public List<InfApartadoDatoDTO> obtenerApartadosDatos(Integer idExpedienteEval,
            Integer idEvaluacion) throws ServiceException;

    /**
     * Metodo para finalizar informe de evaluación
     *
     * @param expedienteEvalDTO
     * @param docPdf
     * @param strIdentificador
     * @return
     * @throws ServiceException the service exception
     */
    public ExpedienteEvalJusTecDTO finalizarInformeEvaluacion(ExpedienteEvalJusTecDTO expedienteEvalDTO,
            String strIdentificador, byte[] docPdf) throws ServiceException;

    /**
     * Metodo para finalizar sin informe de evaluación
     *
     * @param expedienteEvalDTO
     * @param strIdentificador
     * @return
     * @throws ServiceException the service exception
     */
    public ExpedienteEvalJusTecDTO finalizarSinInformeEvaluacion(ExpedienteEvalJusTecDTO expedienteEvalDTO,
            String strIdentificador) throws ServiceException;


    public String obtenerCausaRechazoCoordinador(Integer idExpedienteEvalJustec) throws ServiceException;
    
    /**
     * 
     * @param idExpedienteEvalJustec
     * @return
     * @throws ServiceException
     */
    public List<ExpedienteEvalJusTecDTO> obtenerListaExpedienteEvalJustec(Integer idExpedienteEvalJustec) throws ServiceException;
    
    
    public List<ExpedienteEvalJusTec> expedienteEvalJusTecXExpedienteJusTec(Integer idExpedienteJusTec) throws ServiceException;
    
    /**
     *   Método para buscar ExpedienteEvalJusTecDTO en estado evaluador asignado
     *   
     *   @param idExpedienteJustec id del expediente JCT
     */
    List<ExpedienteEvalJusTecDTO> buscarExpedientesEvalAsignadoJustec(Integer idExpedienteJusTec) throws ServiceException;

    /**
     * Actualiza desde el batch el estado de los expdtes eval justec segun sus estados previos y fechas de JCT
     * para iniciar plazo evaluacion
     * 
     * @return
     * @throws ServiceException
     */
    void iniciarPlazoEvaluacionJCT() throws ServiceException;

    /**
     * Actualiza desde el batch el estado de los expdtes eval justec segun sus estados previos y fechas de JCT
     * para finalizar plazo evaluacion
     * 
     * @return
     * @throws ServiceException
     */
    void finalizarPlazoEvaluacionJCT() throws ServiceException;

    /**
     *   Método para buscar ExpedienteEvalJusTecDTO en estado pdte evaluacion
     */
    List<ExpedienteEvalJusTecDTO> buscarExpedientesPendientesEvalJustec() throws ServiceException;

    List<ExpedienteEvalJusTecDTO> buscarPeriodosPendienteEvalJustec(Integer idExpediente,
            Boolean pendienteEval) throws ServiceException;
    
    public String guardarFicaFichero(ExpedienteEvalJusTec expedienteEval, byte[] documento,
            String strIdentificador) throws ServiceException;

    /**
      *   Método para buscar ExpedienteEvalJusTecDTO en estado evaluador asignado pasado de fecha
      *   
      *   @param idExpedienteJustec id del expediente JCT
      */
    List<ExpedienteEvalJusTecDTO> buscarExpedientesEvalAsignadoPasadoJustec(
            Integer idExpedienteJustec) throws ServiceException;
    
    

}