package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.PersonaEspecDAO;
import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.madrid.saii.lib.dto.converters.PersonaEspecToDTO;
import org.madrid.saii.lib.dto.converters.PersonaEspecToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio PersonaEspecialidad
 *
 * @author nttdata
 */
@Service("personaEspecService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class PersonaEspecServiceImpl extends BaseServiceImpl<PersonaEspec, Integer, PersonaEspecDAO>
        implements PersonaEspecService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param personaTitDAO
     */
    @Autowired
    public PersonaEspecServiceImpl(@Qualifier("personaEspecDAO") PersonaEspecDAO personaEspecDAO) {
        super();
        this.setDao(personaEspecDAO);
    }

    /**
     *
     */
    @Override
    public List<PersonaEspecDTO> buscarPorPersona(Integer idInteresado) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), PersonaEspecToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public PersonaEspecDTO guardar(PersonaEspecDTO personaEspecDTO) throws ServiceException {
        return PersonaEspecToDTO.INSTANCE
                .apply(this.insertOrUpdate(PersonaEspecToEntity.INSTANCE.apply(personaEspecDTO)));
    }

}
