package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidadHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaAnualidadHist
 *
 * @author Quental
 */
@Repository("convocatoriaAnualidadHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaAnualidadHistDAOImpl extends BaseDAOImpl<ConvocatoriaAnualidadHist, Integer>
implements ConvocatoriaAnualidadHistDAO {
    
    /**
    *
    */
   @Transactional(readOnly = false)
   @Override
   public void crearHistorico(ConvocatoriaAnualidad convocatoriaAnualidad) {

       ConvocatoriaAnualidadHist convocatoriaAnualidadHist = new ConvocatoriaAnualidadHist();
       convocatoriaAnualidadHist
               .setIdConvocatoriaAnualidad(convocatoriaAnualidad.getIdConvocatoriaAnualidad());
       convocatoriaAnualidadHist.setIdConvocatoria(convocatoriaAnualidad.getIdConvocatoria());
       convocatoriaAnualidadHist
               .setTipoConvocatoriaAnualidad(convocatoriaAnualidad.getTipoConvocatoriaAnualidad());
       convocatoriaAnualidadHist.setNmAnualidad(convocatoriaAnualidad.getNmAnualidad());
       convocatoriaAnualidadHist.setFcIniEjecucion(convocatoriaAnualidad.getFcIniEjecucion());
       convocatoriaAnualidadHist.setFcFinEjecucion(convocatoriaAnualidad.getFcFinEjecucion());
       convocatoriaAnualidadHist.setTlObservacion(convocatoriaAnualidad.getTlObservacion());

       this.insert(convocatoriaAnualidadHist);

   }

}
