package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import atlas.core.exceptions.ServiceException;

/**
 * Parser Entity to DTO for ExpedienteDoc
 *
 * @author everis
 */
public enum ExpedienteDocToDTO implements Function<ExpedienteDoc, ExpedienteDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteDocDTO apply(ExpedienteDoc entity) {

        ExpedienteDocDTO dto = new ExpedienteDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
            
            if (null != entity.getTlObservaciones()) {
                dto.setTlObservaciones(
                        entity.getTlObservaciones());
            }
            
            if (null != entity.getIdDocumentoPtfr()) {
                dto.setIdDocumentoPtfr(entity.getIdDocumentoPtfr());
            }
            
            if(null!=entity.getEstado() && entity.getEstado().getIdValor() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }            

            if (entity.getTramite() != null && entity.getTramite().getIdValor() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            if (entity.getTipoDocumento() != null
                    && entity.getTipoDocumento().getIdValor() != null) {
                dto.setTipoDocumentoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoDocumento()));
            }

            if (entity.getConvocatoriaCau() != null && entity.getConvocatoriaCau().getIdConvocatoriaCau() != null) {
                dto.setConvocatoriaCauDTO(
                        ConvocatoriaCauToDTO.INSTANCE.apply(entity.getConvocatoriaCau()));
            }

            if (entity.getBloqueComponente() != null && entity.getBloqueComponente().getIdBloqueComponente() != null) {
                dto.setBloqueComponenteDTO(
                        BloqueComponenteToDTO.INSTANCE.apply(entity.getBloqueComponente()));

            }

            this.calcularEstilo(dto);
            dto.setEsPresentado(this.calcularEstadoDocumento(dto));
            
            if (entity.getExpediente() != null && entity.getExpediente().getIdExpediente() != null) {
                if (dto.getExpedienteDTO() == null) {
                    dto.setExpedienteDTO(new ExpedienteDTO());
                }
                dto.getExpedienteDTO().setIdExpediente(entity.getExpediente().getIdExpediente());
            }
            
            if (entity.getTlObservacionesGestor()!= null) { 
                dto.setTlObservacionesGestor(
                        entity.getTlObservacionesGestor());
            }
            
            if (entity.getConvocatoriaCauCont() != null && entity.getConvocatoriaCauCont().getIdConvocatoriaCauCont() != null) {
                dto.setConvocatoriaCauContDTO(
                        ConvocatoriaCauContToDTO.INSTANCE.apply(entity.getConvocatoriaCauCont()));
            }
            
            if (entity.getDato() != null && entity.getDato().getIdDato() != null) {
                dto.setDatoDTO(
                        DatoToDTO.INSTANCE.apply(entity.getDato()));

            }

        }

        return dto;
    }

    /**
     *
     * Calcular el estilo del registro
     *
     * @param dto
     * @return
     */
    private void calcularEstilo(final ExpedienteDocDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setEstilo("colorVerde");
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setEstilo("colorRojo");
        }
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION).equals(dto.getEstadoDTO().getIdValor())) {
            dto.setEstilo("colorAmarillo");

        }
    }

    /**
     * Metodo que calcula si el documento está en estado presentado
     *
     * @param dto
     * @return
     */
    private Boolean calcularEstadoDocumento(ExpedienteDocDTO dto) {
        Boolean bSalida = Boolean.FALSE;
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PRESENTADO).equals(dto.getEstadoDTO().getIdValor())) {
            bSalida = Boolean.TRUE;
        }
        return bSalida;

    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteDocDTO dto, ExpedienteDoc entity) {
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

    /**
     * Metodo para convertir historico a dto
     *
     * @param expedienteDocHist
     * @return
     */
    public ExpedienteDocDTO applyFromHistory(ExpedienteDocHist entity) {

        ExpedienteDocDTO dto = new ExpedienteDocDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setIdExpedienteDoc(entity.getIdExpedienteDocHist());

            dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));

            if (entity.getTramite() != null && entity.getTramite().getIdValor() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            if (entity.getTipoDocumento() != null
                    && entity.getTipoDocumento().getIdValor() != null) {
                dto.setTipoDocumentoDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoDocumento()));
            }

            if (entity.getConvocatoriaCau() != null) {
                dto.setConvocatoriaCauDTO(
                        ConvocatoriaCauToDTO.INSTANCE.apply(entity.getConvocatoriaCau()));
            }

            if (entity.getBloqueComponente() != null) {
                dto.setBloqueComponenteDTO(
                        BloqueComponenteToDTO.INSTANCE.apply(entity.getBloqueComponente()));

            }

            this.calcularEstilo(dto);
            dto.setEsPresentado(this.calcularEstadoDocumento(dto));

        }

        return dto;
    }
}
