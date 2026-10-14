
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalTipoDocToDTO;
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
@Repository("convocatoriaEvalTipoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaEvalTipoDocDAOImpl extends BaseDAOImpl<ConvocatoriaEvalTipoDoc, Integer>
        implements ConvocatoriaEvalTipoDocDAO {
    
    
    /**
    *
    */
   @Override
   public List<ConvocatoriaEvalTipoDocDTO> obtenerTipoDocumentosConvocatoriaEval(Integer idConvocatoria, Integer nivel){
       List<ConvocatoriaEvalTipoDocDTO> lstConvocatoriaEvalTipoDocDTO = null;

       List<AtlasFilter> filtros = new ArrayList<>();
       if (idConvocatoria != null) {
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NM_CONSTANTE_NIVEL, nivel,
                   Operador.EQUALS));
           
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }

       lstConvocatoriaEvalTipoDocDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaEvalTipoDocToDTO.INSTANCE));

       return lstConvocatoriaEvalTipoDocDTO;
   }

}
