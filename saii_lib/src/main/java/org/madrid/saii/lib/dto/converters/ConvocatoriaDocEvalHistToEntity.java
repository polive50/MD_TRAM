package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaDocHist
 *
 * @author everis
 */
public enum ConvocatoriaDocEvalHistToEntity implements Function<ConvocatoriaDocEvalHistDTO, ConvocatoriaDocEvalHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalHist apply(ConvocatoriaDocEvalHistDTO dto) {

        ConvocatoriaDocEvalHist entity = null;

        if (dto != null) {

            entity = new ConvocatoriaDocEvalHist();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdConvocatoria() != null) {
                entity.setIdConvocatoria(dto.getIdConvocatoria());
            }
            if (dto.getIdTipoDocumento().getIdValor() != null) {
                entity.setIdTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoDocumento()));
            }
            
            if(dto.getPersona() != null && dto.getPersona().getIdInteresado() != null) {
                entity.setPersona(InteresadoToEntity.INSTANCE.apply(dto.getPersona()));
            }
            
            if(dto.getIdConvocatoriaDocEval() != null) {
                entity.setIdConvocatoriaDocEval(dto.getIdConvocatoriaDocEval());
            }

            if (dto.getEstado() != null && dto.getEstado().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstado()));
            }

        }

        return entity;
    }
}
