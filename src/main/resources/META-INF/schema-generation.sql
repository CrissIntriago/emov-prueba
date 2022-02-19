CREATE SCHEMA IF NOT EXISTS test_schema;
CREATE OR REPLACE VIEW auth.menu_descripcion AS 
SELECT m.descripcion, m.es_publico, rol.nombre, menu_rol.opciones 
FROM auth.menu m INNER JOIN auth.menu_rol ON menu_rol.menu = m.id 
INNER JOIN auth.rol ON rol.id = menu_rol.rol;

-- INSERT INTO conf.valores VALUES(DEFAULT, 'LISTAR_TRAMITES_POR', NULL, 1); -- 0 TODOS, 1 POR UNIDAD DEL DISTRIBUTIVO