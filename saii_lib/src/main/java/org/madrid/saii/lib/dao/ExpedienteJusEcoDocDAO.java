
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpJusEcoDocConceptoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;

/**
 * Interfaz para el DAO de la clase de dominio ExpedienteJusEcoDoc
 *
 * @author Quental
 */
public interface ExpedienteJusEcoDocDAO extends BaseDAO<ExpedienteJusEcoDoc, Integer> {

    List<ExpJusEcoDocConceptoDTO> cargarExpJusEcoDocPartidaSeleccionada(ExpedienteJusEcoDocDTO expedienteJusEcoDocSeleccionado, Integer partidaSelecc);

}
