package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteAnualidadToEntity implements Function<ExpedienteAnualidadDTO, ExpedienteAnualidad> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteAnualidad apply(ExpedienteAnualidadDTO dto) {

		ExpedienteAnualidad entity = null;

		if (Objects.nonNull(dto)) {

			entity = new ExpedienteAnualidad();

			BeanUtils.copyProperties(dto, entity);

			if (Objects.nonNull(dto.getTipoExpedienteAnualidadDTO())) {
				entity.setTipoExpedienteAnualidad(
						CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteAnualidadDTO()));
			}
		}

		return entity;
	}

}
