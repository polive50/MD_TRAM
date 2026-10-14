package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ApartadoJustecDato;
import org.madrid.saii.lib.dto.ApartadoJustecDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ApartadoJustecDato
 *
 * @author nttdata
 */
public enum ApartadoJustecDatoToEntity implements Function<ApartadoJustecDatoDTO, ApartadoJustecDato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ApartadoJustecDato apply(ApartadoJustecDatoDTO dto) {

        ApartadoJustecDato entity = null;

        if (dto != null) {

            entity = new ApartadoJustecDato();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
