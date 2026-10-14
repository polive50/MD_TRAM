package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.RevisionInfEvaJustecHistDAO;
import org.madrid.saii.lib.domain.RevisionInfEvaJustecHist;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecHistDTO;
import org.madrid.saii.lib.dto.converters.RevisionInfEvaJustecHistToDTO;
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
@Service("revisionInfEvaJustecHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class RevisionInfEvaJustecHistServiceImpl
        extends BaseServiceImpl<RevisionInfEvaJustecHist, Integer, RevisionInfEvaJustecHistDAO>
        implements RevisionInfEvaJustecHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public RevisionInfEvaJustecHistServiceImpl(
            @Qualifier("revisionInfEvaJustecHistDAO") RevisionInfEvaJustecHistDAO revisionInfEvaJustecHistDAO) {
        super();
        this.setDao(revisionInfEvaJustecHistDAO);
    }

    @Override
    public List<RevisionInfEvaJustecHistDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                RevisionInfEvaJustecHistToDTO.INSTANCE));
    }

}
