package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionDatoDAO;
import org.madrid.saii.lib.dto.EvaluacionDatoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio EvaluacionDato
 *
 * @author nttdata
 */
public interface EvaluacionDatoService
        extends BaseService<EvaluacionDatoDTO, Integer, EvaluacionDatoDAO> {

    /**
     * Método para obtener la lista de Datos de Alegaciones
     * 
     * @param idExpediente, identificador que corresponde al Expediente
     * @return List<EvaluacionDatoDTO>
     * @throws ServiceException
     */
    List<EvaluacionDatoDTO> obtenerEvaluacionDato(Integer idExpediente) throws ServiceException;
}
