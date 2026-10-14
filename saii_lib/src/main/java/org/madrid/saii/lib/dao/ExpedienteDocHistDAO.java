
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteDocHist
 *
 * @author everis
 */
public interface ExpedienteDocHistDAO extends BaseDAO<ExpedienteDocHist, Integer> {

    /**
     * Crea el historico
     *
     * @param expedienteDoc
     * @param cdUsuAlta. Se informa si es distinto del login web
     * @return
     */
    ExpedienteDocHist crearHistorico(ExpedienteDoc expedienteDoc, String cdUsuAlta);

    /**
    *  Crea el historico de una eliminacion de expdte doc
    *  
    * @param expedienteDoc
    *  @param cdUsuBaja. Se informa si es distinto del login web
    * @return
    */
    ExpedienteDocHist crearHistoricoBaja(ExpedienteDoc expedienteDoc, String cdUsuBaja);
}
