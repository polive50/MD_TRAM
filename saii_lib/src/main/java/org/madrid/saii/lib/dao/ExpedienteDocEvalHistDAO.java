
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteDocEvalHist
 *
 * @author everis
 */
public interface ExpedienteDocEvalHistDAO extends BaseDAO<ExpedienteDocEvalHist, Integer> {

    /**
    *
    */
    List<ExpedienteDocEvalHistDTO> obtenerDocumentosExpedienteDocEvalHist(Integer idExpedienteEval);

   

}
