package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * VMM 24/09/26 MD-2725: ver comentario en ResolucionModificacionService. Version simplificada de la
 * antigua logica en DocumentoPtfrServiceImpl (que tenia 3 fases para poder "retomar donde se quedo"
 * un intento previo fallido). Con REQUIRES_NEW un fallo aqui deshace TODO lo de esta modificacion, asi
 * que ya no hace falta distinguir "padre creado pero hijo pendiente" ni saltar filas ya asociadas a la
 * sesion - un reintento simplemente vuelve a ejecutar este metodo entero desde cero.
 */
@Slf4j
@Service("resolucionModificacionService")
public class ResolucionModificacionServiceImpl implements ResolucionModificacionService {

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;

    @Autowired
    @Qualifier("expedienteModifPresupMontoService")
    private ExpedienteModifPresupMontoService expedienteModifPresupMontoService;

    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteModifPlazoService")
    private ExpedienteModifPlazoService expedienteModifPlazoService;

    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;

    /**
     * VMM 24/09/26 MD-2725: REQUIRES_NEW - transaccion propia e independiente de la del batch que
     * llama a este metodo, para que un fallo a mitad haga rollback limpio de solo esta modificacion.
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
            ServiceException.class, RuntimeException.class })
    public void aplicarResolucionModificacion(Integer idExpedienteModifPresPlazoOtra)
            throws ServiceException {

        if (idExpedienteModifPresPlazoOtra == null) {
            return;
        }

        ExpedienteModifPresPlazoOtra expdienteModifPresupuesto = this.expedienteModifPresPlazoOtraService
                .find(idExpedienteModifPresPlazoOtra);
        if (expdienteModifPresupuesto == null) {
            return;
        }

        Integer idEstadoResuelta = utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpedienteResolucion.RESUELTA);
        if (expdienteModifPresupuesto.getEstado() != null
                && idEstadoResuelta.equals(expdienteModifPresupuesto.getEstado().getIdValor())) {
            log.info("La modificacion idExpedienteModifPresPlazoOtra={} ya esta en estado Resuelta, "
                    + "se omite (probablemente hay mas de un envio pendiente en SAII_DOCUMENTO_PTFR "
                    + "para la misma modificacion).",
                    idExpedienteModifPresPlazoOtra);
            return;
        }

        try {
            expdienteModifPresupuesto.setEstado(this.catalogosValoresService.find(idEstadoResuelta));

            if (Constantes.AfirmacionNegacion.S.equals(expdienteModifPresupuesto.getItModPres())) {
                this.aplicarResolucionPresupuesto(expdienteModifPresupuesto);
            }

            if (Constantes.AfirmacionNegacion.S.equals(expdienteModifPresupuesto.getItModPlazo())) {
                this.aplicarResolucionPlazos(expdienteModifPresupuesto);
            }

            // VMM 20/10/26 MD-3156: la modificacion pasa a Resuelta por el batch: campos de auditoria del batch
            expdienteModifPresupuesto.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            expdienteModifPresupuesto.setFcModifFila(new Date());

            this.expedienteModifPresPlazoOtraService.insertOrUpdate(expdienteModifPresupuesto);

        } catch (Exception e) {
            log.error("No se pudo resolver la modificacion idExpedienteModifPresPlazoOtra={} "
                    + "(itModPres={}, itModPlazo={}): {}. Transaccion aislada (REQUIRES_NEW) - se ha "
                    + "hecho rollback completo de esta modificacion, no ha quedado nada aplicado a "
                    + "medias. Un reintento repetira todo el trabajo desde cero.",
                    idExpedienteModifPresPlazoOtra,
                    expdienteModifPresupuesto.getItModPres(),
                    expdienteModifPresupuesto.getItModPlazo(),
                    e.getMessage(), e);
            throw (e instanceof ServiceException) ? (ServiceException) e : new ServiceException(e);
        }
    }

    /**
     * VMM 24/09/26 MD-2725: version simplificada, sin fases - ver comentario de clase.
     */
    private void aplicarResolucionPresupuesto(ExpedienteModifPresPlazoOtra expdienteModifPresupuesto)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEMODIFPRESPLAZOOTRA,
                expdienteModifPresupuesto.getIdExpedienteModifPresPlazoOtra(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NM_IMPORTE, Operador.NOT_NULL));

        // Orden explicito por PK de insercion, necesario para poder agrupar mas abajo las filas de
        // conceptos nuevos por concepto (ver comentario en el bucle).
        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_MODIF_PRESUP_MONTO, true));

        List<ExpedienteModifPresupMontoDTO> presup = this.expedienteModifPresupMontoService.buscar(0, 0,
                filtros, orden);

        Integer idExpediente = expdienteModifPresupuesto.getExpediente() != null
                ? expdienteModifPresupuesto.getExpediente().getIdExpediente()
                : null;

        // Conceptos ya existentes (ExpedientePresupMonto ya creado): solo actualizar su importe.
        for (ExpedienteModifPresupMontoDTO presupMonto : presup) {
            if (presupMonto.getIdExpedientePresupMonto() == null) {
                continue;
            }
            ExpedientePresupMontoDTO presupuesto = this.expedientePresupMontoService
                    .buscarId(presupMonto.getIdExpedientePresupMonto());
            if (presupuesto != null) {
                presupuesto.setNmImporte(presupMonto.getNmImporte());
                this.expedientePresupMontoService.guardarConHistorico(presupuesto);
            }
        }

        // Conceptos completamente nuevos (dados de alta directamente en saii_solicitud, sin
        // ExpedientePresup todavia): se agrupan por concepto para crear un unico ExpedientePresup
        // padre por concepto, no uno por cada anualidad. Nos apoyamos en las mismas dos garantias que
        // ya usa copiarListaPresupuestosMod en pantalla: guardarModifPresupConcepto inserta siempre en
        // orden (todas las filas de un concepto seguidas, antes de pasar al siguiente) y tlConcepto
        // solo se rellena en la fila cabecera (nmAnualidad=0).
        List<ExpedienteModifPresupMontoDTO> conceptosNuevosOrdenados = presup.stream()
                .filter(m -> m.getIdExpedientePresupMonto() == null)
                .collect(Collectors.toList());

        List<List<ExpedienteModifPresupMontoDTO>> gruposConceptosNuevos = new ArrayList<>();
        List<ExpedienteModifPresupMontoDTO> grupoActual = null;
        for (ExpedienteModifPresupMontoDTO monto : conceptosNuevosOrdenados) {
            if (StringUtils.isNotBlank(monto.getTlConcepto()) || grupoActual == null) {
                grupoActual = new ArrayList<>();
                gruposConceptosNuevos.add(grupoActual);
            }
            grupoActual.add(monto);
        }

        for (List<ExpedienteModifPresupMontoDTO> grupoConcepto : gruposConceptosNuevos) {
            this.crearConceptoNuevoDesdeModificacion(idExpediente, grupoConcepto);
        }
    }

    /**
     * VMM 24/09/26 MD-2725: crea el ExpedientePresup padre para un concepto nuevo y sus
     * ExpedientePresupMonto hijos (uno por cada fila del grupo, incluida la cabecera con
     * nmAnualidad=0 - mismo rango que usa FinanciacionBean.addNuevoConcepto al dar de alta un
     * concepto nuevo desde pantalla).
     */
    private void crearConceptoNuevoDesdeModificacion(Integer idExpediente,
            List<ExpedienteModifPresupMontoDTO> grupoConcepto) throws ServiceException {

        if (grupoConcepto.isEmpty()) {
            return;
        }

        ExpedienteModifPresupMontoDTO cabecera = grupoConcepto.get(0);
        CatalogosValoresDTO tipoFinanciacionDTO = cabecera.getTipoFinanciacionDTO();

        // nmPresup no viene en ExpedienteModifPresupMonto (no se persiste en ningun sitio hoy) y no
        // hay forma de derivarlo de tipoFinanciacion; miramos si esa partida ya existe en el
        // expediente bajo nmPresup 1 o 2 y reusamos ese, y si es una partida completamente nueva
        // (nunca usada en este expediente) caemos a 1 por defecto. La tematica se copia de esa misma
        // partida hermana si la encontramos.
        ExpedientePresupDTO partidaExistente = this.buscarPartidaExistenteEnExpediente(idExpediente,
                tipoFinanciacionDTO);
        Integer nmPresup = partidaExistente != null ? partidaExistente.getNmPresup() : 1;
        CatalogosValoresDTO tematicaDTO = partidaExistente != null ? partidaExistente.getTematicaDTO() : null;

        ExpedientePresupDTO nuevoConcepto = new ExpedientePresupDTO();
        nuevoConcepto.setIdExpediente(idExpediente);
        nuevoConcepto.setNmPresup(nmPresup);
        nuevoConcepto.setTipoFinanciacionDTO(tipoFinanciacionDTO);
        nuevoConcepto.setTematicaDTO(tematicaDTO);
        nuevoConcepto.setTlConcepto(cabecera.getTlConcepto());
        nuevoConcepto.setItNuevoConceptoFinan(Constantes.AfirmacionNegacion.S);

        // VMM 22/10/26 MD-3168: el concepto nuevo se creaba sin TL_OBSERVACION, NM_PORCENTAJE_INTENSIDAD,
        // ID_ESTADO ni NM_AYUDA, a diferencia de los que da de alta el gestor desde la pantalla de
        // financiacion (FinanciacionBean.addNuevoConcepto). Porcentaje de intensidad, ayuda y estado son
        // comunes a todos los conceptos del expediente: se copian de uno de referencia (la partida
        // hermana si la hay, y si no cualquier otro concepto del expediente con esos datos informados).
        ExpedientePresupDTO referencia = (partidaExistente != null
                && partidaExistente.getNmPorcentajeIntensidad() != null)
                        ? partidaExistente
                        : this.buscarPresupReferenciaEnExpediente(idExpediente);
        if (referencia != null) {
            nuevoConcepto.setNmPorcentajeIntensidad(referencia.getNmPorcentajeIntensidad());
            nuevoConcepto.setNmAyuda(referencia.getNmAyuda());
            nuevoConcepto.setNmAyudaSol(referencia.getNmAyudaSol()); //VMM 27/10/26 MD-3094
            nuevoConcepto.setEstadoDTO(referencia.getEstadoDTO());
        }

        // La observacion es la que el gestor escribio para el concepto en la modificacion; si no hay,
        // el mismo texto que usa la pantalla al dar de alta un concepto nuevo.
        String observacion = grupoConcepto.stream()
                .map(ExpedienteModifPresupMontoDTO::getCdObservaciones)
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(null);
        nuevoConcepto.setTlObservacion(StringUtils.isNotBlank(observacion) ? observacion
                : "Se añade concepto " + StringUtils.defaultString(cabecera.getTlConcepto()));

        nuevoConcepto = this.expedientePresupService.crearNuevoConceptoPadre(nuevoConcepto);
        Integer idExpedientePresupNuevo = nuevoConcepto.getIdExpedientePresup();

        for (ExpedienteModifPresupMontoDTO monto : grupoConcepto) {
            this.crearHijoYEnlazar(monto, idExpedientePresupNuevo);
        }
    }

    /**
     * VMM 24/09/26 MD-2725: crea el ExpedientePresupMonto hijo para una fila de modificacion sobre un
     * ExpedientePresup padre ya existente, y enlaza de vuelta su id en la fila de origen.
     */
    private void crearHijoYEnlazar(ExpedienteModifPresupMontoDTO monto, Integer idExpedientePresup)
            throws ServiceException {

        ExpedientePresupMontoDTO nuevoMonto = new ExpedientePresupMontoDTO();
        nuevoMonto.setIdExpedientePresup(idExpedientePresup);
        nuevoMonto.setNmAnualidad(monto.getNmAnualidad());
        nuevoMonto.setNmImporte(monto.getNmImporte());
        nuevoMonto = this.expedientePresupMontoService.crearNuevoConceptoHijo(nuevoMonto);

        monto.setIdExpedientePresupMonto(nuevoMonto.getIdExpedientePresupMonto());
        this.expedienteModifPresupMontoService.actualizar(monto);
    }

    /**
     * VMM 22/10/26 MD-3168: primer concepto del expediente (nmPresup 1 y luego 2) que tenga informado el
     * porcentaje de intensidad, para copiar de el los datos comunes (porcentaje, ayuda y estado) a un
     * concepto nuevo creado por el batch.
     */
    private ExpedientePresupDTO buscarPresupReferenciaEnExpediente(Integer idExpediente)
            throws ServiceException {

        if (idExpediente == null) {
            return null;
        }

        for (Integer nmPresup : new Integer[] { 1, 2 }) {
            Optional<ExpedientePresupDTO> match = this.expedientePresupService
                    .bucarPresupByIdExpAndNmPresup(idExpediente, nmPresup).stream()
                    .filter(p -> p.getNmPorcentajeIntensidad() != null)
                    .findFirst();
            if (match.isPresent()) {
                return match.get();
            }
        }

        return null;
    }

    /**
     * VMM 24/09/26 MD-2725: busca si la partida (tipoFinanciacion) ya existe para este expediente,
     * bajo nmPresup 1 o 2 (en ese orden), para poder reusar su nmPresup y tematica al crear un
     * concepto nuevo que pertenece a la misma partida.
     */
    private ExpedientePresupDTO buscarPartidaExistenteEnExpediente(Integer idExpediente,
            CatalogosValoresDTO tipoFinanciacionDTO) throws ServiceException {

        if (idExpediente == null || tipoFinanciacionDTO == null || tipoFinanciacionDTO.getIdValor() == null) {
            return null;
        }

        for (Integer nmPresup : new Integer[] { 1, 2 }) {
            List<ExpedientePresupDTO> existentes = this.expedientePresupService
                    .bucarPresupByIdExpAndNmPresup(idExpediente, nmPresup);
            Optional<ExpedientePresupDTO> match = existentes.stream()
                    .filter(p -> p.getTipoFinanciacionDTO() != null
                            && tipoFinanciacionDTO.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                    .findFirst();
            if (match.isPresent()) {
                return match.get();
            }
        }

        return null;
    }

    /**
     * VMM 24/09/26 MD-2725: vuelca las fechas de plazo aceptadas por el gestor a la anualidad
     * correspondiente del expediente.
     */
    private void aplicarResolucionPlazos(ExpedienteModifPresPlazoOtra expdienteModifPresupuesto)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEMODIFPRESPLAZOOTRA,
                expdienteModifPresupuesto.getIdExpedienteModifPresPlazoOtra(), Operador.EQUALS));

        List<ExpedienteModifPlazoDTO> listaPlazosModif = this.expedienteModifPlazoService.buscar(0, 0,
                filtros, null);

        for (ExpedienteModifPlazoDTO plazoModif : listaPlazosModif) {
            ExpedienteAnualidad anualidad = this.expedienteAnualidadService
                    .find(plazoModif.getIdExpedienteAnualidad());
            if (anualidad != null) {
                anualidad.setFcFinEjecucion(plazoModif.getFcFinEjecucion());
                anualidad.setFcIniEjecucion(plazoModif.getFcIniEjecucion());
                // VMM 20/10/26 MD-3156: campos de auditoria del batch (no hay sesion web)
                anualidad.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                anualidad.setFcModifFila(new Date());
                this.expedienteAnualidadService.insertOrUpdate(anualidad);
            }
        }
    }

}
