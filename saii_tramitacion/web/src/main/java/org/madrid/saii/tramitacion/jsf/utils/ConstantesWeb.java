package org.madrid.saii.tramitacion.jsf.utils;

import atlas.componentes.core.utiles.AtlasMessageUtils;

/**
 * Clase de constantes.
 *
 * @author everis
 *
 */
public final class ConstantesWeb {

    /**
     * Constructor para que no se genere el publico por defecto
     */
    protected ConstantesWeb() {
    }

    /**
     *
     * Constantes para Error.
     */
    public static final class Err {

        /**
         *
         */
        private Err() {
        }

        /** Constante mensaje error general */
        public static final String ERROR_GENERAL = "mensaje.error";

        /** Constante mensaje error listado */
        public static final String ERROR_LISTADO_CATALOGOS = "catalogo.error.traerListaCatalogos";

        /** Constante mensaje error listado */
        public static final String ERROR_TOTAL_CATALOGOS = "catalogo.error.errorListaCatalogos";

        /** Constante mensaje error listado */
        public static final String ERROR_LISTADO = "mensaje.error.listado";

        /** Constante mensaje error guardar */
        public static final String ERROR_GUARDAR = "mensaje.error.guardar";

        /** Constante mensaje error usuario sin cdUnidad */
        public static final String CDUNIDAD_VACIA = "mensaje.error.cdUnidadVacia";

        /** Constante mensaje error usuario con cdUnidad no saii */
        public static final String CDUNIDAD_NO_SAII = "mensaje.error.cdUnidadNoSaii";

        /**
         * Constante mensaje de error al enviar documento resumen a portafirmas
         */
        public static final String ERROR_ENVIAR_PORTAFIRMA = "convocatorias.detalle.solicitudes.expedientes.documentos.error.enviar.documento.portafirmas";

        public static final String ERROR_ENVIAR_DESISTIMIENTO = "convocatorias.detalle.solicitudes.expedientes.documentos.error.enviar.desistimiento";
        
        public static final String ERROR_FALTA_DOC_DESISTIMIENTO = "convocatorias.detalle.solicitudes.expedientes.documentos.error.falta.documento.desistimiento";
        
        public static final String ERROR_FALTA_DOC_EXCLUSION = "convocatorias.detalle.solicitudes.expedientes.documentos.error.falta.documento.exclusion";

        /** Constante mensaje error listado */
        public static final String ERROR_NO_ASIGNADO_CAUSA_DENEGACION = "error.no.asignado.causa.denegacion";

        public static final String ERROR_VALIDACION_CAUSA_DENEGACION_NO_SELECCIONADO = "mensaje.error.validacion.causa.denegacion.no.seleccionado";

        public static final String ERROR_VALIDACION_DESC_CAUSA_DENEGACION_NO_INGRESADO = "mensaje.error.validacion.desc.causa.denegacion.no.ingresado";

        public static final String ENVIO_PORTAFIRMAS_INCORRECTO = "mensaje.envio.portafirmas.incorrecto";
        
        public static final String ENVIO_PORTAFIRMAS_SIN_FIRMANTES = "mensaje.envio.portafirmas.sin.firmantes";
        
        public static final String ENVIO_NOTE_INCORRECTO = "mensaje.envio.note.incorrecto";
        
        public static final String ENVIO_NOTE_ERROR = "mensaje.envio.note.error";

        public static final String ENVIO_DESISTIMIENTO_INCORRECTO = "mensaje.envio.desistimiento.incorrecto";

        public static final String ENVIO_COLA_DUPLICADO = "mensaje.envio.portafirmas.incorrecto";
        
        public static final String ENVIO_COLA_DUPLICADO_EXISTE = "mensaje.envio.portafirmas.incorrecto.existe";
        
        public static final String REVISION_CALIFICACION_REQUISITOS_INCOMPLETO = "mensaje.revision.calificacion.requisitos.incompleto";

        public static final String REVISION_CALIFICACION_DOCUMENTOS_INCOMPLETO = "mensaje.revision.calificacion.documentos.incompleto";
        
        public static final String REVISION_CALIFICACION_REQUISITOS_NO_CUMPLE_SIN_OBS = "mensaje.revision.calificacion.requisitos.nocumple.sinobservacion";

        /** * Error expedientes evaluacion rechazo de informe coordinador */
        public static final String ERROR_RECHAZAR_INFORME_COORDINADOR_KO = "convocatorias.detalle.solicitudes.expedientes.evaluacion.msg.error.ko";

        /** * Error expedientes evaluacion aprobar de informe coordinador */
        public static final String ERROR_APROBAR_INFORME_COORDINADOR_KO = "convocatorias.detalle.solicitudes.expedientes.evaluacion.msg.aprobar.error.ko";

        public static final String ERROR_NOTA_MAXIMA = "error.nota.maxima";

        public static final String ERROR_FECHA_INICIO_CONTRATO_VACIO = "error.fecha.inicio.contrato.vacio";

        public static final String ERROR_FECHA_FIN_CONTRATO_VACIO = "error.fecha.fin.contrato.vacio";

        public static final String ERROR_FECHA_CONTRATO_INICIO_MAYOR_FIN = "error.fecha.contrato.inicio.mayor.fin";

        public static final String ERROR_FECHA_CONTRATO_EXPE_FUERA_RANGO = "error.fecha.contrato.expe.fuera.rango";
        
        public static final String ERROR_FECHA_ANUALIDAD_INICIADA = "error.fecha.anualidad.iniciada";

        // VMM 14/09/26 MD-2725: mensaje especifico para el bloqueo de modificacion de presupuesto
        // cuando quedan 60 dias o menos para el fin de ejecucion de la anualidad
        public static final String ERROR_FECHA_ANUALIDAD_PROXIMA_FIN = "error.fecha.anualidad.proxima.fin";

        // VMM 14/09/26 MD-2725: mismo limite de 60 dias pero para la modificacion de plazos
        // (fecha fin de ejecucion), texto distinto porque aqui no se habla de presupuesto
        public static final String ERROR_FECHA_ANUALIDAD_PROXIMA_FIN_PLAZO = "error.fecha.anualidad.proxima.fin.plazo";

        public static final String ERROR_VALIDAR_OBS_PARA_CIU = "error.validar.obs.para.ciu";
    }

    /**
     *
     * Constantes para Mensajes.
     */
    public static final class Mensajes {

        /**
         *
         */
        private Mensajes() {
        }

        public static final String ERROR = "mensaje.error";

        public static final String GUARDAR_OK = "mensaje.guardar";

        public static final String GUARDAR_KO = "mensaje.error.guardar";

        public static final String BAJA_KO = "mensaje.error.baja";
        
        public static final String NO_EVALUADO_GUARDAR_OK = "no.evaluado.mensaje.guardar.ok";

        /** Constante mensaje error cargar convocatoria */
        public static final String CONVOCATORIAS_MENSAJE_CARGAR_KO = "convocatorias.msg.cargar.KO";

        /** Constante mensaje error seleccionar un tipo de documento */
        public static final String DOCUMENTO_MENSAJE_ERROR_TRAMITE = "convocatoria.documentacion.detalle.label.tramite.tooltipForm";
        
        public static final String DOCUMENTO_MENSAJE_VACIO = "convocatoria.documentacion.detalle.label.vacio.tooltipForm";

        /** Constante mensaje error seleccionar un tipo de documento */
        public static final String DOCUMENTO_MENSAJE_ERROR_TIPO_DOCUMENTO = "convocatoria.documentacion.detalle.label.tipoDocumento.tooltipForm";

        /** Constante mensaje error seleccionar un archivo */
        public static final String DOCUMENTO_MENSAJE_ERROR_ARCHIVO = "convocatoria.documentacion.detalle.label.seleccioneDocumento.tooltipForm";

        /** Constante mensaje error tipo documento */
        public static final String TIPO_DOC_DUPLICADO = "documentos.tipo.doc.duplicado";

        /** Constante mensaje OK subir un documento */
        public static final String DOCUMENTO_MENSAJE_SUBIDA_OK = "convocatorias.detalle.msg.documento.subida.OK";
        
        /** Constante mensaje error subir un documento */
        public static final String DOCUMENTO_MENSAJE_SUBIDA_KO = "convocatorias.detalle.msg.documento.subida.KO";
        
        /** Constante resolución conjunta de exclusión y desistimiento ya subida */
        public static final String RESOLUCION_EXCLUSION_DESISTIMIENTO_EXISTE = "resolucion.exclusion.desistimiento.documento.existe";
        
        /** Constante resolución individual de exclusión ya subida */
        public static final String RESOLUCION_EXCLUSION_EXISTE = "resolucion.exclusion.documento.existe";

        /** Constante resolución individual de desistimiento ya subida */
        public static final String RESOLUCION_DESISTIMIENTO_EXISTE = "resolucion.desistimiento.documento.existe";
        
        /** Constante debe seleccionar fichero a subir **/
        public static final String DEBE_SELECCIONAR_FICHERO = "solapa.resol.error.debe.seleccionar.fichero";
        
        
        public static final String DOCUMENTO_MENSAJE_ERROR_FECHAS_REGISTRO = "convocatoria.documentacion.detalle.label.fecha.entrada.tooltipForm";
        
        public static final String DOCUMENTO_MENSAJE_ERROR_CODIGO_EREG_VACIO = "convocatoria.documentacion.detalle.label.codigoEreg.tooltipForm";
        
        public static final String DOCUMENTO_MENSAJE_ERROR_CODIGO_EREG_FORMATO = "convocatoria.documentacion.detalle.label.codigoEreg.formato.tooltipForm";
        
        public static final String DOCUMENTO_MENSAJE_ERROR_OTROS_DOC_OBS_OBLIGATORIO = "convocatoria.documentacion.detalle.label.otros.descripcion.observaciones.obligatoria";

        /**
         * Constante mensaje error subir un documento con el formato de nombre
         * incorrecto
         */
        public static final String DOCUMENTO_MENSAJE_SUBIDA_NOMBRE_FORMATO_KO = "convocatorias.detalle.msg.documento.subida.nombre.formato.KO";

        /** Constante mensaje error subir un documento */
        public static final String DOCUMENTO_MENSAJE_OBTENER_KO = "convocatorias.detalle.msg.documento.obtener.KO";

        /** Constante mensaje error descargar un documento */
        public static final String DOCUMENTO_MENSAJE_DESCARGAR_KO = "convocatorias.detalle.msg.documento.descarga.KO";

        // Solicitud
        public static final String SOLICITUD_MENSAJE_CARGAR_KO = "convocatorias.detalle.solicitud.msg.cargar.KO";

        // Expediente
        public static final String EXPEDIENTE_MENSAJE_CARGAR_KO = "convocatorias.detalle.solicitudes.expediente.msg.cargar.KO";

        // DOCUMENTOS
        public static final String ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE = "convocatorias.detalle.solicitudes.expedientes.documentos.error.documento.no.existe";
        
        public static final String DESCARGA_DOCUMENTO_OK = "convocatorias.detalle.solicitudes.expedientes.documentos.descarga.ok";

        public static final String ERROR_CODIGO_FICA_DOCUMENTO = "convocatorias.detalle.solicitudes.expedientes.documentos.error.documento";

        public static final String ERROR_CODIGO_FICA_FALTANTE = "convocatorias.detalle.solicitudes.expedientes.documentos.error.documento.codigo.fica.faltante";

        public static final String DOCUMENTOS_NO_SELECCION_DOCUMENTO = "documentos.no.seleccion.documento";

        public static final String ENVIO_PORTAFIRMAS_CORRECTO = "mensaje.envio.portafirmas.correcto";
        
        public static final String ENVIO_NOTE_CORRECTO = "mensaje.envio.note.correcto";
        
        public static final String ENVIO_COLA_PORTAFIRMAS_CORRECTO = "mensaje.envio.portafirmas.cola.correcto";

        // VMM 26/09/26 MD-3062: faltaba el mensaje de exito al finalizar la revision de una
        // modificacion de presupuesto/plazo/otra - finalizarRevision() solo tenia mensaje de error.
        public static final String FINALIZAR_REVISION_MODIF_CORRECTO = "solapa.resol.boton.finalizar.revision.modif.ok";

        public static final String EXCLUIDO_DEFINITAVEMENTE_CORRECTO = "mensaje.exclusion.correcto";
        
        public static final String DESISITIDO_DEFINITAVEMENTE_CORRECTO = "mensaje.desistimiento.correcto";

        public static final String CREAR_DOCUMENTO_SUBSANACION_REQUISITOS = "mensaje.crear.documento.subsanacion.requisitos";

        public static final String REVISION_EXPEDIENTE_REQUISITO_KO = "revision.expediente.requisito.ko";

        public static final String REVISION_EXPEDIENTE_ESTADO_REQUISITO_OK = "revision.expediente.estado.requisito.ok";

        public static final String REVISION_EXPEDIENTE_ESTADO_REQUISITO_KO = "revision.expediente.estado.requisito.ko";

        public static final String REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_OK = "revision.expediente.estado.requisito.finalizar.ok";

        public static final String REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_KO = "revision.expediente.estado.requisito.finalizar.ko";
        
        public static final String REVISION_EXPEDIENTE_ESTADO_ALEGACION_ACEPTAR_OK = "revision.expediente.estado.alegacion.aceptar.ok";
        
        public static final String REVISION_EXPEDIENTE_ESTADO_ALEGACION_ACERTAR_KO = "revision.expediente.estado.alegacion.aceptar.ko";

        public static final String REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_OK = "revision.expediente.estado.documento.ok";

        public static final String REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_KO = "revision.expediente.estado.documento.ko";

        public static final String REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK = "revision.expediente.estado.documento.finalizar.ok";

        public static final String REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO = "revision.expediente.estado.documento.finalizar.ko";
        
        public static final String REVISION_EXPEDIENTE_ESTADO_REQUISITO_NO_FINALIZAR = "revision.expediente.estado.documento.no.finalizar";
        
        public static final String REVISION_EXPEDIENTE_ESTADO_FINANCIACION_FINALIZAR_OK = "revision.expediente.estado.financiacion.finalizar.ok";

        public static final String REVISION_EXPEDIENTE_ESTADO_FINANCIACION_FINALIZAR_KO = "revision.expediente.estado.financiacion.finalizar.ko";
        
        public static final String REVISION_CONTRATO_ESTADO_DOCUMENTO_FINALIZAR_OK = "revision.contrato.estado.documento.finalizar.ok";

        public static final String REVISION_CONTRATO_ESTADO_DOCUMENTO_FINALIZAR_KO = "revision.contrato.estado.documento.finalizar.ko";
        
        public static final String REVISION_CONTRATO_FECHA_INICIO_KO = "revision.contrato.fecha.inicio.ko";
        
        public static final String REVISION_CONTRATO_OBSERVACIONES_CIUDADANO_KO = "revision.contrato.observaciones.ciudadano.ko";
        
        public static final String REVISION_CONTRATO_FECHA_FIN_KO = "revision.contrato.fecha.fin.ko";

        public static final String REVISION_REQUISITO_FALTA_ACTUALIZAR_ESTADO_INTERNO = "mensaje.revision.requisito.falta.actualizar.estado.interno";

        public static final String REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO = "mensaje.revision.documento.falta.actualizar.estado.interno";
        
        public static final String REVISION_REQUISITO_FALTA_REVISAR = "mensaje.revision.requisito.falta.revisar";
        
        public static final String REVISION_REQUISITO_FALTA_REVISAR_REQUISITOS = "mensaje.revision.requisito.falta.revisar.requisitos";
        
        public static final String REVISION_REQUISITOS_FALTA_CREAR_DOCUMENTO_SUBSANACION_REQUISITOS = "mensaje.revision.requisito.falta.crear.documentos.subsanacion.requisitos";

        public static final String DESCARGAR_LISTADOS_CONVOCATORIA = "mensaje.descargar.listados.convocatoria";

        /** Constante mensaje de eliminación correctamente */
        public static final String ELIMINADO_CORRECTAMENTE_COORDINADOR_ASOCIADO = "convocatorias.detalle.solicitudes.expedientes.evaluacion.label.eliminado.correctamente";

        /** Constante mensaje de area cientifica coordinador eliminar */
        public static final String EXPEDIENTE_ASIGNADO_COODINADOR_ELIMINAR = "convocatorias.detalle.solicitudes.expedientes.evaluacion.label.cordinador.asignado";

        /**
         * Constante mensaje de eliminación de coordinador en estado diferente
         * de pendiente de asignación de evaluador
         **/
        public static final String EXPEDIENTE_ASIGNADO_COODINADOR_ELIMINAR_ESTADO = "convocatorias.detalle.solicitudes.expedientes.evaluacion.label.cordinador.estado";

        /** Constante mensaje de asignado correctamente */
        public static final String EXPEDIENTE_ASIGNADO_CORRECTAMENTE = "convocatorias.detalle.solicitudes.expedientes.evaluacion.label.asignado.correctamente";

        /** Constante mensaje de asignado correctamente */
        public static final String EXPEDIENTE_TRAMITE_ADMINISTRACION = "solapa.resol.msg.info.informes.tramite.expediente.provicionalmente";

        /** Constante mensaje de asignado correctamente */
        public static final String EXPEDIENTE_ESTADO_RENUNCIA_EXPRESA = "solapa.resol.msg.info.informes.tramite.expediente.renuncia.expresa";

        /***
         * Constante mensaje se ha rechazado correctamente el informe de
         * coordinador
         ***/
        public static final String EVALUACION_COORDINADOR_RECHAZADO_OK = "solapa.eval.msg.rechazar.informe.coordinador.ok";

        public static final String INFORME_COORDINADOR_APROBADO_OK = "solapa.eval.msg.aprobar.informe.coordinador.ok";

        public static final String EVALUACION_COORDINADOR_APROBADO_OK = "solapa.eval.msg.info.informe.aprobado.ok";

        public static final String EVALUACION_COORDINADOR_APROBADO_KO = "solapa.eval.msg.info.informe.aprobado.ko";

        
        public static final String EVALUACION_EVALUADOR_RECHAZADO_OK = "solapa.eval.msg.rechazar.informe.evaluador.ok";
        /***
         * Constante mensaje se ha rechazado correctamente el informe de
         * coordinador
         ***/
        
        /** Error al guardar la nota de evaluación del expediente. */
        public static final String EVALUACION_NOTA_GUARDAR_KO = "solapa.eval.msg.nota.guardar.ko";
        
        public static final String ERROR_EXPEDIENTE_EVAL_NO_EXISTE = "convocatorias.detalle.solicitudes.expedientes.documentos.error.expediente.eval.no.existe";
        
        public static final String ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS = "convocatorias.detalle.solicitudes.expedientes.documentos.error.documentos.no.existe";

        public static final String APROBADO_PROVISIONAL_OK = "mensaje.expediente.aprobado.provisional.ok";
        
        public static final String REVERTIR_CONCEDIDO_PROVISIONAL_OK = "mensaje.expediente.concedido.provisional.revertir.ok";

        public static final String APROBADO_PROVISIONAL_KO = "mensaje.expediente.aprobado.provisional.ko";
        
        public static final String REVERTIR_DENEGADO_PROVISIONAL_OK = "mensaje.expediente.denegado.provisional.revertir.ok";

        public static final String DENEGAR_PROVISIONAL_OK = "mensaje.expediente.denegado.provisional.ok";
        
        public static final String REVERTIR_APROBADO_DEFINITIVO_OK = "mensaje.expediente.concedido.definitio.revertir.ok";

        public static final String DENEGAR_PROVISIONAL_KO = "mensaje.expediente.denegado.provisional.ko";
        
        public static final String REVERTIR_DENEGADO_DEFINITIVO_OK = "mensaje.expediente.denegado.definitio.revertir.ok";
        
        public static final String REVERTIR_DESISTIDO_DEFINITIVO_OK = "mensaje.expediente.desistido.definitio.revertir.ok";
        
        public static final String REVERTIR_EXCLUIDO_DEFINITIVO_OK = "mensaje.expediente.excluido.definitio.revertir.ok";

        public static final String CONCEDIDO_DEFINITVO_AYUDA_OK = "mensaje.expediente.aprobado.concedido.ayuda.ok";
        
        public static final String CONCEDIDO_DEFINITVO_AYUDA_KO = "mensaje.expediente.aprobado.concedido.ayuda.ko";
        
        public static final String APROBADA_ALEGACION = "mensaje.expediente.aprobado.alegacion";
        
        public static final String RECHAZADA_ALEGACION = "mensaje.expediente.rechazado.alegacion";
        
        /**
         * Constante mensaje de aprobado y denegado definitivo del expediente
         ***/
        public static final String APROBADO_DEFINITIVO_OK = "mensaje.expediente.aprobado.definitivo.ok";

        public static final String APROBADO_DEFINITIVO_KO = "mensaje.expediente.aprobado.definitivo.ko";

        public static final String DENEGADO_DEFINITIVO_OK = "mensaje.expediente.denegado.definitivo.ok";

        public static final String DENEGADO_DEFINITIVO_KO = "mensaje.expediente.denegado.definitivo.ko";
        
        public static final String EXCLUIDO_DEFINITIVO_OK = "mensaje.expediente.excluido.definitivo.ok";

        public static final String EXCLUIDO_DEFINITIVO_KO = "mensaje.expediente.excluido.definitivo.ko";
              
        public static final String DESISTIDO_DEFINITIVO_OK = "mensaje.expediente.desistido.definitivo.ok";

        public static final String DESISTIDO_DEFINITIVO_KO = "mensaje.expediente.desistido.definitivo.ko";

        /** constante mensaje de resolver la convbocatoria **/
        public static final String RESOLVER_CONVOCATORIA_OK = "mensaje.resolver.convocatoria.ok";
        
        /** constante mensaje de resolver la convbocatoria **/
        public static final String RESOLVER_CONVOCATORIA_PROV_OK = "mensaje.resolver.convocatoria.prov.ok";

        public static final String MOTIVO_RECHAZO_VACIO = "convocatorias.detalle.solicitudes.expedientes.documentos.motivo.rechazo.vacio";

        public static final String MOTIVO_RECHAZO_VACIO_EVAL = "convocatorias.detalle.solicitudes.expedientes.evaluacion.motivo.rechazo.vacio";

        /* constante mensaje solapa financiacion */
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_KO = "convocatorias.detalle.solicitudes.expedientes.importe.ko";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MINIMO_KO = "convocatorias.detalle.solicitudes.expedientes.importe.min.ko";

        public static final String SOLICITUD_FINANCIACION_ACTUACION_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.actuaciones.ok";

        public static final String SOLICITUD_FINANACIACION_ACTUACION_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.actuaciones.ko";

        public static final String SOLICITUD_FINANCIACION_PRESUPUESTO_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.presupuesto.ok";

        public static final String SOLICITUD_FINANCIACION_PRESUPUESTO_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.presupuesto.ko";
        
        public static final String SOLICITUD_FINANCIACION_INTENSIDAD_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.intensidad.ok";

        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_ANUALIDAD = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto.anualidad";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto.expdte";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_FIJO_PRESUP = "convocatorias.detalle.solicitudes.expedientes.importe.max.fijo.presupuesto";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_FINALIZAR = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto.finalizar";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_ANUALIDAD_FINALIZAR = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto.anualidad.finalizar";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE_FINALIZAR = "convocatorias.detalle.solicitudes.expedientes.importe.max.presupuesto.expdte.finalizar";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MINIMO_PRESUP_EXPDTE_FINALIZAR = "convocatorias.detalle.solicitudes.expedientes.importe.min.presupuesto.expdte.finalizar";
        
        public static final String SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_NULO_DESBORDA = "convocatorias.detalle.solicitudes.expedientes.importe.max.nulo_desborda";

        public static final String SOLICITUD_FINANCIACION_OBSERVACION_PRESUP = "convocatorias.detalle.solicitudes.expedientes.observacion.presupuesto";

        public static final String SOLICITUD_FINANCIACION_OBSERVACION_PRESUP_PENDIENTES = "convocatorias.detalle.solicitudes.expedientes.observacion.presupuesto.pendientes";

        // VMM 06/10/26 MD-3113: validacion de conceptos (descripcion vacia / repetidos)
        public static final String SOLICITUD_CONCEPTO_VACIO = "convocatorias.detalle.solicitudes.expedientes.concepto.vacio";

        public static final String SOLICITUD_CONCEPTO_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.concepto.repetido";

        public static final String SOLICITUD_CONCEPTO_REPETIDO_EDICION = "convocatorias.detalle.solicitudes.expedientes.concepto.repetido.edicion";

        public static final String SOLICITUD_FINANCIACION_OBSERVACION_ACTUACION = "convocatorias.detalle.solicitudes.expedientes.observacion.actuacion";
        
        public static final String SOLICITUD_MODIFPPO_OTRAS_MODIF_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.modifPPO.otrasModif.grabar.ok";
        
        public static final String SOLICITUD_MODIFPPO_OTRAS_MODIF_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.modifPPO.otrasModif.grabar.ko";
        
        public static final String SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.modifPPO.modifPresup.grabar.ok";
        
        public static final String SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.modifPPO.modifPresup.grabar.ko";
        
        public static final String SOLICITUD_MODIFPPO_MODIF_PLAZOS_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.modifPPO.modifPlazos.grabar.ok";
        
        public static final String SOLICITUD_MODIFPPO_MODIF_PLAZOS_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.modifPPO.modifPlazos.grabar.ko";
        
        public static final String SOLICITUD_MODIFPPO_OBSERVAC_MODIF_GRABAR_OK = "convocatorias.detalle.solicitudes.expedientes.modifPPO.observacModif.grabar.ok";
        
        public static final String SOLICITUD_MODIFPPO_OBSERVAC_MODIF_GRABAR_KO = "convocatorias.detalle.solicitudes.expedientes.modifPPO.observacModif.grabar.ko";
        
        public static final String SOLICITUD_FINANCIACION_CONCEPTO_INSERTAR_OK = "convocatorias.detalle.solicitudes.expedientes.financiacion.insertar.concepto.ok";

        public static final String SOLICITUD_FINANCIACION_CONCEPTO_INSERTAR_KO = "convocatorias.detalle.solicitudes.expedientes.financiacion.insertar.concepto.ko";
        
        public static final String SOLICITUD_FINANCIACION_CONCEPTO_ELIMINAR_OK = "convocatorias.detalle.solicitudes.expedientes.financiacion.eliminar.concepto.ok";

        public static final String SOLICITUD_FINANCIACION_CONCEPTO_ELIMINAR_KO = "convocatorias.detalle.solicitudes.expedientes.financiacion.eliminar.concepto.ko";
        
        public static final String SOLICITUD_FINANCIACION_CONCEPTO_GUARDAR_KO = "convocatorias.detalle.solicitudes.expedientes.financiacion.guardar.concepto.ko";
        
        public static final String SOLICITUD_FINANCIACION_CONCEPTO_NO_MODIFICACIONES = "convocatorias.detalle.solicitudes.expedientes.financiacion.grabar.no.modificaciones";
        
        public static final String SOLICITUD_FINANCIACION_ERROR_IMPORTE_NULO = "convocatorias.detalle.solicitudes.expedientes.financiacion.error.importe.nulo";
        

        /** */
        public static final String EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.ejecucion.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.ejecucion.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.justec.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_JUSTEC_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.justec.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSECO_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.juseco.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_JUSECO_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.juseco.vacio";

        public static final String EXPEDIENTE_JUSTEC_OBSERVACION_VACIO = "convocatorias.detalle.solicitudes.expedientes.justec.observacion.vacio";

        public static final String EXPEDIENTE_JUSTEC_FEC_EJECUCION_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ejecucion.error";

        public static final String EXPEDIENTE_JUSTEC_FEC_JUSTEC_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.justec.error";
        
        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_PASADA = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.justec.pasada";
        
        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSTECO_PASADA = "convocatorias.detalle.solicitudes.expedientes.just.eco.fec.ini.justec.pasada";

        public static final String EXPEDIENTE_JUSTEC_FEC_JUSECO_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.juseco.error";

        public static final String EXPEDIENTE_JUSTEC_FEC_EJECUCION_MAYOR_JUSTEC_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ejecucion.mayor.justec.error";

        public static final String EXPEDIENTE_JUSTEC_FEC_EJECUCION_MAYOR_JUSECO_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ejecucion.mayor.juseco.error";

        public static final String EXPEDIENTE_JUSTEC_FEC_JUSTEC_MAYOR_JUSECO_ERROR = "convocatorias.detalle.solicitudes.expedientes.justec.fec.justec.mayor.juseco.error";

        public static final String EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.ejecucion.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.ejecucion.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSTEC_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.justec.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_JUSTEC_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.justec.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_INI_JUSECO_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.ini.juseco.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_FIN_JUSECO_REPETIDO = "convocatorias.detalle.solicitudes.expedientes.justec.fec.fin.juseco.repetido";

        public static final String EXPEDIENTE_JUSTEC_FEC_NO_SECUENCIAL = "convocatorias.detalle.justec.fec.no.secuencial";
        
        public static final String EXPEDIENTE_JUSECO_FEC_NO_SECUENCIAL = "convocatorias.detalle.just.eco.fec.no.secuencial";

        /* Constantes mensajes asignación evaluador a evaluación JCT (justificación científico técnica) */
        public static final String EVALUADOR_ASOCIADO_EVALUACION_JCT_OK = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.guardar.ok";
        
        public static final String EVALUADOR_ASOCIADO_EVALUACION_JCT_KO = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.guardar.ko";
        
        public static final String EVALUADOR_DESASOCIADO_EVALUACION_JCT_OK = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.borrar.ok";
        
        public static final String EVALUADOR_DESASOCIADO_EVALUACION_JCT_KO = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.borrar.ko";
        
        public static final String EVALUADOR_EVAL_VALIDACION_EXISTENTE = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.eval.repetido";
        
        public static final String EVALUADOR_ANUALIDAD_EXISTENTE = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.anualidad.repetida";
        
        public static final String EVALUADOR_PERIODO_EXISTENTE = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.periodo.repetido";
        
        public static final String VALIDACION_FECHA_FIN_MAYOR_FECHA_INICIO = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.fecha.mayor";
        
        public static final String VALIDACION_FECHA_INI_INTERVALO_JCT_KO = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.fechaini.intervalo";
   
        public static final String EVALUADOR_VALIDACION_EXISTENTE = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.evaljct.repetido";
        
        public static final String EVALUADOR_ANUALIDAD_VACIA = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.anulidad.ko";
        
        public static final String EVALUADOR_FECHAS_VACIAS = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.fechas.ko";
        
        public static final String EVALUADOR_FECHAINI_MENOR_FECHAFIN_KO = "convocatorias.detalle.solicitudes.expedientes.justec.label.justevalcietec.fcinimenorfcfin";
        

        /* Constantes mensajes expedientes evaluacion asignar evaluadores */
        public static final String ERROR_CARGAR_PERSONA = "personas.label.expedientes.evaluacion.evaluador.error.cargar.lista.persona";
    
        public static final String MSG_COORDINADOR_BAJA_EVALUADOR_OK = "mensaje.detalle.expedientes.evaluacion.evaluador.baja.evaluador.correcto";
        
        public static final String MSG_COORDINADOR_BAJA_EVALUADOR_KO = "mensaje.detalle.expedientes.evaluador.baja.evaluador.error";
        
        public static final String MSG_EVALUADOR_BAJA_EVALUADOR_OK = "mensaje.detalle.expedientes.evaluador.baja.evaluador.correcto";
        
        public static final String MSG_EVALUADOR_BAJA_EVALUADOR_KO = "mensaje.detalle.expedientes.evaluacion.evaluador.baja.evaluador.error";
        
        public static final String ERROR_EVALUADOR_ASOCIADO_EXPEDIENTE = "mensaje.detalle.evaluador.evaluacion.evaluador.asociado.expediente";
        
        public static final String ERROR_EVALUADOR_ASOCIADO_EXPEDIENTE_PERIODO = "mensaje.detalle.evaluador.evaluacion.evaluador.asociado.expediente.periodo";
        
        public static final String ERROR_ASOCIAR_EVALUADOR_ASOCIADO_EXPEDIENTE = "mensaje.detalle.evaluador.evaluacion.error.asociar.evaluador";
        
        /** Constante mensaje de asignado correctamente */
        public static final String ASIGNADO_CORRECTAMENTE = "mensaje.detalle.evaluador.busque.asignado.correctamente";
        
        public static final String EVALUADOR_YA_ASIGNADO = "mensaje.error.detalle.label.evaluador";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC_APROBADO_OK = "mensaje.detalle.expedientes.evaluacion.aprobado.ok";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC_APROBADO_KO = "mensaje.detalle.expedientes.evaluacion.aprobado.ko";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC_RECHAZADO_OK = "mensaje.detalle.expedientes.evaluacion.desaprobado.ok";
        
        public static final String ERROR_RECHAZAR_EVALUACION_EXPEDIENTE_JUSTEC_KO = "mensaje.detalle.expedientes.evaluacion.desaprobado.ko";
        
        public static final String ERROR_RECHAZAR_INFORME_COORDINADOR_KO = "convocatorias.detalle.solicitudes.expedientes.evaluacion.msg.error.ko";
        
        public static final String REVISION_EXPEDIENTE_EVAL_OBSERVACION_KO = "revision.expedientes.evaluacion,obsevacion.ko";
        
        public static final String OK_CONTRATO_EXTENDER_INFO = "mensaje.ok.extender.informacion";       
        
        public static final String ERROR_CONTRATO_EXTENDER_INFO = "mensaje.error.extender.informacion";       
        
        public static final String ERROR_CONTRATO_EXTENDER_MAX_PERMITIDO = "mensaje.error.extender.maxPermitido";       
        
        public static final String ERROR_CONTRATO_EXTENDER_FC_IGUALES = "mensaje.error.extender.fechasIguales";       
        
        public static final String ERROR_CONTRATO_EXTENDER_FC_INICIAL_POSTERIOR = "mensaje.error.extender.fechasInicialPosterior";       

        public static final String ERROR_CONTRATO_EXTENDER_FC_MAXIMA_NO_MODIFICADA = "mensaje.error.extender.fechasMacimaNoModificada";
        
        public static final String MSG_VALIDAR_FINALIZAR_FINAN_PRESUP_TIPO_PERSONA = "mensaje.validar.finalizar.finan.presupuesto.tipo.persona";

    }

    /**
     *
     * Constantes para caja de texto
     */
    public static final class Resolucion {
        /**
         *
         */
        protected Resolucion() {

        }

        public static final String ALEGACIONES_PRESENTADAS = "solapa.resolucion.alegaciones.presentadas";

        public static final String ALEGACIONES_NO_PRESENTADAS = "solapa.resolucion.alegaciones.no.presentadas";

        public static final String PENDIENTE_REALIZAR_INFORME = "solapa.resolucion.pendiente.realizar.informe";
    }

    /**
     * Constantes para referencias a elementos del formulario
     */
    public static final class Referencia {

        /**
         *
         */
        private Referencia() {
        }

        public static final String DLG_VER_HISTORICO = "dialogVerHistorico";
        
        public static final String DLG_VER_HISTORICO_ANUAL = "dialogVerHistoricoAnual";
        
        public static final String DLG_VER_HISTORICO_ECO = "dialogVerHistoricoEco";

        public static final String EXPEDIENTE_JUSTEC_ID_CONTENEDOR_PERIODOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaPeriodosJustCienTec";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_PERIODOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaPeriodosJustecCT";
       
        public static final String EXPEDIENTE_JUSTEC_ID_CONTENEDOR_DOCUMENTOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaDocumentosJustCienTec";

        public static final String EXPEDIENTE_JUSTEC_BOTON_SUBSANACION = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnEnviarSubsanarRequiPortafirmasJusTec";
        
        public static final String EXPEDIENTE_JUSTEC_COMBO_CAUSA_SUBSANACION = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaCausasJCT";
        
        public static final String EXPEDIENTE_JUSTEC_BOTON_DESISTIMIENTO = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnEnviarDesistirPortafirmasJCT";
        
        public static final String EXPEDIENTE_JUSTEC_BOTON_REVISAR = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnFinalizarRevisionJCT";
        
        public static final String EXPEDIENTE_JUSTEC_BOTON_GUARDAR = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnGuardar";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelEvaluacionJCT";
        
        public static final String EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_EVALUADORES_PORPERIODO = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:expEvalJusTec";
        

        public static final String EXPEDIENTE_JUSECO_ID_CONTENEDOR_PERIODOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaPeriodosJustEco";
        
        public static final String EVALUACION_EXPEDIENTE_JUSECO_ID_CONTENEDOR_PERIODOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaPeriodosJustEco";
       
        public static final String EXPEDIENTE_JUSECO_ID_CONTENEDOR_DOCUMENTOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaDocumentosJustEco";
        
        public static final String EXPEDIENTE_JUSECODOC_ID_CONTENEDOR_DOCUMENTOS = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaDocumentacionJEPartida";

        public static final String EXPEDIENTE_JUSECO_BOTON_SUBSANACION = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnEnviarSubsanarRequiPortafirmasJusEco";
        
        public static final String EXPEDIENTE_JUSECO_COMBO_CAUSA_SUBSANACION = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:listaCausasJE";
        
        public static final String EXPEDIENTE_JUSECO_BOTON_DESISTIMIENTO = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnEnviarDesistirPortafirmasJE";
        
        public static final String EXPEDIENTE_JUSECO_BOTON_REVISAR = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnFinalizarRevisionJE";
        
        public static final String EXPEDIENTE_JUSECO_BOTON_GUARDAR = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnGuardar";
        
        
    }
}
