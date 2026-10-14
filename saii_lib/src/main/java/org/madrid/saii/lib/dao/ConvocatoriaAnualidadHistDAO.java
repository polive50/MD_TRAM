package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidadHist;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaAnualidadHist
 *
 * @author Quental
 */
public interface ConvocatoriaAnualidadHistDAO extends BaseDAO<ConvocatoriaAnualidadHist, Integer> {
    
    /**
     * Metodo que guarda el historico de convocatoriaAnualidad
     *
     * @param convocatoriaAnualidad
     */
    void crearHistorico(ConvocatoriaAnualidad convocatoriaAnualidad);

}
