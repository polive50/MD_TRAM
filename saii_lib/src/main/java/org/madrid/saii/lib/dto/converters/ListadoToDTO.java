package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ListadoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Informe
 *
 * @author nttdata
 */
public enum ListadoToDTO implements Function<Listado, ListadoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ListadoDTO apply(Listado entity) {

        ListadoDTO dto = new ListadoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ListadoDTO dto, Listado entity) {
        if (StringUtils.isBlank(entity.getDsTitulo())) {
            dto.setDsTitulo(StringUtils.EMPTY);
        }
    }
}
