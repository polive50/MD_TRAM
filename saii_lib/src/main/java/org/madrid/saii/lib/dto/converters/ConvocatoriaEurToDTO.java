package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.dto.ConvocatoriaEurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaEur
 *
 * @author everis
 */
public enum ConvocatoriaEurToDTO implements Function<ConvocatoriaEur, ConvocatoriaEurDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEurDTO apply(ConvocatoriaEur entity) {

        ConvocatoriaEurDTO dto = new ConvocatoriaEurDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getConvocatoria() != null) {
                dto.setConvocatoriaDTO(ConvocatoriaToDTO.INSTANCE.apply(entity.getConvocatoria()));
            }
            if (entity.getFondoEuropeo() != null) {
                dto.setFondoEuropeoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getFondoEuropeo()));
            }

        }

        return dto;
    }
}
