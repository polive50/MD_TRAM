
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;

/**
 * Interfaz para el DAO de la clase de dominio DocumentoPtfr
 *
 * @author everis
 */
public interface DocumentoPtfrDAO extends BaseDAO<DocumentoPtfr, Integer> {

    List<DocumentoPtfrDTO> obtenerListaHistoricoNotif(Integer idExpediente);
    
    /**
     * 
     * @param idExpediente
     * @param lstTramites
     * @return
     */
    List<DocumentoPtfrDTO> obtenerPorExpedienteYTramites(Integer idExpediente, List<Integer> lstTramites);
    
    
    /**
     * 
     * @param idExpediente
     * @param tramite
     * @param idModificacion
     * @return
     */
    public List<DocumentoPtfrDTO> obtenerPorExpedienteModifPresupPlazosTramites(Integer idExpediente, Integer tramite, Integer idModificacion);
    
}
