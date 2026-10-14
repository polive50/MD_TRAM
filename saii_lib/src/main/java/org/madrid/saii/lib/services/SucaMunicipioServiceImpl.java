package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.SucaMunicipioDAO;
import org.madrid.saii.lib.domain.SucaMunicipio;
import org.madrid.saii.lib.domain.SucaMunicipioId;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
import org.madrid.saii.lib.dto.converters.SucaMunicipioToDTO;
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
 * Implementación del servicio para la clase de dominio SucaMunicipio.
 *
 * @author everis
 */
@Service("sucaMunicipioService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class SucaMunicipioServiceImpl
        extends BaseServiceImpl<SucaMunicipio, SucaMunicipioId, SucaMunicipioDAO>
        implements SucaMunicipioService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param solicitudDAO
     */
    @Autowired
    public SucaMunicipioServiceImpl(
            @Qualifier("sucaMunicipioDAO") SucaMunicipioDAO sucaMunicipioDAO) {
        super();
        this.setDao(sucaMunicipioDAO);
    }

    /**
     *
     */
    @Override
    public List<SucaMunicipioDTO> buscarPorProvincia(Integer idProvincia) throws ServiceException {
        ArrayList<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort("dsMuni", true));
        ArrayList<AtlasFilter> lisFilter = new ArrayList<>();
        lisFilter.add(new AtlasFilter("nmProv", idProvincia));

        return new ArrayList<>(
                Lists.transform(this.find(lisFilter, lisOrden), SucaMunicipioToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public SucaMunicipioDTO obtenerMunicipio(String provincia, String municipio)
            throws ServiceException {
        SucaMunicipioId id = new SucaMunicipioId();
        id.setCdProv(provincia);
        id.setCdMuni(municipio);
        return SucaMunicipioToDTO.INSTANCE.apply(this.find(id));
    }
}
