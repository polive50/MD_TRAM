package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.NivelOrg;
import org.madrid.saii.lib.dto.NivelOrgDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for NivelOrg
 *
 * @author everis
 */
public enum NivelOrgToDTO implements Function<NivelOrg, NivelOrgDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NivelOrgDTO apply(NivelOrg entity) {

        NivelOrgDTO dto = new NivelOrgDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setIdNivelOrg(entity.getIdNivelOrg());
            dto.setDsNivelOrg(entity.getDsNivelOrg());

        }

        return dto;
    }
}
