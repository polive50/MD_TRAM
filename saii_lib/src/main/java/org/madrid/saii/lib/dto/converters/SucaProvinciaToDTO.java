package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;

import org.madrid.saii.lib.domain.SucaProvincia;
import org.madrid.saii.lib.dto.CatalogoDTO;

import com.google.common.base.Function;

/**
 * Clase que transforma la entidad de dominio en DTO.
 *
 * @author everis
 *
 */
public enum SucaProvinciaToDTO implements Function<SucaProvincia, CatalogoDTO>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public CatalogoDTO apply(final SucaProvincia input) {
        CatalogoDTO output = new CatalogoDTO();
        if (input != null && input.getCdProv() != null) {
            output.setCodigo(input.getCdProv());
            output.setDescripcion(input.getDsProv());

        }
        return output;
    }
}
