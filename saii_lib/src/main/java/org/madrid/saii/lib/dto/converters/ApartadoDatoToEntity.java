package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ApartadoDato;
import org.madrid.saii.lib.dto.ApartadoDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ApartadoDato
 *
 * @author nttdata
 */
public enum ApartadoDatoToEntity implements Function<ApartadoDatoDTO, ApartadoDato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ApartadoDato apply(ApartadoDatoDTO dto) {

        ApartadoDato entity = null;

        if (dto != null) {

            entity = new ApartadoDato();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
