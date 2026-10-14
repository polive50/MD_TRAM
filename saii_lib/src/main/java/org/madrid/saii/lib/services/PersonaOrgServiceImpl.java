package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.PersonaOrgDAO;
import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.dto.PersonaOrgDTO;
import org.madrid.saii.lib.dto.converters.PersonaOrgToDTO;
import org.madrid.saii.lib.dto.converters.PersonaOrgToEntity;
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
 * Implementación del servicio para la clase de dominio PersonaOrg
 *
 * @author nttdata
 */
@Service("personaOrgService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class PersonaOrgServiceImpl extends BaseServiceImpl<PersonaOrg, Integer, PersonaOrgDAO>
        implements PersonaOrgService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param personaOrgDAO
     */
    @Autowired
    public PersonaOrgServiceImpl(@Qualifier("personaOrgDAO") PersonaOrgDAO personaOrgDAO) {
        super();
        this.setDao(personaOrgDAO);
    }

    /**
     *
     */
    @Override
    public List<PersonaOrgDTO> buscarPorPersona(Integer idInteresado) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), PersonaOrgToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public PersonaOrgDTO guardar(PersonaOrgDTO personaOrgDTO) throws ServiceException {
        return PersonaOrgToDTO.INSTANCE
                .apply(this.insertOrUpdate(PersonaOrgToEntity.INSTANCE.apply(personaOrgDTO)));
    }

}
