package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.EvaluacionJustecApartaDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecNotaDAO;
import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecNotaToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecToEntity;
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
 * Implementación del servicio para la clase de dominio Evaluacion
 *
 * @author nttdata
 */
@Service("evaluacionJustecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionJustecServiceImpl
        extends BaseServiceImpl<EvaluacionJustec, Integer, EvaluacionJustecDAO>
        implements EvaluacionJustecService {

    @Autowired
    @Qualifier("evaluacionJustecNotaDAO")
    private EvaluacionJustecNotaDAO evaluacionJustecNotaDAO;

    @Autowired
    @Qualifier("evaluacionJustecApartaDAO")
    private EvaluacionJustecApartaDAO evaluacionJustecApartaDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionDAO
     */
    @Autowired
    public EvaluacionJustecServiceImpl(
            @Qualifier("evaluacionJustecDAO") EvaluacionJustecDAO evaluacionJustecDAO) {
        super();
        this.setDao(evaluacionJustecDAO);
    }

    /**
     *
     */
    @Override
    public EvaluacionJustecDTO buscarId(Integer idEvaluacion) throws ServiceException {
        return EvaluacionJustecToDTO.INSTANCE.apply(this.getDao().find(idEvaluacion));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionJustecToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionJustecDTO guardar(EvaluacionJustecDTO evaluacionJustecDTO)
            throws ServiceException {

        if (StringUtils.isBlank(evaluacionJustecDTO.getDsTitulo())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<EvaluacionJustecNotaDTO> lstNotaDTO = evaluacionJustecDTO
                .getListaEvaluacionJustecNotaDTO();
        evaluacionJustecDTO.setListaEvaluacionJustecNotaDTO(null);

        EvaluacionJustec evaluacion = this
                .insertOrUpdate(EvaluacionJustecToEntity.INSTANCE.apply(evaluacionJustecDTO));

        if (Objects.isNull(evaluacion) || Objects.isNull(evaluacion.getIdEvaluacionJustec())) {
            throw new ServiceException(Constantes.Error.ERROR_GENERAL);
        }

        EvaluacionJustecDTO evaluacionDTO = EvaluacionJustecToDTO.INSTANCE.apply(evaluacion);

        if (!lstNotaDTO.isEmpty()) {
            for (EvaluacionJustecNotaDTO notaDto : lstNotaDTO) {
                if (Objects.isNull(notaDto.getIdEvaluacionJustecNota())) {
                    notaDto.setIdEvaluacionJustec(evaluacionDTO.getIdEvaluacionJustec());
                    notaDto.setIdConvocatoria(evaluacionDTO.getIdConvocatoria());
                    EvaluacionJustecNota evaluacionNota = this.evaluacionJustecNotaDAO
                            .insert(EvaluacionJustecNotaToEntity.INSTANCE.apply(notaDto));
                    evaluacionDTO.getListaEvaluacionJustecNotaDTO()
                            .add(EvaluacionJustecNotaToDTO.INSTANCE.apply(evaluacionNota));
                } else {
                    if (Boolean.FALSE.equals(notaDto.getEsBajaBd())) {
                        EvaluacionJustecNota evaluacionNota = this.evaluacionJustecNotaDAO
                                .merge(EvaluacionJustecNotaToEntity.INSTANCE.apply(notaDto));
                        evaluacionDTO.getListaEvaluacionJustecNotaDTO()
                                .add(EvaluacionJustecNotaToDTO.INSTANCE.apply(evaluacionNota));
                    } else {
                        this.evaluacionJustecNotaDAO
                                .deleteLogico(notaDto.getIdEvaluacionJustecNota());
                    }
                }
            }
        }

        if (Constantes.AfirmacionNegacion.S.equals(evaluacion.getItCalificacion())) {

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION_JUSTEC,
                    evaluacion.getIdEvaluacionJustec(), Operador.EQUALS));
            List<EvaluacionJustecAparta> lstApartados = this.evaluacionJustecApartaDAO.find(filtros,
                    null);
            if (!lstApartados.isEmpty()) {
                for (EvaluacionJustecAparta apartado : lstApartados) {
                    apartado.setNmCalificacionMax(null);
                    this.evaluacionJustecApartaDAO.update(apartado);
                }
            }

        }

        return evaluacionDTO;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final EvaluacionJustecDTO evaluacionJustecDTO)
            throws ServiceException {

        EvaluacionJustec evaluacion = EvaluacionJustecToEntity.INSTANCE.apply(evaluacionJustecDTO);
        evaluacion.setCdUsuBaja(UserUtils.getUserName());
        evaluacion.setFcBajaFila(new Date());
        this.update(evaluacion);

        // consultar todas las notas asociadas a la evaluacion
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION_JUSTEC,
                evaluacion.getIdEvaluacionJustec(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<EvaluacionJustecNota> listaNotas = this.evaluacionJustecNotaDAO.find(filtros, null);

        for (EvaluacionJustecNota nota : listaNotas) {
            nota.setCdUsuBaja(UserUtils.getUserName());
            nota.setFcBajaFila(new Date());
            this.evaluacionJustecNotaDAO.update(nota);
        }

        List<EvaluacionJustecAparta> listaApartados = this.evaluacionJustecApartaDAO.find(filtros,
                null);
        for (EvaluacionJustecAparta apartado : listaApartados) {
            apartado.setCdUsuBaja(UserUtils.getUserName());
            apartado.setFcBajaFila(new Date());
            this.evaluacionJustecApartaDAO.update(apartado);
        }

    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecDTO> obtenerPorConvocatoria(Integer idConvocatoria,
            String itTipoEval) throws ServiceException {

        List<EvaluacionJustecDTO> lstEvaluaciones = new ArrayList<>();

        if (Objects.isNull(idConvocatoria) || StringUtils.isBlank(itTipoEval)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ITTIPOEVAL, itTipoEval, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

        List<EvaluacionJustec> lstEvaluacion = this.getDao().find(filtros, lisOrden);

        if (Objects.nonNull(lstEvaluacion)) {

            for (EvaluacionJustec eva : lstEvaluacion) {

                List<EvaluacionJustecNota> lstNotaSinNull = eva.getListaEvaluacionJustecNota()
                        .stream().filter(p -> Objects.nonNull(p.getNmDe())
                                && Objects.nonNull(p.getNmA()) && p.getFcBajaFila() == null)
                        .collect(Collectors.toList());
                lstNotaSinNull.sort(Comparator.comparing(EvaluacionJustecNota::getNmDe));

                List<EvaluacionJustecNota> lstNotaConNull = eva.getListaEvaluacionJustecNota()
                        .stream()
                        .filter(p -> (Objects.isNull(p.getNmDe()) || Objects.isNull(p.getNmA()))
                                && p.getFcBajaFila() == null)
                        .collect(Collectors.toList());

                List<EvaluacionJustecNota> listaOrdenada = new ArrayList<>();
                listaOrdenada.addAll(lstNotaSinNull);
                listaOrdenada.addAll(lstNotaConNull);

                eva.setListaEvaluacionJustecNota(listaOrdenada);

            }

            lstEvaluaciones = new ArrayList<>(
                    Lists.transform(lstEvaluacion, EvaluacionJustecToDTO.INSTANCE));

        }

        return lstEvaluaciones;
    }
    
    
    /**
    *
    */
   @Override
   public List<EvaluacionJustec> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException {

       List<EvaluacionJustec> lstEvaluacionJustec = new ArrayList<>();

       if (Objects.isNull(idConvocatoria)) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
       }

       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       List<AtlasSort> lisOrden = new ArrayList<>();
       lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

       lstEvaluacionJustec = this.getDao().find(filtros, lisOrden);

       return lstEvaluacionJustec;
   }

}
