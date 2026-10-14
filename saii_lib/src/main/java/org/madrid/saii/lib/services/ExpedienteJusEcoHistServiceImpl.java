package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoHistToDTO;
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
 * @author quental
 */
@Service("expedienteJusEcoHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusEcoHistServiceImpl
        extends BaseServiceImpl<ExpedienteJusEcoHist, Integer, ExpedienteJusEcoHistDAO>
        implements ExpedienteJusEcoHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public ExpedienteJusEcoHistServiceImpl(
            @Qualifier("expedienteJusEcoHistDAO") ExpedienteJusEcoHistDAO expedienteJusEcoHistDAO) {
        super();
        this.setDao(expedienteJusEcoHistDAO);
    }

    @Override
    public List<ExpedienteJusEcoHistDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteJusEcoHistToDTO.INSTANCE));
    }

}
