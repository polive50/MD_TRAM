package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Bloque
 *
 * @author everis
 */
public enum BloqueToEntity implements Function<BloqueDTO, Bloque> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Bloque apply(BloqueDTO dto) {

        Bloque entity = null;

        if (dto != null) {

            entity = new Bloque();
            BeanUtils.copyProperties(dto, entity);

            // Impreso
            Impreso impreso = new Impreso();
            impreso.setIdImpreso(dto.getIdImpreso());
            entity.setImpreso(impreso);

            if (dto.getIdFormato() != null && dto.getIdFormato().getIdValor() != null) {
                entity.setIdFormato(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdFormato()));
            }
            if (dto.getIdTematica() != null && dto.getIdTematica().getIdValor() != null) {
                entity.setIdTematica(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTematica()));
            }
            if (dto.getActor() != null && dto.getActor().getIdActor() != null) {
                // entity.getActor().setIdActor(dto.getActor().getIdActor());
                entity.setActor(ConvocatoriaActToEntity.INSTANCE.apply(dto.getActor()));
            }

            // BloquesComponentes
            if (!Objects.nonNull(dto.getLstBloqueComponenteDTO())
                    && !dto.getLstBloqueComponenteDTO().isEmpty()) {
                entity.setLstBloqueComponente(new ArrayList<>());
                Iterator<BloqueComponenteDTO> it = dto.getLstBloqueComponenteDTO().iterator();
                while (it.hasNext()) {
                    BloqueComponenteDTO bloqueComponenteDTO = it.next();
                    BloqueComponente bloqueComponente = BloqueComponenteToEntity.INSTANCE
                            .apply(bloqueComponenteDTO);
                    bloqueComponente.setBloque(entity);
                    entity.getLstBloqueComponente().add(bloqueComponente);
                }
            }

            if (dto.getItAutorizaConsulta() != null
                    && Boolean.TRUE.equals(dto.getItAutorizaConsulta())) {
                entity.setItAutorizaConsulta(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItAutorizaConsulta(Constantes.AfirmacionNegacion.N);
            }

            if (null != dto.getNmMaxPalabras() && 0 == dto.getNmMaxPalabras()) {
                entity.setNmMaxPalabras(null);
            }

        }

        return entity;
    }
}
