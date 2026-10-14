package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;

/**
 * Interfaz para el DAO de la clase de dominio InformeEvalDoc
 *
 * @author quental
 */
public interface InformeEvalDocDAO extends BaseDAO<InformeEvalDoc, Integer> {

    /**
     * Obtiene el registro de evaluación de informe por expediente eval e informe
     *
     * @param idExpedienteEval
     * @param idInforme
     * @return InformeEvalDocDTO
     */
    InformeEvalDocDTO obtenerPorExpedienteEvalEInforme(Integer idExpedienteEval, Integer idInforme);

    /**
     * Obtiene la lista de registros de evaluación de informe por expediente eval
     *
     * @param idExpedienteEval
     * @return List<InformeEvalDocDTO>
     */
    List<InformeEvalDocDTO> obtenerListaPorExpedienteEval(Integer idExpedienteEval);

    /**
     * Obtiene el registro de evaluación de informe por expediente eval, informe e itFirmado
     *
     * @param idExpedienteEval
     * @param idInforme
     * @param itFirmado
     * @return InformeEvalDocDTO
     */
    InformeEvalDocDTO obtenerPorExpedienteEvalInformeItFirmado(Integer idExpedienteEval,
            Integer idInforme, String itFirmado);
    
    /**
     * Obtiene los documentos de informe evaluación por id expediente, itCoordEval y itFirmado
     * @param idExpediente
     * @param itCoorEval
     * @param itFirmado
     * @return
     */
    List<InformeEvalDocDTO> obtenerIdExpedienteItCoorEvalItFirmado(Integer idExpediente);

    /**
     * Obtiene los informes de evaluadores (itCoorEval='E') por idExpedienteEvalPadre
     *
     * @param idExpedienteEvalPadre
     * @return List<InformeEvalDocDTO>
     */
    List<InformeEvalDocDTO> obtenerListaInformesEvaluadoresPorExpediente(Integer idExpedienteEvalPadre);

    List<InformeEvalDocDTO> obtenerListaPorExpedienteEvalSinEstado(Integer idExpedienteEval);

}
