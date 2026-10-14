package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaFirDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaFirToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaFirToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaPlaFir
 *
 * @author everis
 */
@Service("convocatoriaPlaFirService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaPlaFirServiceImpl
        extends BaseServiceImpl<ConvocatoriaPlaFir, Integer, ConvocatoriaPlaFirDAO>
        implements ConvocatoriaPlaFirService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaPlaFirDAO
     */
    @Autowired
    public ConvocatoriaPlaFirServiceImpl(
            @Qualifier("convocatoriaPlaFirDAO") ConvocatoriaPlaFirDAO convocatoriaPlaFirDAO) {
        super();
        this.setDao(convocatoriaPlaFirDAO);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaFirDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaPlaFirToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaPlaFirDTO buscarId(Integer idConvocatoriaPlaFir) throws ServiceException {
        return ConvocatoriaPlaFirToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaPlaFir));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaFirDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaPlaFirToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaPlaFirDTO guardar(ConvocatoriaPlaFirDTO convocatoriaPlaFirDTO)
            throws ServiceException {
        return ConvocatoriaPlaFirToDTO.INSTANCE.apply(this
                .insertOrUpdate(ConvocatoriaPlaFirToEntity.INSTANCE.apply(convocatoriaPlaFirDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ConvocatoriaPlaFirDTO convocatoriaPlaFirDTO)
            throws ServiceException {
        convocatoriaPlaFirDTO.setCdUsuBaja(null);
        convocatoriaPlaFirDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaPlaFirToEntity.INSTANCE.apply(convocatoriaPlaFirDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ConvocatoriaPlaFirDTO convocatoriaPlaFirDTO)
            throws ServiceException {
        convocatoriaPlaFirDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaPlaFirDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaPlaFirToEntity.INSTANCE.apply(convocatoriaPlaFirDTO));
    }

}
