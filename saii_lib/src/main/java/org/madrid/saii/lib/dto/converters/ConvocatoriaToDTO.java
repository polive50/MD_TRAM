package org.madrid.saii.lib.dto.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Convocatoria
 *
 * @author everis
 */
public enum ConvocatoriaToDTO implements Function<Convocatoria, ConvocatoriaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
//    public ConvocatoriaDTO apply(Convocatoria entity) {
//
//        ConvocatoriaDTO dto = new ConvocatoriaDTO();
//
//        if (entity != null) {
//
//            BeanUtils.copyProperties(entity, dto);
//            this.replaceNull(dto, entity);
//
//            dto.setIdEstado(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
//            
//            dto.setIdTipoSolicitud(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoSolicitud()));
//            
//            dto.setIdTipoConcesion(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoConcesion()));
//
//            dto.setIdLineaAyuda(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdLineaAyuda()));
//
//            this.transformarUnidadAdmin(entity, dto);
//
//            dto.setIdModalidadConvocatoria(
//                    CatalogosValoresToDTO.INSTANCE.apply(entity.getIdModalidadConvocatoria()));
//
//            dto.setIdTipoModalidad(
//                    CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoModalidad()));
//
//            this.procesarFechaPresentacion(dto, entity);
//
//            dto.setBlPresFueraPlazo(
//                    Constantes.AfirmacionNegacion.S.equals(dto.getItPresFueraPlazo()));
//
//            if (StringUtils.isNotBlank(entity.getDsDescripcion())
//                    && entity.getDsDescripcion().length() > 50) {
//                dto.setDsDescripcionCorta(entity.getDsDescripcion().substring(0, 50).concat("..."));
//            } else {
//                dto.setDsDescripcionCorta(entity.getDsDescripcion());
//            }
//
//            this.procesarAsociarCoordinador(dto);
//            this.procesarAsociarArea(dto);
//            
//            this.procesarFechaTramiteEvaluacion(dto);
//
//            this.procesarFechaAlegaciones(dto);
//
//            this.validarFechaPlazoEvaluacion(dto);
//            
//            if (entity.getIdModalidadConvocatoria() != null
//                    && entity.getIdModalidadConvocatoria().getTipoResolModalidad() != null) {
//                dto.setTipoResolModalidadDTO(TipoResolModalidadToDTO.INSTANCE
//                        .apply(entity.getIdModalidadConvocatoria().getTipoResolModalidad()));
//            }
//            
//            if (entity.getIdAreaAdministrativaJCT() != null
//                    && entity.getIdAreaAdministrativaJCT().getIdUnidadAdmin() != null) {
//                dto.setUnidadAdminJCTSel(
//                        UnidadAdminToDTO.INSTANCE.apply(entity.getIdAreaAdministrativaJCT()));
//            }
//            
//            if (entity.getIdAceptacionDesistimientoAyuda() != null) {
//                dto.setIdAceptacionDesistimientoAyuda(CatalogosValoresToDTO.INSTANCE
//                        .apply(entity.getIdAceptacionDesistimientoAyuda()));
//            }
//            
//
//             dto.setItResProvConcDeneg("S".equalsIgnoreCase(entity.getItResProvConcDeneg()));
//             dto.setItResProvExclusion("S".equalsIgnoreCase(entity.getItResProvExclusion()));
//             dto.setItResProvDesistimiento("S".equalsIgnoreCase(entity.getItResProvDesistimiento()));
//
//
//        }
//
//        return dto;
//    }
    
    public ConvocatoriaDTO apply(Convocatoria entity) {

        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);

        ConvocatoriaDTO dto = new ConvocatoriaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);


            if (entity.getIdEstado() != null && entity.getIdEstado().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdEstado().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado());
                }
                dto.setIdEstado(catDTO);
            }
            

            if (entity.getIdTipoSolicitud() != null && entity.getIdTipoSolicitud().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdTipoSolicitud().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoSolicitud());
                }
                dto.setIdTipoSolicitud(catDTO);
            }
            

            if (entity.getIdTipoConcesion() != null && entity.getIdTipoConcesion().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdTipoConcesion().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoConcesion());
                }
                dto.setIdTipoConcesion(catDTO);
            }


            if (entity.getIdLineaAyuda() != null && entity.getIdLineaAyuda().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdLineaAyuda().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdLineaAyuda());
                }
                dto.setIdLineaAyuda(catDTO);
            }

            this.transformarUnidadAdmin(entity, dto);


            if (entity.getIdModalidadConvocatoria() != null && entity.getIdModalidadConvocatoria().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdModalidadConvocatoria().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdModalidadConvocatoria());
                }
                dto.setIdModalidadConvocatoria(catDTO);
            }


            if (entity.getIdTipoModalidad() != null && entity.getIdTipoModalidad().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdTipoModalidad().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoModalidad());
                }
                dto.setIdTipoModalidad(catDTO);
            }

            this.procesarFechaPresentacion(dto, entity);

            dto.setBlPresFueraPlazo(
                    Constantes.AfirmacionNegacion.S.equals(dto.getItPresFueraPlazo()));

            if (StringUtils.isNotBlank(entity.getDsDescripcion())
                    && entity.getDsDescripcion().length() > 50) {
                dto.setDsDescripcionCorta(entity.getDsDescripcion().substring(0, 50).concat("..."));
            } else {
                dto.setDsDescripcionCorta(entity.getDsDescripcion());
            }

            this.procesarAsociarCoordinador(dto);
            this.procesarAsociarArea(dto);
            
            this.procesarFechaTramiteEvaluacion(dto);
            this.procesarFechaAlegaciones(dto);
            this.validarFechaPlazoEvaluacion(dto);
            
            if (entity.getIdModalidadConvocatoria() != null
                    && entity.getIdModalidadConvocatoria().getTipoResolModalidad() != null) {
                dto.setTipoResolModalidadDTO(TipoResolModalidadToDTO.INSTANCE
                        .apply(entity.getIdModalidadConvocatoria().getTipoResolModalidad()));
            }
            
            if (entity.getIdAreaAdministrativaJCT() != null
                    && entity.getIdAreaAdministrativaJCT().getIdUnidadAdmin() != null) {
                dto.setUnidadAdminJCTSel(
                        UnidadAdminToDTO.INSTANCE.apply(entity.getIdAreaAdministrativaJCT()));
            }

            if (entity.getIdAceptacionDesistimientoAyuda() != null && entity.getIdAceptacionDesistimientoAyuda().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getIdAceptacionDesistimientoAyuda().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getIdAceptacionDesistimientoAyuda());
                }
                dto.setIdAceptacionDesistimientoAyuda(catDTO);
            }

            dto.setItResProvConcDeneg("S".equalsIgnoreCase(entity.getItResProvConcDeneg()));
            dto.setItResProvExclusion("S".equalsIgnoreCase(entity.getItResProvExclusion()));
            dto.setItResProvDesistimiento("S".equalsIgnoreCase(entity.getItResProvDesistimiento()));
            
            dto.setNmAnualJusEco(dto.getNmDurMaxAyuda());
            
        }

        return dto;
    }
    

    /**
     * Metodo que transforma direccion,subdireccion y unidad administrativa
     *
     * @param dto
     * @param entity
     */
    private void transformarUnidadAdmin(Convocatoria entity, ConvocatoriaDTO dto) {

        if (Objects.isNull(entity.getIdDireccion())
                || Objects.isNull(entity.getIdDireccion().getIdUnidadAdmin())) {
            dto.setIdDireccion(null);
        } else {
            dto.setIdDireccion(UnidadAdminToDTO.INSTANCE.apply(entity.getIdDireccion()));
        }

        if (Objects.isNull(entity.getIdSubdireccion())
                || Objects.isNull(entity.getIdSubdireccion().getIdUnidadAdmin())) {
            dto.setIdSubdireccion(null);
        } else {
            dto.setIdSubdireccion(UnidadAdminToDTO.INSTANCE.apply(entity.getIdSubdireccion()));
        }

        if (Objects.isNull(entity.getIdAreaAdministrativa())
                || Objects.isNull(entity.getIdAreaAdministrativa().getIdUnidadAdmin())) {
            dto.setIdAreaAdministrativa(null);
        } else {
            dto.setIdAreaAdministrativa(
                    UnidadAdminToDTO.INSTANCE.apply(entity.getIdAreaAdministrativa()));
        }
        
        if (Objects.isNull(entity.getIdAreaAdministrativaJCT())
                || Objects.isNull(entity.getIdAreaAdministrativaJCT().getIdUnidadAdmin())) {
            dto.setIdAreaAdministrativaJCT(null);
        } else {
            dto.setIdAreaAdministrativaJCT(
                    UnidadAdminToDTO.INSTANCE.apply(entity.getIdAreaAdministrativaJCT()));
        }
    }

    /**
     * Metodo para procesar la validación de permiso para asociar coordinadores
     * a convocatoria
     *
     * @param dto
     */
    private void procesarAsociarCoordinador(ConvocatoriaDTO dto) {
        dto.setEsPermiteAsociarCoordinador(Boolean.TRUE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(dto.getIdEstado().getIdValor())) {
            dto.setEsPermiteAsociarCoordinador(Boolean.FALSE);
        }
    }

    
    /**
     * Metodo para procesar la validación de permiso para asociar areas
     * a convocatoria
     *
     * @param dto
     */
    private void procesarAsociarArea(ConvocatoriaDTO dto) {
        dto.setEsPermiteAsociarArea(Boolean.TRUE);
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(dto.getIdEstado().getIdValor())) {
            dto.setEsPermiteAsociarArea(Boolean.FALSE);
        }
    }
    
    /**
     * Metodo para procesar las fechas de presentación
     *
     * @param dto
     * @param entity
     */
    private void procesarFechaPresentacion(ConvocatoriaDTO dto, Convocatoria entity) {
        if (entity.getFcIniPresentacion() != null) {
            dto.setFcIniPresentacion(entity.getFcIniPresentacion());
        }

        if (entity.getFcFinPresentacion() != null) {
            dto.setFcFinPresentacion(entity.getFcFinPresentacion());
        }
    }

    /**
     * Metodo para procesar las fecha de trámite de evaluación
     *
     * @param dto
     */
    private void procesarFechaTramiteEvaluacion(ConvocatoriaDTO dto) {
        if (Objects.isNull(dto.getFcIniTramiEval())) {
            dto.setIsMenorFechaIniEva(Boolean.FALSE);
        } else {
            LocalDateTime fecIniTraEval = Utils.convertDateToLocalDateTime(dto.getFcIniTramiEval());
            LocalDateTime fecHoy = LocalDateTime.now();

            if (fecIniTraEval.isBefore(fecHoy)) {
                dto.setIsMenorFechaIniEva(Boolean.TRUE);
            } else if (fecIniTraEval.equals(fecHoy) || fecIniTraEval.isAfter(fecHoy)) {
                dto.setIsMenorFechaIniEva(Boolean.FALSE);
            }

            dto.setFcIniTramiEvalFormat(
                    fecIniTraEval.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }

        if (Objects.nonNull(dto.getFcFinTramiEval())) {
            LocalDateTime fecFinTraEval = Utils.convertDateToLocalDateTime(dto.getFcFinTramiEval());
            dto.setFcFinTramiEvalFormat(
                    fecFinTraEval.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
    }

    /**
     * Metodo para procesar fecha de alegaciones
     *
     * @param dto
     */
    private void procesarFechaAlegaciones(ConvocatoriaDTO dto) {
        if (Objects.isNull(dto.getFcIniTramiEval())) {
            dto.setIsMenorFechaIniTramEval(Boolean.TRUE);
        } else {
            LocalDateTime fecIniTramEv = Utils.convertDateToLocalDateTime(dto.getFcIniTramiEval());
            LocalDateTime fecHoy = LocalDateTime.now();

            if (fecHoy.isBefore(fecIniTramEv)) {
                dto.setIsMenorFechaIniTramEval(Boolean.FALSE);
            } else if (fecHoy.equals(fecIniTramEv) || fecHoy.isAfter(fecIniTramEv)) {
                dto.setIsMenorFechaIniTramEval(Boolean.TRUE);
            }
        }
    }

    /**
     * Metodo para validar fecha de plazo de evaluación
     *
     * @param dto
     */
    private void validarFechaPlazoEvaluacion(ConvocatoriaDTO dto) {

        dto.setIsValFechaEval(Boolean.FALSE);

        if (Objects.nonNull(dto.getFcIniTramiEval()) && Objects.nonNull(dto.getFcFinTramiEval())) {
            LocalDateTime fecIniTraEval = Utils.convertDateToLocalDateTime(dto.getFcIniTramiEval());
            LocalDateTime fecFinTraEval = Utils.convertDateToLocalDateTime(dto.getFcFinTramiEval());

            dto.setIsValFechaEval(Utils.fechaHoraActualEntreRango(fecIniTraEval, fecFinTraEval));
        }

    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ConvocatoriaDTO dto, Convocatoria entity) {

        if (StringUtils.isBlank(entity.getItConvocatoriaActiva())) {
            dto.setItConvocatoriaActiva(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getItInicioSolOrg())) {
            dto.setItInicioSolOrg(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getItPresFueraPlazo())) {
            dto.setItPresFueraPlazo(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getItImporteMaxAnual())) {
            dto.setItImporteMaxAnual(Constantes.AfirmacionNegacion.N);
        }
        if (StringUtils.isBlank(entity.getItImporteMaxExpdte())) {
            dto.setItImporteMaxExpdte(Constantes.AfirmacionNegacion.N);
        }

    }

}
