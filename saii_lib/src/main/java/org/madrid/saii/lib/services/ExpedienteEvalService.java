package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteEval
 *
 * @author nttdata
 */
public interface ExpedienteEvalService
        extends BaseService<ExpedienteEval, Integer, ExpedienteEvalDAO> {

    /**
     * Buscar por id Entidad.
     *
     * @param idEntidad the id Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    public List<ExpedienteEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar por id Entidad.
     *
     * @param idEntidad the id Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    ExpedienteEvalDTO buscarId(Integer idExpedienteEval) throws ServiceException;

    /**
     * Obtiene lista de coordinador asignado por idExpediente y itCoorEval.
     *
     * @param idExpediente el Expediente
     * @param itCoorEval el ExpedienteEval
     * @return List<EvaluacionDTO>
     * @throws ServiceException
     */
    List<ExpedienteEvalDTO> obtenerCoordinadorExpediente(Integer idExpediente, String itCoorEval)
            throws ServiceException;

    /**
     * Metodo dar alta del coordinado de expedienteEval
     *
     * @param expEvalCoor
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO altaCoordinadorExpediente(ExpedienteEvalDTO expEvalCoor) throws ServiceException;

    /**
     * Metodo para validar si existen más de un coordinador asociado al
     * expediente
     *
     * @param idExpedienteEval
     * @return
     * @throws ServiceException
     */
    Boolean existenVariosCoordinadorExpediente(Integer idExpediente) throws ServiceException;

    /**
     * Metodo para verificar si tiene permiso coordinador de dicho expediente
     *
     * @param idExpediente
     * @param cdUsuario
     * @return
     * @throws ServiceException
     */
    Boolean verificarPermisoCoordinadorExpediente(Integer idExpediente, String cdUsuario)
            throws ServiceException;

    /**
     * Obtiene lista de evaluadores asignado.
     *
     * @param idExpedienteEvalPadre
     * @return List<ExpedienteEvalDTO>
     * @throws ServiceException
     */
    List<ExpedienteEvalDTO> obtenerEvaluadoresAsignados(Integer idExpedienteEvalPadre)
            throws ServiceException;

    List<ExpedienteDocEvalDTO> obtenerListaDocEvalPorCdFicaInformeEvalDoc(String cdFica)
            throws ServiceException;

    List<CatalogosValoresDTO> obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador, Integer estadoCerradoEvaluador) throws ServiceException;

    List<CatalogosValoresDTO> obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(ExpedienteFiltrosDTO item, Integer estadoCerradoCoordinador)
            throws ServiceException;

    /**
     * Metodo para asignar evaluador a un expediente
     *
     * @param idExpediente del expediente seleccionado
     * @param idEvaluador para registrar
     * @param cdUsuario es el usuario logueado
     * @return ExpedienteEvalDTO
     * @throws ServiceException
     */
    ExpedienteEvalDTO asignarEvaluador(Integer idExpediente, Integer idEvaluador, String cdUsuario)
            throws ServiceException;

    /**
     * Metodo para desasociar un evaluador a un expediente.
     *
     * @param idExpediente del expediente seleccionado
     * @param idEvaluador para registrar
     * @param cdUsuario es el usuario logueado
     * @return ExpedienteEvalDTO
     * @throws ServiceException
     */
    ExpedienteEvalDTO desasociarEvaluador(Integer idExpediente, Integer idEvaluador,
            String cdUsuario) throws ServiceException;

    /**
     * método para recchazar el informe
     *
     * @param idExpedienteEval
     * @return
     * @throws ServiceException
     */
    public ExpedienteEvalDTO rechazarInformeCoordinador(Integer idExpedienteEval)
            throws ServiceException;

    /**
     * método para aprobar el informe
     *
     * @param idExpedienteEval
     * @return
     * @throws ServiceException
     */
    public ExpedienteEvalDTO aprobarInformeCoordinador(Integer idExpedienteEval)
            throws ServiceException;

    /**
     * método para consultar si todos los informes de coordinadores estan
     * cerrados
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    public Boolean esCerradoGestorTodos(Integer idExpediente) throws ServiceException;

    /**
     * método para consultar si todos los informes de coordinadores estan
     * cerrados
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    public Boolean esCerradoCoordinadorTodos(Integer idExpediente) throws ServiceException;

    /**
     * método para consultar si todos los informes de evaluadores estan en
     * cerrados
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    public Boolean esCerradoEvaluadorTodos(Integer idExpediente) throws ServiceException;

    /**
     * Obtiene lista de coordinador asignado por idExpediente y itCoorEval.
     *
     * @param idExpediente el Expediente
     * @param itCoorEval el ExpedienteEval
     * @return List<EvaluacionDTO>
     * @throws ServiceException
     */
    ExpedienteEvalDTO obtenerCoorEvalExpedienteEval(Integer idExpediente, String itCoorEval)
            throws ServiceException;

    /**
     * Obtiene la causa de denegación del registro expediente eval
     *
     * @param idExpediente el Expediente
     * @return String
     * @throws ServiceException
     */
    String obtenerCausaRechazoCoordinador(Integer idExpediente) throws ServiceException;

    /**
     * Metodo para desaprobar un informe de evaluador
     *
     * @param idExpediente
     * @param idEvaluador
     * @param cdNumIden
     * @return
     */
    public ExpedienteEvalDTO desaprobarInformeEvaluador(String cdNumIden, ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException;

    /**
     * Metodo para obtener el informe de evaluación de evaluador o coordinador
     *
     * @param idExpedienteEval
     * @param itCoorEval
     * @return
     * @throws ServiceException the service exception
     */
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            String itCoorEval) throws ServiceException;

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
    public ExpedienteEvalDTO finalizarInformeEvaluacion(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] docPdf) throws ServiceException;

    /**
     * Metodo para finalizar sin informe de evaluación
     *
     * @param expedienteEvalDTO
     * @param strIdentificador
     * @return
     * @throws ServiceException the service exception
     */
    public ExpedienteEvalDTO finalizarSinInformeEvaluacion(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador) throws ServiceException;
    
    /**
    *
    * @param idInteresado 
    * @return
    * @throws ServiceException
    */
    Boolean existePersonaExpedienteEval(Integer idInteresado, Integer idExpediente) throws ServiceException;

    /**
    *
    * @param idInteresado 
    * @return
    * @throws ServiceException
    */
    Boolean existePersonaExpedienteEvalComoCoordinador(Integer idInteresado, Integer idExpediente) throws ServiceException;

    public List<InformeEvaluacionDTO> obtenerInformeEvaluacionByInforme(Integer idExpedienteEval,
            InformeDTO informeDTO) throws ServiceException;

    ExpedienteEvalDTO finalizarInformeEvaluacionFirmado(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] documentoFirmado, byte[] documentoSinFirma)
            throws ServiceException;

    public void finalizarInformeEvaluadorFirmado(ExpedienteEvalDTO expedienteEvalDTO,
            String strIdentificador, byte[] docPdfFirmado, byte[] docPdfSinFirmado) throws ServiceException;

    public ExpedienteEvalDTO cerrarExpedienteEvalPorCoordinador(ExpedienteEvalDTO expEvalCoordinadorDTO,
            ExpedienteEvalDTO expEvalEvaluadorDTO, String strIdentificador) throws ServiceException;

    public ExpedienteEvalDTO obtenerExpedienteEvalByIdExpediente(Integer idExpediente,
            String string) throws ServiceException;

    /**
     *
     */
    InformeEvalDocDTO rechazarInformeCoordinador(InformeEvalDocDTO informe) throws ServiceException;

    /**
    *
    */
    InformeEvalDocDTO aprobarInformeCoordinador(InformeEvalDocDTO informe) throws ServiceException;

    ExpedienteEvalDTO rechazarInformeEvaluador(Integer idExpedienteEval, Integer idInforme) throws ServiceException;
}
