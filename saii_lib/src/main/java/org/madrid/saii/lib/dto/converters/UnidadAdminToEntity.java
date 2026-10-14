package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for UnidadAdmin
 *
 * @author everis
 */
public enum UnidadAdminToEntity implements Function<UnidadAdminDTO, UnidadAdmin> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public UnidadAdmin apply(UnidadAdminDTO dto) {

        UnidadAdmin entity = null;

        if (dto != null) {

            entity = new UnidadAdmin();

            BeanUtils.copyProperties(dto, entity);

			if (dto.getNivelOrgDTO() == null || dto.getNivelOrgDTO().getIdNivelOrg() == null) {
				entity.setNivelOrg(null);
			} else {
				entity.setNivelOrg(NivelOrgToEntity.INSTANCE.apply(dto.getNivelOrgDTO()));
			}


        }

        return entity;
    }
}
