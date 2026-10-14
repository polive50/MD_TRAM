package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.dto.PersonaOrgDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for PersonaOrg
 *
 * @author nttdata
 */
public enum PersonaOrgToDTO implements Function<PersonaOrg, PersonaOrgDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public PersonaOrgDTO apply(PersonaOrg entity) {

        PersonaOrgDTO dto = new PersonaOrgDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getOrganismo() != null && entity.getOrganismo().getIdValor() != null) {
                dto.setOrganismoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getOrganismo()));
            }
        }

        return dto;
    }
}
