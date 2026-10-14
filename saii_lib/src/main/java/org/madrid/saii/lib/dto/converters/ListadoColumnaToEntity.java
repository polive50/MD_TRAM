package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoColumnaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ListadoColumna
 *
 * @author nttdata
 */
public enum ListadoColumnaToEntity implements Function<ListadoColumnaDTO, ListadoColumna> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ListadoColumna apply(ListadoColumnaDTO dto) {

        ListadoColumna entity = null;

        if (dto != null) {

            entity = new ListadoColumna();

            BeanUtils.copyProperties(dto, entity);
        }

        return entity;
    }
}
