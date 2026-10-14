package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteActuaHist
 *
 * @author nttdata
 */
public interface ExpedienteActuaHistDAO extends BaseDAO<ExpedienteActuaHist, Integer> {

	public void crearHistorico(ExpedienteActua expedienteActua);

	List<ExpedienteActuaHistDTO> buscarPorExpediente(Integer idExpediente);
}
