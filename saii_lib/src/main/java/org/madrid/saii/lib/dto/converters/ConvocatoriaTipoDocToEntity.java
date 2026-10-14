package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaTip
 *
 * @author everis
 */
public enum ConvocatoriaTipoDocToEntity
        implements Function<ConvocatoriaTipoDocDTO, ConvocatoriaTipoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaTipoDoc apply(ConvocatoriaTipoDocDTO dto) {

        ConvocatoriaTipoDoc entity = null;

        if (dto != null) {

            entity = new ConvocatoriaTipoDoc();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTipoDocumentoDTO() != null) {
                entity.setTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDocumentoDTO()));
            }

            if (dto.getTramiteDTO() != null) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            if (Boolean.TRUE.equals(dto.getItObligatorio())) {
                entity.setItObligatorio(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItObligatorio(Constantes.AfirmacionNegacion.N);
            }
            
            if (Boolean.TRUE.equals(dto.getItPermiDescaEval())) {
                entity.setItPermiDescaEval(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItPermiDescaEval(Constantes.AfirmacionNegacion.N);
            }
            
            if (Boolean.TRUE.equals(dto.getItConsentimiento())) {
                entity.setItConsentimiento(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItConsentimiento(Constantes.AfirmacionNegacion.N);
            }
            
            if (dto.getTipoICDA() != null && dto.getTipoICDA().getIdValor() != null) {
                entity.setTipoICDA(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoICDA()));
            }
            
            if (dto.getCodProcedimiento() != null && dto.getCodProcedimiento().getIdValor() != null) {
                entity.setCodProcedimiento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getCodProcedimiento()));
            }

        }

        return entity;
    }
}
