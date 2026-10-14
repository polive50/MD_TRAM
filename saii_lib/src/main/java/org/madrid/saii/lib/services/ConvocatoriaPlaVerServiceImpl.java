package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaVerDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.dto.ConvocatoriaPlaVerDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaVerToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaVerToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaPlaVer
 *
 * @author everis
 */
@Service("convocatoriaPlaVerService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaPlaVerServiceImpl
        extends BaseServiceImpl<ConvocatoriaPlaVer, Integer, ConvocatoriaPlaVerDAO>
        implements ConvocatoriaPlaVerService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaPlaVerDAO
     */
    @Autowired
    public ConvocatoriaPlaVerServiceImpl(
            @Qualifier("convocatoriaPlaVerDAO") ConvocatoriaPlaVerDAO convocatoriaPlaVerDAO) {
        super();
        this.setDao(convocatoriaPlaVerDAO);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaVerDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaPlaVerToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaPlaVerDTO buscarId(Integer idConvocatoriaPlaVer) throws ServiceException {
        return ConvocatoriaPlaVerToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaPlaVer));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaVerDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaPlaVerToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaPlaVerDTO guardar(ConvocatoriaPlaVerDTO convocatoriaPlaVerDTO)
            throws ServiceException {
        return ConvocatoriaPlaVerToDTO.INSTANCE.apply(this
                .insertOrUpdate(ConvocatoriaPlaVerToEntity.INSTANCE.apply(convocatoriaPlaVerDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ConvocatoriaPlaVerDTO convocatoriaPlaVerDTO)
            throws ServiceException {
        convocatoriaPlaVerDTO.setCdUsuBaja(null);
        convocatoriaPlaVerDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaPlaVerToEntity.INSTANCE.apply(convocatoriaPlaVerDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ConvocatoriaPlaVerDTO convocatoriaPlaVerDTO)
            throws ServiceException {
        convocatoriaPlaVerDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaPlaVerDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaPlaVerToEntity.INSTANCE.apply(convocatoriaPlaVerDTO));
    }

}
