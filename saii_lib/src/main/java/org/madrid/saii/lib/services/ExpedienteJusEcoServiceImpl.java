package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoDocToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToEntity;
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

/**
 * @author quental
 */
@Service("expedienteJusEcoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusEcoServiceImpl
        extends BaseServiceImpl<ExpedienteJusEco, Integer, ExpedienteJusEcoDAO>
        implements ExpedienteJusEcoService {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteJusEcoDAO
     */
    @Autowired
    public ExpedienteJusEcoServiceImpl(
            @Qualifier("expedienteJusEcoDAO") ExpedienteJusEcoDAO expedienteJusEcoDAO) {
        super();
        this.setDao(expedienteJusEcoDAO);
    }

    @Autowired
    @Qualifier("expedienteJusEcoHistDAO")
    private ExpedienteJusEcoHistDAO expedienteJusEcoHistDAO;

    @Autowired
    @Qualifier("expedienteJusEcoDocDAO")
    private ExpedienteJusEcoDocDAO expedienteJusEcoDocDAO;

    /**
     *
     */
    @Override
    public List<ExpedienteJusEcoDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteJusEcoToDTO.INSTANCE));
    }

    /**
     * Método para insertar o actualizar los datos editados de los registros
     * ExpedienteJusEco, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteJusEcoDTO guardar(ExpedienteJusEcoDTO expedienteJusEcoDTO)
            throws ServiceException {
        
        ExpedienteJusEco expedienteJusEco = this
                .insertOrUpdate(ExpedienteJusEcoToEntity.INSTANCE.apply(expedienteJusEcoDTO));

        this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

        return ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEco);
    }

    /**
    *   Método para buscar ExpedienteJusEcoDTO por el id de expediente
    *   
    *   @param idExpediente id del expediente
    */
    @Override
    public List<ExpedienteJusEcoDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusEcoToDTO.INSTANCE));
    }

    /**
    *
    */
   @Override
   public List<ExpedienteJusEcoDocDTO> buscarDocs(int index, int pageSize, List<AtlasFilter> filtros,
           List<AtlasSort> orden) throws ServiceException {
       
       List<ExpedienteJusEcoDoc> lstExpJusEcoDocs = this.expedienteJusEcoDocDAO.find(index, pageSize, filtros, orden);
       
       List<ExpedienteJusEcoDocDTO> lstExpJusEcoDocsDTO = new ArrayList<>(Lists.transform(lstExpJusEcoDocs,ExpedienteJusEcoDocToDTO.INSTANCE));
       
       return lstExpJusEcoDocsDTO;
   }
   
   /**
   *
   */
    @Override
    @Transactional(readOnly = false)
    public void guardarDocumentoJusEco(ExpedienteJusEcoDocDTO expedienteJusEcoDocDTO) {
        this.expedienteJusEcoDocDAO.insertOrUpdate(ExpedienteJusEcoDocToEntity.INSTANCE.apply(expedienteJusEcoDocDTO));
    }
    
    
    /**
     *   Método para buscar ExpedienteJusEcoDTO por el id de expediente
     *   
     *   @param idExpediente id del expediente
     */
     @Override
     public List<ExpedienteJusEcoDTO> buscarPeriodosPendientesJusecoPorExpediente(Integer idExpediente, Integer idEstado)
             throws ServiceException {
         Date currentDate = new Date();
         List<AtlasFilter> filtros = new ArrayList<>();
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_INICIO_JE,currentDate, Operador.LESS_OR_EQUAL));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_JE,currentDate, Operador.GREATER_OR_EQUAL));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, idEstado, Operador.EQUALS));

         List<AtlasSort> ordenamiento = new ArrayList<>();
         ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));
         List<ExpedienteJusEco> result = this.find(0, -1, filtros, ordenamiento);
         return new ArrayList<>(Lists.transform(result,
                 ExpedienteJusEcoToDTO.INSTANCE));
     } 
     
     
     /**
      *   Método para buscar ExpedienteJusEcoDTO por el id de expediente
      *   
      *   @param idExpediente id del expediente
      */
      @Override
      public List<ExpedienteJusEcoDTO> buscarPeriodosPendientesJusecoPorExpediente(Integer idExpediente)
              throws ServiceException {
          Date currentDate = new Date();
          List<AtlasFilter> filtros = new ArrayList<>();
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_INICIO_JE,currentDate, Operador.LESS_OR_EQUAL));
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_JE,currentDate, Operador.GREATER_OR_EQUAL));
          filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.INICIO_PLAZO_DE_PRESENTACION), Operador.EQUALS));

          List<AtlasSort> ordenamiento = new ArrayList<>();
          ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));
          List<ExpedienteJusEco> result = this.find(0, -1, filtros, ordenamiento);
          return new ArrayList<>(Lists.transform(result,
                  ExpedienteJusEcoToDTO.INSTANCE));
      } 
     
     /**
      * Método para buscar ExpedienteJusTecDTO por el id de expediente
      * 
      * @param idExpediente id del expediente
      */
     @Override
     public List<ExpedienteJusEcoDTO> buscarPeriodosFinalizadoPlazoJusEcoPorExpediente(
             Integer idExpediente, Integer idEstado) throws ServiceException {
         Date currentDate = new Date();
         List<AtlasFilter> filtros = new ArrayList<>();

         if (idExpediente != null) {
             filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                     Operador.EQUALS));
         }
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_JE, currentDate,
                 Operador.LESS_THAN));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, idEstado, Operador.EQUALS));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

         List<AtlasSort> ordenamiento = new ArrayList<>();
         ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

         List<ExpedienteJusEco> result = this.find(0, -1, filtros, ordenamiento);
         return new ArrayList<>(Lists.transform(result, ExpedienteJusEcoToDTO.INSTANCE));
     }
     
     
     @Override
     public ExpedienteJusEcoDTO buscarPorIdExpedienteJuseco(Integer idExpedienteJuseco)
             throws ServiceException {
 
        return  ExpedienteJusEcoToDTO.INSTANCE.apply(this.find(idExpedienteJuseco));
         
     }
     
     @Override
     @Transactional(readOnly = false)
     public ExpedienteJusEcoDTO update(ExpedienteJusEcoDTO expedienteJusEcoDTO)
             throws ServiceException {
         
         ExpedienteJusEco expedienteJusEco =this.find(expedienteJusEcoDTO.getIdExpedienteJusEco());
         expedienteJusEco.setIdEstado( CatalogosValoresToEntity.INSTANCE.apply(expedienteJusEcoDTO.getIdEstadoDTO()));
         expedienteJusEco.setTlObservacionDelCiudadano(expedienteJusEcoDTO.getTlObservacionDelCiudadano());
         expedienteJusEco = this.insertOrUpdate(expedienteJusEco);

         this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);

         return ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEco);
     }
     
     @Override
     public List<ExpedienteJusEcoDTO> buscarPeriodosPendientesSubsanarJusecoPorExpediente(Integer idExpediente)
             throws ServiceException {
         List<AtlasFilter> filtros = new ArrayList<>();
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                 Operador.EQUALS));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO, 
                 utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION) , Operador.EQUALS));

         List<AtlasSort> ordenamiento = new ArrayList<>();
         ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));
         List<ExpedienteJusEco> result = this.find(0, -1, filtros, ordenamiento);
         return new ArrayList<>(Lists.transform(result,
                 ExpedienteJusEcoToDTO.INSTANCE));
     }  
     
     @Override
     @Transactional(readOnly = false)
     public void borradoLogico(Integer idExpedienteJuseco) throws ServiceException {
         this.deleteLogico(idExpedienteJuseco);
     }
     
     /**
      * Actualiza desde el batch el estado de los expdtes justec segun sus
      * estados previos y fechas de JCT
      *
      * @return
      * @throws ServiceException
      */
     @Override
     @Transactional(readOnly = false)
     public void procesarPlazosJUSECO() throws ServiceException {

         // iniciarPlazoJCT
         List<ExpedienteJusEcoDTO> lstExpdteJusEcoDTO = this
                 .buscarPeriodosPendientesJusecoPorExpediente(null,
                         utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_PRESENTACION));

         if (lstExpdteJusEcoDTO != null && !lstExpdteJusEcoDTO.isEmpty()) {

             for (ExpedienteJusEcoDTO expedienteJusEcoDTOAux : lstExpdteJusEcoDTO) {
                 // actualizamos el estado de los expdtes justec pdtes de
                 // presentacion a inicio plazo presentacion
                 expedienteJusEcoDTOAux.setIdEstadoDTO(
                         new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.INICIO_PLAZO_DE_PRESENTACION)));

                 ExpedienteJusEco expedienteJusEco = ExpedienteJusEcoToEntity.INSTANCE
                         .apply(expedienteJusEcoDTOAux);
                 expedienteJusEco.setFcModifFila(new Date());
                 expedienteJusEco.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                 ExpedienteJusEco mergedExpedienteJusEco = this.getDao().merge(expedienteJusEco);
                 this.expedienteJusEcoHistDAO.crearHistorico(mergedExpedienteJusEco);
             }
         }

         // finalizarPlazoJCT
         List<ExpedienteJusEcoDTO> lstExpdteJusEcoFinDTO = this
                 .buscarPeriodosFinalizadoPlazoJusEcoPorExpediente(null,
                         utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.INICIO_PLAZO_DE_PRESENTACION));

         if (lstExpdteJusEcoFinDTO != null && !lstExpdteJusEcoFinDTO.isEmpty()) {

             for (ExpedienteJusEcoDTO expedienteJusecoDTOAux : lstExpdteJusEcoFinDTO) {
                 // actualizamos el estado de los expdtes justec en inicio plazo
                 // presentacion a finalizado plazo presentacion
                 expedienteJusecoDTOAux.setIdEstadoDTO(
                         new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.FINALIZADO_PLAZO_PRESENTACION)));

                 ExpedienteJusEco expedienteJusEco = ExpedienteJusEcoToEntity.INSTANCE
                         .apply(expedienteJusecoDTOAux);
                 expedienteJusEco.setFcModifFila(new Date());
                 expedienteJusEco.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                 ExpedienteJusEco mergedExpedienteJusEco = this.getDao().merge(expedienteJusEco);
                 this.expedienteJusEcoHistDAO.crearHistorico(mergedExpedienteJusEco);
             }
         }

         // procesar JCTs no presentadas
         List<ExpedienteJusEcoDTO> lstExpdteJusEcoNPDTO = this
                 .buscarPeriodosFinalizadoPlazoJusEcoPorExpediente(null,
                         utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.RECLAMADA_JUSTIFICACION));

         if (lstExpdteJusEcoNPDTO != null && !lstExpdteJusEcoNPDTO.isEmpty()) {

             for (ExpedienteJusEcoDTO expedienteJusEcoDTOAux : lstExpdteJusEcoNPDTO) {
                 // actualizamos el estado de los expdtes justec en reclamada
                 // justificacion con fecha vencida a no presentada
                 expedienteJusEcoDTOAux.setIdEstadoDTO(
                         new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.NO_PRESENTADA)));

                 ExpedienteJusEco expedienteJusEco = ExpedienteJusEcoToEntity.INSTANCE
                         .apply(expedienteJusEcoDTOAux);
                 expedienteJusEco.setFcModifFila(new Date());
                 expedienteJusEco.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                 ExpedienteJusEco mergedExpedienteJusEco = this.getDao().merge(expedienteJusEco);
                 this.expedienteJusEcoHistDAO.crearHistorico(mergedExpedienteJusEco);
             }
         }
     }

     
}
