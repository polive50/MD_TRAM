package org.madrid.saii.lib.services;

import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.tramite.TramiteConsecion;
import org.madrid.saii.lib.tramite.TramiteDesistimiento;
import org.madrid.saii.lib.tramite.TramiteExclusion;
import org.madrid.saii.lib.tramite.TramiteInformeAyudaRevisadaJustificacionEconomica;
import org.madrid.saii.lib.tramite.TramiteInformeFinalJustificacionEconomica;
import org.madrid.saii.lib.tramite.TramiteInicioPlazoJustificacionCientificoTecnica;
import org.madrid.saii.lib.tramite.TramiteInicioPlazoJustificacionEconomica;
import org.madrid.saii.lib.tramite.TramitePresentaSolicitud;
import org.madrid.saii.lib.tramite.TramiteRequerimientoPresentacionActaFinalComiteGestion;
import org.madrid.saii.lib.tramite.TramiteRequerimientoPresentacionJustificacionCientificoTecnico;
import org.madrid.saii.lib.tramite.TramiteRequerimientoPresentacionJustificacionEconomica;
import org.madrid.saii.lib.tramite.TramiteRequerimientoSubsanacionBorradorActaComiteGestion;
import org.madrid.saii.lib.tramite.TramiteRequerimientoSubsanacionContratos;
import org.madrid.saii.lib.tramite.TramiteRequerimientoSubsanacionJustificacionCientificoTecnica;
import org.madrid.saii.lib.tramite.TramiteRequerimientoSubsanacionJustificacionEconomica;
import org.madrid.saii.lib.tramite.TramiteSubsanacionAdministrativa;
import org.madrid.saii.lib.tramite.TramiteSubsanacionRequisito;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

@Service("tramiteService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class TramiteServiceImpl implements TramiteService{
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Override
    public Tramite getTramite(Integer idTipoTramite) throws ServiceException {

        //VMM 26/09/24 MD-593: metemos las nuevas clases de los nuevos tramites
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(idTipoTramite)) {
            return new TramitePresentaSolicitud();      
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(idTipoTramite)) {
            return new TramiteSubsanacionAdministrativa();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(idTipoTramite)) {
            return new TramiteSubsanacionRequisito();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL).equals(idTipoTramite)) {
            return new TramiteRequerimientoSubsanacionContratos();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA).equals(idTipoTramite)) {
            return new TramiteRequerimientoSubsanacionBorradorActaComiteGestion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_ACTA_FINAL).equals(idTipoTramite)) {
            return new TramiteRequerimientoPresentacionActaFinalComiteGestion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA).equals(idTipoTramite)) {
            return new TramiteRequerimientoSubsanacionBorradorActaComiteGestion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_JUSTIFICACION_ECONOMICA).equals(idTipoTramite)) {
            return new TramiteRequerimientoPresentacionJustificacionEconomica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA).equals(idTipoTramite)) {
            return new TramiteRequerimientoSubsanacionJustificacionEconomica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_JUSTIFICACION_ECONOMICA).equals(idTipoTramite)) {
            return new TramiteInformeAyudaRevisadaJustificacionEconomica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_FINAL_JUSTIFICACION_ECONOMICA).equals(idTipoTramite)) {
            return new TramiteInformeFinalJustificacionEconomica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(idTipoTramite)) {
            return new TramiteDesistimiento();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(idTipoTramite)) {
            return new TramiteDesistimiento();    
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(idTipoTramite)) {
            return new TramiteExclusion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(idTipoTramite)) {
            return new TramiteExclusion(); 
            // MD-2190 Quitamos cuando es un trámite de alegación exclusión provisional
//        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL).equals(idTipoTramite)) {
//            return new TramiteExclusion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION).equals(idTipoTramite)) {
            return new TramiteConsecion();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA).equals(idTipoTramite)) {
            return new TramiteRequerimientoPresentacionJustificacionCientificoTecnico();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUST_ECONOMICA).equals(idTipoTramite)) {
            return new TramiteInicioPlazoJustificacionEconomica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUSTIFICACION_CIENTIFICO_TECNICA).equals(idTipoTramite)) {
            return new TramiteInicioPlazoJustificacionCientificoTecnica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA).equals(idTipoTramite)) {
            return new TramiteRequerimientoSubsanacionJustificacionCientificoTecnica();
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(idTipoTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(idTipoTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(idTipoTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(idTipoTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO).equals(idTipoTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL).equals(idTipoTramite)) {
            return new TramiteConsecion();
        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA).equals(idTipoTramite)) {
            return new TramiteConsecion();   
        } else {
            return null;

        }

    }

}
