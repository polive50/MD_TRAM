package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.RepresentanteJurFiltrosDTO;

/**
 * Interfaz para el DAO de la clase de dominio RepresentanteJur
 *
 * @author everis
 */
public interface RepresentanteJurDAO extends BaseDAO<RepresentanteJur, Integer> {

    /**
     * Realiza un filtrado de espediente
     *
     * @param representanteJurFiltrosDTO
     * @return List<RepresentanteJurFiltrosDTO>
     */
    List<RepresentanteJur> filtrar(RepresentanteJurFiltrosDTO dto);

}
