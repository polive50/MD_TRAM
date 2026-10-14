package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio Componente
 *
 * @author everis
 */
@Repository("componenteDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ComponenteDAOImpl extends BaseDAOImpl<Componente, Integer> implements ComponenteDAO {
    
    
public List<Componente> cargaNmConstante() {
        
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
        return this.find(filtros, null);
        
    }
    

}
