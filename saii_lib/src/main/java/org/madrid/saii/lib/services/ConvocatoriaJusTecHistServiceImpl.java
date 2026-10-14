package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusTecHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecHistDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusTecHistToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusTecHistToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author everis
 */
@Service("convocatoriaJusTecHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaJusTecHistServiceImpl
        extends BaseServiceImpl<ConvocatoriaJusTecHist, Integer, ConvocatoriaJusTecHistDAO>
        implements ConvocatoriaJusTecHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaJusTecHistDAO
     */
    @Autowired
    public ConvocatoriaJusTecHistServiceImpl(
            @Qualifier("convocatoriaJusTecHistDAO") ConvocatoriaJusTecHistDAO convocatoriaJusTecHistDAO) {
        super();
        this.setDao(convocatoriaJusTecHistDAO);
    }

    @Override
    public List<ConvocatoriaJusTecHistDTO> buscar(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaJusTecHistToDTO.INSTANCE));
    }

    @Override
    public ConvocatoriaJusTecHistDTO guardar(ConvocatoriaJusTecHistDTO convocatoriaJusTecHistDTO)
            throws ServiceException {
        return ConvocatoriaJusTecHistToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaJusTecHistToEntity.INSTANCE.apply(convocatoriaJusTecHistDTO)));
    }

}
