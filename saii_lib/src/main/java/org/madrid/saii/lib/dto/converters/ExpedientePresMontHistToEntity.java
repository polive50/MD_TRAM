package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedientePremontoHist
 *
 * @author nttdata
 */
public enum ExpedientePresMontHistToEntity implements Function<ExpedientePresMontHistDTO, ExpedientePresMontHist> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedientePresMontHist apply(ExpedientePresMontHistDTO dto) {

		ExpedientePresMontHist entity = null;

		if (Objects.nonNull(dto)) {

			entity = new ExpedientePresMontHist();

			BeanUtils.copyProperties(dto, entity);

		}

		return entity;
	}
}
