package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.TipoViaDAO;
import org.madrid.saii.lib.domain.SucaTipoVial;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.converters.SucaTipoViaToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio SucaTipoVial.
 *
 * @author everis
 */
@Service("tipoViaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class TipoViaServiceImpl extends BaseServiceImpl<SucaTipoVial, String, TipoViaDAO>
        implements TipoViaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param solicitudDAO
     */
    @Autowired
    public TipoViaServiceImpl(@Qualifier("tipoViaDAO") TipoViaDAO tipoViaDAO) {
        super();
        this.setDao(tipoViaDAO);
    }

    /**
     *
     */
    @Override
    public List<CatalogoDTO> obtener() throws ServiceException {
        ArrayList<AtlasSort> lstOrden = new ArrayList<>();
        lstOrden.add(new AtlasSort("dsTvia", true));

        return new ArrayList<>(
                Lists.transform(this.find(null, lstOrden), SucaTipoViaToDTO.INSTANCE));
    }

}
