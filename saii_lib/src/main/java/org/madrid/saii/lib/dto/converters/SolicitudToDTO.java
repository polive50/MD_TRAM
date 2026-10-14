package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Solicitud
 *
 * @author everis
 */
public enum SolicitudToDTO implements Function<Solicitud, SolicitudDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
//    public SolicitudDTO apply(Solicitud entity) {
//
//        SolicitudDTO dto = new SolicitudDTO();
//
//        if (entity != null) {
//
//            BeanUtils.copyProperties(entity, dto);
//
//            if (entity.getInteresado() != null) {
//                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
//            }
//
//            if (entity.getConvocatoria() != null) {
//                dto.setConvocatoriaDTO(ConvocatoriaToDTO.INSTANCE.apply(entity.getConvocatoria()));
//            }
//
//            if (entity.getEstado() != null) {
//                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
//            }
//
//            if (entity.getTipoActor() != null) {
//                dto.setTipoActorDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoActor()));
//            }
//
//            if (entity.getConvocatoriaSubmod() != null) {
//                dto.setConvocatoriaSubmodDTO(
//                        ConvocatoriaSubmodToDTO.INSTANCE.apply(entity.getConvocatoriaSubmod()));
//            }
//
//            if (entity.getConvocatoriaArea() != null) {
//                dto.setConvocatoriaAreaDTO(
//                        ConvocatoriaAreaToDTO.INSTANCE.apply(entity.getConvocatoriaArea()));
//            }
//            
//            if (entity.getConvocatoriaAreaSubarea() != null) {
//                dto.setConvocatoriaAreaSubareaDTO(
//                        ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(entity.getConvocatoriaAreaSubarea()));
//            }
//
//            dto.setItExpeCoor(Constantes.AfirmacionNegacion.S.equals(entity.getItExpeCoor()));
//
//        }
//
//        return dto;
//    }
    
    public SolicitudDTO apply(Solicitud entity) {

        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);

        SolicitudDTO dto = new SolicitudDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getInteresado() != null) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            }

            if (entity.getConvocatoria() != null) {
                dto.setConvocatoriaDTO(ConvocatoriaToDTO.INSTANCE.apply(entity.getConvocatoria()));
            }

            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getEstado().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado());
                }
                dto.setEstadoDTO(catDTO);
            }

            if (entity.getTipoActor() != null && entity.getTipoActor().getIdValor() != null) {
                CatalogosValoresDTO catDTO = utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getTipoActor().getIdValor());
                if (catDTO == null) {
                    catDTO = CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoActor());
                }
                dto.setTipoActorDTO(catDTO);
            }

            if (entity.getConvocatoriaSubmod() != null) {
                dto.setConvocatoriaSubmodDTO(
                        ConvocatoriaSubmodToDTO.INSTANCE.apply(entity.getConvocatoriaSubmod()));
            }

            if (entity.getConvocatoriaArea() != null) {
                dto.setConvocatoriaAreaDTO(
                        ConvocatoriaAreaToDTO.INSTANCE.apply(entity.getConvocatoriaArea()));
            }
            
            if (entity.getConvocatoriaAreaSubarea() != null) {
                dto.setConvocatoriaAreaSubareaDTO(
                        ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(entity.getConvocatoriaAreaSubarea()));
            }

            dto.setItExpeCoor(Constantes.AfirmacionNegacion.S.equals(entity.getItExpeCoor()));

        }

        return dto;
    }
    
    
    
}
