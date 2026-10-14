
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Catalogos
 *
 * @author everis
 */
@Repository("catalogosDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class CatalogosDAOImpl extends BaseDAOImpl<Catalogos, Integer> implements CatalogosDAO {
    
    
    @Override
    public List<Catalogos> cargaNmConstante() {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
        return this.find(filtros, null);
        
    }
    

}
