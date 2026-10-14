package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.madrid.saii.lib.dto.ConvocatoriaCauContDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCauCont
 *
 * @author quental
 */
public enum ConvocatoriaCauContToEntity
        implements Function<ConvocatoriaCauContDTO, ConvocatoriaCauCont> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCauCont apply(ConvocatoriaCauContDTO dto) {

        ConvocatoriaCauCont entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCauCont();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getCausaContDTO())) {
                entity.setCausaCont(CatalogosValoresToEntity.INSTANCE.apply(dto.getCausaContDTO()));
            }

        }

        return entity;
    }
}
