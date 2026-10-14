package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteModifPlazo;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteModifPlazoToEntity implements Function<ExpedienteModifPlazoDTO, ExpedienteModifPlazo> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteModifPlazo apply(ExpedienteModifPlazoDTO dto) {

	    ExpedienteModifPlazo entity = null;

		if (Objects.nonNull(dto)) {

			entity = new ExpedienteModifPlazo();

			BeanUtils.copyProperties(dto, entity);

			if (Objects.nonNull(dto.getTipoExpedienteAnualidadDTO())) {
				entity.setTipoExpedienteAnualidad(
						CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteAnualidadDTO()));
			}
		}

		return entity;
	}

}
