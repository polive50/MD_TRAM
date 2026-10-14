package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaGestor;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaGestor
 *
 * @author nttdata
 */
public interface ConvocatoriaGestorDAO extends BaseDAO<ConvocatoriaGestor, Integer> {
    
    public List<Integer> obtenerTiposTramite(Integer idConvocatoria, String login);
    
}
