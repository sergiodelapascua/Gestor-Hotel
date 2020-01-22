/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104.modelo;

/**
 *
 * @author sergio
 */
public class Evento {

    private int id;
    private TipoEvento tipo;
    private String nombre;
    private int numDias; 
    private int asistentes;
    private MenuBanquete menu;
    private HabitacionCongreso habitaciones;

    public Evento(int id, TipoEvento tipo, String nombre, int numDias, int asistentes) {// Jornada
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
    }

    public Evento(TipoEvento tipo, String nombre, int numDias, int asistentes) {// Jornada
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
    }

    public Evento(int id, TipoEvento tipo, String nombre, int numDias, int asistentes, MenuBanquete menu) { //Banquete
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
        this.menu = menu;
    }

    public Evento(TipoEvento tipo, String nombre, int numDias, int asistentes, MenuBanquete menu) { //Banquete
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
        this.menu = menu;
    }

    public Evento(int id, TipoEvento tipo, String nombre, int numDias, int asistentes, HabitacionCongreso habitaciones) { //Congreso
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
        this.habitaciones = habitaciones;
    }

    public Evento(TipoEvento tipo, String nombre, int numDias, int asistentes, HabitacionCongreso habitaciones) { //Congreso
        this.tipo = tipo;
        this.nombre = nombre;
        this.numDias = numDias;
        this.asistentes = asistentes;
        this.habitaciones = habitaciones;
    }
    
    public int getId() {
        return id;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public MenuBanquete getMenu() {
        return menu;
    }

    public void setMenu(MenuBanquete menu) {
        this.menu = menu;
    }

    public HabitacionCongreso getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(HabitacionCongreso habitaciones) {
        this.habitaciones = habitaciones;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }
    
    @Override
    public String toString() {
        return "Evento{" + "id=" + id + ", tipo=" + tipo + ", nombre=" + nombre + ", numDias=" + numDias + ", asistentes=" + asistentes + ", menu=" + menu + ", habitaciones=" + habitaciones + '}';
    }
    
}
