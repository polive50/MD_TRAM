package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.domain.ExpedienteJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecHistToDTO;
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
 * @author everis
 */
@Service("expedienteJusTecHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusTecHistServiceImpl
        extends BaseServiceImpl<ExpedienteJusTecHist, Integer, ExpedienteJusTecHistDAO>
        implements ExpedienteJusTecHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public ExpedienteJusTecHistServiceImpl(
            @Qualifier("expedienteJusTecHistDAO") ExpedienteJusTecHistDAO expedienteJusTecHistDAO) {
        super();
        this.setDao(expedienteJusTecHistDAO);
    }

    @Override
    public List<ExpedienteJusTecHistDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteJusTecHistToDTO.INSTANCE));
    }

}
