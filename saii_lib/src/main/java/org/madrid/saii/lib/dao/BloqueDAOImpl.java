package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.converters.BloqueToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Bloque
 *
 * @author everis
 */
@Repository("bloqueDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class BloqueDAOImpl extends BaseDAOImpl<Bloque, Integer> implements BloqueDAO {
    
    @Override
    @SuppressWarnings("unchecked")
    public Bloque buscarIdBloquePadre(Integer idBloquePadre) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT bloque");
        queryHql.append(" FROM Bloque bloque ");
        queryHql.append(" WHERE ");
        queryHql.append(" bloque.idBloquePadre = :idBloquePadre ");
        queryHql.append(" AND bloque.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idBloquePadre", idBloquePadre);
   
        return (Bloque) super.executeUniqueHQLQuery(
                queryHql.toString(), params);
  
    }
    
   
    @Override
    public List<Bloque> listBloquesMultiplesPorBloquePadre(Integer idBloquePadre) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT bloque");
        queryHql.append(" FROM Bloque bloque ");
        queryHql.append(" WHERE ");
        queryHql.append(" bloque.idBloquePadre = :idBloquePadre ");
        queryHql.append(" AND bloque.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idBloquePadre", idBloquePadre);

        @SuppressWarnings("unchecked")
        List<Bloque> executeHQLQueryList = (List<Bloque>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }
    
    @Override
    public List<BloqueDTO> lstBloqueMultipleByIdBloquePadre(Integer idBloquePadre) {
        
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_BLOQUE_PADRE,
                idBloquePadre, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), BloqueToDTO.INSTANCE));
    }
}
