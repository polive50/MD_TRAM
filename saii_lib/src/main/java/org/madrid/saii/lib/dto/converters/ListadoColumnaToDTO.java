package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoColumnaDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ListadoColumna
 *
 * @author nttdata
 */
public enum ListadoColumnaToDTO implements Function<ListadoColumna, ListadoColumnaDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ListadoColumnaDTO apply(ListadoColumna entity) {

        ListadoColumnaDTO dto = new ListadoColumnaDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }

}
