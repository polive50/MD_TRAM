
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
public interface ConvocatoriaEvalTipoDocDAO extends BaseDAO<ConvocatoriaEvalTipoDoc, Integer> {

    /**
    *
    */
    List<ConvocatoriaEvalTipoDocDTO> obtenerTipoDocumentosConvocatoriaEval(Integer idConvocatoria, Integer nivel);

}
