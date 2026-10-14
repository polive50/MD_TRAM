package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaEntidadCerradaToDTO
        implements Function<ConvocatoriaEntidadCerrada, ConvocatoriaEntidadCerradaDTO> {
    
    /**
    *
    */
    INSTANCE;
    
    /**
    *
    */
   @Override
   public ConvocatoriaEntidadCerradaDTO apply(ConvocatoriaEntidadCerrada entity) {
       
       ConvocatoriaEntidadCerradaDTO dto = new ConvocatoriaEntidadCerradaDTO();
       
       if (entity != null) {
           
           BeanUtils.copyProperties(entity, dto);
           
           if (entity.getConvocatoria() != null) {
               dto.setConvocatoriaDTO(ConvocatoriaToDTO.INSTANCE.apply(entity.getConvocatoria()));
           }
           
           if (entity.getEntidadCerrada() != null) {
               dto.setEntidadCerrada(
                       CatalogosValoresToDTO.INSTANCE.apply(entity.getEntidadCerrada()));
           }
       }
       
       return dto;
   }

}
