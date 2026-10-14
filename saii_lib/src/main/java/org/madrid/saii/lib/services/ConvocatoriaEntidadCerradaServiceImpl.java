package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEntidadCerradaDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEntidadCerradaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEntidadCerradaToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio
 * ConvocatoriaEntidadCerrada
 *
 * @author quental
 */
@Service("convocatoriaEntidadCerradaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaEntidadCerradaServiceImpl
        extends BaseServiceImpl<ConvocatoriaEntidadCerrada, Integer, ConvocatoriaEntidadCerradaDAO>
        implements ConvocatoriaEntidadCerradaService {
    
    /**
     * 
     */
    public ConvocatoriaEntidadCerradaServiceImpl(@Qualifier("convocatoriaEntidadCerradaDAO")
    ConvocatoriaEntidadCerradaDAO convocatoriaEntidadCerradaDAO) {
        super();
        this.setDao(convocatoriaEntidadCerradaDAO);
    }

    @Override
    public List<ConvocatoriaEntidadCerradaDTO> buscarIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        return this.getDao().obtenerPorConvocatoria(idConvocatoria);
    }

    @Override
    public ConvocatoriaEntidadCerradaDTO buscarIdCatalogoValores(Integer idCatalogoValor,
            Integer idConvocatoria) throws ServiceException {
        ConvocatoriaEntidadCerradaDTO convEntCer = new ConvocatoriaEntidadCerradaDTO();
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter("entidadCerrada.idValor", idCatalogoValor, Operador.EQUALS));
        List<ConvocatoriaEntidadCerradaDTO> lstEntCer = new ArrayList<>(Lists
                .transform(this.find(filtros, null), ConvocatoriaEntidadCerradaToDTO.INSTANCE));
        if (Boolean.FALSE.equals(lstEntCer.isEmpty())) {
            convEntCer = lstEntCer.get(0);
        }
        return convEntCer;
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEntidadCerradaDTO guardar(
            ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO) throws ServiceException {
        return ConvocatoriaEntidadCerradaToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaEntidadCerradaToEntity.INSTANCE.apply(convocatoriaEntidadCerradaDTO)));
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEntidadCerradaDTO altaLogica(
            ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO) throws ServiceException {
        convocatoriaEntidadCerradaDTO.setCdUsuBaja(null);
        convocatoriaEntidadCerradaDTO.setFcBajaFila(null);
        return ConvocatoriaEntidadCerradaToDTO.INSTANCE.apply(this.update(
                ConvocatoriaEntidadCerradaToEntity.INSTANCE.apply(convocatoriaEntidadCerradaDTO)));
    }

    /**
     * Brrado lógico de una entidad cerrada
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEntidadCerradaDTO borradoLogico(
            ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO) throws ServiceException {
        convocatoriaEntidadCerradaDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaEntidadCerradaDTO.setFcBajaFila(new Date());
        return ConvocatoriaEntidadCerradaToDTO.INSTANCE.apply(this.update(
                ConvocatoriaEntidadCerradaToEntity.INSTANCE.apply(convocatoriaEntidadCerradaDTO)));
    }

}
