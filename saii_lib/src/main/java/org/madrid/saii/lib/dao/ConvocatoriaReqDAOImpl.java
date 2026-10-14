package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaReq
 *
 * @author nttdata
 */
@Repository("convocatoriaReqDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaReqDAOImpl extends BaseDAOImpl<ConvocatoriaReq, Integer>
        implements ConvocatoriaReqDAO {
    
    @Override
    @SuppressWarnings("unchecked")
    public ConvocatoriaReq buscarConvocatoriaReqByIdConvocatoria(Integer idConvocatoria) {
        
       
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT conreq from ConvocatoriaReq as conreq "); 
        queryHql.append("where conreq.idConvocatoria = :idConvocatoria ");
        queryHql.append("and conreq.fcBajaFila IS NULL");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        
        ConvocatoriaReq executeUniqueHQLQuery =  (ConvocatoriaReq) super.executeUniqueHQLQuery(
                queryHql.toString(), params);

        return executeUniqueHQLQuery;
    }

}
