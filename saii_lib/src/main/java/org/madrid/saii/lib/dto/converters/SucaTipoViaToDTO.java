package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;

import org.madrid.saii.lib.domain.SucaTipoVial;
import org.madrid.saii.lib.dto.CatalogoDTO;

import com.google.common.base.Function;

/**
 * Clase que transforma la entidad de dominio en DTO.
 *
 * @author everis
 *
 */
public enum SucaTipoViaToDTO implements Function<SucaTipoVial, CatalogoDTO>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public CatalogoDTO apply(final SucaTipoVial input) {
        CatalogoDTO output = new CatalogoDTO();
        if (input != null) {
            output.setCodigo(input.getCdTvia());
            output.setDescripcion(input.getDsTvia());
        }
        return output;
    }
}
