
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
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
@Repository("expedienteDocEvalDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteDocEvalDAOImpl extends BaseDAOImpl<ExpedienteDocEval, Integer>
        implements ExpedienteDocEvalDAO {
    
    
    /**
    *
    */
   @Override
   public List<ExpedienteDocEvalDTO> obtenerDocumentosExpedienteDocEval(Integer idExpedienteEval){
       List<ExpedienteDocEvalDTO> lstExpedienteDocEvalDTO = null;


       List<AtlasFilter> filtros = new ArrayList<>();
       if (idExpedienteEval != null) {
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_OBJETO, idExpedienteEval,
                   Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       }


       lstExpedienteDocEvalDTO = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ExpedienteDocEvalToDTO.INSTANCE));

       return lstExpedienteDocEvalDTO;
   }

    @Override
    public ExpedienteDocEvalDTO obtenerPorTipoDocPorExpedienteEval(Integer idValorTipoDoc,
            Integer idExpedienteEval) {
        List<ExpedienteDocEvalDTO> lstExpedienteDocEvalDTO = null;


        List<AtlasFilter> filtros = new ArrayList<>();
        if (idExpedienteEval != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_OBJETO, idExpedienteEval,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO, idValorTipoDoc,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }


        lstExpedienteDocEvalDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocEvalToDTO.INSTANCE));
        
        if(!lstExpedienteDocEvalDTO.isEmpty()) {
            return lstExpedienteDocEvalDTO.get(0);
        }

        return null;
    }

    @Override
    public List<ExpedienteDocEvalDTO> buscarDocumentosInformePorFica(Integer cdFica) {
        List<ExpedienteDocEvalDTO> lstExpedienteDocEvalDTO = null;


        List<AtlasFilter> filtros = new ArrayList<>();
        if (cdFica != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, cdFica,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }


        lstExpedienteDocEvalDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocEvalToDTO.INSTANCE));

        return lstExpedienteDocEvalDTO;
    }

    @Override
    public List<ExpedienteDocEvalDTO> buscarDocumentosPortipoDocumento(Integer idExpedienteEval,
            String dsNombreFichero) {
        List<ExpedienteDocEvalDTO> lstExpedienteDocEvalDTO = null;


        List<AtlasFilter> filtros = new ArrayList<>();
        if (idExpedienteEval != null && dsNombreFichero != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEEVAL_OBJETO, idExpedienteEval,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_DS_TIPO_DOCUMENTO, dsNombreFichero,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }


        lstExpedienteDocEvalDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocEvalToDTO.INSTANCE));

        return lstExpedienteDocEvalDTO;
    }

}
