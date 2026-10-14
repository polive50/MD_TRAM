package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for FinanciacionDato
 *
 * @author nttdata
 */
public enum FinanciacionDatoToEntity implements Function<FinanciacionDatoDTO, FinanciacionDato> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public FinanciacionDato apply(FinanciacionDatoDTO dto) {

		FinanciacionDato entity = null;

		if (Objects.nonNull(dto)) {

			entity = new FinanciacionDato();

			BeanUtils.copyProperties(dto, entity);

			if (Objects.nonNull(dto.getTipoDTO())) {
				entity.setTipo(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDTO()));
			}
			
			if (dto.getItImporteFijoAux() != null) {
			    if (Boolean.TRUE.equals(dto.getItImporteFijoAux())) {
			        entity.setItImporteFijo("S");
			    } else {
			        entity.setItImporteFijo("N");
			    }
			}

		}

		return entity;
	}
}
