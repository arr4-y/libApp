/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lenovo
 * Created: 16 jul. 2023
 */

--Triggers
CREATE TRIGGER `tgPrestamo` AFTER INSERT ON `prestamos` FOR EACH ROW UPDATE libros set cantidad=cantidad-1 where libro_id=new.libro_id

