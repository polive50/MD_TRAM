package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteAnualidad;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteAnualidad
 *
 * @author nttdata
 */
public interface ExpedienteAnualidadDAO extends BaseDAO<ExpedienteAnualidad, Integer> {


    List<ExpedienteAnualidad> findByIdExpediente(Integer idExpediente);


}
