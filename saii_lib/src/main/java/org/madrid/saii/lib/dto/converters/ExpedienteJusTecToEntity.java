package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusTecToEntity implements Function<ExpedienteJusTecDTO, ExpedienteJusTec> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteJusTec apply(ExpedienteJusTecDTO dto) {

		ExpedienteJusTec entity = null;

		if (Objects.nonNull(dto)) {

			entity = new ExpedienteJusTec();

			BeanUtils.copyProperties(dto, entity);

			if (Objects.nonNull(dto.getTipoExpedienteJusTecDTO())) {
				entity.setTipoExpedienteJusTec(
						CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteJusTecDTO()));
			}
			if (Objects.nonNull(dto.getIdEstadoRevAdminDTO())) {
                entity.setIdEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoRevAdminDTO()));
            }
			
			if (Objects.nonNull(dto.getIdEstadoDTO())) {
                entity.setIdEstado(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoDTO()));
            }

		}

		return entity;
	}

}
