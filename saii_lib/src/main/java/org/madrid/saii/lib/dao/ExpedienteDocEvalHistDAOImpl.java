
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaDoc
 *
 * @author everis
 */
@Repository("ExpedienteDocEvalHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteDocEvalHistDAOImpl extends BaseDAOImpl<ExpedienteDocEvalHist, Integer>
        implements ExpedienteDocEvalHistDAO {
    
    
    /**
    *
    */
   @Override
   public List<ExpedienteDocEvalHistDTO> obtenerDocumentosExpedienteDocEvalHist(Integer idExpedienteEval){
       List<ExpedienteDocEvalHistDTO> lstExpedienteDocEvalHistDTO = null;

       List<AtlasFilter> filtros = new ArrayList<>();
       if (idExpedienteEval != null) {
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL, idExpedienteEval,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }

       lstExpedienteDocEvalHistDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ExpedienteDocEvalHistToDTO.INSTANCE));

       return lstExpedienteDocEvalHistDTO;
   }

    

}
