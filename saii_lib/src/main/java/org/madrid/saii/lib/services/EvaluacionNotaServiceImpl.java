package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionNotaDAO;
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaToEntity;
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
 * Implementación del servicio para la clase de dominio EvaluacionNota
 *
 * @author nttdata
 */
@Service("evaluacionNotaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionNotaServiceImpl
        extends BaseServiceImpl<EvaluacionNota, Integer, EvaluacionNotaDAO>
        implements EvaluacionNotaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionNotaDAO
     */
    @Autowired
    public EvaluacionNotaServiceImpl(
            @Qualifier("evaluacionNotaDAO") EvaluacionNotaDAO evaluacionNotaDAO) {
        super();
        this.setDao(evaluacionNotaDAO);
    }

    /**
     *
     */
    @Override
    public EvaluacionNotaDTO buscarId(Integer idEvaluacionNota) throws ServiceException {
        return EvaluacionNotaToDTO.INSTANCE.apply(this.getDao().find(idEvaluacionNota));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionNotaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionNotaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionNotaDTO guardar(EvaluacionNotaDTO evaluacionNotaDTO) throws ServiceException {
        return EvaluacionNotaToDTO.INSTANCE.apply(
                this.insertOrUpdate(EvaluacionNotaToEntity.INSTANCE.apply(evaluacionNotaDTO)));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionNotaDTO> obtenerPorEvaluacion(Integer idEvaluacion)
            throws ServiceException {
        if (idEvaluacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_ID_EVALUACION_FALTANTE);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION, idEvaluacion,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), EvaluacionNotaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionNotaDTO> obtenerListaRangoNotasPorDefecto(
            List<CatalogosValoresDTO> lstRangos, Integer idConvocatoria) throws ServiceException {

        List<EvaluacionNotaDTO> listaEvalNotaDTO = new ArrayList<>();

        lstRangos.forEach(e -> {
            EvaluacionNotaDTO evalNotaDTO = new EvaluacionNotaDTO();
            evalNotaDTO.setIdConvocatoria(idConvocatoria);
            evalNotaDTO.setNotaDTO(e);
            if (e.getCdCodigo().equals(Constantes.RangoNota.DEFICIENTE)) {
                evalNotaDTO.setNmDe(0);
                evalNotaDTO.setNmA(19);
            } else if (e.getCdCodigo().equals(Constantes.RangoNota.CUESTIONABLE)) {
                evalNotaDTO.setNmDe(20);
                evalNotaDTO.setNmA(49);
            } else if (e.getCdCodigo().equals(Constantes.RangoNota.ACEPTABLE)) {
                evalNotaDTO.setNmDe(50);
                evalNotaDTO.setNmA(69);
            } else if (e.getCdCodigo().equals(Constantes.RangoNota.BUENO)) {
                evalNotaDTO.setNmDe(70);
                evalNotaDTO.setNmA(79);
            } else if (e.getCdCodigo().equals(Constantes.RangoNota.EXCELENTE)) {
                evalNotaDTO.setNmDe(80);
                evalNotaDTO.setNmA(100);
            } else {
                evalNotaDTO.setNmDe(0);
                evalNotaDTO.setNmA(0);
            }
            evalNotaDTO.setEsBajaBd(Boolean.FALSE);
            listaEvalNotaDTO.add(evalNotaDTO);
        });

        return listaEvalNotaDTO;

    }

}
