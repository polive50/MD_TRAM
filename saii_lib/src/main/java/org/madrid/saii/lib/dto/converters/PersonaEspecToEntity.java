package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for PersonaEspecialidad
 *
 * @author nttdata
 */
public enum PersonaEspecToEntity implements Function<PersonaEspecDTO, PersonaEspec> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaEspec apply(PersonaEspecDTO dto) {

        PersonaEspec entity = null;

        if (dto != null) {

            entity = new PersonaEspec();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
