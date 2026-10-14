package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.InformeEvalDocDAO;
import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio InformeEvalDoc
 *
 * @author quental
 */
public interface InformeEvalDocService
        extends BaseService<InformeEvalDoc, Integer, InformeEvalDocDAO> {

    /**
     * Obtiene el registro de evaluación de informe por expediente eval e informe
     *
     * @param idExpedienteEval
     * @param idInforme
     * @return InformeEvalDocDTO
     * @throws ServiceException
     */
    InformeEvalDocDTO obtenerPorExpedienteEvalEInforme(Integer idExpedienteEval, Integer idInforme)
            throws ServiceException;

    /**
     * Obtiene la lista de registros de evaluación de informe por expediente eval
     *
     * @param idExpedienteEval
     * @return List<InformeEvalDocDTO>
     * @throws ServiceException
     */
    List<InformeEvalDocDTO> obtenerListaPorExpedienteEval(Integer idExpedienteEval)
            throws ServiceException;

    /**
     * Guarda o actualiza un registro de evaluación de informe
     *
     * @param informeEvalDocDTO
     * @return InformeEvalDocDTO
     * @throws ServiceException
     */
    InformeEvalDocDTO guardar(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException;

    InformeEvalDocDTO mergeActualizar(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException;

    /**
     * Busca un registro por su identificador
     *
     * @param idEvalInfrmDoc
     * @return InformeEvalDocDTO
     * @throws ServiceException
     */
    InformeEvalDocDTO buscarId(Integer idEvalInfrmDoc) throws ServiceException;

    /**
     * Borrado lógico de un registro de evaluación de informe
     *
     * @param informeEvalDocDTO
     * @throws ServiceException
     */
    void borradoLogico(InformeEvalDocDTO informeEvalDocDTO) throws ServiceException;

    /**
     * Obtiene el registro de evaluación de informe por expediente eval, informe e itFirmado
     *
     * @param idExpedienteEval
     * @param idInforme
     * @param itFirmado
     * @return InformeEvalDocDTO
     * @throws ServiceException
     */
    InformeEvalDocDTO obtenerPorExpedienteEvalInformeItFirmado(Integer idExpedienteEval,
            Integer idInforme, String itFirmado) throws ServiceException;
    
    /**
     * Obtiene los informes evaluación documentos por el expediente, coordinado/evaluador y firmado
     * @param idExpediente
     * @param itCoorEval
     * @param itFirmado
     * @return
     * @throws ServiceException
     */
    List<InformeEvalDocDTO> obtenerIdExpedienteItCoorEvalItFirmado(Integer idExpediente) throws ServiceException;

    /**
     * Obtiene los informes de evaluadores (itCoorEval='E') por idExpedienteEvalPadre
     *
     * @param idExpedienteEvalPadre
     * @return List<InformeEvalDocDTO>
     * @throws ServiceException
     */
    List<InformeEvalDocDTO> obtenerListaInformesEvaluadoresPorExpediente(Integer idExpedienteEvalPadre)
            throws ServiceException;

}
