package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for TipoResolucionConvocatoria
 *
 * @author quental
 */
public enum TipoResolModalidadToDTO implements Function<TipoResolModalidad, TipoResolModalidadDTO> {
    
    /**
    *
    */
    INSTANCE;

    /**
    *
    */
    @Override
    public TipoResolModalidadDTO apply(TipoResolModalidad entity) {
        TipoResolModalidadDTO dto = new TipoResolModalidadDTO();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }

}
