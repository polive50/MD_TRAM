package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for EvaluacionTipoDoc
 *
 * @author everis
 */
public enum EvaluacionTipoDocToEntity implements Function<EvaluacionTipoDocDTO, EvaluacionTipoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EvaluacionTipoDoc apply(EvaluacionTipoDocDTO dto) {

        EvaluacionTipoDoc entity = null;

        if (dto != null) {
            entity = new EvaluacionTipoDoc();
            BeanUtils.copyProperties(dto, entity);
           
            if (dto.getIdTipoDocumento().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoDocumento()));
            }
            
            if (Boolean.TRUE.equals(dto.getItObligatorio())) {
                entity.setItObligatorio(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItObligatorio(Constantes.AfirmacionNegacion.N);
            }

        }

        return entity;
    }
}
