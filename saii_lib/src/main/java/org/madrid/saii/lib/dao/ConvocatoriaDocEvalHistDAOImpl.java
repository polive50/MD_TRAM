
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaDocEvalHist;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalHistToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaDocHist
 *
 * @author everis
 */
@Repository("convocatoriaDocEvalHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaDocEvalHistDAOImpl extends BaseDAOImpl<ConvocatoriaDocEvalHist, Integer>
        implements ConvocatoriaDocEvalHistDAO {
    
    
    /**
    *
    */
   @Override
   public List<ConvocatoriaDocEvalHistDTO> obtenerDocumentosConvocatoriaEval(Integer idConvocatoria,Integer interesado, String tipo){
       List<ConvocatoriaDocEvalHistDTO> lstConvocatoriaDocDTO = null;

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
               Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalHistToDTO.INSTANCE));

       return lstConvocatoriaDocDTO;
   }

}
