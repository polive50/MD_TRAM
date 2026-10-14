package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.dto.EvaluacionDatoDTO;

/**
 * Interfaz para el DAO de la clase de dominio EvaluacionDato
 *
 * @author nttdata
 */
public interface EvaluacionDatoDAO extends BaseDAO<EvaluacionDatoDTO, Integer> {

    /**
     * Método para obtener la lista de Datos de Alegaciones
     * 
     * @param idExpediente
     * @return List<EvaluacionDatoDTO>
     */
    List<EvaluacionDatoDTO> obtenerEvaluacionDato(Integer idExpediente);

}
