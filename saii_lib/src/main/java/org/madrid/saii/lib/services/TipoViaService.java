package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.TipoViaDAO;
import org.madrid.saii.lib.domain.SucaTipoVial;
import org.madrid.saii.lib.dto.CatalogoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio de la clase de dominio SucaTipoVial.
 *
 * @author ICM
 * @see atlas.rest.SucaTipoVial.TipoVia
 */

public interface TipoViaService extends BaseService<SucaTipoVial, String, TipoViaDAO> {

    /**
     * Obtener.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    List<CatalogoDTO> obtener() throws ServiceException;
}
