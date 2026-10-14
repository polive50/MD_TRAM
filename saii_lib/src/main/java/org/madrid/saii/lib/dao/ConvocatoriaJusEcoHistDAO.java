package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaJusEcoHist
 *
 * @author quental
 */
public interface ConvocatoriaJusEcoHistDAO extends BaseDAO<ConvocatoriaJusEcoHist, Integer> {

    /**
     * Metodo que guarda el historico de convocatoriaJusEco
     *
     * @param convocatoriaJusEco
     */
    void crearHistorico(ConvocatoriaJusEco convocatoriaJusEco);
}
