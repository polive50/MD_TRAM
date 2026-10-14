
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocToDTO;
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
@Repository("convocatoriaDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaDocDAOImpl extends BaseDAOImpl<ConvocatoriaDoc, Integer>
        implements ConvocatoriaDocDAO {
    
    
    /**
    *
    */
   @Override
   public List<ConvocatoriaDocDTO> obtenerDocumentosConvocatoria(Integer idConvocatoria){
       List<ConvocatoriaDocDTO> lstConvocatoriaDocDTO = null;

       List<AtlasFilter> filtros = new ArrayList<>();
       if (idConvocatoria != null) {
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }

       lstConvocatoriaDocDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaDocToDTO.INSTANCE));

       return lstConvocatoriaDocDTO;
   }

}
