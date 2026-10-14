package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaReq;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaReq
 *
 * @author nttdata
 */
public interface ConvocatoriaReqDAO extends BaseDAO<ConvocatoriaReq, Integer> {
    
    /**
     * @param idConvocatoria
     * @return
     */
    ConvocatoriaReq buscarConvocatoriaReqByIdConvocatoria(Integer idConvocatoria);
    

}
