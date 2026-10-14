package org.madrid.saii.lib.dto.converters;


import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
public enum ConvocatoriaEvalTipoDocToEntity implements Function<ConvocatoriaEvalTipoDocDTO, ConvocatoriaEvalTipoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEvalTipoDoc apply(ConvocatoriaEvalTipoDocDTO dto) {

        ConvocatoriaEvalTipoDoc entity = null;

        if (dto != null) {

            entity = new ConvocatoriaEvalTipoDoc();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdConvocatoria() != null) {
                entity.setIdConvocatoria(dto.getIdConvocatoria());
            }
            if (dto.getTipoDocumentoDTO().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDocumentoDTO()));
            }
            
            if (dto.getNivelAsociacionDTO() != null && dto.getNivelAsociacionDTO().getIdValor() != null) {
                entity.setIdNivel(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getNivelAsociacionDTO()));
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
