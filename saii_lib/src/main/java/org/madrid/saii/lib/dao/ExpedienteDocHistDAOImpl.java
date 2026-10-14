
package org.madrid.saii.lib.dao;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteDocHist
 *
 * @author everis
 */
@Repository("expedienteDocHistDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteDocHistDAOImpl extends BaseDAOImpl<ExpedienteDocHist, Integer>
        implements ExpedienteDocHistDAO {

    /**
     *
     * @param expedienteDoc
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDocHist crearHistorico(ExpedienteDoc expedienteDoc, String cdUsuAlta) {

        ExpedienteDocHist expedienteDocHist = new ExpedienteDocHist();
        expedienteDocHist.setExpedienteDoc(expedienteDoc);
        expedienteDocHist.setIdExpediente(expedienteDoc.getExpediente().getIdExpediente());
        expedienteDocHist.setCdFica(expedienteDoc.getCdFica());
        expedienteDocHist.setCdAnexoEreg(expedienteDoc.getCdAnexoEreg());
        expedienteDocHist.setCdAnexoEregPadre(expedienteDoc.getCdAnexoEregPadre());
        expedienteDocHist.setEstado(expedienteDoc.getEstado());
        expedienteDocHist.setTipoDocumento(expedienteDoc.getTipoDocumento());
        expedienteDocHist.setTlCausaDescripcion(expedienteDoc.getTlCausaDescripcion());
        expedienteDocHist.setBloqueComponente(expedienteDoc.getBloqueComponente());
        expedienteDocHist.setIdExpediente(expedienteDoc.getExpediente().getIdExpediente());
        expedienteDocHist.setIdDocumentoPtfr(expedienteDoc.getIdDocumentoPtfr());
        expedienteDocHist.setConvocatoriaCau(expedienteDoc.getConvocatoriaCau());
        expedienteDocHist.setConvocatoriaCauCont(expedienteDoc.getConvocatoriaCauCont());
        expedienteDocHist.setIdCausa(expedienteDoc.getIdCausa());
        expedienteDocHist.setTlObservaciones(expedienteDoc.getTlObservaciones());
        expedienteDocHist.setTramite(expedienteDoc.getTramite());
        expedienteDocHist.setItAdicional(expedienteDoc.getItAdicional());
        expedienteDocHist.setItAutoriza(expedienteDoc.getItAutoriza());
        expedienteDocHist.setTlObservacionesGestor(expedienteDoc.getTlObservacionesGestor());
        expedienteDocHist.setDato(expedienteDoc.getDato());
        expedienteDocHist.setCodigoEreg(expedienteDoc.getCodigoEreg());
        expedienteDocHist.setFcRegistro(expedienteDoc.getFcRegistro());

        // Auditoria
        expedienteDocHist.setFcAltaFila(new Date());
        if (StringUtils.isNotBlank(cdUsuAlta)) {
            expedienteDocHist.setCdUsuAlta(cdUsuAlta);
        }

        expedienteDocHist = this.insert(expedienteDocHist);

        return expedienteDocHist;
    }
    
    
    /**
    *
    * @param expedienteDoc
    * @return
    */
   @Transactional(readOnly = false)
   @Override
   public ExpedienteDocHist crearHistoricoBaja(ExpedienteDoc expedienteDoc, String cdUsuBaja) {

       ExpedienteDocHist expedienteDocHist = new ExpedienteDocHist();
       expedienteDocHist.setExpedienteDoc(expedienteDoc);
       expedienteDocHist.setIdExpediente(expedienteDoc.getExpediente().getIdExpediente());
       expedienteDocHist.setCdFica(expedienteDoc.getCdFica());
       expedienteDocHist.setCdAnexoEreg(expedienteDoc.getCdAnexoEreg());
       expedienteDocHist.setCdAnexoEregPadre(expedienteDoc.getCdAnexoEregPadre());
       expedienteDocHist.setEstado(expedienteDoc.getEstado());
       expedienteDocHist.setTipoDocumento(expedienteDoc.getTipoDocumento());
       expedienteDocHist.setTlCausaDescripcion(expedienteDoc.getTlCausaDescripcion());
       expedienteDocHist.setBloqueComponente(expedienteDoc.getBloqueComponente());
       expedienteDocHist.setIdExpediente(expedienteDoc.getExpediente().getIdExpediente());
       expedienteDocHist.setIdDocumentoPtfr(expedienteDoc.getIdDocumentoPtfr());
       expedienteDocHist.setConvocatoriaCau(expedienteDoc.getConvocatoriaCau());
       expedienteDocHist.setConvocatoriaCauCont(expedienteDoc.getConvocatoriaCauCont());
       expedienteDocHist.setIdCausa(expedienteDoc.getIdCausa());
       expedienteDocHist.setTlObservaciones(expedienteDoc.getTlObservaciones());
       expedienteDocHist.setTramite(expedienteDoc.getTramite());
       expedienteDocHist.setItAdicional(expedienteDoc.getItAdicional());
       expedienteDocHist.setItAutoriza(expedienteDoc.getItAutoriza());
       expedienteDocHist.setTlObservacionesGestor(expedienteDoc.getTlObservacionesGestor());
       expedienteDocHist.setDato(expedienteDoc.getDato());

       // Auditoria
       expedienteDocHist.setFcAltaFila(new Date());
       expedienteDocHist.setFcBajaFila(new Date());
       if (StringUtils.isNotBlank(cdUsuBaja)) {
           expedienteDocHist.setCdUsuAlta(cdUsuBaja);
           expedienteDocHist.setCdUsuBaja(cdUsuBaja);
       }

       expedienteDocHist = this.insert(expedienteDocHist);

       return expedienteDocHist;
   }
}
