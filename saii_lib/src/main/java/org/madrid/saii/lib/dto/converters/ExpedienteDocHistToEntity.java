package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteDocHist
 *
 * @author everis
 */
public enum ExpedienteDocHistToEntity implements Function<ExpedienteDocHistDTO, ExpedienteDocHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocHist apply(ExpedienteDocHistDTO dto) {

        ExpedienteDocHist entity = null;

        if (dto != null) {

            entity = new ExpedienteDocHist();

            BeanUtils.copyProperties(dto, entity);

            entity.setExpedienteDoc(
                    ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            
            if (dto.getTramiteDTO() != null) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }
            
            if (dto.getTipoDocumentoDTO() != null) {
                entity.setTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDocumentoDTO()));
            }

            if (dto.getConvocatoriaCauDTO() != null) {
                entity.setConvocatoriaCau(
                        ConvocatoriaCauToEntity.INSTANCE.apply(dto.getConvocatoriaCauDTO()));
            }
            
            if (dto.getConvocatoriaCauContDTO() != null) {
                entity.setConvocatoriaCauCont(
                        ConvocatoriaCauContToEntity.INSTANCE.apply(dto.getConvocatoriaCauContDTO()));
            }
        }

        return entity;
    }
}
