package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.NivelOrgDAO;
import org.madrid.saii.lib.domain.NivelOrg;
import org.madrid.saii.lib.dto.NivelOrgDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio NivelOrg
 *
 * @author everis
 */
public interface NivelOrgService extends BaseService<NivelOrg, Integer, NivelOrgDAO> {
    /**
     * Buscar todos los registros de NivelOrg.
     *
     * @return List<NivelOrgDTO>
     * @throws ServiceException the service exception
     */
    List<NivelOrgDTO> obtener() throws ServiceException;

    /**
     * Buscar por id idValor.
     *
     * @param idValor the id NivelOrg
     * @return NivelOrgDTO
     * @throws ServiceException the service exception
     */
    NivelOrgDTO buscarId(final Integer idValor) throws ServiceException;

    /**
     * Buscar paginadamente NivelOrg.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<NivelOrgDTO>
     * @throws ServiceException the service exception
     */
    List<NivelOrgDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;

}
