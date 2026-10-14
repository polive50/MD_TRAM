package org.madrid.saii.lib.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("expedienteContratoHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteContratoHistDAOImpl extends BaseDAOImpl<ExpedienteContratoHist, Integer> implements ExpedienteContratoHistDAO {

    
    @Transactional(readOnly = false)
    @Override
    public ExpedienteContratoHist crearHistorico(ExpedienteContrato expedienteContrato, String cdUsuAlta) {

        ExpedienteContratoHist expedienteContratoHist = new ExpedienteContratoHist();

        expedienteContratoHist.setIdExpedienteContrato(expedienteContrato.getIdExpedienteContrato());
        expedienteContratoHist.setIdExpediente(expedienteContrato.getIdExpediente());
        expedienteContratoHist.setInteresado(expedienteContrato.getInteresado());
        expedienteContratoHist.setIdEstado(expedienteContrato.getIdEstado());
        expedienteContratoHist.setIdEstadoRevAdmin(expedienteContrato.getIdEstadoRevAdmin());
       
        expedienteContratoHist.setIdTipoContrato(expedienteContrato.getIdTipoContrato());
        expedienteContratoHist.setFcIniContrato(expedienteContrato.getFcIniContrato());
        expedienteContratoHist.setFcFinContrato(expedienteContrato.getFcFinContrato());
        expedienteContratoHist.setItIndefinido(expedienteContrato.getItIndefinido());
        expedienteContratoHist.setInteresado(expedienteContrato.getInteresado());
        expedienteContratoHist.setFcRenuncia(expedienteContrato.getFcRenuncia());
        expedienteContratoHist.setCdEregPresent(expedienteContrato.getCdEregPresent());
        expedienteContratoHist.setCdEregSubsana(expedienteContrato.getCdEregSubsana());
        expedienteContratoHist.setDsDestinoEreg(expedienteContrato.getDsDestinoEreg());
        expedienteContratoHist.setFcFirmaPresent(expedienteContrato.getFcFirmaPresent());
        expedienteContratoHist.setFcFirmaSubsana(expedienteContrato.getFcFirmaSubsana());
        expedienteContratoHist.setCdEregRenuncia(expedienteContrato.getCdEregRenuncia());
        expedienteContratoHist.setFcFirmaRenuncia(expedienteContrato.getFcFirmaRenuncia());
        
        // Auditoria
        expedienteContratoHist.setFcAltaFila(new Date());
        if (StringUtils.isNotBlank(cdUsuAlta)) {
            expedienteContratoHist.setCdUsuAlta(cdUsuAlta);
        }
        expedienteContratoHist = this.insert(expedienteContratoHist);

        return expedienteContratoHist;
    }
    
}
