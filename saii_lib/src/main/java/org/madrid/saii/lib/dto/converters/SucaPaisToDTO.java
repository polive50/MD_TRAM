package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.SucaPais;
import org.madrid.saii.lib.dto.SucaPaisDTO;

import java.io.Serializable;
import com.google.common.base.Function;

public enum SucaPaisToDTO implements Function<SucaPais, SucaPaisDTO>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public SucaPaisDTO apply(final SucaPais input) {
        SucaPaisDTO output = new SucaPaisDTO();
        if (input != null && input.getCdPais() != null) {

            output.setCdPais(input.getCdPais());
            output.setDsPais(input.getDsPais());
            output.setDsGentilicioM(input.getDsGentilicioM());
            output.setDsGentilicioF(input.getDsGentilicioF());

        }
        return output;
    }
}
