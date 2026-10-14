package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteDoc
 *
 * @author everis
 */
public enum ExpedienteDocToEntity implements Function<ExpedienteDocDTO, ExpedienteDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDoc apply(ExpedienteDocDTO dto) {

        ExpedienteDoc entity = null;

        if (dto != null) {

            entity = new ExpedienteDoc();

            BeanUtils.copyProperties(dto, entity);
            if(dto.getEstadoDTO() != null && dto.getEstadoDTO().getIdValor() !=null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }
            
            if (dto.getTramiteDTO() != null && dto.getTramiteDTO().getIdValor() != null) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            if (dto.getTipoDocumentoDTO() != null
                    && dto.getTipoDocumentoDTO().getIdValor() != null) {
                entity.setTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDocumentoDTO()));
            }

            if (dto.getConvocatoriaCauDTO() != null && dto.getConvocatoriaCauDTO().getIdConvocatoriaCau() != null) {
                entity.setConvocatoriaCau(
                        ConvocatoriaCauToEntity.INSTANCE.apply(dto.getConvocatoriaCauDTO()));
            }
            
            if (dto.getConvocatoriaCauContDTO() != null && dto.getConvocatoriaCauContDTO().getIdConvocatoriaCauCont() != null) {
                entity.setConvocatoriaCauCont(
                        ConvocatoriaCauContToEntity.INSTANCE.apply(dto.getConvocatoriaCauContDTO()));
            }

            if (dto.getBloqueComponenteDTO() != null && dto.getBloqueComponenteDTO().getIdBloqueComponente() != null) {
                entity.setBloqueComponente(
                        BloqueComponenteToEntity.INSTANCE.apply(dto.getBloqueComponenteDTO()));
            }
            
            if (dto.getDatoDTO() != null && dto.getDatoDTO().getIdDato() != null) {
                entity.setDato(
                        DatoToEntity.INSTANCE.apply(dto.getDatoDTO()));
            }
            
            if (dto.getExpedienteDTO() != null && dto.getExpedienteDTO().getIdExpediente() != null) {
                entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));
            }
            
            if (dto.getIdDocumentoPtfr() != null) {
                entity.setIdDocumentoPtfr(dto.getIdDocumentoPtfr());
            }
            
            
        }

        return entity;
    }
}
