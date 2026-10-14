package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaPlaDocDAO;
import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaDocToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaDocToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaPlaDoc
 *
 * @author quental
 */
@Service("convocatoriaPlaDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaPlaDocServiceImpl
        extends BaseServiceImpl<ConvocatoriaPlaDoc, Integer, ConvocatoriaPlaDocDAO>
        implements ConvocatoriaPlaDocService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaPlaDocDAO
     */
    @Autowired
    public ConvocatoriaPlaDocServiceImpl(
            @Qualifier("convocatoriaPlaDocDAO") ConvocatoriaPlaDocDAO convocatoriaPlaDocDAO) {
        super();
        this.setDao(convocatoriaPlaDocDAO);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public List<ConvocatoriaPlaDocDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaPlaDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaPlaDocDTO buscarId(Integer idConvocatoriaPlaDoc) throws ServiceException {
        return ConvocatoriaPlaDocToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaPlaDoc));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public List<ConvocatoriaPlaDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaPlaDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaPlaDocDTO guardar(ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO)
            throws ServiceException {
        return ConvocatoriaPlaDocToDTO.INSTANCE.apply(this
                .insertOrUpdate(ConvocatoriaPlaDocToEntity.INSTANCE.apply(convocatoriaPlaDocDTO)));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO)
            throws ServiceException {
        convocatoriaPlaDocDTO.setCdUsuBaja(null);
        convocatoriaPlaDocDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaPlaDocToEntity.INSTANCE.apply(convocatoriaPlaDocDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO)
            throws ServiceException {
        convocatoriaPlaDocDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaPlaDocDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaPlaDocToEntity.INSTANCE.apply(convocatoriaPlaDocDTO));
    }

    @Override
    @Transactional(readOnly = false)
    public List<ConvocatoriaPlaDocDTO> consultarDocuSubsanaPlantilla(Integer idConvocatoriaPla) throws ServiceException {
        List<AtlasFilter> filtrosEnt = new ArrayList<>();
      
        filtrosEnt.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_PLA,
                idConvocatoriaPla, Operador.EQUALS));
        filtrosEnt.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        return this.buscar(0, -1, filtrosEnt, null);
    }
}
