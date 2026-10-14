package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.ConvocatoriaGestorDAO;
import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaGestorToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaGestorToEntity;
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

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaGestor
 *
 * @author nttdata
 */
@Service("convocatoriaGestorService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaGestorServiceImpl
        extends BaseServiceImpl<ConvocatoriaGestor, Integer, ConvocatoriaGestorDAO>
        implements ConvocatoriaGestorService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaGestorDAO
     */
    @Autowired
    public ConvocatoriaGestorServiceImpl(
            @Qualifier("convocatoriaGestorDAO") ConvocatoriaGestorDAO convocatoriaGestorDAO) {
        super();
        this.setDao(convocatoriaGestorDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaGestorServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaGestorDTO guardar(ConvocatoriaGestorDTO convocatoriaGestorDTO)
            throws ServiceException {
        return ConvocatoriaGestorToDTO.INSTANCE.apply(this
                .insertOrUpdate(ConvocatoriaGestorToEntity.INSTANCE.apply(convocatoriaGestorDTO)));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaGestorDTO> obtenerListaConvGestorPorIdConv(Integer idConvocatoria)
            throws ServiceException {

        if (Objects.isNull(idConvocatoria)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaGestorToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaGestorDTO obtenerPorIdConvocatoriaCodigoUsuario(Integer idConvocatoria,
            String cdUsuario) throws ServiceException {

        if (Objects.isNull(idConvocatoria) || Objects.isNull(cdUsuario)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO, cdUsuario, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaGestor> lst = this.find(filtros, null);
        ConvocatoriaGestor ent = new ConvocatoriaGestor();

        if (!lst.isEmpty()) {
            ent = lst.get(0);
        }

        return ConvocatoriaGestorToDTO.INSTANCE.apply(ent);

    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaGestorDTO> buscarPorUsuario(String cdUsuarioGestor)
            throws ServiceException {
        if (Objects.isNull(cdUsuarioGestor)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO, cdUsuarioGestor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaGestorToDTO.INSTANCE));
    }

    /**
     * 
     */
    @Override
    public List<ConvocatoriaGestorDTO> obtenerListaGestoresPorFiltro(int index, int pageSize,
            List<AtlasFilter> filtros, List<AtlasSort> orden) throws ServiceException {

        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        
        if(filtros != null)
            filtrosEntrada = filtros;
        
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaGestorToDTO.INSTANCE));
    }
    
    /**
    *
    */
   @Override
   public ConvocatoriaGestorDTO obtenerPorIdConvocatoria(Integer idConvocatoria) throws ServiceException {

       if (Objects.isNull(idConvocatoria)) {
           throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
       }

       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       List<ConvocatoriaGestor> lst = this.find(filtros, null);
       ConvocatoriaGestor ent = new ConvocatoriaGestor();

       if (!lst.isEmpty()) {
           ent = lst.get(0);
       }

       return ConvocatoriaGestorToDTO.INSTANCE.apply(ent);

   }

    @Override
    public List<Integer> obtenerTiposTramite(Integer idConvocatoria, String login) {
        List<Integer> lstTipoTramite = new ArrayList<>();
        lstTipoTramite = this.getDao().obtenerTiposTramite(idConvocatoria, login);
        return lstTipoTramite;
    }


}
