
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;

/**
 * Interfaz para el DAO de la clase de dominio EvaluacionTipoDoc
 *
 * @author everis
 */
public interface EvaluacionTipoDocDAO extends BaseDAO<EvaluacionTipoDoc, Integer> {

    /**
    *
    */
    List<EvaluacionTipoDocDTO> obtenerTipoDocumentosEvaluacion(Integer idInforme);

   

}
