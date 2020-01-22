/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author sergio
 */
public class Reserva {
    
    private LocalDate fecha;
    private Cliente cliente;
    private Salon salon;
    private Evento evento;
    //ID, FECHA, NOMBRE, TIPO, SALON, ASISTENTES, ACCION
    public Reserva(LocalDate fecha, Cliente cliente, Salon salon, Evento evento) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.salon = salon;
        this.evento = evento;
    }
    
    public int getReserva() {
        return evento.getId();
    }
    
    public String getFecha() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(fecha);
    }
    
    public LocalDate getDate(){ return fecha;}
    
    public String getNombre(){
        return evento.getNombre();
    }
    
    public String getTipoEvento(){
        return evento.getTipo().toString();
    } 
    
    public int getNumDias(){
        return evento.getNumDias();
    }
    
    public void setId(int id){
        evento.setId(id);
    }
    
    public int getSalon(){
        return salon.getId();
    }
    
    public int getAsistentes(){
        return evento.getAsistentes();
    }

    public Evento getEvento() {
        return evento;
    }
    
    public Salon getObjSalon(){ return salon;}
    
    public Cliente getObjCliente(){ return cliente; }
    
    public int getClienteID(){
        return cliente.getId();
    }

    @Override
    public String toString() {
        return "Reserva{" + "fecha=" + fecha + ", cliente=" + cliente + ", salon=" + salon + ", evento=" + evento + '}';
    }
}
