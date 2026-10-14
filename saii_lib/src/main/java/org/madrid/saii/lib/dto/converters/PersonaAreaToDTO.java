package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for PersonaAreaCientifica
 *
 * @author nttdata
 */
public enum PersonaAreaToDTO implements Function<PersonaArea, PersonaAreaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaAreaDTO apply(PersonaArea entity) {

        PersonaAreaDTO dto = new PersonaAreaDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getArea() != null && entity.getArea().getIdValor() != null) {
                dto.setAreaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getArea()));
            }

        }

        return dto;
    }
}
