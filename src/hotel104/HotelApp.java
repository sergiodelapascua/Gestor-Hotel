/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104;

import hotel104.modelo.*;
import hotel104.vista.VentanaPrincipal;
import java.util.*;

/**
 *
 * @author sergio
 */
public class HotelApp {
    
    private List<Reserva> reservas;
    private List<Cliente> clientes;
    private List<Salon> salones;
    private List<Evento> eventos;
    private ManejoBD manejo;

    public HotelApp() {
        manejo = new ManejoBD();
        this.clientes = manejo.consultarClientes();
        this.salones = manejo.consultarSalones();
        this.eventos = manejo.consultarEventos();
        this.reservas = manejo.consultarReservas(clientes, salones, eventos);
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Salon> getSalones() {
        return salones;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
    
    public Cliente buscarDni(String D){
        for (Cliente c : clientes) {
            if(c.getDNI().equalsIgnoreCase(D))
                return c;
        }
        return null;
    }
    
    public static void main(String[] args) {        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
      
    public void añadirReserva(Reserva r){
        manejo.insertarReserva(r);
        reservas.add(r);
        
        if(r.getEvento().getNumDias() == 3){
            reservas.add(new Reserva(r.getDate().plusDays(1), r.getObjCliente(), r.getObjSalon(), r.getEvento()));
            reservas.add(new Reserva(r.getDate().plusDays(2), r.getObjCliente(), r.getObjSalon(), r.getEvento()));
        } else if(r.getEvento().getNumDias() == 2){
            reservas.add(new Reserva(r.getDate().plusDays(1), r.getObjCliente(), r.getObjSalon(), r.getEvento()));           
        }
    }
    
    public void borrarReserva(Reserva r){
        List<Reserva> borrar = new ArrayList<>();
        
        for (Reserva reserva : reservas) {
            if(reserva.getReserva() == r.getReserva())
                borrar.add(reserva);
        }
        
        for (Reserva reser : borrar) {
            reservas.remove(reser);
        }
        
        manejo.borrarReserva(r);
    }
}
