package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ExpedientePresMontHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresupDAO;
import org.madrid.saii.lib.dao.ExpedientePresupHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresupMontoDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupToDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio ExpedienteFin
 *
 * @author nttdata
 */
@Service("expedientePresupService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedientePresupServiceImpl
        extends BaseServiceImpl<ExpedientePresup, Integer, ExpedientePresupDAO>
        implements ExpedientePresupService {

    @Autowired
    @Qualifier("expedientePresupHistDAO")
    private ExpedientePresupHistDAO expedientePresupHistDAO;

    @Autowired
    @Qualifier("expedientePresupMontoDAO")
    private ExpedientePresupMontoDAO expedientePresupMontoDAO;

    @Autowired
    @Qualifier("expedientePresMontHistDAO")
    private ExpedientePresMontHistDAO expedientePresMontHistDAO;
    
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;
    
    @Autowired
    @Qualifier("expedientePresupDAO")
    private ExpedientePresupDAO expedientePresupDAO;
    
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedientePresupDAO
     */
    @Autowired
    public ExpedientePresupServiceImpl(
            @Qualifier("expedientePresupDAO") ExpedientePresupDAO expedientePresupDAO) {
        super();
        this.setDao(expedientePresupDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedientePresupServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedientePresupDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ExpedientePresupToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedientePresupDTO buscarId(Integer idExpedienteFin) throws ServiceException {
        return ExpedientePresupToDTO.INSTANCE.apply(this.getDao().find(idExpedienteFin));
    }

    /**
     *
     */
    @Override
    public List<ExpedientePresupDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedientePresupToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void guardar(List<ExpedientePresupDTO> presupuestos, Double montoAyuda, Double montoAyudaSol,
            ExpedienteDTO exp) throws ServiceException {

        Date fechaHoy = new Date();

        for (ExpedientePresupDTO presupuestoDTO : presupuestos) {
            ExpedientePresup presup = this.getDao().find(presupuestoDTO.getIdExpedientePresup());
            //MD-2731: no guardamos unas observaciones generales sino que ahora cada exp presup tiene su propia observacion que se guarda al editar su celda
            //presup.setTlObservacion(tlObservacion);  
            //MD-2803: setamos aqui los valores de conceptos y observaciones de todas las filas 
            //por si el usuario metió alguno nuevo y tabuló sin disparar los listeners intermedios de guardarConceptoObservac
            presup.setTlObservacion(presupuestoDTO.getTlObservacion()); 
            presup.setTlConcepto(presupuestoDTO.getTlConcepto());
            presup.setNmAyuda(montoAyuda);
            presup.setNmAyudaSol(montoAyudaSol); //VMM 27/10/26 MD-3094
            //MD-2801: ponemos aqui los campos de auditoria antes del merge
            presup.setFcAltaFila(fechaHoy);
            presup.setFcModifFila(fechaHoy);
            
            if (exp.getEstadoProvisionalDTO() != null && exp.getEstadoProvisionalDTO().getIdValor() != null
                    && exp.getItAlegaciones()  != null && exp.getItAlegaciones().equals(Constantes.AfirmacionNegacion.S)
                    && exp.getItAlegacionRevisada() != null && exp.getItAlegacionRevisada().equals(Constantes.AfirmacionNegacion.S)) {               
                presup.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_DEFINITVA))); 
            }
            
            if (Objects.isNull(presup.getIdExpedientePresup())) {
                presup = this.getDao().insert(presup);  //cuando entra aqui? no deberia...siempre es un update de lo que hay en presup financiable
            } else {
                presup = this.getDao().merge(presup);
            }
            
            //cambio de estados solo para grabar en el historico
            if (exp.getEstadoProvisionalDTO() != null && exp.getEstadoProvisionalDTO().getIdValor() != null
                    && exp.getItAlegaciones()  != null && exp.getItAlegaciones().equals(Constantes.AfirmacionNegacion.S)
                    && exp.getItAlegacionRevisada() != null && exp.getItAlegacionRevisada().equals(Constantes.AfirmacionNegacion.S)) {     
                presup.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_DEFINITVA)));
                this.expedientePresupHistDAO.crearHistorico(presup);
            }else {
                
                presup.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_PROVISIONAL)));
                this.expedientePresupHistDAO.crearHistorico(presup);
                
                presup.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_APROBADA)));
                this.expedientePresupHistDAO.crearHistorico(presup);
            }
            
            List<ExpedientePresupMontoDTO> montosPresupuesto = presupuestoDTO
                    .getLstExpedientePresupMontoDTO().stream()
                    .filter(monto -> Boolean.FALSE.equals(monto.getEsMontoTotal()))
                    .collect(Collectors.toList());

            for (ExpedientePresupMontoDTO montoDTO : montosPresupuesto) {
                ExpedientePresupMonto monto = this.expedientePresupMontoDAO
                        .find(montoDTO.getIdExpedientePresupMonto());
                monto.setNmImporte(montoDTO.getNmImporte());

                if (Objects.isNull(monto.getIdExpedientePresupMonto())) {
                    monto = this.expedientePresupMontoDAO.insert(monto);
                } else {
                    monto = this.expedientePresupMontoDAO.merge(monto);
                }

                this.expedientePresMontHistDAO.crearHistorico(monto);
            }
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ExpedientePresupDTO expedientePresupDTO) throws ServiceException {
        expedientePresupDTO.setCdUsuBaja(null);
        expedientePresupDTO.setFcBajaFila(null);
        this.getDao().update(ExpedientePresupToEntity.INSTANCE.apply(expedientePresupDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ExpedientePresupDTO expedientePresupDTO)
            throws ServiceException {
        expedientePresupDTO.setCdUsuBaja(UserUtils.getUserName());
        expedientePresupDTO.setFcBajaFila(new Date());
        this.getDao().update(ExpedientePresupToEntity.INSTANCE.apply(expedientePresupDTO));
    }

    /**
     * VMM 24/09/26 MD-3074: guardarFinanciacion/guardarFinanciacionConcepto se invocan cada vez que el gestor
     * pulsa "calcular intensidad" (ver FinanciacionBean.cargarDatosPresupuesto) e insertaban siempre
     * conceptos NUEVOS sin comprobar si ya existian de una llamada anterior - pulsar el boton dos
     * veces duplicaba los conceptos "cargados desde el formulario" en la primera partida. Antes de
     * volver a cargar, se da de baja logica cualquier concepto de este (idExpediente, nmPresup) que
     * venga de una carga previa (itNuevoConceptoFinan != S), dejando intactos los que el gestor haya
     * anadido a mano con el boton "+" (itNuevoConceptoFinan = S, ver FinanciacionBean.addNuevoConcepto),
     * que no se deben perder al recalcular la intensidad.
     */
    private void darDeBajaConceptosCargadosPrevios(Integer idExpediente, Integer nmPresup)
            throws ServiceException {
        for (ExpedientePresupDTO presupExistente : this.bucarPresupByIdExpAndNmPresup(idExpediente,
                nmPresup)) {
            if (Constantes.AfirmacionNegacion.S.equals(presupExistente.getItNuevoConceptoFinan())) {
                continue;
            }
            if (presupExistente.getLstExpedientePresupMontoDTO() != null) {
                for (ExpedientePresupMontoDTO montoDTO : presupExistente.getLstExpedientePresupMontoDTO()) {
                    if (Boolean.TRUE.equals(montoDTO.getEsMontoTotal())
                            || montoDTO.getIdExpedientePresupMonto() == null) {
                        continue;
                    }
                    this.expedientePresupMontoService.eliminarMonto(montoDTO);
                }
            }
            this.eliminarExpPresup(presupExistente);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void guardarFinanciacion(List<BloqueComponenteDTO> bloquesSinTotal, Double montoAyuda,
            Double montoAyudaSol, Integer nmPresup, Integer idExpediente, Integer idTematica,
            Double intensidadSol) throws ServiceException {

        // VMM 24/09/26 MD-3074: ver comentario en darDeBajaConceptosCargadosPrevios - evita duplicar
        // conceptos si el gestor pulsa "calcular intensidad" mas de una vez.
        this.darDeBajaConceptosCargadosPrevios(idExpediente, nmPresup);

        // Recuperamos la fecha de alta para asignar la misma a todos los
        // registros

        for (BloqueComponenteDTO bc : bloquesSinTotal) {

            ExpedientePresup pre = new ExpedientePresup();
            pre.setTipoFinanciacion(new CatalogosValores(bc.getTipoFinanciacionDTO().getIdValor()));
            pre.setNmAyuda(montoAyuda);
            pre.setNmAyudaSol(montoAyudaSol); //VMM 27/10/26 MD-3094
            pre.setIdExpediente(idExpediente);
            pre.setNmPresup(nmPresup);
            //MD-2731: las observaciones ya no son obligatorias a menos que se modifique un importe por el gestor
            //pre.setTlObservacion("Datos de presupuesto financiable inicial.");
            pre.setItNuevoConceptoFinan(Constantes.AfirmacionNegacion.N);
            if (idTematica != null) {
                pre.setTematica(new CatalogosValores(idTematica));
            }
            //MD-2788: seteamos la intensidad de la ayuda informada en el formulario de solicitud
            pre.setNmPorcentajeIntensidad(intensidadSol);
            
            pre = this.getDao().insert(pre);

            this.expedientePresupHistDAO.crearHistorico(pre);

            List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                    .collect(Collectors.toList());

            Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                    .compareTo(c2.getNmColumna());

            Collections.sort(datos, compareByNmColumna);
           
            for (DatoDTO dato : datos) {  
                
                ExpedientePresupMonto preMon = new ExpedientePresupMonto();
                preMon.setNmAnualidad(dato.getNmColumna());
                preMon.setNmImporte(Double.valueOf(dato.getDsValor()));
                preMon.setIdExpedientePresup(pre.getIdExpedientePresup());
                preMon = this.expedientePresupMontoDAO.insert(preMon);

                this.expedientePresMontHistDAO.crearHistorico(preMon);

            }
        }
    }
    
    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void guardarFinanciacionConcepto(List<BloqueComponenteDTO> bloquesSinTotal, Double montoAyuda,
            Double montoAyudaSol, Integer nmPresup, Integer idExpediente, Integer idTematica,
            Double intensidadSol) throws ServiceException {

        // VMM 24/09/26 MD-3074: ver comentario en darDeBajaConceptosCargadosPrevios - evita duplicar
        // conceptos si el gestor pulsa "calcular intensidad" mas de una vez.
        this.darDeBajaConceptosCargadosPrevios(idExpediente, nmPresup);

        // Recuperamos la fecha de alta para asignar la misma a todos los
        // registros

        for (BloqueComponenteDTO bc : bloquesSinTotal) {

            ExpedientePresup pre = new ExpedientePresup();
            pre.setTipoFinanciacion(new CatalogosValores(bc.getTipoFinanciacionDTO().getIdValor()));
            pre.setNmAyuda(montoAyuda);
            pre.setNmAyudaSol(montoAyudaSol); //VMM 27/10/26 MD-3094
            pre.setIdExpediente(idExpediente);
            pre.setNmPresup(nmPresup);
            //MD-2731: las observaciones ya no son obligatorias a menos que se modifique un importe por el gestor
            //pre.setTlObservacion("Datos de presupuesto financiable inicial.");
            pre.setItNuevoConceptoFinan(Constantes.AfirmacionNegacion.N);
            pre.setTematica(new CatalogosValores(idTematica));
            //MD-2788: seteamos la intensidad de la ayuda informada en el formulario de solicitud
            pre.setNmPorcentajeIntensidad(intensidadSol);
            // Asigna el valor del dato donde nmColumna = 0
            List<DatoDTO> datosConcepto = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                    .collect(Collectors.toList());

            String tlConcepto = datosConcepto.stream()
                    .filter(d -> d.getNmColumna() == 0)
                    .map(DatoDTO::getDsValor)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            pre.setTlConcepto(tlConcepto);
            pre = this.getDao().insert(pre);
            
            this.expedientePresupHistDAO.crearHistorico(pre);
            
            List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                    .collect(Collectors.toList());
            
            Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                    .compareTo(c2.getNmColumna());
            
            Collections.sort(datos, compareByNmColumna);
            
            for (DatoDTO dato : datos) {
                
                ExpedientePresupMonto preMon = new ExpedientePresupMonto();
                preMon.setNmAnualidad(dato.getNmColumna());

                if (dato.getNmColumna() != null && Integer.valueOf(0).equals(dato.getNmColumna())) {
                    preMon.setNmImporte(0.0);
                } else {
                    //VMM 28/10/25 MD-2110: controlamos que no lleguen importes nulos de saii_dato
                    if (dato.getDsValor() != null) {
                        preMon.setNmImporte(Double.valueOf(dato.getDsValor()));
                    }else {
                        //si nos llegara algun valor nulo de saii_sol, que no deberia, le seteamos un 0
                        preMon.setNmImporte(0.0);
                    }
                }
                
                preMon.setIdExpedientePresup(pre.getIdExpedientePresup());
                preMon = this.expedientePresupMontoDAO.insert(preMon);
                
                this.expedientePresMontHistDAO.crearHistorico(preMon);
            }
        }
    }

    @Override
    public List<ExpedientePresupDTO> buscarPresupIncialExpediente(Integer idExpediente) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));        

        return this.buscar(0, -1, filtros, null);
    }
    
    @Override
    public List<ExpedientePresupDTO> consultarExpPresupFinanPorIdExp(Integer idExpediente, Integer nmPresup) throws ServiceException {
        
        List<AtlasFilter> filtros = new ArrayList<>();
        List<AtlasSort> ordenamiento = new ArrayList<>();
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_NM_PRESUP, nmPresup, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PRESUP, true));

        //buscamos el presupuesto financiable actual del expediente
        return this.buscar(0, -1, filtros, ordenamiento);
    }
    
    @Override
    public List<ExpedientePresupDTO> bucarPresupByIdExpAndNmPresup(Integer idExpediente, Integer nmPresup) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_NM_PRESUP, nmPresup, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.buscar(0, -1, filtros, null);
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public void actualizarConceptoObservac(final ExpedientePresupDTO expedientePresupDTO) 
            throws ServiceException {
        
        //buscamos la entidad a modificar
        ExpedientePresup expPresup = this.expedientePresupDAO.find(expedientePresupDTO.getIdExpedientePresup());
        
        //ver si esto es necesario...
        if (expedientePresupDTO.getTlConcepto() != null) {
            expPresup.setTlConcepto(expedientePresupDTO.getTlConcepto());
        }else {
            expPresup.setTlConcepto("");    
        }
        if (expedientePresupDTO.getTlObservacion() != null) {
            expPresup.setTlObservacion(expedientePresupDTO.getTlObservacion());
        }else {
            expPresup.setTlObservacion("");    
        }
        
        // campos de auditoría de modificación 
        expPresup.setCdUsuModif(UserUtils.getUserName());
        expPresup.setFcModifFila(new Date());

        // lanzamos el UPDATE del DAO hacia la BD
        this.getDao().merge(expPresup);
    }
    
    @Override
    @Transactional(readOnly = false)
    public void actualizarIntensidadAyuda(Integer idExpediente, Integer nmPresup, Double nmPorcentajeIntensidad,
            Double montoAyuda, Double montoAyudaSol) throws ServiceException {

        List<ExpedientePresupDTO> registros = bucarPresupByIdExpAndNmPresup(idExpediente, nmPresup);

        for (ExpedientePresupDTO dto : registros) {
            ExpedientePresup expPresup = this.expedientePresupDAO.find(dto.getIdExpedientePresup());
            expPresup.setNmPorcentajeIntensidad(nmPorcentajeIntensidad);
            expPresup.setNmAyuda(montoAyuda);
            expPresup.setNmAyudaSol(montoAyudaSol); //VMM 27/10/26 MD-3094
            expPresup.setCdUsuModif(UserUtils.getUserName());
            expPresup.setFcModifFila(new Date());
            this.getDao().merge(expPresup);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedientePresupDTO crearNuevoConceptoPadre(ExpedientePresupDTO expPresupDTO) throws ServiceException {
        
        // Convertir el DTO del Padre a Entidad
        ExpedientePresup expPresupEntity = ExpedientePresupToEntity.INSTANCE.apply(expPresupDTO);
        
        if (expPresupEntity != null) {
            // VMM 24/09/26 MD-2725: UserUtils.getUserName() da vacio cuando este metodo se llama desde
            // el batch (ResolucionModificacionServiceImpl), que no tiene sesion web - caia en
            // ORA-01400 al ser CD_USU_ALTA NOT NULL. Mismo fallback que ya usan otros *HistDAOImpl.
            String cdUsuAlta = UserUtils.getUserName();
            expPresupEntity.setCdUsuAlta(
                    StringUtils.isNotBlank(cdUsuAlta) ? cdUsuAlta : Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            expPresupEntity.setFcAltaFila(new Date());

            expPresupEntity = this.getDao().insert(expPresupEntity);

            //guardamos el nuevo concepto en el historico
            this.expedientePresupHistDAO.crearHistorico(expPresupEntity);
        }
        
        return ExpedientePresupToDTO.INSTANCE.apply(expPresupEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void eliminarExpPresup(ExpedientePresupDTO expPresupDTO) {
        
        //buscamos la entidad a eliminar para dar la baja logica
        ExpedientePresup expPresup = this.expedientePresupDAO.find(expPresupDTO.getIdExpedientePresup());
        
        if (expPresup != null) {
            //campos de auditoria de la baja
            expPresup.setCdUsuBaja(UserUtils.getUserName());
            expPresup.setFcBajaFila(new Date());
            
            // lanzamos el UPDATE del DAO hacia la BD
            this.getDao().merge(expPresup);
        }
        
    }

    @Override
    @Transactional(readOnly = false)
    public Boolean existeModificacionesParaGuardar(List<BloqueDTO> lstBloque,
            List<ExpedientePresupDTO> lstPresupuestoFinanciable1,
            List<ExpedientePresupDTO> lstPresupuestoFinanciable2, Double montoAyuda) {
        Boolean existeModificaciones = Boolean.FALSE;
        for (int i = 0; i < lstBloque.size(); i++) {
            List<ExpedientePresupDTO> presupuestosSinTotal = new ArrayList<>();
            if (i == 0) {
                presupuestosSinTotal = lstPresupuestoFinanciable1.stream()
                        .filter(presupuesto -> Objects.nonNull(presupuesto.getIdExpedientePresup()))
                        .collect(Collectors.toList());

            } else {
                presupuestosSinTotal = lstPresupuestoFinanciable2.stream()
                        .filter(presupuesto -> Objects.nonNull(presupuesto.getIdExpedientePresup()))
                        .collect(Collectors.toList());
            }
            for (ExpedientePresupDTO presupuestoDTO : presupuestosSinTotal) {
                ExpedientePresup presup = this.getDao()
                        .find(presupuestoDTO.getIdExpedientePresup());
                if ((presup.getTlObservacion() != null
                        && !presup.getTlObservacion().equals(presupuestoDTO.getTlObservacion()))
                        || (presup.getTlObservacion() == null
                                && !"".equals(presupuestoDTO.getTlObservacion()))
                        || (presup.getTlConcepto() != null
                                && !presup.getTlConcepto().equals(presupuestoDTO.getTlConcepto()))
                        || (presup.getTlConcepto() == null
                                && !"".equals(presupuestoDTO.getTlConcepto()))
                        || !presup.getNmAyuda().equals(montoAyuda)) {
                    existeModificaciones = Boolean.TRUE;
                }
            }
        }
        return existeModificaciones;
    }

}
