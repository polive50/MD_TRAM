package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.SucaProvincia;
import org.madrid.saii.lib.dto.CatalogoDTO;

import com.google.common.base.Function;

/**
 * Clase que transforma el DTO a la entidad de dominio.
 *
 * @author everis
 *
 */
public enum SucaProvinciaToEntity implements Function<CatalogoDTO, SucaProvincia>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public SucaProvincia apply(final CatalogoDTO input) {
        SucaProvincia output = null;
        if (input != null && StringUtils.isNotBlank(input.getCodigo())) {
            output = new SucaProvincia();
            output.setCdProv(input.getCodigo());
            output.setDsProv(input.getDescripcion());
        }
        return output;
    }
}
