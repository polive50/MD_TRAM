package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.SucaProvinciaDAO;
import org.madrid.saii.lib.domain.SucaProvincia;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.converters.SucaProvinciaToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio SucaProvincia.
 *
 * @author everis
 */
@Service("sucaProvinciaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class SucaProvinciaServiceImpl extends
        BaseServiceImpl<SucaProvincia, String, SucaProvinciaDAO> implements SucaProvinciaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param solicitudDAO
     */
    @Autowired
    public SucaProvinciaServiceImpl(
            @Qualifier("sucaProvinciaDAO") SucaProvinciaDAO sucaProvinciaDAO) {
        super();
        this.setDao(sucaProvinciaDAO);
    }

    /**
     *
     */
    @Override
    public List<CatalogoDTO> obtener() throws ServiceException {
        ArrayList<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort("dsProv", true));

        return new ArrayList<>(
                Lists.transform(this.find(null, lisOrden), SucaProvinciaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public CatalogoDTO obtenerPorProvincia(String provincia) throws ServiceException {
        return SucaProvinciaToDTO.INSTANCE.apply(this.find(provincia));
    }

}
