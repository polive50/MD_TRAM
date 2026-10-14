package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.dto.ConvocatoriaEurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaEur
 *
 * @author everis
 */
public enum ConvocatoriaEurToEntity implements Function<ConvocatoriaEurDTO, ConvocatoriaEur> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEur apply(ConvocatoriaEurDTO dto) {

        ConvocatoriaEur entity = null;

        if (dto != null) {

            entity = new ConvocatoriaEur();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getConvocatoriaDTO() != null) {
                entity.setConvocatoria(
                        ConvocatoriaToEntity.INSTANCE.apply(dto.getConvocatoriaDTO()));
            }

            if (dto.getFondoEuropeoDTO() != null) {
                entity.setFondoEuropeo(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getFondoEuropeoDTO()));
            }

        }

        return entity;
    }
}
