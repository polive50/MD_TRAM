package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Evaluacion
 *
 * @author nttdata
 */
public enum EvaluacionJustecToDTO implements Function<EvaluacionJustec, EvaluacionJustecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustecDTO apply(EvaluacionJustec entity) {

        EvaluacionJustecDTO dto = new EvaluacionJustecDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            dto.setBlCalificacion(Constantes.AfirmacionNegacion.S.equals(entity.getItCalificacion())
                    ? Boolean.TRUE
                    : Boolean.FALSE);

            if (entity.getListaEvaluacionJustecAparta() != null) {
                dto.setListaEvaluacionJustecApartaDTO(new ArrayList<>());
                entity.getListaEvaluacionJustecAparta().forEach(e -> {
                    if (e.getFcBajaFila() == null) {
                        dto.getListaEvaluacionJustecApartaDTO()
                                .add(EvaluacionJustecApartaToDTO.INSTANCE.apply(e));
                    }
                });
            }

            if (entity.getListaEvaluacionJustecNota() != null) {
                dto.setListaEvaluacionJustecNotaDTO(new ArrayList<>());
                entity.getListaEvaluacionJustecNota()
                        .forEach(e -> dto.getListaEvaluacionJustecNotaDTO()
                                .add(EvaluacionJustecNotaToDTO.INSTANCE.apply(e)));
            }

            if (entity.getImagenLogo() != null) {
                dto.setImagenLogoDTO(ImagenToDTO.INSTANCE.apply(entity.getImagenLogo()));
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
    private void replaceNull(EvaluacionJustecDTO dto, EvaluacionJustec entity) {

        if (StringUtils.isBlank(entity.getTlDescripcion())) {
            dto.setTlDescripcion(StringUtils.EMPTY);
        }

    }
}
