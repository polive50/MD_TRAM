package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Apartado
 *
 * @author nttdata
 */
public enum ApartadoToDTO implements Function<Apartado, ApartadoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ApartadoDTO apply(Apartado entity) {

        ApartadoDTO dto = new ApartadoDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
            
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItInformativo())) {
                dto.setBolItInformativo(Boolean.TRUE);
            } else {
                dto.setBolItInformativo(Boolean.FALSE);
            }
            
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItObligatorio())) {
                dto.setBolItObligatorio(Boolean.TRUE);
            } else {
                dto.setBolItObligatorio(Boolean.FALSE);
            }
            
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ApartadoDTO dto, Apartado entity) {

        if (StringUtils.isBlank(entity.getTlTexto())) {
            dto.setTlTexto(StringUtils.EMPTY);
        }
    }
}
