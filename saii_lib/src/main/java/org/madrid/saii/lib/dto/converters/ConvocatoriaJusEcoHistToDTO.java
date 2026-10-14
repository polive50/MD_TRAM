package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusEcoHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusEcoHistToDTO
        implements Function<ConvocatoriaJusEcoHist, ConvocatoriaJusEcoHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusEcoHistDTO apply(ConvocatoriaJusEcoHist entity) {

        ConvocatoriaJusEcoHistDTO dto = new ConvocatoriaJusEcoHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoConvocatoriaJusEco())) {
                dto.setTipoConvocatoriaJusEcoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoConvocatoriaJusEco()));
            }

        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ConvocatoriaJusEcoHistDTO dto, ConvocatoriaJusEcoHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
