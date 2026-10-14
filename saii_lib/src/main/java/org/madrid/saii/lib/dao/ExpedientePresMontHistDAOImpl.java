package org.madrid.saii.lib.dao;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedientePremontoHist
 *
 * @author nttdata
 */
@Repository("expedientePresMontHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedientePresMontHistDAOImpl extends BaseDAOImpl<ExpedientePresMontHist, Integer>
        implements ExpedientePresMontHistDAO {

    @Override
    public void crearHistorico(ExpedientePresupMonto expedientePresupMonto) {
        ExpedientePresMontHist expedientePremontoHist = new ExpedientePresMontHist();
        expedientePremontoHist
                .setIdExpedientePresupMonto(expedientePresupMonto.getIdExpedientePresupMonto());
        expedientePremontoHist.setIdExpedientePresup(expedientePresupMonto.getIdExpedientePresup());
        expedientePremontoHist.setNmAnualidad(expedientePresupMonto.getNmAnualidad());
        expedientePremontoHist.setNmImporte(expedientePresupMonto.getNmImporte());
        expedientePremontoHist.setNmImporteAdmitido(expedientePresupMonto.getNmImporteAdmitido());
        expedientePremontoHist.setNmImporteJustificado(expedientePresupMonto.getNmImporteJustificado());
        expedientePremontoHist.setEstado(expedientePresupMonto.getEstado());

        if (Objects.nonNull(expedientePresupMonto.getFcModifFila())) {
            expedientePremontoHist.setFcAltaFila(expedientePresupMonto.getFcModifFila());
        } else {
            expedientePremontoHist.setFcAltaFila(expedientePresupMonto.getFcAltaFila());
        }

        // VMM 24/09/26 MD-2725: CD_USU_ALTA es NOT NULL en BD y aqui no se estaba informando nunca
        // (a diferencia de otros *HistDAOImpl como ExpedienteJusEcoHistDAOImp, que siguen este mismo
        // patron). En el flujo online probablemente no se notaba porque el propio origen ya traia el
        // dato de otro punto, pero en el flujo batch (ResolucionModificacionServiceImpl) el origen no
        // tiene usuario de sesion y la insercion fallaba con ORA-01400.
        if (Objects.nonNull(expedientePresupMonto.getCdUsuModif())) {
            expedientePremontoHist.setCdUsuAlta(expedientePresupMonto.getCdUsuModif());
        } else if (Objects.nonNull(expedientePresupMonto.getCdUsuAlta())) {
            expedientePremontoHist.setCdUsuAlta(expedientePresupMonto.getCdUsuAlta());
        } else {
            expedientePremontoHist.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        }

        this.insert(expedientePremontoHist);
    }

}
