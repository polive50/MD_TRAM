package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;

/**
 * Interfaz para el DAO de la clase de dominio ExpedientePremontoHist
 *
 * @author nttdata
 */
public interface ExpedientePresMontHistDAO extends BaseDAO<ExpedientePresMontHist, Integer> {

    void crearHistorico(ExpedientePresupMonto expedientePresupMonto);
}
