package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaReq
 *
 * @author nttdata
 */
public enum ConvocatoriaReqToDTO implements Function<ConvocatoriaReq, ConvocatoriaReqDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaReqDTO apply(ConvocatoriaReq entity) {

        ConvocatoriaReqDTO dto = new ConvocatoriaReqDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getRequisito() != null) {
                dto.setRequisitoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getRequisito()));
            }

            if (entity.getConvocatoriaSubmod() != null) {
                dto.setConvocatoriaSubmodDTO(
                        ConvocatoriaSubmodToDTO.INSTANCE.apply(entity.getConvocatoriaSubmod()));
            }
            
            if (entity.getConvocatoriaAct() != null) {
                dto.setConvocatoriaActDTO(
                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaAct()));
            }
            
            if (entity.getConvocatoriaTipoDoc() != null) {
                dto.setConvocatoriaTipoDocDTO(
                        ConvocatoriaTipoDocToDTO.INSTANCE.apply(entity.getConvocatoriaTipoDoc()));
            }
      
        }

        return dto;
    }
    
    
}
