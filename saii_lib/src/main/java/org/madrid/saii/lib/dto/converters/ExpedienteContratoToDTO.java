package org.madrid.saii.lib.dto.converters;


import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum  ExpedienteContratoToDTO implements Function<ExpedienteContrato, ExpedienteContratoDTO> {
    
      /**
         *
         */
        INSTANCE;

        /**
         *
         */
        @Override
    
  public ExpedienteContratoDTO apply(ExpedienteContrato entity) {

        ExpedienteContratoDTO dto = new ExpedienteContratoDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getIdCausa()) && Objects.nonNull(entity.getIdCausa().getIdValor())) {
                dto.setIdCausaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdCausa()));
            }
            if (Objects.nonNull(entity.getIdTipoContrato()) && Objects.nonNull(entity.getIdTipoContrato().getIdValor())) {
                dto.setIdTipoContratoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoContrato()));
            }
            if (Objects.nonNull(entity.getIdEstado()) && Objects.nonNull(entity.getIdEstado().getIdValor())) {
                dto.setIdEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
            }
            
            if (Objects.nonNull(entity.getIdEstadoRevAdmin()) && Objects.nonNull(entity.getIdEstadoRevAdmin().getIdValor())) {
                dto.setIdEstadoRevAdminDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstadoRevAdmin()));
            }
            
            if (Objects.nonNull(entity.getInteresado()) && Objects.nonNull(entity.getInteresado().getIdInteresado())) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            }
            
            dto.setTlObservaciParaCiudadano(entity.getTlObservaciParaCiudadano() == null ? "" : entity.getTlObservaciParaCiudadano());
            dto.setTlObservaciDelCiudadano(entity.getTlObservaciDelCiudadano() == null ? "" : entity.getTlObservaciDelCiudadano());
            dto.setTlCausaDescripcion(entity.getTlCausaDescripcion() == null ? "" : entity.getTlCausaDescripcion());
            dto.setItIndefinido(entity.getItIndefinido() == null ? "" : entity.getItIndefinido());
        }

        return dto;
    }


    private void replaceNull(ExpedienteContratoDTO dto, ExpedienteContrato entity) {

        if (StringUtils.isBlank(entity.getTlCausaDescripcion())) {
            dto.setTlCausaDescripcion(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlObservaciDelCiudadano())) {
            dto.setTlObservaciDelCiudadano(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlObservaciParaCiudadano())) {
            dto.setTlObservaciParaCiudadano(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getCdEregPresent())) {
            dto.setCdEregPresent(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getCdEregSubsana())) {
            dto.setCdEregSubsana(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getDsDestinoEreg())) {
            dto.setDsDestinoEreg(StringUtils.EMPTY);
        }
    }
}
