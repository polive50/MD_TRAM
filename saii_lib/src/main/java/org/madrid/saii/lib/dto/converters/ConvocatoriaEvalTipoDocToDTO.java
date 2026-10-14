package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
public enum ConvocatoriaEvalTipoDocToDTO implements Function<ConvocatoriaEvalTipoDoc, ConvocatoriaEvalTipoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaEvalTipoDocDTO apply(ConvocatoriaEvalTipoDoc entity) {

        ConvocatoriaEvalTipoDocDTO dto = new ConvocatoriaEvalTipoDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdConvocatoria() != null) {
                dto.setIdConvocatoria(entity.getIdConvocatoria());
            }
            if (entity.getIdTipoDocumento() != null && entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setTipoDocumentoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }
            if(entity.getIdNivel() != null && entity.getIdNivel().getIdValor() !=null ) {
                dto.setNivelAsociacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdNivel()));
            }
            dto.setItObligatorio(Constantes.AfirmacionNegacion.S.equals(entity.getItObligatorio()));
        }

        return dto;
    }
}
