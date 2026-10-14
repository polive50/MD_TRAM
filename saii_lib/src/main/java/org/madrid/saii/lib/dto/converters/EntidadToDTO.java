package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Entidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Entidad
 *
 * @author everis
 */
public enum EntidadToDTO implements Function<Entidad, EntidadDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EntidadDTO apply(Entidad entity) {

        EntidadDTO dto = new EntidadDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
            dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            dto.setTipoEntidadDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoEntidad()));
            
            
            // TODO descomentar
            /*
             * if(entity.getInteresado() != null){
             * dto.setInteresadoDTO(entity.getInteresado()); }
             */

            /*
             * if(entity.getEstado() != null){
             * dto.setEstadoDTO(entity.getEstado()); }
             */

        }

        return dto;
    }
}
