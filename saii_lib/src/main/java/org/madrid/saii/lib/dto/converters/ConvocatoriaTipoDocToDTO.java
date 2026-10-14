package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaTip
 *
 * @author everis
 */
public enum ConvocatoriaTipoDocToDTO
        implements Function<ConvocatoriaTipoDoc, ConvocatoriaTipoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaTipoDocDTO apply(ConvocatoriaTipoDoc entity) {

        ConvocatoriaTipoDocDTO dto = new ConvocatoriaTipoDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getTipoDocumento() != null) {
                dto.setTipoDocumentoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoDocumento()));
            }
            
            if (entity.getTipoICDA() != null) {
                dto.setTipoICDA(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoICDA()));
            }
            
            if (entity.getCodProcedimiento() != null) {
                dto.setCodProcedimiento(CatalogosValoresToDTO.INSTANCE.apply(entity.getCodProcedimiento()));
            }

            if (entity.getTramite() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            dto.setItObligatorio(Constantes.AfirmacionNegacion.S.equals(entity.getItObligatorio()));
            
            dto.setItConsentimiento(Constantes.AfirmacionNegacion.S.equals(entity.getItConsentimiento()));
            
            dto.setItPermiDescaEval(Constantes.AfirmacionNegacion.S.equals(entity.getItPermiDescaEval()));
            
            dto.setItConsentimiento(Constantes.AfirmacionNegacion.S.equals(entity.getItConsentimiento()));

        }

        return dto;
    }
}
