package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaDoc
 *
 * @author everis
 */
public enum ConvocatoriaDocEvalToEntity implements Function<ConvocatoriaDocEvalDTO, ConvocatoriaDocEval> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaDocEval apply(ConvocatoriaDocEvalDTO dto) {

        ConvocatoriaDocEval entity = null;

        if (dto != null) {

            entity = new ConvocatoriaDocEval();

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

            if (dto.getEstado() != null && dto.getEstado().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstado()));
            }
        }

        return entity;
    }
}
