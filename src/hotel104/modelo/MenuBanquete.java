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
public class MenuBanquete {
        
    private Menu primero;
    private Menu segundo;

    public MenuBanquete(Menu primero, Menu segundo) {
        this.primero = primero;
        this.segundo = segundo;
    }

    public Menu getPrimero() {
        return primero;
    }

    public void setPrimero(Menu primero) {
        this.primero = primero;
    }

    public Menu getSegundo() {
        return segundo;
    }

    public void setSegundo(Menu segundo) {
        this.segundo = segundo;
    }

    @Override
    public String toString() {
        return "MenuBanquete{" + "primero=" + primero + ", segundo=" + segundo + '}';
    }
    
}
