package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaTip
 *
 * @author everis
 */
@Repository("convocatoriaTipoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaTipoDocDAOImpl extends BaseDAOImpl<ConvocatoriaTipoDoc, Integer>
        implements ConvocatoriaTipoDocDAO {
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ConvocatoriaTipoDoc> buscarNoConfiguradasImpresos(Integer idConvocatoria) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ctd ");
        queryHql.append(" FROM ConvocatoriaTipoDoc ctd ");
        queryHql.append(" WHERE ctd.idConvocatoria = :idConvocatoria ");
        queryHql.append(" AND ctd.idConvocatoriaTipoDoc NOT IN ");
        queryHql.append(" ( ");
        queryHql.append("   SELECT ct ");
        queryHql.append("   FROM ConvocatoriaTipoDoc ct ");
        queryHql.append("   INNER JOIN CatalogosValores cv ON ct.tipoDocumento.idValor = cv.idValor ");
        queryHql.append("   INNER JOIN Componente c ON cv.idValor = c.tipoDocumento.idValor ");
        queryHql.append("   INNER JOIN BloqueComponente bc ON c.idComponente = bc.componente.idComponente ");
        queryHql.append("   INNER JOIN Bloque b ON bc.bloque.idBloque = b.idBloque ");
        queryHql.append("   INNER JOIN Impreso i ON b.impreso.idImpreso = i.idImpreso AND i.convocatoria.idConvocatoria = :idConvocatoria ");
        queryHql.append(" ) ");
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        return (List<ConvocatoriaTipoDoc>) super.executeHQLQueryList(queryHql.toString(), params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ConvocatoriaTipoDoc> buscarPorConvocatoria(Integer idConvocatoria) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ctd ");
        queryHql.append(" FROM ConvocatoriaTipoDoc ctd ");
        queryHql.append(" WHERE ctd.idConvocatoria = :idConvocatoria ");
        queryHql.append(" AND ctd.fcBajaFila is null ");
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        return (List<ConvocatoriaTipoDoc>) super.executeHQLQueryList(queryHql.toString(), params);
    }

}
