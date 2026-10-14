package org.madrid.saii.lib.dto.converters;


import org.madrid.saii.lib.domain.Configuracion;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConfiguracionToEntity implements Function<ConfiguracionDTO, Configuracion> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Configuracion apply(ConfiguracionDTO dto) {

        Configuracion entity = null;

        if (dto != null) {

            entity = new Configuracion();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}

