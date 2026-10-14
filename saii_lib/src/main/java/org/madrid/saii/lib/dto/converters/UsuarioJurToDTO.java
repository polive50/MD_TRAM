package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.UsuarioJurDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for UsuarioJur
 *
 * @author everis
 */
public enum UsuarioJurToDTO implements Function<UsuarioJur, UsuarioJurDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public UsuarioJurDTO apply(UsuarioJur entity) {

        UsuarioJurDTO dto = new UsuarioJurDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

            dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            dto.setEntidadJurDTO(InteresadoToDTO.INSTANCE.apply(entity.getEntidadJur()));
        }

        return dto;
    }
}
