package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.PersonaAreaToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio PersonaAreaCientifica
 *
 * @author nttdata
 */
@Repository("personaAreaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PersonaAreaDAOImpl extends BaseDAOImpl<PersonaArea, Integer>
        implements PersonaAreaDAO {
    
    @Override
    public List<PersonaAreaDTO> listObtenerAreas(Integer idInteresado) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" Select per ");
        queryHql.append(" from PersonaArea per");
        queryHql.append(" where ");
        queryHql.append(" per.idInteresado = :idInteresado");
        queryHql.append(" and per.fcBajaFila IS NULL");

        Map<String, Object> params = new HashMap<>();
        params.put("idInteresado", idInteresado);
        
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<PersonaArea> executeHQLQueryList = (List<PersonaArea>) super.executeHQLQueryList(
                queryHql.toString(), params);
        
        Iterator<?> it = executeHQLQueryList.iterator();

        List<PersonaAreaDTO> lstPersonaAreaDTO = new ArrayList<>();
        while (it.hasNext()) {
           
            PersonaArea personaArea =  (PersonaArea) it.next();
            lstPersonaAreaDTO.add(PersonaAreaToDTO.INSTANCE.apply(personaArea));
       
        }
            
           return lstPersonaAreaDTO;
       }


}
