package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresMontHistDAO;
import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresMontHistToDTO;
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
 * Implementación del servicio para la clase de dominio ExpedientePremontoHist
 *
 * @author nttdata
 */
@Service("expedientePresMontHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedientePresMontHistServiceImpl
        extends BaseServiceImpl<ExpedientePresMontHist, Integer, ExpedientePresMontHistDAO>
        implements ExpedientePresMontHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedientePremontoHistDAO
     */
    @Autowired
    public ExpedientePresMontHistServiceImpl(
            @Qualifier("expedientePresMontHistDAO") ExpedientePresMontHistDAO expedientePresMontHistDAO) {
        super();
        this.setDao(expedientePresMontHistDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedientePresMontHistServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedientePresMontHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedientePresMontHistToDTO.INSTANCE));
    }

    @Override
    public List<ExpedientePresMontHistDTO> buscarIdExpedientePresup(Integer idExpedientePresup) {
        // TODO Auto-generated method stub
        return null;
    }
}
