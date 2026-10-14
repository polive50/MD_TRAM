package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.madrid.saii.lib.dao.ExpedienteModifPresupMontoDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPresupMonto;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresupMontoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresupMontoToEntity;
import org.madrid.saii.lib.util.Constantes;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ExpedienteModifPresupMonto
 *
 * @author quental
 */
@Slf4j
@Service("expedienteModifPresupMontoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteModifPresupMontoServiceImpl
        extends BaseServiceImpl<ExpedienteModifPresupMonto, Integer, ExpedienteModifPresupMontoDAO>
        implements ExpedienteModifPresupMontoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteModifPresupMontoDAO
     */
    @Autowired
    public ExpedienteModifPresupMontoServiceImpl(
            @Qualifier("expedienteModifPresupMontoDAO") ExpedienteModifPresupMontoDAO expedienteModifPresupMontoDAO) {
        super();
        this.setDao(expedienteModifPresupMontoDAO);
    }
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;

    /**
     * Constructor Vacio
     */
    public ExpedienteModifPresupMontoServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedienteModifPresupMontoDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ExpedienteModifPresupMontoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteModifPresupMontoDTO buscarId(Integer idExpedienteModifPresupMonto) throws ServiceException {
        return ExpedienteModifPresupMontoToDTO.INSTANCE.apply(this.getDao().find(idExpedienteModifPresupMonto));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteModifPresupMontoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteModifPresupMontoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModifPresupMontoDTO guardar(ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException {
        return ExpedienteModifPresupMontoToDTO.INSTANCE.apply(this.insertOrUpdate(
                ExpedienteModifPresupMontoToEntity.INSTANCE.apply(expedienteModifPresupMontoDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException {
        expedienteModifPresupMontoDTO.setCdUsuBaja(UserUtils.getUserName());
        expedienteModifPresupMontoDTO.setFcBajaFila(new Date());
        this.getDao()
                .update(ExpedienteModifPresupMontoToEntity.INSTANCE.apply(expedienteModifPresupMontoDTO));
    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void actualizar(ExpedienteModifPresupMontoDTO expedienteModifPresupMontoDTO)
            throws ServiceException {
       expedienteModifPresupMontoDTO.setCdUsuModif(UserUtils.getUserName());
       expedienteModifPresupMontoDTO.setFcModifFila(new Date());
       // VMM 24/09/26 MD-2725: update() (session.update()) rechaza esta fila con
       // NonUniqueObjectException si el llamante ya la cargo en la sesion actual (p.ej. desde la
       // misma lista devuelta por buscar() antes de llamar aqui) - la entidad reconstruida desde el
       // DTO es una instancia Java distinta con el mismo id. merge() concilia el estado sobre la
       // instancia ya gestionada en vez de exigir que sea la unica - ver mismo fix en
       // ExpedientePresupMontoServiceImpl.guardarConHistorico.
       this.getDao()
               .merge(ExpedienteModifPresupMontoToEntity.INSTANCE.apply(expedienteModifPresupMontoDTO));
   }
   
   
   @Override
   public List<ExpedienteModifPresupMontoDTO> consultarModifPresupPorIdModif(Integer IdExpedienteModifPresPlazoOtra)
           throws ServiceException {
       
          List<AtlasFilter> filtros = new ArrayList<>();
          List<AtlasSort> ordenamiento = new ArrayList<>();
          
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEMODIFPRESPLAZOOTRA,
                  IdExpedienteModifPresPlazoOtra, Operador.EQUALS));
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                     
          ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_MODIF_PRESUP_MONTO, true));
          
          return this.buscar(0, -1, filtros, ordenamiento);
   }

//    @Override
//    public void guardarModifPresupConcepto(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
//            Integer idExpedienteModifPresPlazoOtra) throws ServiceException {
//        
//        for (BloqueComponenteDTO bc : bloquesSinTotal) {
//            
//            // Asigna el valor del dato donde nmColumna = 0
//            List<DatoDTO> datosConcepto = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
//                    .collect(Collectors.toList());
//
//            String tlConcepto = datosConcepto.stream()
//                    .filter(d -> d.getNmColumna() == 0)
//                    .map(DatoDTO::getDsValor)
//                    .filter(Objects::nonNull)
//                    .findFirst()
//                    .orElse(null);
//
//            List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
//                    .collect(Collectors.toList());
//            
//            Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
//                    .compareTo(c2.getNmColumna());
//            
//            Collections.sort(datos, compareByNmColumna);
//
//            for (DatoDTO dato : datos) {
//                
//                ExpedienteModifPresupMonto modPreMonto = new ExpedienteModifPresupMonto();
//                modPreMonto.setNmAnualidad(dato.getNmColumna());
//                
//                if (dato.getNmColumna() != null && Integer.valueOf(0).equals(dato.getNmColumna())) {
//                    modPreMonto.setNmImporte(0.0);
//                } else {
//                    if (dato.getDsValor() != null) {
//                        modPreMonto.setNmImporte(Double.valueOf(dato.getDsValor()));
//                    }else {
//                        //si nos llegara algun valor nulo de saii_sol, que no deberia, le seteamos un 0
//                        modPreMonto.setNmImporte(0.0);
//                    }
//                }
//                //modPreMonto.setNmImporte(Double.valueOf(dato.getDsValor()));
//                //consultar el importe concedido con dato.getExpedientePresupMonto().getIdExpedientePresupMonto()
//                ExpedientePresupMonto expPresupMonto = new ExpedientePresupMonto(); //this.expedientePresupMontoService.find(dato.getExpedientePresupMonto().getIdExpedientePresupMonto());
//                modPreMonto.setNmImporteConcedido(expPresupMonto.getNmImporte());
//                modPreMonto.setIdExpedientePresupMonto(null);//dato.getExpedientePresupMonto().getIdExpedientePresupMonto());
//                modPreMonto.setCdObservaciones(null);  //las observaciones de modificaciones iniciales van vacias
//                modPreMonto.setIdExpedienteModifPresPlazoOtra(idExpedienteModifPresPlazoOtra);
//                modPreMonto.setCheckModifMonto(Constantes.AfirmacionNegacion.N);  //creo que no se usa....
//                //modPreMonto.setTlConcepto(tlConcepto);  //falta crear esta columna...si quereis, yo lo veo mas facil asi
//   
//                 this.insert(modPreMonto);
//            }
//        }
//    }
    
//    @Override
//    public void guardarModifPresup(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
//            Integer idExpedienteModifPresPlazoOtra) throws ServiceException {
//        
//        for (BloqueComponenteDTO bc : bloquesSinTotal) {
//
//            List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
//                    .collect(Collectors.toList());
//
//            Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
//                    .compareTo(c2.getNmColumna());
//
//            Collections.sort(datos, compareByNmColumna);
//
//            for (DatoDTO dato : datos) {
//                
//                ExpedienteModifPresupMonto modPreMonto = new ExpedienteModifPresupMonto();
//                modPreMonto.setNmAnualidad(dato.getNmColumna());
//                modPreMonto.setNmImporte(Double.valueOf(dato.getDsValor()));
//                //consultar el importe concedido con dato.getExpedientePresupMonto().getIdExpedientePresupMonto()
//                ExpedientePresupMonto expPresupMonto = new ExpedientePresupMonto(); //this.expedientePresupMontoService.find(dato.getExpedientePresupMonto().getIdExpedientePresupMonto());
//                modPreMonto.setNmImporteConcedido(expPresupMonto.getNmImporteAdmitido());
//                modPreMonto.setIdExpedientePresupMonto(null);//dato.getExpedientePresupMonto().getIdExpedientePresupMonto());
//                modPreMonto.setCdObservaciones(null);  //las observaciones de modificaciones iniciales van vacias
//                modPreMonto.setIdExpedienteModifPresPlazoOtra(idExpedienteModifPresPlazoOtra);
//                modPreMonto.setCheckModifMonto(Constantes.AfirmacionNegacion.N);
//   
//               this.insert(modPreMonto);
//            }
//        }
//        
//    }
   
   
   @Override
   @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
   public void guardarModifPresup(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
           Integer idExpedienteModifPresPlazoOtra) throws ServiceException {

       for (BloqueComponenteDTO bc : bloquesSinTotal) {

           List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                   .collect(Collectors.toList());

           Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                   .compareTo(c2.getNmColumna());

           Collections.sort(datos, compareByNmColumna);

           for (DatoDTO dato : datos) {

               ExpedienteModifPresupMonto modPreMonto = new ExpedienteModifPresupMonto();
               modPreMonto.setNmAnualidad(dato.getNmColumna());
               modPreMonto.setNmImporte(Double.valueOf(dato.getDsValor()));

               // VMM 10/09/26 MD-2725: el enlace dato.getExpedientePresupMonto() se anadio
               // recientemente; puede venir sin rellenar tanto en datos antiguos (revisar/rellenar
               // a mano en BBDD) como en conceptos nuevos dados de alta directamente en
               // saii_solicitud, que todavia no tienen ExpedientePresupMonto (se creara al resolver
               // la modificacion, ver crearNuevoConceptoHijo en DocumentoPtfrServiceImpl). En ambos
               // casos se deja sin enlazar aqui en vez de bloquear la carga de toda la modificacion.
               if (dato.getExpedientePresupMonto() == null
                       || dato.getExpedientePresupMonto().getIdExpedientePresupMonto() == null) {
                   log.warn("El Dato idDato={} (idBloqueComponente={}, nmColumna={}) no tiene enlazado su "
                           + "ExpedientePresupMonto - se asume concepto nuevo sin presupuesto final todavia.",
                           dato.getIdDato(), dato.getIdBloqueComponente(), dato.getNmColumna());
                   modPreMonto.setNmImporteConcedido(0.0);
                   if (bc.getTipoFinanciacionDTO() != null) {
                       modPreMonto.setTipoFinanciacion(
                               CatalogosValoresToEntity.INSTANCE.apply(bc.getTipoFinanciacionDTO()));
                   }
               } else {
                   //consultar el importe concedido con dato.getExpedientePresupMonto().getIdExpedientePresupMonto()
                   ExpedientePresupMonto expPresupMonto = this.expedientePresupMontoService
                           .find(dato.getExpedientePresupMonto().getIdExpedientePresupMonto());

                   modPreMonto.setNmImporteConcedido(expPresupMonto.getNmImporte());
                   modPreMonto.setIdExpedientePresupMonto(expPresupMonto.getIdExpedientePresupMonto());
               }

               modPreMonto.setCdObservaciones(null);  //las observaciones de modificaciones iniciales van vacias
               modPreMonto.setIdExpedienteModifPresPlazoOtra(idExpedienteModifPresPlazoOtra);
               modPreMonto.setCheckModifMonto(Constantes.AfirmacionNegacion.N);

                this.insert(modPreMonto);
           }
       }

   }
   
   @Override
   @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
   public void guardarModifPresupConcepto(List<BloqueComponenteDTO> bloquesSinTotal, Integer nmPresup,
           Integer idExpedienteModifPresPlazoOtra) throws ServiceException {

       for (BloqueComponenteDTO bc : bloquesSinTotal) {

           // Asigna el valor del dato donde nmColumna = 0
           List<DatoDTO> datosConcepto = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                   .collect(Collectors.toList());

           String tlConcepto = datosConcepto.stream()
                   .filter(d -> d.getNmColumna() == 0)
                   .map(DatoDTO::getDsValor)
                   .filter(Objects::nonNull)
                   .findFirst()
                   .orElse(null);

           List<DatoDTO> datos = bc.getDatos().stream().filter(d -> !d.getEsMontoTotal())
                   .collect(Collectors.toList());

           Comparator<DatoDTO> compareByNmColumna = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                   .compareTo(c2.getNmColumna());

           Collections.sort(datos, compareByNmColumna);

           for (DatoDTO dato : datos) {

               ExpedienteModifPresupMonto modPreMonto = new ExpedienteModifPresupMonto();
               modPreMonto.setNmAnualidad(dato.getNmColumna());

               if (dato.getNmColumna() != null && Integer.valueOf(0).equals(dato.getNmColumna())) {
                   modPreMonto.setNmImporte(0.0);
                   modPreMonto.setTlConcepto(tlConcepto);
               } else {
                   if (dato.getDsValor() != null) {
                       modPreMonto.setNmImporte(Double.valueOf(dato.getDsValor()));
                   }else {
                       //si nos llegara algun valor nulo de saii_sol, que no deberia, le seteamos un 0
                       modPreMonto.setNmImporte(0.0);
                   }
               }

               // VMM 11/09/26 MD-2725: el enlace dato.getExpedientePresupMonto() se anadio
               // recientemente; puede venir sin rellenar tanto en datos antiguos (revisar/rellenar
               // a mano en BBDD) como en conceptos nuevos dados de alta directamente en
               // saii_solicitud, que todavia no tienen ExpedientePresupMonto - eso lo crea el
               // batch al resolver la modificacion (ver TODO en DocumentoPtfrServiceImpl). Mientras
               // tanto guardamos a que partida pertenece (tipoFinanciacion, sacado del
               // bloque_componente de origen) para poder mostrar/agrupar el concepto nuevo, en vez
               // de bloquear la carga de toda la modificacion.
               if (dato.getExpedientePresupMonto() == null
                       || dato.getExpedientePresupMonto().getIdExpedientePresupMonto() == null) {
                   log.warn("El Dato idDato={} (idBloqueComponente={}, nmColumna={}) no tiene enlazado su "
                           + "ExpedientePresupMonto - se asume concepto nuevo sin presupuesto final todavia.",
                           dato.getIdDato(), dato.getIdBloqueComponente(), dato.getNmColumna());
                   modPreMonto.setNmImporteConcedido(0.0);
                   if (bc.getTipoFinanciacionDTO() != null) {
                       modPreMonto.setTipoFinanciacion(
                               CatalogosValoresToEntity.INSTANCE.apply(bc.getTipoFinanciacionDTO()));
                   }
               } else {
                   //consultar el importe concedido con dato.getExpedientePresupMonto().getIdExpedientePresupMonto()
                   ExpedientePresupMonto expPresupMonto = this.expedientePresupMontoService
                           .find(dato.getExpedientePresupMonto().getIdExpedientePresupMonto());
                   modPreMonto.setNmImporteConcedido(expPresupMonto.getNmImporte());
                   modPreMonto.setIdExpedientePresupMonto(expPresupMonto.getIdExpedientePresupMonto());
               }
               modPreMonto.setCdObservaciones(null);  //las observaciones de modificaciones iniciales van vacias
               modPreMonto.setIdExpedienteModifPresPlazoOtra(idExpedienteModifPresPlazoOtra);
               modPreMonto.setCheckModifMonto(Constantes.AfirmacionNegacion.N);  //creo que no se usa....

                this.insert(modPreMonto);
           }
       }
   }
   
}
