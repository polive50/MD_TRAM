package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ComunicacionCorreoActorDAO;
import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.madrid.saii.lib.dto.converters.ComunicacionCorreoActorToDTO;
import org.madrid.saii.lib.dto.converters.ComunicacionCorreoActorToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaCom
 *
 * @author nttdata
 */
@Slf4j
@Service("comunicacionCorreoActorService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ComunicacionCorreoActorServiceImpl
        extends BaseServiceImpl<ComunicacionCorreoActor, Integer, ComunicacionCorreoActorDAO>
        implements ComunicacionCorreoActorService {


    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param comunicacionCorreoActorDAO
     */
    @Autowired
    public ComunicacionCorreoActorServiceImpl(@Qualifier("comunicacionCorreoActorDAO")
    ComunicacionCorreoActorDAO comunicacionCorreoActorDAO) {
        super();
        this.setDao(comunicacionCorreoActorDAO);
    }

    /**
     * Constructor Vacio
     */
    public ComunicacionCorreoActorServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public ComunicacionCorreoActorDTO buscarId(Integer idConvocatoriaCom) throws ServiceException {
        return ComunicacionCorreoActorToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCom));
    }

    /**
     *
     */
    @Override
    public List<ComunicacionCorreoActorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ComunicacionCorreoActorToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ComunicacionCorreoActorDTO guardar(ComunicacionCorreoActorDTO convocatoriaComDTO)
            throws ServiceException {
        return ComunicacionCorreoActorToDTO.INSTANCE.apply(this.insertOrUpdate(ComunicacionCorreoActorToEntity.INSTANCE.apply(convocatoriaComDTO)));
    }

    /**
    *
    */
    @Override
    public List<ComunicacionCorreoActorDTO> buscarComunicacionCorreoActorPorIdConvocatoriaCom(Integer idConvocatoriaCom) throws ServiceException{
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_COM, idConvocatoriaCom, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ComunicacionCorreoActorToDTO.INSTANCE));
    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void borradoLogico(final ComunicacionCorreoActorDTO comunicacionCorreoActorDTO) throws ServiceException {
       comunicacionCorreoActorDTO.setCdUsuBaja(UserUtils.getUserName());
       comunicacionCorreoActorDTO.setFcBajaFila(new Date());
       this.getDao().update(ComunicacionCorreoActorToEntity.INSTANCE.apply(comunicacionCorreoActorDTO));
   }
   

}
