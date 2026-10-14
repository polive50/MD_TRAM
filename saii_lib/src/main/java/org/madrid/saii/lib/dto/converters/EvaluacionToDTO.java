package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Evaluacion
 *
 * @author nttdata
 */
public enum EvaluacionToDTO implements Function<Evaluacion, EvaluacionDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionDTO apply(Evaluacion entity) {

        EvaluacionDTO dto = new EvaluacionDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            dto.setBlCalificacion(Constantes.AfirmacionNegacion.S.equals(entity.getItCalificacion())
                    ? Boolean.TRUE
                    : Boolean.FALSE);

            if (entity.getListaApartado() != null) {
                dto.setListaApartadoDTO(new ArrayList<>());
                entity.getListaApartado().forEach(e -> {
                    if (e.getFcBajaFila() == null) {
                        dto.getListaApartadoDTO().add(ApartadoToDTO.INSTANCE.apply(e));
                    }
                });
            }

            if (entity.getListaEvaluacionNota() != null) {
                dto.setListaEvaluacionNotaDTO(new ArrayList<>());
                entity.getListaEvaluacionNota().forEach(e -> dto.getListaEvaluacionNotaDTO()
                        .add(EvaluacionNotaToDTO.INSTANCE.apply(e)));
            }
            
            if (entity.getInforme() != null) {
                dto.setInformeDTO(InformeToDTO.INSTANCE.apply(entity.getInforme()));
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
    private void replaceNull(EvaluacionDTO dto, Evaluacion entity) {

        if (StringUtils.isBlank(entity.getTlDescripcion())) {
            dto.setTlDescripcion(StringUtils.EMPTY);
        }

    }
}
