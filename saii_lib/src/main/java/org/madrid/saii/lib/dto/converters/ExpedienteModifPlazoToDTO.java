package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteModifPlazo;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteModifPlazoToDTO implements Function<ExpedienteModifPlazo, ExpedienteModifPlazoDTO> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteModifPlazoDTO apply(ExpedienteModifPlazo entity) {

	    ExpedienteModifPlazoDTO dto = new ExpedienteModifPlazoDTO();

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
	private void replaceNull(ExpedienteModifPlazoDTO dto, ExpedienteModifPlazo entity) {

	}

}
