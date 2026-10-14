package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDocDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToEntity;
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


@Service("expedienteContratoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteContratoServiceImpl extends BaseServiceImpl<ExpedienteContrato, Integer, ExpedienteContratoDAO>
    implements ExpedienteContratoService {
    
    @Autowired
    @Qualifier("expedienteContratoHistService")
    private ExpedienteContratoHistService expedienteContratoHistService;
    
    @Autowired
    @Qualifier("expedienteContratoDocDAO")
    private ExpedienteContratoDocDAO expedienteContratoDocDAO;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param evaluacionDAO
     */
    @Autowired
    public ExpedienteContratoServiceImpl(
            @Qualifier("expedienteContratoDAO") ExpedienteContratoDAO expedienteContratoDAO) {
        super();
        this.setDao(expedienteContratoDAO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteContratoDTO guardarExpedienteContrato(ExpedienteContratoDTO expedienteContratoDTO) throws ServiceException {
        
        /*ExpedienteContrato expedienteContrato = ExpedienteContratoToEntity.INSTANCE.apply(expedienteContratoDTO);
        if(expedienteContrato.getIdExpedienteContrato() != null) {
             this.getDao().update(expedienteContrato);
        }else {
            expedienteContrato =this.getDao().insert(expedienteContrato);
        }*/
        
        ExpedienteContrato expedienteContrato = ExpedienteContratoToEntity.INSTANCE.apply(expedienteContratoDTO);
        this.getDao().insertOrUpdate(expedienteContrato);
        
        List<ExpedienteContrato> listExpContratoNuevo = this.getDao().findByIdExpediente(expedienteContratoDTO.getIdExpediente()); 

        if(listExpContratoNuevo != null && !listExpContratoNuevo.isEmpty()) {
            ExpedienteContrato nuevoUpdateContrato=listExpContratoNuevo.get(0);
            ExpedienteContratoHist expedienteContratoHist = new ExpedienteContratoHist();
            expedienteContratoHist.setIdExpedienteContrato(nuevoUpdateContrato.getIdExpedienteContrato());
            expedienteContratoHist.setIdExpediente(nuevoUpdateContrato.getIdExpediente());
            expedienteContratoHist.setIdEstado(nuevoUpdateContrato.getIdEstado());
            expedienteContratoHist.setIdTipoContrato(nuevoUpdateContrato.getIdTipoContrato());
            expedienteContratoHist.setFcIniContrato(nuevoUpdateContrato.getFcIniContrato());
            expedienteContratoHist.setFcFinContrato(nuevoUpdateContrato.getFcFinContrato());
            expedienteContratoHist.setItIndefinido(nuevoUpdateContrato.getItIndefinido());
            expedienteContratoHist.setCdUsuAlta(nuevoUpdateContrato.getCdUsuAlta());
            expedienteContratoHist.setFcAltaFila(new Date());
            expedienteContratoHist.setInteresado(nuevoUpdateContrato.getInteresado());
            expedienteContratoHist.setIdEstadoRevAdmin(nuevoUpdateContrato.getIdEstadoRevAdmin());
            expedienteContratoHist.setFcRenuncia(nuevoUpdateContrato.getFcRenuncia());
            expedienteContratoHist.setCdEregRenuncia(nuevoUpdateContrato.getCdEregRenuncia());
            expedienteContratoHist.setFcFirmaRenuncia(nuevoUpdateContrato.getFcFirmaRenuncia());
            this.expedienteContratoHistService.insert(expedienteContratoHist);
            
            return  ExpedienteContratoToDTO.INSTANCE.apply(nuevoUpdateContrato);
                
            
        }
        
       return expedienteContratoDTO;
        
         
     }
        
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteContratoDTO actualizarExpedienteContrato(ExpedienteContratoDTO expContratoDTO) throws ServiceException {

       ExpedienteContratoDTO expedienteContratoDTOAux = new ExpedienteContratoDTO();
        
       if(expContratoDTO != null){
        
           ExpedienteContrato expedienteContrato = ExpedienteContratoToEntity.INSTANCE.apply(expContratoDTO);
           this.getDao().insertOrUpdate(expedienteContrato);
    
           ExpedienteContrato expContratoNuevo = new ExpedienteContrato();
           
           if(expContratoDTO.getIdExpedienteContrato() != null) {
               expContratoNuevo = this.find(expContratoDTO.getIdExpedienteContrato());
           }else {
               // Recuperamos el expediente
                List<ExpedienteContrato> lstExpedienteContrato = this.getDao().findByIdExpediente(expContratoDTO.getIdExpediente());
                if(lstExpedienteContrato == null || lstExpedienteContrato.isEmpty()) return null;
                    expContratoNuevo = lstExpedienteContrato.get(0);
                }
           
    
           ExpedienteContratoHist expedienteContratoHist = new ExpedienteContratoHist();
           
           expedienteContratoHist.setIdExpedienteContrato(expContratoNuevo.getIdExpedienteContrato());
           expedienteContratoHist.setIdExpediente(expContratoDTO.getIdExpediente());
           expedienteContratoHist.setIdEstado(expContratoNuevo.getIdEstado());
           expedienteContratoHist.setIdTipoContrato(expContratoNuevo.getIdTipoContrato());
           expedienteContratoHist.setFcIniContrato(expContratoNuevo.getFcIniContrato());
           expedienteContratoHist.setFcFinContrato(expContratoNuevo.getFcFinContrato());
           expedienteContratoHist.setItIndefinido(expContratoNuevo.getItIndefinido());
           expedienteContratoHist.setCdUsuAlta(expContratoNuevo.getCdUsuAlta());
           expedienteContratoHist.setFcAltaFila(new Date());
    
           expedienteContratoHist.setIdEstadoRevAdmin(expContratoNuevo.getIdEstadoRevAdmin());
           expedienteContratoHist.setCdEregRenuncia(expContratoNuevo.getCdEregRenuncia());
           expedienteContratoHist.setFcFirmaRenuncia(expContratoNuevo.getFcFirmaRenuncia());
    
           this.expedienteContratoHistService.insert(expedienteContratoHist);
    
           expedienteContratoDTOAux = ExpedienteContratoToDTO.INSTANCE.apply(expedienteContrato);
       
       }
       
    return expedienteContratoDTOAux;
    
   }
    
    public ExpedienteContratoDTO obtenerExpedienteContratoByExpediente(ExpedienteDTO dto) throws ServiceException  {
        
        ExpedienteContratoDTO result = null;
        List<ExpedienteContrato> lstExpedienteContra = this.getDao().findByIdExpediente(dto.getIdExpediente());
        if(lstExpedienteContra != null && !lstExpedienteContra.isEmpty()) {
            
            List<ExpedienteContratoDTO> expContraDTO = new ArrayList<>( Lists.transform(lstExpedienteContra,
                    ExpedienteContratoToDTO.INSTANCE));
            result = expContraDTO.get(0);
        }
        
        return result;
    }

    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public ExpedienteContratoDTO guardar(ExpedienteContratoDTO expContratoDTO) throws ServiceException {
       return ExpedienteContratoToDTO.INSTANCE
               .apply(this.insertOrUpdate(ExpedienteContratoToEntity.INSTANCE.apply(expContratoDTO)));
   }

   /**
    * Actualiza estado y estadoRevAdmin del contrato del expediente facilitado
    */
   @Override
   @Transactional(readOnly = false)
   public ExpedienteContratoDTO cambiarEstadoContrato(ExpedienteContratoDTO expContratoDTO, Integer idExpediente, Integer idEstado, Integer idEstadoRevAdmin)
           throws ServiceException {

       ExpedienteContrato expedienteContrato;
       ExpedienteContratoDTO expedienteContratoDTOAux = new ExpedienteContratoDTO();
       ExpedienteContratoDTO dto;
       
       if (expContratoDTO != null) {
       
       
           if(expContratoDTO.getIdExpedienteContrato() != null) {
               expedienteContrato = this.find(expContratoDTO.getIdExpedienteContrato());
           }else {
               // Recuperamos el expediente
               List<ExpedienteContrato> lstExpedienteContrato = this.getDao().findByIdExpediente(expContratoDTO.getIdExpediente());
               if(lstExpedienteContrato == null || lstExpedienteContrato.isEmpty()) return null;
               expedienteContrato = lstExpedienteContrato.get(0);
           }
    
           if(idEstado != null) {
               expedienteContrato.setIdEstado(new CatalogosValores(idEstado));
           }
           
           if(idEstadoRevAdmin != null) {
               expedienteContrato.setIdEstadoRevAdmin(new CatalogosValores(idEstadoRevAdmin));
           }
           
           if(expContratoDTO.getTlObservaciParaCiudadano() != null && !expContratoDTO.getTlObservaciParaCiudadano().isEmpty()) {
               expedienteContrato.setTlObservaciParaCiudadano(expContratoDTO.getTlObservaciParaCiudadano());
           }
           
           dto = ExpedienteContratoToDTO.INSTANCE.apply(expedienteContrato);
    
           // Historico
           ExpedienteContratoHist expedienteContratoHist = new ExpedienteContratoHist();
           
           expedienteContratoHist.setIdExpedienteContrato(expedienteContrato.getIdExpedienteContrato());
           expedienteContratoHist.setIdExpediente(expedienteContrato.getIdExpediente());
           expedienteContratoHist.setIdEstado(expedienteContrato.getIdEstado());
           expedienteContratoHist.setIdTipoContrato(expedienteContrato.getIdTipoContrato());
           expedienteContratoHist.setFcIniContrato(expedienteContrato.getFcIniContrato());
           expedienteContratoHist.setFcFinContrato(expedienteContrato.getFcFinContrato());
           expedienteContratoHist.setItIndefinido(expedienteContrato.getItIndefinido());
           expedienteContratoHist.setCdUsuAlta(expedienteContrato.getCdUsuAlta());
           expedienteContratoHist.setFcAltaFila(new Date());
           
           expedienteContratoHist.setIdEstadoRevAdmin(expedienteContrato.getIdEstadoRevAdmin());
           expedienteContratoHist.setCdEregRenuncia(expedienteContrato.getCdEregRenuncia());
           expedienteContratoHist.setFcFirmaRenuncia(expedienteContrato.getFcFirmaRenuncia());
    
           this.expedienteContratoHistService.insert(expedienteContratoHist);
       
           expedienteContratoDTOAux = dto;
       }
       
    return expedienteContratoDTOAux;

   }
   
   @Override
   public List<ExpedienteContratoDTO> buscarPorExpediente(Integer idExpediente)
           throws ServiceException {

       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       List<AtlasSort> ordenamiento = new ArrayList<>();
       ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_CONTRATO, true));

       return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
               ExpedienteContratoToDTO.INSTANCE));
   }
   
   public ExpedienteContratoDTO buscarContratoInicialSustitucion(ExpedienteDTO dto) throws ServiceException  {
       
       ExpedienteContratoDTO result = null;
       ExpedienteContrato expedienteContra = this.getDao().buscarContratoInicialSustitucion(dto.getIdExpediente());
       if(expedienteContra != null) {
           result = ExpedienteContratoToDTO.INSTANCE.apply(expedienteContra);
       }
       
       return result;
   }
   
   @Override
   @Transactional(readOnly = false)
   public void borradoLogico(Integer idExpedienteContrato) throws ServiceException {
       this.deleteLogico(idExpedienteContrato);
   }

   
   @Override
   public List<ExpedienteContratoDocDTO> buscarDocs(int index, int pageSize,
           List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {

       return new ArrayList<>(
               Lists.transform(this.expedienteContratoDocDAO.find(index, pageSize, filtros, orden),
                       ExpedienteContratoDocToDTO.INSTANCE));
   }
}
