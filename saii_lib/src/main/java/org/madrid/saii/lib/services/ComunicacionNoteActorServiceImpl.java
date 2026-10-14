package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ComunicacionNoteActorDAO;
import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;
import org.madrid.saii.lib.dto.converters.ComunicacionNoteActorToDTO;
import org.madrid.saii.lib.dto.converters.ComunicacionNoteActorToEntity;
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
 * @author quental
 */
@Slf4j
@Service("comunicacionNoteActorService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ComunicacionNoteActorServiceImpl
        extends BaseServiceImpl<ComunicacionNoteActor, Integer, ComunicacionNoteActorDAO>
        implements ComunicacionNoteActorService {


    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param comunicacionNoteActorDAO
     */
    @Autowired
    public ComunicacionNoteActorServiceImpl(@Qualifier("comunicacionNoteActorDAO")
    ComunicacionNoteActorDAO comunicacionNoteActorDAO) {
        super();
        this.setDao(comunicacionNoteActorDAO);
    }

    /**
     * Constructor Vacio
     */
    public ComunicacionNoteActorServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public ComunicacionNoteActorDTO buscarId(Integer idConvocatoriaCom) throws ServiceException {
        return ComunicacionNoteActorToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCom));
    }

    /**
     *
     */
    @Override
    public List<ComunicacionNoteActorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ComunicacionNoteActorToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ComunicacionNoteActorDTO guardar(ComunicacionNoteActorDTO convocatoriaComDTO)
            throws ServiceException {
        return ComunicacionNoteActorToDTO.INSTANCE.apply(this.insertOrUpdate(ComunicacionNoteActorToEntity.INSTANCE.apply(convocatoriaComDTO)));
    }

    /**
    *
    */
    @Override
    public List<ComunicacionNoteActorDTO> buscarComunicacionNoteActorPorIdConvocatoriaCom(Integer idConvocatoriaCom) throws ServiceException{
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_COM, idConvocatoriaCom, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ComunicacionNoteActorToDTO.INSTANCE));
    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void borradoLogico(final ComunicacionNoteActorDTO comunicacionNoteActorDTO) throws ServiceException {
       comunicacionNoteActorDTO.setCdUsuBaja(UserUtils.getUserName());
       comunicacionNoteActorDTO.setFcBajaFila(new Date());
       this.getDao().update(ComunicacionNoteActorToEntity.INSTANCE.apply(comunicacionNoteActorDTO));
   }
   

}
