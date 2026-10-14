package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.SucaPaisDAO;
import org.madrid.saii.lib.domain.SucaPais;
import org.madrid.saii.lib.dto.SucaPaisDTO;
import org.madrid.saii.lib.dto.converters.SucaPaisToDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio SucaPais.
 *
 * @author Quental
 */
@Service("sucaPaisService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class SucaPaisServiceImpl
        extends BaseServiceImpl<SucaPais, String, SucaPaisDAO>
        implements SucaPaisService {

    @Autowired
    public SucaPaisServiceImpl(
            @Qualifier("sucaPaisDAO") SucaPaisDAO sucaPaisDAO) {
        super();
        this.setDao(sucaPaisDAO);
    }

    @Override
    public SucaPaisDTO obtenerPais(String codigoPais) throws ServiceException{
        return SucaPaisToDTO.INSTANCE.apply(this.find(codigoPais));

    }

    @Override
    public List<SucaPaisDTO> obtenerPaises() throws ServiceException{
    	ArrayList<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort("dsPais", true));

        return new ArrayList<>(
                Lists.transform(this.find(null, lisOrden), SucaPaisToDTO.INSTANCE));

    }








}
