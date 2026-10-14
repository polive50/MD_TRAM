package org.madrid.saii.lib.services;


import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoHistDAO;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoHistToDTO;
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
 * Implementación del servicio para la clase de dominio ExpedienteContratoHist
 *
 */
@Service("expedienteContratoHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteContratoHistServiceImpl
        extends BaseServiceImpl<ExpedienteContratoHist, Integer, ExpedienteContratoHistDAO>
        implements ExpedienteContratoHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteContratoHistDAO
     */
    @Autowired
    public ExpedienteContratoHistServiceImpl(
            @Qualifier("expedienteContratoHistDAO") ExpedienteContratoHistDAO expedienteContratoHistDAO) {
        super();
        this.setDao(expedienteContratoHistDAO);
    }

    
    @Override
    public List<ExpedienteContratoHistDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteContratoHistToDTO.INSTANCE));
    }
}



