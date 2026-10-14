package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.GcatusuarsDAO;
import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.GcatusuarsDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpeT046
 *
 * @author everis
 */
public interface GcatusuarsService extends BaseService<UnidadAdmin, Integer, GcatusuarsDAO> {

    /**
     * Buscar por id cdUsuario.
     *
     * @param cdUsuario the id ExpeT046
     * @return ExpeT046DTO
     * @throws ServiceException the service exception
     */
    GcatusuarsDTO buscarCdUsuario(final String cdUsuario) throws ServiceException;

    /**
     * Metodo que devuelve la consejeria
     *
     * @return
     * @throws ServiceException
     */
    String obtenerConsejeria() throws ServiceException;

    /**
     * Metodo que devuelve la descripcion de una Unidad
     *
     * @param cdUnidad
     * @return
     * @throws ServiceException
     */
    String descripcionUnidad(String cdUnidad) throws ServiceException;
}
