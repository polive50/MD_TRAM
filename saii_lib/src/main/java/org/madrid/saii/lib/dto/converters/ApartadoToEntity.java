package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Apartado
 *
 * @author nttdata
 */
public enum ApartadoToEntity implements Function<ApartadoDTO, Apartado> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Apartado apply(ApartadoDTO dto) {

        Apartado entity = null;

        if (dto != null) {

            entity = new Apartado();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
