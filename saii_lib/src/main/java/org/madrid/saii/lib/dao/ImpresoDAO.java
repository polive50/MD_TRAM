package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Impreso;
import java.util.List;

/**
 * Interfaz para el DAO de la clase de dominio Impreso
 *
 * @author everis
 */
public interface ImpresoDAO extends BaseDAO<Impreso, Integer> {

    List<Impreso> buscarPorConvActorContrato(final Integer idConvocatoria, final Integer idActor,
            final List<Integer> lstTipoImpreso);
    
    List<Impreso> buscarPorExpedienteTipo(final Integer idExpediente, final List<Integer> lstTipoImpreso);
    
    List<Impreso> buscarPorIdConvocatoria(final Integer idConvocatoria);
    
}
