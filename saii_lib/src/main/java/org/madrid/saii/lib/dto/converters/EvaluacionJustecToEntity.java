package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Evaluacion
 *
 * @author nttdata
 */
public enum EvaluacionJustecToEntity implements Function<EvaluacionJustecDTO, EvaluacionJustec> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionJustec apply(EvaluacionJustecDTO dto) {

        EvaluacionJustec entity = null;

        if (dto != null) {

            entity = new EvaluacionJustec();
            BeanUtils.copyProperties(dto, entity);

            entity.setItCalificacion(
                    Boolean.TRUE.equals(dto.getBlCalificacion()) ? Constantes.AfirmacionNegacion.S
                            : Constantes.AfirmacionNegacion.N);

            if (dto.getListaEvaluacionJustecNotaDTO() != null) {
                entity.setListaEvaluacionJustecNota(new ArrayList<>());
                for (EvaluacionJustecNotaDTO en : dto.getListaEvaluacionJustecNotaDTO()) {
                    entity.getListaEvaluacionJustecNota()
                            .add(EvaluacionJustecNotaToEntity.INSTANCE.apply(en));
                }
            }

            if (dto.getListaEvaluacionJustecApartaDTO() != null) {
                entity.setListaEvaluacionJustecAparta(new ArrayList<>());
                for (EvaluacionJustecApartaDTO en : dto.getListaEvaluacionJustecApartaDTO()) {
                    entity.getListaEvaluacionJustecAparta()
                            .add(EvaluacionJustecApartaToEntity.INSTANCE.apply(en));
                }
            }

            if (dto.getImagenLogoDTO() != null && dto.getImagenLogoDTO().getIdImagen() != null) {
                entity.setImagenLogo(ImagenToEntity.INSTANCE.apply(dto.getImagenLogoDTO()));
            }

        }

        return entity;
    }
}
