package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteAnualidadToDTO implements Function<ExpedienteAnualidad, ExpedienteAnualidadDTO> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteAnualidadDTO apply(ExpedienteAnualidad entity) {

		ExpedienteAnualidadDTO dto = new ExpedienteAnualidadDTO();

		if (Objects.nonNull(entity)) {

			BeanUtils.copyProperties(entity, dto);
			this.replaceNull(dto, entity);

			if (Objects.nonNull(entity.getTipoExpedienteAnualidad())) {
				dto.setTipoExpedienteAnualidadDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoExpedienteAnualidad()));
			}
		}

		return dto;
	}

	/**
	 * Reemplaza los null de los Strings no obligatorios por EMPTY
	 *
	 * @param dto
	 * @param entity
	 */
	private void replaceNull(ExpedienteAnualidadDTO dto, ExpedienteAnualidad entity) {

		if (StringUtils.isBlank(entity.getTlObservacion())) {
			dto.setTlObservacion(StringUtils.EMPTY);
		}

	}

}
