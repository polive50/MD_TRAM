package org.madrid.saii.lib.services.adme;

import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.eregws2.domain.EregRelAnexosUnificadoOUT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interfaz del servicio EregService.
 *
 * @author Everis
 */
public interface EregAdmeService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTANTES
    // ////////////////////////////////////////////////////////
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public final class ConstantesEreg {
        /**
         *
         */
        public static final String ENTRADA_EREG_INTERNET = "I";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ConstantesEregTipoAnexos {
        /**
         *
         */
        public static final String LISTADO_DOC_ANEXOS = "Listado documentos anexos";

    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public final class ConstantesEregEstado {

        /**
         *
         */
        public static final String ESTADO_DOCUMENTO_EREG = "3";

        /**
         *
         */
        public static final String ESTADO_SIN_ERRORES = "0";

    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final class ConstantesEregTipoDoc {

        /**
         *
         */
        public static final String PDF_SOLICITUD_FIRMADO = "T1";

        /**
         *
         */
        public static final String PDF_SOLICITUD_EREG_FIRMADO = "T2";

        /**
         *
         */
        public static final String FDF_SOLICITUD = "T3";

        /**
         *
         */
        public static final String XFDF_SOLICITUD = "T4";

        /**
         *
         */
        public static final String XFDF_SOLICITUD_EREG = "T5";

    }

    /**
     * Firmar datos.
     *
     * @param firmado the firmado
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    byte[] firmarDatos(byte[] firmado) throws ServiceException;

    /**
     * Obtener pdf plantilla.
     *
     * @param plantilla the plantilla
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    byte[] obtenerPdfPlantilla(String plantilla) throws ServiceException;

    /**
     * Generar entrada.
     *
     * @param dto the DTO
     * @param cdasunto the cdasunto
     * @param cdunidad the cdunidad
     * @return ExpedienteDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDTO generarEntrada(ExpedienteDTO dto, String cdasunto, String cdunidad)
            throws ServiceException;

    /**
     * Anexar ficheros docum.
     *
     * @param cdEreg the cdEreg
     * @param fichero documento a subir
     * @param tipo the tipo
     * @param cdModuloTecnico the cdModuloTecnico
     * @return Código del anexo
     * @throws ServiceException the service exception
     */
    String anexarFicherosDocum(String cdEreg, byte[] fichero, String tipo, String cdModuloTecnico)
            throws ServiceException;

    /**
     * Anexar fichero anexo.
     *
     * @param String cdEreg
     * @param fichero the fichero
     * @param nombre del anexo
     * @return cdEreg
     * @throws ServiceException the service exception
     */
    String anexarFicheroAnexo(String cdEreg, byte[] fichero, String nombreAnexo)
            throws ServiceException;

    /**
     * Descargar Anexo por cdAnexo.
     *
     * @param cdEreg the cd ereg
     * @param cdAnexo the cdAnexo
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    byte[] descargarAnexoPorCodigo(String cdEreg, String cdAnexo) throws ServiceException;

    /**
     * Descargar Anexo por Tipo de Anexo.
     *
     * @param cdEreg the cd ereg
     * @param strTipoAnexo tipo de fichero Anexo
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    byte[] descargarAnexoPorTipo(String cdEreg, String strTipoAnexo) throws ServiceException;

    /**
     * Obtiene la relación de anexos unificada de EREG
     *
     * @param cdEreg the cd ereg
     * @return the EregRelAnexosUnificadoOUT
     * @throws ServiceException the service exception
     */
    EregRelAnexosUnificadoOUT obtenerRelAnexosEreg(String cdEreg) throws ServiceException;


    ExpedienteJusTecDTO generarEntradaJCT(ExpedienteDTO dto, String cdasunto, String cdunidad,
            ExpedienteJusTecDTO expedienteJustec, Boolean subsanar) throws ServiceException;

    ExpedienteDTO generarEntradaContrato(ExpedienteDTO dto, String cdAsunto, String cdUnidad) throws ServiceException;

    ExpedienteDTO generarEntradaPresupPlazosOtros(ExpedienteDTO dto, String cdasunto,
            String cdunidad, Boolean subsanar) throws ServiceException;

    ExpedienteDTO generarEntradaSubsanarReq(ExpedienteDTO dto, String cdasunto, String cdunidad,
            Boolean subsanar) throws ServiceException;
    
    /**
     * Generar entrada de subsanación administrativa
     * 
     * @param dto
     * @param cdAsunto
     * @param cdUnidad
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO generarEntradaSubsanarAdm(ExpedienteDTO dto, String cdAsunto, String cdUnidad,
            Boolean subsanar) throws ServiceException;
    
    ExpedienteDTO generarEntradaAlegaciones(ExpedienteDTO dto, String cdAsunto, String cdUnidad
            ) throws ServiceException;

    /**
     * Genera una nueva entrada en EREG para el trámite de Notificación del
     * ciudadano. El código devuelto se guarda en
     * {@link ExpedienteDTO#getCdEregNotifiCiud()} y debe persistirse en
     * SAII_EXPEDIENTE_NOTIF.CD_EREG, no en el expediente.
     *
     * @param dto
     * @param cdAsunto
     * @param cdUnidad
     * @return
     * @throws ServiceException
     */
    ExpedienteDTO generarEntradaNotificacionCiudadano(ExpedienteDTO dto, String cdAsunto,
            String cdUnidad) throws ServiceException;

}
