/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel104.vista;

import hotel104.HotelApp;
import hotel104.modelo.*;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.*;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author sergio
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private HotelApp h;
    private int contPlatos;
    ModeloTablaReservas modeloReservas;
    private List<JCheckBox> listaChechBox;
    private List<LocalDate> fechasValidas;
    private int diaSemana;
    
    
    private String nombreNuevaReserva;
    private TipoEvento tipoDeEvento;
    private LocalDate fechaQueReserva;
    private Cliente clienteQueReserva;
    private Salon salonQueReserva;
    
    private Menu plato1;
    private Menu plato2;
    
    private TipoHabitacion tipoHabitacion;
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        h = new HotelApp();
        contPlatos = 0;
        modeloReservas = new ModeloTablaReservas();
        initComponents();
        listaChechBox = new ArrayList();
        listaChechBox.add(jCheckBoxPlato1);
        listaChechBox.add(jCheckBoxPlato2);
        listaChechBox.add(jCheckBoxPlato3);
        listaChechBox.add(jCheckBoxPlato4);
        listaChechBox.add(jCheckBoxPlato5);
        listaChechBox.add(jCheckBoxPlato6);
        this.setLocationRelativeTo(null);
        refrescarTable();
        
        this.jSpinnerFecha.setEditor(new javax.swing.JSpinner.DateEditor(this.jSpinnerFecha, "dd-MM-yyyy"));
        fechasValidas = Util.getNextWeekend();
    }
    
    class ModeloTablaClientes extends AbstractTableModel{
        
        String[] columnNames = {"ID",
        "DNI",
        "NOMBRE",
        "APELLIDOS",
        "DIRECCION",
        "TELEFONO"};
        
        @Override
        public int getRowCount() {
            return h.getClientes().size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
                        
            Cliente c = h.getClientes().get(rowIndex);
            
            switch (columnIndex) {
                case 0:
                    return c.getId();
                case 1:
                    return c.getDNI();
                case 2:
                    return c.getNombre();
                case 3:
                    return c.getApellidos();
                case 4:
                    return c.getDireccion();
                case 5:
                    return c.getTelefono();
                default:
                    return null;
            }
            //return data[rowIndex][columnIndex];
        }
    
        @Override
        public String getColumnName(int c){
            return columnNames[c];
        }
    }
    
    class ModeloTablaReservas extends AbstractTableModel{
        
        String[] columnNames = {"ID",
        "FECHA",
        "NOMBRE",
        "TIPO",
        "SALON",
        "ASISTENTES",
        "ACCION"};
        
        private Object[][] data = {};
        
        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        //@Override
        public Object getValueAt(int rowIndex, int columnIndex) {            
            return data[rowIndex][columnIndex];
        }
        
        @Override
        public String getColumnName(int c){
            return columnNames[c];
        }
        
        @Override
        public Class getColumnClass(int colum){
            return getValueAt(0,colum).getClass();
        }
        
        public void refreshTableModel(List<Reserva> lista){
                        
            if(!lista.isEmpty()){
                int numCol = columnNames.length + 1;
                int numFilas = lista.size();
                
                data = new Object[numFilas][numCol];
                ImageIcon icono = new ImageIcon("icon.png");
                ImageIcon iconoEscala = new ImageIcon(icono.getImage().getScaledInstance(18, -1, java.awt.Image.SCALE_DEFAULT));
                
                for (int i = 0; i < numFilas; i++) {
                    data[i][0] = lista.get(i).getReserva();
                    data[i][1] = lista.get(i).getFecha();
                    data[i][2] = lista.get(i).getNombre();
                    data[i][3] = lista.get(i).getTipoEvento();
                    data[i][4] = lista.get(i).getSalon();
                    data[i][5] = lista.get(i).getAsistentes();
                    data[i][6] = new JButton(iconoEscala);
                    data[i][7] = lista.get(i);
                    final Reserva r = lista.get(i);
                    ((JButton) data[i][6]).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            int opcion = JOptionPane.showConfirmDialog(null, "¿Quieres borrar el evento " + r.getNombre() + "?",
                                    "Question (application-modal dialog)",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE);
                            if (opcion == JOptionPane.YES_OPTION){
                                h.borrarReserva(r);
                                refrescarTable();
                            }else if (opcion == JOptionPane.NO_OPTION)
                                JOptionPane.showMessageDialog(null, "No se ha borrado "+r.getNombre()+".", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }else
                data = new Object[0][0];   
            
            fireTableDataChanged();
        }
        
    }
    
    private void refrescarTable(){
        modeloReservas.refreshTableModel(h.getReservas());
        
        jLabelInfoNomEvento.setVisible(false);
        jLabelNomEvento.setVisible(false);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        jTableReservas.getColumn("ACCION").setCellRenderer(buttonRenderer);
        jLabelFechaHoy.setText(Util.getToday());
        jLabelNumeroReservas.setText(Integer.toString(jTableReservas.getRowCount()));
    }
    
    private static class JTableButtonRenderer implements TableCellRenderer {        
        @Override 
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;  
        }
    }
    
    public void comprobarCheckBox(){
        if(contPlatos == 2){
            for (JCheckBox jCheckBox : listaChechBox) {
                if(!jCheckBox.isSelected())
                    jCheckBox.setEnabled(false);
            }
            jButtonReservar.setVisible(true);
        } else {
            for (JCheckBox jCheckBox : listaChechBox) {
                jCheckBox.setEnabled(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogAutor = new javax.swing.JDialog();
        jPanelAutor = new javax.swing.JPanel();
        jLabelNombreAutor = new javax.swing.JLabel();
        jLabelVerion = new javax.swing.JLabel();
        jButtonCerrarAutor = new javax.swing.JButton();
        jDialogNuevaReserva = new javax.swing.JDialog();
        jPanelNuevaReserva = new javax.swing.JPanel();
        jLabelDNI = new javax.swing.JLabel();
        jTextFieldDNI = new javax.swing.JTextField();
        jButtonBuscarDNI = new javax.swing.JButton();
        jLabelDniEscogido = new javax.swing.JLabel();
        jLabelNAsistentes = new javax.swing.JLabel();
        jSpinnerAsistentes = new javax.swing.JSpinner();
        jButtonDisponibilidad = new javax.swing.JButton();
        jLabelFecha = new javax.swing.JLabel();
        jTextFieldFecha = new javax.swing.JTextField();
        jLabelSalon = new javax.swing.JLabel();
        jLabelEvento = new javax.swing.JLabel();
        jComboBoxEvento = new javax.swing.JComboBox<>();
        jLabelMenu = new javax.swing.JLabel();
        jCheckBoxPlato1 = new javax.swing.JCheckBox();
        jCheckBoxPlato2 = new javax.swing.JCheckBox();
        jCheckBoxPlato3 = new javax.swing.JCheckBox();
        jCheckBoxPlato4 = new javax.swing.JCheckBox();
        jCheckBoxPlato5 = new javax.swing.JCheckBox();
        jLabelDias = new javax.swing.JLabel();
        jCheckBoxPlato6 = new javax.swing.JCheckBox();
        jSliderDias = new javax.swing.JSlider();
        jLabelHabitaciones = new javax.swing.JLabel();
        jRadioButtonSi = new javax.swing.JRadioButton();
        jRadioButtonNo = new javax.swing.JRadioButton();
        jLabelTipo = new javax.swing.JLabel();
        jRadioButtonSimple = new javax.swing.JRadioButton();
        jRadioButtonDoble = new javax.swing.JRadioButton();
        jLabelNumeroHabitaciones = new javax.swing.JLabel();
        jSpinnerNHabitaciones = new javax.swing.JSpinner();
        jTextFieldSalonEscogido = new javax.swing.JTextField();
        jPanelBotonosNuevaReserva = new javax.swing.JPanel();
        jButtonReservar = new javax.swing.JButton();
        jButtonCancelarReserva = new javax.swing.JButton();
        buttonGroupSiNo = new javax.swing.ButtonGroup();
        jDialogDisponibilidad = new javax.swing.JDialog();
        jComboBoxSalones = new javax.swing.JComboBox<>();
        jLabelSalonDisponibilidad = new javax.swing.JLabel();
        jLabelFechaDisponibilidad = new javax.swing.JLabel();
        jSpinnerFecha = new javax.swing.JSpinner();
        jButtonAceptarDisponibilidad = new javax.swing.JButton();
        jPanelVentanaPrincipal = new javax.swing.JPanel();
        jTabbedPaneClientes = new javax.swing.JTabbedPane();
        jPanelReservas = new javax.swing.JPanel();
        jScrollPaneReservas = new javax.swing.JScrollPane();
        jTableReservas = new javax.swing.JTable();
        jPanelClientes = new javax.swing.JPanel();
        jScrollPaneClientes = new javax.swing.JScrollPane();
        jTableClientes = new javax.swing.JTable();
        jLabelInfoFecha = new javax.swing.JLabel();
        jLabelFechaHoy = new javax.swing.JLabel();
        jLabelInfoNumReservas = new javax.swing.JLabel();
        jLabelNumeroReservas = new javax.swing.JLabel();
        jLabelInfoNomEvento = new javax.swing.JLabel();
        jLabelNomEvento = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuAccion = new javax.swing.JMenu();
        jMenuItemNuevaReserva = new javax.swing.JMenuItem();
        jSeparatorAccion = new javax.swing.JPopupMenu.Separator();
        jMenuItemSalir = new javax.swing.JMenuItem();
        jMenuAcerca = new javax.swing.JMenu();
        jMenuItemAutor = new javax.swing.JMenuItem();

        jPanelAutor.setPreferredSize(new java.awt.Dimension(400, 287));

        jLabelNombreAutor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNombreAutor.setText("Autor: Sergio de la Pascua Roca");

        jLabelVerion.setText("Version: 1.0");

        jButtonCerrarAutor.setText("Cerrar");
        jButtonCerrarAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarAutorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAutorLayout = new javax.swing.GroupLayout(jPanelAutor);
        jPanelAutor.setLayout(jPanelAutorLayout);
        jPanelAutorLayout.setHorizontalGroup(
            jPanelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAutorLayout.createSequentialGroup()
                .addGroup(jPanelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAutorLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addGroup(jPanelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelVerion)
                            .addComponent(jButtonCerrarAutor)))
                    .addGroup(jPanelAutorLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jLabelNombreAutor)))
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanelAutorLayout.setVerticalGroup(
            jPanelAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAutorLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(jLabelNombreAutor)
                .addGap(34, 34, 34)
                .addComponent(jLabelVerion)
                .addGap(39, 39, 39)
                .addComponent(jButtonCerrarAutor)
                .addGap(54, 54, 54))
        );

        javax.swing.GroupLayout jDialogAutorLayout = new javax.swing.GroupLayout(jDialogAutor.getContentPane());
        jDialogAutor.getContentPane().setLayout(jDialogAutorLayout);
        jDialogAutorLayout.setHorizontalGroup(
            jDialogAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
            .addGroup(jDialogAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelAutor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogAutorLayout.setVerticalGroup(
            jDialogAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
            .addGroup(jDialogAutorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelAutor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialogNuevaReserva.setModal(true);

        jPanelNuevaReserva.setPreferredSize(new java.awt.Dimension(400, 287));

        jLabelDNI.setText("DNI: ");

        jTextFieldDNI.setText("Introducir el DNI");

        jButtonBuscarDNI.setText("Buscar");
        jButtonBuscarDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarDNIActionPerformed(evt);
            }
        });

        jLabelDniEscogido.setText("DNI: MIMIMIMI");

        jLabelNAsistentes.setText("Nº Asistentes");

        jSpinnerAsistentes.setModel(new javax.swing.SpinnerNumberModel(1, 1, 500, 1));
        jSpinnerAsistentes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerAsistentesStateChanged(evt);
            }
        });

        jButtonDisponibilidad.setText("Disponibilidad");
        jButtonDisponibilidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisponibilidadActionPerformed(evt);
            }
        });

        jLabelFecha.setText("Fecha");

        jTextFieldFecha.setText("Fecha elegida");
        jTextFieldFecha.setFocusable(false);

        jLabelSalon.setText("Salon ");

        jLabelEvento.setText("Tipo Evento");

        jComboBoxEvento.setModel(Util.getCBModelOfEnum(TipoEvento.class, "un evento"));
        jComboBoxEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEventoActionPerformed(evt);
            }
        });

        jLabelMenu.setText("Menu");

        jCheckBoxPlato1.setText("Solomillo");
        jCheckBoxPlato1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato1ActionPerformed(evt);
            }
        });

        jCheckBoxPlato2.setText("Puchero");
        jCheckBoxPlato2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato2ActionPerformed(evt);
            }
        });

        jCheckBoxPlato3.setText("Croquetas");
        jCheckBoxPlato3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato3ActionPerformed(evt);
            }
        });

        jCheckBoxPlato4.setText("Pescado");
        jCheckBoxPlato4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato4ActionPerformed(evt);
            }
        });

        jCheckBoxPlato5.setText("Fabada");
        jCheckBoxPlato5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato5ActionPerformed(evt);
            }
        });

        jLabelDias.setText("Nº Días");

        jCheckBoxPlato6.setText("Potaje");
        jCheckBoxPlato6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPlato6ActionPerformed(evt);
            }
        });

        jSliderDias.setMajorTickSpacing(1);
        jSliderDias.setMaximum(3);
        jSliderDias.setMinimum(1);
        jSliderDias.setPaintLabels(true);
        jSliderDias.setValue(2);
        jSliderDias.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderDiasStateChanged(evt);
            }
        });

        jLabelHabitaciones.setText("Habitaciones");

        buttonGroupSiNo.add(jRadioButtonSi);
        jRadioButtonSi.setText("Si");
        jRadioButtonSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSiActionPerformed(evt);
            }
        });

        buttonGroupSiNo.add(jRadioButtonNo);
        jRadioButtonNo.setText("No");
        jRadioButtonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonNoActionPerformed(evt);
            }
        });

        jLabelTipo.setText("Tipo");

        jRadioButtonSimple.setText("Simple");
        jRadioButtonSimple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSimpleActionPerformed(evt);
            }
        });

        jRadioButtonDoble.setText("Doble");
        jRadioButtonDoble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonDobleActionPerformed(evt);
            }
        });

        jLabelNumeroHabitaciones.setText("Nº");

        jSpinnerNHabitaciones.setModel(new javax.swing.SpinnerNumberModel(0, 0, 500, 1));
        jSpinnerNHabitaciones.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNHabitacionesStateChanged(evt);
            }
        });

        jTextFieldSalonEscogido.setText("Salon Escogido");
        jTextFieldSalonEscogido.setFocusable(false);

        jButtonReservar.setText("Realizar Reserva");
        jButtonReservar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReservarActionPerformed(evt);
            }
        });
        jPanelBotonosNuevaReserva.add(jButtonReservar);

        jButtonCancelarReserva.setText("Cancelar Reserva");
        jButtonCancelarReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarReservaActionPerformed(evt);
            }
        });
        jPanelBotonosNuevaReserva.add(jButtonCancelarReserva);

        javax.swing.GroupLayout jPanelNuevaReservaLayout = new javax.swing.GroupLayout(jPanelNuevaReserva);
        jPanelNuevaReserva.setLayout(jPanelNuevaReservaLayout);
        jPanelNuevaReservaLayout.setHorizontalGroup(
            jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                .addComponent(jLabelNAsistentes)
                                .addGap(31, 31, 31)
                                .addComponent(jSpinnerAsistentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelDniEscogido)
                            .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBoxPlato4)
                                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jCheckBoxPlato1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jCheckBoxPlato3)
                                            .addComponent(jCheckBoxPlato6))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jCheckBoxPlato5)
                                            .addComponent(jCheckBoxPlato2))))
                                .addComponent(jPanelBotonosNuevaReserva, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelDias)
                                    .addComponent(jLabelEvento))
                                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jSliderDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(jComboBoxEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFecha)
                                    .addComponent(jLabelSalon))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldFecha)
                                    .addComponent(jTextFieldSalonEscogido, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jButtonDisponibilidad))
                            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                .addComponent(jLabelDNI)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscarDNI))
                            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                                .addComponent(jLabelHabitaciones)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonSi)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonNo))))
                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(jLabelMenu))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNuevaReservaLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabelTipo)
                        .addGap(12, 12, 12)
                        .addComponent(jRadioButtonSimple)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButtonDoble)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNumeroHabitaciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerNHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanelNuevaReservaLayout.setVerticalGroup(
            jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDNI)
                    .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscarDNI))
                .addGap(18, 18, 18)
                .addComponent(jLabelDniEscogido)
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNAsistentes)
                    .addComponent(jSpinnerAsistentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFecha)
                    .addComponent(jTextFieldFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDisponibilidad))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSalon)
                    .addComponent(jTextFieldSalonEscogido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEvento)
                    .addComponent(jComboBoxEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBoxPlato4)
                    .addGroup(jPanelNuevaReservaLayout.createSequentialGroup()
                        .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxPlato3)
                            .addComponent(jCheckBoxPlato2)
                            .addComponent(jCheckBoxPlato1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxPlato6)
                            .addComponent(jCheckBoxPlato5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDias)
                    .addComponent(jSliderDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelHabitaciones)
                    .addComponent(jRadioButtonSi)
                    .addComponent(jRadioButtonNo))
                .addGap(18, 18, 18)
                .addGroup(jPanelNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTipo)
                    .addComponent(jRadioButtonSimple)
                    .addComponent(jRadioButtonDoble)
                    .addComponent(jLabelNumeroHabitaciones)
                    .addComponent(jSpinnerNHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jPanelBotonosNuevaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jDialogNuevaReservaLayout = new javax.swing.GroupLayout(jDialogNuevaReserva.getContentPane());
        jDialogNuevaReserva.getContentPane().setLayout(jDialogNuevaReservaLayout);
        jDialogNuevaReservaLayout.setHorizontalGroup(
            jDialogNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogNuevaReservaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelNuevaReserva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jDialogNuevaReservaLayout.setVerticalGroup(
            jDialogNuevaReservaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogNuevaReservaLayout.createSequentialGroup()
                .addComponent(jPanelNuevaReserva, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDialogDisponibilidad.setMaximumSize(new java.awt.Dimension(400, 180));
        jDialogDisponibilidad.setModal(true);
        jDialogDisponibilidad.setPreferredSize(new java.awt.Dimension(400, 180));

        jComboBoxSalones.setEnabled(false);
        jComboBoxSalones.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxSalonesFocusGained(evt);
            }
        });

        jLabelSalonDisponibilidad.setText("Salon");

        jLabelFechaDisponibilidad.setText("Fecha");

        jSpinnerFecha.setModel(new javax.swing.SpinnerDateModel());
        jSpinnerFecha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerFechaStateChanged(evt);
            }
        });

        jButtonAceptarDisponibilidad.setText("Aceptar");
        jButtonAceptarDisponibilidad.setEnabled(false);
        jButtonAceptarDisponibilidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarDisponibilidadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogDisponibilidadLayout = new javax.swing.GroupLayout(jDialogDisponibilidad.getContentPane());
        jDialogDisponibilidad.getContentPane().setLayout(jDialogDisponibilidadLayout);
        jDialogDisponibilidadLayout.setHorizontalGroup(
            jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDisponibilidadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogDisponibilidadLayout.createSequentialGroup()
                        .addComponent(jLabelSalonDisponibilidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxSalones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialogDisponibilidadLayout.createSequentialGroup()
                        .addComponent(jLabelFechaDisponibilidad)
                        .addGap(45, 45, 45)
                        .addComponent(jSpinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 189, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogDisponibilidadLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAceptarDisponibilidad)
                .addContainerGap())
        );
        jDialogDisponibilidadLayout.setVerticalGroup(
            jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDisponibilidadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelFechaDisponibilidad)
                    .addComponent(jSpinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialogDisponibilidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxSalones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSalonDisponibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonAceptarDisponibilidad)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 270));

        javax.swing.GroupLayout jPanelVentanaPrincipalLayout = new javax.swing.GroupLayout(jPanelVentanaPrincipal);
        jPanelVentanaPrincipal.setLayout(jPanelVentanaPrincipalLayout);
        jPanelVentanaPrincipalLayout.setHorizontalGroup(
            jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelVentanaPrincipalLayout.setVerticalGroup(
            jPanelVentanaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jTabbedPaneClientes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneClientesStateChanged(evt);
            }
        });

        jTableReservas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableReservas.setMinimumSize(new java.awt.Dimension(670, 64));
        jTableReservas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableReservasMouseClicked(evt);
            }
        });
        jScrollPaneReservas.setViewportView(jTableReservas);

        javax.swing.GroupLayout jPanelReservasLayout = new javax.swing.GroupLayout(jPanelReservas);
        jPanelReservas.setLayout(jPanelReservasLayout);
        jPanelReservasLayout.setHorizontalGroup(
            jPanelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneReservas, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelReservasLayout.setVerticalGroup(
            jPanelReservasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReservasLayout.createSequentialGroup()
                .addComponent(jScrollPaneReservas, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jTabbedPaneClientes.addTab("Reservas", jPanelReservas);

        jPanelClientes.setPreferredSize(new java.awt.Dimension(610, 232));

        jTableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPaneClientes.setViewportView(jTableClientes);

        javax.swing.GroupLayout jPanelClientesLayout = new javax.swing.GroupLayout(jPanelClientes);
        jPanelClientes.setLayout(jPanelClientesLayout);
        jPanelClientesLayout.setHorizontalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelClientesLayout.setVerticalGroup(
            jPanelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneClientes.addTab("Clientes", jPanelClientes);

        jLabelInfoFecha.setText("Fecha de hoy: ");

        jLabelFechaHoy.setText("aqui va la fecha de hoy");

        jLabelInfoNumReservas.setText("Numero de Reservas:");

        jLabelNumeroReservas.setText("aqui va el numero de las reservas");

        jLabelInfoNomEvento.setText("Nombre del Evento:");

        jLabelNomEvento.setText("aqui va el nombre");

        jMenuAccion.setText("Accion");

        jMenuItemNuevaReserva.setText("Nueva Reserva");
        jMenuItemNuevaReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNuevaReservaActionPerformed(evt);
            }
        });
        jMenuAccion.add(jMenuItemNuevaReserva);
        jMenuAccion.add(jSeparatorAccion);

        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenuAccion.add(jMenuItemSalir);

        jMenuBar.add(jMenuAccion);

        jMenuAcerca.setText("Acerca de");

        jMenuItemAutor.setText("Autor");
        jMenuItemAutor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAutorActionPerformed(evt);
            }
        });
        jMenuAcerca.add(jMenuItemAutor);

        jMenuBar.add(Box.createHorizontalGlue());

        jMenuBar.add(jMenuAcerca);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPaneClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelInfoFecha)
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabelFechaHoy))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabelInfoNumReservas)
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabelNumeroReservas)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelInfoNomEvento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNomEvento)))))
                .addGap(6, 6, 6)
                .addComponent(jPanelVentanaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVentanaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInfoFecha)
                    .addComponent(jLabelFechaHoy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInfoNumReservas)
                    .addComponent(jLabelNumeroReservas))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInfoNomEvento)
                    .addComponent(jLabelNomEvento))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPaneClientesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneClientesStateChanged
        // TODO add your handling code here:
        if(jTabbedPaneClientes.getSelectedIndex() == 1){
            VentanaPrincipal.ModeloTablaClientes model = new VentanaPrincipal.ModeloTablaClientes();
            jTableClientes.setModel(model);
        } else {
            jTableReservas.setModel(modeloReservas);
            refrescarTable();
        }
    }//GEN-LAST:event_jTabbedPaneClientesStateChanged

    private void jButtonCerrarAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarAutorActionPerformed
        // TODO add your handling code here:
        jDialogAutor.setVisible(false);
    }//GEN-LAST:event_jButtonCerrarAutorActionPerformed

    private void jMenuItemAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAutorActionPerformed
        // TODO add your handling code here:
        jDialogAutor.setSize(400,300); 
        jDialogAutor.setLocationRelativeTo(null);
        jDialogAutor.setVisible(true);
    }//GEN-LAST:event_jMenuItemAutorActionPerformed

    private void jTableReservasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableReservasMouseClicked
        int column = jTableReservas.getColumnModel().getColumnIndexAtX(evt.getX()); // get the coloum of the button
        int row = evt.getY() / jTableReservas.getRowHeight(); //get the row of the button

        /*Checking the row or column is valid or not*/
        if (row < jTableReservas.getRowCount() && row >= 0 && column < jTableReservas.getColumnCount() && column >= 0) {
            Object value = jTableReservas.getValueAt(row, column);
            if (value instanceof JButton) {
                ((JButton) value).doClick();
                refrescarTable();
            }
            else{
                jLabelNomEvento.setVisible(true);
                jLabelInfoNomEvento.setVisible(true);
                jLabelNomEvento.setText((String)jTableReservas.getValueAt(row, 2));
            }
        }
    }//GEN-LAST:event_jTableReservasMouseClicked

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jComboBoxEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEventoActionPerformed
        // TODO add your handling code here:
        jLabelMenu.setVisible(false);
        jCheckBoxPlato1.setVisible(false);
        jCheckBoxPlato2.setVisible(false);
        jCheckBoxPlato3.setVisible(false);
        jCheckBoxPlato4.setVisible(false);
        jCheckBoxPlato5.setVisible(false);
        jCheckBoxPlato6.setVisible(false);
        jLabelDias.setVisible(false);
        jSliderDias.setVisible(false);
        jLabelHabitaciones.setVisible(false);
        jRadioButtonSi.setVisible(false);
        jRadioButtonNo.setVisible(false);
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        jButtonReservar.setVisible(false);
        
        if (jComboBoxEvento.getSelectedIndex() == 0) {
            System.out.println("Seleccion: Nada");
        } else {
            if (jComboBoxEvento.getSelectedItem() == TipoEvento.Jornada) {
                System.out.println("Seleccion: Jornada");
                jDialogNuevaReserva.setSize(400,400);
                tipoDeEvento =(TipoEvento) jComboBoxEvento.getSelectedItem();
                jButtonReservar.setVisible(true);
            } else if (jComboBoxEvento.getSelectedItem() == TipoEvento.Banquete) {
                System.out.println("Seleccion: Banquete");
                jLabelMenu.setVisible(true);
                contPlatos = 0;
                jCheckBoxPlato1.setVisible(true);
                jCheckBoxPlato2.setVisible(true);
                jCheckBoxPlato3.setVisible(true);
                jCheckBoxPlato4.setVisible(true);
                jCheckBoxPlato5.setVisible(true);
                jCheckBoxPlato6.setVisible(true);
                tipoDeEvento =(TipoEvento) jComboBoxEvento.getSelectedItem();
                
                jDialogNuevaReserva.setSize(400,500);
                jDialogAutor.setLocationRelativeTo(null);
            } else if (jComboBoxEvento.getSelectedItem() == TipoEvento.Congreso) {
                System.out.println("Seleccion: Congreso");
                jLabelDias.setVisible(true);
                jSliderDias.setVisible(true);
                tipoDeEvento =(TipoEvento) jComboBoxEvento.getSelectedItem();
                
                jDialogNuevaReserva.setSize(400,450);
                jDialogAutor.setLocationRelativeTo(null);
            }
        }
    }//GEN-LAST:event_jComboBoxEventoActionPerformed

    private void jRadioButtonSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSiActionPerformed
        // TODO add your handling code here:
        jLabelTipo.setVisible(true);
        jRadioButtonSimple.setVisible(true);
        jRadioButtonDoble.setVisible(true);
        
        SpinnerNumberModel spinnerModel;
        int current = 1;
        int min = 1;
        int max = (int) jSpinnerAsistentes.getValue();
        int step = 1;
        spinnerModel = new SpinnerNumberModel(current, min, max, step);
        jSpinnerNHabitaciones.setModel(spinnerModel);
        
        jSpinnerNHabitaciones.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        
        jDialogNuevaReserva.setSize(400,500);
    }//GEN-LAST:event_jRadioButtonSiActionPerformed

    private void jRadioButtonNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonNoActionPerformed
        // TODO add your handling code here:
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        
        jButtonReservar.setVisible(true);
    }//GEN-LAST:event_jRadioButtonNoActionPerformed

    private void jButtonBuscarDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarDNIActionPerformed
        // TODO add your handling code here:
        if(h.buscarDni(jTextFieldDNI.getText()) != null){
            jLabelDniEscogido.setText("DNI: "+jTextFieldDNI.getText());
            jLabelDniEscogido.setVisible(true);
            jLabelNAsistentes.setVisible(true);
            jSpinnerAsistentes.setVisible(true);
            
            clienteQueReserva = h.buscarDni(jTextFieldDNI.getText());
        } else{
            jTextFieldDNI.setText("");
            jTextFieldDNI.requestFocus();
            JOptionPane.showMessageDialog(null, "Introduzca un DNI correcto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonBuscarDNIActionPerformed

    private void jMenuItemNuevaReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNuevaReservaActionPerformed
        // TODO add your handling code here:
        jLabelDniEscogido.setVisible(false);
        jLabelNAsistentes.setVisible(false);
        jSpinnerAsistentes.setVisible(false);
        jButtonDisponibilidad.setVisible(false);
        jLabelFecha.setVisible(false);
        jTextFieldFecha.setVisible(false);
        jLabelSalon.setVisible(false);
        jTextFieldSalonEscogido.setVisible(false);
        jLabelEvento.setVisible(false);
        jComboBoxEvento.setVisible(false);
        jLabelMenu.setVisible(false);
        jCheckBoxPlato1.setVisible(false);
        jCheckBoxPlato2.setVisible(false);
        jCheckBoxPlato3.setVisible(false);
        jCheckBoxPlato4.setVisible(false);
        jCheckBoxPlato5.setVisible(false);
        jCheckBoxPlato6.setVisible(false);
        jLabelDias.setVisible(false);
        jSliderDias.setVisible(false);
        jLabelHabitaciones.setVisible(false);
        jRadioButtonSi.setVisible(false);
        jRadioButtonNo.setVisible(false);
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        jButtonReservar.setVisible(false);
        jDialogNuevaReserva.setSize(400,400);
        jDialogNuevaReserva.setLocationRelativeTo(null);
        jDialogNuevaReserva.setVisible(true);
    }//GEN-LAST:event_jMenuItemNuevaReservaActionPerformed

    private void jSpinnerNHabitacionesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerNHabitacionesStateChanged
        // TODO add your handling code here:
        jButtonReservar.setVisible(true);
    }//GEN-LAST:event_jSpinnerNHabitacionesStateChanged

    private void jSliderDiasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderDiasStateChanged
        // TODO add your handling code here:
        jLabelHabitaciones.setVisible(true);
        jRadioButtonSi.setVisible(true);
        jRadioButtonNo.setVisible(true);
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        jButtonReservar.setVisible(false);
    }//GEN-LAST:event_jSliderDiasStateChanged

    private void jSpinnerAsistentesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerAsistentesStateChanged
        // TODO add your handling code here:
        jLabelFecha.setVisible(false);
        jTextFieldFecha.setVisible(false);
        jLabelSalon.setVisible(false);
        jTextFieldSalonEscogido.setVisible(false);
        jLabelEvento.setVisible(false);
        jComboBoxEvento.setVisible(false);
        jLabelMenu.setVisible(false);
        jCheckBoxPlato1.setVisible(false);
        jCheckBoxPlato2.setVisible(false);
        jCheckBoxPlato3.setVisible(false);
        jCheckBoxPlato4.setVisible(false);
        jCheckBoxPlato5.setVisible(false);
        jCheckBoxPlato6.setVisible(false);
        jLabelDias.setVisible(false);
        jSliderDias.setVisible(false);
        jLabelHabitaciones.setVisible(false);
        jRadioButtonSi.setVisible(false);
        jRadioButtonNo.setVisible(false);
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        jButtonReservar.setVisible(false);
        
        jButtonDisponibilidad.setVisible(true);
        
        
    }//GEN-LAST:event_jSpinnerAsistentesStateChanged

    private void jCheckBoxPlato1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato1ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato1.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato1ActionPerformed

    private void jCheckBoxPlato2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato2ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato2.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato2ActionPerformed

    private void jCheckBoxPlato3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato3ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato3.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato3ActionPerformed

    private void jCheckBoxPlato4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato4ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato4.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato4ActionPerformed

    private void jCheckBoxPlato5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato5ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato5.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato5ActionPerformed

    private void jCheckBoxPlato6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPlato6ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxPlato6.isSelected())
            contPlatos++;
        else
            contPlatos--;
        
        comprobarCheckBox();
    }//GEN-LAST:event_jCheckBoxPlato6ActionPerformed

    private void jButtonDisponibilidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisponibilidadActionPerformed
        // TODO add your handling code here:
        
        jDialogDisponibilidad.setSize(400,180);
        jDialogDisponibilidad.setLocationRelativeTo(null);
        jDialogDisponibilidad.setVisible(true);
        
    }//GEN-LAST:event_jButtonDisponibilidadActionPerformed

    public boolean comprobarDisponibilidadSalon(int  id, LocalDate fecha){
        for (Reserva reserva : h.getReservas()) {
            if(reserva.getDate().compareTo(fecha) == 0 && reserva.getSalon() == id)
                return false;
        }
        return true;       
    }
    
    private void jButtonAceptarDisponibilidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptarDisponibilidadActionPerformed
        // TODO add your handling code here:   
        jLabelMenu.setVisible(false);
        jCheckBoxPlato1.setVisible(false);
        jCheckBoxPlato2.setVisible(false);
        jCheckBoxPlato3.setVisible(false);
        jCheckBoxPlato4.setVisible(false);
        jCheckBoxPlato5.setVisible(false);
        jCheckBoxPlato6.setVisible(false);
        jLabelDias.setVisible(false);
        jSliderDias.setVisible(false);
        jLabelHabitaciones.setVisible(false);
        jRadioButtonSi.setVisible(false);
        jRadioButtonNo.setVisible(false);
        jLabelTipo.setVisible(false);
        jRadioButtonSimple.setVisible(false);
        jRadioButtonDoble.setVisible(false);
        jLabelNumeroHabitaciones.setVisible(false);
        jSpinnerNHabitaciones.setVisible(false);
        jButtonReservar.setVisible(false);
        
        jDialogNuevaReserva.setSize(400,400);
        jDialogNuevaReserva.setLocationRelativeTo(null);
        
        jTextFieldSalonEscogido.setText(Integer.toString(((Salon)jComboBoxSalones.getSelectedItem()).getId()));
        salonQueReserva = (Salon)jComboBoxSalones.getSelectedItem();
        jDialogNuevaReserva.setVisible(true);
        jDialogDisponibilidad.setVisible(false);
        
        jLabelFecha.setVisible(true);
        jTextFieldFecha.setVisible(true);
        jLabelSalon.setVisible(true);
        jTextFieldSalonEscogido.setVisible(true);
        jLabelEvento.setVisible(true);
        jComboBoxEvento.setVisible(true);
    }//GEN-LAST:event_jButtonAceptarDisponibilidadActionPerformed
    
    public boolean comprobarFecha(LocalDate fecha){
        for (LocalDate date : fechasValidas) {
            if(date.compareTo(fecha) == 0)
                return true;
        }
        return false;
    }
    
    private void jSpinnerFechaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerFechaStateChanged
        // TODO add your handling code here:
        jLabelHabitaciones.setVisible(false);
        jRadioButtonSi.setVisible(false);
        jRadioButtonNo.setVisible(false);
        
        Date date = (Date) jSpinnerFecha.getValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(comprobarFecha(localDate)){
            jComboBoxSalones.setEnabled(true);
            jComboBoxSalones.removeAllItems();
            jTextFieldFecha.setText(localDate.toString());
            fechaQueReserva = localDate;
            
            if(localDate.getDayOfWeek()==DayOfWeek.FRIDAY)
                diaSemana = 3;
            else if (localDate.getDayOfWeek()==DayOfWeek.SATURDAY)
                diaSemana = 2;
            else 
                diaSemana = 1;
            jSliderDias.setMaximum(diaSemana);
            
            int cont = 0;
            for (Salon salon : h.getSalones()) {
                if(salon.getCapacidad() > (Integer)jSpinnerAsistentes.getValue()){
                    if(comprobarDisponibilidadSalon(salon.getId(), localDate)){
                        jComboBoxSalones.addItem(salon);
                        cont++;
                    }
                }
            }
            
            if(cont == 0)
                JOptionPane.showMessageDialog (null, "No hay salones disponibles para esa fecha.", "Error", JOptionPane.ERROR_MESSAGE);            
            
        }else {
            JOptionPane.showMessageDialog (null, "La fecha es incorrecta. Lo siento.", "Error", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this,ayudaFechas());  
        }
    }//GEN-LAST:event_jSpinnerFechaStateChanged
    
    private String ayudaFechas(){
        String salida = "Fechas Validas: \n";
        for (LocalDate fechasValida : fechasValidas) {
            salida += "" + fechasValida.toString()+"\n";
        }
        return salida;
    }
    
    private void jButtonCancelarReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarReservaActionPerformed
        // TODO add your handling code here:
        jDialogNuevaReserva.setVisible(false);
    }//GEN-LAST:event_jButtonCancelarReservaActionPerformed
    
    public Menu plato  (String plat){
        switch (plat) {
            case "Solomillo":
                return Menu.solomillo;
            case "Croquetas":
                return Menu.croquetas;
            case "Fabada":
                return Menu.fabada;
            case "Pescado":
                return Menu.pescado;
            case "Potaje":
                return Menu.potaje;
            case "Puchero":
                return Menu.puchero;
            default:
                throw new AssertionError();
        }
    }
    
    private void jButtonReservarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReservarActionPerformed
        // TODO add your handling code here:
        nombreNuevaReserva = JOptionPane.showInputDialog(this, "Nombre del evento:");
        System.out.println(nombreNuevaReserva);
        
        if(tipoDeEvento == TipoEvento.Jornada){
            Evento e = new Evento(tipoDeEvento, nombreNuevaReserva, 1, (Integer) jSpinnerAsistentes.getValue());
            Reserva nuevaReserva = new Reserva(fechaQueReserva, clienteQueReserva, salonQueReserva, e);
            h.añadirReserva(nuevaReserva);
            
        } else if (tipoDeEvento == TipoEvento.Banquete){
            for (JCheckBox jCheckBox : listaChechBox) {
                if(jCheckBox.isSelected())
                    if (plato1 == null)
                        plato1 = plato(jCheckBox.getText());
                    else
                        plato2 = plato(jCheckBox.getText());
            }            
            Evento e = new Evento(tipoDeEvento, nombreNuevaReserva, 1, (Integer) jSpinnerAsistentes.getValue(), new MenuBanquete(plato1, plato2));
            Reserva nuevaReserva = new Reserva(fechaQueReserva, clienteQueReserva, salonQueReserva, e);
            h.añadirReserva(nuevaReserva);
            
        } else {
            if((Integer)jSpinnerNHabitaciones.getValue() != 0){
                Evento e = new Evento(tipoDeEvento, nombreNuevaReserva, (Integer) jSliderDias.getValue(), 
                        (Integer) jSpinnerAsistentes.getValue(), new HabitacionCongreso((Integer) jSpinnerNHabitaciones.getValue(), tipoHabitacion));
                
                Reserva nuevaReserva = new Reserva(fechaQueReserva, clienteQueReserva, salonQueReserva, e);                
                h.añadirReserva(nuevaReserva);
            } else {
                Evento e = new Evento(tipoDeEvento, nombreNuevaReserva, (Integer) jSliderDias.getValue(), 
                        (Integer) jSpinnerAsistentes.getValue());
                Reserva nuevaReserva = new Reserva(fechaQueReserva, clienteQueReserva, salonQueReserva, e);
                h.añadirReserva(nuevaReserva);                
            }            
        }        
        refrescarTable();
        jDialogNuevaReserva.setVisible(false);
    }//GEN-LAST:event_jButtonReservarActionPerformed

    private void jRadioButtonSimpleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSimpleActionPerformed
        // TODO add your handling code here:        
        jSpinnerNHabitaciones.setVisible(true);
        jLabelNumeroHabitaciones.setVisible(true);
        tipoHabitacion = TipoHabitacion.individual;
    }//GEN-LAST:event_jRadioButtonSimpleActionPerformed

    private void jRadioButtonDobleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonDobleActionPerformed
        // TODO add your handling code here:
        jSpinnerNHabitaciones.setVisible(true);
        jLabelNumeroHabitaciones.setVisible(true);
        tipoHabitacion = TipoHabitacion.doble;
        jSpinnerNHabitaciones.setValue(0);
    }//GEN-LAST:event_jRadioButtonDobleActionPerformed

    private void jComboBoxSalonesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxSalonesFocusGained
        // TODO add your handling code here:
        jButtonAceptarDisponibilidad.setEnabled(true);
    }//GEN-LAST:event_jComboBoxSalonesFocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSiNo;
    private javax.swing.JButton jButtonAceptarDisponibilidad;
    private javax.swing.JButton jButtonBuscarDNI;
    private javax.swing.JButton jButtonCancelarReserva;
    private javax.swing.JButton jButtonCerrarAutor;
    private javax.swing.JButton jButtonDisponibilidad;
    private javax.swing.JButton jButtonReservar;
    private javax.swing.JCheckBox jCheckBoxPlato1;
    private javax.swing.JCheckBox jCheckBoxPlato2;
    private javax.swing.JCheckBox jCheckBoxPlato3;
    private javax.swing.JCheckBox jCheckBoxPlato4;
    private javax.swing.JCheckBox jCheckBoxPlato5;
    private javax.swing.JCheckBox jCheckBoxPlato6;
    private javax.swing.JComboBox<String> jComboBoxEvento;
    private javax.swing.JComboBox<Salon> jComboBoxSalones;
    private javax.swing.JDialog jDialogAutor;
    private javax.swing.JDialog jDialogDisponibilidad;
    private javax.swing.JDialog jDialogNuevaReserva;
    private javax.swing.JLabel jLabelDNI;
    private javax.swing.JLabel jLabelDias;
    private javax.swing.JLabel jLabelDniEscogido;
    private javax.swing.JLabel jLabelEvento;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelFechaDisponibilidad;
    private javax.swing.JLabel jLabelFechaHoy;
    private javax.swing.JLabel jLabelHabitaciones;
    private javax.swing.JLabel jLabelInfoFecha;
    private javax.swing.JLabel jLabelInfoNomEvento;
    private javax.swing.JLabel jLabelInfoNumReservas;
    private javax.swing.JLabel jLabelMenu;
    private javax.swing.JLabel jLabelNAsistentes;
    private javax.swing.JLabel jLabelNomEvento;
    private javax.swing.JLabel jLabelNombreAutor;
    private javax.swing.JLabel jLabelNumeroHabitaciones;
    private javax.swing.JLabel jLabelNumeroReservas;
    private javax.swing.JLabel jLabelSalon;
    private javax.swing.JLabel jLabelSalonDisponibilidad;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JLabel jLabelVerion;
    private javax.swing.JMenu jMenuAccion;
    private javax.swing.JMenu jMenuAcerca;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItemAutor;
    private javax.swing.JMenuItem jMenuItemNuevaReserva;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JPanel jPanelAutor;
    private javax.swing.JPanel jPanelBotonosNuevaReserva;
    private javax.swing.JPanel jPanelClientes;
    private javax.swing.JPanel jPanelNuevaReserva;
    private javax.swing.JPanel jPanelReservas;
    private javax.swing.JPanel jPanelVentanaPrincipal;
    private javax.swing.JRadioButton jRadioButtonDoble;
    private javax.swing.JRadioButton jRadioButtonNo;
    private javax.swing.JRadioButton jRadioButtonSi;
    private javax.swing.JRadioButton jRadioButtonSimple;
    private javax.swing.JScrollPane jScrollPaneClientes;
    private javax.swing.JScrollPane jScrollPaneReservas;
    private javax.swing.JPopupMenu.Separator jSeparatorAccion;
    private javax.swing.JSlider jSliderDias;
    private javax.swing.JSpinner jSpinnerAsistentes;
    private javax.swing.JSpinner jSpinnerFecha;
    private javax.swing.JSpinner jSpinnerNHabitaciones;
    private javax.swing.JTabbedPane jTabbedPaneClientes;
    private javax.swing.JTable jTableClientes;
    private javax.swing.JTable jTableReservas;
    private javax.swing.JTextField jTextFieldDNI;
    private javax.swing.JTextField jTextFieldFecha;
    private javax.swing.JTextField jTextFieldSalonEscogido;
    // End of variables declaration//GEN-END:variables
}
