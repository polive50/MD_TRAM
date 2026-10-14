/*
 *  DATOS DE LAS TABLAS DE LA APLICACION NECESARIOS PARA PODER HACER LOS TEST
 *
 *  AQUI SE INCLUIRAN LOS INSERT NECESARIOS PARA QUE LOS TEST QUE NECESITAN DATOS
 *  EN LA BBDD
 *
 *  LOS TEST CON BBDD EN MEMORIA AUTOMÁTICAMENTE CREAN EL ESQUEMA DE LA BBDD
 *  PARTIENDO DE LA BBDD REAL EN UNA BBDD EN MEMORIA Y CARGAN:
 *   - LOS DATOS DEL FICHERO: comunes.sql (NO SE DEBE MODIFICAR)
 *   - LOS DATOS DEL FICHERO: data.sql
 *
 */

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (763, 'PR 1 NO', 'PR 1 AP 1', 'PR 1 AP 2', to_date('01-12-1986', 'dd-mm-yyyy'), 'C/ PR 1', 'PR 1', 'PR 1', 'usuario_ok', to_date('28-08-2019 12:19:15', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('24-09-2019 10:54:12', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (766, 'Antonio', 'Marian', 'Tiru', to_date('01-09-2014', 'dd-mm-yyyy'), 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 12:56:53', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 17:00:35', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (767, 'Adrián', 'Peralta', 'Cruz', to_date('03-09-2014', 'dd-mm-yyyy'), 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 13:03:12', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 13:06:43', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (768, 'Jose María', 'Becerra', 'Larrauri', to_date('17-09-2014', 'dd-mm-yyyy'), 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 13:04:56', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 13:06:58', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (769, 'Víctor', 'Gil', 'Delgado', to_date('01-04-2014', 'dd-mm-yyyy'), 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 13:06:08', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 13:06:20', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (770, 'Esther', 'Cruz', 'Lopez', null, 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 13:08:36', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 13:08:49', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (771, 'Ana', 'Delgado', 'Tolomé', null, 'C/ Atlas Nº 2 Piso 0 Puerta 0', 'Madrid', 'Madrid', 'usuario_ok', to_date('25-09-2019 13:09:20', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 13:09:45', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (765, 'Antonio', 'Perez', 'Reverte', null, '', 'Parla', 'Española', 'usuario_ok', to_date('25-09-2019 12:28:53', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:29:18', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (758, 'Javier', 'Larrauri', 'Ape2. Al. 1', to_date('14-08-2014', 'dd-mm-yyyy'), 'C/ Del Atlas Nº 2 Esc 0 Piso 0', 'Madrid', 'Española', 'usuario_ok', to_date('14-08-2019 13:26:07', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('24-09-2019 13:31:59', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (759, 'Nom. Al. 2', 'Ape1. Al. 2', '', null, '', '', '', 'usuario_ok', to_date('14-08-2019 13:26:19', 'dd-mm-yyyy hh24:mi:ss'), '', null, '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (760, 'Nom. Al. 3', 'Ape1. Al. 3', '', null, '', '', '', 'usuario_ok', to_date('14-08-2019 13:26:31', 'dd-mm-yyyy hh24:mi:ss'), '', null, '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (761, 'Nom. Al. 4', 'Ape1. Al. 4', '', null, '', '', '', 'usuario_ok', to_date('14-08-2019 13:26:45', 'dd-mm-yyyy hh24:mi:ss'), '', null, '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (762, 'Nom. Al. 5', 'Ape1. Al. 5', '', null, '', '', '', 'usuario_ok', to_date('14-08-2019 13:26:58', 'dd-mm-yyyy hh24:mi:ss'), '', null, '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (1, 'ALUMNO', 'APELLIDO1', 'APELLIDO211111', to_date('01-01-1980', 'dd-mm-yyyy'), 'DIRECCION 1', 'MUNICIPIO', 'ESPAÑOL', 'ABP5', to_date('01-01-2017', 'dd-mm-yyyy'), '', null, '', null);

insert into ejpl_alumnos (ID_ALUMNO, NOMBRE, APELLIDO1, APELLIDO2, FC_NACIMIENTO, DIRECCION, MUNIC_NACIMIENTO, NACIONALIDAD, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (764, 'Antonio', 'Perez', 'Lopez', to_date('01-09-2014', 'dd-mm-yyyy'), 'C/ Falsa Nº 123', 'Madrid', 'Española', 'usuario_ok', to_date('25-09-2019 12:21:09', 'dd-mm-yyyy hh24:mi:ss'), '', null, '', null);

/****************************************************************************************************************************************************************************************/

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (359, 'PR C7', to_date('27-08-2019 14:41:31', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 14:41:31', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', 'usuario_ok', to_date('27-08-2019 14:41:07', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (350, 'Curso 3', null, '', null, '', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (351, 'Curso 4', null, '', null, '', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (352, 'Curso 5', null, '', null, '', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (348, 'Curso 2018-2019', null, '', to_date('25-09-2019 12:46:23', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (349, 'Curso 2019-2020', null, '', to_date('25-09-2019 12:46:10', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (355, 'PR C3', to_date('27-08-2019 11:43:50', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 11:43:50', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (356, 'PR C4', to_date('27-08-2019 11:44:06', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 11:44:06', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (357, 'PR C5', to_date('27-08-2019 11:46:01', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 11:46:01', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (358, 'PR C6', to_date('27-08-2019 14:34:15', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 14:34:15', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (353, 'PR C1', null, '', null, '', '', null);

insert into ejpl_cursos (ID_CURSO, DS_CURSO, FC_BAJA_FILA, CD_USU_BAJA, FC_MODIF_FILA, CD_USU_MODIF, CD_USU_ALTA, FC_ALTA_FILA)
values (354, 'PR C2', to_date('27-08-2019 13:07:06', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('27-08-2019 13:07:06', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', '', null);

/****************************************************************************************************************************************************************************************/

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (189, 'Mortadelo', 'Cálculo', 'Matemáticas');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (190, 'Filemón', 'Ciencias', 'Biología');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (191, 'Profesor 3', 'Especialidad 3', 'Doctorado 3');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (192, 'Profesor 4', 'Especialidad 4', 'Doctorado 4');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (193, 'Profesor 5', 'Especialidad 5', 'Doctorado 5');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (196, 'PROF. 3', 'ESP. 3', 'DOCT. 3');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (197, 'PROF. 4', 'ESP. 4', 'DOCT. 4');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (198, 'PROF. 5', 'ESP. 5', 'DOCT. 5');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (199, 'PROF. 6', 'ESP. 6', 'DOCT. 6');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (195, 'PROF. 2', 'ESP. 2', 'DOCT. 2');

insert into ejpl_profesores (ID_PROFESOR, NOMBRE_PROFESOR, ESPECIALIDAD, DOCTORADO)
values (194, 'PROF. 1', 'ESP. 1', 'DOCT. 1');

/****************************************************************************************************************************************************************************************/

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (352, 'Inglés', 'usuario_ok', to_date('14-08-2019 12:38:33', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:46:52', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (350, 'Matemáticas', 'usuario_ok', to_date('14-08-2019 12:38:20', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:46:41', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (351, 'Literatura', 'usuario_ok', to_date('14-08-2019 12:38:27', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:47:02', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (353, 'Historia', 'usuario_ok', to_date('14-08-2019 12:38:40', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:47:09', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (354, 'Informática', 'usuario_ok', to_date('14-08-2019 12:38:54', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:47:29', 'dd-mm-yyyy hh24:mi:ss'), '', null);

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (355, 'PR1', 'usuario_ok', to_date('20-08-2019 10:09:40', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:47:33', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('25-09-2019 12:47:33', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (357, 'PR3', 'usuario_ok', to_date('20-08-2019 14:34:31', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:27', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:27', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (358, 'PR4', 'usuario_ok', to_date('20-08-2019 14:34:41', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:29', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:29', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (359, 'PR5', 'usuario_ok', to_date('20-08-2019 14:34:52', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:32', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:32', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (360, 'PR6', 'usuario_ok', to_date('20-08-2019 14:35:28', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:34', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:34', 'dd-mm-yyyy hh24:mi:ss'));

insert into ejpl_asignaturas (ID_ASIGNATURA, DS_ASIGNATURA, CD_USU_ALTA, FC_ALTA_FILA, CD_USU_MODIF, FC_MODIF_FILA, CD_USU_BAJA, FC_BAJA_FILA)
values (356, 'PR2', 'usuario_ok', to_date('20-08-2019 14:34:03', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:37', 'dd-mm-yyyy hh24:mi:ss'), 'usuario_ok', to_date('20-08-2019 14:40:37', 'dd-mm-yyyy hh24:mi:ss'));

/****************************************************************************************************************************************************************************************/

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (166, to_date('20-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 350, 189);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (167, to_date('21-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 351, 190);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (168, to_date('22-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 352, 191);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (169, to_date('23-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 353, 190);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (170, to_date('24-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 354, 190);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (176, to_date('25-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 353, 189);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (177, to_date('21-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 348, 350, 189);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (174, to_date('22-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 349, 353, 189);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (175, to_date('23-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 349, 354, 190);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (172, to_date('24-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 349, 351, 190);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (178, to_date('25-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 349, 355, 196);

insert into ejpl_asignatura_curso (ID_ASIGNATURA_CURSO, FC_INI_ASIGNATURA, FC_FIN_ASIGNATURA, ID_CURSO, ID_ASIGNATURA, ID_PROFESOR)
values (171, to_date('25-09-2019', 'dd-mm-yyyy'), to_date('18-06-2020', 'dd-mm-yyyy'), 349, 350, 189);

/****************************************************************************************************************************************************************************************/

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (766, 171);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (766, 174);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (766, 177);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (767, 166);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (767, 171);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (767, 174);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (768, 174);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (769, 171);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (769, 176);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (770, 171);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (770, 176);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (771, 171);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (771, 172);

insert into ejpl_alumno_asignatura (ID_ALUMNO, ID_ASIGNATURA_CURSO)
values (771, 174);


