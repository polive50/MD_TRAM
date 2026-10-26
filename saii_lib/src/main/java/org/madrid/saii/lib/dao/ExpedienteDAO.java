package org.madrid.saii.lib.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ResolucionConcesionDTO;
import org.madrid.saii.lib.dto.ResolucionExclusionDesistimientoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio Expediente
 *
 * @author everis
 */
public interface ExpedienteDAO extends BaseDAO<Expediente, Integer> {

    /**
     * Realiza un filtrado de espediente
     *
     * @param expedienteFiltrosDTO
     * @return List<ExpedienteFiltrosDTO>
     * @throws ServiceException 
     */
    List<ExpedienteFiltrosDTO> filtrar(ExpedienteFiltrosDTO dto) ;

    /**
     * Realiza un filtrado de expediente para un coordinador / evaluador
     *
     * @param expedienteFiltrosDTO
     * @return List<ExpedienteFiltrosDTO>
     */
    List<ExpedienteFiltrosDTO> obtenerExpedienteCoorEval(ExpedienteFiltrosDTO dto);

    /**
     * Obtiene una lista de expedientes por convocatoria
     *
     * @param idConvocatoria
     * @return List<Expediente>
     */
    List<Expediente> listaExpedientePorConvocatoria(Integer idConvocatoria);

    /**
     * Obtiene los ids de los expedientes de una convocatoria cuyo estado esté entre los indicados.
     * Consulta ligera (solo trae el idExpediente) pensada para comprobaciones de existencia,
     * evitando cargar el listado completo de expedientes con toda su información.
     *
     * @param idConvocatoria
     * @param lstEstados lista de idValor de CatalogosValores de los estados a comprobar
     * @return List<Integer> ids de expediente encontrados (vacía si ninguno cumple el filtro)
     */
    List<Integer> listaExpConcedidosPorConvocatoria(Integer idConvocatoria, List<Integer> lstEstados);

    /**
     * Obtiene los ids de los expedientes de una convocatoria que tengan alegaciones presentadas
     * (itAlegaciones = 'S'). Consulta ligera (solo trae el idExpediente) pensada para
     * comprobaciones de existencia, evitando cargar el listado completo de expedientes.
     *
     * @param idConvocatoria
     * @return List<Integer> ids de expediente encontrados (vacía si ninguno cumple el filtro)
     */
    List<Integer> listaExpConAlegacionPorConvocatoria(Integer idConvocatoria);

    /**
     * método para comprobar que todos los expedientes hijos estén en un estado
     * terminado
     *
     * @param expedienteDTO
     * @return
     * @throws ServiceException 
     */
    Boolean validarExpedienteSolicitanteHijoEstadoFinal(ExpedienteDTO expedienteDTO);

    /**
     * Método para calcular la cantidad de expedientes pendientes por solicitud
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @throws ServiceException
     */
    Integer alertaGestorSolicitud(Integer idSolicitud, Boolean solicitudEnTramitacion);

    /**
     *
     * @param idExpediente
     * @return
     * @throws ServiceException 
     */
    Map<String, Object> obtenerMapaDatosExcel(Integer idExpediente) ;

    /**
     *
     * @param lstIdExpediente
     * @return
     * @throws ServiceException 
     */
    Map<String, String> obtenerMapaDatosComunExcel(List<Integer> lstIdExpediente);

    /**
     *
     * @param lstIdExpediente
     * @return
     */
    Map<String, String> obtenerMapaDatosActuacionFinExcel(List<Integer> lstIdExpediente);

    /**
     *
     * @param lstIdExpediente
     * @return
     */
    int obtenerCantidadColumnasReservadaActuacion(List<Integer> lstIdExpediente);

    /**
     *
     * @param listaIdExpediente
     * @param idSolicitud
     * @param idExpediente
     * @param solicitudEnTramitacion
     * @return
     * @throws ServiceException 
     */
    int alertaFinanciacion(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion) ;

    /**
     * @throws ServiceException 
    *
    */
    List<Integer> listaExpedienteConContrato() ;

    /**
    *
    */
    List<ExpedienteDTO> listExpedientePorExpediente(Integer idExpediente);

    /**
     * @param idExpediente
     * @return
     */
    List<ExpedienteContratoDTO> findAllExpContract(Integer idExpediente);

    /**
     * @param idExpediente
     * @return
     */
    ExpedienteContrato findExpContratById(Integer idExpediente);

    /**
     * @param idExpedientePadre
     * @return
     * @throws ServiceException 
     */
    List<Expediente> listExpedientePorExpedientePadre(Integer idSolicitud,
            Integer idExpedientePadre);

    public List<Expediente[]> listExpedientesContratoPorPadre(Integer idExpedientePadre);

    /**
     * @param idSolicitud
     * @return
     */
    ExpedienteDTO buscarIdSolicitud(Integer idSolicitud);

    /**
     * @param idSolicitud
     * @return
     */
    List<Expediente> recuperarExpedientesByIdSolicitud(Integer idSolicitud);

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @param solicitudEnTramitacion
     * @throws ServiceException 
     */
    int alertaModifPlazosAnualidades(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion);

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @param solicitudEnTramitacion
     * @throws ServiceException 
     */
    int alertaModifPesupFinanciable(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion);

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @throws ServiceException 
     */
    int alertaOtrasModificaciones(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion);

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @param solicitudEnTramitacion
     * @throws ServiceException 
     */
    Integer alertaJCT(List<Integer> listaIdExpediente, Integer idSolicitud, Integer idExpediente, Boolean solicitudEnTramitacion);

    /**
     *
     * @param idSolicitud
     * @param listaIdExpediente
     * @param solicitudEnTramitacion
     * @throws ServiceException 
     */
    Integer alertaContratos(List<Integer> listaIdExpediente, Integer idSolicitud,
            Integer idExpediente, Boolean solicitudEnTramitacion);

    List<ExpedienteDoc> findExpJusTecDocByIdExp(Integer idExpediente);

    List<ExpedienteDoc> findExpContratosDocByIdExp(Integer idExpediente) ;

    List<ExpedienteDoc> findExpModifDocByIdExp(Integer idExpediente) ;

    /**
     * @param idExpedientePadre
     * @return
     */
    List<Expediente> listExpedientesHijoPorExpedientePadre(Integer idExpedientePadre);
    
    /**
     * Obtiene los expedientes de una convocatoria que se encuentran en los
     * estados indicados, junto con los requisitos que no cumplen.
     *
     * @param idConvocatoria identificador de la convocatoria
     * @param idsEstado identificadores de los estados del expediente
     * @param idCatalogoRequisitos identificador del catálogo de requisitos
     * @return expedientes de la resolución de exclusión o desistimiento
     */
    List<ResolucionExclusionDesistimientoDTO> listaResolucionExclusionDesistimiento(
            Integer idConvocatoria, List<Integer> idsEstado, Integer idCatalogoRequisitos);
    
    /**
     * Obtiene la fecha de presentacion del expediente en base a la solicitud indicada
     * @param idSolicitud
     * @param idExpediente 
     * @return
     */
    Date obtenerFcPresentacionBySolicitud(Integer idSolicitud, Integer idExpediente);

    /**
    *
    * @param idSolicitud
    * @param listaIdExpediente
    * @param solicitudEnTramitacion
    * @ 
    */
    Integer alertaJEco(List<Integer> listaIdExpediente, Integer idSolicitud, Integer idExpediente, Boolean solicitudEnTramitacion);

    List<ExpedienteDoc> findExpJusEcoDocByIdExp(Integer idExpediente);

    Map<Integer, Long> contarExpedientesPendientesPorConvocatoria(List<Integer> idsPagina);

    Map<Integer, Long> contarExpedientesPendientesPorSolicitud(List<Integer> idsSolicitudesPantalla);

    Map<Integer, Date> obtenerFcPresentacionPorLote(List<Integer> idsExpediente);

    Map<Integer, Object[]> obtenerEstadosExpedienteSolicitantePorLote(List<Integer> idsSolicitud);
    
    /**
     * Obtiene los expedientes concedidos definitivos aceptados de la convocatoria.
     *
     * @param idConvocatoria identificador de la convocatoria
     * @param idEstado identificador del estado concedido definitivo aceptado
     * @return expedientes de la resolución de concesión
     */
    List<ResolucionConcesionDTO> listaResolucionConcesion(
            Integer idConvocatoria, Integer idEstado);
    
    /**
     * Obtiene los estados provisionales presentes en los expedientes activos de la convocatoria.
     *
     * @param idConvocatoria identificador de la convocatoria
     * @param idsEstado identificadores de los estados provisionales
     * @return estados presentes, sin duplicados
     */
    List<Integer> obtenerEstadosProvisionalesResolucion(Integer idConvocatoria,
            List<Integer> idsEstado);

}
