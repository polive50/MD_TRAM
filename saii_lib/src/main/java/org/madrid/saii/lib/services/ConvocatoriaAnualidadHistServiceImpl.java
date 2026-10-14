package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaAnualidadHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidadHist;
import org.madrid.saii.lib.dto.ConvocatoriaAnualidadHistDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAnualidadHistToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAnualidadHistToEntity;
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
 * @author Quental
 */
@Service("convocatoriaAnualidadHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaAnualidadHistServiceImpl
        extends BaseServiceImpl<ConvocatoriaAnualidadHist, Integer, ConvocatoriaAnualidadHistDAO>
        implements ConvocatoriaAnualidadHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaAnualidadHistDAO
     */
    @Autowired
    public ConvocatoriaAnualidadHistServiceImpl(
            @Qualifier("convocatoriaAnualidadHistDAO") ConvocatoriaAnualidadHistDAO convocatoriaAnualidadHistDAO) {
        super();
        this.setDao(convocatoriaAnualidadHistDAO);
    }

    @Override
    public List<ConvocatoriaAnualidadHistDTO> buscar(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaAnualidadHistToDTO.INSTANCE));
    }

    @Override
    public ConvocatoriaAnualidadHistDTO guardar(ConvocatoriaAnualidadHistDTO convocatoriaAnualidadHistDTO)
            throws ServiceException {
        return ConvocatoriaAnualidadHistToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaAnualidadHistToEntity.INSTANCE.apply(convocatoriaAnualidadHistDTO)));
    }

}
