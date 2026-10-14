package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ListadoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Informe
 *
 * @author nttdata
 */
public enum ListadoToEntity implements Function<ListadoDTO, Listado> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Listado apply(ListadoDTO dto) {

        Listado entity = null;

        if (dto != null) {

            entity = new Listado();
            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
