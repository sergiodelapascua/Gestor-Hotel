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
public class HabitacionCongreso {
    
    private int numeroHab;
    private TipoHabitacion tipo;

    public HabitacionCongreso(int numeroHab, TipoHabitacion tipo) {
        this.numeroHab = numeroHab;
        this.tipo = tipo;
    }

    public int getNumeroHab() {
        return numeroHab;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "HabitacionCongreso{" + "numeroHab=" + numeroHab + ", tipo=" + tipo + '}';
    }
}
