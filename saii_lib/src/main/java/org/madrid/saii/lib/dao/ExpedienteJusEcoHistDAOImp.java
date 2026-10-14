package org.madrid.saii.lib.dao;

import java.util.Date;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusEcoHist;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusEco
 *
 * @author quental
 */
@Repository("expedienteJusEcoHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteJusEcoHistDAOImp extends BaseDAOImpl<ExpedienteJusEcoHist, Integer>
        implements ExpedienteJusEcoHistDAO {

    @Override
    @Transactional(readOnly = false)
    public void crearHistorico(ExpedienteJusEco expedienteJusEco) {

        ExpedienteJusEcoHist expedienteJusEcoHist = new ExpedienteJusEcoHist();
        expedienteJusEcoHist.setIdExpedienteJusEco(expedienteJusEco.getIdExpedienteJusEco());
        expedienteJusEcoHist.setIdExpediente(expedienteJusEco.getIdExpediente());
        expedienteJusEcoHist.setTipoExpedienteJusEco(expedienteJusEco.getTipoExpedienteJusEco());
        expedienteJusEcoHist.setNmPeriodo(expedienteJusEco.getNmPeriodo());
        expedienteJusEcoHist.setFcIniJustiEconomica(expedienteJusEco.getFcIniJustiEconomica());
        expedienteJusEcoHist.setFcFinJustiEconomica(expedienteJusEco.getFcFinJustiEconomica());
        expedienteJusEcoHist.setTlObservacion(expedienteJusEco.getTlObservacion());    
        
        //GIM 04/02/2026 MD-2172 añadimos al historico los nuevos campos que se han ido creando
        expedienteJusEcoHist.setIdEstado(expedienteJusEco.getIdEstado());
        expedienteJusEcoHist.setIdEstadoRevAdmin(expedienteJusEco.getIdEstadoRevAdmin());
        expedienteJusEcoHist.setFcFirmaSubsana(expedienteJusEco.getFcFirmaSubsana());
        expedienteJusEcoHist.setFcFirmaPresent(expedienteJusEco.getFcFirmaPresent());
        expedienteJusEcoHist.setDsDestinoEreg(expedienteJusEco.getDsDestinoEreg());
        expedienteJusEcoHist.setCdEregSubsana(expedienteJusEco.getCdEregSubsana());
        expedienteJusEcoHist.setCdEregPresent(expedienteJusEco.getCdEregPresent());
        expedienteJusEcoHist.setTlObservacionParaCiudadano(expedienteJusEco.getTlObservacionParaCiudadano());
        expedienteJusEcoHist.setTlObservacionDelCiudadano(expedienteJusEco.getTlObservacionDelCiudadano());
        
        // Auditoria
        expedienteJusEcoHist.setFcAltaFila(new Date());
        if (Objects.nonNull(expedienteJusEco.getCdUsuModif())) {
            expedienteJusEcoHist.setCdUsuAlta(expedienteJusEco.getCdUsuModif());
        } else {
            if (Objects.nonNull(expedienteJusEco.getCdUsuAlta())) {
                expedienteJusEcoHist.setCdUsuAlta(expedienteJusEco.getCdUsuAlta());
            }else {
                expedienteJusEcoHist.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);    
            }
        }
        
        this.insert(expedienteJusEcoHist);
    }

}
