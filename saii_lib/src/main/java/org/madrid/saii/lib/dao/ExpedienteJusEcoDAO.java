package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteJusEco;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteJusEco
 *
 * @author quental
 */
public interface ExpedienteJusEcoDAO extends BaseDAO<ExpedienteJusEco, Integer> {


    List<ExpedienteJusEco> findByIdExpediente(Integer idExpediente);


}
