package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Catalogos
 *
 * @author everis
 */
public enum CatalogosToEntity implements Function<CatalogosDTO, Catalogos> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Catalogos apply(CatalogosDTO dto) {

        Catalogos entity = null;

        if (dto != null) {

            entity = new Catalogos();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdCatalogo(dto.getIdCatalogo());

        }

        return entity;
    }
}
