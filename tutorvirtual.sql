BEGIN;

-- =========================
-- TABLA: documentos
-- =========================
CREATE TABLE documentos (
    id SERIAL PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    tamaño_archivo REAL NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    fecha_subida VARCHAR(255) NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    gemini_file_id VARCHAR(255),
    contenido_texto TEXT
);

-- =========================
-- DATOS: documentos
-- =========================
INSERT INTO documentos
(id, nombre_archivo, tamaño_archivo, categoria, fecha_subida, ruta_archivo, gemini_file_id, contenido_texto)
VALUES
(38, '1766893296467_Certificado de Estudios de UNAMBA.pdf', 3.76832, 'Certificados', '2025-12-27', 'uploads\\documentos\\1766893296467_Certificado de Estudios de UNAMBA.pdf', NULL, 'Certificado de Estudios de UNAMBA  \r\n  PASO 1: Requisitos \r\n1. Ficha de seguimiento \r\n2. Comprobante de pago (S/. 10.00 por semestre) \r\n3. Solicitud dirigida al jefe de la Unidad de Servicios Académicos: \r\nIng. Marco Antonio Aguilar Espinoza \r\n  Lugares de pago: \r\n• Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:15 pm \r\n• Banco de la Nación (agencias/sucursales) \r\n• Código: 1213 Certificado de Estudios \r\n  PASO 2: Entrega de requisitos \r\n• Mesa de Partes \r\n  PASO 3: Recojo del Certificado de Estudios \r\n• Tiempo estimado: 5 a 7 días hábiles \r\n• Lugar: Servicios Académicos (SS. AA) \r\n  Al momento de recoger: \r\n• Adjuntar 2 fotos tamaño carnet \r\n• Fondo blanco \r\n• Ropa formal \r\n  NOTAS IMPORTANTES \r\n• En caso de filiales, tramitar los documentos mediante su escuela \r\n• Si no eres el titular, recoger el certificado con una carta poder simple \r\n  Horario de recojo del Certificado \r\n• Oficina de Servicios Académicos (lunes a viernes) \r\n• Mañanas: 8:00 am – 12:40 pm \r\n• Tardes: 2:00 pm – 3:15 pm \r\n'),
(39, '1766893312217_constancia de egresado.pdf', 3.87804, 'Constancias', '2025-12-27', 'uploads\\documentos\\1766893312217_constancia de egresado.pdf', NULL, 'Constancia de Egresado en UNAMBA  \r\n굽굾굿궀긍긎긏긐긑 PASO 1: Requisitos \r\n1. Comprobante de pago (S/. 15.00) \r\n2. Solicitud dirigida al Jefe de la Unidad de Servicios Académicos: \r\nIng. Marco Antonio Aguilar Espinoza \r\n괴괵괶괸괷 Lugares de pago: \r\n Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:30 pm \r\n Banco de la Nación \r\n Código: 1218 Constancia de Egresado \r\n깑깒깓깙깗깘깚깛깜깝 PASO 2: Entrega de requisitos \r\n Mesa de Partes \r\n Recojo de la constancia: Después de 3 días hábiles \r\n굽굾굿궀 PASO 3: Recojo de la Constancia \r\n Lugar: Servicios Académicos (SS. AA) \r\n꾳꾴꾵꾬꾶 Horario de atención: \r\n Lunes a viernes \r\n Mañanas: 8:00 am – 1:00 pm \r\n Tardes: 2:00 pm – 3:30 pm \r\n껵껶껴 NOTAS IMPORTANTES \r\n En caso de filiales, tramitar los documentos enviando al correo: \r\ntramitedocumentario@unamba.edu.pe \r\n Si no eres el titular, recoger el documento con una carta poder simple \r\n No se aceptarán pagos a la cuenta corriente de la UNAMBA \r\n \r\n \r\n \r\n'),
(40, '1766893331405_matricula de grado.pdf', 3.87816, 'General', '2025-12-27', 'uploads\\documentos\\1766893331405_matricula de grado.pdf', NULL, 'Constancia de Matrícula en UNAMBA para grado   \r\n굽굾굿궀긍긎긏긐긑 PASO 1: Requisitos \r\n1. Comprobante de pago (S/. 6.00) \r\n2. Solicitud dirigida al Jefe de la Unidad de Servicios Académicos: \r\nIng. Marco Antonio Aguilar Espinoza \r\n괴괵괶괸괷 Lugares de pago: \r\n Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:30 pm \r\n Banco de la Nación \r\n Código: 1220 Constancia de Matrícula \r\n깑깒깓깙깗깘깚깛깜깝 PASO 2: Entrega de requisitos \r\n Lugar: Servicios Académicos (SS. AA) \r\n굽굾굿궀 PASO 3: Recojo de la Constancia \r\n Tiempo estimado: Después de 3 días hábiles \r\n Lugar: Oficina de Servicios Académicos \r\n꾳꾴꾵꾬꾶 Horario de atención: \r\n Lunes a viernes \r\n Mañanas: 8:00 am – 1:00 pm \r\n Tardes: 2:00 pm – 3:30 pm \r\n껵껶껴 NOTAS IMPORTANTES \r\n En caso de filiales, tramitar los documentos enviando al correo: \r\ntramitedocumentario@unamba.edu.pe \r\n Si no eres el titular, recoger el documento con una carta poder simple \r\n No se aceptarán pagos a la cuenta corriente de la UNAMBA \r\n \r\n \r\n \r\n'),
(41, '1766893340158_matricula regular.pdf', 3.87466, 'General', '2025-12-27', 'uploads\\documentos\\1766893340158_matricula regular.pdf', NULL, 'Constancia de Matrícula Regular en UNAMBA  \r\n굽굾굿궀긍긎긏긐긑 PASO 1: Requisitos \r\n1. Comprobante de pago (S/. 6.00) \r\n괴괵괶괸괷 Lugares de pago: \r\n Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:15 pm \r\n Banco de la Nación (agencias/sucursales) \r\n Código: 1220 Constancia de Matrícula \r\n깑깒깓깙깗깘깚깛깜깝 PASO 2: Entrega del comprobante de pago \r\n굽굾굿궀 PASO 3: Recojo de la Constancia \r\n Lugar: Servicios Académicos (SS. AA) \r\n꾳꾴꾵꾬꾶 Horario de atención: \r\n Lunes a viernes \r\n Mañanas: 8:00 am – 12:40 pm \r\n Tardes: 2:00 pm – 3:15 pm \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n'),
(42, '1766893351486_informacionb adicional.pdf', 3.88329, 'General', '2025-12-27', 'uploads\\documentos\\1766893351486_informacionb adicional.pdf', NULL, 'La Universidad Nacional Micaela Bastidas de Apurímac (UNAMBA) está \r\nubicada en Abancay, Perú, con sede principal en Tamburco. Para pagar \r\ntrámites como certificados o constancias, puedes usar la app o web \r\nPágalo.pe del Banco de la Nación, ingresando el código \r\ncorrespondiente al trámite. \r\n궼궾궽 Universidad Nacional Micaela Bastidas de Apurímac (UNAMBA) \r\n Ubicación principal: \r\nAv. Inca Garcilaso de la Vega S/N, Tamburco, Abancay, Apurímac, Perú \r\n Teléfono: +51 83 321965 \r\n Correo institucional: contacto@unamba.edu.pe / \r\nunamba@unamba.edu.pe \r\n Sitio web oficial: www.unamba.edu.pe \r\nFacultades y carreras destacadas: \r\n Ingeniería: Civil, Minas, Informática y Sistemas, Agroindustrial, \r\nAgroecológica \r\n Administración: Empresas \r\n Educación y Ciencias Sociales: Ciencia Política, Educación Inicial \r\nIntercultural Bilingüe \r\n Medicina Veterinaria y Zootecnia \r\n괴괵괶괸괷 App y Web Págalo.pe del Banco de la Nación \r\nPágalo.pe es una plataforma digital para pagar tasas de entidades públicas \r\nsin ir al banco. \r\n¿Cómo usarla? \r\n1. Regístrate en www.pagalo.pe o descarga la app en Android/iOS. \r\n2. Ingresa tus datos personales y crea una contraseña. \r\n3. Busca el trámite por nombre o código (por ejemplo: \r\no 1213 Certificado de Estudios \r\no 1220 Constancia de Matrícula \r\no 1218 Constancia de Egresado \r\no 1246 Ficha de Seguimiento) \r\n4. Agrégalo al carrito y paga con: \r\n Tarjeta Visa, Mastercard, American Express \r\n Billetera Yape \r\n Agentes MultiRed \r\nBeneficios: \r\n Disponible 24/7 desde PC o celular \r\n No hay comisiones adicionales \r\n Notificación automática a la entidad pública \r\n Compatible con trámites de SUNAT, RENIEC, PNP, UNAMBA, entre otros \r\n \r\n'),
(43, '1766893370564_ficha de seguimiento.pdf', 3.87627, 'General', '2025-12-27', 'uploads\\documentos\\1766893370564_ficha de seguimiento.pdf', NULL, 'Ficha de Seguimiento en UNAMBA  \r\n굽굾굿궀긍긎긏긐긑 PASO 1: Requisitos \r\n1. Comprobante de pago (S/. 6.00) \r\n괴괵괶괸괷 Lugares de pago: \r\n Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:30 pm \r\n Banco de la Nación \r\n Código: 1246 Ficha de Seguimiento \r\n깑깒깓깙깗깘깚깛깜깝 PASO 2: Entrega del comprobante de pago \r\n굽굾굿궀 PASO 3: Recojo de la Ficha \r\n Lugar: Unidad de Servicios Académicos \r\n꾳꾴꾵꾬꾶 Horario de atención: \r\n Lunes a viernes \r\n Mañanas: 8:00 am – 1:00 pm \r\n Tardes: 2:00 pm – 3:30 pm \r\n껵껶껴 NOTAS IMPORTANTES \r\n En caso de filiales, tramitar los documentos enviando al correo: \r\ntramitedocumentario@unamba.edu.pe \r\n No se aceptarán pagos a la cuenta corriente de la UNAMBA \r\n \r\n'),
(44, '1766893377867_Historial académico unamba.pdf', 3.76613, 'General', '2025-12-27', 'uploads\\documentos\\1766893377867_Historial académico unamba.pdf', NULL, 'Historial académico unamba \r\n \r\n \r\n PASO 1: REQUISITOS \r\n1. Comprobante de pago (S/.6.00) \r\n• Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 01:00 pm \r\no Tardes: 2:00 pm – 03:15 pm \r\n• Agencias/sucursales Banco de la Nación \r\no Código: 1215 Historial Académico \r\n \r\n  PASO 2: ENTREGA DE COMPROBANTE DE PAGO \r\n \r\n  PASO 3: SS. AA – HISTORIAL ACADÉMICO \r\nRecoger en la oficina de Servicios Académicos (lunes a viernes) \r\n• Mañanas: 8:00 am – 12:40 pm \r\n• Tardes: 2:00 pm – 03:15 pm \r\n \r\n'),
(45, '1766893623373_constancia de egresado.pdf', 3.87804, 'General', '2025-12-27', 'uploads\\documentos\\1766893623373_constancia de egresado.pdf', NULL, 'Constancia de Egresado en UNAMBA  \r\n굽굾굿궀긍긎긏긐긑 PASO 1: Requisitos \r\n1. Comprobante de pago (S/. 15.00) \r\n2. Solicitud dirigida al Jefe de la Unidad de Servicios Académicos: \r\nIng. Marco Antonio Aguilar Espinoza \r\n괴괵괶괸괷 Lugares de pago: \r\n Caja UNAMBA (lunes a viernes) \r\no Mañanas: 8:00 am – 1:00 pm \r\no Tardes: 2:00 pm – 3:30 pm \r\n Banco de la Nación \r\n Código: 1218 Constancia de Egresado \r\n깑깒깓깙깗깘깚깛깜깝 PASO 2: Entrega de requisitos \r\n Mesa de Partes \r\n Recojo de la constancia: Después de 3 días hábiles \r\n굽굾굿궀 PASO 3: Recojo de la Constancia \r\n Lugar: Servicios Académicos (SS. AA) \r\n꾳꾴꾵꾬꾶 Horario de atención: \r\n Lunes a viernes \r\n Mañanas: 8:00 am – 1:00 pm \r\n Tardes: 2:00 pm – 3:30 pm \r\n껵껶껴 NOTAS IMPORTANTES \r\n En caso de filiales, tramitar los documentos enviando al correo: \r\ntramitedocumentario@unamba.edu.pe \r\n Si no eres el titular, recoger el documento con una carta poder simple \r\n No se aceptarán pagos a la cuenta corriente de la UNAMBA \r\n \r\n \r\n \r\n'),
(47, '1767665777870_MINSA - Carné Vacunación.pdf', 0.226612, 'Traslados', '2026-01-05', 'uploads\\documentos\\1767665777870_MINSA - Carné Vacunación.pdf', NULL, '15/10/25, 19:12 MINSA - Carné Vacunación\r\nhttps://carnetvacunacion.minsa.gob.pe/#/main/certificate 1/1\r\n'),
(48, '1767665858612_BANCO DE PREGUNTAS.pdf', 0.115502, 'Traslados', '2026-01-05', 'uploads\\documentos\\1767665858612_BANCO DE PREGUNTAS.pdf', NULL, 'UNIVERSIDAD NACIONAL MICAELA BASTIDAS DE APURIMAC \r\nESCUELA ACADEMICO PROFESIONAL INGENIERIA INFORMATICA Y SISTEMAS \r\n \r\n \r\nBANCO DE PREGUNTAS - SISTEMAS OPERATIVOS \r\nHistoria de los Sistemas Operativos \r\n1. ¿Qué es un sistema operativo y cuál es su función principal dentro de un computador? \r\n2. Explica cómo evolucionaron los sistemas operativos desde los sistemas por lotes hasta \r\nlos actuales sistemas distribuidos. \r\n3. Menciona tres sistemas operativos modernos y describe una característica destacada de \r\ncada uno. \r\n4. ¿Qué diferencias existen entre un sistema operativo monousuario y uno multiusuario? \r\n5. ¿Por qué el desarrollo de UNIX marcó un punto importante en la historia de los sistemas \r\noperativos? \r\nGestión de Procesos \r\n6. Define qué es un proceso en un sistema operativo y cómo se diferencia de un programa. \r\n7. Explica el propósito del PCB (Process Control Block) en la gestión de procesos. \r\n8. ¿Qué mecanismos utiliza un sistema operativo para la sincronización y comunicación \r\nentre procesos? \r\n9. Diferencia entre proceso e hilo (thread), y menciona una ventaja del uso de hilos. \r\n10. ¿Qué papel cumple el planificador (scheduler) en la gestión de procesos? \r\n11. Explica cómo el sistema operativo garantiza la seguridad y aislamiento entre procesos. \r\nGestión del Sistema de Entrada y Salida \r\n12. ¿Qué se entiende por sistema de entrada y salida (E/S) en un sistema operativo? \r\n13. Explica las funciones del subsistema de E/S. \r\n14. Menciona tres ejemplos de dispositivos de entrada y salida y su importancia en la \r\ninteracción con el sistema. \r\n15. Describe cómo el sistema operativo utiliza interrupciones para optimizar la E/S. \r\n16. ¿Qué ventajas ofrece el uso de buffers y spooling en la gestión de E/S? \r\n17. ¿Cómo influye la gestión de E/S en el rendimiento general del sistema operativo? \r\nPlanificación de Procesos y Mecanismos de Procesos \r\n18. Define qué es la planificación de procesos y su objetivo dentro del sistema operativo. \r\n19. Explica cómo funciona el algoritmo Round Robin y en qué tipo de sistemas se utiliza. \r\n20. ¿Qué criterios se emplean para evaluar la eficiencia de un planificador de procesos? \r\n21. Describe la diferencia entre planificación a corto, mediano y largo plazo. \r\n22. Explica cómo se coordinan los mecanismos de creación, suspensión y finalización de \r\nprocesos. \r\nAdministración de Dispositivos de Entrada y Salida, Dispositivos y Controladores \r\n23. Define qué es un controlador de dispositivo y cuál es su función principal. \r\n24. Explica la diferencia entre controlador de dispositivo y driver. \r\n25. ¿Cómo se comunican los dispositivos de hardware con el sistema operativo? \r\n26. ¿Qué es una interfaz de controlador estándar (como los drivers genéricos) y por qué son \r\nimportantes? \r\n27. ¿Por qué los sistemas operativos modernos utilizan controladores virtuales en entornos \r\nde virtualización? \r\n'),
(49, '1767666001977_Qué son los mamíferos.pdf', 3.73944, 'Traslados', '2026-01-05', 'uploads\\documentos\\1767666001977_Qué son los mamíferos.pdf', NULL, '¿Qué son los mamíferos? \r\nLos mamíferos son un grupo de animales vertebrados que se caracterizan por \r\ntener glándulas mamarias, las cuales producen leche para alimentar a sus \r\ncrías. \r\n  Definición biológica \r\nLos mamíferos pertenecen a la clase Mammalia y se distinguen por poseer \r\nsangre caliente, pulmones para respirar y un sistema nervioso desarrollado. \r\n  Definición general \r\nSon animales que, en su mayoría, nacen del vientre de la madre, tienen pelo o \r\npelaje y cuidan a sus crías durante sus primeras etapas de vida. \r\n  Definición según su hábitat \r\nLos mamíferos pueden vivir en tierra, agua o aire. Por ejemplo, existen mamíferos \r\nterrestres, marinos y voladores. \r\n  Características principales de los mamíferos \r\n• Son vertebrados \r\n• Tienen sangre caliente \r\n• Poseen pelo o pelaje \r\n• Las crías se alimentan de leche materna \r\n• Respiran por pulmones \r\n \r\n');

-- Ajustar secuencia
SELECT setval(pg_get_serial_sequence('documentos', 'id'), (SELECT MAX(id) FROM documentos));


-- =========================
-- TABLA: registro_usuarios
-- =========================
CREATE TABLE registro_usuarios (
    id SERIAL PRIMARY KEY,
    apellidos_nombres VARCHAR(255) NOT NULL,
    nombre_usuario VARCHAR(255) NOT NULL UNIQUE,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL
);

-- =========================
-- DATOS: registro_usuarios
-- =========================
INSERT INTO registro_usuarios
(id, apellidos_nombres, nombre_usuario, correo, contraseña)
VALUES
(24, 'Deyli Jara', 'Deyli', 'deyli@gmail.com', '123456'),
(25, 'moises', 'moises', '222169@unamba.edu.pe', 'moises'),
(26, 'Chahuayo Juarez Noel', '222159', '222159@unamba.edu.pe', '123456'),
(27, 'luz', 'luz1', 'luz@unamba.edu.pe', '123456'),
(45, 'maria ', 'magdalena', '2221196@unamba.edu.pe', '123456');

SELECT setval(pg_get_serial_sequence('registro_usuarios', 'id'),
              (SELECT MAX(id) FROM registro_usuarios));


-- =========================
-- TABLA: usuarios
-- =========================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(255) UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    registro_usuario_id INTEGER,
    CONSTRAINT fk_usuario_registro
        FOREIGN KEY (registro_usuario_id)
        REFERENCES registro_usuarios(id)
        ON DELETE CASCADE
);

-- =========================
-- DATOS: usuarios
-- =========================
INSERT INTO usuarios
(id, nombre_usuario, email, contraseña, registro_usuario_id)
VALUES
(9, 'Deyli', 'deyli@gmail.com', '$2a$10$7.cGsRfA8kFHOrBx3wyuCeJEIIPZIq2dtrJFM.DzN24vRWqJouywC', NULL),
(10, 'moises', '222169@unamba.edu.pe', '$2a$10$9SudTXagHO5/5k9cLCIn7uxUtcwbluofJ6mKRQsa0HaTPzWMSCTnG', NULL),
(11, '222159', '222159@unamba.edu.pe', '$2a$10$1TMUPu.P23ZiqiO99ccZkOOomIMgKE3B0jCi5i16QJIDTCCmQL.Lu', NULL),
(12, 'luz1', 'luz@unamba.edu.pe', '$2a$10$CLLSck5btqDOdy.tmQ5v1O71iOhHgESxpR6RHJFzUcEK6cdjA0dHS', NULL),
(21, 'magdalena', '2221196@unamba.edu.pe', '$2a$10$MaPCeVKWLJmdP3KkYb1B6OIIfH0BeSzbmwoDhUA4MDfK1YzAugG8a', NULL);

SELECT setval(pg_get_serial_sequence('usuarios', 'id'),
              (SELECT MAX(id) FROM usuarios));

COMMIT;
