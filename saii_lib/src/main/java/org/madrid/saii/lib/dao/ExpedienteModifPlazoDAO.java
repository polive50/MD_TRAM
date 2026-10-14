package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteModifPlazo;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteModifPlazo
 *
 * @author quental
 */
public interface ExpedienteModifPlazoDAO extends BaseDAO<ExpedienteModifPlazo, Integer> {


    List<ExpedienteModifPlazo> findByIdExpediente(Integer idExpediente);


}
