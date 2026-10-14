
package org.madrid.saii.lib.dao;

import java.util.List;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteDocEval
 *
 * @author everis
 */
public interface ExpedienteDocEvalDAO extends BaseDAO<ExpedienteDocEval, Integer> {

    /**
    *
    */
    List<ExpedienteDocEvalDTO> obtenerDocumentosExpedienteDocEval(Integer idExpedienteEval);

    ExpedienteDocEvalDTO obtenerPorTipoDocPorExpedienteEval(Integer idValorTipoDoc,
            Integer idExpedienteEval);

    List<ExpedienteDocEvalDTO> buscarDocumentosInformePorFica(Integer cdFica);

    List<ExpedienteDocEvalDTO> buscarDocumentosPortipoDocumento(Integer idExpedienteEval,
            String dsNombreFichero);
    

}
