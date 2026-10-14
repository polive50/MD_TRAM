package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dao.ConvocatoriaCoorEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.ConvocatoriaCoorEvalDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCoorEvalToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCoorEvalToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaCoorEval
 *
 * @author nttdata
 */
@Service("convocatoriaCoorEvalService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaCoorEvalServiceImpl
        extends BaseServiceImpl<ConvocatoriaCoorEval, Integer, ConvocatoriaCoorEvalDAO>
        implements ConvocatoriaCoorEvalService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaCoorEvalDAO
     */
    @Autowired
    public ConvocatoriaCoorEvalServiceImpl(
            @Qualifier("convocatoriaCoorEvalDAO") ConvocatoriaCoorEvalDAO convocatoriaCoorEvalDAO) {
        super();
        this.setDao(convocatoriaCoorEvalDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaCoorEvalServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaCoorEvalDTO> obtener() throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.getDao().findAll(), ConvocatoriaCoorEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ConvocatoriaCoorEvalDTO buscarId(Integer idConvocatoriaCoorEval)
            throws ServiceException {
        return ConvocatoriaCoorEvalToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCoorEval));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaCoorEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaCoorEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaCoorEvalDTO guardar(ConvocatoriaCoorEvalDTO convocatoriaCoorEvalDTO)
            throws ServiceException {
        return ConvocatoriaCoorEvalToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaCoorEvalToEntity.INSTANCE.apply(convocatoriaCoorEvalDTO)));
    }

    /**
     *
     */
    @Override
    public void altaLogica(final ConvocatoriaCoorEvalDTO convocatoriaCoorEvalDTO)
            throws ServiceException {
        convocatoriaCoorEvalDTO.setCdUsuBaja(null);
        convocatoriaCoorEvalDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaCoorEvalToEntity.INSTANCE.apply(convocatoriaCoorEvalDTO));
    }

    /**
     *
     */
    @Override
    public void borradoLogico(final ConvocatoriaCoorEvalDTO convocatoriaCoorEvalDTO)
            throws ServiceException {
        convocatoriaCoorEvalDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaCoorEvalDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaCoorEvalToEntity.INSTANCE.apply(convocatoriaCoorEvalDTO));
    }
    
    @Override
    public ConvocatoriaCoorEvalDTO buscarByIdConvocatoriaCoor(Integer idConvocatoriaCoor) throws ServiceException {

        if (Objects.isNull(idConvocatoriaCoor)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_COOR, idConvocatoriaCoor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaCoorEval> lst = this.find(filtros, null);
        ConvocatoriaCoorEval ent = new ConvocatoriaCoorEval();

        if (!lst.isEmpty()) {
            ent = lst.get(0);
        }

        return ConvocatoriaCoorEvalToDTO.INSTANCE.apply(ent);

    }
    
    @Override
    public  List<ConvocatoriaCoorEvalDTO> buscarByIdConvocatoriaCoorLista(Integer idConvocatoriaCoor) throws ServiceException {

        if (Objects.isNull(idConvocatoriaCoor)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_COOR, idConvocatoriaCoor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaCoorEval> lst = this.find(filtros, null);
        
        List<ConvocatoriaCoorEvalDTO> lstDTO = new ArrayList<>();
        
        for (ConvocatoriaCoorEval convocatoriaCoorEval : lst) {
            lstDTO.add( ConvocatoriaCoorEvalToDTO.INSTANCE.apply(convocatoriaCoorEval));
            
        }
        
        return lstDTO;

    }
    

    @Override
    public Boolean existeAreaPersonaConvocatoriaCoorEval(Integer idConvocatoriaCoor,
            Integer idTipoInforme, Integer idArea, Integer idInteresado) throws ServiceException {
        Boolean resultado = Boolean.FALSE;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUADOR_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_INFORME_ID_VALOR, idTipoInforme,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_AREA_CIENTIFICA, idArea,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_COOR,
                idConvocatoriaCoor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        int coincidencias = this.getDao().count(filtros);

        if (coincidencias > 0) {
            resultado = Boolean.TRUE;
        }
        return resultado;
    }

}
