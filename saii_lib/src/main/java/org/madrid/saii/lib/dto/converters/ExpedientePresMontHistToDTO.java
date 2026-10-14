package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedientePremontoHist
 *
 * @author nttdata
 */
public enum ExpedientePresMontHistToDTO implements Function<ExpedientePresMontHist, ExpedientePresMontHistDTO> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedientePresMontHistDTO apply(ExpedientePresMontHist entity) {

		ExpedientePresMontHistDTO dto = new ExpedientePresMontHistDTO();

		if (Objects.nonNull(entity)) {

			BeanUtils.copyProperties(entity, dto);
		}

		return dto;
	}
}