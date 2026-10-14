package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.domain.ConvocatoriaJusTecHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusTecHist
 *
 * @author nttdata
 */
@Repository("convocatoriaJusTecHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaJusTecHistDAOImpl extends BaseDAOImpl<ConvocatoriaJusTecHist, Integer>
        implements ConvocatoriaJusTecHistDAO {

    /**
     *
     */
    @Transactional(readOnly = false)
    @Override
    public void crearHistorico(ConvocatoriaJusTec convocatoriaJusTec) {

        ConvocatoriaJusTecHist convocatoriaJusTecHist = new ConvocatoriaJusTecHist();
        convocatoriaJusTecHist
                .setIdConvocatoriaJusTec(convocatoriaJusTec.getIdConvocatoriaJusTec());
        convocatoriaJusTecHist.setIdConvocatoria(convocatoriaJusTec.getIdConvocatoria());
        convocatoriaJusTecHist
                .setTipoConvocatoriaJusTec(convocatoriaJusTec.getTipoConvocatoriaJusTec());
        convocatoriaJusTecHist.setNmPeriodo(convocatoriaJusTec.getNmPeriodo());
        convocatoriaJusTecHist.setFcIniJustiCienTec(convocatoriaJusTec.getFcIniJustiCienTec());
        convocatoriaJusTecHist.setFcFinJustiCienTec(convocatoriaJusTec.getFcFinJustiCienTec());
        convocatoriaJusTecHist.setTlObservacion(convocatoriaJusTec.getTlObservacion());

        this.insert(convocatoriaJusTecHist);

    }

}
