
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;

/**
 * Implementacion para el DAO de la clase de dominio CatalogosValores
 *
 * @author everis
 */
@Repository("catalogosValoresDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class CatalogosValoresDAOImpl extends BaseDAOImpl<CatalogosValores, Integer>
        implements CatalogosValoresDAO {
    
    @Override
    public Integer countDistinctDsValor() {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("select count(distinct cv.dsValor) from CatalogosValores cv");

        Map<String, Object> params = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Long> lista = (List<Long>) super.executeHQLQueryList(queryHql.toString(), params);
        // El select count devuelve un solo valor, el cual es la cantidad de
        // catalogosValores que hay sin que se repita su dsValor. Por ello, solo
        // obtenemos el primer valor de la lista. Ademas, tenemos que
        // transformarlo a Integer, ya que "Select Count" devuelve un valor de
        // tipo Long
        return (int) (long) lista.get(0);
    }

    /**
     * Implementacion para el DAO de la clase de dominio CatalogosValores
     *
     * @author everis
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CatalogosValoresDTO> obtenerCatalogoConvocatoria(
            Integer idConvocatoria) {

        List<CatalogosValoresDTO> listaValores = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("distinct cva.idValor, cva.dsValor, cva.nmConstante ");
        queryHql.append("FROM ");
        queryHql.append("ConvocatoriaCauEval cce ");
        queryHql.append("inner join CatalogosValores cva ");
        queryHql.append("on cce.causaEval.idValor = cva.idValor ");
        queryHql.append("WHERE ");
        //queryHql.append("cva.catalogo.idCatalogo = :idCatalogo ");
        queryHql.append("cva.fcBajaFila IS NULL ");
        queryHql.append("AND cce.idConvocatoria = :idConvocatoria");

        Map<String, Object> params = new HashMap<>();
        
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            CatalogosValoresDTO valorDto = new CatalogosValoresDTO();
            valorDto.setIdValor((Integer) object[0]);
            valorDto.setDsValor((String) object[1]);
            valorDto.setNmConstante((Integer) object[2]);
            listaValores.add(valorDto);

        }
        return listaValores;
    }

    
    @SuppressWarnings("unchecked")
    @Override
    public List<CatalogosValoresDTO> obtenerTipoEntidades() {
        List<CatalogosValoresDTO> listaValores = new ArrayList<>();
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append(" cva.idValor, cva.dsValor, cva.nmConstante  ");
        queryHql.append("FROM ");
        queryHql.append("CatalogosValores cva ");
        queryHql.append("WHERE ");
        queryHql.append("cva.catalogo.nmConstante = :nmConstanteCatalogo ");
        queryHql.append("AND cva.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("nmConstanteCatalogo", Constantes.Cata.TIPO_ENTIDAD);
       
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            CatalogosValoresDTO valorDto = new CatalogosValoresDTO();
            valorDto.setIdValor((Integer) object[0]);
            valorDto.setDsValor((String) object[1]);
            valorDto.setNmConstante((Integer) object[2]);
            listaValores.add(valorDto);

        }
        return listaValores;
        
       
    }
    
public List<CatalogosValores> cargaNmConstante() {
        
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
        return this.find(filtros, null);

    }

public List<CatalogosValores> cargaNmConstanteObjeto() {
    
    List<AtlasFilter> filtros = new ArrayList<>();
    return this.find(filtros, null);
}
  /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CatalogosValoresDTO> obtenerCatalogoConvocatoriaTipoCausa(Integer idConvocatoria,
            Integer idTipoCausa) {
        List<CatalogosValoresDTO> listaValores = new ArrayList<>();
    
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ");
        queryHql.append(" DISTINCT cv.idValor, cv.dsValor, cv.nmConstante ");
        queryHql.append(" FROM ");
        queryHql.append(" CatalogosValores cv ");
        queryHql.append(" INNER JOIN ConvocatoriaCau cc ");
        queryHql.append(" ON cv.idValor = cc.causa.idValor ");
        queryHql.append(" WHERE ");
        queryHql.append(" cv.fcBajaFila IS NULL ");
        queryHql.append(" AND cc.fcBajaFila IS NULL ");
        queryHql.append(" AND cc.idConvocatoria = :idConvocatoria ");
        queryHql.append(" AND cc.tipoCausa.idValor = :idTipoCausa ");
        queryHql.append(" ORDER BY cv.dsValor ");
    
        Map<String, Object> params = new HashMap<>();
        
        params.put("idConvocatoria", idConvocatoria);
        params.put("idTipoCausa", idTipoCausa);
    
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);
    
        Iterator<?> it = executeHQLQueryList.iterator();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();
    
            CatalogosValoresDTO valorDto = new CatalogosValoresDTO();
            valorDto.setIdValor((Integer) object[0]);
            valorDto.setDsValor((String) object[1]);
            valorDto.setNmConstante((Integer) object[2]);
            listaValores.add(valorDto);
    
        }
        return listaValores;
    }

}
