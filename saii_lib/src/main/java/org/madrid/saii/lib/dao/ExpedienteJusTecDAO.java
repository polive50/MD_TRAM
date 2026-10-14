package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteJusTec
 *
 * @author nttdata
 */
public interface ExpedienteJusTecDAO extends BaseDAO<ExpedienteJusTec, Integer> {


    List<ExpedienteJusTec> findByIdExpediente(Integer idExpediente);

    List<ExpedienteJusTec> buscarExpJCT(Integer idExpedienteJusTec);


}
