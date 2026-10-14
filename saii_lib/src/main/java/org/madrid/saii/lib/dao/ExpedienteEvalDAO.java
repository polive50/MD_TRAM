package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteEval
 *
 * @author nttdata
 */
public interface ExpedienteEvalDAO extends BaseDAO<ExpedienteEval, Integer> {

    List<ExpedienteEval> obtenerCoordinadorPorEvaluador(Integer idExpediente, Integer idInteresadoEvaluador, Integer idTipoInforme);

    /**
     * Metodo para obtener el informe de evaluación de evaluador o coordinador
     *
     * @param idExpedienteEval
     * @param itCoorEval
     * @return
     */
    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            String itCoorEval);

    /**
     * Metodo para obtener los apartados, comentarios y notas
     *
     * @param idExpedienteEval
     * @param idEvaluacion
     * @return
     */
    public List<InfApartadoDatoDTO> obtenerApartadosDatos(Integer idExpedienteEval,
            Integer idEvaluacion);

    /**
     * Metodo para obtener la nota media de los informes de coordinador del
     * expediente
     *
     * @param idExpediente
     * @return
     * @ 
     */
    Integer calcularNotaMediaExpediente(Integer idExpediente) ;

    /**
     * Metodo para obtener el informe de la evaluacion para validar
     *
     * @param idPersona
     * @param itCoorEval
     * @param idConvocatoria
     * @return
     */
    public List<ExpedienteEval> obtenerInformeEvaluacionValidarDesasociar(Integer idPersona,
            String itCoorEval, Integer idConvocatoria);

    public List<CatalogosValoresDTO> obtenerListaTipoInformesCoordinador(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador);

    public List<CatalogosValoresDTO> obtenerListaTipoInformesEvaluador(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador, Integer estadoCerradoEvaluador);

    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesEvaluadorInformeEvalDoc(ExpedienteFiltrosDTO item,
            Integer estadoCerradoCoordinador, Integer estadoCerradoEvaluador);

    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesCoordinadorInformeEvalDoc(ExpedienteFiltrosDTO item, Integer estadoCerradoCoordinador);

    /**
     * Metodo para obtener los tipos de informe pendientes de revision por el
     * coordinador de CUALQUIERA de los evaluadores que tiene asignados
     * (ExpedienteEval con idExpedienteEvalPadre = idExpedienteEvalPadre), dentro
     * del mismo expediente. Un tipo de informe se considera pendiente mientras no
     * exista un InformeEvalDoc en estado "Cerrado coordinador" para ese
     * evaluador y ese tipo de informe.
     *
     * @param idExpedienteEvalPadre idExpedienteEval del coordinador
     * @param estadoCerradoCoordinador idValor del estado "Cerrado coordinador"
     * @return lista de tipos de informe pendientes (vacia si todos los
     *         evaluadores tienen todos sus tipos de informe cerrados por el
     *         coordinador)
     */
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesRevisionCoordinadorEvaluadores(
            Integer idExpedienteEvalPadre, Integer estadoCerradoCoordinador);

    /**
     * Metodo para obtener los tipos de informe asignados a UN evaluador concreto
     * (ExpedienteEval identificado por idExpedienteEval) que todavia no han sido
     * cerrados/revisados por el coordinador (no basta con que el evaluador los
     * haya presentado: se exige estado "Cerrado coordinador"). Se usa para saber
     * si, al cerrar un informe concreto de este evaluador, quedan otros tipos de
     * informe pendientes de revisar antes de poder cerrar su ExpedienteEval.
     *
     * @param idExpedienteEval idExpedienteEval del evaluador
     * @param estadoCerradoCoordinador idValor del estado "Cerrado coordinador"
     * @return lista de tipos de informe de este evaluador pendientes de cierre
     *         por el coordinador (vacia si todos estan ya cerrados por el
     *         coordinador)
     */
    public List<CatalogosValoresDTO> obtenerTipoInformesPendientesCierreCoordinadorEvaluador(
            Integer idExpedienteEval, Integer estadoCerradoCoordinador);

    public List<InformeEvaluacionDTO> obtenerInformeEvaluacion(Integer idExpedienteEval,
            InformeDTO informeDTO);

    public List<ExpedienteDocEvalDTO> obtenerListaInformesEvaluador(Integer idExpedienteEval, Integer informeEvaluador);

    public List<ExpedienteDocEvalDTO> obtenerListaDocEvalPorCdFicaInformeEvalDoc(String cdFica);

}
