package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for CatalogosValores
 *
 * @author everis
 */
public enum CatalogosValoresToDTO implements Function<CatalogosValores, CatalogosValoresDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public CatalogosValoresDTO apply(CatalogosValores entity) {

        CatalogosValoresDTO dto = new CatalogosValoresDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getIdValor() != null && entity.getIdValor() != 0) {
                dto.setIdValor(entity.getIdValor());
            }

            if (StringUtils.isNotBlank(entity.getDsValor())) {
                dto.setDsValor(entity.getDsValor());
            }

            if (entity.getNmConstante() != null) {
                dto.setNmConstante(entity.getNmConstante());
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItMantenible())) {
                dto.setItMantenible(Boolean.TRUE);
            } else {
                dto.setItMantenible(Boolean.FALSE);
            }

            dto.setCatalogoDTO(CatalogosToDTO.INSTANCE.apply(entity.getCatalogo()));

        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(CatalogosValoresDTO dto, CatalogosValores entity) {

        if (StringUtils.isBlank(entity.getCdCodigo())) {
            dto.setCdCodigo(StringUtils.EMPTY);
        }
    }
}
