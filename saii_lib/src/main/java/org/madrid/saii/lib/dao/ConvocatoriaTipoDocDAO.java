package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaTip
 *
 * @author everis
 */
public interface ConvocatoriaTipoDocDAO extends BaseDAO<ConvocatoriaTipoDoc, Integer> {
    
    /**
     * 
     * @param idConvocatoria
     * @return
     */
    public List<ConvocatoriaTipoDoc> buscarNoConfiguradasImpresos(Integer idConvocatoria);
    
    /**
     * 
     * @param idConvocatoria
     * @return
     */
    public List<ConvocatoriaTipoDoc> buscarPorConvocatoria(Integer idConvocatoria);
    
}
