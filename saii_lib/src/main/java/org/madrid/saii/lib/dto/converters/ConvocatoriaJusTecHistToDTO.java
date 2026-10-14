package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConvocatoriaJusTecHistToDTO
        implements Function<ConvocatoriaJusTecHist, ConvocatoriaJusTecHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaJusTecHistDTO apply(ConvocatoriaJusTecHist entity) {

        ConvocatoriaJusTecHistDTO dto = new ConvocatoriaJusTecHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoConvocatoriaJusTec())) {
                dto.setTipoConvocatoriaJusTecDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoConvocatoriaJusTec()));
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
    private void replaceNull(ConvocatoriaJusTecHistDTO dto, ConvocatoriaJusTecHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }

    }

}
