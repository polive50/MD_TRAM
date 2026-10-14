package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.SucaMunicipioDAO;
import org.madrid.saii.lib.domain.SucaMunicipio;
import org.madrid.saii.lib.domain.SucaMunicipioId;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio de la clase de dominio SucaMunicipio.
 *
 * @author everis
 */
public interface SucaMunicipioService
        extends BaseService<SucaMunicipio, SucaMunicipioId, SucaMunicipioDAO> {

    /**
     * Realiza la busqueda de los municipios de una provincia.
     *
     * @param idProvincia the id provincia
     * @return List<SucaMunicipioDTO>
     * @throws ServiceException the service exception
     */
    List<SucaMunicipioDTO> buscarPorProvincia(Integer idProvincia) throws ServiceException;

    /**
     * Devuelve el municipio a partir de la pronvicia y el código de municipio.
     *
     * @param provincia the provincia
     * @param municipio the municipio
     * @return SucaMunicipioDTO
     * @throws ServiceException the service exception
     */
    SucaMunicipioDTO obtenerMunicipio(String provincia, String municipio) throws ServiceException;
}
