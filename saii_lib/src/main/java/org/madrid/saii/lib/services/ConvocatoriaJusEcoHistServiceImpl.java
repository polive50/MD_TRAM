package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaJusEcoHistDAO;
import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoHistDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusEcoHistToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaJusEcoHistToEntity;
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
 * @author quental
 */
@Service("convocatoriaJusEcoHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaJusEcoHistServiceImpl
        extends BaseServiceImpl<ConvocatoriaJusEcoHist, Integer, ConvocatoriaJusEcoHistDAO>
        implements ConvocatoriaJusEcoHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaJusEcoHistDAO
     */
    @Autowired
    public ConvocatoriaJusEcoHistServiceImpl(
            @Qualifier("convocatoriaJusEcoHistDAO") ConvocatoriaJusEcoHistDAO convocatoriaJusEcoHistDAO) {
        super();
        this.setDao(convocatoriaJusEcoHistDAO);
    }

    @Override
    public List<ConvocatoriaJusEcoHistDTO> buscar(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaJusEcoHistToDTO.INSTANCE));
    }

    @Override
    public ConvocatoriaJusEcoHistDTO guardar(ConvocatoriaJusEcoHistDTO convocatoriaJusEcoHistDTO)
            throws ServiceException {
        return ConvocatoriaJusEcoHistToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaJusEcoHistToEntity.INSTANCE.apply(convocatoriaJusEcoHistDTO)));
    }

}
