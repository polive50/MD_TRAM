package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEurDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.dto.ConvocatoriaEurDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEurToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEurToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaEur
 *
 * @author everis
 */
@Service("convocatoriaEurService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaEurServiceImpl
        extends BaseServiceImpl<ConvocatoriaEur, Integer, ConvocatoriaEurDAO>
        implements ConvocatoriaEurService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaEurDAO
     */
    @Autowired
    public ConvocatoriaEurServiceImpl(
            @Qualifier("convocatoriaEurDAO") ConvocatoriaEurDAO convocatoriaEurDAO) {
        super();
        this.setDao(convocatoriaEurDAO);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaEurDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaEurToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaEurDTO buscarId(Integer idConvocatoriaEur) throws ServiceException {
        return ConvocatoriaEurToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaEur));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaEurDTO buscarIdCatalogoValores(Integer idCatalogoValor,
            Integer idConvocatoria) throws ServiceException {

        ConvocatoriaEurDTO convEurDTO = new ConvocatoriaEurDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter("convocatoria.idConvocatoria", idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter("fondoEuropeo.idValor", idCatalogoValor, Operador.EQUALS));

        List<ConvocatoriaEurDTO> lstConvEur = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaEurToDTO.INSTANCE));

        if (Boolean.FALSE.equals(lstConvEur.isEmpty())) {
            convEurDTO = lstConvEur.get(0);
        }

        return convEurDTO;

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaEurDTO> buscarIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<ConvocatoriaEurDTO> lstConvEur;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter("convocatoria.idConvocatoria", idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        lstConvEur = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaEurToDTO.INSTANCE));

        return lstConvEur;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaEurDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaEurToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEurDTO guardar(ConvocatoriaEurDTO convocatoriaEurDTO)
            throws ServiceException {
        return ConvocatoriaEurToDTO.INSTANCE.apply(
                this.insertOrUpdate(ConvocatoriaEurToEntity.INSTANCE.apply(convocatoriaEurDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEurDTO altaLogica(final ConvocatoriaEurDTO convocatoriaEurDTO)
            throws ServiceException {
        convocatoriaEurDTO.setCdUsuBaja(null);
        convocatoriaEurDTO.setFcBajaFila(null);
        return ConvocatoriaEurToDTO.INSTANCE
                .apply(this.update(ConvocatoriaEurToEntity.INSTANCE.apply(convocatoriaEurDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEurDTO borradoLogico(final ConvocatoriaEurDTO convocatoriaEurDTO)
            throws ServiceException {
        convocatoriaEurDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaEurDTO.setFcBajaFila(new Date());
        return ConvocatoriaEurToDTO.INSTANCE
                .apply(this.update(ConvocatoriaEurToEntity.INSTANCE.apply(convocatoriaEurDTO)));
    }

}
