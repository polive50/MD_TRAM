
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalTipoDocToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionTipoDocToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaEvalTipoDoc
 *
 * @author everis
 */
@Repository("evaluacionTipoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionTipoDocDAOImpl extends BaseDAOImpl<EvaluacionTipoDoc, Integer>
        implements EvaluacionTipoDocDAO {
    
    
    /**
    *
    */
   @Override
   public List<EvaluacionTipoDocDTO> obtenerTipoDocumentosEvaluacion(Integer idInforme){
       List<EvaluacionTipoDocDTO> lstEvalTipoDocDTO = null;

       List<AtlasFilter> filtros = new ArrayList<>();
       if (idInforme != null) {
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_INFORME, idInforme,
                   Operador.EQUALS));
           
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }

       lstEvalTipoDocDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), EvaluacionTipoDocToDTO.INSTANCE));

       return lstEvalTipoDocDTO;
   }

}
