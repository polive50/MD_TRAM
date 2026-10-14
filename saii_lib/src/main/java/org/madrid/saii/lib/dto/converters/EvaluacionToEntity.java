package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Evaluacion
 *
 * @author nttdata
 */
public enum EvaluacionToEntity implements Function<EvaluacionDTO, Evaluacion> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Evaluacion apply(EvaluacionDTO dto) {

        Evaluacion entity = null;

        if (dto != null) {

            entity = new Evaluacion();
            BeanUtils.copyProperties(dto, entity);

            entity.setItCalificacion(
                    Boolean.TRUE.equals(dto.getBlCalificacion()) ? Constantes.AfirmacionNegacion.S
                            : Constantes.AfirmacionNegacion.N);

            if (dto.getListaEvaluacionNotaDTO() != null) {
                entity.setListaEvaluacionNota(new ArrayList<>());
                for (EvaluacionNotaDTO en : dto.getListaEvaluacionNotaDTO()) {
                    entity.getListaEvaluacionNota().add(EvaluacionNotaToEntity.INSTANCE.apply(en));
                }
            }

            if (dto.getListaApartadoDTO() != null) {
                entity.setListaApartado(new ArrayList<>());
                for (ApartadoDTO en : dto.getListaApartadoDTO()) {
                    entity.getListaApartado().add(ApartadoToEntity.INSTANCE.apply(en));
                }
            }
            
            if (dto.getInformeDTO() != null) {
                entity.setInforme(InformeToEntity.INSTANCE.apply(dto.getInformeDTO()));
            }

            if (dto.getImagenLogoDTO() != null && dto.getImagenLogoDTO().getIdImagen() != null) {
                entity.setImagenLogo(ImagenToEntity.INSTANCE.apply(dto.getImagenLogoDTO()));
            }

        }

        return entity;
    }
}
