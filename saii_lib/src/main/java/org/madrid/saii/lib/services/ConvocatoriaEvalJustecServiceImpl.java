package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ConvocatoriaEvalJustecDAO;
import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEvalJustec;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.ConvocatoriaEvalJustecDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalJustecToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEvalJustecToEntity;
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

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaEvalJustec
 *
 * @author quental
 */
@Service("convocatoriaEvalJustecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaEvalJustecServiceImpl
        extends BaseServiceImpl<ConvocatoriaEvalJustec, Integer, ConvocatoriaEvalJustecDAO>
        implements ConvocatoriaEvalJustecService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCoorEvalDAO
     */
    @Autowired
    public ConvocatoriaEvalJustecServiceImpl(
            @Qualifier("convocatoriaEvalJustecDAO") ConvocatoriaEvalJustecDAO convocatoriaEvalJustecDAO) {
        super();
        this.setDao(convocatoriaEvalJustecDAO);
    }
    
    @Autowired
    @Qualifier("personaAreaDAO")
    private PersonaAreaDAO personaAreaDAO;

    /**
     * Constructor Vacio
     */
    public ConvocatoriaEvalJustecServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaEvalJustecDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaEvalJustecToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaEvalJustecDTO buscarId(Integer idConvocatoriaEvalJustec)
            throws ServiceException {
        return ConvocatoriaEvalJustecToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaEvalJustec));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaEvalJustecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaEvalJustecToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEvalJustecDTO guardar(ConvocatoriaEvalJustecDTO convocatoriaEvalJustecDTO)
            throws ServiceException {
        return ConvocatoriaEvalJustecToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaEvalJustecToEntity.INSTANCE.apply(convocatoriaEvalJustecDTO)));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final ConvocatoriaEvalJustecDTO convocatoriaEvalJustecDTO)
            throws ServiceException {
        convocatoriaEvalJustecDTO.setCdUsuBaja(null);
        convocatoriaEvalJustecDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaEvalJustecToEntity.INSTANCE.apply(convocatoriaEvalJustecDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final ConvocatoriaEvalJustecDTO convocatoriaEvalJustecDTO)
            throws ServiceException {
        convocatoriaEvalJustecDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaEvalJustecDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaEvalJustecToEntity.INSTANCE.apply(convocatoriaEvalJustecDTO));
    }

   /**
   *
   */
    @Override
    public Boolean buscarByIdEvaluadorExist(Integer idEvaluador, Integer idConvocatoria) throws ServiceException {
      
       Boolean exist = Boolean.FALSE;

       List<AtlasFilter> filtros = new ArrayList<>();
       
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUADOR_IDINTERESADO, idEvaluador,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       List<ConvocatoriaEvalJustec> lstConvocatoriaEvalJustec = new ArrayList<>(this.find(filtros, null));
       
       for (ConvocatoriaEvalJustec convEvalJustec : lstConvocatoriaEvalJustec) {
           
           if (convEvalJustec != null 
                   && convEvalJustec.getEvaluador().getIdInteresado().equals(idEvaluador)
                       && convEvalJustec.getFcBajaFila() == null) {
                           exist = Boolean.TRUE;
           }
           
       }

       return exist;
   } 
    
    
    /**
    *
    */
    public List<ConvocatoriaEvalJustecDTO> buscarEvaluadoresJCT(final int index, final int pageSize,
           final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea, List<ConvocatoriaEvalJustecDTO> listaEvalJustecDTO)
           throws ServiceException {
       
       StringBuilder cadenaAreasCientificas = new StringBuilder();
       PersonaAreaDTO personaAreaDTO = new PersonaAreaDTO();

       if (Boolean.FALSE.equals(listaEvalJustecDTO.isEmpty())) {

           for (int i = 0; i < listaEvalJustecDTO.size(); i++) {
               if (cadenaAreasCientificas != null) {
                   cadenaAreasCientificas = new StringBuilder();
               }
               List<PersonaAreaDTO> areas = this.personaAreaDAO.listObtenerAreas(listaEvalJustecDTO.get(i).getInteresadoDTO().getIdInteresado());
               for (int y = 0; y < areas.size(); y++) {
                   personaAreaDTO.getAreaDTO().setDsValor(areas.get(y).getAreaDTO().getDsValor());
                   if(personaAreaDTO.getAreaDTO().getIdValor() == null) {
                       if (cadenaAreasCientificas.length()> 0) {
                           cadenaAreasCientificas.append(" " + "/" + " ");
                           cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                           listaEvalJustecDTO.get(i).setAreasCientificas(cadenaAreasCientificas.toString());
                       }else {
                           cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                           listaEvalJustecDTO.get(i).setAreasCientificas(cadenaAreasCientificas.toString());
                       }
                   }
               }
           }
         
       }

       return listaEvalJustecDTO;
   }
   
   /**
   *
   */
   @Override
   public List<ConvocatoriaEvalJustecDTO> retornarPaginado(List<ConvocatoriaEvalJustecDTO> listaEvaluadoresJCTFiltrada,
           int index, int pageSize) {

       List<ConvocatoriaEvalJustecDTO> listaEvaluadoresJCTPaginada = new ArrayList<>();

       if (!listaEvaluadoresJCTFiltrada.isEmpty()) {

           if (pageSize != -1) {

               int lastIndex = 0;

               if (listaEvaluadoresJCTFiltrada.size() <= 10) {
                   index = 0;
                   lastIndex = listaEvaluadoresJCTFiltrada.size();
               } else {
                   lastIndex = index + pageSize;
                   lastIndex = lastIndex > listaEvaluadoresJCTFiltrada.size()
                           ? listaEvaluadoresJCTFiltrada.size()
                           : lastIndex;
               }

               for (int i = index; i < lastIndex; i++) {
                   listaEvaluadoresJCTPaginada.add(listaEvaluadoresJCTFiltrada.get(i));
               }
           } else {
               listaEvaluadoresJCTPaginada.addAll(listaEvaluadoresJCTFiltrada);
           }
       }

       return listaEvaluadoresJCTPaginada;

   }
   
   /**
   *
   */
  @Override
  public List<ConvocatoriaEvalJustecDTO> buscarPersona(final int index, final int pageSize,
          final List<AtlasFilter> filtros, final List<AtlasSort> orden, Integer idArea)
          throws ServiceException {
    
      PersonaAreaDTO personaAreaDTO = new PersonaAreaDTO();
      StringBuilder cadenaAreasCientificas = new StringBuilder();
      String propertyOrden = StringUtils.EMPTY;
      Boolean ordenAsc = Boolean.FALSE;
      if (Objects.nonNull(orden) && !orden.isEmpty()) {
          propertyOrden = orden.get(0).getProperty();
          ordenAsc = orden.get(0).isAscendente();
      }

      List<ConvocatoriaEvalJustec> lista;
      if (StringUtils.isNotBlank(propertyOrden) && propertyOrden.equals("areasCientificas")) {
          lista = this.find(index, pageSize, filtros, null);
      } else {
          lista = this.find(index, pageSize, filtros, orden);
      }
      
      List<ConvocatoriaEvalJustecDTO> lstConvocatoriaEvalJustecFinal = new ArrayList<>(
              Lists.transform(lista, ConvocatoriaEvalJustecToDTO.INSTANCE));

      if (Boolean.FALSE.equals(lista.isEmpty())) {

          for(int i=0; i < lista.size(); i++){
              
              cadenaAreasCientificas =  new StringBuilder();

              List<AtlasFilter> filtroAux = new ArrayList<>();
              filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO,
                      lista.get(i).getEvaluador().getIdInteresado(), Operador.EQUALS));
              filtroAux.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

              List<PersonaArea> areas = this.personaAreaDAO.find(filtroAux, null);
              
              for (int z = 0; z < areas.size(); z++) {
                  personaAreaDTO.getAreaDTO().setDsValor(areas.get(z).getArea().getDsValor());
                  if(personaAreaDTO.getAreaDTO().getIdValor() == null) {
                      if (cadenaAreasCientificas.length()> 0) {
                          cadenaAreasCientificas.append(" " + "/" + " ");
                          cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                          lstConvocatoriaEvalJustecFinal.get(i).setAreasCientificas(cadenaAreasCientificas.toString());
                      }else {
                          cadenaAreasCientificas.append(personaAreaDTO.getAreaDTO().getDsValor());
                          lstConvocatoriaEvalJustecFinal.get(i).setAreasCientificas(cadenaAreasCientificas.toString());
                      }
                  }
              }
          }
      }
      return lstConvocatoriaEvalJustecFinal;
  }

}

