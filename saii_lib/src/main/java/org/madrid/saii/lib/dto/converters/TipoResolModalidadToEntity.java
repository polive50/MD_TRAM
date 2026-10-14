package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for TipoResolModalidadTo
 *
 * @author everis
 */
public enum TipoResolModalidadToEntity implements Function<TipoResolModalidadDTO, TipoResolModalidad>{
    
    /**
    *
    */
   INSTANCE;

    /**
    *
    */
    @Override
    public TipoResolModalidad apply(TipoResolModalidadDTO dto) {
        TipoResolModalidad entity = null;
        if (dto != null) {
            entity = new TipoResolModalidad();
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

}
