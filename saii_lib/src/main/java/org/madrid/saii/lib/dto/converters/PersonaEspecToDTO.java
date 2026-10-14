package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for PersonaEspecialidad
 *
 * @author nttdata
 */
public enum PersonaEspecToDTO implements Function<PersonaEspec, PersonaEspecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaEspecDTO apply(PersonaEspec entity) {

        PersonaEspecDTO dto = new PersonaEspecDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
