package org.madrid.saii.lib.util;

import java.util.Date;

/**
 * Clase de constantes.
 *
 * @author everis
 *
 */
public final class Constantes {

    /**
     * Constructor para que no se genere el publico por defecto
     */
    protected Constantes() {
    }

    /**
     *
     * Constantes para Catalogos.
     */
    public static final class Cata {

        /**
         *
         */
        protected Cata() {
        }

        /** ID de ESTADO_CONVOCATORIA */
        public static final Integer ESTADO_CONVOCATORIA = 1;

        /** ID de TIPO_MODALIDAD */
        public static final Integer TIPO_MODALIDAD = 2;

        /** ID de MODALIDAD_CONVOCATORIA */
        public static final Integer MODALIDAD_CONVOCATORIA = 3;

        /** ID de IDENTIFICACION */
        public static final Integer IDENTIFICACION = 5;

        /** ID de TIPO_ACTOR */
        public static final Integer TIPO_ACTOR = 4;

        /** ID de ESTADO_SOLICITUD */
        public static final Integer ESTADO_SOLICITUD = 6;

        /** ID de ESTADO_EXPEDIENTE */
        public static final Integer ESTADO_EXPEDIENTE = 7;

        /** ID de REGLA */
        public static final Integer REGLA = 8;

        /** ID de TIPO_IMPRESO */
        public static final Integer TIPO_IMPRESO = 9;

        /** ID de FORMATO_BLOQUE */
        public static final Integer FORMATO_BLOQUE = 10;

        /** ID de CATEGORIA_FORMULARIO */
        public static final Integer CATEGORIA_FORMULARIO = 11;

        /** ID de TIPO_COMPONENTE */
        public static final Integer TIPO_COMPONENTE = 14;

        /** ID de TIPO_DOCUMENTACION_SOLICITUD */
        public static final Integer TIPO_DOCUMENTACION_SOLICITUD = 107;

        /** ID de SUB_TIPO_COMPONENTE */
        public static final Integer SUB_TIPO_COMPONENTE = 15;

        /** ID de ESTADO_ENTIDAD */
        public static final Integer ESTADO_ENTIDAD = 16;

        /** ID de ESTADO_REPRESENTANTE_LEGAL */
        public static final Integer ESTADO_REPRESENTANTE_LEGAL = 17;

        /** ID de ESTADO_EXPEDIENTE_BENEFICIARIO */
        public static final Integer ESTADO_EXPEDIENTE_BENEFICIARIO = 19;

        /** Constante Áreas científicas */
        public static final Integer AREAS_CIENTIFICAS = 20;
        
        /** Constante SubÁreas científicas */
        public static final Integer SUBAREAS_CIENTIFICAS = 144;

        /** Constante Documentacion de la convocatoria */
        public static final Integer DOCUMENTACION_CONVOCATORIA = 21;

        /** Constante Líneas de ayuda */
        public static final Integer LINEAS_AYUDA = 22;

        /** Constante Tipo actor persona física (solicitantes) */
        public static final Integer TIPO_ACTOR_PERSONA_FISICA_SOLICITANTE = 24;

        /** Constante Tipo actor persona física (coordinadores) */
        public static final Integer TIPO_ACTOR_PERSONA_FISICA_COORDINADOR = 43;

        /** Constante Tipo actor persona física (participantes) */
        public static final Integer TIPO_ACTOR_PERSONA_FISICA_PARTICIPANTE = 44;

        /** Constante Tipo actor persona jurídica */
        public static final Integer TIPO_ACTOR_PERSONA_JURIDICA = 25;

        /** Constante Temática bloque */
        public static final Integer TEMATICAS_BLOQUE = 26;

        /** Constante Documentación de la solicitud */
        public static final Integer DOCUMENTACION_SOLICITUD = 27;

        /** Constante catalogo Fondos Europeos */
        public static final Integer DS_FONDOS_EUROPEOS = 28;

        /** Constante catálogo Causa de denegación */
        public static final Integer CAUSA_DENEGACION = 29;

        /** Constante catálogo Requisitos */
        public static final Integer REQUISITOS = 30;

        /** Constante catálogo Tipos Identificacion */
        public static final Integer TIPOS_IDENTIFICACION = 31;

        /** Constante Titulacion */
        public static final Integer TITULACION = 32;

        /** Constante Organismo */
        public static final Integer ORGANISMO = 33;

        /** Constante Rango de notas */
        public static final Integer RANGO_NOTAS = 34;

        /** Constante Rango de notas */
        public static final Integer ESTADO_COORDINADOR = 35;
        
        /** Constante Rango de notas */
        public static final Integer TIPO_DOCUMENTO = 56;

        /** Constante Rango de notas */
        public static final Integer ESTADO_EVALUADOR = 36;

        /** Constante Causa de denegacion de evaluacion */
        public static final Integer CAUSA_DENEGACION_JCT = 37;
        
        /** Constante Tramites del gestor */
        public static final Integer TRAMITES_GESTOR = 38;
        
        /** Constante Tramites del ciudadano */
        public static final Integer TRAMITES_CIUDADANO = 46;

        /** Constante del tipo de justificacion cientifica tecnica */
        public static final Integer TIPO_JUSTIFICACION_CIENTIFICO_TECNICA = 39;

        /** Constante para partidas presupuestarias */
        public static final Integer PARTIDA_PRESUPUESTARIA = 40;

        /** Constante para la ayuda solicitada */
        public static final Integer AYUDA_SOLICITADA = 41;

        /** Constante para las actuaciones */
        public static final Integer ACTUACIONES = 42;

        /* Constante para financiacion */
        public static final Integer CATALOGOS = 48;
        
        /** tipo causa denegacion  **/
        public static final Integer TIPO_CAUSA_DENEGACION = 53;
        
        /** Causa de denegación Evaluación **/
        public static final Integer CAUSA_DENEGACION_EVALUACION = 54;
        
        /** Causa de denegación Económica **/
        public static final Integer CAUSA_DENEGACION_CONOMICA = 55;
        
        /** Tipo de informe **/
        public static final Integer TIPO_INFORME = 57;
        
        public static final Integer NIVEL_ASOCIACION = 59;
        
        /* Código del catálogo de la revisión administrativa  */
        public static final Integer CATALOGO_DOC_REV_ADMIN = 134;
        
        /** Constante Causa de denegacion de Contratos */
        public static final Integer CAUSA_DENEGACION_CONTRATOS = 138;
        
        /* Código del catálogo de la revisión administrativa  */
        public static final Integer CATALOGO_ESTADO_TIPO_RESOLUCION = 139;
        
        /** ID de TIPO_ACTOR */
        public static final Integer CATALOGO_ESTADO_TIPO_CONCESION = 141;
        
        /** Constante catalogo tipo entidad */
        public static final Integer TIPO_ENTIDAD = 148;
        
        /** Constante catalogo tipo solicitud */
        public static final Integer TIPO_SOLICITUD = 158;
        
        /** Constante catálogo Medio notificación */
        public static final Integer MEDIO_NOTIFICACION = 165;
        
        /** Constante catálogo persona solicitante */
        public static final Integer PERSONA_SOLICITANTE = 166;
        
        /** **/
        public static final Integer ACEPTACION_AYUDA = 51;
        
        
        public static final Integer ID_ACEPTACION_DESISTIMIE_AYUDA_ACEPTAR = 600;
        
        
        public static final Integer ID_ACEPTACION_DESISTIMIE_AYUDA_DESISTIR = 601;
        
        /** ID de TIPO_ACTOR */
        public static final Integer MOTIVO_RECHAZO_CONFLICTO_INTERES = 60;
        
        /** ID de ICDA */
        public static final Integer ICDA = 61;
        
        /** ID de PROCEDIMIENTO_ICDA */
        public static final Integer PROCEDIMIENTO_ICDA = 62;
        
        /** ID de LISTADOS_CONVOCATORIAS */
        public static final Integer LISTADOS_CONVOCATORIAS = 63;
    }

    /**
     * Constantes para los listados de convocatorias.
     */
    public static final class ListadoConvocatoria {

        /**
         * Constructor privado.
         */
        protected ListadoConvocatoria() {
        }

        /** Listado de situación de evaluadores */
        public static final Integer SITUACION_EVALUADORES = 780;
    }

    /**
     *
     * Constantes para TipoActor.
     */
    public static final class TipoActor {

        /**
         *
         */
        protected TipoActor() {
        }

        /** Constante codigo SOLICITANTE */
        public static final Integer SOLICITANTE = 11;

        /** Constante codigo PARTICIPANTE */
        public static final Integer PARTICIPANTE = 12;

        /** Constante codigo COORDINDADOR */
        public static final Integer COORDINADOR = 13;

    }
    
    
    /**
    *
    * Constantes para TipoActor.
    */
   public static final class NivelInformeEvaluacion {

       /**
        *
        */
       protected NivelInformeEvaluacion() {
       }

       /** Constante codigo SOLICITANTE */
       public static final Integer EVALUACION_EXPEDIENTE_DOC = 551;

       /** Constante codigo PARTICIPANTE */
       public static final Integer EVALUACION_INFORME_DOC = 552;

   }
    
    /**
    *
    * Constantes para TipoActor.
    */
   public static final class TipoConcesion {

       /**
        *
        */
       protected TipoConcesion() {
       }

       /** Constante codigo SOLICITANTE */
       public static final Integer CONCESION_SOLICITANTE = 1762;

       /** Constante codigo PARTICIPANTE */
       public static final Integer CONCESION_PARTICIPANTE = 1763;



   }
    
    

    /**
     *
     * Constantes para TipoDocumento.
     */
    public static final class TipoDocumento {

        /**
         *
         */
        protected TipoDocumento() {
        }

        /** Constante Pdf Expediente firmado */
        public static final Integer PDF_EXPEDIENTE_FIRMADO = 170;

        /** Constante Recibo presentacion */
        public static final Integer RECIBO_PRESENTACION = 171;

        /** Constante Acta de comisión de evaluación ***/
        public static final Integer ACTA_COMISION_EVALUACION = 301;

        /** Constante Generación de la propuesta de resolución provisional ***/
        public static final Integer PROPUESTA_RESOLUCION_PROVISIONAL = 302;

        /** Constante Generación de la propuesta de resolución definitiva ***/
        public static final Integer PROPUESTA_RESOLUCION_DEFINITIVA = 303;

        /** Constante Generación de la propuesta de resolución concesion ***/
        public static final Integer RESOLUCION_DEFINITIVA = 304;

        /** Constante Alegaciones */
        public static final Integer ALEGACIONES = 172;

        /** Constante Autorizado */
        public static final Integer DOCUMENTO_AUTORIZADO = 553;
        
        public static final Integer DOCUMENTO_MODIF_CONCEPTO_PRESUP = 554;

        /** Constante Generación de la propuesta de resolución provisional ***/
        public static final Integer RESOLUCION_PROVISIONAL = 305;

        public static final Integer RESOLUCION_PLAZO_OFICIO = 306;
        
        public static final Integer CONTRATO = 1531;
        
        public static final Integer PDF_FORM_JUSTEC_FINAL_FIRMADO = 1607;
        
        public static final Integer PDF_FORM_JUSTEC_INTERMEDIA_FIRMADO = 1612; 
        
        public static final Integer PDF_FORM_GESTION_CONTRATO_FIRMADO = 1608;
        
        public static final Integer PDF_FORM_CONTRATO_RENUNCIA_SIN_SUSTI_FIRMADO = 1614;
        
        public static final Integer PDF_FORM_MODIFICACIONES_PRESUP_PLAZOS_OTROS_FIRMADO = 1613;
        
        public static final Integer PDF_FORM_CONTRATO_RENUNCIA_CON_SUSTI_FIRMADO = 1611;
        
        public static final Integer RESOLUCION_PRESUPUESTO_MODIF = 1750;
        
        public static final Integer BORRADOR_ACTA_COMITE_GESTION = 176;
        
        public static final Integer ACTA_FINAL_COMITE_GESTION = 177;
        
        // MD-2190 quitam os la constante de alegaciones excluido provisional
        // public static final Integer ALEGACIONES_EXCLUIDO_PROVISIONAL = 1775;
        
        public static final Integer RESOLUCION_CONVOCATORIA_PROVISIONAL = 1919;
        
        public static final Integer RESOLUCION_CONVOCATORIA_DEFINITIVO = 1920;
        
        public static final Integer PDF_FORM_ACEPTACION_DESIS_FIRMADO = 1615; 
        
        public static final Integer PDF_FORM_JUSECO_FIRMADO = 179;
        
        public static final Integer DOC_ZIP_JUSTIFICACION_ECONOMICO = 178;
        
        public static final Integer PROPUESTA_RESOLUCION_PROVISIONAL_EXCLUIDA = 308;
        
        public static final Integer PROPUESTA_RESOLUCION_PROVISIONAL_DESISTIDA = 309;
        
        public static final Integer DOCUMENTO_EVALUACION_INFORME = 552;
        
        public static final Integer DOCUMENTO_EVALUACION_INFORME_FIRMADO = 682;
        
        public static final Integer DOCUMENTO_EVALUACION_INFORME_SIN_FIRMA = 683;
        
        public static final Integer DOCUMENTO_EVALUACION_INFORME_SIN_FIRMA_BORRADOR = 684;
        
        
    }

    /**
     *
     * Constantes para Identificacion.
     */
    public static final class Identificacion {

        /**
         *
         */
        protected Identificacion() {
        }

        /** Constante codigo FISICA */
        public static final Integer FISICA = 15;

        /** Constante codigo JURIDICA */
        public static final Integer JURIDICA = 14;

        /** Constante texto FISICA */
        public static final String DS_FISICA = "PF";

        /** Constante texto JURIDICA */
        public static final String DS_JURIDICA = "PJ";

        /**
         * Valor que devuelve el NIF del certificado para usuarios de persona física
         * extracomunitarios, que no disponen de NIF/NIE. Estos usuarios se
         * identifican por su email (tokenResultado.getCdUsuario()).
         */
        public static final String NO_NIE = "NO_NIE";

        /**
         * Valor a usar en los campos de auditoría (CD_USU_ALTA, CD_USU_MODIF,
         * CD_USU_BAJA) para usuarios extracomunitarios en lugar de su identificador
         * (el email), ya que dichos campos son VARCHAR2(12) y el email no cabe.
         */
        public static final String CD_USU_EXTRACOMUNITARIO = "ESTRA_C";
    }

    /**
     *
     * Constantes para EstadoConvocatoria.
     */
    public static final class EstadoConvocatoria {

        /**
         *
         */
        protected EstadoConvocatoria() {
        }

        /** Constante codigo EN CONSTRUCCION */
        public static final Integer EN_CONSTRUCCION = 1;

        /** Constante codigo APROBADA */
        public static final Integer APROBADA = 2;

        /** Constante codigo INICIADO PLAZO PRESENTACION DE SOLICITUDES */
        public static final Integer INICIADO_PLAZO_PRESENTACION_SOLICITUDES = 150;

        /** Constante codigo FINALIZADO PLAZO PRESENTACION DE SOLICITUDES */
        public static final Integer FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES = 151;

        /** Constante codigo RESUELTA */
        public static final Integer RESUELTA = 152;

    }

    /**
     *
     * Constantes para EstadoSolicitud.
     */
    public static final class EstadoSolicitud {

        /**
         *
         */
        protected EstadoSolicitud() {
        }

        /** Constante codigo PRESENTADA */
        public static final Integer PRESENTADA = 16;

        /** Constante codigo INICIADA */
        public static final Integer INICIADA = 17;

        /** Constante codigo CONCEDIDA */
        public static final Integer CONCEDIDA = 18;

        /** Constante codigo DENEGADA */
        public static final Integer DENEGADA = 19;

        /** Constante codigo EXCLUIDA */
        public static final Integer EXCLUIDA = 20;

        /** Constante codigo DESISTIDA */
        public static final Integer DESISTIDA = 21;

        /** Constante codigo RENUNCIADA */
        public static final Integer RENUNCIADA = 22;
        
        /** Constante codigo RENUNCIADA */
        public static final Integer CONCEDIDA_DESISTIDA = 23;
        

    }

    /**
     *
     * Constantes para TipoImpreso.
     */
    public static final class TipoImpreso {

        /**
         *
         */
        protected TipoImpreso() {
        }

        /** Constante codigo IMPRESO SOLICITUD */
        public static final Integer FORMULARIO_SOLICITUD = 30;

        /** Constante codigo FORMULARIO COMPLEMENTARIO*/
        public static final Integer FORMULARIO_COMPLEMENTARIO = 31;

        /** Constante Formulario de renuncia */
        public static final Integer FORMULARIO_CONTRATO_RENUNCIA = 83;

        /** Constante Formulario de contrato sustitucion */
        public static final Integer FORMULARIO_CONTRATO_SUSTITUCION = 84;

        /** Constante Formulario de Justificacion cientifico tecnica intermedia */
        public static final Integer FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA = 85;
        
        /* Constante para tipo de formularios de Modificaciones presup/plazos/otros (sin actas de comite) */
        public static final Integer FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS = 86;
        
        /* Constantes para tipos de formularios de Actas de Comites de Gestion */
        public static final Integer FORMULARIO_ACTA_GESTION_ORDINARIA = 87;
        
        /** Constante Formulario de Coççntrat inicial */
        public static final Integer FORMULARIO_CONTRATO_INICIAL = 89;
        
        /** Constante Formulario de Acta Presupuestaria */
        public static final Integer FORMULARIO_ACTA_PRESUPUESTARIA = 88;
        
        /** Constante Formulario de Justificacion cientifico tecnica final */
        public static final Integer FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL = 90;
        
        /** Constante Formulario de Justificacion Economica intermedia */
        public static final Integer FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA= 91;
        
        /** Constante Formulario de Justificacion Economica final */
        public static final Integer FORMULARIO_JUSTIFICACION_ECO_FINAL = 92;
        
        /** Constante Formulario de Formulario aceptación de ayuda */
        public static final Integer FORMULARIO_ACEPTACION_AYUDA = 93;
        
    }

    /**
     *
     * Constantes para TipoIdentificacion.
     */
    public static final class TipoIdentificacion {

        /**
         *
         */
        protected TipoIdentificacion() {
        }

        /** Constante codigo NIF */
        public static final Integer NIF = 160;

        /** Constante codigo NIE */
        public static final Integer NIE = 161;

        /** Constante codigo tipo identificación extracomunitario (usuarios sin NIF/NIE, identificados por email) */
        public static final Integer EXTRA_COMUNITARIO = 164;
    }

    /**
     *
     * Constantes para Portafirmas.
     */
    public static final class Portafirmas {

        /**
         *
         */
        protected Portafirmas() {
        }

        /** Constante codigo Firmante */
        public static final String FIRMANTE = "firmante";

        /** Constante codigo Verificador */
        public static final String VERIFICADOR = "verificador";

        /** Constante respuesta OK */
        public static final String RESPUESTA_OK = "0";

        /** Constante documento rechazado */
        public static final String RECHAZADO = "1";

        /** Constante modo respuesta */
        public static final Integer MODO_RESPUESTA = 2;

    }

    /**
    *
    * Constantes para Portafirmas.
    */
   public static final class EstadoExpedienteResolucion {

       /**
        *
        */
       protected EstadoExpedienteResolucion() {
       }

       /** Constante codigo Firmante */
       public static final Integer INICIADA = 1747;

       /** Constante codigo Verificador */
       public static final Integer PEDIENTE = 1748;
       
       /** Constante codigo Verificador */
       public static final Integer PENDIENTE_PORTAFIRMAS = 1757;

       
       /** Constante codigo Verificador */
       public static final Integer ENVIADA_PORTAFIRMAS = 1752;

       /** Constante respuesta OK */
       public static final Integer RESUELTA = 1749;
       
       public static final Integer REQUIERE_SUBSANACION = 1753;
       
       public static final Integer PENDIENTE_REVISION_SUBSANACION = 1754;
       
       public static final Integer REVISION_OK = 1755;
       
       public static final Integer REVISION_KO = 1756;
       
       public static final Integer PEDIENTE_MODIFICACIONES = 1759;
       
       public static final Integer PENDIENTE_ENVIO_COLA = 1758;

       public static final Integer NOTIFICADO = 1760;
       
   }
   
   /**
   *
   * Constantes para Portafirmas.
   */
  public static final class EstadoExpedienteTipoResolucion {

      /**
       *
       */
      protected EstadoExpedienteTipoResolucion() {
      }

      /** Constante codigo Firmante */
      public static final Integer ACEPTADA = 1744;

      /** Constante codigo Verificador */
      public static final Integer ACEPTADA_PARCIALMENTE = 1745;

      /** Constante respuesta OK */
      public static final Integer RECHAZADA = 1746;


  }
   
    
    /**
     *
     * Constantes para EstadoExpediente.
     */
    public static final class EstadoExpediente {

        /**
         *
         */
        protected EstadoExpediente() {
        }

        /** Constante codigo INICIADA */
        public static final Integer INICIADA = 200;

        /** Constante codigo PROPUESTA */
        public static final Integer PROPUESTA = 201;

        /** Constante codigo ACEPTADA */
        public static final Integer ACEPTADA = 202;

        /** Constante codigo NO_ACEPTADA */
        public static final Integer NO_ACEPTADA = 203;

        /** Constante codigo SELECCIONADA */
        public static final Integer SELECCIONADA = 204;

        /** Constante codigo NO_SELECCIONADA */
        public static final Integer NO_SELECCIONADA = 205;

        /**
         * Estado cuando se completa el registro del expediente
         */
        public static final Integer EN_TRAMITACION_PRESENTADA = 206;

        /**
         * Estado cuando esta en tramitacion presentanda y acaba el plazo de
         * convocatoria
         */
        public static final Integer EN_TRAMITACION = 207;

        /**
         * Estado cuando se puede iniciar con el proceso de revisión de
         * documentos
         */
        public static final Integer PENDIENTE_REVISION = 208;

        /**
         * Estado cuando existe algún documento observado
         */
        public static final Integer PENDIENTE_REQUERIMIENTO = 209;

        /**
         * Estado cuando la revisión de todos los documentos es correcto
         */
        public static final Integer REVISION_OK = 210;

        /**
         * Estado cuando se ha notificado al solicitante que tiene documentos
         * por corregir
         */
        public static final Integer PENDIENTE_SUBSANACION_REQUISITOS = 211;

        /**
         * Estado cuando se ha subsanado los documentos observados
         */
        public static final Integer PENDIENTE_REVISION_SUBSANACION_REQUISITOS = 212;

        /**
         * Estado cuando se rechaza un expediente
         */
        public static final Integer DESISTIDO = 213;

        /**
         * Estado cuando se excluye un expediente
         */
        public static final Integer EXCLUIDO = 214;
        
        /**
         * Estado cuando se rechaza un expediente
         */
        public static final Integer DESISTIDO_PROVISIONAL = 248;

        /**
         * Estado cuando se excluye un expediente
         */
        public static final Integer EXCLUIDOS_PROVISIONAL = 249;

        /**
         * Estado de pendiente asignacion coordinador
         */
        public static final Integer PENDIENTE_ASIGNACION_COORDINADOR = 215;

        /**
         * Estado de expediente de evaluadores
         */
        public static final Integer TRAMITE_EN_EVALUACION = 217;

        /**
         *
         */
        public static final Integer DENEGADO_PROVISIONAL = 218;

        /**
         *
         */
        public static final Integer APROBADO_PROVISIONAL = 219;

        /**
         *
         */
        public static final Integer RENUNCIA_EXPRESA = 220;

        /**
         *
         */
        public static final Integer PROPUESTA_DE_RESOLUCION = 221;

        /**
         *
         */
        public static final Integer DENEGADO_DEFINITIVO = 222;

        /**
         *
         */
        public static final Integer CONCEDIDO_DEFINITIVO = 223;

        /**
         *
         */
        public static final Integer EVALUADO_KO = 224;

        /**
         *
         */
        public static final Integer EVALUADO_OK = 225;
        
        /** Rechazado definitivo */
        public static final Integer RECHAZADO_DEFINITIVO = 226;

        /** Constantes Pendiente revisión evaluación gestor */
        public static final Integer PENDIENTE_REVISION_EVALUACION_GESTOR = 227;

        /** Constante pendiente provisional */
        public static final Integer PENDIENTE_PROVISIONAL = 228;
        
        /** Constante pendiente provisional */
        public static final Integer PENDIENTE_DEFINITIVO = 250;
        
        public static final Integer PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO = 251;
        
        public static final Integer PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO = 252;
        
        public static final Integer PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO = 253;
       
        public static final Integer PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO = 254;

        /** Constante Pendiente de revisión alegaciones */
        public static final Integer PENDIENTE_REVISION_ALEGACIONES = 229;
        
        // MD-2190 Quitamos la constante del estado del expediente Pendiente revisión alegaciones excluido provisional
        /** Constante Pendiente de revisión alegaciones */
//        public static final Integer PENDIENTE_REVISION_ALEGACIONES_EXCLUIDO_PROVISIONAL =  1799;

        /** Constante Pendiente de revisión subsanacion */
        public static final Integer PENDIENTE_REVISION_SUBSANACION = 238;

        /** Constante Pendiente de requerimiento - Firma rechazada */
        public static final Integer PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA = 239;
        
        public static final Integer PENDIENTE_PRESENTACION_CONTRATOS = 240;
        
        public static final Integer PENDIENTE_PRESENTACION_SUBSANACION = 241;
        
        /** Constante de Exclusión provisional */
        public static final Integer EXCLUIDO_PROVISIONAL= 1773;
        
        public static final Integer PENDIENTE_PRESENTAR = 242;
        
        public static final Integer PENDIENTE_SUBSANACION_CONJUNTA_REQ_DOC = 243;
        
        public static final Integer CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST = 244; 
        
        public static final Integer CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST = 245;  
        
        public static final Integer CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO = 246;  
        
        public static final Integer CONCEDIDO_DEFINITIVO_DESISTIDO = 247;  
        
    }

    /**
     * Constantes para Estado de coordinador
     */
    public static final class EstadoExpedienteContrato {

        /**
         * Constructor de clase
         */
        protected EstadoExpedienteContrato() {

        }
        
        //añadir numeros
        public static final Integer PENDIENTE = 1596;
        
        public static final Integer PENDIENTE_PRESENTAR = 1597;
        
        public static final Integer PENDIENTE_REVISION = 1598;
        
        public static final Integer  PENDIENTE_ENVIO_SUBSANACION = 1599;
        
        public static final Integer PENDIENTE_ENVIO_PORTAFIRMAS = 1600;
        
        public static final Integer PENDIENTE_FIRMA = 1601;
        
        public static final Integer  PENDIENTE_ENVIO_NOTE = 1602;
        
        public static final Integer PENDIENTE_SUBSANACION = 1603;
        
        public static final Integer  PENDIENTE_REVISION_SUBSANACION = 1604;
        
        public static final Integer REVISION_KO = 1605;
        
        public static final Integer FINALIZADO = 1606;
        


    }
    
    /**
    *
    * Constantes para EstadoFinanciacion
    */
   public static final class EstadoFinanciacion {

       /**
        *
        */
       protected EstadoFinanciacion() {
       }

       /** Constante codigo Pendiente revisión Financiación */
       public static final Integer PDTE_REVISION_FINANCIACION = 140;

       /** Constante codigo Revisión Financiación OK */
       public static final Integer REVISION_FINANCIACION_OK = 141;
       
       /** Constante codigo Revisión Financiación OK */
       public static final Integer REVISION_FINANCIACION_PROVISIONAL = 142;
       
       /** Constante codigo Revisión Financiación OK */
       public static final Integer REVISION_FINANCIACION_APROBADA = 143;
       
       /** Constante codigo Revisión Financiación OK */
       public static final Integer REVISION_FINANCIACION_DEFINITVA = 144;
   }
    
    
    /**
     *
     * Constantes para EstadoExpedienteBeneficiario.
     */
    public static final class EstadoExpedienteBeneficiario {

        /**
         *
         */
        protected EstadoExpedienteBeneficiario() {
        }

        /** Constante codigo INICIADA */
        public static final Integer INICIADA = 270;

        /** Constante codigo PROPUESTA */
        public static final Integer PROPUESTA = 271;

        /** Constante codigo ACEPTADA */
        public static final Integer ACEPTADA = 272;

        /** Constante codigo NO_ACEPTADA */
        public static final Integer NO_ACEPTADA = 273;

        /** Constante codigo SELECCIONADA */
        public static final Integer SELECCIONADA = 274;

        /** Constante codigo NO_SELECCIONADA */
        public static final Integer NO_SELECCIONADA = 275;

        /** Constante codigo EN_TRAMITACION */
        public static final Integer EN_TRAMITACION = 276;

        /** Constante codigo PENDIENTE_SUBSANACION_REQUISITOS */
        public static final Integer PENDIENTE_SUBSANACION_REQUISITOS = 277;

        /** Constante codigo EXCLUIDO */
        public static final Integer EXCLUIDO = 278;

        /** Constante codigo DESISTIDO */
        public static final Integer DESISTIDO = 279;
        
        
        /** Constante codigo EXCLUIDO */
        public static final Integer EXCLUIDO_PROVISIONAL = 292;

        /** Constante codigo DESISTIDO */
        public static final Integer DESISTIDO_PROVISIOANL = 291;

        /** Constante codigo DENEGADO_PROVISIONAL */
        public static final Integer DENEGADO_PROVISIONAL = 280;

        /** Constante codigo APROBADO_PROVISIONAL */
        public static final Integer APROBADO_PROVISIONAL = 281;

        /** Constante codigo PROPUESTA_DE_RESOLUCION */
        public static final Integer PROPUESTA_DE_RESOLUCION = 282;

        /** Constante codigo DENEGADO_DEFINITIVO */
        public static final Integer DENEGADO_DEFINITIVO = 283;

        /** Constante codigo CONCEDIDO_DEFINITIVO */
        public static final Integer CONCEDIDO_DEFINITIVO = 284;
        
        /** Constante codigo ENVIADA_SUBSANACION */
        public static final Integer ENVIADA_SUBSANACION = 1178;
        
        /** Constante codigo ENVIADA_SUBSANACION */
        public static final Integer EN_REVISION = 1180;
        
        /** Constante codigo ENVIADA_SUBSANACION */
        public static final Integer RESOLUCION_DESESTIMATORIA = 1181;
        
        public static final Integer PENDIENTE_PRESENTACION_CONTRATOS = 286;

        /**
         *
         */
        public static final Integer RENUNCIA_EXPRESA = 285;
        
        /** Rechazado definitivo */
        public static final Integer RECHAZADO_DEFINITIVO = 287;
        
        public static final Integer CONDEDIDO_DEFINITIVO_PEDIENTE_DE_ACEPTACION = 288;
        
        
        /** Constante codigo CONCEDIDO_DEFINITIVO */
        public static final Integer CONCEDIDO_DEFINITIVO_ACEPTADO = 289;
        
        /** Constante codigo CONCEDIDO_DEFINITIVO */
        public static final Integer CONCEDIDO_DEFINITIVO_DESISTIDO = 290;
        
        /** Constante codigo DESISTIMIENTO PROVISIONAL */
        public static final Integer DESISTIMIENTO_PROVIONAL = 291;
        
        /** Constante codigo EXCLUIDO PROVISIONAL */
        public static final Integer EXCLUSION_PROVISIONAL = 292;

    }

    /**
     * Constantes para Estado de coordinador
     */
    public static final class EstadoCoordinador {

        /**
         * Constructor de clase
         */
        protected EstadoCoordinador() {

        }

        public static final Integer PENDIENTE_ASIGNACION_EVALUADORES = 230;

        public static final Integer EVALUADORES_ASIGNADOS = 231;

        public static final Integer EN_EVALUACION_POR_COORDINADOR = 232;

        public static final Integer CERRADO_COORDINADOR = 233;

        public static final Integer EN_EVALUACION_POR_COORDINADOR_EVALUADOR = 234;

        public static final Integer CERRADO_GESTOR = 235;
        
        public static final Integer EVALUACION_RECHAZADA_EXPERTO = 556;
        
        public static final Integer PENDIENTE_ACEPTACION_EXPERTO = 555;
        

    }

    /**
     * Constantes para Estado de evaluador
     */
    public static final class EstadoEvaluador {

        /**
         * Constructor de clase
         */
        protected EstadoEvaluador() {

        }

        public static final Integer EN_EVALUACION_POR_EVALUADOR = 236;

        public static final Integer CERRADO_EVALUADOR = 237;
        
        public static final Integer EN_EVALUACION_POR_EVALUADOR_JUSTEC = 1659;

        public static final Integer CERRADO_EVALUADOR_JUSTEC = 1661;
    }

    /**
     *
     * Constantes para Estado de documentos
     *
     */
    public static final class EstadoDocumento {

        /**
         * Constructor de clase
         */
        protected EstadoDocumento() {

        }

        public static final Integer SIN_SUBIR = 119;

        /**
         * Estado cuando sube el documento
         */
        public static final Integer PRESENTADO = 110;

        /**
         * Estado cuando se puede iniciar con el proceso de revisión de
         * documento
         */
        public static final Integer PENDIENTE_REVISION = 111;

        /**
         * Estado cuando se asocia a un documento una causa de denegación
         */
        public static final Integer REQUIERE_SUBSANACION = 112;
        
        /**
         * Estado cuando se envía a la cola de envío de notificaciones
         * subsanación
         */
        public static final Integer ENVIO_COLA_NOTIFICACIONES = 120;

        /**
         * Estado cuando se envía a Portafirmas el informe de requiere
         * subsanación
         */
        public static final Integer ENVIADO_A_PTFR = 113;

        /**
         * Estado cuando se envía a NOTE el informe de requiere subsanación
         */
        public static final Integer REQUIERE_SUBSANACION_NOTIFICADO = 114;
        
        /**
         * Estado cuando se envía a NOTE el informe de requiere subsanación
         */
        public static final Integer RESOLUCION_NOTIFICADO = 181;

        /**
         * Estado cuando se subsana el documento pero no se finaliza el proceso
         * de subsanación
         */
        public static final Integer EN_TRAMITE_SUBSANACION = 126;

        /**
         * Estado cuando finaliza el proceso de subsanación
         */
        public static final Integer PENDIENTE_REVISION_SUBSANACION = 115;

        /**
         * Estado cuando el documento es aprobado
         */
        public static final Integer REVISION_OK = 116;

        /**
         * Estado cuando el documento es rechazado
         */
        public static final Integer REVISION_KO = 117;

        /**
         * Estado cuando no se subsano el documento en el plazo configurado
         */
        public static final Integer PLAZO_SUBSANACION_VENCIDO = 118;

        /**
         * El beneficiario ha subido el documento pero no se ha presentando
         */
        public static final Integer PENDIENTE_PRESENTAR = 180;
        
        /** Constante Pendiente de subsanación */
        public static final Integer PENDIENTE_SUBSANACION = 1177;
        
        /** Constante Pendiente de subsanación científico técnica */
        public static final Integer PENDIENTE_SUBSANACION_CIENTIFICO_TECNICA = 1178;
        
        // MD-2190 Quitamos la declaración del estado del documento requiere alegación excluido provisional notificado
        /**
         * Estado cuando se envía a NOTE el informe de requiere alegación de exclusión provisional
         */
//        public static final Integer REQUIERE_ALEGACION_EXCLUIDO_PROVISIONAL_NOTIFICADO = 1774;
        
    }

    /**
     *
     * Constantes para Estado Documento Portafirmas
     */
    public static final class EstadoDocumentoPtfr {
        /**
         *
         */
        protected EstadoDocumentoPtfr() {

        }

        public static final String CODIGO_PENDIENTE = "P";

        public static final String CODIGO_FIRMADO = "F";

        public static final String CODIGO_RECHAZADO = "R";
        
        public static final String CODIGO_FIRMADO_TEXTO = "FIRMADO";

        /**
         * Constante EstadoDocumentoPtfr codigo estado 'Pendiente'
         */
        public static final String DS_PENDIENTE = "Pendiente";

        /**
         * Constante EstadoDocumentoPtfr codigo estado 'Firmado'
         */
        public static final String DS_FIRMADO = "Leído";

        /**
         * Constante EstadoDocumentoPtfr codigo estado 'Rechazado'
         */
        public static final String DS_RECHAZADO = "Rechazado";

    }

    /**
     * Constantes para los estados de SAII_EXPEDIENTE_NOTIF (catálogo idCatalogo=65)
     */
    public static final class EstadoNotifiCiud {

        /**
         * Constructor de clase
         */
        protected EstadoNotifiCiud() {

        }

        /** Estado inicial al presentar la notificación del ciudadano */
        public static final Integer PENDIENTE_REVISION = 850;

        /** Estado cuando la administración ha resuelto la notificación */
        public static final Integer RESUELTA = 851;

    }

    /**
     *
     * Constantes para los codigos de plantillas portafirmas
     *
     */
    public static final class TipoDocumentoPtfr {
        /**
         *
         */
        protected TipoDocumentoPtfr() {

        }

        public static final String PLANTILLA_SUBSANACION_ADMINISTRATIVA = "SAII-subsanacion_adm";

        public static final String PLANTILLA_DESISTIMIENTO = "SAII-desistimiento_def";
        
        public static final String PLANTILLA_DESISTIMIENTO_PROVISIONAL = "SAII-desistimiento_prov";

        public static final String PLANTILLA_SUBSANACION_REQUISITOS = "SAII-subsanacion_requisitos";

        public static final String PLANTILLA_EXCLUSION = "SAII-exclusion_def";
        
        public static final String PLANTILLA_EXCLUSION_PROV = "SAII-exclusion_prov";
        
        public static final String PLANTILLA_SUBSANACION_CONTRATOS = "SAII-subsanacion_contratos";
        
        public static final String PLANTILLA_SUBSANACION_JUSTIFICACION_TECNICA = "SAII-subsanacion_justificacion_ct";
        
        public static final String PLANTILLA_SUBSANACION_JUSTIFICACION_ECO = "SAII-subsanacion_justificacion_eco";
        
        public static final String PLANTILLA_CONCESION_PROVISIONAL = "SAII-concesion_prov";
        
        public static final String PLANTILLA_CONCESION_DEFINITIVA = "SAII-concesion_def";
        
        public static final String PLANTILLA_DENEGACION_PROVISIONAL = "SAII-denegacion_prov";
        
        public static final String PLANTILLA_DENEGACION_DEFINITIVA = "SAII-denegacion_def";
        
        public static final String PLANTILLA_RESOLUCION_PRESUPUESTO_MODIF = "SAII-modif_presup_plz";
        
        public static final String INFORME_PARRAFO_1 = "El expediente ";

        public static final String INFORME_PARRAFO_2 = " requiere la subsanación de los siguientes documentos:\n\n";

        public static final String INFORME_PARRAFO_3 = " presenta los siguientes documentos por desistir:\n\n";

        public static final String INFORME_PARRAFO_4 = " requiere la subsanación de los siguientes documentos:\n\n";

        public static final String INFORME_PARRAFO_5 = " se ha excluido al no cumplir los siguientes requisitos:\n\n";

        public static final String INFORME_TITULO_DOCUMENTO = "Documento: ";

        public static final String INFORME_TITULO_REQUISITO = "Requisito: ";

        public static final String INFORME_TITULO_MOTIVO = "Observaciones: ";

        public static final String NOMBRE_ARCHIVO_INFORME = "InformePortafirmas";

        public static final String NOMBRE_ARCHIVO_EXTENSION = "pdf";
        
        public static final String PLANTILLA_RESOLUCION_PROVISIONAL_CONV = "SAII-resolucion_prov";
        
        public static final String PLANTILLA_RESOLUCION_DEFINITVA_CONV = "SAII-resolucion_def";
        
        public static final String PLANTILLA_ACEPTACION_DESISTIMIENTO_AYUDA = "SAII-aceptacion_ayuda";

    }

    /**
     *
     * Constantes para causa de denegación
     *
     */
    public static final class CausaDenegacion {
        /**
         *
         */
        protected CausaDenegacion() {

        }

        public static final String CODIGO_OTROS = "OTRO";

        public static final String CODIGO_DS_OTROS = "Otros";

        public static final String ERROR_ELIMINAR_OTROS = "No se puede eliminar la causa de denegación otros.";
        
        public static final Integer CODIGO_CAUSA_JUSTEC_OTROS = 1189;
        
        public static final Integer CODIGO_CAUSA_ADMIN_OTROS = 1122;
        
        public static final Integer CODIGO_CAUSA_CONTRATO_OTROS = 1736;
        
        public static final Integer CODIGO_CAUSA_EVAL_OTROS = 650;
        
        public static final Integer CODIGO_CAUSA_ECO_OTROS = 651;
        
    }

    /**
     *
     * Constantes para EstadoEntidad.
     */
    public static final class EstadoEntidad {

        /**
         *
         */
        protected EstadoEntidad() {
        }

        /** Constante codigo PENDIENTE */
        public static final Integer PENDIENTE = 100;
    }

    /**
     *
     * Constantes para EstadoEntidad.
     */
    public static final class EstadoRepresentantesLegales {

        /**
         *
         */
        protected EstadoRepresentantesLegales() {
        }

        /** Constante codigo PENDIENTE */
        public static final Integer PENDIENTE = 103;
        /** Constante codigo APROBADO */
        public static final Integer APROBADO = 104;
    }

    /**
     * Modalidad Convocatoria
     */
    public static final class ModalidadConvocatoria {
        protected ModalidadConvocatoria() {
        }

        public static final Integer ADJUDICACION_DIRECTA = 7;




    }
    /**
     *
     * Constantes para TipoModalidad.
     */
    public static final class TipoModalidad {

        /**
         *
         */
        protected TipoModalidad() {
        }

        /** Constante codigo SOLICITANTE */
        public static final Integer SOLICITANTE_CODIGO = 3;

        /** Constante codigo ADMINISTRACION */
        public static final Integer ADMINISTRACION_CODIGO = 4;
    }

    /**
     *
     * Constantes para Test.
     */
    public static final class Test {

        /**
         *
         */
        protected Test() {
        }

        /** Constante mensaje error error en la prueba */
        public static final String TEST_ERROR_PRUEBA = "Error en la prueba";

        public static final String TEST_ADMIN = "ADMIN";

        public static final String TEST_MOD = "Prueba Test Mod";
    }

    /**
     *
     * Constantes para Error.
     */
    public static final class Error {

        /**
         *
         */
        protected Error() {

        }

        /** Constante ERROR */
        public static final String ERROR_EXTENSION_FICHERO = "la imagen adjunta no tiene extensión.";

        /** Constante ERROR */
        public static final String ERROR_GENERAL = "ERROR";

        /** Constante ERROR_DATA */
        public static final String ERROR_DATA = "ERROR DataAccess";

        /** Constante mensaje error guardar */
        public static final String ERROR_GUARDAR = "mensaje.error.guardar";

        /** Constante ERROR_CLOSE */
        public static final String ERROR_CLOSE = "ERROR close";

        /** Constante mensaje error valores de entrada */
        public static final String ERROR_VALORES_ENT = "Error en los valores de entrada.";
        
        /** Constante mensaje error valores de entrada */
        public static final String ERROR_EXISTE_EXP_MOD_PENDIENTE = "Error tiene una modificación pendiente de revisión.";

        /**
         * Constante mensaje error valores de entrada en el formato del nombre
         * del documento
         */
        public static final String ERROR_NOMBRE_DOCUMENTO = "El nombre del documento no tiene el formato correcto.";

        /** Constante mensaje error valores de entrada */
        public static final String ERROR_ID_NO_ENCONTRADO = "Error, id no encontrado.";

        /** Constante mensaje de error al no poder traer un fichere */
        public static final String ERROR_TRAER_FICHERO = "Error recuperando el fichero.";
        
        /** Constante mensaje de error al no poder traer un contrato */
        public static final String ERROR_TRAER_CONTRATO = "Error recuperando el contrato.";

        /**
         * Constante mensaje de error al no encontrar impreso para la
         * convocatoria y el tipo de actor
         */
        public static final String ERROR_IMPRESO_ACTOR = "No se encuentra el impreso para la convocatoria y el tipo de actor";
        
        /**
         * Constante mensaje de error al no encontrar impreso de contrato para la
         * convocatoria y el tipo de actor
         */
        public static final String ERROR_IMPRESO_ACTOR_CONTRATO = "No se encuentra el impreso de contrato para la convocatoria y el tipo de actor";

        /**
         * Constante mensaje de error al no tener un actor una convocatoria
         * activa
         */
        public static final String ERROR_ACTOR_SIN_CONVOCATORIA = "Para esta convocatoria no hay definidos actores para el tipo de persona indicada";

        /**
         * Constante mensaje de error al superar el máximo de participantes en
         * la convocatoria
         */
        public static final String ERROR_MAXIMO_PARTICIPANTES_CONVOCATORIA = "Se ha superado el número máximo de participantes para el tipo de actor indicado";

        /**
         * Constante mensaje de error al no econtrar actor configurado de envio
         * a note
         */
        public static final String ERROR_ACTOR_NOTE = "No existe actor configurado para recibir la notificación";

        /**
         * Constante mensaje de error al no superar el mínimo de participantes
         * en la convocatoria
         */
        public static final String ERROR_MINIMO_PARTICIPANTES_CONVOCATORIA = "No se ha alcanzado el número mínimo de participantes requeridos en la convocatoria";

        /**
         * Constante mensaje de error al no superar el mínimo de coordinadores
         * en la convocatoria
         */
        public static final String ERROR_MINIMO_COORDINADORES_CONVOCATORIA = "No se ha alcanzado el número mínimo de cooordinadores requeridos en la convocatoria";

        /**
         * Constante mensaje de error al superar el máximo de coordinador en la
         * convocatoria
         */
        public static final String ERROR_MAXIMO_COORDINADORES_CONVOCATORIA = "Se ha superado el número máximo de coordinadores para el tipo de actor indicado";

        public static final String ERROR_CAUSA_DENEGACION_DUPLICADA = "convocatoria.causaDenegacion.label.mensaje.anadir.duplicado";

        public static final String ERROR_REQUISITO_SUBMODALIDAD_DUPLICADO = "convocatoria.requisito.submodalidad.mensaje.duplicado";

        public static final String ERROR_REQUISITO_SUBMODALIDAD_NO_PERMITIDO = "convocatoria.requisito.submodalidad.mensaje.no.permitido";

        public static final String ERROR_TIPO_DOCUMENTO_DUPLICADO = "convocatoria.tipoDocumento.label.mensaje.anadir.duplicado";

        public static final String ERROR_TRAMITE_NO_SELECCIONADO = "convocatoria.tipoDocumento.error.tramite";

        public static final String ERROR_TIPO_AREA_DUPLICADO = "convocatorias.detalle.area.cientifica.mensaje.anadir.duplicado";
        
        public static final String ERROR_TIPO_SUBAREA_DUPLICADO = "convocatorias.detalle.subarea.cientifica.mensaje.anadir.duplicado";

        public static final String MSG_ERROR = "MSG_ERROR";

        public static final String MSG_WARNING = "MSG_WARNING";

        public static final String ERROR_ID_CONVOCATORIA_FALTANTE = "No se especificó el identificador de la convocatoria.";

        public static final String ERROR_ID_EVALUACION_FALTANTE = "No se especificó el identificador de la evaluación.";

        public static final String ERROR_PLANTILLA_NO_CONFIGURADA = "convocatorias.plantilla.descarga.error";
        
        public static final String ERROR_REQUISITO_TIPO_PERSONA_DUPLICADO = "convocatoria.tipo.persona.mensaje.duplicado";

        /**
         * Constante mensaje de error al no encontrarse el interesado dado de
         * alta como coordinador o evaluador en el sistema en la convocatoria
         */
        public static final String ERROR_ALTA_COORDINADOR_EVALUADOR = "No se encuentra dado de alta como coordinador o evaluador en el sistema. Consulte con el administrador";

        public static final String ERROR_INFORME_NO_EXISTE_EVALUACION = "No existen evaluaciones para generar el informe.";

        public static final String CODIGO_EXCEPTION_PERSONALIZADA = "EXPPER";

        public static final String ERROR_COORDINADOR_ASIGNADO_EXPEDIENTE = "El coordinador ya está asignado al expediente.";

        public static final String ERROR_NO_SE_PUDO_CALCULAR_FECHA = "No se pudo calcular la fecha.";

        public static final String ERROR_EJECUCION_FUNCION = "Error al ejecutar funcion";

        public static final String ERROR_SUBIR_DOCUMENTO_EXPEDIENTES_TRAMITES_ACTIVOS = "Para poder subir el documento es necesario que todos los expedientes en trámite activos estén evaluados.";

        public static final String ERROR_SUBIR_DOCUMENTO_COMISION_EVALUACION = "Para poder subir el documento es necesario que esté adjuntado previamente el Acta de comisión de evaluación.";

        public static final String ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA = "No se puede subir el documento de Resolución de concesión. Aún no se ha adjuntado el documento de Propuesta de resolución definitiva.";
        
        public static final String ERROR_SUBIR_DOCUMENTO_RESOLUCION_PROVISIONAL = "No se puede subir el documento de Resolución provisional. Aún no se ha adjuntado el documento de Propuesta de resolución provisional.";

        public static final String ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA_EXISTE = "El documento de Propuesta de resolución concesión ya se encuentra registrado";
        
        public static final String ERROR_SUBIR_DOCUMENTO_RESOLUCION_PROVISIONAL_EXISTE = "El documento de Propuesta de resolución provsional ya se encuentra registrado";

        public static final String ERROR_SUBIR_DOCUMENTO_PROPUESTA_RESOLUCION_DEFINITIVA = "El documento de Propuesta de resolución definitiva ya se encuentra registrado";
        
        public static final String ERROR_SUBIR_DOCUMENTO_PROPUESTA_RESOLUCION_PROVISIONAL = "El documento de Propuesta de resolución provisional ya se encuentra registrado";

        public static final String ERROR_INFORME_ALEGACIONES_NO_EXISTE_DATO = "No existen alegaciones para generar el informe.";

        public static final String ERROR_FECHA_FIN_ALEGACIONES_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA = "No se puede subir el documento de Propuesta de resolución definitiva. El proceso de alegaciones finaliza el día: ";

        public static final String ERROR_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA_ESTADO_PROPUESTA_RESOLUCION = "No se puede subir el documento de Propuesta de resolución definitiva. Existen expedientes en estado Propuesta de resolución que están pendientes de la valoración de las alegaciones presentadas.";

        public static final String ERROR_RESOLVER_CONVOCATORIA = "No es posible resolver la convocatoria, existe algún expediente en algún estado no terminado.";
        
        public static final String ERROR_RESOLVER_CONVOCATORIA_IMPRESOS_INCOMPLETOS = "No es posible resolver la convocatoria, existe algún impreso incompleto. Debe ir al modulo de SAII_ADMINISTRACIÓN y configurarlo correctamente.";

        public static final String ERROR_AL_INTENTAR_RESOLVER_CONVOCATORIA = "Error al intentar resolver la convocatoria.";

        public static final String ERROR_ID_CONVOCATORIA = "Error al encontrar el ID de la convocatoria.";
        
        public static final String ERROR_MENSAJE_FECHAS_CONTRATO_VACIO = "convocatorias.detalle.idenConvocatoria.label.fechas.contrato.vacia.error";

        public static final String ERROR_CARGAR_VALIDACIONES = "Error, no se pudo cargar las validaciones de resolucion definitiva";

        public static final String ERROR_CREAR_INFORME = "Error, no se pudo crear el informe";

        public static final String ERROR_AL_FINALIZAR_CONVOCATORIA_SIN_FONDO_IMAGEN_EUROPEO = "Existe algún fondo europeo seleccionado en la convocatoria que no contiene archivo de imagen. Configurarlo en el menú Imágenes.";

        public static final String ERROR_NO_SE_FINALIZO_CONVOCATORIA = "Error no se pudo finalizar la convocatoria validación de imágen";

        public static final String ERROR_FONDOS_EUROPEOS = "No existen fondos europeos para la convocatoria";

        public static final String ERROR_FECHA_FIN_ALEGACIONES_NULL_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA = "No se puede subir el documento de Propuesta de resolución definitiva. Ingresar una fecha de alegación";
        
        public static final String ERROR_SIN_ALEGACIONES_NULL_SUBIR_DOCUMENTO_RESOLUCION_DEFINITIVA = "No se puede subir el documento de Propuesta de resolución definitiva. La convocatoria esta configurada sin Alegaciones";

        public static final String ERROR_CANTIDAD_MAXIMA_PERMITIDA_ALEGACIONES = "Error el texto ingresado supera los 2600 carácteres";
        
        public static final String ERROR_ESTADO_REPRESENTANTE_LEGAL = "Error no puede acceder por encontrarse en el estado ";
        
        public static final String ERROR_FECHA_VIGENCIA_REPRESENTANTE_LEGAL ="Error no puede acceder por estar fuera de vigencia, las fechas de vigencia: ";
        
    }

    /**
     *
     * Constantes para Roles.
     */
    public static final class Rol {

        /**
         *
         */
        protected Rol() {
        }

        /** Constante rol SUPER ADMINISTRADOR */
        public static final String SUPER_ADMINISTRADOR = "ROLE_1";

        /** Constante mensaje SUPER ADMINISTRADOR */
        public static final String SUPER_ADMINISTRADOR_DS = "Super Administrador";

        /** Constante rol ADMINISTRADOR */
        public static final String ADMINISTRADOR = "ROLE_2";

        /** Constante mensaje ADMINISTRADOR */
        public static final String ADMINISTRADOR_DS = "Administrador";

        /** Constante rol SUPERVISOR */
        public static final String SUPERVISOR = "ROLE_3";

        /** Constante mensaje SUPERVISOR */
        public static final String SUPERVISOR_DS = "Supervisor";

        /** Constante rol GESTOR */
        public static final String GESTOR = "ROLE_4";

        /** Constante mensaje GESTOR */
        public static final String GESTOR_DS = "Gestor";

        /** Constante rol AUDITOR */
        public static final String AUDITOR = "ROLE_5";

        /** Constante mensaje AUDITOR */
        public static final String AUDITOR_DS = "Auditor";

        /** Constante rol GESTOR_EXTERNO */
        public static final String GESTOR_EXTERNO = "ROLE_6";

        /** Constante mensaje GESTOR_EXTERNO */
        public static final String GESTOR_EXTERNO_DS = "Gestor (Externo)";
    }

    /**
     *
     * Constantes para Los Niveles Organizativos.
     */
    public static final class NivelOrganizativo {

        /**
         *
         */
        protected NivelOrganizativo() {

        }

        /** Constante mensaje DIRECCION */
        public static final Integer DIRECCION = 2;

        /** Constante mensaje DIRECCION */
        public static final String DIRECCION_DS = "Direccion";

        /** Constante mensaje SUBDIRECCION */
        public static final Integer SUBDIRECCION = 3;

        /** Constante mensaje SUBDIRECCION */
        public static final String SUBDIRECCION_DS = "SubDireccion";

        /** Constante mensaje UNIDADADMIN */
        public static final Integer UNIDADADMIN = 4;

        /** Constante mensaje UNIDADADMIN */
        public static final String UNIDADADMIN_DS = "Unidad Administrativa";

        /** Constante mensaje UNIDADADMIN */
        public static final String CD_CONSEJERIA = "C0900";
    }

    /**
     *
     * Constantes para AfirmacionNegacion.
     */
    public static final class AfirmacionNegacion {

        /**
         *
         */
        protected AfirmacionNegacion() {
        }

        /** Constante mensaje S */
        public static final String S = "S";

        /** Constante mensaje N */
        public static final String N = "N";
    }

    /**
     *
     * Constantes para Filtros.
     */
    public static final class Filtro {

        /**
         *
         */
        protected Filtro() {
        }

        public static final String FILTRO_CONVOCATORIA_EUR_ID_FONDO_EUROPEO = "fondoEuropeo.idValor";

        /** Constante filtro cdFirmante */
        public static final String FILTRO_CD_PLANTILLA = "cdPlantilla";

        /** Constante filtro cdFirmante */
        public static final String FILTRO_CD_CODIGO_FIRMANTE = "cdFirmante";

        /** Constante filtro cdFirmante */
        public static final String FILTRO_CD_CODIGO_VERIFICADOR = "cdVerificador";

        /** Constante filtro idConvocatoria */
        public static final String FILTRO_IDCONVOCATORIA = "idConvocatoria";
        
        /** Constante filtro idConvocatoriaArea */
        public static final String FILTRO_IDCONVOCATORIA_AREA = "idConvocatoriaArea";
        
        /** Constante filtro idConvocatoria */
        public static final String FILTRO_IDCAUSA= "causaEval";
        
        /** Constante filtro evaluador.idInteresado */
        public static final String FILTRO_IDEVALUADOR_IDINTERESADO = "evaluador.idInteresado";
   
        /** Constante filtro  */
        public static final String FILTRO_FC_ENVIO_PTFR = "fcEnvioPTFR";
        
        
        /** Constante filtro  */
        public static final String FILTRO_FC_ENVIO_NOTE = "fcEnvioNote";
        
        /** Constante filtro  */
        public static final String FILTRO_CD_ESTADO_PTFR = "cdEstadoPTFR";
        
        /** Constante filtro  */
        public static final String FILTRO_CD_ESTADO_NOTE = "cdEstadoNote";

        /** Constante filtro idConvocatoriaCoor */
        public static final String FILTRO_IDCONVOCATORIA_COOR = "idConvocatoriaCoor";

        /** Constante filtro idTramite */
        public static final String FILTRO_IDTRAMITE = "tramite";

        /** Constante filtro convocatoria.idConvocatoria */
        public static final String FILTRO_CONVOCATORIA_IDCONVOCATORIA = "convocatoria.idConvocatoria";

        /** Constante filtro convocatoriaAct.idActor" */
        public static final String FILTRO_CONVOCATORIA_ACT_IDACTOR = "convocatoriaAct.idActor";
        
        /** Constante filtro convocatoriaAct.fcBajaFila" */
        public static final String FILTRO_CONVOCATORIA_ACT_FCBAJA = "convocatoriaAct.fcBajaFila";
        
        public static final String FILTRO_EXPEDIENTE_DOC_FCBAJA = "expedienteDoc.fcBajaFila";
        
        /** Constante filtro convocatoriaAct.itContrato" */
        public static final String FILTRO_CONVOCATORIA_ACT_ITCONTRATO = "convocatoriaAct.itContrato";

        /* Constante giltro convocaotiraAct.tipoActor.idValor */
        public static final String FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR = "convocatoriaAct.tipoActor.idValor";

        /** Constante filtro idSolicitud */
        public static final String FILTRO_IDSOLICITUD = "idSolicitud";
        
        /** Constante filtro solicitud.idSolicitud */
        public static final String FILTRO_SOLICITUD_IDSOLICITUD = "solicitud.idSolicitud";
        
        public static final String FILTRO_SOLICITUD_IDCONVOCATORIA ="solicitud.convocatoria.idConvocatoria";
        

        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_IDEXPEDIENTE = "idExpediente";
        
        /** Constante del nombre del campo idModificacion */
        public static final String FILTRO_IDMODIFICACION = "idModificacion";
        
        /** Constante del nombre del campo idExpedienteModifPresPlazoOtra */
        public static final String FILTRO_IDEXPEDIENTEMODIFPRESPLAZOOTRA = "idExpedienteModifPresPlazoOtra";
        
        public static final String FILTRO_ID_EXPEDIENTE_MODIF_PRESUP_MONTO= "idExpedienteModifPresupMonto";
        
        public static final String FILTRO_ID_EXPEDIENTE_MODIF_PLAZO = "idExpedienteModifPlazo";
        
        public static final String FILTRO_ID_EXPEDIENTE_MODIF_OTRA = "idExpedienteModifOtra";
        
        public static final String FILTRO_MODIFPRESPLAZOOTRA_ID_EXPEDIENTE = "expedienteModifPresPlazoOtra.expediente.idExpediente";
        
        public static final String FILTRO_MODIF_OTRA_ID_ESTADO = "expedienteModifPresPlazoOtra.estado.idValor";
        
        public static final String FILTRO_MODIF_PRESPLAZO_OTRA_ID_ESTADO = "estado.idValor";
        
        
        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_IDEXPEDIENTE_OBJ = "expediente.idExpediente";
        
        
        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_IDEXPEDIENTE_CONTRATO_OBJ = "idExpedienteContrato.idExpedienteContrato"; 
        
        public static final String FILTRO_EXPEDIENTE_CONTRATO_OBJ = "expedienteContrato.idExpedienteContrato";
        
        public static final String FILTRO_EXPEDIENTE_CONTRATO_IDEXPEDIENTE = "expedienteContrato.idExpediente";
        
        /** Constante filtro idExpedientePadre */
        public static final String FILTRO_ID_EXPEDIENTE_PADRE = "idExpedientePadre";
        
        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_USARIO_IDUSUARIO = "usuario.idUsuario";


        /** Constante del nombre del campo idBloqueComponente */
        public static final String FILTRO_IDBLOQUECOMPONENTE = "idBloqueComponente";
        
        /** Constante del nombre del campo bloqueComponente.idBloqueComponente */
        public static final String FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE = "bloqueComponente.idBloqueComponente";

        /** Constante del nombre del campo idColumna */
        public static final String FILTRO_IT_COLUMNA = "itColumna";

        public static final String FILTRO_ID_TEMATICA = "idTematica";
        
        /** Constante del nombre del campo idDocJusTec */
        // public static final String FILTRO_IT_DOC_JUSTEC = "itDocJusTec";

        /** Constante del nombre del campo idExpedienteDoc */
        public static final String FILTRO_IDEXPEDIENTEDOC = "idExpedienteDoc";

        /** Constante filtro idInteresado */
        public static final String FILTRO_IDINTERESADO = "idInteresado";

        /** Constante filtro evaluador idInteresado */
        public static final String FILTRO_EVAL_IDINTERESADO = "evaluador.idInteresado";

        /** Constante filtro cdNumIden */
        public static final String FILTRO_CDNUMIDEN = "cdNumIden";

        /** Constante filtro cdUsuario */
        public static final String FILTRO_CDUSUARIO = "cdUsuario";

        /** Constante filtro interesado.idInteresado */
        public static final String FILTRO_INTERESADO_IDINTERESADO = "interesado.idInteresado";
        
        /** Constante filtro solicitud.interesado.idInteresado */
        public static final String FILTRO_SOLICITUD_INTERESADO_IDINTERESADO = "solicitud.interesado.idInteresado";
        
        /** Constante filtro solicitud.interesado.dsNombre */
        public static final String FILTRO_SOLICITUD_INTERESADO_DSNOMBRE = "solicitud.interesado.dsNombre";
        
        /** Constante filtro interesado.dsNombre */
        public static final String FILTRO_INTERESADO_DSNOMBRE = "interesado.dsNombre";

        /** Constante filtro interesado.cdNumIden */
        public static final String FILTRO_INTERESADO_CDNUMIDEN = "interesado.cdNumIden";

        /** Constante filtro persona.cdNumIden */
        public static final String FILTRO_PERSONA_CDNUMIDEN = "persona.cdNumIden";

        /** Constante filtro entidad.idInteresado */
        public static final String FILTRO_ENTIDAD_IDINTERESADO = "entidad.idInteresado";

        /** Constante filtro entidad.interesado.idInteresado */
        public static final String FILTRO_ENTIDAD_INTERESADO_IDINTERESADO = "entidad.interesado.idInteresado";

        /** Constante filtro entidad.interesado.cdNumIden */
        public static final String FILTRO_ENTIDAD_CDNUMIDEN = "entidad.interesado.cdNumIden";

        /** Constante filtro catalogo.idCatalogo */
        public static final String FILTRO_CATALOGO_IDCATALOGO = "catalogo.idCatalogo";

        /** Constante filtro catalogo.idCatalogo */
        public static final String FILTRO_CATALOGO_NMCONSTANTE = "catalogo.nmConstante";

        /** Constante filtro estado.idValor */
        public static final String FILTRO_ESTADO_IDVALOR = "estado.idValor";
        
        
        /** Constante filtro estado.idValor */
        public static final String FILTRO_ESTADO_IDVALOR_PROVISIONAL = "estadoProvisional.idValor";

        /** Constante filtro idEstado.idValor */
        public static final String FILTRO_ID_ESTADO_IDVALOR = "idEstado.idValor";

        /** Constante filtro idActorSolicitante */
        public static final String FILTRO_IDACTOR_SOLICITANTE = "idActorSolicitante";

        /** Constante filtro idActorCoordinador */
        public static final String FILTRO_IDACTOR_COORDINADOR = "idActorCoordinador";

        /** Constante del nombre del campo expediente.idExpediente */
        public static final String FILTRO_EXPEDIENTE_IDEXPEDIENTE = "expediente.idExpediente";
        
        /** Constante del nombre del campo expediente.solicitud.idSolicitud */
        public static final String FILTRO_EXPEDIENTE_SOLICITUD_IDSOLICITUD = "expediente.solicitud.idSolicitud";
        
        public static final String FILTRO_EXPEDIENTE_EXPEDIENTE_PADRE = "expediente.idExpedientePadre";

        /** Constante del nombre del campo cdEreg (p.ej. NotificacionCiudadano) */
        public static final String FILTRO_CDEREG = "cdEreg";

        /** Constante del nombre del campo asunto.idValor (NotificacionCiudadano) */
        public static final String FILTRO_NOTIFICACION_CIUDADANO_ID_ASUNTO = "asunto.idValor";

        /** Constante del nombre del campo notificacionCiudadano.idNotificacion (NotificacionCiudadanoDoc) */
        public static final String FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION = "notificacionCiudadano.idNotificacion";

        /** Constante del nombre del campo estadoRevAdmin.idValor */
        public static final String FILTRO_EXPEDIENTE_ESTADO_REV_ADMIN = "estadoRevAdmin.idValor";

        /** Constante del nombre del campo estadoRevRequi.idValor */
        public static final String FILTRO_EXPEDIENTE_ESTADO_REV_REQUI = "estadoRevRequi.idValor";

        /** Constante del nombre del campo estadoRevEval.idValor */
        public static final String FILTRO_EXPEDIENTE_ESTADO_REV_EVAL = "estadoRevEval.idValor";

        /** Constante del nombre del campo estadoRevEval.idValor */
        public static final String FILTRO_EXPEDIENTE_ESTADO_REV_JUSTEC = "estadoRevJusTec.idValor";
        
        /** Constante del nombre del campo estadoRevModif.idValor */
        public static final String FILTRO_EXPEDIENTE_ESTADO_REV_MODIF_ACTAS = "estadoRevModifActas.idValor";
        
        /** Constante del nombre del campo estadoRevEval.idValor */
        public static final String FILTRO_NM_FILA = "nmFila";

        /** Constante del nombre del campo nm_columna*/
        public static final String FILTRO_NM_COLUMNA = "nmColumna";
        
        /** Constante del nombre del campo idDato */
        public static final String FILTRO_ID_DATO = "idDato";
        
        /** Constante del nombre del campo idDato */
        public static final String FILTRO_DATO_ID_DATO = "dato.idDato";

        /** Constante filtro nmAno */
        public static final String NM_ANO = "nmAno";

        /** Constante filtro idNivelOrg.idNivelOrg */
        public static final String FILTRO_IDNIVELORG_IDNIVELORG = "nivelOrg.idNivelOrg";

        /** Constante filtro idSubdireccion.cdunidad.dsexten */
        // public static final String FILTRO_UNIDAD_CDUNIDAD =
        // "unidad.cdUnidad";

        /** Constante filtro idSubdireccion.cdunidad.dsexten */
        // public static final String FILTRO_CDUNIDAD_DSEXTEN =
        // "unidad.dsExten";

        /** Constante filtro idSubdireccion.cdunidad.dsexten */
        // public static final String FILTRO_UNIDADPADRE_CDUNIDAD =
        // "unidadPadre.cdUnidad";

        /** Constante filtro idSubdireccion.cdunidad.dsexten */
        // public static final String FILTRO_CDUNIDADPADRE_DSEXTEN =
        // "unidadPadre.dsExten";

        /** Constante filtro cdUnidad */
        public static final String FILTRO_CDUNIDADPADRE = "cdUnidadPadre";

        public static final String FILTRO_DSUNIDAD = "dsUnidad";

        /** Constante filtro cdUnidad */
        public static final String FILTRO_CDUNIDAD = "cdUnidad";

        public static final String FILTRO_ITNOTE = "itNote";

        /** Constante filtro fcAltaFila */
        public static final String FILTRO_FCALTA = "fcAltaFila";

        /** Constante filtro fcBajaFila */
        public static final String FILTRO_FCBAJA = "fcBajaFila";
        
        /** Constante filtro fcFirma */
        public static final String FILTRO_FCFIRMA = "fcFirma";
        
        /** Constante filtro nmNota */
        public static final String FILTRO_NMNOTA = "nmNota";
        
        public static final String FILTRO_FCINIEJECUCION = "fcIniEjecucion";
        
        public static final String FILTRO_FCFINEJECUCION = "fcFinEjecucion";
        
        public static final String FILTRO_FCINIJUSTICIENTEC = "fcIniJustiCienTec";
        
        public static final String FILTRO_FCFINJUSTICIENTEC = "fcFinJustiCienTec";
        
        public static final String FILTRO_FCINIJUSTIECONOMICA = "fcIniJustiEconomica";
        
        public static final String FILTRO_FCFINJUSTIECONOMICA = "fcFinJustiEconomica";

        public static final String FILTRO_IMAGENLOGO_IDIMAGEN = "imagenLogo.idImagen";

        /** Constante filtro fcLecturaNote */
        public static final String FILTRO_FCLECTURA_NOTE = "fcLecturaNote";

        public static final String FILTRO_CONVOCATORIA_CONVOCATORIA = "idConvocatoria";
        
        public static final String FILTRO_ID_DOC_PTFR_PADRE = "idDocPtfrPadre";
        
        public static final String FILTRO_ENVIO_TRAMITE = "dsTramite";

        public static final String FILTRO_IMPRESO = "impreso.idImpreso";
        
        public static final String FILTRO_ID_IMPRESO = "idImpreso";

        /** Constante Tipo identificacion actor */
        public static final String FILTRO_IDENTIFICACION = "identificacion.idValor";

        /** Constante Tipo actor */
        public static final String FILTRO_TIPO_ACTOR = "tipoActor.idValor";

        public static final String FILTRO_ID_COMPONENTE = "idComponente";

        public static final String FILTRO_ID_TIPO_FINANCIACION = "idTipoFinanciacion";

        /** constante componente.idComponente */
        public static final String ID_COMPONENTE = "componente.idComponente";
        
        /** constantes bloqueComponente.idComponente" **/
        public static final String FILTRO_BLOQUECOMPONENTE_COMPONENTE_IDCOMPONENTE = "bloqueComponente.componente.idComponente";
        
        /** constante componente.fcBaja **/
        public static final String FILTRO_BLOQUECOMPONENTE_FCBAJAFILA = "bloqueComponente.fcBajaFila";

        /** constante idComponente */
        public static final String ID_COMP = "idComponente";

        /** constante bloque */
        public static final String ID_BLOQUE = "bloque.idBloque";

        /** constante de expediente idProvisional */
        public static final String ID_PROVISIONAL = "idProvisional";

        /** constante de expediente itAlegaciones */
        public static final String IT_ALEGACIONES = "itAlegaciones";

        /** Constante del nombre del campo cdCodigo de Catalogo Valores */
        public static final String FILTRO_CATALOGO_VALOR_CDCODIGO = "cdCodigo";
        
        /** Constante del nombre del campo cdCodigo de Catalogo Valores */
        public static final String FILTRO_CATALOGO_VALOR_NMCONSTANTE = "nmConstante";

        /** Constante del nombre del campo dsValor de Catalogo Valores */
        public static final String FILTRO_CATALOGO_VALOR_DSVALOR = "dsValor";

        /** Constante del nombre del campo idValor de Catalogo Valores */
        public static final String FILTRO_COMPONENTE_TIPO_DOCUMENTO = "tipoDocumento.idValor";
        
        /** Constante del nombre del campo dsValor de Catalogo Valores */
        public static final String FILTRO_COMPONENTE_TIPO_DOCUMENTO_DSVALOR = "tipoDocumento.dsValor";

        /** Constante del nombre del campo tipo.idValor en Componentes */
        public static final String FILTRO_COMPONENTE_TIPO_IDVALOR = "tipo.idValor";

        /** Constante del nombre del campo nmOrden en Bloque Componente */
        public static final String FILTRO_NMORDEN = "nmOrden";
        
        /** Constante del nombre del campo nmOrden en Bloque Componente */
        public static final String FILTRO_NMSUBORDEN = "subNmOrden";

        /** Constante del nombre del campo dsValor de Catalogo Valores */
        public static final String FILTRO_BLOQUE_COMPONENTE_TIPO_DOCUMENTO = "componente.tipoDocumento.idValor";

        /** constante para filtro categoria.dsValor */
        public static final String FILTRO_CATEGORIA_DSVALOR = "categoria.dsValor";

        /** constante para filtro categoria.idValor */
        public static final String FILTRO_CATEGORIA_IDVALOR = "categoria.idValor";

        /** constante para filtro dsNombre */
        public static final String FILTRO_DSNOMBRE = "dsNombre";

        /** constante para filtro cdAcronimo */
        public static final String FILTRO_CDACRONIMO = "cdAcronimo";

        /** constante para filtro idValor de causa de denegacion de catálogo */
        public static final String FILTRO_CATALOGO_ID_CAUSA_DENEGACION = "causa.idValor";
        
        /** constante para filtro nmConstante de causa de denegacion de catálogo */
        public static final String FILTRO_CATALOGO_NM_CONSTANTE = "causa.nmConstante";

        /**
         * constante para filtro idValor de causa de denegacion de evaluacion de
         * catálogo
         */
        public static final String FILTRO_CATALOGO_ID_CAUSA_DENEGACION_EVAL = "causaEval.idValor";
        
        public static final String FILTRO_CATALOGO_ID_CAUSA_DENEGACION_CONT = "causaCont.idValor";

        public static final String FILTRO_CATALOGO_ID_REQUISITO = "requisito.idValor";

        /** Constantes para filtro dsApellido1 */
        public static final String FILTRO_DSAPELLIDO1 = "dsApellido1";

        /** Constantes para filtro dsApellido2 */
        public static final String FILTRO_DSAPELLIDO2 = "dsApellido2";

        /** Constantes para filtro itCoorEval */
        public static final String FILTRO_ITCOOREVAL = "itCoorEval";

        public static final String FILTRO_ITTIPOEVAL = "itTipoEval";

        /** Constantes para filtro persona.idInteresado */
        public static final String FILTRO_PERSONA_IDINTERESADO = "persona.idInteresado";
        
        /** Constantes para filtro idNivel.idValor */
        public static final String FILTRO_ID_NIVEL = "idNivel.idValor";

        /** Constantes para filtro idNivel.nmConstante */
        public static final String FILTRO_NM_CONSTANTE_NIVEL = "idNivel.nmConstante";
        
        /** Constantes para filtro itCoorEval */
        public static final String FILTRO_ID_EXP_DOC_EVAL = "idExpedienteEval.idExpedienteEval";

        /** Constantes para filtro idArea */
        public static final String FILTRO_IDAREA = "area.idValor";

        /** Constantes para filtro itCoorEval */
        public static final String FILTRO_ITNOTIFICADO = "itNotificado";

        public static final String FILTRO_INTERESADO_AREA_IDVALOR = "listaPersonaArea.area.idValor";
        
        

        /**
         * constante para filtro idValor de tipo de documento de documento de
         * catálogo
         */
        public static final String FILTRO_CATALOGO_ID_TIPO_DOCUMENTO = "tipoDocumento.idValor";

        public static final String FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO = "idTipoDocumento.idValor";
        
        public static final String FILTRO_CONVOCATORIA_DOC_DS_TIPO_DOCUMENTO = "idTipoDocumento.dsValor";

        public static final String FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC = "expedienteDoc.idExpedienteDoc";
        
        public static final String FILTRO_EXPEDIENTEDOC_EXPEDIENTE_IDEXPEDIENTE = "expedienteDoc.expediente.idExpediente";
        
        public static final String FILTRO_EXPEDIENTE_DOC_ID_TIPO_DOCUMENTO = "expedienteDoc.tipoDocumentoDTO.idValor";
        
        public static final String FILTRO_EXPEDIENTE_DOC_ID_ESTADO = "expedienteDoc.estado.idValor";
        
        public static final String FILTRO_EXPEDIENTE_DOC_ID_TRAMITE = "expedienteDoc.tramite.idValor";

        public static final String FILTRO_FECHA_ALTA = "fcAltaFila";
        
        public static final String FILTRO_ID_UNIDAD_ADMIN = "unidadAdmin.idUnidadAdmin";

        public static final String FILTRO_ID_CONVOCATORIA_PLA = "idConvocatoriaPla";
        
        public static final String FILTRO_ID_CONVOCATORIA_DOC_EVAL = "idConvocatoriaDocEval";

        public static final String FILTRO_NMANUALIDAD = "nmAnualidad";
        
        public static final String FILTRO_NMPERIODO = "nmPeriodo";

        public static final String FILTRO_ID_CONVOCATORIA = "idConvocatoria";

        public static final String FILTRO_CONVOCATORIA_FC_INI_PRESENTACION = "fcIniPresentacion";

        public static final String FILTRO_CONVOCATORIA_FC_FIN_PRESENTACION = "fcFinPresentacion";
        
        public static final String FILTRO_CONVOCATORIA_ACTIVA = "itConvocatoriaActiva";

        public static final String FILTRO_TIPO_DOCUMENTO_CDPLANTILLA = "cdPlantilla";

        public static final String FILTRO_CONVOCATORIA_NM_DIAS_SUBSANACION = "nmDiasSubsanacion";

        public static final String FILTRO_DOCUMENTO_PTFR_FC_ENVIO_NOTE = "fcEnvioNote";

        public static final String FILTRO_DOCUMENTO_PTFR_FC_ENVIO_CORREO = "fcEnvioCorreo";

        public static final String FILTRO_DOCUMENTO_PTFR_FC_ENVIO_PTFR = "fcEnvioPtfr";

        public static final String FILTRO_DOCUMENTO_PTFR_FC_LECTURA_NOTE = "fcLecturaNote";

        public static final String FILTRO_DOCUMENTO_PTFR_CD_ESTADO_PTFR = "cdEstadoPtfr";

        public static final String FILTRO_DOCUMENTO_PTFR_IDDOCUMENTOPTFR = "documentoPtfr.idDocumentoPtfr";

        public static final String FILTRO_IDDOCUMENTOPTFR = "idDocumentoPtfr";
        
        /** Con mayúscula las letras PTFR para que sea reconocido por la vista SAII_V_ENVIO_NOTIFICACION  **/
        public static final String FILTRO_ID_DOCUMENTO_PTFR = "idDocumentoPTFR";

        public static final String FILTRO_CATALOGO_ID_AREA_CIENTIFICA = "area.idValor";
        
        public static final String FILTRO_CATALOGO_ID_SUBAREA_CIENTIFICA = "subarea.idValor";

        public static final String FILTRO_CONVOCATORIA_SUBMOD_ID_CONV_SUBMODALIDAD = "convocatoriaSubmod.idConvocatoriaSubmod";

        public static final String FILTRO_CONVOCATORIA_SUBMOD_DSSUBMODALIDAD = "dsSubmodalidad";

        public static final String FILTRO_ITCUMPLE = "itCumple";

        public static final String FILTRO_ID_EXPEDIENTE_DOC = "idExpedienteDoc";
        
        public static final String FILTRO_EXPEDIENTE_DOC_ID_DOCUMENTOPTFR = "expedienteDoc.idDocumentoPtfr";

        public static final String FILTRO_IDEVALUACION = "idEvaluacion";

        public static final String FILTRO_IDEVALUACION_JUSTEC = "idEvaluacionJustec";

        public static final String FILTRO_NMDE = "nmDe";

        /** Constantes para filtro OR */
        public static final String FILTRO_OR = "OR";

        /** Constantes para filtro cdFica */
        public static final String FILTRO_CDFICA = "cdFica";

        /** Constantes para filtro cdAnexoEreg */
        public static final String FILTRO_CDANEXOEREG = "cdAnexoEreg";

        /** Constantes para filtro cdAnexoEregPadre */
        public static final String FILTRO_CDANEXOEREGPADRE = "cdAnexoEregPadre";

        /** Constantes para filtro cdAnexoEregPadre */
        public static final String FILTRO_IMPRESO_CONVOC_ACT_IDENTIFICACION_IDVALOR = "impreso.convocatoriaAct.identificacion.idValor";

        /** Constante para filtro itMantenible */
        public static final String FILTRO_IT_MANTENIBLE = "itMantenible";

        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_IDEXPEDIENTEEVAL = "idExpedienteEval"; 
        
        /** Constante del nombre del campo idExpediente */
        public static final String FILTRO_IDEXPEDIENTEEVAL_OBJETO = "idExpedienteEval.idExpedienteEval"; 

        /** Constante del nombre del campo idExpedienteEvalPadre en la entidad expedienteEval */
        public static final String FILTRO_IDEXPEDIENTEEVAL_PADRE = "idExpedienteEvalPadre";

        /** Constante del nombre del campo idExpedienteEvalJusTec en la entidad expedienteEvalJusTec */
        public static final String FILTRO_IDEXPEDIENTEEVALJUSTEC = "idExpedienteEvalJusTec";
        
        /** Constante del nombre del campo idExpedienteEvalJustec en la entidad apartadoJustecDto */
        public static final String FILTRO_IDEXPEDIENTE_APARTADO_JTC_DATO = "idExpedienteEvalJustec";
        
        
        public static final String FILTRO_EXPEDIENTE_EVAL_JUSTEC_ID_EXPEDIENTE = "expedienteEvalJusTec.idExpedienteEvalJusTec";
        
        /** Constante para filtro itMantenible */
        public static final String FILTRO_ID_APARTADO = "idApartado";

        public static final String FILTRO_ID_EVALUACION_JUSTEC_APARTA = "idEvaluacionJustecAparta";

        /** Constante para filtro itMantenible */
        public static final String FILTRO_ID_EVALUACION_NOTA_DATO = "idEvaluacionNotaDato"; 

        public static final String FILTRO_ID_EVALUACION_JUSTEC_NOTA_DATO = "idEvaluacionJustecNotaDato";
        
        /** Constante para filtro impreso.convocatoriaAct.idActor */
        public static final String FILTRO_IMPRESO_ID_ACTOR = "impreso.convocatoriaAct.idActor";

        /**
         * Constante para filtro impreso.convocatoriaAct.tipoActor.idValor
         */
        public static final String FILTRO_IMPRESO_TIPO_ACTOR = "impreso.convocatoriaAct.tipoActor.idValor";

        public static final String FILTRO_IMPRESO_CONVACT_IDACTORSOLICITANTE = "impreso.convocatoriaAct.idActorSolicitante";

        public static final String FILTRO_IMPRESO_CONVACT_IDACTORCOORDINADOR = "impreso.convocatoriaAct.idActorCoordinador";

        /** Constante para filtro fondo europeo saii_imagen */
        public static final String FILTRO_IMAGEN_FONDO_EUROPEO = "fondoEur.idValor";

        public static final String MOTIVO_RECHAZO_VACIO = "convocatorias.detalle.solicitudes.expedientes.documentos.motivo.rechazo.vacio";

        public static final String FILTRO_IMAGEN_CDUNIDAD = "cdUnidad";

        public static final String FILTRO_TRAMITE = "tramite.idValor";
        
        public static final String FILTRO_CONVOCATORIA_ACT_CORREO_ID_ACTOR = "convocatoriaActCorreo.idActor";
        
        public static final String FILTRO_CONVOCATORIA_ACT_NOTE_ID_ACTOR = "convocatoriaActNote.idActor";

        public static final String FILTRO_ITCORREO = "itCorreo";

        /** Constante filtro itContrato */
        public static final String FILTRO_ITCONTRATO = "itContrato";
        
        public static final String FILTRO_ITPARTIDASPRESUP = "itPartidasPresupuestarias";
        
        public static final String FILTRO_ITDETALLEJUSECO = "itDetalleJusEco";

        public static final String FILTRO_IDVALOR = "idValor";
        
        public static final String FILTRO_NMCONSTANTE = "nmConstante";
        
        public static final String FILTRO_IDCATALOGO = "idCatalogo";

        /* Filtro temática */
        public static final String FILTRO_TEMATICA = "tematica.idValor";
        
        /* Filtro idActor */
        public static final String FILTRO_ID_ACTOR = "idActor.idActor";

        /* Filtro financiacion tipo */
        public static final String FILTRO_IT_TIPO = "itTipo";

        /** Constante para filtro configuración financiación */
        public static final String FILTRO_ID_FINANCIACION = "idFinanciacion";

        public static final String FILTRO_FINANCIACION_TIPO = "tipo.idValor";

        public static final String FILTRO_FINANCIACION_IT_TIPO = "itTipo";

        public static final String FILTRO_FINANCIACION_ANUALIDAD = "nmAnualidad";

        public static final String FILTRO_ID_CONVOCATORIA_JUSTEC = "idConvocatoriaJusTec";
        
        public static final String FILTRO_ID_CONVOCATORIA_JUSECO = "idConvocatoriaJusEco";
        
        public static final String FILTRO_ID_CONVOCATORIA_ANUALIDAD = "idConvocatoriaAnualidad";

        public static final String FILTRO_ID_EXPEDIENTE_JUSTEC = "idExpedienteJusTec";
        
        public static final String FILTRO_ID_EXPEDIENTE_JUSTEC_HIST = "idExpedienteJusTecHist";
        
        public static final String FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE = "expedienteJusTec.idExpedienteJusTec";
        
        public static final String FILTRO_EXPEDIENTE_JUSTEC_IDEXPEDIENTE_JUSTEC = "expedienteJustec.idExpedienteJusTec";
        
        public static final String FILTRO_EXPEDIENTE_JUSECO_IDEXPEDIENTE_JUSECO = "expedienteJusEco.idExpedienteJusEco";
        
        public static final String FILTRO_EXPEDIENTE_JUSECO_IDEXPEDIENTE = "expedienteJusEco.idExpediente";
        
        public static final String FILTRO_ID_EXPEDIENTE_JUSECO = "idExpedienteJusEco";
        
        public static final String FILTRO_ID_EXPEDIENTE_JUSECO_DOC = "idExpedienteJusEcoDoc";
        
        public static final String FILTRO_ID_EXPEDIENTE_JUSECO_HIST = "idExpedienteJusEcoHist";
        
        public static final String FILTRO_ID_EXPEDIENTE_ANUALIDAD = "idExpedienteAnualidad";
        
        public static final String FILTRO_ID_EXPEDIENTE_ANUALIDAD_HIST = "idExpedienteAnualidadHist";

        /* Constantes para expediente presupuesto */
        public static final String FILTRO_EXPEDIENTE_PRESU_TIPO_FINANCIACION = "tipoFinanciacion.idValor";
        
        public static final String FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF = "expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra";

        /* Filtro sobre ExpedienteDoc por la modificación asociada a través del dato (concepto de presupuesto) */
        public static final String FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF_DATO = "dato.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra";

        /* Constantes para expediente presupuesto */
        public static final String FILTRO_CD_EXPEDIENTE = "cdExpediente";
        
        /* Constantes para expediente solicitud */
        public static final String FILTRO_CD_EXPEDIENTE_SOL = "cdExpedienteSol";

        /* Constantes para expediente actuaciones */
        public static final String FILTRO_DS_PROVEEDOR = "dsProveedor";

        /* Constantes para expediente actuaciones */
        public static final String FILTRO_NM_IMPORTE = "nmImporte";

        /* Constantes para expediente actuaciones */
        public static final String FILTRO_NM_AYUDA = "nmAyuda";

        /* Constantes para expediente presupuesto */
        public static final String FILTRO_NM_PRESUP = "nmPresup";
        
        public static final String FILTRO_ID_EXPEDIENTE_PRESUP = "idExpedientePresup";
        
        public static final String FILTRO_ID_EXPEDIENTE_PRESUP_MONTO = "idExpedientePresupMonto";
        
        public static final String FILTRO_EXPEDIENTE_PRESUP_MONTO_ID_EXP_PRES_MONTO = "expedientePresupMonto.idExpedientePresupMonto";
        
        public static final String FILTRO_ID_EXPEDIENTE_PRESUP_HIST = "idExpedientePresupHist";

        /* Constantes para tipo de documento Contrato*/
        public static final String FILTRO_DS_VALOR_CONTRATOS = "Contrato";
        
        /* Constantes para Estado de evaluador OK*/
        public static final String FILTRO_ID_ESTADO_DS_VALOR = "idEstado.dsValor";
        
        /* Constantes para Estado de evaluador OK*/
        public static final String FILTRO_DS_VALOR_EVALUADO_OK = "Evaluado Ok";
        
        /* Constantes para Estado de evaluador KO*/
        public static final String FILTRO_DS_VALOR_EVALUADO_KO = "Evaluado KO";
        
        /** Constante filtro para impresos tipo.idValor */
        public static final String FILTRO_TIPO_ID_VALOR = "tipo.idValor";
        
        /** Constante filtro para idTipoActor */
        public static final String FILTRO_ID_TIPO_ACTOR = "idTipoActor";
        
        /** Constatnte filtro para tipoTramite */
        public static final String FILTRO_TIPO_TRAMITE = "tipoTramite";
        
        /** Constatnte filtro para fcIniJustiCienTec */
        public static final String FILTRO_FECHA_INICIO_JT = "fcIniJustiCienTec";
        
        /** Constatnte filtro para fcFinJustiCienTec */
        public static final String FILTRO_FECHA_FIN_JT = "fcFinJustiCienTec"; 
        
        /** Constatnte filtro para fcIniPlazo */
        public static final String FILTRO_FECHA_INI_PLAZO = "fcIniPlazo";
        
        /** Constatnte filtro para fcFinPlazo */
        public static final String FILTRO_FECHA_FIN_PLAZO = "fcFinPlazo"; 
        
        /** Constatnte filtro para fcFinPlazo */
        public static final String FILTRO_FECHA_FIN_PLAZO_SUBSANACION = "fcFinPlazoSubsanacion";
        
        /** Constatnte filtro para fcIniJustiEconomica */
        public static final String FILTRO_FECHA_INICIO_JE = "fcIniJustiEconomica";
        
        /** Constatnte filtro para fcFinJustiEconomica */
        public static final String FILTRO_FECHA_FIN_JE = "fcFinJustiEconomica";
        
        /** Constatnte filtro para idEstado */
        public static final String FILTRO_ID_ESTADO = "idEstado.idValor"; 
        
        /** Constatnte filtro para idEstado */
        public static final String FILTRO_ID_ESTADODTO = "estadoDTO.idValor"; 
        
        /** Constatnte filtro para IdEstadoRevAdmin */
        public static final String FILTRO_ID_ESTADO_REV_ADMIN = "idEstadoRevAdmin.idValor"; 
       
        /** Constante para filtro convocatoriaAct.idActor */
        public static final String FILTRO_EXP_ID_ACTOR = "convocatoriaAct.idActor";

        /**
         * Constante para filtro convocatoriaAct.tipoActor.idValor
         */
        public static final String FILTRO_EXP_TIPO_ACTOR = "convocatoriaAct.tipoActor.idValor";
        
        public static final String FILTRO_EXP_TIPO_ACTOR_DSVALOR = "tipoActor.dsValor";

        public static final String FILTRO_EXP_CONVACT_IDACTORSOLICITANTE = "convocatoriaAct.idActorSolicitante";

        public static final String FILTRO_EXP_CONVACT_IDACTORCOORDINADOR = "convocatoriaAct.idActorCoordinador";

        /** Constantes para filtro convocatoriaAct.identificacion.idValor */
        public static final String FILTRO_EXP_CONVOC_ACT_IDENTIFICACION_IDVALOR = "convocatoriaAct.identificacion.idValor";
        
        /** Constantes para filtro convocatoriaAct.identificacion.dsValor */
        public static final String FILTRO_EXP_CONVOC_ACT_IDENTIFICACION_DSVALOR = "convocatoriaAct.identificacion.dsValor";

       /** Constante filtro para idExpedienteHisty */ 
        public static final String FILTRO_ID_EXPEDIENTE_HIST = "idExpedienteHist";
        
        /** Constante del nombre del campo idExpedienteContrato */
        public static final String FILTRO_ID_EXPEDIENTE_CONTRATO = "idExpedienteContrato";
        
        public static final String FILTRO_ID_EXPEDIENTE_CONTRATO_HIST = "idExpedienteContratoHist";
        
        /** Constante del nombre del campo idConvocatoriaCom */
        public static final String FILTRO_ID_CONVOCATORIA_COM = "idConvocatoriaCom";
        
        /** Constante del nombre del campo idBloquePadre */
        public static final String FILTRO_ID_BLOQUE_PADRE = "idBloquePadre";
        
        /** Constante del nombre del campo idBloque */
        public static final String FILTRO_ID_BLOQUE = "idBloque";

        
        public static final String FILTRO_ID_MODALIDAD = "idModalidad.idValor";  
        
        public static final String FILTRO_CODIGO_EREG = "codigoEreg";
        
        public static final String FILTRO_EXPEDIENTE_JUSECO_FCBAJA = "expedienteJusEco.fcBajaFila";
        
        public static final String FILTRO_TIPO_CAUSA_DENEGACION_IDVALOR = "tipoCausa.idValor";
        
        public static final String FILTRO_CAUSADTO_DSVALOR = "causa.dsValor";
        
        public static final String FILTRO_INFORME_ID_CONVOCATORIA = "informe.idConvocatoria";
        
        public static final String FILTRO_INFORME_ID_INFORME = "informe.idInforme";
        
        public static final String FILTRO_INFORME_IMAGEN_LOGO_ID_IMAGEN = "informe.imagenLogo.idImagen";
        
        public static final String FILTRO_ID_INFORME = "idInforme";
        
        public static final String FILTRO_INFORME_TIPO_INFORME_ID_VALOR = "informe.tipoInforme.idValor";
        
        public static final String FILTRO_TIPO_INFORME_ID_VALOR = "tipoInforme.idValor";
        
        public static final String FILTRO_ID_CONVOCATORIA_REQ = "idConvocatoriaReq";
        
    }

    /**
     * Constantes para SUCA.
     */
    public static final class Suca {

        /**
         *
         */
        protected Suca() {
        }

        /**
         * Constante codigo Provincia Madrid
         */
        public static final Integer PROV_MADRID = 28;

        /**
         * Constante codigo municipio Madrid
         */
        public static final String CD_MUNI_MADRID = "079";
    }

    /**
     *
     * Constantes para Formatos.
     */
    public static final class Formatos {

        /**
         *
         */
        protected Formatos() {
        }

        /** Bloque tipo id formulario */
        public static final Integer ID_FORMULARIO = 32;

        /** Bloque tipo id listado */
        public static final Integer ID_LISTADO = 33;

        /** Bloque tipo id tabla */
        public static final Integer ID_TABLA = 34;

        /** Bloque tipo id campo de texto */
        public static final Integer ID_TEXTO = 35;

        /** Bloque tipo id campo de texto */
        public static final Integer ID_TEXTO_FIJO = 36;

        /** Bloque tipo id documento */
        public static final Integer ID_DOCUMENTO = 37;

        /** Bloque tipo id participantes */
        public static final Integer ID_PARTICIPANTES = 38;

        /** Bloque tipo id presupuesto presentado */
        public static final Integer ID_PRESUPUESTO_PRESENTADO = 190;

        /** Bloque tipo id ayuda solicitada */
        public static final Integer ID_AYUDA_SOLICITADA = 191;

        /** Bloque tipo id ayuda solicitada */
        public static final Integer ID_FINANCIACION_ACTUACIONES = 192;

        /* Tipo Bloque Financiacion Partidas Presupuestarias */
        public static final String PARTIDAS_PRESUPUESTARIAS = "PP";

        /* Tipo Bloque FInanciacion Ayuda Solicitada */
        public static final String AYUDA_SOLICITADA = "AS";

        /* Tipo Bloque FInanciacion Actuaciones*/
        public static final String FINANCIACION_ACTUACIONES = "AC";
        
        /** Bloque tipo id plazos ejecucion anualidades */
        public static final Integer ID_PLAZOS_EJEC_ANUAL = 193;
        
        /** Bloque tipo id personal con cargo */
        public static final Integer ID_PERSONAL_CON_CARGO = 194;
        
        /** Bloque tipo id personal sin cargo */
        public static final Integer ID_PERSONAL_SIN_CARGO = 195;
                
        /** Bloque tipo id presupuesto concedido o financiable */
        public static final Integer ID_PRESUPUESTO_FINANCIABLE = 197;
        
        /** Bloque tipo id formulario contrato inicial */
        public static final Integer ID_FORMULARIO_CONTRATO_INICIAL = 196; 
        
        /** Bloque tipo id formulario renuncia */
        public static final Integer ID_FORMULARIO_CONTRATO_RENUNCIA = 198; 
        
        /** Bloque tipo id formulario sustitucion */
        public static final Integer ID_FORMULARIO_CONTRATO_SUSTITUCION = 199; 
        
        /** Bloque tipo id formulario NOTIFICACIONES */
        public static final Integer ID_FORMULARIO_NOTIFICACION = 189; 
        
        
        /** Bloque tipo id formulario ID_FORMULARIO_PERSONA_SOLICITANTE */
        public static final Integer ID_FORMULARIO_PERSONA_SOLICITANTE = 188; 
        
        /** Bloque tipo id formulario NOTIFICACIONES */
        public static final Integer ID_CUENTA_BANCARIA = 39; 
        
        /** Bloque tipo id formulario ID_DISCAPACIDAD */
        public static final Integer ID_DISCAPACIDAD = 29;
        
        /** Bloque tipo id porcentaje intensidad ayuda */
        public static final Integer ID_PORCENTAJE_INTENSIDAD = 25;

    }

    /**
     * Clase que contiene los tipos de componentes
     *
     * @author everis
     *
     */
    public static final class TipoComponente {

        /**
         * Constructor de la clase
         */
        protected TipoComponente() {
        }

        /**
         * Identificador de tipo componente "CAMPO"
         */
        public static final Integer ID_CAMPO = 60;

        /**
         * Identificador de tipo componente "CHECK"
         */
        public static final Integer ID_CHECK = 61;

        /**
         * Identificador de tipo componente "RADIO"
         */
        public static final Integer ID_RADIO = 62;

        /**
         * Identificador de tipo componente "COMBO"
         */
        public static final Integer ID_COMBO = 63;

        /**
         * Identificador de tipo componente "ETIQUETA"
         */
        public static final Integer ID_ETIQUETA = 64;
        
        /** Bloque tipo id catálogo*/
        public static final Integer ID_CATALOGO = 65;

    }

    /**
     * Clase que contiene los sub-tipos de componentes
     *
     * @author everis
     *
     */
    public static final class SubTipoComponente {

        /**
         * Constructor de la clase
         */
        protected SubTipoComponente() {
        }

        /**
         * Identificador de sub tipo componente "TEXTO"
         */
        public static final Integer ID_TEXTO = 70;

        /**
         * Identificador de sub tipo componente "NUMERO"
         */
        public static final Integer ID_NUMERO = 71;

        /**
         * Identificador de sub tipo componente "FECHA"
         */
        public static final Integer ID_FECHA = 72;

        /**
         * Identificador de sub tipo componente "MULTITEXTO"
         */
        public static final Integer ID_MULTI_TEXTO = 73;
        
        
        /**
         * Identificador de sub tipo componente "TEXTO_FIJO"
         */
        public static final Integer TEXTO_FIJO = 74;
        
        /**
         * Identificador de sub tipo componente "TEXTO_FIJO"
         */
        public static final Integer MONEDA = 75;

    }

    /* Clase que contiene los componente de financiación */
    public static final class ComponentesFinanciacion {
        /**
         * Constructor de la clase
         */
        protected ComponentesFinanciacion() {
        }

        /* Componente Importe */
        public static final Integer IMPORTE = 24;

        /* Componente Código Actuación */
        public static final Integer CODIGO_ACTUACION = 25;

        /* Componente Actuación */
        public static final Integer ACTUACION = 26;

        /* Componente Oferta Proveedor 1 */
        public static final Integer OFERTA1_PROVEEDOR = 27;

        /* Componente Oferta Importe 1 */
        public static final Integer OFERTA1_IMPORTE = 28;

        /* Componente Oferta Proveedor 2 */
        public static final Integer OFERTA2_PROVEEDOR = 29;

        /* Componente Oferta Importe 2 */
        public static final Integer OFERTA2_IMPORTE = 30;

        /* Componente Oferta Proveedor 3 */
        public static final Integer OFERTA3_PROVEEDOR = 31;

        /* Componente Oferta Importe 3 */
        public static final Integer OFERTA3_IMPORTE = 32;

        /* Componente Oferta Memoria Explicativa */
        public static final Integer MEMORIAS_EXPLICATIVA = 33;

        /* Componente Oferta Proveedor Seleccionada */
        public static final Integer OFERTA_SELECCIONADA_PROVEEDOR = 34;

        /* Componente Oferta Importe Seleccionada */
        public static final Integer OFERTA_SELECCIONADA_IMPORTE = 35;

        /* Componente Importe Inversion */
        public static final Integer TOTAL_IMPORTE_INVERSION = 36;

        /* Componente Importe Ayuda */
        public static final Integer TOTAL_IMPORTE_AYUDA = 37;

        public static final String DS_MEMORIAS_EXPLICATIVAS = "Memorias explicativas";

        public static final String DS_OFERTA_IMPORTE = "Oferta seleccionada - importe (sin IVA)";
        
        /* Componente Fecha Inicio Anualidad */
        public static final Integer FECHA_INICIO = 43;
        
        /* Componente Fecha Fin Anualidad */
        public static final Integer FECHA_FIN = 44;
        
        /* Componente Anualidad*/
        public static final Integer ANUALIDAD = 45;
        
        /* Componente Borrador de acta */
        public static final Integer BORRADOR_ACTA = 46;
        
        /* Componente Acta Final */
        public static final Integer ACTA_FINAL = 47;
        
    }

    
    

    public static final class ComponenteSolicitud {

        /**
         * Constructor de la clase
         */
        protected ComponenteSolicitud() {
        }
        
        /**
         * Componente radio intereesado
         */
        public static final Integer RADIO_ENVIO_NOTIFICACION = 48;
        
        /**
         * Componente radio intereesado
         */
        public static final Integer CHECK_TEXTO = 49;
        
        /**
         * Componente radio intereesado
         */
        public static final Integer TEXTO_FIJO = 50;
        
        
        public static final Integer RADIO_ENVIO_INTERESADO = 51;
        
        public static final Integer CHECK_OPONGO = 52;
        
        public static final Integer MULTITEXTO_OPONGO = 53;
        
        public static final Integer AYUDAS_CONCURRENTES = 54;
        
        public static final Integer GRADO_DISCAPACIDAD = 55;

    }
    
    
    
    /**
     * Clase que contiene los componentes relacionables
     */
    public static final class ComponentesRelacionables {

        /**
         * Constructor de la clase
         */
        protected ComponentesRelacionables() {
        }

        /**
         * Componente TIPO_VIA
         */
        public static final Integer TIPO_VIA = 1;

        /**
         * Componente RELACION_TIPO_VIA
         */
        public static final String RELACION_TIPO_VIA = "RELACION_TIPO_VIA";

        /**
         * Componente NOMBRE_VIA
         */
        public static final Integer NOMBRE_VIA = 2;

        /**
         * Componente RELACION_NOMBRE_VIA
         */
        public static final String RELACION_NOMBRE_VIA = "RELACION_NOMBRE_VIA";

        /**
         * Componente NUMERO
         */
        public static final Integer NUMERO = 3;

        /**
         * Componente RELACION_NUMERO
         */
        public static final String RELACION_NUMERO = "RELACION_NUMERO";

        /**
         * Componente PISO
         */
        public static final Integer PISO = 4;

        /**
         * Componente RELACION_PISO
         */
        public static final String RELACION_PISO = "RELACION_PISO";

        /**
         * Componente PUERTA
         */
        public static final Integer PUERTA = 5;

        /**
         * Componente RELACION_PUERTA
         */
        public static final String RELACION_PUERTA = "RELACION_PUERTA";

        /**
         * Componente CP
         */
        public static final Integer CP = 6;

        /**
         * Componente RELACION_CP
         */
        public static final String RELACION_CP = "RELACION_CP";

        /**
         * Componente MUNICIPIO
         */
        public static final Integer MUNICIPIO = 7;

        /**
         * Componente RELACION_MUNICIPIO
         */
        public static final String RELACION_MUNICIPIO = "RELACION_MUNICIPIO";

        /**
         * Componente PROVINCIA
         */
        public static final Integer PROVINCIA = 8;

        /**
         * Componente RELACION_PROVINCIA
         */
        public static final String RELACION_PROVINCIA = "RELACION_PROVINCIA";

        /**
         * Componente NIF_NIE
         */
        public static final Integer NIF_NIE = 9;

        /**
         * Componente RELACION_NIF_NIE
         */
        public static final String RELACION_NIF_NIE = "RELACION_NIF_NIE";

        /**
         * Componente TIPO_VIA
         */
        public static final Integer RAZON_SOCIAL = 10;

        /**
         * Componente RELACION_RAZON_SOCIAL
         */
        public static final String RELACION_RAZON_SOCIAL = "RELACION_RAZON_SOCIAL";

        /**
         * Componente NOMBRE
         */
        public static final Integer NOMBRE = 11;

        /**
         * Componente NOMBRE
         */
        public static final String RELACION_NOMBRE = "RELACION_NOMBRE";

        /**
         * Componente APELLIDO_1
         */
        public static final Integer APELLIDO_1 = 12;

        /**
         * Componente RELACION_APELLIDO_1
         */
        public static final String RELACION_APELLIDO_1 = "RELACION_APELLIDO_1";

        /**
         * Componente APELLIDO_2
         */
        public static final Integer APELLIDO_2 = 13;

        /**
         * Componente RELACION_APELLIDO_2
         */
        public static final String RELACION_APELLIDO_2 = "RELACION_APELLIDO_2";

        /**
         * Componente TELEFONO_FIJO
         */
        public static final Integer TELEFONO_FIJO = 14;

        /**
         * Componente RELACION_TELEFONO_FIJO
         */
        public static final String RELACION_TELEFONO_FIJO = "RELACION_TELEFONO_FIJO";

        /**
         * Componente TELEFONO_MOVIL
         */
        public static final Integer TELEFONO_MOVIL = 15;

        /**
         * Componente RELACION_TELEFONO_MOVIL
         */
        public static final String RELACION_TELEFONO_MOVIL = "RELACION_TELEFONO_MOVIL";

        /**
         * Componente CORREO_ELECTRONICO
         */
        public static final Integer CORREO_ELECTRONICO = 16;

        /**
         * Componente RELACION_CORREO_ELECTRONICO
         */
        public static final String RELACION_CORREO_ELECTRONICO = "RELACION_CORREO_ELECTRONICO";

        /**
         * Componente CARGO
         */
        public static final Integer CARGO = 17;

        /**
         * Componente RELACION_CARGO
         */
        public static final String RELACION_CARGO = "RELACION_CARGO";
        
        
        public static final Integer FECHA_INI  = 38;

        /**
         * Componente RELACION_CARGO
         */
        public static final String RELACION_FECHA_INI = "RELACION_FECHA_INI";
        
        
        public static final Integer FECHA_FIN  = 39;

        /**
         * Componente RELACION_CARGO
         */
        public static final String RELACION_FECHA_FIN = "RELACION_FECHA_FIN";
        
        public static final Integer ES_INDEFINIDO = 40;

        /**
         * Componente RELACION_CARGO
         */
        public static final String RELACION_ES_INDEFINIDO = "RELACION_ES_INDEFINIDO";
        
        /**
         * Componente FECHA_RENUNCIA
         */
        public static final Integer FECHA_RENUNCIA = 41;
        
        /**
         * Componente INDICADOR_SUSTITUCION
         */
        public static final Integer INDICADOR_SUSTITUCION = 42;
        
    }

    /**
     *
     * Constantes para documentos anexos.
     */
    public static final class DocAnexos {

        /**
         *
         */
        protected DocAnexos() {
        }

        /** Constante Texto titulo */
        public static final String TEXTOTITULO = "APORTE DE DOCUMENTACIÓN\n";

        /** Constante Texto Cuerpo */
        public static final String TEXTOCUERPO = "A fecha " + Utils.dateToString(new Date())
                + " constan como registrados los siguientes documentos:\n";

    }

    /**
     *
     * Constantes para documentos anexos.
     */
    public static final class ReciboPresentacion {

        /**
         *
         */
        protected ReciboPresentacion() {
        }

        /** Constante Texto titulo */
        public static final String TEXTOTITULO = "RECIBO DE PRESENTACIÓN\n";

        /** Constante Texto titulo */
        public static final String ASUNTO = "Asunto: \n";

        /** Constante datos del interesado */
        public static final String DATOS_INTERESADO = "Datos del interesado: \n";

        /** Constante interesado */
        public static final String INTERESADO = "Interesado: \n";

        /** Constante expediente */
        public static final String EXPEDIENTE = "Expediente: \n";

        /** Constante aporte de documentación */
        public static final String DOCUMENTACION = "Aporte de documentación\n";

        /** Constante descripción de la documentación */
        public static final String DOCUMENTACION_DESCRIPCION = "Se han registrado los siguientes documentos\n";
        
        /** Constante descripción de la documentación */
        public static final String NO_DOCUMENTACION_DESCRIPCION = "No se aporta documentación\n";

        /** Constante Texto Cuerpo */
        public static final String TEXTOCUERPO = "A fecha " + Utils.dateToString(new Date())
                + " constan como registrados los siguientes documentos:\n";

        /** Constante Asunto Alegaciones */
        public static final String ASUNTO_ALEGACIONES = "Presentación de alegaciones";
        
        /** Constante Asunto Alegaciones excluido provisional */
        public static final String ASUNTO_ALEGACIONES_EXCLUIDO_PROVISIONAL = "Presentación de alegaciones excluido provisional";

        /** Constante Asunto Notificación del ciudadano */
        public static final String ASUNTO_DESISTIMIENTO_EXPRESO = "Presentación de notificación del ciudadano";

        /** Constante Expone (Notificación del ciudadano) */
        public static final String EXPONE = "Expone: \n";

        /** Constante Solicita (Notificación del ciudadano) */
        public static final String SOLICITA = "Solicita: \n";

    }

    /**
     *
     * Constantes para documentos anexos.
     */
    public static final class Evaluacion {

        /**
         *
         */
        protected Evaluacion() {
        }

        /** Constante Texto titulo */
        public static final String TEXTOTITULO = "Informe de evaluación\n";

        public static final String TEXTO_EXPEDIENTE = "Expediente: ";

        public static final String TEXTO_COMENTARIO = "Comentario: ";

        public static final String TEXTO_NOTA = "Nota: ";

        public static final String TEXTO_INVES_COORDINADOR_EVALUADOR = "Investigador coordinador/evaluador: ";

        public static final String TEXTO_INVES_COORDINADOR = "Investigador coordinador: ";

        public static final String TEXTO_INVES_EVALUADOR = "Investigador evaluador: ";

        public static final String TEXTO_FECHA = "Fecha: ";

        public static final String TEXTO_ASUNTO = "Asunto: ";

        public static final String TEXTO_INVES_JUSTEC_INTERMEDIO = "Investigador Justificación Científico Técnico Intermedio: ";

        public static final String TEXTO_INVES_JUSTEC_FINAL = "Investigador Justificación Científico Técnico Final: ";

        public static final String TEXTO_SIN_CD_EXPEDIENTE = "Sin código del expediente";
        
        public static final String TEXTO_DATOS_DEL_PROYECTO = "DATOS DEL PROYECTO";
        
        public static final String TEXTO_REFERENCIA = "Referencia: ";
        
        public static final String TEXTO_ACRONIMO = "Acrónimo: ";
        
        public static final String TEXTO_EVALUADOR = "Evaluador/a: ";
        
        public static final String TEXTO_COORDINADOR = "Coordinador/a: ";
        
        public static final String TEXTO_NIF_SOLICITANTE = "Nif solicitante: ";
        
        public static final String TEXTO_RAZON_SOCIAL = "Razón social: ";
        
        public static final String TEXTO_NIF_RAZON_SOCIAL = "Nif/Razón social solicitante: ";

    }
    
    /**
    *
    * Constantes para revision de contratos.
    */
   public static final class RevisionContratos {

       /**
        *
        */
       protected RevisionContratos() {
       }

       public static final String SUBSANAR = "Subsanar";
       
       public static final String OK = "OK";
       
       public static final String KO = "KO";


   }

    /**
     *
     * Constantes para documentos anexos.
     */
    public static final class Plantilla {

        /**
         *
         */
        protected Plantilla() {
        }

        public static final String TEXTO_TITULO = "Informe de trámite\n";

        public static final String TEXTO_TITULO_SUPERIOR = "Título";

        public static final String TEXTO_TITULO_INFERIOR = "Detalle Título";

        public static final String TEXTO_CONVOCATORIA = "Convocatoria: ";

        public static final String TEXTO_PROCEDIMIENTO = "Procedimiento: ";

        public static final String TEXTO_EXPEDIENTE = "Expediente: ";

        public static final String TEXTO_SUB_EXPEDIENTE = "Sub-Expedientes: ";

        public static final String TEXTO_ASUNTO = "Asunto: ";

        public static final String TEXTO_NIF_SOLICITANTE = "NIF Solicitante: ";

        public static final String TEXTO_NOMBRE_RAZON_SOCIAL = "Nombre y Apellidos/Razón Social";
        
        public static final String TEXTO_NOMBRE = "Nombre y Apellidos";
        
        public static final String TEXTO_RAZON_SOCIAL = "Razón Social";

        public static final String TEXTO_DESCRIPCION = "Descripción: ";

        public static final String TEXTO_FIRMA = "Firma: ";

        public static final String TEXTO_AREA = "Área: ";

        public static final String TEXTO_SUBDIRECCION = "Subdirección: ";

        public static final String TEXTO_DIRECCION = "Dirección: ";

        public static final String TEXTO_CONSEJERIA = "Consejería: ";

        public static final String TEXTO_FECHA = "Fecha: ";
        
        public static final String TEXTO_CAUSA_SUBSANACION = "Causa de la subsanación: ";

        public static final String TEXTO_TIPO_EXCLUSION = "Requisito A - Observación 1";

        public static final String TEXTO_TIPO_SUBSANACION_ADMINISTRATIVA = "Documento A - observación 1";

        public static final String TEXTO_TIPO_SUBSANACION_REQUISITO = "Requisito A - Observación 1";

        public static final String TEXTO_CONTENIDO_CONSEJERIA = "Vicepresidencia, Consejería de Educación y Universidades";
        
        public static final String TEXTO_LISTA_DOC_SUBSANAR = "Documentos a subsanar: ";
        
        public static final String TEXTO_CAUSA_DESISTIMIENTO = "Causa del desistimiento: ";
        
        public static final String TEXTO_NUMERO_EXPEDIENTE = "Nº EXPEDIENTE: ";
    }

    /**
     *
     * Constantes para documentos anexos.
     */
    public static final class Alegacion {

        /**
         *
         */
        protected Alegacion() {
        }

        /** Constante Texto titulo */
        public static final String TEXTOTITULO = "Informe de valoración\n de alegaciones";

        public static final String TEXTO_EXPEDIENTE = "Expediente: ";

        public static final String TEXTO_INVES = "Investigador: ";

        public static final String TEXTO_EVAL = "Evaluador: ";

        public static final String TEXTO_FECHA = "Fecha: ";

    }

    /**
     * Constantes de extensiones
     */
    public static final class Extensiones {

        /**
         *
         */
        protected Extensiones() {
        }

        /** Constante png */
        public static final String PNG = "png";

        /** Constante jpg */
        public static final String JPG = "jpg";

        /** Constante jpeg */
        public static final String JPEG = "jpeg";

        /** Constante pdf */
        public static final String PDF = "pdf";

        /** Constante xls */
        public static final String XLS = "xls";

        /** Constante xlsx */
        public static final String XLSX = "xlsx";
    }

    /**
     * Constantes de content-types
     */
    public static final class ContentType {

        /**
         *
         */
        protected ContentType() {
        }

        /** Constante application/zip */
        public static final String ZIP = "application/zip";

        /** Constante text/plain */
        public static final String TEXTO_PLANO = "text/plain";

        /** Constante image/png */
        public static final String PNG = "image/png";

        /** Constante image/JPEG */
        public static final String JPEG = "image/jpeg";

        /** Constante text/pdf */
        public static final String PDF = "text/pdf";

        /** Constante application/vnd.ms-excel */
        public static final String XLS = "application/vnd.ms-excel";

        /**
         * Constante
         * application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
         */
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    /**
     * Constantes de Multipart
     */
    public static final class Multipart {

        /**
         *
         */
        protected Multipart() {
        }

        /** Constante file */
        public static final String ARCHIVO = "file";

        /** Constante idExpediente */
        public static final String ID_EXPEDIENTE = "idExpediente";

        /** Constante idExpedienteDoc */
        public static final String ID_EXPEDIENTE_DOC = "idExpedienteDoc";

        /** Constante idTipoDoc */
        public static final String ID_TIPO_DOC = "idTipoDoc";

        /** Constante esDocumentoAdicional */
        public static final String ES_DOCUMENTO_ADICIONAL = "esDocumentoAdicional";

        /** Constante tipoSubsanacion */
        public static final String TIPO_SUBSANACION = "tipoSubsanacion";
        
        public static final String TIPO_PRESENTACION = "tipoPresentacion";

        /** Constante tipoSubsanacion - administrativa */
        public static final String TIPO_SUBSANACION_ADMIN = "administrativa";

        /** Constante tipoSubsanacion - requisitos */
        public static final String TIPO_SUBSANACION_REQ = "requisitos";

        /** Constante tlObservaciones */
        public static final String TL_OBSERVACIONES = "tlObservaciones";

        /**
         * Constante para identificar el valor del indicador si es documento
         * alegación
         */
        public static final String ES_DOCUMENTO_ALEGACION = "esDocumentoAlegacion";

        /**
         * Constante para identificar el valor del indicador del tramite
         */
        public static final String ID_TRAMITE = "idTramite";
        
        /** Constante tipoPresentacion - contratos */
        public static final String TIPO_PRESENTACION_CONTRATO = "presentacionContrato";
        
        /** Constante tipoSubsanacion - contratos */
        public static final String TIPO_SUBSANACION_CONTRATO = "subsanacionContrato";
        
        /** Constante itAutoriza */
        public static final String IT_AUTORIZA = "itAutoriza";

        public static final String ID_CONVOCATORIA = "id_convocatoria";

        /** Constante idAsunto (notificación del ciudadano) */
        public static final String ID_ASUNTO = "idAsunto";

    }

    /**
     * Clase que contiene las constantes de Utilidad
     *
     * @author everis
     *
     */
    public static final class Utiles {

        /**
         * Constructor de la clase
         */
        protected Utiles() {
        }

        /** Longitud máxima de los campos de auditoría CD_USU_ALTA/CD_USU_MODIF/CD_USU_BAJA (VARCHAR2(12) según normativa de modelado de BBDD). */
        public static final Integer LONGITUD_MAX_CD_USU_AUDITORIA = 12;

        /** Formato de la fecha. */
        public static final String FORMATO_FECHA = "dd/MM/yyyy";

        /** Formato de la fecha. */
        public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";

        /** Formato de la fecha para las descargas. */
        public static final String FORMATO_FECHA_HORA_DESCARGA = "dd-MM-yyyy-HH_mm_ss";

        /** Usuario para auditoria ejecutada por batch */
        public static final String COD_USU_BATCH_AUDITORIA = "SAII_BATCH";

        /** Etiqueta OTROS */
        public static final String ETIQUETA_OTROS = "OTROS";

        public static final String ETIQUETA_OTROS_MINUSCULA = "Otros";

    }

    /**
     * Clase que contiene los nombres del aplicativo y modulos
     *
     * @author everis
     *
     */
    public static final class AplicativoSaii {

        /**
         * Constructor de la clase
         */
        protected AplicativoSaii() {

        }

        public static final String CODIGO_APLICATIVO = "SAII";

        /** Constante SAII_REST_SOLICITUD */
        public static final String SAII_REST_SOLICITUD = "SAII_REST_SOLICITUD";

    }

    /**
     * Clase para control de solapas
     */
    public static final class AdministracionConvocatoria {

        /**
         * Constructor
         */
        protected AdministracionConvocatoria() {
        }

        // Solapa padre - Convocatoria

        public static final int SOLAPA_CONVOCATORIA = 0;

        public static final int SOLAPA_CONVOCATORIA_IDENTIFICACION = 0;

        public static final int SOLAPA_CONVOCATORIA_AREA_CIENTIFCA = 1;

        public static final int SOLAPA_CONVOCATORIA_SUBMODALIDAD = 2;

        public static final int SOLAPA_CONVOCATORIA_PLAZOS = 3;
        
        public static final int SOLAPA_CONVOCATORIA_ANUALIDADES = 4;

        public static final int SOLAPA_CONVOCATORIA_ACTORES = 5;

        public static final int SOLAPA_CONVOCATORIA_FORMULARIOS = 6;
     
        public static final int SOLAPA_CONVOCATORIA_DOCUMENTO_PRESENTAR = 7;

        public static final int SOLAPA_CONVOCATORIA_AREA_TRAMITADORA = 8;
        
        public static final int SOLAPA_CONVOCATORIA_GESTORES = 9;
        

        // Solapa padre - Financiacion

        public static final int SOLAPA_FINANCIACION = 1;

        public static final int SOLAPA_FINANCIACION_CONFIGURACION = 0;

        // Solapa padre - Revisión

        public static final int SOLAPA_REVISION = 2;

        public static final int SOLAPA_REVISION_CAUSA_DENEGACION = 0;

        public static final int SOLAPA_REVISION_REQUISITOS = 1;

        // Solapa padre - Evaluación

        public static final int SOLAPA_EVALUACION = 3;

        public static final int SOLAPA_EVALUACION_COORDINADORES = 0;

        public static final int SOLAPA_EVALUACION_FORMULARIO_EVALUACION = 1;

        public static final int SOLAPA_EVALUACION_FORMULARIO_VALORACION_ALEGACION = 2;

        public static final int SOLAPA_EVALUACION_CAUSA_DENEGACION = 3;

        // Solapa padre - Comunicaciones

        public static final int SOLAPA_COMUNICACIONES = 4;

        public static final int SOLAPA_PLANTILLAS_PLANTILLA = 0;

        public static final int SOLAPA_PLANTILLAS_COMUNICACION = 1;

        // Solapa padre - Listados

        public static final int SOLAPA_LISTADOS = 5;

        public static final int SOLAPA_LISTADOS_CONFIGURACION = 0;

        // Solapa padre - Otros Datos

        public static final int SOLAPA_OTROS_DATOS = 6;

        public static final int SOLAPA_OTROS_DATOS_DOCUMENTACION = 0;

        public static final int SOLAPA_OTROS_DATOS_FECHA_TRAMITE = 1;

        public static final int SOLAPA_OTROS_DATOS_ENLACES_EXTERNOS = 2;

    }

    /**
     * Clase para control de opciones de rango de notas
     */
    public static final class RangoNota {

        /**
         * Constructor
         */
        protected RangoNota() {
        }

        public static final String DEFICIENTE = "DEF";

        public static final String CUESTIONABLE = "CUE";

        public static final String ACEPTABLE = "ACE";

        public static final String BUENO = "BUE";

        public static final String EXCELENTE = "EXC";

    }

    /**
     * Clase para control de opciones de persona
     */
    public static final class FormularioPersona {

        /**
         * Constructor
         */
        protected FormularioPersona() {
        }

        public static final String OPCION_CODIGO_OTRO = "OT";

    }

    /**
     *
     * Constantes para TipoCoorEval
     */
    public static final class TipoCoorEval {
        /**
         *
         */
        protected TipoCoorEval() {

        }

        public static final String COORDINADOR = "C";

        public static final String EVALUADOR = "E";

        public static final String NOMBRE_COORDINADOR = "Coordinador";

        public static final String NOMBRE_EVALUADOR = "Evaluador";

        public static final String INTERESADO_IT_COOR_EVAL = "S";

        public static final String INFORME_EVALUACION = "informe_evaluacion.pdf";

        public static final String INFORME_ALEGACION = "Informe de valoracion de alegación";
    }

    /**
     *
     * Constantes para TipoEvalJustec
     */
    public static final class TipoEvalJustec {
        /**
        *
        */
        protected TipoEvalJustec() {

        }

        public static final String INTERMEDIA = "I";

        public static final String FINAL = "F";

        public static final String NOMBRE_INTERMEDIA = "Intermedia";

        public static final String NOMBRE_FINAL = "Final";

        public static final String INFORME_EVALUACION = "informe_evaluacion_justec.pdf";
        
        public static final String TODAS ="Todas";

    }

    /**
     * Constantes para patrones regexp
     */
    public static final class CorreoValido {
        private CorreoValido() {
        }

        public static final String PATRON_CORREO = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    }

    /**
     * Constantes para datos fijos de la convocatoria
     */
    public static final class Convocatoria {

        /**
         *
         */
        private Convocatoria() {
        }

        /**
         * Dias habiles de plazo para alegaciones
         */
        public static final Integer DIAS_ALEGACIONES = 10;
        
        public static final String RESOL_CONJ = "C"; 
        
        public static final String RESOL_INDIV = "I";

    }

    /**
     *
     * Constantes para notificacion
     */
    public static final class Notificacion {
        /**
         *
         */
        protected Notificacion() {

        }

        public static final Integer NM_DIAS_SUBSANACION = 10;

        public static final String ITNOTIFICADO_S = "S";

        public static final String EVALUACION_EXPEDIENTE = "La evaluación del expediente ";

        public static final String CUMPLIMENTAR_COORDINADOR_PENDIENTE = " está pendiente de cumplimentar.";

        public static final String RECHAZADA_POR_GESTOR = " ha sido rechazada por el Gestor.";

        public static final String RECHAZADA_POR_COORDINADOR = " ha sido rechazada por el Coordinador.";

        public static final String REVISAR_EVALUACION_EXPEDIENTE = "Es necesario revisar la evaluación de expediente ";

        public static final String ALEGACIONES_EXPEDIENTE = "Para el expediente ";

        public static final String ALEGACIONES_EXPEDIENTE_2 = " se han presentado alegaciones. Revise las alegaciones y cumplimente el formulario de valoración.";

        public static final String ASIGNACION_EVALUADORES = " está pendiente de asignar evaluador(es). ";

        public static final String EVALUADOR_ASIGNADO_EXPEDIENTE = "Ha sido asignado al expediente ";

        public static final String PENDIENTE_CUMPLEMENTAR_EVALUACION = ". Tiene pendiente cumplimentar la evaluación.";
        
        public static final String PENDIENTE_CUMPLEMENTAR_EVALUACION_JUSTEC = ". Tiene pendiente cumplimentar la evaluación de justificación científico ténica.";

        public static final String PENDIENTE_ACEPTACION_EXPERTO = ". Tiene pendiente de aceptar la asignación como experto evaluador.";
    }

    /**
     *
     * Constantes para EREG.
     */
    public static final class Ereg {

        /**
         *
         */
        protected Ereg() {
        }

        /** Constante PDF_FIRMADO. El nombre viene de EREG */
        public static final String PDF_FIRMADO = "Firma Incrustada en el PDF (B64)";

        /** Constante LISTADO_DOC */
        public static final String LISTADO_DOC = "Listado documentos anexos";

        /** Constante LISTADO_DOC */
        public static final String LISTADO_DOC_SUBSANADOS = "Listado documentos anexos (subsanados)";
    }

    /**
     *
     * Constantes para Tramite.
     */
    public static final class Tramite {
        /**
         *
         */
        protected Tramite() {

        }

        /** Constante Tramite de Presentación de la solicitud */
        public static final Integer PRESENTACION = 400;

        /**
         * Constante Tramite de Requerimiento de Subsanación - Solicitud -
         * Revisión administrativa
         */
        public static final Integer REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA = 401;
        
        /**
         * Constante Tramite de Subsanación Revisión administrativa
         */
        public static final Integer SUBSANACION_REVISION_ADMIN = 500;

        /**
         * Constante Tramite de Requerimiento de Subsanación - Solicitud -
         * Revisión requisitos
         */
        public static final Integer REQUERIMIENTO_SUBSANACION_REQUISITO = 402;
        
        /**
         * Constante Tramite de Subsanación Revisión requisitos
         */
        public static final Integer SUBSANACION_REVISION_REQUISITO = 501;

        //VMM 02/08/24 MD-531: se cambia el nombre de requerimiento a presentacion de subsanacion de contrato
        /** Constante Tramite de Presentación de Subsanación - Contrato Inicial*/
        public static final Integer PRESENTACION_SUBSANACION_CONTRATO_INICIAL = 403;

        /**
         * Constante Tramite de Requerimiento de Subsanación - Borrador Acta del
         * Comité de Gestión
         */
        public static final Integer REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA = 404;

        /**
         * Constante Tramite de Requerimiento de Presentación - Acta Final del
         * Comité de Gestión
         */
        public static final Integer REQUERIMIENTO_PRESENTACION_ACTA_FINAL = 405;

        //VMM 02/08/24 MD-531: se cambia el nombre de requerimiento a presentacion de subsanacion de JCT
        /**
         * Constante Tramite de Presentacion de Subsanación - Justificación
         * Científico Técnica
         */
        public static final Integer PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA = 406;
        

        /**
         * Constante Tramite de Requerimiento de Presentación - Justificación
         * Económica
         */
        public static final Integer REQUERIMIENTO_PRESENTACION_JUSTIFICACION_ECONOMICA = 407;
        
        /** Tramite de Presentación - Justificación Económica **/
        public static final Integer TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA = 505;

        /**
         * Constante Tramite de Requerimiento de Subsanación - Justificación
         * Económica
         */
        public static final Integer REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA = 408;
        
        /** Tramite de Subsanación - Justificación Económica **/
        public static final Integer TRAMITE_SUBSANACION_JUSTIFICACION_ECONOMICA = 506;
        
        /**
         * Constante Tramite de Informe de Ayuda Revisada - Justificación
         * Económica
         */
        public static final Integer INFORME_JUSTIFICACION_ECONOMICA = 409;

        /** Constante Tramite de Informe Final - Justificación Económica */
        public static final Integer INFORME_FINAL_JUSTIFICACION_ECONOMICA = 410;

        /** Constante Tramite de Desistimiento de un expediente */
        public static final Integer DESISTIMIENTO = 411;

        
        public static final Integer NOTIFICACION_CIUDADANO = 701;

        /** Constante Tramite de Exclusión de expediente */
        public static final Integer EXCLUSION = 412;

        /* Constatnte Tramite Concesión */
        public static final Integer CONCESION = 413;
        
        /* Constante Tramite Concesión provisional */
        public static final Integer CONCESION_PROVISIONAL = 439;
        
        /* Constante Tramite Concesión provisional */
        public static final Integer DENEGACION_PROVISIONAL = 447;
        
        public static final Integer RESOLUCION_DESISTIMIENTO_PROVISIONAL = 418;
        
        public static final Integer RESOLUCION_EXCLUSION_PROVISIONAL= 419;
        
        /** Exclusión y desistimiento conjunto de expedientes. */
        public static final Integer RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO = 450;
        
        /* Constante Tramite Concesión provisional */
        public static final Integer CONCESION_DEFINITIVA = 448;
        
        /* Constante Tramite Concesión provisional */
        public static final Integer DENEGACION_DEFINITIVA = 449;

        //VMM 31/07/24 MD-505: se cambia el nombre en BD de requerimiento a presentacion de JCT
        /** Constante Tramite de Presentación Justificación Cientifico Tecnica */
        public static final Integer PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA = 414;

        /** Constante Tramite de Inicio Plazos Justificación Económica */
        public static final Integer INICIO_PLAZOS_JUST_ECONOMICA = 415;

        /* Constante Tramite Alegaciones */
        public static final Integer ALEGACIONES = 438;
                
        /* Constante Tramite Inicio Plazos Justificación Científico Técnica */
        public static final Integer INICIO_PLAZOS_JUSTIFICACION_CIENTIFICO_TECNICA = 440;
        
        /* Constante Tramite de Presentación - Contrato inicial */
        public static final Integer PRESENTACION_CONTRATO_INICIAL = 441;
        
        /* Constante Tramite de Presentación - Contrato inicial */
        public static final Integer GESTION_CONTRATO = 1690;
        
        /* Constante Tramite de Presentación - Contrato Sustitución */
        public static final Integer PRESENTACION_CONTRATO_SUSTITUCION = 442;
        
        // MD-2190 Quitamos la declaración de la constante de alegación exclusión provisional
        /* Constante Tramite Alegacion Exclusion Provisional */
//        public static final Integer ALEGACION_EXCLUSION_PROVISIONAL = 1772;
                
        //VMM 31/07/24 MD-505: se genera aqui y en BD el nuevo tramite de requerimiento de presentacion de JCT
        /** Constante Tramite de Requerimiento de Presentación Justificación Cientifico Tecnica */
        public static final Integer REQUERIMIENTO_PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA = 443;
        
        //VMM 02/08/24 MD-531: se genera aqui y en BD el nuevo tramite de requerimiento de subsanacion de JCT
        /** Constante Tramite de Requerimiento de Subsanación Justificación Cientifico Tecnica */
        public static final Integer REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA = 444;
        
        //VMM 02/08/24 MD-531: se genera aqui y en BD el nuevo tramite de requerimiento de subsanacion contrato inicial
        /** Constante Tramite de Requerimiento de Subsanación Contrato Inicial */
        public static final Integer REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL = 445;
        
        //VMM 02/08/24 MD-531: se genera aqui y en BD el nuevo tramite de requerimiento de subsanacion contrato sustitución
        /** Constante Tramite de Requerimiento de Subsanación Contrato Sustitución */
        public static final Integer REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION = 446;
        
        /** Tramite de Presentación borrador acta comité gestión  **/
        public static final Integer PRESENTACION_BORRADOR_ACTA_COMITE_GESTION = 502;
        
        /** Tramite de Presentación - Acta Final del Comité de Gestión **/
        public static final Integer PRESENTACION_ACTA_FINAL_COMITE_GESTION = 503;
        
        /** Tramite de Subsanación - Borrador Acta del Comité de Gestión **/
        public static final Integer SUBSANACION_BORRADOR_ACTA_COMITE_GESTION = 504;
        
        /** Tramite de Requerimiento de Presentación - Contrato inicial **/
        public static final Integer REQUERIMIENTO_PRESENTACION_CONTRATO_INICIAL = 507;
        
        /** Tramite de Requerimiento de Presentación - Contrato sustitución **/
        public static final Integer REQUERIMIENTO_PRESENTACION_CONTRATO_SUSTITUCION = 508;   
        
        /** Tramite de Requerimiento de Presentación - Contrato renuncia**/
        public static final Integer REQUERIMIENTO_PRESENTACION_CONTRATO_RENUNCIA = 509; 
        
        /** Tramite de Requerimiento de Subsanación - Contrato renuncia**/
        public static final Integer REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA = 510; 
        
        /* Constante Tramite de Presentación - Contrato Renuncia */
        public static final Integer PRESENTACION_CONTRATO_RENUNCIA = 511;
        
        /** Constante Tramite de Presentación de Subsanación - Contrato Renuncia */
        public static final Integer PRESENTACION_SUBSANACION_CONTRATO_RENUNCIA = 512;
        
        /** Constante Tramite de Presentación de Subsanación - Contrato Sustitución */
        public static final Integer PRESENTACION_SUBSANACION_CONTRATO_SUSTITUCION = 513;

        public static final Integer MODIFICACION_PRESP_PLAZOS_OTROS = 1735;
        
        /* Constante Tramite rESOLUCIONM PRESUPUESTOM MODIFICADA */
        public static final Integer RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA = 514;
        
//        /* Constante Tramite rESOLUCIONM PRESUPUESTOM MODIFICADA */
//        public static final Integer RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA_PARCIAL = 515;
//        
//        /* Constante Tramite rESOLUCIONM PRESUPUESTOM MODIFICADA */
//        public static final Integer RESOLUCION_PRESUPUESTO_MODIFICADA_RECHAZADA = 516;
        
        
        public static final Integer PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS = 1751;
        
        // MD-2190 Quitamos la declaración de la constante del trámite de alegaciones exclusión provisional
        /* Constante Tramite Alegaciones de exclusión provisional */
//        public static final Integer ALEGACIONES_EXCLUSION_PROVISIONAL = 1772;
        
        /* Constante Tramite Alegaciones de exclusión provisional */
        public static final Integer RESOLUCION_CONVOCATORIA_PROVISIONAL = 1917;
        
        /* Constante Tramite Alegaciones de exclusión provisional */
        public static final Integer RESOLUCION_CONVOCATORIA_DEFINITIVO = 1918;
        
        /* Constante Tramite Aceptacion desistimiento ayuda */
        public static final Integer ACEPTACION_DESISTIMIENTO_AYUDA = 700;
        
        
        
    }

    /**
     *
     * Constantes para Tramite.
     */
    public static final class Note {
        /**
         *
         */
        protected Note() {

        }

        /**
         * Constante Note codigo estado 'Puesta a disposición'
         */
        public static final String CD_PD = "1";

        /**
         * Constante Note codigo estado 'Leidas'
         */
        public static final String CD_L = "2";

        /**
         * Constante Note codigo estado 'Rechazadas'
         */
        public static final String CD_R = "3";

        /**
         * Constante Note codigo estado 'Rechazadas automáticamente'
         */
        public static final String CD_RA = "4";

        /**
         * Constante Note codigo estado 'Puesta a disposición'
         */
        public static final String DS_PUESTA_DISPOSICION = "Puesta a disposición";

        /**
         * Constante Note codigo estado 'Puesta a disposición'
         */
        public static final String DS_LEIDAS = "Leída";

        /**
         * Constante Note codigo estado 'Puesta a disposición'
         */
        public static final String DS_RECHAZADAS = "Rechazada";

        /**
         * Constante Note codigo estado 'Puesta a disposición'
         */
        public static final String DS_RECHAZADAS_AUTO = "Rechazada automáticamente";
    }

    /**
     *
     * Constantes para Tipo de Justificacion Cientifica-Tecnica.
     */
    public static final class TipoJustificacionCienTec {
        /**
         *
         */
        protected TipoJustificacionCienTec() {

        }

        public static final Integer INTERMEDIA = 416;

        public static final Integer FINAL = 417;

        public static final String LABEL_INTERMEDIA = "INTERMEDIA";

        public static final String LABEL_FINAL = "FINAL";
    }
    
    /**
    *
    * Constantes para Tipo de Justificacion Económica
    */
   public static final class TipoJustificacionEco {
       /**
        *
        */
       protected TipoJustificacionEco() {

       }

       public static final Integer INTERMEDIA = 498;

       public static final Integer FINAL = 499;

       public static final String LABEL_INTERMEDIA = "INTERMEDIA";

       public static final String LABEL_FINAL = "FINAL";
   }

    /**
     *
     * Constantes para Tematicas de bloque
     */
    public static final class TematicaBloque {
        /**
         *
         */
        protected TematicaBloque() {

        }

        public static final Integer DATOS_RENUNCIA = 1171;

        public static final Integer AYUDA_SOLICITADA = 427;

        public static final Integer FINANCIACION_ACTUACIONES = 1118;
        
        // Nuevas tematicas para generar bloques por defecto en
        // formularios de modificaciones presupuestarias y de actas
        public static final Integer PLAZOS_EJECUCION_ANUALIDADES = 1557;
        
        public static final Integer PERSONAL_SIN_CARGO = 1559;
        
        public static final Integer PERSONAL_CON_CARGO = 1561;
        
        public static final Integer OTRAS_MODIFICACIONES = 1562;
        
        public static final Integer OBSERVACIONES = 1563;
        
        public static final Integer DATOS_CONTRATO = 1574;
        
        public static final Integer DATOS_CONTRATO_DOCUMENTACION = 1588;
        
        public static final Integer DATOS_RENUNCIA_DOCUMENTACION = 1172;
        
        public static final Integer DATOS_CONTRATO_SUSTITUCION = 1173;
        
        public static final Integer DATOS_SUSTITUCION_DOCUMENTACION = 1174;
        
        public static final Integer DATOS_ACTAS_DOCUMENTACION = 1175;
        
        public static final Integer DATOS_MEDIO_NOTIFICACION =  187;
        
        public static final Integer DATOS_PERSONA_SOLICITANTE =  186;
        
        public static final Integer DATOS_CUENTA_BANCARIA =  185;
        
        public static final Integer DATOS_GRADO_DISCAPACIDAD = 184;
        
        public static final Integer DATOS_INTENSIDAD_AYUDA = 183;
        
    }

    /**
     *
     * Constantes para TipoFormulario.
     */
    public static final class TipoFormulario {
        /**
         * Constructor de clase
         */
        protected TipoFormulario() {

        }

        public static final Integer TIPO_FORMULARIO_CIETEC = 85;
    }

    public static final class TablaFinanciacion {
        protected TablaFinanciacion() {

        }

        public static final String IMPORTE = "Importe";

        public static final String DETALLE_OFERTA = "Detalle de propuestos";

        public static final String OFERTA_SELECCIONADA = "Ofertas seleccionadas";

        public static final String AYUDA_SOLICITADA = "Ayuda solicitada";
    }

    /**
     *
     * Constantes para Tipo de envío de notificación.
     */
    public static final class TipoEnvio {
        /**
        *
        */
        protected TipoEnvio() {

        }

        public static final String DS_PORTAFIRMAS = "Portafirmas";

        public static final String DS_NOTE = "Note";

        public static final String DS_PORTAFIRMAS_NOTE = "Portafirmas / Note";

        public static final String DS_CORREO = "Correo";
    }

    /**
     * Constantes de apoyo para la generación de formulario pdf
     *
     */
    public static final class FormularioPdf {

        protected FormularioPdf() {
        }

        public static final String CDNUMREFDOCUM = "cdNumRefDocum";

        public static final String DSDESTINO = "dsDestino";

        public static final String FCREGISTRO = "fcRegistro";

        public static final String HUELLA = "HUELLA";

    }
    
    public static final String FC_ENVIO_COLA = "fcEnvioCola";
    
    public static final String FILTRO_ENVIOS_ESTADO = "nmEstado";
    
    
    public static final class TramiteConvocatoria{
        protected TramiteConvocatoria() {
            
        }
        
        public static final Integer CONCESION = 1;
        
        public static final Integer JCT = 2;
    }
    
    /**
    *
    * Constantes para EstadoExpedienteJustec.
    */
   public static final class EstadoExpedienteJustec {

       /**
        *
        */
       protected EstadoExpedienteJustec() {
       }

       /** Constante codigo PENDIENTE_DE_PRESENTACION */
       public static final Integer PENDIENTE_DE_PRESENTACION = 1575;
       /** Constante codigo PENDIENTE_DE_REVISION */
       public static final Integer PENDIENTE_DE_REVISION_ADM = 1576;
        /** Constante codigo RECLAMADA_JUSTIFICACION */
       public static final Integer RECLAMADA_JUSTIFICACION = 1578;
       /** Constante codigo PENDIENTE_DE_SUBSANACION */
       public static final Integer PENDIENTE_DE_SUBSANACION = 1579;
       /** Constante codigo PENDIENTE_DE_REVISION_SUBSANACION */
       public static final Integer PENDIENTE_DE_REVISION_SUBSANACION = 1580;
       /** Constante codigo REVISION_OK */
       public static final Integer REVISION_OK = 1581;
       /** Constante codigo REVISION_KO */
       public static final Integer REVISION_KO = 1582;
       /** Constante codigo EN_EVALUACION */
       public static final Integer EN_EVALUACION = 1583;
       /** Constante codigo RESOLUCION_DESESTIMATORIA */
       public static final Integer RESOLUCION_DESESTIMATORIA = 1584;
       /** Constante codigo RESOLUCION_ESTIMATORIA */
       public static final Integer RESOLUCION_ESTIMATORIA = 1585;
       /** Constante codigo NO_PRESENTADA */
       public static final Integer NO_PRESENTADA = 1586;
       /** Constante codigo FINALIZADO_PLAZO_PRESENTACION */
       public static final Integer FINALIZADO_PLAZO_PRESENTACION = 1587;
       /** Constante codigo PENDIENTE_DE_EVALUACION */
       public static final Integer PENDIENTE_DE_EVALUACION = 1663;
       /** Constante codigo PENDIENTE_DE_REVISIION_EVALUACION */
       public static final Integer PENDIENTE_REVISIION_EVALUACION = 1657;
       /** Constante codigo REVISION_ADM_KO */
       public static final Integer REVISION_ADM_KO = 1664;
       /** Constante codigo EVALUADO */
       public static final Integer EVALUADO = 1665;
       /** Constante codigo INICIO_PLAZO DE PRESENTACION */
       public static final Integer INICIO_PLAZO_DE_PRESENTACION = 1666;
       
       
   }
   
   
   
   /**
   *
   * Constantes para EstadoExpedienteJusEco.
   */
  public static final class EstadoExpedienteJusEco {

      /**
       *
       */
      protected EstadoExpedienteJusEco() {
      }

      /** Constante codigo PENDIENTE_DE_PRESENTACION */
      public static final Integer PENDIENTE_DE_PRESENTACION = 800;
      
      /** Constante codigo PENDIENTE_DE_REVISION */
      public static final Integer PENDIENTE_DE_REVISION_ADM = 801;
      
      /** Constante codigo EN_REVISION */
      public static final Integer EN_REVISION = 802;
      
       /** Constante codigo RECLAMADA_JUSTIFICACION */
      public static final Integer RECLAMADA_JUSTIFICACION = 803;
      
      /** Constante codigo PENDIENTE_DE_SUBSANACION */
      public static final Integer PENDIENTE_DE_SUBSANACION = 804;
      
      /** Constante codigo PENDIENTE_DE_REVISION_SUBSANACION */
      public static final Integer PENDIENTE_DE_REVISION_SUBSANACION = 805;
      
      /** Constante codigo REVISION_OK */
      public static final Integer REVISION_OK = 806;
      
      /** Constante codigo REVISION_KO */
      public static final Integer REVISION_KO = 807;
      
      /** Constante codigo NO_PRESENTADA */
      public static final Integer NO_PRESENTADA = 808;
      
      /** Constante codigo FINALIZADO_PLAZO_PRESENTACION */
      public static final Integer FINALIZADO_PLAZO_PRESENTACION = 809;
      
      /** Constante codigo INICIO_PLAZO DE PRESENTACION */
      public static final Integer INICIO_PLAZO_DE_PRESENTACION = 810;
      
      /** Constante codigo REVISION_ADM_KO */
      public static final Integer REVISION_ADM_KO = 811;
      
  }
   
   /**
   *
   * Constantes para EstadoExpediente.
   */
   public static final class EstadosEvaluacionJCT {

      /**
       *
       */
      protected EstadosEvaluacionJCT() {
      }

      /** Constante codigo Evaluador Asignado */
      public static final Integer EVALUADOR_ASIGNADO = 1658;
      /** Constante codigo Pendiente Evaluacion */
      public static final Integer PENDIENTE_EVALUACION = 1659;
      /** Constante codigo No evaluado */
      public static final Integer NO_EVALUADO = 1660;
      /** Constante codigo Cerrado Evaluador */
      public static final Integer CERRADO_EVALUADOR = 1661;
      /** Constante codigo Cerrado Gestor */
      public static final Integer CERRADO_GESTOR = 1662;
      
    
  }
   
   public static final class TipoContrato {
       protected TipoContrato() {
           
       }
       
       public static final Integer CONTRATO_INICIAL = 1625;
       public static final Integer CONTRATO_SUSTITUCION = 1626;
       
   }
   
   /**
   *
   * Constantes para TipoEvalJustec
   */
   public static final class TipoResolucionModalidad {
      /**
      *
      */
      protected TipoResolucionModalidad() {

      }

      /** Constante codigo TipoResolucionModalidad conjunta */
      public static final String RESOLUCION_CONJUNTA = "C";
      
      /** Constante codigo TipoResolucionModalidad individual */
      public static final String RESOLUCION_INDIVIDUAL = "I";
  }
   
   /**
    * Constantes para Estado de contrato
    */
   public static final class EstadoContrato {

       /**
        * Constructor de clase
        */
       protected EstadoContrato() {

       }
       
       //añadir numeros
       
       public static final Integer REVISION_OK = 1627;
       
       public static final Integer PENDIENTE_REVISION_CONTRATO = 1628;
       
       public static final Integer REVISION_KO = 1629;
       
       public static final Integer PENDIENTE_SUBSANACION = 1630;
       
       public static final Integer PENDIENTE_REVISION_SUBSANACION = 1631;
       
       public static final Integer ENVIO_COLA_NOTIFICACIONES = 1684;
       
       public static final Integer ENVIADO_PORTAFIRMAS = 1685;
       
       public static final Integer SUBSANACION_PRESENTADA = 1683;
       
       public static final Integer SUSTITUCION_PRESENTADA = 1682;
       
       public static final Integer RENUNCIA_CON_SUST_PRESENTADA_TIPO_C_INICIAL =1681;
       
       public static final Integer RENUNCIA_SIN_SUSTITUCION_PRESENTADA =1680;
       
       public static final Integer CONTRATO_PRESENTADO =1679;
       
       public static final Integer PENDIENTE_PRESENTAR_SUBSANACION = 1678;
       
       public static final Integer PENDIENTE_PRESENTAR_RENUNCIA_SIN_SUSTITUCION =1677;
       
       public static final Integer PENDIENTE_PRESENTAR_RENUNCIA_CON_SUSTITUCION =1676;
       
       public static final Integer PENDIENTE_PRESENTAR_SUSTITUCION =1675;
       
       public static final Integer PENDIENTE_PRESENTAR_CONTRATO =1674;
       
       public static final Integer REQUIERE_SUBSANACION = 1673;
       
       public static final Integer SIN_SUBIR_SUSTITUCION =1672;
       
       public static final Integer SIN_SUBIR =1669;
       
       public static final Integer PENDIENTE_PRESENTAR_RENUNCIA =1687;
       
       public static final Integer PENDIENTE_REVISION_RENUNCIA =1686;
       
       public static final Integer RENUNCIA_APROBADA =1700;
       
       public static final Integer REQUIERE_SUBSANACION_RENUNCIA =1701;
       
       public static final Integer CONTRATO_DESISTIDO =1702;
       
       public static final Integer REQUIERE_SUBSANACION_NOTIFICADO =1703;

   }

   
   /**
   *
   * Constantes para Tipo de modificaciones P/P/O
   */
  public static final class TipoModificacionesPresup {

      /**
       *
       */
      protected TipoModificacionesPresup() {
      }

      /** Constante mensaje Solicitada */
      public static final String S = "S";

      /** Constante mensaje Inicial o Definitiva */
      public static final String D = "D";
      
      /** Constante mensaje Aceptada */
      public static final String A = "A";
  }

  
  public static final class TipoConfiguracion {

      /**
       *
       */
      protected TipoConfiguracion() {
      }

      /** Constante nombre CD_PARAM_ACRONIMO */
      public static final String CD_PARAM_ACRONIMO = "NOMBELA";



  }
  
  public static final class RadioSelectOpcion {

      protected RadioSelectOpcion() {
      }
      
      public static final String OPCION_EXPEDIENTES = "expedientes";
      
      public static final String OPCION_SOLICITUDES = "solicitudes";

  }
  
  /**
   *
   * Constantes para TipoSolicitud.
   */
  public static final class TipoSolicitud {

      /**
       *
       */
      protected TipoSolicitud() {
      }

      /** Constante codigo INICIADA_SOLICITANTE */
      public static final Integer INICIADA_SOLICITANTE = 1985;

      /** Constante codigo INICIADA_PARTICIPANTE */
      public static final Integer INICIADA_PARTICIPANTE = 1986;

  }
  
  /**
  *
  * Constantes para CategoriasFormulario.
  */
 public static final class CategoriasFormulario {

     /**
      *
      */
     protected CategoriasFormulario() {
     }

     /* Constante para financiacion */
     public static final Integer FINANCIACION = 47;

 }
 
 public static final class TipoSeleccionDocumento {

     protected TipoSeleccionDocumento() {
     }

     /** Constante mensaje C */
     public static final String C = "C";
     
     /** Constante mensaje R */
     public static final String R = "R";

     /** Constante mensaje N */
     public static final String N = "N";     
 }   
 

 public static final class TipoPersonaSolicitante {
     public static final Integer TIPO_PERSONA_SOLICITANTE_INTERESADO = 130;
     public static final Integer TIPO_PERSONA_SOLICITANTE_REPRESENTANTE = 131;
     public static final Integer TIPO_PERSONA_SOLICITANTE_AUTORIZADO = 132;
 }

    public static final class MedioNotificacion {
        public static final Integer MEDIO_NOTIFICACION_INTERESADO = 133;
        public static final Integer MEDIO_NOTIFICACION_REPRESENTANTE = 134;

    }
    
    
    /**
    *
    * Constantes para causa de denegación
    *
    */
   public static final class ComponenteCuentaBancaria  {
       /**
        *
        */
       protected ComponenteCuentaBancaria() {

       }

       public static final Integer CODIGO_IBAN = 18;

       public static final Integer ENTIDAD = 19;

       public static final Integer OFICINA = 20;
       
       public static final Integer CD_CUENTA = 21;
       
       public static final Integer NUMERO_CUENTA = 22;
   }
    
   /**
    * Constantes para causa denegación
    */
   public static final class TipoCausaDenegacion {
       protected TipoCausaDenegacion() {
       }
       public static final Integer CAUSA_DENEG_ADM = 610;
       public static final Integer CAUSA_DENEG_JUST_CIENT_TEC = 611;
       public static final Integer CAUSA_DENEG_JUST_ECO = 612;
       public static final Integer CAUSA_DENEG_CONTRATOS = 613;
       public static final Integer CAUSA_DENEG_EVAL = 614;
   }
   
   public static final class TipoNivelAsociacion {
       protected TipoNivelAsociacion() {
       }
       public static final Integer NIVEL_CONVOCATORIA = 550;
      
   }
   
   public static final class ComponenteIntensidad {
       protected ComponenteIntensidad() {
       }
       public static final Integer PORCENTAJE_INTENSIDAD=56;
   }
   
   /**
   *
   * Constantes para Estado Documento Portafirmas
   */
  public static final class TipoDocumentoICDA {
      /**
       *
       */
      protected TipoDocumentoICDA() {

      }

      public static final int IAE10 = 750;

      public static final int SVDDGPCIWS02 = 751;

      public static final int codeCENO = 752;

      public static final int ECOT103 = 753;

      public static final int Q2827003ATGSS001 = 754;

      public static final int ORVASUB = 755;

  }
  
  public static final class ProcedimientoICDA {
      /**
       *
       */
      protected ProcedimientoICDA() {

      }

      public static final int PA2601 = 730;

      public static final int PA2607 = 731;



  }
  
  public static final class AccionFICA {
      protected AccionFICA() {
          
      }
      
      public static final String FIRMA = "firma";
      public static final String CONSULTA = "consulta";
      
  }
   

}
