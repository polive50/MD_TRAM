package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.UsuarioJurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for UsuarioJur
 *
 * @author everis
 */
public enum UsuarioJurToEntity implements Function<UsuarioJurDTO, UsuarioJur> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public UsuarioJur apply(UsuarioJurDTO dto) {

        UsuarioJur entity = null;

        if (dto != null) {
            entity = new UsuarioJur();

            BeanUtils.copyProperties(dto, entity);

            entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            entity.setEntidadJur(InteresadoToEntity.INSTANCE.apply(dto.getEntidadJurDTO()));
        }

        return entity;
    }
}
