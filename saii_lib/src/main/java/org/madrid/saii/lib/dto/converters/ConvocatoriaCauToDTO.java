package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCau
 *
 * @author everis
 */
public enum ConvocatoriaCauToDTO implements Function<ConvocatoriaCau, ConvocatoriaCauDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCauDTO apply(ConvocatoriaCau entity) {

        ConvocatoriaCauDTO dto = new ConvocatoriaCauDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getCausa() != null) {
                dto.setCausaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getCausa()));
            }
            
            if (entity.getTipoCausa() != null) {
                dto.setTipoCausa(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoCausa()));
            }

        }

        return dto;
    }
}
