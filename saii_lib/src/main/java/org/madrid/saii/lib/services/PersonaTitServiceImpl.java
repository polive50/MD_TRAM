package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.PersonaTitDAO;
import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.PersonaTitDTO;
import org.madrid.saii.lib.dto.converters.PersonaTitToDTO;
import org.madrid.saii.lib.dto.converters.PersonaTitToEntity;
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
 * Implementación del servicio para la clase de dominio PersonaTit
 *
 * @author nttdata
 */
@Service("personaTitService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class PersonaTitServiceImpl extends BaseServiceImpl<PersonaTit, Integer, PersonaTitDAO>
        implements PersonaTitService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param personaTitDAO
     */
    @Autowired
    public PersonaTitServiceImpl(@Qualifier("personaTitDAO") PersonaTitDAO personaTitDAO) {
        super();
        this.setDao(personaTitDAO);
    }

    /**
     *
     */
    @Override
    public List<PersonaTitDTO> buscarPorPersona(Integer idInteresado) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), PersonaTitToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public PersonaTitDTO guardar(PersonaTitDTO personaTitDTO) throws ServiceException {
        return PersonaTitToDTO.INSTANCE
                .apply(this.insertOrUpdate(PersonaTitToEntity.INSTANCE.apply(personaTitDTO)));
    }

}
