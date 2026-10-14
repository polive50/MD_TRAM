package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.NivelOrg;
import org.madrid.saii.lib.dto.NivelOrgDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for NivelOrg
 *
 * @author everis
 */
public enum NivelOrgToEntity implements Function<NivelOrgDTO, NivelOrg> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public NivelOrg apply(NivelOrgDTO dto) {

        NivelOrg entity = null;

        if (dto != null) {

            entity = new NivelOrg();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdNivelOrg(dto.getIdNivelOrg());
            entity.setDsNivelOrg(dto.getDsNivelOrg());

        }

        return entity;
    }
}
