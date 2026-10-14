package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import atlas.core.exceptions.ServiceException;

/**
 * Parser Entity to DTO for ConvocatoriaAct
 *
 * @author everis
 */
public enum ConvocatoriaActToDTO implements Function<ConvocatoriaAct, ConvocatoriaActDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaActDTO apply(ConvocatoriaAct entity) {

        ConvocatoriaActDTO dto = new ConvocatoriaActDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getTipoActor() != null) {
                dto.setTipoActorDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoActor()));
            }

            if (entity.getTipoActorFisJur() != null) {
                dto.setTipoPersonaFisJurDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoActorFisJur()));
                
                dto.setTipoPersonaFisJurNombre(dto.getTipoPersonaFisJurDTO().getDsValor());
                
                dto.setDsValor(dto.getTipoPersonaFisJurDTO().getDsValor());
            }

            if (entity.getIdentificacion() != null) {
                dto.setIdentificacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdentificacion()));
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItContrato())) {
                dto.setItContrato(Boolean.TRUE);
            } else {
                dto.setItContrato(Boolean.FALSE);
            }
            
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItContratoIndefinido())) {
                dto.setItContratoIndefinido(Boolean.TRUE);
            } else {
                dto.setItContratoIndefinido(Boolean.FALSE);
            }

            if (Objects.nonNull(entity.getIdActorCoordinador())) {
                dto.setItTipoActorPadre("C");
            }
            if (Objects.nonNull(entity.getIdActorSolicitante())) {
                dto.setItTipoActorPadre("S");
            }

            dto.setBolGeneraExpediente(
                    Constantes.AfirmacionNegacion.S.equals(entity.getItGeneraExpediente()));
            this.setDescripcionCorta(dto, entity);

            this.calcularTipoActor(dto);
            
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItCumplimentaFormulario())) {
                dto.setItCumplimentaFormulario(Boolean.TRUE);
            } else {
                dto.setItCumplimentaFormulario(Boolean.FALSE);
            }
            
        }

        return dto;
    }

    /**
     * Crea la descripcion Corta
     *
     * @param dto
     * @param entity
     */
    private void setDescripcionCorta(ConvocatoriaActDTO dto, ConvocatoriaAct entity) {

        if (StringUtils.isNotBlank(entity.getTlDescripcion())
                && entity.getTlDescripcion().length() > 50) {
            dto.setTlDescripcionCorta(entity.getTlDescripcion().substring(0, 50).concat("..."));
        } else {
            dto.setTlDescripcionCorta(entity.getTlDescripcion());
        }

    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ConvocatoriaActDTO dto, ConvocatoriaAct entity) {

        if (StringUtils.isBlank(entity.getTlDescripcion())) {
            dto.setTlDescripcion(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getItGeneraExpediente())) {
            dto.setItGeneraExpediente(StringUtils.EMPTY);
        }
    }

    /**
     *
     * @param dto
     */
    public void calcularTipoActor(ConvocatoriaActDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        dto.setEsActorCoordinador(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR).equals(dto.getTipoActorDTO().getIdValor()));
        dto.setEsActorParticipante(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE).equals(dto.getTipoActorDTO().getIdValor()));
        dto.setEsActorSolicitante(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE).equals(dto.getTipoActorDTO().getIdValor()));
    }
}
