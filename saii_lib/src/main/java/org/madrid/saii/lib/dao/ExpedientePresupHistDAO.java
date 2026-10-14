package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedientePresupHist
 *
 * @author nttdata
 */
public interface ExpedientePresupHistDAO extends BaseDAO<ExpedientePresupHist, Integer> {

	void crearHistorico(ExpedientePresup expedientePresup);

	/**
	 * Metodo que devuelve la lista de historico de presupusto
	 *
	 * @param idExpediente
	 * @return List<ExpedientePresupHist>
	 */

	List<ExpedientePresupHistDTO> buscarPorExpediente(Integer idExpediente);
}
