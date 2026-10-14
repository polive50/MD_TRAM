package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCauToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCauToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaCau
 *
 * @author everis
 */
@Service("convocatoriaCauService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaCauServiceImpl
        extends BaseServiceImpl<ConvocatoriaCau, Integer, ConvocatoriaCauDAO>
        implements ConvocatoriaCauService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCauDAO
     */
    @Autowired
    public ConvocatoriaCauServiceImpl(
            @Qualifier("convocatoriaCauDAO") ConvocatoriaCauDAO convocatoriaCauDAO) {
        super();
        this.setDao(convocatoriaCauDAO);
    }

    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     *
     */
    @Override
    public ConvocatoriaCauDTO buscarId(Integer idConvocatoriaCau) throws ServiceException {
        return ConvocatoriaCauToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCau));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaCauDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaCauToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauDTO agregar(Integer idConvocatoria, Integer idCausa)
            throws ServiceException {

        ConvocatoriaCau convCau = new ConvocatoriaCau();

        ConvocatoriaCau convCauExist = this.buscarIdConvocatoriaCau(idConvocatoria, idCausa);

        if (convCauExist == null || convCauExist.getIdConvocatoriaCau() == null) {

            CatalogosValores causa = this.catalogosValoresDAO.find(idCausa);

            if (causa != null) {
                convCau.setCausa(causa);
                convCau.setIdConvocatoria(idConvocatoria);
                convCau = this.insertOrUpdate(convCau);
            }

        } else if (convCauExist.getFcBajaFila() != null) {
            convCauExist.setCdUsuBaja(null);
            convCauExist.setFcBajaFila(null);
            convCau = this.insertOrUpdate(convCauExist);
        } else if (convCauExist.getFcBajaFila() == null) {
            throw new ServiceException(Constantes.Error.ERROR_CAUSA_DENEGACION_DUPLICADA, null,
                    Constantes.Error.MSG_WARNING);
        }
        return ConvocatoriaCauToDTO.INSTANCE.apply(convCau);
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauDTO actualizarNormativa(Integer idConvocatoria, Integer idCausa,
            String tlNormativa) throws ServiceException {

        ConvocatoriaCau convCauExist = this.buscarIdConvocatoriaCau(idConvocatoria, idCausa);

        if (Boolean.TRUE.equals(convCauExist != null)) {

            convCauExist.setTlNormativa(tlNormativa);
            convCauExist = this.insertOrUpdate(convCauExist);
        }

        return ConvocatoriaCauToDTO.INSTANCE.apply(convCauExist);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauDTO borradoLogico(final ConvocatoriaCauDTO convocatoriaCauDTO)
            throws ServiceException {
        // No eliminar causa de denegación 'Otros'
        if (Constantes.CausaDenegacion.CODIGO_OTROS
                .equals(convocatoriaCauDTO.getCausaDTO().getCdCodigo())) {
            throw new ServiceException(Constantes.CausaDenegacion.ERROR_ELIMINAR_OTROS, null,
                    Constantes.Error.MSG_WARNING);
        }
        convocatoriaCauDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaCauDTO.setFcBajaFila(new Date());
        return ConvocatoriaCauToDTO.INSTANCE
                .apply(this.update(ConvocatoriaCauToEntity.INSTANCE.apply(convocatoriaCauDTO)));
    }

    /**
     * Buscar por Id convocatoria y id causa de denegación
     */
    private ConvocatoriaCau buscarIdConvocatoriaCau(final Integer idConvocatoria,
            final Integer idCausa) throws ServiceException {

        ConvocatoriaCau convocatoriaCau = new ConvocatoriaCau();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION, idCausa,
                Operador.EQUALS));
        List<ConvocatoriaCau> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCau = lstConvocatoriaCau.get(0);
        }
        return convocatoriaCau;
    }
    
    /**
     * Verifica la existencia de un causa
     *
     * @param idConvocatoria
     * @param idCausa
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaCauDTO buscarIdConvocatoriaCauDTO(final Integer idConvocatoria,
            final Integer idCausa) throws ServiceException {

        ConvocatoriaCauDTO convocatoriaCau = new ConvocatoriaCauDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION,
                idCausa, Operador.EQUALS));

        List<ConvocatoriaCau> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCau = ConvocatoriaCauToDTO.INSTANCE.apply( lstConvocatoriaCau.get(0));
        }
        return convocatoriaCau;
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauDTO agregar(Integer idConvocatoria, Integer idCausa, Integer idTipoCausa)
            throws ServiceException {
        ConvocatoriaCau convCau = new ConvocatoriaCau();

        ConvocatoriaCau convCauExist = this.buscarIdConvocatoriaCauTipoCausa(idConvocatoria,
                idCausa, idTipoCausa);

        if (convCauExist == null || convCauExist.getIdConvocatoriaCau() == null) {

            CatalogosValores causa = this.catalogosValoresDAO.find(idCausa);
            CatalogosValores tipoCausa = this.catalogosValoresDAO.find(idTipoCausa);

            if (causa != null) {
                convCau.setCausa(causa);
                convCau.setIdConvocatoria(idConvocatoria);
                convCau.setTipoCausa(tipoCausa);
                convCau = this.insertOrUpdate(convCau);
            }

        } else if (convCauExist.getFcBajaFila() != null) {
            convCauExist.setCdUsuBaja(null);
            convCauExist.setFcBajaFila(null);
            convCau = this.insertOrUpdate(convCauExist);
        } else if (convCauExist.getFcBajaFila() == null) {
            throw new ServiceException(Constantes.Error.ERROR_CAUSA_DENEGACION_DUPLICADA, null,
                    Constantes.Error.MSG_WARNING);
        }
        return ConvocatoriaCauToDTO.INSTANCE.apply(convCau);
    }
    
    /**
     * Buscar por Id convocatoria y id causa de denegación
     */
    private ConvocatoriaCau buscarIdConvocatoriaCauTipoCausa(final Integer idConvocatoria,
            final Integer idCausa, final Integer idTipoCausa) throws ServiceException {

        ConvocatoriaCau convocatoriaCau = new ConvocatoriaCau();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION, idCausa,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_CAUSA_DENEGACION_IDVALOR,
                idTipoCausa));
        List<ConvocatoriaCau> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCau = lstConvocatoriaCau.get(0);
        }
        return convocatoriaCau;
    }

}
