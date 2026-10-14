package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Usuario;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Usuario
 *
 * @author everis
 */
public enum UsuarioToEntity implements Function<UsuarioDTO, Usuario> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Usuario apply(UsuarioDTO dto) {

        Usuario entity = null;

        if (dto != null) {
            entity = new Usuario();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
