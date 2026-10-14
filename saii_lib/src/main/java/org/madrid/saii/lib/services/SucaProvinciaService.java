package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.SucaProvinciaDAO;
import org.madrid.saii.lib.domain.SucaProvincia;
import org.madrid.saii.lib.dto.CatalogoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio SucaProvincia.
 *
 * @author everis
 */
public interface SucaProvinciaService extends BaseService<SucaProvincia, String, SucaProvinciaDAO> {

    /**
     * Devuelve todas las provincias.
     *
     * @return List<CatalogoDTO>
     * @throws ServiceException the service exception
     */
    List<CatalogoDTO> obtener() throws ServiceException;

    /**
     * Devuelve la provincia a partir del código.
     *
     * @param provincia the provincia
     * @return CatalogoDTO
     * @throws ServiceException the service exception
     */
    CatalogoDTO obtenerPorProvincia(String provincia) throws ServiceException;
}
