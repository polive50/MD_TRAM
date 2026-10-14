package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaArea
 *
 * @author nttdata
 */
public enum ConvocatoriaAreaToDTO implements Function<ConvocatoriaArea, ConvocatoriaAreaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAreaDTO apply(ConvocatoriaArea entity) {

        ConvocatoriaAreaDTO dto = new ConvocatoriaAreaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getArea() != null) {
                dto.setAreaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getArea()));
            }
            
            if(entity.getConvocatoriaAreaSubarea() != null) {
                dto.setConvocatoriaAreaSubareaDTO(new ArrayList<>());
                    entity.getConvocatoriaAreaSubarea().forEach(e -> {
                        if (e.getFcBajaFila() == null) {
                            dto.getConvocatoriaAreaSubareaDTO()
                                    .add(ConvocatoriaAreaSubareaToDTO.INSTANCE.apply(e));
                        }
                    });
                
            }

        }

        return dto;
    }
}
