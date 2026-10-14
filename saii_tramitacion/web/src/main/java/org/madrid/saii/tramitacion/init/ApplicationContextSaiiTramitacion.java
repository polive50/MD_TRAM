package org.madrid.saii.tramitacion.init;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import atlas.componentes.menu.provider.CacheMenuProvider;
import atlas.componentes.menu.provider.MenuProvider;
import atlas.starter.crypt.AtlasHabilitaConfiguracionCrypt;
import atlas.starter.eadm.AtlasHabilitaConfiguracionEadm;
import atlas.starter.eadm.context.AtlasHabilitaConfiguracionICDA;
import atlas.starter.enviocorreo.conf.AtlasHabilitaConfiguracionEnvioCorreo;
import atlas.starter.fica.AtlasHabilitaConfiguracionFica;

/**
 * Clase ApplicationContext que carga el contexto de Spring.
 *
 * @author Madrid digital
 * @version Atlas 2.1.0
 * @since 23/07/2021 10:00:53 AM
 */
@Configuration
@ComponentScan({"org.madrid.saii.tramitacion", "org.madrid.saii.lib"})
@AtlasHabilitaConfiguracionFica
@AtlasHabilitaConfiguracionEadm
@AtlasHabilitaConfiguracionCrypt
@AtlasHabilitaConfiguracionEnvioCorreo
@AtlasHabilitaConfiguracionICDA // <-- Las anotaciones de Atlas ya se encargan de registrar sus propios beans
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ApplicationContextSaiiTramitacion {

//    @Value("${icda.ws.endpoint}")
//    private String icdaWsEndpoint;
//
//    @Value("${icda.cm.ws.endpoint}")
//    private String icdaCmWsEndpoint;
//
//    @Value("${icda.tgss.ws.endpoint}")
//    private String icdaTgssWsEndpoint;

    /**
     * Configuración del proveedor de Menú para la aplicación
     *
     * @param xmlMenuProvider
     * @return {@link MenuProvider}
     */
    @Bean
    public MenuProvider cacheMenuProvider(
            @Qualifier("atlasXmlMenuProvider") MenuProvider xmlMenuProvider) {

        CacheMenuProvider result = new CacheMenuProvider(xmlMenuProvider);
        result.setCachearPorRoles(true);
        result.setCachearPorUsuario(false);
        result.setTiempoCache(3600);

        return result;
    }

    // --- CLIENTES ICDA CON PROXY CGLIB PUNTUAL ---
//
//    @Bean
//    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ClienteIcdaWSService clienteIcdaWSService() {
//        return new ClienteIcdaWSService(icdaWsEndpoint);
//    }
//
//    @Bean
//    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ClienteIcdaCmWSService clienteIcdaCmWSService() {
//        return new ClienteIcdaCmWSService(icdaCmWsEndpoint);
//    }
//
//    @Bean
//    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ClienteIcdaTgssWSService clienteIcdaTgssWSService() {
//        return new ClienteIcdaTgssWSService(icdaTgssWsEndpoint);
//    }
}