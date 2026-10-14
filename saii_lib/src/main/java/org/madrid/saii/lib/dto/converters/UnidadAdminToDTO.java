package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for UnidadAdmin
 *
 * @author everis
 */
public enum UnidadAdminToDTO implements Function<UnidadAdmin, UnidadAdminDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public UnidadAdminDTO apply(UnidadAdmin entity) {

        UnidadAdminDTO dto = new UnidadAdminDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setNivelOrgDTO(NivelOrgToDTO.INSTANCE.apply(entity.getNivelOrg()));


		}

		return dto;
	}

}
