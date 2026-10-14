package org.madrid.saii.tramitacion.init;

import atlas.starter.web.init.AtlasWebContextInit;

/**
 * Inicializa el contexto de Spring en nuestra aplicación web.
 *
 * @author Madrid digital
 * @version Atlas 2.1.0
 * @since  23/07/2021 10:00:53 AM
 */
public class AppInitializerSaiiTramitacion extends AtlasWebContextInit {

    /**
     * Instancia el inicializador del contexto de Spring.
     */
    public AppInitializerSaiiTramitacion() {
        super(ApplicationContextSaiiTramitacion.class);
    }
}
