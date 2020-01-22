/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104.modelo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author sergio
 */
public abstract class Util {

    public static DefaultComboBoxModel getCBModelOfEnum(Class c, String desc) {
        if (c.isEnum()) {
            Object constantes[] = c.getEnumConstants();
            DefaultComboBoxModel cb = new DefaultComboBoxModel();
            cb.addElement("Seleccione " + desc + "...");
            for (int i = 0; i < constantes.length; i++) {
                Object object = constantes[i];
                cb.addElement(object);
            }
            return cb;
        } else {
            return null;
        }
    }
    public static final DateTimeFormatter formatter = 
                                      DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String getToday(){
        LocalDate today=LocalDate.now();
        String stringLocalDate = today.format(Util.formatter);
        return stringLocalDate;
    }
    
    public static List getNextWeekend(){
       List datesNW = new LinkedList<LocalDate>();
        int max=8; 
        LocalDate nearFriday=LocalDate.now();
        while(nearFriday.getDayOfWeek()!=DayOfWeek.FRIDAY){
            nearFriday=nearFriday.plusDays(1);
        }        
        for(int i=0; i<max;i++){
            datesNW.add(nearFriday);
            datesNW.add(nearFriday.plusDays(1));
            datesNW.add(nearFriday.plusDays(2));
            nearFriday=nearFriday.plusDays(7);
        }
        return datesNW;
    }
    
    public static boolean esFechaValida(String cadenaFecha){
        boolean esValida = false;
        DateTimeFormatter formatoFecha = 
                             DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(cadenaFecha, formatoFecha);
        } catch (DateTimeParseException e) { 
            fecha = null;
        }
        if (fecha != null && 
                         fecha.format(formatoFecha).equals(cadenaFecha)){
            esValida=true;
        }
        return esValida;
    }
   //Una variente:
    public static boolean esFechaValida(String cadenaFecha, 
                                                   String formato){
        boolean esValida = false;
        DateTimeFormatter formatoFecha = 
                                 DateTimeFormatter.ofPattern(formato);
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(cadenaFecha, formatoFecha);
        } catch (DateTimeParseException e) { 
            fecha = null;
        }
        if (fecha != null && 
              fecha.format(formatoFecha).equals(cadenaFecha)){
            esValida=true;
        }
    
           return esValida;
    }

}
