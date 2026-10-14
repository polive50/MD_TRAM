package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaReqDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaReqToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaReqToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaReq
 *
 * @author nttdata
 */
@Service("convocatoriaReqService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaReqServiceImpl
        extends BaseServiceImpl<ConvocatoriaReq, Integer, ConvocatoriaReqDAO>
        implements ConvocatoriaReqService {

    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaReqDAO
     */
    @Autowired
    public ConvocatoriaReqServiceImpl(
            @Qualifier("convocatoriaReqDAO") ConvocatoriaReqDAO convocatoriaReqDAO) {
        super();
        this.setDao(convocatoriaReqDAO);
    }

    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /**
     *
     */
    @Override
    public ConvocatoriaReqDTO buscarId(Integer idConvocatoriaReq) throws ServiceException {
        return ConvocatoriaReqToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaReq));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaReqDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaReqToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaReqDTO guardar(ConvocatoriaReqDTO convocatoriaReqDTO)
            throws ServiceException {
        return ConvocatoriaReqToDTO.INSTANCE.apply(
                this.insertOrUpdate(ConvocatoriaReqToEntity.INSTANCE.apply(convocatoriaReqDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ConvocatoriaReqDTO convocatoriaReqDTO) throws ServiceException {
        convocatoriaReqDTO.setCdUsuBaja(null);
        convocatoriaReqDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaReqToEntity.INSTANCE.apply(convocatoriaReqDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ConvocatoriaReqDTO convocatoriaReqDTO) throws ServiceException {
        convocatoriaReqDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaReqDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaReqToEntity.INSTANCE.apply(convocatoriaReqDTO));
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO borradoLogicoPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        Convocatoria convocatoria = this.convocatoriaDAO.find(idConvocatoria);
        if (convocatoria != null) {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ConvocatoriaReq> lstConvocatoriaReq = this.find(filtros, null);
            if (lstConvocatoriaReq != null && !lstConvocatoriaReq.isEmpty()) {
                for (ConvocatoriaReq convocatoriaReq : lstConvocatoriaReq) {
                    convocatoriaReq.setCdUsuBaja(UserUtils.getUserName());
                    convocatoriaReq.setFcBajaFila(new Date());
                    this.update(convocatoriaReq);
                }
            }

            convocatoria.setItRequisitos(Constantes.AfirmacionNegacion.N);
            convocatoria = this.convocatoriaDAO.merge(convocatoria);
        }

        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaReqDTO agregar(Integer idConvocatoria, Integer idRequisito)
            throws ServiceException {

        ConvocatoriaReq convReq = new ConvocatoriaReq();

        CatalogosValores requisito = this.catalogosValoresDAO.find(idRequisito);

        if (requisito != null) {
            convReq.setRequisito(requisito);
            convReq.setIdConvocatoria(idConvocatoria);
            convReq.setConvocatoriaSubmod(null);
            convReq.setConvocatoriaTipoDoc(null);
            convReq = this.insert(convReq);
        }

        return ConvocatoriaReqToDTO.INSTANCE.apply(convReq);
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaReqDTO> obtenerRequisitoPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaReqToDTO.INSTANCE));

    }

    /**
    *
    */
    @Override
    public ConvocatoriaReqDTO buscarExistencia(final Integer idConvocatoria,
            final Integer idRequisito) throws ServiceException {
        ConvocatoriaReqDTO convocatoriaReqDTO = new ConvocatoriaReqDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_REQUISITO, idRequisito,
                Operador.EQUALS));

        List<ConvocatoriaReq> lstConvocatoriaReq = new ArrayList<>(this.find(filtros, null));

        if (!lstConvocatoriaReq.isEmpty()) {
            convocatoriaReqDTO = ConvocatoriaReqToDTO.INSTANCE.apply(lstConvocatoriaReq.get(0));
        }
        return convocatoriaReqDTO;
    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public ConvocatoriaReq guardarActor(ConvocatoriaReq convocatoriaReq)
           throws ServiceException {
       return this.insertOrUpdate(convocatoriaReq);
   }

   
   @Override
   public List<ConvocatoriaReqDTO> obtenerRequisitoPorConvocatoriaIdActor(Integer idConvocatoria, Integer idActor)
           throws ServiceException {

       List<AtlasFilter> filtros = new ArrayList<>();

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor,
               Operador.EQUALS));
       
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       return new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaReqToDTO.INSTANCE));

   }
}
