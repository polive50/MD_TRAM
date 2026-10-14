package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaJusTecHist
 *
 * @author nttdata
 */
public interface ConvocatoriaJusTecHistDAO extends BaseDAO<ConvocatoriaJusTecHist, Integer> {

    /**
     * Metodo que guarda el historico de convocatoriaJusTec
     *
     * @param convocatoriaJusTec
     */
    void crearHistorico(ConvocatoriaJusTec convocatoriaJusTec);
}
