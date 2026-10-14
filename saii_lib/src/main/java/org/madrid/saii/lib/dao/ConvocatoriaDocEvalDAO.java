
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaDoc
 *
 * @author everis
 */
public interface ConvocatoriaDocEvalDAO extends BaseDAO<ConvocatoriaDocEval, Integer> {

    /**
    *
    */
    List<ConvocatoriaDocEvalDTO> obtenerDocumentosConvocatoriaEval(Integer idConvocatoria, Integer interesado, String tipo);

}
