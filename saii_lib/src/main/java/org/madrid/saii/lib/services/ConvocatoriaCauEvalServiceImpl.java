package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauEvalDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCauEvalToDTO;
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

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaCauEval
 *
 * @author nttdata
 */
@Service("convocatoriaCauEvalService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaCauEvalServiceImpl
        extends BaseServiceImpl<ConvocatoriaCauEval, Integer, ConvocatoriaCauEvalDAO>
        implements ConvocatoriaCauEvalService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCauEvalDAO
     */
    @Autowired
    public ConvocatoriaCauEvalServiceImpl(
            @Qualifier("convocatoriaCauEvalDAO") ConvocatoriaCauEvalDAO convocatoriaCauEvalDAO) {
        super();
        this.setDao(convocatoriaCauEvalDAO);
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
    public ConvocatoriaCauEvalDTO buscarId(Integer idConvocatoriaCauEval) throws ServiceException {
        return ConvocatoriaCauEvalToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCauEval));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaCauEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaCauEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCauEvalDTO agregar(Integer idConvocatoria, Integer idCausaEval)
            throws ServiceException {
        if (Objects.isNull(idConvocatoria) || Objects.isNull(idCausaEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ConvocatoriaCauEval convCauEval = new ConvocatoriaCauEval();
        ConvocatoriaCauEval convCauEvalExist = this.buscarIdConvocatoriaCau(idConvocatoria,
                idCausaEval);

        if (Objects.isNull(convCauEvalExist)
                || Objects.isNull(convCauEvalExist.getIdConvocatoriaCauEval())) {

            CatalogosValores causaEval = this.catalogosValoresDAO.find(idCausaEval);

            if (Objects.nonNull(causaEval)) {
                convCauEval.setCausaEval(causaEval);
                convCauEval.setIdConvocatoria(idConvocatoria);
                convCauEval = this.insertOrUpdate(convCauEval);
            }

        } else if (Objects.nonNull(convCauEvalExist.getFcBajaFila())) {
            convCauEvalExist.setCdUsuBaja(null);
            convCauEvalExist.setFcBajaFila(null);
            convCauEval = this.insertOrUpdate(convCauEvalExist);
        } else if (Objects.isNull(convCauEvalExist.getFcBajaFila())) {
            throw new ServiceException(Constantes.Error.ERROR_CAUSA_DENEGACION_DUPLICADA, null,
                    Constantes.Error.MSG_WARNING);
        }

        return ConvocatoriaCauEvalToDTO.INSTANCE.apply(convCauEval);
    }

    /**
     * Verifica la existencia de un causa de evaluacion
     *
     * @param idConvocatoria
     * @param idCausaEval
     * @return
     * @throws ServiceException
     */
    private ConvocatoriaCauEval buscarIdConvocatoriaCau(final Integer idConvocatoria,
            final Integer idCausaEval) throws ServiceException {

        ConvocatoriaCauEval convocatoriaCauEval = new ConvocatoriaCauEval();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION_EVAL,
                idCausaEval, Operador.EQUALS));

        List<ConvocatoriaCauEval> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCauEval = lstConvocatoriaCau.get(0);
        }
        return convocatoriaCauEval;
    }
    
    
    /**
     * Verifica la existencia de un causa de evaluacion
     *
     * @param idConvocatoria
     * @param idCausaEval
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaCauEvalDTO buscarIdConvocatoriaCauDTO(final Integer idConvocatoria,
            final Integer idCausaEval) throws ServiceException {

        ConvocatoriaCauEvalDTO convocatoriaCauEval = new ConvocatoriaCauEvalDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION_EVAL,
                idCausaEval, Operador.EQUALS));

        List<ConvocatoriaCauEval> lstConvocatoriaCau = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaCau.isEmpty()) {
            convocatoriaCauEval = ConvocatoriaCauEvalToDTO.INSTANCE.apply( lstConvocatoriaCau.get(0));
        }
        return convocatoriaCauEval;
    }
    
}
