package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.EvaluacionJustecNotaDatoDAO;
import org.madrid.saii.lib.dao.EvaluacionNotaDatoDAO;
import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaDatoToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaDatoToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaDatoToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaDatoToEntity;
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
 * Implementación del servicio para la clase de dominio EvaluacionNotaDato
 *
 * @author nttdata
 */
@Service("evaluacionJustecNotaDatoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionJustecNotaDatoServiceImpl
        extends BaseServiceImpl<EvaluacionJustecNotaDato, Integer, EvaluacionJustecNotaDatoDAO>
        implements EvaluacionJustecNotaDatoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionNotaDatoDAO
     */
    @Autowired
    public EvaluacionJustecNotaDatoServiceImpl(
            @Qualifier("evaluacionJustecNotaDatoDAO") EvaluacionJustecNotaDatoDAO evaluacionNotaDatoDAO) {
        super();
        this.setDao(evaluacionNotaDatoDAO);
    }

    /**
     * Constructor Vacio
     */
    public EvaluacionJustecNotaDatoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecNotaDatoDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), EvaluacionJustecNotaDatoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public EvaluacionJustecNotaDatoDTO buscarId(Integer idEvaluacionNotaDato) throws ServiceException {
        return EvaluacionJustecNotaDatoToDTO.INSTANCE.apply(this.getDao().find(idEvaluacionNotaDato));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecNotaDatoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionJustecNotaDatoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final EvaluacionJustecNotaDatoDTO evaluacionNotaDatoDTO)
            throws ServiceException {
        evaluacionNotaDatoDTO.setCdUsuBaja(null);
        evaluacionNotaDatoDTO.setFcBajaFila(null);
        this.getDao().update(EvaluacionJustecNotaDatoToEntity.INSTANCE.apply(evaluacionNotaDatoDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final EvaluacionJustecNotaDatoDTO evaluacionNotaDatoDTO)
            throws ServiceException {
        evaluacionNotaDatoDTO.setCdUsuBaja(UserUtils.getUserName());
        evaluacionNotaDatoDTO.setFcBajaFila(new Date());
        this.getDao().update(EvaluacionJustecNotaDatoToEntity.INSTANCE.apply(evaluacionNotaDatoDTO));
    }

    @Override
    public List<EvaluacionJustecNotaDatoDTO> obtenerEvaluacionJustecNotaDatoPorIdExpedienteEvalJustec(
            Integer idExpedienteEvalJustec) throws ServiceException {
        if (Objects.isNull(idExpedienteEvalJustec)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE_APARTADO_JTC_DATO, idExpedienteEvalJustec,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EVALUACION_JUSTEC_NOTA_DATO, true));

        return new ArrayList<>(Lists.transform(this.getDao().find(filtros, orden),
                EvaluacionJustecNotaDatoToDTO.INSTANCE));
    }

    @Override
    public List<EvaluacionJustecNotaDatoDTO> obtenerEvaluacionJustecNotaDatoPorIdEvalIdExpEvalJustec(
            Integer idExpedienteEvalJustec, Integer idEvaluacionJustec) throws ServiceException {

        if (Objects.isNull(idExpedienteEvalJustec)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE_APARTADO_JTC_DATO, idExpedienteEvalJustec,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION_JUSTEC, idEvaluacionJustec,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EVALUACION_JUSTEC_NOTA_DATO, true));

        return new ArrayList<>(Lists.transform(this.getDao().find(filtros, orden),
                EvaluacionJustecNotaDatoToDTO.INSTANCE));
    }

}
