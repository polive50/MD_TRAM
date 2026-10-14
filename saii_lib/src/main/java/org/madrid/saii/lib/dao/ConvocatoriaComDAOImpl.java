package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCom
 *
 * @author nttdata
 */
@Repository("convocatoriaComDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaComDAOImpl extends BaseDAOImpl<ConvocatoriaCom, Integer>
        implements ConvocatoriaComDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Override
    public List<ConvocatoriaComDTO> buscarAsc(Integer idConvocatoria)  {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" select ");
        queryHql.append(" tramite.idValor, ");
        //VMM 05/06/25 MD-1630: metemos la descripcion del tramite para poder ordenar por ella
        queryHql.append(" tramite.dsValor, ");
        queryHql.append(" itNote, ");
        queryHql.append(" itCorreo, ");
        queryHql.append(" convocatoriaActNote.idActor, ");
        queryHql.append(" fcBajaFila, ");
        queryHql.append(" idConvocatoriaCom ");
        queryHql.append(" from ConvocatoriaCom "); 
        queryHql.append(" where idConvocatoria = :idConvocatoria ");
        queryHql.append(" and fcBajaFila is null ");
        //VMM 18/06/25 MD-1686: cada plantilla o tramite debe salir en concordancia con su check de la convocatoria (lo filtro despues de obtener estos resultados)
        //queryHql.append(" and tramite.idValor <> "+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));
        queryHql.append(" group by tramite.idValor, tramite.dsValor, itNote, itCorreo, convocatoriaActNote.idActor, fcBajaFila, idConvocatoriaCom ");
        queryHql.append(" order by tramite.dsValor asc");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);
        
        Iterator<?> it = executeHQLQueryList.iterator();

        List<ConvocatoriaComDTO> lstConvocatoriaComDTO = new ArrayList<>();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ConvocatoriaComDTO convocatoriaComDTO = new ConvocatoriaComDTO();
            convocatoriaComDTO.getTramiteDTO().setIdValor((Integer) object[0]);
            convocatoriaComDTO.getTramiteDTO().setDsValor((String) object[1]);
            convocatoriaComDTO.setItNote((String) object[2]);
            convocatoriaComDTO.setItCorreo((String) object[3]);
            convocatoriaComDTO.getConvocatoriaActNoteDTO().setIdActor((Integer) object[4]);
            convocatoriaComDTO.setFcBajaFila((Date) object[5]);
            convocatoriaComDTO.setIdConvocatoriaCom((Integer) object[6]);
            
            if (convocatoriaComDTO.getItCorreo().equals("S")) {
                    convocatoriaComDTO.setBlCorreo(true);
                }else {
                    convocatoriaComDTO.setBlCorreo(false);
                }
            
            if (convocatoriaComDTO.getItNote().equals("S")) {
                    convocatoriaComDTO.setBlNote(true);
            }else {
                    convocatoriaComDTO.setBlNote(false);
            }

            lstConvocatoriaComDTO.add(convocatoriaComDTO);
        }
      
        return lstConvocatoriaComDTO;
    }
    
    @Override
    public List<ConvocatoriaComDTO> buscarDesc(Integer idConvocatoria) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" select ");
        queryHql.append(" tramite.idValor, ");
        //VMM 05/06/25 MD-1630: metemos la descripcion del tramite para poder ordenar por ella
        queryHql.append(" tramite.dsValor, ");
        queryHql.append(" itNote, ");
        queryHql.append(" itCorreo, ");
        queryHql.append(" convocatoriaActNote.idActor, ");
        queryHql.append(" fcBajaFila, ");
        queryHql.append(" idConvocatoriaCom ");
        queryHql.append(" from ConvocatoriaCom "); 
        queryHql.append(" where idConvocatoria = :idConvocatoria ");
        queryHql.append(" and fcBajaFila is null ");
        //VMM 18/06/25 MD-1686: cada plantilla o tramite debe salir en concordancia con su check de la convocatoria (lo filtro despues de obtener estos resultados)
        //queryHql.append(" and tramite.idValor <> "+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));
        queryHql.append(" group by tramite.idValor, tramite.dsValor, itNote, itCorreo, convocatoriaActNote.idActor, fcBajaFila, idConvocatoriaCom ");
        queryHql.append(" order by tramite.dsValor desc");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);
        
        Iterator<?> it = executeHQLQueryList.iterator();

        List<ConvocatoriaComDTO> lstConvocatoriaComDTO = new ArrayList<>();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ConvocatoriaComDTO convocatoriaComDTO = new ConvocatoriaComDTO();
            convocatoriaComDTO.getTramiteDTO().setIdValor((Integer) object[0]);
            convocatoriaComDTO.getTramiteDTO().setDsValor((String) object[1]);
            convocatoriaComDTO.setItNote((String) object[2]);
            convocatoriaComDTO.setItCorreo((String) object[3]);
            convocatoriaComDTO.getConvocatoriaActNoteDTO().setIdActor((Integer) object[4]);
            convocatoriaComDTO.setFcBajaFila((Date) object[5]);
            convocatoriaComDTO.setIdConvocatoriaCom((Integer) object[6]);
            
            if (convocatoriaComDTO.getItCorreo().equals("S")) {
                    convocatoriaComDTO.setBlCorreo(true);
                }else {
                    convocatoriaComDTO.setBlCorreo(false);
                }
            
            if (convocatoriaComDTO.getItNote().equals("S")) {
                    convocatoriaComDTO.setBlNote(true);
            }else {
                    convocatoriaComDTO.setBlNote(false);
            }

            lstConvocatoriaComDTO.add(convocatoriaComDTO);
        }
      
        return lstConvocatoriaComDTO;
    }
    
    @Override
    public List<ConvocatoriaComDTO> buscarActCorreo(Integer idConvocatoria, Integer idTramite) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" select");
        queryHql.append(" idConvocatoriaCom, ");
        queryHql.append(" tramite.idValor, ");
        queryHql.append(" convocatoriaActCorreo.idActor ");
        queryHql.append(" from ConvocatoriaCom cc"); 
        queryHql.append(" where cc.idConvocatoria = :idConvocatoria ");
        queryHql.append(" and cc.fcBajaFila is null ");
        //VMM 18/06/25 MD-1686: cada plantilla o tramite debe salir en concordancia con su check de la convocatoria (lo filtro despues de obtener estos resultados)
        //queryHql.append(" and cc.tramite.idValor <> "+ utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));
        queryHql.append(" and cc.tramite.idValor = :idTramite ");



        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("idTramite", idTramite);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);
        
        Iterator<?> it = executeHQLQueryList.iterator();

        List<ConvocatoriaComDTO> lstConvocatoriaComDTO = new ArrayList<>();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            ConvocatoriaComDTO convocatoriaComDTO = new ConvocatoriaComDTO();
            convocatoriaComDTO.setIdConvocatoriaCom((Integer) object[0]);
            convocatoriaComDTO.getTramiteDTO().setIdValor((Integer) object[1]);
            convocatoriaComDTO.getConvocatoriaActCorreoDTO().setIdActor((Integer) object[2]);
            
            lstConvocatoriaComDTO.add(convocatoriaComDTO);
        }
      
        return lstConvocatoriaComDTO;
    }

}
