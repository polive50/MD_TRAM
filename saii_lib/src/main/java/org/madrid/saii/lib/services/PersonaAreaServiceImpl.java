package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.converters.PersonaAreaToDTO;
import org.madrid.saii.lib.dto.converters.PersonaAreaToEntity;
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
 * Implementación del servicio para la clase de dominio PersonaAreaCientifica
 *
 * @author nttdata
 */
@Service("personaAreaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class PersonaAreaServiceImpl extends BaseServiceImpl<PersonaArea, Integer, PersonaAreaDAO>
        implements PersonaAreaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param personaTitDAO
     */
    @Autowired
    public PersonaAreaServiceImpl(@Qualifier("personaAreaDAO") PersonaAreaDAO personaAreaDAO) {
        super();
        this.setDao(personaAreaDAO);
    }

    /**
     *
     */
    @Override
    public List<PersonaAreaDTO> buscarPorPersona(Integer idInteresado) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), PersonaAreaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public PersonaAreaDTO guardar(PersonaAreaDTO interesadoDTO) throws ServiceException {
        return PersonaAreaToDTO.INSTANCE
                .apply(this.insertOrUpdate(PersonaAreaToEntity.INSTANCE.apply(interesadoDTO)));
    }

}
