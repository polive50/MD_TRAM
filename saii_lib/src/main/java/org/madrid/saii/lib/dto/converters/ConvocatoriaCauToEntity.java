package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCau
 *
 * @author everis
 */
public enum ConvocatoriaCauToEntity implements Function<ConvocatoriaCauDTO, ConvocatoriaCau> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCau apply(ConvocatoriaCauDTO dto) {

        ConvocatoriaCau entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCau();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getCausaDTO() != null) {
                entity.setCausa(CatalogosValoresToEntity.INSTANCE.apply(dto.getCausaDTO()));
            }
            
            if (dto.getTipoCausa() != null) {
                entity.setTipoCausa(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoCausa()));
            }

        }

        return entity;
    }
}
