package org.madrid.saii.lib.services.adme;

import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dto.NoteEnviarNotificacionDTO;
import org.madrid.saii.lib.dto.NoteObtenerNotificacionesDTO;

import atlas.core.exceptions.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interfaz del servicio NOTE Administracion
 *
 * @author everis
 */
public interface NoteAdmeService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTANTES
    // ////////////////////////////////////////////////////////
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public final class ConstantesNote {
        /**
         * Código de aplicacion
         */
        public static final String COD_APP = "SAII";

        /**
         * Cifrado del cuerpo del mensaje: Valores posibles: true/false o 1/0
         */
        public static final String CIFRADA_FALSE = "0";

        /**
         * Código de origen
         */
        public static final String CODIGO_ORIGEN = "011043569";

        /**
         * Código interno de origen
         */
        public static final String CODIGO_ORIGEN_INT = "011043569";


        /**
         * Tipo de servicio
         */
        public static final String SERVICIO = "Ayudas investigación e innovación";

        /**
         * Asunto Notificación presentacion
         */
        public static final String ASUNTO_PRESENTACION = "Notificación de presentación de solicitud de ayudas de investigación e innovación";

        /**
         * Asunto Notificación subsanacion_adm
         */
        public static final String ASUNTO_SUBSANACION_ADM = "Notificación de subsanación administrativa de solicitud de ayudas de investigación e innovación";

        /**
         * Asunto Notificación subsanacion_exc
         */
        public static final String ASUNTO_SUBSANACION_REQ = "Notificación de subsanación de requisitos de solicitud de ayudas de investigación e innovación";

        /**
         * Asunto Notificación exclusion
         */
        public static final String ASUNTO_EXCLUSION = "Notificación de exclusion de solicitud de ayudas de investigación e innovación";

        /**
         * Asunto Notificación desestimiento
         */
        public static final String ASUNTO_DESESTIMIENTO = "Notificación de desestimiento de solicitud de ayudas de investigación e innovación";

        /**
         * Asunto Notificación resolucion provisional
         */
        public static final String ASUNTO_RESOLUCION_PROVISIONAL = "Notificación de propuesta resolución provisional de solicitud de ayudas de investigación e innovación";

        /**
         * Nombre del archivo de notificación
         */
        public static final String NOMBRE_ARCHIVO_NOTIFICACION = "NotificacionPresentacionSolicitud.pdf";

        /**
         * Asunto de notificación de propuesta
         */
        public static final String ASUNTO_NOTIFICION_RES_PRO = "Notificación de propuesta resolución provisional de solicitud de ayudas de investigación e innovación";

    }

    /**
     * Comprueba si el interesado está dado de alta en el servicio de
     * notificaciones telemáticas, si es así, devuelve el cdAbonado
     *
     * @param cdNumIden
     * @return cdAbonado
     * @throws ServiceException
     */
    String recuperarCdAbonado(String cdNumIden) throws ServiceException;

    /**
     * Comprueba si el interesado está dado de alta en el servicio de
     * notificaciones telemáticas, si es así, devuelve el mail
     *
     * @param cdNumIden
     * @return cdAbonado
     * @throws ServiceException
     */
    String recuperarEmailNote(String cdNumIden) throws ServiceException;



    /**
     *
     * @param codigoNote
     * @return
     * @throws ServiceException
     */
    public NoteObtenerNotificacionesDTO obtenerNotificacion(String codigoNote)
            throws ServiceException;

    /**
     * Enviar Notificacion
     * @param input
     * @return
     * @throws ServiceException
     */
    public String enviarNotificacion(NoteEnviarNotificacionDTO inputNotifNoteDTO)
            throws ServiceException;
    
    /**
     * Genera el pdf del acuse de recibo
     * @param nmNotificacion
     * @param nmAbonado
     * @param accion
     * @return
     * @throws ServiceException
     */
    public FicaFichero generarFicaAcuseReciboPDF(String nmNotificacion, String nmAbonado,
            String accion) throws ServiceException;

}
