package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;

public interface ConvocatoriaEntidadCerradaDAO extends BaseDAO<ConvocatoriaEntidadCerrada, Integer> {

    /**
     * Método para recuperar las entidades cerradas por convocatoria
     * @param idConvocatoria
     * @return
     */
    List<ConvocatoriaEntidadCerradaDTO> obtenerPorConvocatoria(Integer idConvocatoria);
    
}
