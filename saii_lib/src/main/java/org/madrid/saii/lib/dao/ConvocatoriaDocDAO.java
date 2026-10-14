
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaDoc
 *
 * @author everis
 */
public interface ConvocatoriaDocDAO extends BaseDAO<ConvocatoriaDoc, Integer> {

    /**
    *
    */
    List<ConvocatoriaDocDTO> obtenerDocumentosConvocatoria(Integer idConvocatoria);

}
