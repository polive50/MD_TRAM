package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.domain.ConvocatoriaJusEcoHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusEcoHist
 *
 * @author quental
 */
@Repository("convocatoriaJusEcoHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaJusEcoHistDAOImpl extends BaseDAOImpl<ConvocatoriaJusEcoHist, Integer>
        implements ConvocatoriaJusEcoHistDAO {

    /**
     *
     */
    @Transactional(readOnly = false)
    @Override
    public void crearHistorico(ConvocatoriaJusEco convocatoriaJusEco) {

        ConvocatoriaJusEcoHist convocatoriaJusEcoHist = new ConvocatoriaJusEcoHist();
        convocatoriaJusEcoHist
                .setIdConvocatoriaJusEco(convocatoriaJusEco.getIdConvocatoriaJusEco());
        convocatoriaJusEcoHist.setIdConvocatoria(convocatoriaJusEco.getIdConvocatoria());
        convocatoriaJusEcoHist
                .setTipoConvocatoriaJusEco(convocatoriaJusEco.getTipoConvocatoriaJusEco());
        convocatoriaJusEcoHist.setNmPeriodo(convocatoriaJusEco.getNmPeriodo());
        convocatoriaJusEcoHist.setFcIniJustiEconomica(convocatoriaJusEco.getFcIniJustiEconomica());
        convocatoriaJusEcoHist.setFcFinJustiEconomica(convocatoriaJusEco.getFcFinJustiEconomica());
        convocatoriaJusEcoHist.setTlObservacion(convocatoriaJusEco.getTlObservacion());

        this.insert(convocatoriaJusEcoHist);

    }

}
