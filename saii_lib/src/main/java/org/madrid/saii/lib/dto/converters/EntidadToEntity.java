package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Entidad;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Entidad
 *
 * @author everis
 */
public enum EntidadToEntity implements Function<EntidadDTO, Entidad> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Entidad apply(EntidadDTO dto) {

        Entidad entity = null;

        if (dto != null) {

            entity = new Entidad();

            BeanUtils.copyProperties(dto, entity);

            if (Boolean.TRUE.equals(dto.getEstadoDTO() != null)) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

            if (Boolean.TRUE.equals(dto.getInteresadoDTO() != null)) {
                entity.setInteresado(InteresadoToEntity.INSTANCE.apply(dto.getInteresadoDTO()));
            }
            
            if(Boolean.TRUE.equals(dto.getTipoEntidadDTO() != null) && Boolean.TRUE.equals(dto.getTipoEntidadDTO().getIdValor() != null)) {
                entity.setTipoEntidad(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoEntidadDTO()));
            }else {
                entity.setTipoEntidad(null);
            }

        }

        return entity;
    }
}
