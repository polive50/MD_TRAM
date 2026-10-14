package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for FinanciacionDato
 *
 * @author nttdata
 */
public enum FinanciacionDatoToDTO implements Function<FinanciacionDato, FinanciacionDatoDTO> {

	/**
	 *
	 */
	INSTANCE;

	/**
	 *
	 */
	@Override
	public FinanciacionDatoDTO apply(FinanciacionDato entity) {

		FinanciacionDatoDTO dto = new FinanciacionDatoDTO();

		if (Objects.nonNull(entity)) {

			BeanUtils.copyProperties(entity, dto);

			if (Objects.nonNull(entity.getTipo())) {
				dto.setTipoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipo()));
			}
			
			if (entity.getItImporteFijo() != null) {
			    if (Constantes.AfirmacionNegacion.S.equals(entity.getItImporteFijo())) {
			        dto.setItImporteFijoAux(Boolean.TRUE);
			    } else {
			        dto.setItImporteFijoAux(Boolean.FALSE);
			    }
			}
			
		}

		return dto;
	}
}