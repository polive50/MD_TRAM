package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecDTO;
import org.madrid.saii.lib.dto.RevisionInfEvaJustecHistDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ExpedienteEvalService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class RevisionInfEvaJustecServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("revisionInfEvaJustecService")
    private RevisionInfEvaJustecService service;

    /**
     * Servicio histórico
     */
    @Autowired
    @Qualifier("revisionInfEvaJustecHistService")
    private RevisionInfEvaJustecHistService serviceHist;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Session factory de hibernate
     */
    @Resource
    private SessionFactory sessionFactory;

    /**
     * Asocia la sessionFactory de Hibernate a la variable interna
     *
     * @param sessionFactory la sessionFactory
     */
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Inicialización antes de empezar los test
     */
    @Before
    @Override
    public void setUp() {
        assertNotNull(this.service);
    }

    /**
     * buscar
     */
    @Test
    public void buscar() {
        try {
            RevisionInfEvaJustecDTO rev=new RevisionInfEvaJustecDTO();
            rev.setIdRevInfEvaJustec(566666666);
            rev.setExpedienteDTO(new ExpedienteDTO());
            rev.setExpedienteDocDTO(new ExpedienteDocDTO());
            rev.setExpedienteJusTecDTO(new ExpedienteJusTecDTO());
            rev.getIdEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
            
            RevisionInfEvaJustecDTO revisionFinal = this.service.guardar(rev);

            Assert.assertNotNull(revisionFinal);
            Assert.assertNotNull(revisionFinal.getIdRevInfEvaJustec());
            
            List<AtlasFilter> filtros= new ArrayList<AtlasFilter>();
            filtros.add(new AtlasFilter("idRevInfEvaJustec",566666666, AtlasFilter.Operador.EQUALS));

            List<RevisionInfEvaJustecDTO> lista=this.service.buscar(0, 1, filtros, null);
            Assert.assertNotNull(lista);
            Assert.assertTrue(lista.size()>0);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * guardar Revisión
     */
    @Test
    public void guardar() {
        try {
            RevisionInfEvaJustecDTO rev=new RevisionInfEvaJustecDTO();
            rev.setExpedienteDTO(new ExpedienteDTO());
            rev.setExpedienteDocDTO(new ExpedienteDocDTO());
            rev.setExpedienteJusTecDTO(new ExpedienteJusTecDTO());
            rev.getIdEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
            
            RevisionInfEvaJustecDTO revisionFinal = this.service.guardar(rev);

            Assert.assertNotNull(revisionFinal);
            Assert.assertNotNull(revisionFinal.getIdRevInfEvaJustec());
            
            List<AtlasFilter> filtros= new ArrayList<AtlasFilter>();
            filtros.add(new AtlasFilter("revisionInfEvaJustec.idRevInfEvaJustec",revisionFinal.getIdRevInfEvaJustec(), AtlasFilter.Operador.EQUALS));

            List<RevisionInfEvaJustecHistDTO> listaHistoricos=this.serviceHist.buscar(0, 1, filtros, null);
            Assert.assertNotNull(listaHistoricos);
            Assert.assertTrue(listaHistoricos.size()>0);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * Buscar por Id de expediente
     */
    @Test
    public void buscarPorExpediente() {
        try {
            RevisionInfEvaJustecDTO rev=new RevisionInfEvaJustecDTO();
            rev.setExpedienteDTO(new ExpedienteDTO());
            rev.getExpedienteDTO().setIdExpediente(566666666);;
            rev.setExpedienteDocDTO(new ExpedienteDocDTO());
            rev.setExpedienteJusTecDTO(new ExpedienteJusTecDTO());
            rev.getIdEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
            
            RevisionInfEvaJustecDTO revisionFinal = this.service.guardar(rev);

            Assert.assertNotNull(revisionFinal);
            Assert.assertNotNull(revisionFinal.getIdRevInfEvaJustec());
            
            List<AtlasFilter> filtros= new ArrayList<AtlasFilter>();
            filtros.add(new AtlasFilter("revisionInfEvaJustec.idRevInfEvaJustec", AtlasFilter.Operador.EQUALS));

            List<RevisionInfEvaJustecDTO> listaPorExp=this.service.buscarPorExpediente(new Integer(566666666));
            Assert.assertNotNull(listaPorExp);
            Assert.assertTrue(listaPorExp.size()>0);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
    

}
