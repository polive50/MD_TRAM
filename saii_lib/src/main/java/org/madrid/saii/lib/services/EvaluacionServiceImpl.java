package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ApartadoDAO;
import org.madrid.saii.lib.dao.EvaluacionDAO;
import org.madrid.saii.lib.dao.EvaluacionNotaDAO;
import org.madrid.saii.lib.dao.InformeDAO;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionNotaToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionToEntity;
import org.madrid.saii.lib.dto.converters.InformeToDTO;
import org.madrid.saii.lib.dto.converters.InformeToEntity;
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
@Service("evaluacionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionServiceImpl extends BaseServiceImpl<Evaluacion, Integer, EvaluacionDAO>
        implements EvaluacionService {

    @Autowired
    @Qualifier("evaluacionNotaDAO")
    private EvaluacionNotaDAO evaluacionNotaDAO;

    @Autowired
    @Qualifier("apartadoDAO")
    private ApartadoDAO apartadoDAO;
    
    @Autowired
    @Qualifier("informeDAO")
    private InformeDAO informeDAO;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionDAO
     */
    @Autowired
    public EvaluacionServiceImpl(@Qualifier("evaluacionDAO") EvaluacionDAO evaluacionDAO) {
        super();
        this.setDao(evaluacionDAO);
    }

    /**
     *
     */
    @Override
    public EvaluacionDTO buscarId(Integer idEvaluacion) throws ServiceException {
        return EvaluacionToDTO.INSTANCE.apply(this.getDao().find(idEvaluacion));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionToDTO.INSTANCE));
    }
    
    /**
    *
    */
//   @Override
//   @Transactional(readOnly = false)
//   public EvaluacionDTO guardar(EvaluacionDTO evaluacionDTO) throws ServiceException {
//
//       if (StringUtils.isBlank(evaluacionDTO.getDsTitulo())) {
//           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
//       }
//
//       List<EvaluacionNotaDTO> lstNotaDTO = evaluacionDTO.getListaEvaluacionNotaDTO();
//       evaluacionDTO.setListaEvaluacionNotaDTO(null);
//
//       Evaluacion evaluacion = this
//               .insertOrUpdate(EvaluacionToEntity.INSTANCE.apply(evaluacionDTO));
//
//       if (Objects.isNull(evaluacion) || Objects.isNull(evaluacion.getIdEvaluacion())) {
//           throw new ServiceException(Constantes.Error.ERROR_GENERAL);
//       }
//
//       EvaluacionDTO evaluacionDto = EvaluacionToDTO.INSTANCE.apply(evaluacion);
//
//       if (!lstNotaDTO.isEmpty()) {
//           for (EvaluacionNotaDTO notaDto : lstNotaDTO) {
//               if (Objects.isNull(notaDto.getIdEvaluacionNota())) {
//                   notaDto.setIdEvaluacion(evaluacionDto.getIdEvaluacion());
//                   notaDto.setIdConvocatoria(evaluacionDto.getIdConvocatoria());
//                   EvaluacionNota evaluacionNota = this.evaluacionNotaDAO
//                           .insert(EvaluacionNotaToEntity.INSTANCE.apply(notaDto));
//                   evaluacionDto.getListaEvaluacionNotaDTO()
//                           .add(EvaluacionNotaToDTO.INSTANCE.apply(evaluacionNota));
//               } else {
//                   if (Boolean.FALSE.equals(notaDto.getEsBajaBd())) {
//                       EvaluacionNota evaluacionNota = this.evaluacionNotaDAO
//                               .merge(EvaluacionNotaToEntity.INSTANCE.apply(notaDto));
//                       evaluacionDto.getListaEvaluacionNotaDTO()
//                               .add(EvaluacionNotaToDTO.INSTANCE.apply(evaluacionNota));
//                   } else {
//                       this.evaluacionNotaDAO.deleteLogico(notaDto.getIdEvaluacionNota());
//                   }
//               }
//           }
//       }
//
//       if (Constantes.AfirmacionNegacion.S.equals(evaluacion.getItCalificacion())) {
//
//           List<AtlasFilter> filtros = new ArrayList<>();
//           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION,
//                   evaluacion.getIdEvaluacion(), Operador.EQUALS));
//           List<Apartado> lstApartados = this.apartadoDAO.find(filtros, null);
//           if (!lstApartados.isEmpty()) {
//               for (Apartado apartado : lstApartados) {
//                   apartado.setNmCalificacionMax(null);
//                   this.apartadoDAO.update(apartado);
//               }
//           }
//
//       }
//
//       return evaluacionDto;
//
//   }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionDTO guardar(EvaluacionDTO evaluacionDTO, Integer idConvocatoria) throws ServiceException {

        if (StringUtils.isBlank(evaluacionDTO.getDsTitulo())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        
        List<EvaluacionNotaDTO> lstNotaDTO = evaluacionDTO.getListaEvaluacionNotaDTO();
        evaluacionDTO.setListaEvaluacionNotaDTO(null);
        
        Evaluacion evaluacion = this
                .insertOrUpdate(EvaluacionToEntity.INSTANCE.apply(evaluacionDTO));

        if (Objects.isNull(evaluacion) || Objects.isNull(evaluacion.getIdEvaluacion())) {
            throw new ServiceException(Constantes.Error.ERROR_GENERAL);
        }

        EvaluacionDTO evaluacionDto = EvaluacionToDTO.INSTANCE.apply(evaluacion);

        if (!lstNotaDTO.isEmpty()) {
            for (EvaluacionNotaDTO notaDto : lstNotaDTO) {
                if (Objects.isNull(notaDto.getIdEvaluacionNota())) {
                    notaDto.setIdEvaluacion(evaluacionDto.getIdEvaluacion());
//                    notaDto.setIdConvocatoria(evaluacionDto.getIdConvocatoria());
                    notaDto.setIdConvocatoria(idConvocatoria);
                    EvaluacionNota evaluacionNota = this.evaluacionNotaDAO
                            .insert(EvaluacionNotaToEntity.INSTANCE.apply(notaDto));
                    evaluacionDto.getListaEvaluacionNotaDTO()
                            .add(EvaluacionNotaToDTO.INSTANCE.apply(evaluacionNota));
                } else {
                    if (Boolean.FALSE.equals(notaDto.getEsBajaBd())) {
                        EvaluacionNota evaluacionNota = this.evaluacionNotaDAO
                                .merge(EvaluacionNotaToEntity.INSTANCE.apply(notaDto));
                        evaluacionDto.getListaEvaluacionNotaDTO()
                                .add(EvaluacionNotaToDTO.INSTANCE.apply(evaluacionNota));
                    } else {
                        this.evaluacionNotaDAO.deleteLogico(notaDto.getIdEvaluacionNota());
                    }
                }
            }
        }

        if (Constantes.AfirmacionNegacion.S.equals(evaluacion.getItCalificacion())) {
//             MD-2972 GIM quitamos esto para que no nos quiote la calificacion maxima cada vez que guardamos
//            List<AtlasFilter> filtros = new ArrayList<>();
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION,
//                    evaluacion.getIdEvaluacion(), Operador.EQUALS));
//            List<Apartado> lstApartados = this.apartadoDAO.find(filtros, null);
//            if (!lstApartados.isEmpty()) {
//                for (Apartado apartado : lstApartados) {
//                    apartado.setNmCalificacionMax(null);
//                    this.apartadoDAO.update(apartado);
//                }
//            }

        }

        return evaluacionDto;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final EvaluacionDTO evaluacionDTO) throws ServiceException {

        Evaluacion evaluacion = EvaluacionToEntity.INSTANCE.apply(evaluacionDTO);
        evaluacion.setCdUsuBaja(UserUtils.getUserName());
        evaluacion.setFcBajaFila(new Date());
        this.update(evaluacion);

        // consultar todas las notas asociadas a la evaluacion
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION,
                evaluacion.getIdEvaluacion(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<EvaluacionNota> listaNotas = this.evaluacionNotaDAO.find(filtros, null);

        for (EvaluacionNota nota : listaNotas) {
            nota.setCdUsuBaja(UserUtils.getUserName());
            nota.setFcBajaFila(new Date());
            this.evaluacionNotaDAO.update(nota);
        }

        List<Apartado> listaApartados = this.apartadoDAO.find(filtros, null);
        for (Apartado apartado : listaApartados) {
            apartado.setCdUsuBaja(UserUtils.getUserName());
            apartado.setFcBajaFila(new Date());
            this.apartadoDAO.update(apartado);
        }
        
        // Borrado lógico del registro INFORME asociado si no tiene más EVALUACIONES
//        List<Evaluacion> lstEvaluacion = this
//                .buscarPorInforme(evaluacionDTO.getInformeDTO().getIdInforme());
//        if (lstEvaluacion == null || lstEvaluacion.isEmpty()) {
//            Informe informe = InformeToEntity.INSTANCE.apply(evaluacionDTO.getInformeDTO());
//            informe.setCdUsuBaja(UserUtils.getUserName());
//            informe.setFcBajaFila(new Date());
//            this.informeDAO.update(informe);
//        }

    }
    
    /**
    *
    */
   @Override
   public List<EvaluacionDTO> obtenerPorConvocatoria(Integer idConvocatoria, String itCoorEval)
           throws ServiceException {

       List<EvaluacionDTO> lstEvaluaciones = new ArrayList<>();

       if (Objects.isNull(idConvocatoria) || StringUtils.isBlank(itCoorEval)) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
       }

       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(
               new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       List<AtlasSort> lisOrden = new ArrayList<>();
       lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

       List<Evaluacion> lstEvaluacion = this.getDao().find(filtros, lisOrden);

       if (Objects.nonNull(lstEvaluacion)) {

           for (Evaluacion eva : lstEvaluacion) {

               List<EvaluacionNota> lstNotaSinNull = eva
                       .getListaEvaluacionNota().stream().filter(p -> Objects.nonNull(p.getNmDe())
                               && Objects.nonNull(p.getNmA()) && p.getFcBajaFila() == null)
                       .collect(Collectors.toList());
               lstNotaSinNull.sort(Comparator.comparing(EvaluacionNota::getNmDe));

               List<EvaluacionNota> lstNotaConNull = eva.getListaEvaluacionNota().stream()
                       .filter(p -> (Objects.isNull(p.getNmDe()) || Objects.isNull(p.getNmA()))
                               && p.getFcBajaFila() == null)
                       .collect(Collectors.toList());

               List<EvaluacionNota> listaOrdenada = new ArrayList<>();
               listaOrdenada.addAll(lstNotaSinNull);
               listaOrdenada.addAll(lstNotaConNull);

               eva.setListaEvaluacionNota(listaOrdenada);

           }

           lstEvaluaciones = new ArrayList<>(
                   Lists.transform(lstEvaluacion, EvaluacionToDTO.INSTANCE));

       }

       return lstEvaluaciones;
   }
   
   /**
   *
   */
  @Override
  public List<EvaluacionDTO> obtenerPorInforme( InformeDTO informe)
          throws ServiceException {

      List<EvaluacionDTO> lstEvaluaciones = new ArrayList<>();

      if ( Objects.isNull(informe) || Objects.isNull(informe.getIdInforme())) {
          throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
      }

      List<AtlasFilter> filtros = new ArrayList<>();
//      filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
//              Operador.EQUALS));
      
//      filtros.add(
//              new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
      
      filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INFORME_ID_INFORME, informe.getIdInforme(),
              Operador.EQUALS));
      
      filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

      List<AtlasSort> lisOrden = new ArrayList<>();
      lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

      List<Evaluacion> lstEvaluacion = this.getDao().find(filtros, lisOrden);

      if (Objects.nonNull(lstEvaluacion)) {

          for (Evaluacion eva : lstEvaluacion) {

              List<EvaluacionNota> lstNotaSinNull = eva
                      .getListaEvaluacionNota().stream().filter(p -> Objects.nonNull(p.getNmDe())
                              && Objects.nonNull(p.getNmA()) && p.getFcBajaFila() == null)
                      .collect(Collectors.toList());
              lstNotaSinNull.sort(Comparator.comparing(EvaluacionNota::getNmDe));

              List<EvaluacionNota> lstNotaConNull = eva.getListaEvaluacionNota().stream()
                      .filter(p -> (Objects.isNull(p.getNmDe()) || Objects.isNull(p.getNmA()))
                              && p.getFcBajaFila() == null)
                      .collect(Collectors.toList());

              List<EvaluacionNota> listaOrdenada = new ArrayList<>();
              listaOrdenada.addAll(lstNotaSinNull);
              listaOrdenada.addAll(lstNotaConNull);

              eva.setListaEvaluacionNota(listaOrdenada);

          }

          lstEvaluaciones = new ArrayList<>(
                  Lists.transform(lstEvaluacion, EvaluacionToDTO.INSTANCE));

      }

      return lstEvaluaciones;
  }
    
  /**
  *
  */
 @Override
 public List<EvaluacionDTO> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException {

     List<EvaluacionDTO> lstEvaluaciones = new ArrayList<>();

     if (Objects.isNull(idConvocatoria)) {
         throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
     }

     List<AtlasFilter> filtros = new ArrayList<>();
//     filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
//             Operador.EQUALS));

     filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

     List<AtlasSort> lisOrden = new ArrayList<>();
     lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

     List<Evaluacion> lstEvaluacion = this.getDao().find(filtros, lisOrden);

     if (Objects.nonNull(lstEvaluacion)) {

         for (Evaluacion eva : lstEvaluacion) {

             List<EvaluacionNota> lstNotaSinNull = eva
                     .getListaEvaluacionNota().stream().filter(p -> Objects.nonNull(p.getNmDe())
                             && Objects.nonNull(p.getNmA()) && p.getFcBajaFila() == null)
                     .collect(Collectors.toList());
             lstNotaSinNull.sort(Comparator.comparing(EvaluacionNota::getNmDe));

             List<EvaluacionNota> lstNotaConNull = eva.getListaEvaluacionNota().stream()
                     .filter(p -> (Objects.isNull(p.getNmDe()) || Objects.isNull(p.getNmA()))
                             && p.getFcBajaFila() == null)
                     .collect(Collectors.toList());

             List<EvaluacionNota> listaOrdenada = new ArrayList<>();
             listaOrdenada.addAll(lstNotaSinNull);
             listaOrdenada.addAll(lstNotaConNull);

             eva.setListaEvaluacionNota(listaOrdenada);

         }

         lstEvaluaciones = new ArrayList<>(
                 Lists.transform(lstEvaluacion, EvaluacionToDTO.INSTANCE));

     }

     return lstEvaluaciones;
 }

  
    @Override
    public List<Evaluacion> buscarPorInforme(Integer idInforme) throws ServiceException {
        List<Evaluacion> lstEvaluacion = new ArrayList<>();
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INFORME_ID_INFORME, idInforme,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        lstEvaluacion = this.getDao().find(filtros, null);
        return lstEvaluacion;
    }

    @Override
    public List<EvaluacionDTO> obtenerPorConvocatoria(Integer idConvocatoria, Integer idTipoInforme)
            throws ServiceException {
        List<EvaluacionDTO> lstEvaluaciones = new ArrayList<>();

        if ( Objects.isNull(idTipoInforme)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INFORME_ID_CONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        
//        filtros.add(
//                new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval, Operador.EQUALS));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INFORME_TIPO_INFORME_ID_VALOR, idTipoInforme,
                Operador.EQUALS));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

        List<Evaluacion> lstEvaluacion = this.getDao().find(filtros, lisOrden);

        if (Objects.nonNull(lstEvaluacion)) {

            for (Evaluacion eva : lstEvaluacion) {

                List<EvaluacionNota> lstNotaSinNull = eva
                        .getListaEvaluacionNota().stream().filter(p -> Objects.nonNull(p.getNmDe())
                                && Objects.nonNull(p.getNmA()) && p.getFcBajaFila() == null)
                        .collect(Collectors.toList());
                lstNotaSinNull.sort(Comparator.comparing(EvaluacionNota::getNmDe));

                List<EvaluacionNota> lstNotaConNull = eva.getListaEvaluacionNota().stream()
                        .filter(p -> (Objects.isNull(p.getNmDe()) || Objects.isNull(p.getNmA()))
                                && p.getFcBajaFila() == null)
                        .collect(Collectors.toList());

                List<EvaluacionNota> listaOrdenada = new ArrayList<>();
                listaOrdenada.addAll(lstNotaSinNull);
                listaOrdenada.addAll(lstNotaConNull);

                eva.setListaEvaluacionNota(listaOrdenada);

            }

            lstEvaluaciones = new ArrayList<>(
                    Lists.transform(lstEvaluacion, EvaluacionToDTO.INSTANCE));

        }

        return lstEvaluaciones;
    }

}
