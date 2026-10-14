package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteEvalJusTecHist
 *
 * @author quental
 */
public interface ExpedienteEvalJusTecHistDAO extends BaseDAO<ExpedienteEvalJusTecHist, Integer> {
    
    List<ExpedienteEvalJusTecHist> buscarPorIdExpedienteJusTecAndPersona(Integer idExpedienteJusTec, Integer idPersona);
    
    ExpedienteEvalJusTecHistDTO buscarByIdAndPeson(Integer idExpedienteJusTec, Integer idPersona);

    /**
     * Metodo que guarda el historico de expedienteEvalJusTec
     *
     * @param expedienteEvalJusTec
     * @return
     */
    void crearHistoricoExpEvalJus(ExpedienteEvalJusTec expedienteEvalJusTec);

}
