
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToDTO;
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
@Repository("convocatoriaDocEvalDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaDocEvalDAOImpl extends BaseDAOImpl<ConvocatoriaDocEval, Integer>
        implements ConvocatoriaDocEvalDAO {
    
    
    /**
    *
    */
   @Override
   public List<ConvocatoriaDocEvalDTO> obtenerDocumentosConvocatoriaEval(Integer idConvocatoria,Integer interesado, String tipo){
       List<ConvocatoriaDocEvalDTO> lstConvocatoriaDocDTO = null;

       List<AtlasFilter> filtros = new ArrayList<>();
       if (idConvocatoria != null) {
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, interesado,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, tipo,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }

       lstConvocatoriaDocDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalToDTO.INSTANCE));

       return lstConvocatoriaDocDTO;
   }

}
