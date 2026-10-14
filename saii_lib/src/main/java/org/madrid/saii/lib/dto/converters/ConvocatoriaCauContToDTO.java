package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.madrid.saii.lib.dto.ConvocatoriaCauContDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCauCont
 *
 * @author quental
 */
public enum ConvocatoriaCauContToDTO
        implements Function<ConvocatoriaCauCont, ConvocatoriaCauContDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCauContDTO apply(ConvocatoriaCauCont entity) {

        ConvocatoriaCauContDTO dto = new ConvocatoriaCauContDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getCausaCont())) {
                dto.setCausaContDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getCausaCont()));
            }

        }

        return dto;
    }

}
