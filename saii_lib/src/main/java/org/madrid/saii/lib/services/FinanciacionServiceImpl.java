package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.FinanciacionDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Financiacion;
import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.dto.converters.FinanciacionToDTO;
import org.madrid.saii.lib.dto.converters.FinanciacionToEntity;
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
 * Implementación del servicio para la clase de dominio Financiacion
 *
 * @author nttdata
 */
@Service("financiacionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class FinanciacionServiceImpl extends BaseServiceImpl<Financiacion, Integer, FinanciacionDAO>
        implements FinanciacionService {

    /**
     * Servicio de financiacionService
     */
    @Autowired
    @Qualifier("financiacionDatoService")
    FinanciacionDatoService financiacionDatoService;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param financiacionDAO
     */
    @Autowired
    public FinanciacionServiceImpl(@Qualifier("financiacionDAO") FinanciacionDAO financiacionDAO) {
        super();
        this.setDao(financiacionDAO);
    }

    /**
     * Constructor Vacio
     */
    public FinanciacionServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<FinanciacionDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), FinanciacionToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public FinanciacionDTO buscarId(Integer idFinanciacion) throws ServiceException {
        return FinanciacionToDTO.INSTANCE.apply(this.getDao().find(idFinanciacion));
    }

    /**
     *
     */
    @Override
    public List<FinanciacionDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                FinanciacionToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public FinanciacionDTO guardar(FinanciacionDTO financiacionDTO) throws ServiceException {
        return FinanciacionToDTO.INSTANCE
                .apply(this.insertOrUpdate(FinanciacionToEntity.INSTANCE.apply(financiacionDTO)));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final FinanciacionDTO financiacionDTO) throws ServiceException {
        financiacionDTO.setCdUsuBaja(null);
        financiacionDTO.setFcBajaFila(null);
        this.getDao().update(FinanciacionToEntity.INSTANCE.apply(financiacionDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final FinanciacionDTO financiacionDTO) throws ServiceException {
        financiacionDTO.setCdUsuBaja(UserUtils.getUserName());
        financiacionDTO.setFcBajaFila(new Date());
        this.getDao().update(FinanciacionToEntity.INSTANCE.apply(financiacionDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public FinanciacionDTO guardarConfiguracionFinanciacion(Integer idConvocatoria, String itTipo,
            List<Integer> listaIdCatalogosSeleccionados, List<Integer> listaAnualidades)
            throws ServiceException {

        Financiacion fin = new Financiacion();
        fin.setIdConvocatoria(idConvocatoria);
        fin.setItTipo(itTipo);

        fin = this.insertOrUpdate(fin);

        for (Integer idPresupuesto : listaIdCatalogosSeleccionados) {

            if (Constantes.Formatos.PARTIDAS_PRESUPUESTARIAS.equals(fin.getItTipo())) {
                for (Integer anualidad : listaAnualidades) {
                    FinanciacionDato financiacionDato = new FinanciacionDato();
                    financiacionDato.setIdFinanciacion(fin.getIdFinanciacion());
                    financiacionDato.setTipo(new CatalogosValores(idPresupuesto));
                    financiacionDato.setNmAnualidad(anualidad);
                    financiacionDato.setItImporteFijo(Constantes.AfirmacionNegacion.N);
                    this.financiacionDatoService.insertOrUpdate(financiacionDato);
                }
            } else {
                FinanciacionDato financiacionDato = new FinanciacionDato();
                financiacionDato.setIdFinanciacion(fin.getIdFinanciacion());
                financiacionDato.setTipo(new CatalogosValores(idPresupuesto));
                financiacionDato.setItImporteFijo(Constantes.AfirmacionNegacion.N);
                this.financiacionDatoService.insertOrUpdate(financiacionDato);
            }

        }

        return FinanciacionToDTO.INSTANCE.apply(fin);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void eliminarConfiguracionFinanciacion(FinanciacionDTO financiacionDTO)
            throws ServiceException {

        List<AtlasFilter> filtrosFinDato = new ArrayList<AtlasFilter>();
        List<AtlasSort> orden = new ArrayList<AtlasSort>();

        filtrosFinDato.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_FINANCIACION,
                financiacionDTO.getIdFinanciacion(), Operador.EQUALS));
        filtrosFinDato.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<FinanciacionDatoDTO> listaFinanciacionDato = this.financiacionDatoService.buscar(0, -1,
                filtrosFinDato, orden);

        for (FinanciacionDatoDTO financiacionDato : listaFinanciacionDato) {
            this.financiacionDatoService.deleteLogico(financiacionDato.getIdFinanciacionDato());
        }

        this.getDao().deleteLogico(financiacionDTO.getIdFinanciacion());

    }

    /*
     *
     */
    @Override
    public List<FinanciacionDTO> buscaFinanciacionrPorConvocatoriaTipo(Integer idConvocatoria,
            String itTipo) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FINANCIACION_IT_TIPO, itTipo,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), FinanciacionToDTO.INSTANCE));
    }
    
    /*
    *
    */
   @Override
   public List<FinanciacionDTO> buscaFinanciacionByIdConvocatoria(Integer idConvocatoria) throws ServiceException {

       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       return new ArrayList<>(
               Lists.transform(this.find(filtros, null), FinanciacionToDTO.INSTANCE));
   }

   /**
    * Método auxiliar para comprobar si se ha introducido el valor de la tematica en cada elemento de la lista
    */
   public Boolean compruebaTematicaEnListado(List<FinanciacionDTO> listFinanciacion) {
       
       if (listFinanciacion!= null && !listFinanciacion.isEmpty()) {
          for (FinanciacionDTO finan: listFinanciacion) {
              if (finan == null || finan.getTematicaDTO() == null || finan.getTematicaDTO().getIdValor() == null) {
                  return Boolean.TRUE;
              }
          }
       }
       
       return Boolean.FALSE;
   }
   
   /**
    * Método auxiliar para comprobar si se ha introducido el actor en cada elemento de la lista
    */
   public Boolean compruebaActorEnListado(List<FinanciacionDTO> listFinanciacion) {
       
       if (listFinanciacion!= null && !listFinanciacion.isEmpty()) {
          for (FinanciacionDTO finan: listFinanciacion) {
              if (finan == null || finan.getIdActor() == null) {
                  return Boolean.TRUE;
              }
          }
       }
       
       return Boolean.FALSE;
   }
   
   /**
    * 
    * @param idConvocatoria
    * @param idActor
    * @return
    * @throws ServiceException
    */
   @Override
   public List<FinanciacionDTO> buscarPorIdConvocatoriaIdActor(Integer idConvocatoria, Integer idActor)
           throws ServiceException {
       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ACTOR, idActor, Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       return new ArrayList<>(Lists.transform(this.find(filtros, null), FinanciacionToDTO.INSTANCE));
   }

   /**
    * Método para eliimnar el actor del cuadro de financiación
    * 
    * @param idActor
    * @throws ServiceException
    */
   @Override
   @Transactional(readOnly = false)
    public void eliminarActorCuadroFinanciacion(FinanciacionDTO financiacionDTO)
            throws ServiceException {
        
        financiacionDTO.setIdActor(null);
        this.getDao().update(FinanciacionToEntity.INSTANCE.apply(financiacionDTO));
        
    }

    @Override
    @Transactional(readOnly = false)
    public FinanciacionDTO guardarFinanciacion(Integer idConvocatoria,
            String tipoPartidasPresupuestaria) throws ServiceException {
        Financiacion fin = new Financiacion();
        fin.setIdConvocatoria(idConvocatoria);
        fin.setItTipo(tipoPartidasPresupuestaria);
        fin.setItMostrarAyuda(Constantes.AfirmacionNegacion.N);
        fin = this.insertOrUpdate(fin);
        return FinanciacionToDTO.INSTANCE.apply(fin);
    }
    
    
    @Override
    public List<FinanciacionDTO> buscarTablasFinanByIdConvocAndTipo(Integer idConvocatoria, String tipo) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_TIPO,
                tipo, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.buscar(0, -1, filtros, null);
    }
}
