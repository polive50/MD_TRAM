package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OpcionesComponenteCatalogoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /*
     * Atributo que representa la lista de opciones de un tipo de componente igual a catalogo
     */
    private List<CatalogosValoresDTO> opciones;
    
    public OpcionesComponenteCatalogoDTO () {
        super();
    }

}
