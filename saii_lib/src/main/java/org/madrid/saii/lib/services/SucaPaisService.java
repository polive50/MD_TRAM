package org.madrid.saii.lib.services;


import org.madrid.saii.lib.dao.SucaPaisDAO;
import org.madrid.saii.lib.domain.SucaPais;
import org.madrid.saii.lib.dto.SucaPaisDTO;

import atlas.core.exceptions.ServiceException;

import java.util.List;

public interface SucaPaisService extends BaseService<SucaPais, String, SucaPaisDAO> {

    /**
     * Realiza la busqueda de los pais por un codigo de pais.
     *
     * @param codigoPais
     * @return SucaPaisDTO
     * @throws ServiceException the service exception
     */
    SucaPaisDTO obtenerPais(String codigoPais) throws ServiceException;

    /**
     * Realiza la busqueda de los pais por un codigo de pais.
     *
     * @return List<SucaPaisDTO>
     * @throws ServiceException the service exception
     */
    List<SucaPaisDTO> obtenerPaises() throws ServiceException;



}
