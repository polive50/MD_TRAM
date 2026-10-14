package org.madrid.saii.lib.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.madrid.saii.lib.dao.CatalogosDAO;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import atlas.core.exceptions.ServiceException;

@Lazy
@Component("utilConstantes")
public class UtilConstantes {
    
//    /** Servicio de CatalogosValoresService. */
//    @Autowired
//    @Qualifier("catalogosValoresService")
//    private CatalogosValoresService catalogosValoresService;
    
//    /** Servicio de ComponenteService. */
//    @Autowired
//    @Qualifier("componenteService")
//    private ComponenteService componenteService;
    
//    /** Servicio de Catalogos */
//    @Autowired
//    @Qualifier("catalogosService")
//    private CatalogosService catalogosService;
    
    
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;
    
    @Autowired
    @Qualifier("componenteDAO")
    private ComponenteDAO componenteDAO;
    
    @Autowired
    @Qualifier("catalogosDAO")
    private CatalogosDAO catalogosDAO;
    
    
    
    private final Map<Integer, Integer> globalCatalogos = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> globalCatalogosValores = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> globalComponente = new ConcurrentHashMap<>();
    
    private final Map<Integer ,CatalogosValoresDTO> globalCatalogosValoresObjeto = new HashMap<>();
    
    public Map<Integer, Integer> getGlobalCatalogos() {
        return globalCatalogos;
    }
    
    public Map<Integer, Integer> getGlobalCatalogosValores() {
        return globalCatalogosValores;
    }
    
    public Map<Integer, CatalogosValoresDTO> getGlobalCatalogosValoresObjeto() {
        return globalCatalogosValoresObjeto;
    }
    
    public Map<Integer, Integer> getGlobalComponente() {
        return globalComponente;
    }
    
    public CatalogosValoresDTO obtenerCatalogoValorPorId(Integer idValor) {
        if (idValor == null) {
            return null;
        }
        return this.globalCatalogosValoresObjeto.get(idValor);
    }
    
    
//    @PostConstruct
//    public void init() throws ServiceException {
//        
//        for(Catalogos cv : catalogosDAO.cargaNmConstante()) {
//            globalCatalogos.put(cv.getNmConstante(), cv.getIdCatalogo());
//        }
//        
//        for(CatalogosValores cv : catalogosValoresDAO.cargaNmConstante()) {
//            globalCatalogosValores.put(cv.getNmConstante(), cv.getIdValor());
//            globalCatalogosValoresObjeto.put(cv.getIdValor(), CatalogosValoresToDTO.INSTANCE.apply(cv));
//        }
//        
//        for(Componente cv : componenteDAO.cargaNmConstante()) {
//            globalComponente.put(cv.getNmConstante(), cv.getIdComponente());
//        }
//        
//        
//    }
    
    @PostConstruct
    public void init() throws ServiceException {
        
        for(Catalogos cv : catalogosDAO.cargaNmConstante()) {
            globalCatalogos.put(cv.getNmConstante(), cv.getIdCatalogo());
        }
        
        for(CatalogosValores cv : catalogosValoresDAO.cargaNmConstante()) {
            globalCatalogosValores.put(cv.getNmConstante(), cv.getIdValor());
            

        }
        
        for(CatalogosValores cv : catalogosValoresDAO.cargaNmConstanteObjeto()) {
            // Mapeo explícito manual saltándonos la auditoría conflictiva
            CatalogosValoresDTO dto = new CatalogosValoresDTO();
            if (cv.getIdValor() != null) {
                dto.setIdValor(cv.getIdValor());
                dto.setCdCodigo(cv.getCdCodigo());
                dto.setDsValor(cv.getDsValor()); 
                dto.setNmConstante(cv.getNmConstante());
                
                // Conversión segura de String a Boolean
                dto.setItMantenible("S".equalsIgnoreCase(cv.getItMantenible()));
                
                // Mapeamos únicamente el identificador para evitar inicializar el proxy
                if (cv.getCatalogo() != null) {
                    CatalogosDTO catPadreDto = new CatalogosDTO();
                    catPadreDto.setIdCatalogo(cv.getCatalogo().getIdCatalogo());
                    dto.setCatalogoDTO(catPadreDto);
                }
            }
            
            globalCatalogosValoresObjeto.put(cv.getIdValor(), dto); 
        }
        
        
        
        
        for(Componente cv : componenteDAO.cargaNmConstante()) {
            globalComponente.put(cv.getNmConstante(), cv.getIdComponente());
        }
    }
    
}
