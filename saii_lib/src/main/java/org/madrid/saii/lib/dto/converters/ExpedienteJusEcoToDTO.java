package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ExpedienteJusEcoToDTO implements Function<ExpedienteJusEco, ExpedienteJusEcoDTO> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public ExpedienteJusEcoDTO apply(ExpedienteJusEco entity) {

		ExpedienteJusEcoDTO dto = new ExpedienteJusEcoDTO();

		if (Objects.nonNull(entity)) {

			BeanUtils.copyProperties(entity, dto);
			this.replaceNull(dto, entity);

			if (Objects.nonNull(entity.getTipoExpedienteJusEco())) {
				dto.setTipoExpedienteJusEcoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoExpedienteJusEco()));
			}
			if (Objects.nonNull(entity.getIdEstadoRevAdmin())) {
                dto.setIdEstadoRevAdminDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstadoRevAdmin()));
            }
			if (Objects.nonNull(entity.getIdEstado())) {
                dto.setIdEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getIdEstado()));
            }
			
			dto.setTlObservacionParaCiudadanoMod(entity.getTlObservacionParaCiudadano() == null ? "" : entity.getTlObservacionParaCiudadano());
		}

		return dto;
	}

	/**
	 * Reemplaza los null de los Strings no obligatorios por EMPTY
	 *
	 * @param dto
	 * @param entity
	 */
	private void replaceNull(ExpedienteJusEcoDTO dto, ExpedienteJusEco entity) {

		if (StringUtils.isBlank(entity.getTlObservacion())) {
			dto.setTlObservacion(StringUtils.EMPTY);
		}

	}

}
