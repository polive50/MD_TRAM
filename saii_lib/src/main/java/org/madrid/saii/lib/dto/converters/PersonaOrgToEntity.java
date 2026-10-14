package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.dto.PersonaOrgDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for PersonaOrg
 *
 * @author nttdata
 */
public enum PersonaOrgToEntity implements Function<PersonaOrgDTO, PersonaOrg> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaOrg apply(PersonaOrgDTO dto) {

        PersonaOrg entity = null;

        if (dto != null) {

            entity = new PersonaOrg();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getOrganismoDTO() != null && dto.getOrganismoDTO().getIdValor() != null) {
                entity.setOrganismo(CatalogosValoresToEntity.INSTANCE.apply(dto.getOrganismoDTO()));
            }

        }

        return entity;
    }
}
