package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.InfApartadoDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteEvalJusTec
 *
 * @author quental
 */
public interface ExpedienteEvalJusTecDAO extends BaseDAO<ExpedienteEvalJusTec, Integer> {
    
    List<ExpedienteEvalJusTec> buscarExpEvalJCT(Integer idExpedienteJusTec);
    
    List<ExpedienteEvalJusTec> buscarIdExpedienteEvalJCT(Integer idExpedienteJusTec);
 
    /**
    *
    */
    List<InformeEvaluacionDTO> obtenerInformeEvaluacionJustec(Integer idExpedienteEvalJustec);

    /**
    *
    */
    List<InfApartadoDatoDTO> obtenerApartadosJustecDatos(Integer idExpedienteEval,
            Integer idEvaluacion);

    List<ExpedienteEvalJusTecDTO> obtenerListaExpedienteEvalJustec(Integer idExpedienteEvalJustec, Integer idInteresado);

    List<ExpedienteEvalJusTecDTO> buscarPeriodosPendienteEvalJustec(Integer idExpediente,
            Boolean pendienteEval) ;


   
}
