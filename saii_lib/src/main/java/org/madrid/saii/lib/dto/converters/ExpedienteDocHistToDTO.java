package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteDocHist
 *
 * @author everis
 */
public enum ExpedienteDocHistToDTO implements Function<ExpedienteDocHist, ExpedienteDocHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocHistDTO apply(ExpedienteDocHist entity) {

        ExpedienteDocHistDTO dto = new ExpedienteDocHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            
            if (entity.getTramite() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }
            
            if (entity.getTipoDocumento() != null) {
                dto.setTipoDocumentoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoDocumento()));
            }

            if (entity.getConvocatoriaCau() != null) {
                dto.setConvocatoriaCauDTO(
                        ConvocatoriaCauToDTO.INSTANCE.apply(entity.getConvocatoriaCau()));
            }
            
            if (entity.getConvocatoriaCauCont() != null) {
                dto.setConvocatoriaCauContDTO(
                        ConvocatoriaCauContToDTO.INSTANCE.apply(entity.getConvocatoriaCauCont()));
            }

            this.calcularMostrarBotonObservaciones(dto);

        }

        return dto;
    }

    /**
     * Calcular mostrarBotonObservaciones
     *
     * @param dto
     * @return
     */
    private void calcularMostrarBotonObservaciones(final ExpedienteDocHistDTO dto) {

        dto.setMostrarBotonObservaciones(Boolean.FALSE);

        if (dto.getTlObservaciones() != null) {
            dto.setMostrarBotonObservaciones(Boolean.TRUE);
        }

    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteDocHistDTO dto, ExpedienteDocHist entity) {

        if (StringUtils.isBlank(entity.getCdAnexoEreg())) {
            dto.setCdAnexoEreg(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getCdAnexoEregPadre())) {
            dto.setCdAnexoEregPadre(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getTlCausaDescripcion())) {
            dto.setTlCausaDescripcion(StringUtils.EMPTY);
        }
    }
}
