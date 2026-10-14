package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
public interface ExpedienteModifPresPlazoOtraDAO extends BaseDAO<ExpedienteModifPresPlazoOtra, Integer> {

    List<ExpedientePresupDTO> buscarPresupModifTemp(Integer idExpedienteModifPresPlazoOtra);


}