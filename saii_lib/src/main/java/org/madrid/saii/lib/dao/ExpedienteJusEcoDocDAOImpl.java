
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpJusEcoDocConceptoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteJusEcoDoc
 *
 * @author Quental
 */
@Repository("expedienteJusEcoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteJusEcoDocDAOImpl extends BaseDAOImpl<ExpedienteJusEcoDoc, Integer>
        implements ExpedienteJusEcoDocDAO {

    @Override
    public List<ExpJusEcoDocConceptoDTO> cargarExpJusEcoDocPartidaSeleccionada(
            ExpedienteJusEcoDocDTO expedienteJusEcoDocSeleccionado, Integer partidaSelecc) {
        
        List<ExpJusEcoDocConceptoDTO> resultado = new ArrayList<>();
        
        if (partidaSelecc != null) {
            StringBuilder queryHql = new StringBuilder();
            queryHql.append("SELECT jedoc.idExpedienteJusEcoDoc, ep.tlConcepto, epm.nmImporte, epm.nmImporteJustificado, ");
            queryHql.append("epm.nmImporteAdmitido, epm.estado.idValor, jedoc.expedienteDoc.idExpedienteDoc, epm.idExpedientePresupMonto ");
            queryHql.append("FROM ExpedienteJusEcoDoc jedoc, ExpedientePresupMonto epm, ExpedientePresup ep ");
            queryHql.append("WHERE jedoc.expedientePresupMonto.idExpedientePresupMonto = epm.idExpedientePresupMonto ");
            queryHql.append("and epm.idExpedientePresup = ep.idExpedientePresup ");
            queryHql.append("and jedoc.expedienteJusEco.idExpedienteJusEco = :idExpedienteJusEco "); 
            queryHql.append("and ep.tipoFinanciacion.idValor = :idTipoFinanciacion "); 
            queryHql.append("and epm.fcBajaFila IS NULL ");
            queryHql.append("and jedoc.fcBajaFila IS NULL ");
            queryHql.append("and ep.fcBajaFila IS NULL ");
    
            Map<String, Object> params = new HashMap<>();
            params.put("idExpedienteJusEco", expedienteJusEcoDocSeleccionado.getExpedienteJusEcoDTO().getIdExpedienteJusEco());
            params.put("idTipoFinanciacion", partidaSelecc);
            //params.put("idExpedientePresupMonto", expedienteJusEcoDocSeleccionado.getExpedientePresupMontoDTO().getIdExpedientePresupMonto());
            
            List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                    queryHql.toString(), params);

            for (Object[] row : executeHQLQueryList) {
                ExpJusEcoDocConceptoDTO dto = new ExpJusEcoDocConceptoDTO();
                dto.setIdExpedienteJusEcoDoc((Integer) row[0]);
                dto.setConcepto((String) row[1]);
                dto.setImporte((Double) row[2]);
                dto.setImporteJustificado((Double) row[3]);
                dto.setImporteAdmitido((Double) row[4]);
                dto.setIdEstado((Integer) row[5]);
                dto.setIdExpedienteDoc((Integer) row[6]);
                dto.setIdExpedientePresupMonto((Integer) row[7]);
                resultado.add(dto);
            }
        }
        
        return resultado;
    }

}
