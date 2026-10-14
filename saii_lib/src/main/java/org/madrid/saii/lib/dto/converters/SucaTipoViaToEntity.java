package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.SucaTipoVial;
import org.madrid.saii.lib.dto.CatalogoDTO;

import com.google.common.base.Function;

/**
 * Clase que transforma el DTO a la entidad de dominio.
 *
 * @author everis
 *
 */
public enum SucaTipoViaToEntity implements Function<CatalogoDTO, SucaTipoVial>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public SucaTipoVial apply(final CatalogoDTO input) {
        SucaTipoVial output = null;
        if (input != null && StringUtils.isNotBlank(input.getCodigo())) {
            output = new SucaTipoVial();
            output.setCdTvia(input.getCodigo());
            output.setDsTvia(input.getDescripcion());
        }
        return output;
    }
}
