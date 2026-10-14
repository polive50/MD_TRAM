package org.madrid.saii.lib.dao;

import java.util.Date;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteJusTecHist;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusTec
 *
 * @author nttdata
 */
@Repository("expedienteJusTecHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteJusTecHistDAOImp extends BaseDAOImpl<ExpedienteJusTecHist, Integer>
        implements ExpedienteJusTecHistDAO {

    @Override
    @Transactional(readOnly = false)
    public void crearHistorico(ExpedienteJusTec expedienteJusTec) {

        ExpedienteJusTecHist expedienteJusTecHist = new ExpedienteJusTecHist();
        expedienteJusTecHist.setIdExpedienteJusTec(expedienteJusTec.getIdExpedienteJusTec());
        expedienteJusTecHist.setIdExpediente(expedienteJusTec.getIdExpediente());
        expedienteJusTecHist.setTipoExpedienteJusTec(expedienteJusTec.getTipoExpedienteJusTec());
        expedienteJusTecHist.setNmPeriodo(expedienteJusTec.getNmPeriodo());
        expedienteJusTecHist.setFcIniJustiCienTec(expedienteJusTec.getFcIniJustiCienTec());
        expedienteJusTecHist.setFcFinJustiCienTec(expedienteJusTec.getFcFinJustiCienTec());
        expedienteJusTecHist.setTlObservacion(expedienteJusTec.getTlObservacion());
        //VMM MD-913 28/10/24: aniadimos campos de EREG a nivel Justec
        expedienteJusTecHist.setIdEstado(expedienteJusTec.getIdEstado());
        expedienteJusTecHist.setIdEstadoRevAdmin(expedienteJusTec.getIdEstadoRevAdmin());
        expedienteJusTecHist.setCdEregPresent(expedienteJusTec.getCdEregPresent());
        expedienteJusTecHist.setCdEregSubsana(expedienteJusTec.getCdEregSubsana());
        expedienteJusTecHist.setDsDestinoEreg(expedienteJusTec.getDsDestinoEreg());
        expedienteJusTecHist.setFcFirmaPresent(expedienteJusTec.getFcFirmaPresent());
        expedienteJusTecHist.setFcFirmaSubsana(expedienteJusTec.getFcFirmaSubsana());
        expedienteJusTecHist.setTlObservacionParaCiudadano(expedienteJusTec.getTlObservacionParaCiudadano());
        expedienteJusTecHist.setTlObservacionDelCiudadano(expedienteJusTec.getTlObservacionDelCiudadano());
        
        // Auditoria
        expedienteJusTecHist.setFcAltaFila(new Date());
        if (Objects.nonNull(expedienteJusTec.getCdUsuModif())) {
            expedienteJusTecHist.setCdUsuAlta(expedienteJusTec.getCdUsuModif());
        } else {
            if (Objects.nonNull(expedienteJusTec.getCdUsuAlta())) {
                expedienteJusTecHist.setCdUsuAlta(expedienteJusTec.getCdUsuAlta());
            }else {
                expedienteJusTecHist.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);    
            }
        }
        

        this.insert(expedienteJusTecHist);
    }

}
