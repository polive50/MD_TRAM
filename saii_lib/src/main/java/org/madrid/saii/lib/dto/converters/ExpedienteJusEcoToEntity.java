package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusEcoToEntity implements Function<ExpedienteJusEcoDTO, ExpedienteJusEco> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteJusEco apply(ExpedienteJusEcoDTO dto) {

		ExpedienteJusEco entity = null;

		if (Objects.nonNull(dto)) {

			entity = new ExpedienteJusEco();

			BeanUtils.copyProperties(dto, entity);

			if (Objects.nonNull(dto.getTipoExpedienteJusEcoDTO()) && dto.getTipoExpedienteJusEcoDTO().getIdValor() != null) {
				entity.setTipoExpedienteJusEco(
						CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoExpedienteJusEcoDTO()));
			}
			if (Objects.nonNull(dto.getIdEstadoRevAdminDTO()) && dto.getIdEstadoRevAdminDTO().getIdValor() != null) {
                entity.setIdEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoRevAdminDTO()));
            }
			
			if (Objects.nonNull(dto.getIdEstadoDTO()) && dto.getIdEstadoDTO().getIdValor() != null) {
                entity.setIdEstado(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstadoDTO()));
            }

		}

		return entity;
	}

}
