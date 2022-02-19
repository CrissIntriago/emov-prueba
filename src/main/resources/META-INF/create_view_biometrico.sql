-- DROP VIEW public.marcacion;
CREATE OR REPLACE VIEW public.marcacion
 AS
 SELECT it.id,
    pe.emp_code,
    pe.first_name,
    pe.last_name,
    it.c,
    date(it.punch_time) AS date_,
    it.punch_time::time without time zone AS time_,
    it.terminal_alias,
    it.area_alias,
    it.mobile,
    it.punch_state,
        CASE
            WHEN it.punch_state::text ~~ '0'::text THEN 'ENTRADA'::text
            WHEN it.punch_state::text ~~ '1'::text THEN 'SALIDA'::text
            WHEN it.punch_state::text ~~ '2'::text THEN 'DESCANSO'::text
            WHEN it.punch_state::text ~~ '3'::text THEN 'ENTRADA DESCANSO'::text
            WHEN it.punch_state::text ~~ '4'::text THEN 'ENTRADA HE'::text
            WHEN it.punch_state::text ~~ '5'::text THEN 'SALIDA HE'::text
            ELSE NULL::text
        END AS event_name
   FROM iclock_transaction it
     JOIN personnel_employee pe ON pe.id = it.emp_id;
-- ### Obtiene la primera y la ultima marcacion de empleado
-- DROP VIEW primera_ultima_marcacion;
CREATE OR REPLACE VIEW primera_ultima_marcacion AS 
SELECT ROW_NUMBER () OVER (ORDER BY 1), pu.* FROM (
	SELECT DISTINCT emp_code, first_name, date_, MIN(time_) time_ FROM marcacion GROUP BY 1,2,3
	UNION ALL 
	SELECT DISTINCT emp_code, first_name, date_, MAX(time_) time_ FROM marcacion GROUP BY 1,2,3 ORDER BY 1,3,4
) pu; 
-- Funciones para obtener los segundos trabajados diarios
-- Solo con fecha
CREATE OR REPLACE FUNCTION get_working_time_in_a_day(date_ date, end_date date, swt TIME, ewt TIME) RETURNS INT AS
$$
DECLARE
  sd TIMESTAMP; ed TIMESTAMP; swdt TIMESTAMP; ewdt TIMESTAMP; seconds INT; sdt TIMESTAMP; edt TIMESTAMP;
BEGIN
  sdt = date_ || ' ' || swt; 
  edt = end_date || ' ' || ewt;  
  swdt = date_ || ' ' || swt; -- work start datetime for a day
  ewdt = date_ || ' ' || ewt; -- work end datetime for a day
  IF (ewt IS NULL) THEN -- case 1 and 2
    seconds = 0;
  END IF;
  IF (sdt < swdt AND edt <= swdt) THEN -- case 1 and 2
    seconds = 0;
  END IF;
  IF (sdt < swdt AND edt > swdt AND edt <= ewdt) THEN -- case 3 and 4
    seconds = EXTRACT(EPOCH FROM (edt - swdt));
  END IF;
  IF (sdt < swdt AND edt > swdt AND edt > ewdt) THEN  -- case 5
    seconds = EXTRACT(EPOCH FROM (ewdt - swdt));
  END IF;
  IF (sdt = swdt AND edt > swdt AND edt <= ewdt) THEN -- case 6 and 7
    seconds = EXTRACT(EPOCH FROM (edt - sdt));
  END IF;
  IF (sdt = swdt AND edt > ewdt) THEN                 -- case 8
    seconds = EXTRACT(EPOCH FROM (ewdt - sdt));
  END IF;
  IF (sdt > swdt AND edt <= ewdt) THEN                -- case 9 and 10
    seconds = EXTRACT(EPOCH FROM (edt - sdt));
  END IF;
  IF (sdt > swdt AND sdt < ewdt AND edt > ewdt) THEN  -- case 11
    seconds = EXTRACT(EPOCH FROM (ewdt - sdt));
  END IF;
  IF (sdt >= ewdt AND edt > ewdt) THEN                -- case 12 and 13
    seconds = 0;
  END IF;

  RETURN seconds;
END;
$$
LANGUAGE plpgsql;
-- Con fecha y hora
CREATE OR REPLACE FUNCTION get_working_time_in_a_day(sdt TIMESTAMP, edt TIMESTAMP, swt TIME, ewt TIME) RETURNS INT AS
$$
DECLARE
  sd TIMESTAMP; ed TIMESTAMP; swdt TIMESTAMP; ewdt TIMESTAMP; seconds INT;
BEGIN
  swdt = sdt::DATE || ' ' || swt; -- work start datetime for a day
  ewdt = sdt::DATE || ' ' || ewt; -- work end datetime for a day
  IF (ewt IS NULL) THEN -- case 1 and 2
    seconds = 0;
  END IF;
  IF (sdt < swdt AND edt <= swdt) THEN -- case 1 and 2
    seconds = 0;
  END IF;
  IF (sdt < swdt AND edt > swdt AND edt <= ewdt) THEN -- case 3 and 4
    seconds = EXTRACT(EPOCH FROM (edt - swdt));
  END IF;
  IF (sdt < swdt AND edt > swdt AND edt > ewdt) THEN  -- case 5
    seconds = EXTRACT(EPOCH FROM (ewdt - swdt));
  END IF;
  IF (sdt = swdt AND edt > swdt AND edt <= ewdt) THEN -- case 6 and 7
    seconds = EXTRACT(EPOCH FROM (edt - sdt));
  END IF;
  IF (sdt = swdt AND edt > ewdt) THEN                 -- case 8
    seconds = EXTRACT(EPOCH FROM (ewdt - sdt));
  END IF;
  IF (sdt > swdt AND edt <= ewdt) THEN                -- case 9 and 10
    seconds = EXTRACT(EPOCH FROM (edt - sdt));
  END IF;
  IF (sdt > swdt AND sdt < ewdt AND edt > ewdt) THEN  -- case 11
    seconds = EXTRACT(EPOCH FROM (ewdt - sdt));
  END IF;
  IF (sdt >= ewdt AND edt > ewdt) THEN                -- case 12 and 13
    seconds = 0;
  END IF;

  RETURN seconds;
END;
$$
LANGUAGE plpgsql;

-- Get work time difference
CREATE OR REPLACE FUNCTION get_working_time(sdt TIMESTAMP, edt TIMESTAMP, swt TIME, ewt TIME) RETURNS INT AS
$$
DECLARE
  seconds INT = 0;
  strst VARCHAR(9) = ' 00:00:00';
  stret VARCHAR(9) = ' 23:59:59';
  tend TIMESTAMP; tempEdt TIMESTAMP;
  x int;
BEGIN
  <<test>>
  WHILE sdt <= edt LOOP
  tend = sdt::DATE || stret; -- get the false end datetime for start time
  IF edt >= tend 
  THEN
    tempEdt = tend;
  ELSE
    tempEdt = edt;
  END IF;
  -- skip saturday and sunday
  x = EXTRACT(DOW FROM sdt);
  if (x > 0 AND x < 6)
  THEN
     seconds = seconds + get_working_time_in_a_day(sdt, tempEdt, swt, ewt); 
   ELSE
  --   RAISE NOTICE 'MISSED A DAY';
   END IF;

  sdt = (sdt + (INTERVAL '1 DAY'))::DATE || strst;
  END LOOP test;
  --RAISE NOTICE 'diff in minutes = %', (seconds / 60);
  RETURN seconds;
END;
$$
LANGUAGE plpgsql;
-- Obtiene la primera y la ultima marcacion del cada dia
CREATE OR REPLACE VIEW marcacion_diaria AS 
SELECT DISTINCT fm. emp_code, fm.first_name, fm.date_ start_date, fm.start_time, 
CASE WHEN fm.start_time = lm.time_ THEN NULL ELSE lm.time_ END end_time, 
get_working_time_in_a_day(fm.date_, fm.date_, start_time, CASE WHEN fm.start_time = lm.time_ THEN NULL ELSE lm.time_ END) wt_second,
ROW_NUMBER () OVER (ORDER BY 1) id
FROM (SELECT DISTINCT fm. emp_code, fm.first_name, fm.date_ , MIN(fm.time_) start_time FROM marcacion fm GROUP BY 1,2,3 ORDER BY 1,3) fm 
LEFT JOIN (SELECT DISTINCT emp_code, date_, MAX(time_) time_ FROM marcacion GROUP BY 1,2) lm ON (lm.emp_code = fm.emp_code AND lm.date_ = fm.date_)
ORDER BY 1,3,4




