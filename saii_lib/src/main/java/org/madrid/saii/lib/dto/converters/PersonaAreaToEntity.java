package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for PersonaAreaCientifica
 *
 * @author nttdata
 */
public enum PersonaAreaToEntity implements Function<PersonaAreaDTO, PersonaArea> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaArea apply(PersonaAreaDTO dto) {

        PersonaArea entity = null;

        if (dto != null) {

            entity = new PersonaArea();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getAreaDTO() != null && dto.getAreaDTO().getIdValor() != null) {
                entity.setArea(CatalogosValoresToEntity.INSTANCE.apply(dto.getAreaDTO()));
            }
        }

        return entity;
    }
}
