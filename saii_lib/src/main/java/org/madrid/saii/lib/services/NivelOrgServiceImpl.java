package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.NivelOrgDAO;
import org.madrid.saii.lib.domain.NivelOrg;
import org.madrid.saii.lib.dto.NivelOrgDTO;
import org.madrid.saii.lib.dto.converters.NivelOrgToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio NivelOrg
 *
 * @author everis
 */
@Service("nivelOrgService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class NivelOrgServiceImpl extends BaseServiceImpl<NivelOrg, Integer, NivelOrgDAO>
        implements NivelOrgService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param nivelOrgDao
     */
    @Autowired
    public NivelOrgServiceImpl(@Qualifier("nivelOrgDAO") NivelOrgDAO nivelOrgDao) {
        super();
        this.setDao(nivelOrgDao);
    }

    /**
     * Obtener todos los catalogo valor.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<NivelOrgDTO> obtener() throws ServiceException {

        return new ArrayList<>(Lists.transform(this.findAll(), NivelOrgToDTO.INSTANCE));

    }

    /**
     * Buscar id.
     *
     * @param idValor the id NivelOrg
     * @return the NivelOrg DTO
     * @throws ServiceException the service exception
     */
    @Override
    public NivelOrgDTO buscarId(final Integer idValor) throws ServiceException {

        return NivelOrgToDTO.INSTANCE.apply(this.find(idValor));

    }

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<NivelOrgDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {

        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                NivelOrgToDTO.INSTANCE));

    }

}
