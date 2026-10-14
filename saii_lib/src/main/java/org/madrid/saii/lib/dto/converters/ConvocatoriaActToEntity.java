package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaAct
 *
 * @author everis
 */
public enum ConvocatoriaActToEntity implements Function<ConvocatoriaActDTO, ConvocatoriaAct> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaAct apply(ConvocatoriaActDTO dto) {

        ConvocatoriaAct entity = null;

        if (dto != null) {

            entity = new ConvocatoriaAct();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdConvocatoria(dto.getIdConvocatoria());

            if (dto.getIdentificacionDTO() != null) {
                entity.setIdentificacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getIdentificacionDTO()));
            }

            if (dto.getTipoActorDTO() != null) {
                entity.setTipoActor(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoActorDTO()));
            }

            if (dto.getTipoPersonaFisJurDTO().getIdValor() != null) {
                entity.setTipoActorFisJur(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoPersonaFisJurDTO()));
            }

            entity.setNmMin(dto.getNmMin());
            entity.setNmMax(dto.getNmMax());

            if (Boolean.TRUE.equals(dto.getItContrato())) {
                entity.setItContrato(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItContrato(Constantes.AfirmacionNegacion.N);
            }
            
            if (Boolean.TRUE.equals(dto.getItContratoIndefinido())) {
                entity.setItContratoIndefinido(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItContratoIndefinido(Constantes.AfirmacionNegacion.N);
            }

            if (Boolean.TRUE.equals(dto.getBolGeneraExpediente())) {
                entity.setItGeneraExpediente(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItGeneraExpediente(Constantes.AfirmacionNegacion.N);
            }
            
            if (Boolean.TRUE.equals(dto.getItCumplimentaFormulario())) {
                entity.setItCumplimentaFormulario(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItCumplimentaFormulario(Constantes.AfirmacionNegacion.N);
            }
            
        }

        return entity;
    }
}
