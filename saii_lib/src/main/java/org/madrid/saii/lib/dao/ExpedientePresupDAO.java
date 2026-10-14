package org.madrid.saii.lib.dao;

import java.util.Map;
import java.util.Set;

import org.madrid.saii.lib.domain.ExpedientePresup;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteFin
 *
 * @author nttdata
 */
public interface ExpedientePresupDAO extends BaseDAO<ExpedientePresup, Integer> {

    Map<Integer, Integer> obtenerTiposFinanciacionPorIds(Set<Integer> idsExpedientePresup);

}
