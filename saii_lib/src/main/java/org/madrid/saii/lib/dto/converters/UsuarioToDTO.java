package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Usuario;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Usuario
 *
 * @author everis
 */
public enum UsuarioToDTO implements Function<Usuario, UsuarioDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public UsuarioDTO apply(Usuario entity) {

        UsuarioDTO dto = new UsuarioDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
