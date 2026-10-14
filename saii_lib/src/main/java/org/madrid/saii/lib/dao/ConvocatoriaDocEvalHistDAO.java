
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaDocEvalHist
 *
 * @author everis
 */
public interface ConvocatoriaDocEvalHistDAO extends BaseDAO<ConvocatoriaDocEvalHist, Integer> {

    /**
    *
    */
    List<ConvocatoriaDocEvalHistDTO> obtenerDocumentosConvocatoriaEval(Integer idConvocatoria, Integer interesado, String tipo);

}
