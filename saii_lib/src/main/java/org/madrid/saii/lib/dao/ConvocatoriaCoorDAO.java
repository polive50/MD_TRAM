package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.dto.ConvocatoriaCoorFiltrosDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorNavegacionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio ConvocatoriaCoor
 *
 * @author nttdata
 */
public interface ConvocatoriaCoorDAO extends BaseDAO<ConvocatoriaCoor, Integer> {

    /**
     * Obtener convocatorias Coordinador.
     *
     * @param filtros
     * @return ConvocatoriaCoorNavegacionDTO
     * @throws ServiceException
     */
    ConvocatoriaCoorNavegacionDTO obtenerConvocatoriasCoor(ConvocatoriaCoorFiltrosDTO filtros);

}
