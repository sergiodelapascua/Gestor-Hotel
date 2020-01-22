/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104.modelo;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author sergio
 */
public class ManejoBD {
    
    private String driver = "org.mariadb.jdbc.Driver";    
    private String database = "di_t02_p02_e1";// Nombre de la base de datos    
    //private String hostname = "192.168.0.89";// Host    
    private String hostname = "localhost";// Host    
    private String port = "3306";// Puerto
    // Ruta de nuestra base de datos
    private String url = "jdbc:mariadb://" + hostname + ":" + port + "/" + database;    
    private String username = "root";// Nombre de usuario    
    private String password = "";// Clave de usuario
    
    private Connection connection;

    public ManejoBD() {
    }
    
    
    public void realizarConexion() {
        connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        //Conexión realizada
    }
    
    public void cerrarConexion() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Cliente> consultarClientes(){
        List<Cliente> lista = new ArrayList();
        Cliente c = null;
        
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM clientes");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setDNI(rs.getString("dni"));
                c.setNombre(rs.getString("nombre"));
                c.setApellidos(rs.getString("apellidos"));
                c.setDireccion(rs.getString("direccion"));
                c.setTelefono(rs.getInt("telefono"));
                lista.add(c);
            }
            cerrarConexion();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Salon> consultarSalones(){
        List<Salon> lista = new ArrayList();
        Salon s = null;
        
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM salones");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                s = new Salon();
                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setCapacidad(rs.getInt("capacidad"));
               
                lista.add(s);
            }
            cerrarConexion();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Reserva> consultarReservas(List<Cliente> clientes, List<Salon> salones, List<Evento> eventos){
        List<Reserva> lista = new ArrayList();
        Reserva r = null;
        
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservas WHERE fecha > NOW()");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int idCliente = rs.getInt(1);
                Cliente cl = null;
                for(Cliente x: clientes){
                    if(x.getId()==idCliente)
                        cl = x;
                }
                int idSalon = rs.getInt(2);
                Salon sl = null;
                for(Salon x: salones){
                    if(x.getId()==idSalon)
                        sl = x;
                }
                int idEvento = rs.getInt(3);
                Evento ev = null;
                for(Evento x: eventos){
                    if(x.getId()==idEvento)
                        ev = x;
                }
                java.sql.Date date = rs.getDate(4);
                LocalDate localD = date.toLocalDate(); 
                lista.add(new Reserva(localD, cl, sl, ev));
            }
            cerrarConexion();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Evento> consultarEventos() {
        List<Evento> lista = new ArrayList();
        Evento e = null;
        
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM eventos WHERE tipo = 'Jornada' AND id IN(SELECT id_evento FROM reservas WHERE fecha > NOW())");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                String tipo = rs.getString(3);
                TipoEvento evento = null;
                for (TipoEvento evt : TipoEvento.values()) {
                    if (evt.toString().equalsIgnoreCase(tipo)) 
                        evento = evt;
                }
                int asit = rs.getInt(4);
                int dias = rs.getInt(5);
                e = new Evento(id, evento, nombre, dias, asit);//EVENTOS JORNADA
                lista.add(e);
            }
            cerrarConexion();
        }catch(SQLException ex){
            System.out.println("Conexion fallida en las jornadas");
            ex.printStackTrace();
        }
        
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM eventos, menu_banquetes WHERE tipo = 'Banquete' AND id IN(SELECT id_evento FROM reservas WHERE fecha > NOW()) AND id = menu_banquetes.id_evento");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                String tipo = rs.getString(3);
                TipoEvento evento = null;
                for (TipoEvento evt : TipoEvento.values()) {
                    if (evt.toString().equalsIgnoreCase(tipo)) 
                        evento = evt;
                }
                int asit = rs.getInt(4);                
                int dias = rs.getInt(5);
                String primero = rs.getString(6);
                Menu p = null;
                for (Menu me : Menu.values()) {
                    if (me.toString().equalsIgnoreCase(primero)) 
                        p = me;
                }
                String segundo = rs.getString(7);
                Menu s = null;
                for (Menu me : Menu.values()) {
                    if (me.toString().equalsIgnoreCase(segundo)) 
                        s = me;
                }
                e = new Evento(id, evento, nombre, dias, asit, new MenuBanquete(p, s));//EVENTOS BANQUETES
                lista.add(e);
            }
            cerrarConexion();
        }catch(SQLException ex){
            System.out.println("Conexion fallida en las banquetes");
            ex.printStackTrace();
        }
          
        try {
            realizarConexion();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM eventos WHERE eventos.tipo = 'Congreso' AND id IN(SELECT id_evento FROM reservas WHERE fecha > NOW())");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                String tipo = rs.getString(3);
                TipoEvento evento = null;
                for (TipoEvento evt : TipoEvento.values()) {
                    if (evt.toString().equalsIgnoreCase(tipo)) 
                        evento = evt;
                }
                int asit = rs.getInt(4);
                int dias = rs.getInt(5);
                
                e = new Evento(id, evento, nombre, dias, asit);//EVENTOS CONGRESO
                lista.add(e);
            }
            cerrarConexion();
        }catch(SQLException ex){
            System.out.println("Conexion fallida en las congresos");
            ex.printStackTrace();
        }
        
        return lista;
    }

    public void insertarReserva(Reserva reserva){
        realizarConexion();
        
        int idEvento = 0;
         try( PreparedStatement p = connection.prepareStatement("INSERT INTO reservas VALUES (?,?,?,?)");
                PreparedStatement p1 = connection.prepareStatement("INSERT INTO eventos (nombre, tipo, asistentes, num_dias) VALUES (?,?,?,?)");
                PreparedStatement p2 = connection.prepareStatement("SELECT MAX(id) FROM eventos");
                PreparedStatement p3 = connection.prepareStatement("INSERT INTO menu_banquetes VALUES (?,?,?)");
                PreparedStatement p4 = connection.prepareStatement("INSERT INTO habitacion_congresos VALUES (?,?,?)");) {     
            p1.setString(1,reserva.getNombre());
            p1.setString(2,reserva.getTipoEvento());
            p1.setInt(3,reserva.getAsistentes());
            p1.setInt(4,reserva.getNumDias());
            p1.executeUpdate();
            
            ResultSet resultado = p2.executeQuery();
            while(resultado.next()){
                idEvento = resultado.getInt(1);   
                reserva.setId(idEvento);             
            }
            
            if(reserva.getEvento().getMenu() != null){
                p3.setInt(1, idEvento);
                p3.setString(2, reserva.getEvento().getMenu().getPrimero().toString());
                p3.setString(3, reserva.getEvento().getMenu().getSegundo().toString());
                p3.executeUpdate();
            }
            
            if(reserva.getEvento().getHabitaciones() != null){
                p4.setInt(1, idEvento);
                p4.setInt(2, reserva.getEvento().getHabitaciones().getNumeroHab());
                p4.setString(3, reserva.getEvento().getHabitaciones().getTipo().toString());
                String tipoHab = reserva.getEvento().getHabitaciones().getTipo().toString();
                p4.executeUpdate();
            }
            
            p.setInt(1, reserva.getClienteID());
            p.setInt(2, reserva.getSalon());
            p.setInt(3, idEvento);
            p.setDate(4, java.sql.Date.valueOf(reserva.getDate()));
            p.executeUpdate();
            
            if(reserva.getEvento().getNumDias() == 3){
                p.setDate(4, java.sql.Date.valueOf(reserva.getDate().plusDays(1)));
                p.executeUpdate();
                p.setDate(4, java.sql.Date.valueOf(reserva.getDate().plusDays(2)));
                p.executeUpdate();
            } else if(reserva.getEvento().getNumDias() == 2){
                p.setDate(4, java.sql.Date.valueOf(reserva.getDate().plusDays(1)));
                p.executeUpdate();
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cerrarConexion();
    }
    
    public void borrarReserva(Reserva reserva){
        realizarConexion();
        try(PreparedStatement p = connection.prepareStatement("DELETE FROM eventos WHERE id = (?)");){
            p.setInt(1, reserva.getReserva());
            p.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        cerrarConexion();
    }
}
