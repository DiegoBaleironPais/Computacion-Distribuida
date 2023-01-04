package GUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BuyerGUI extends javax.swing.JFrame {
    private Buyer comprador;

    public BuyerGUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jButton2.setText("Borrar");
        setResizable(false);

        jLabel1.setText("Titulo");
        jButton1.setText("Crear");
        jLabel2.setText("Precio maximo");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {                },
                new String [] {
                        "Titulo", "Precio umbral"
                }
        ));

        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] {"Titulo", "Estado", "Precio Actual"}));
        jScrollPane2.setViewportView(jTable2);

        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jButton1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jTextField1.getText().replace(" ", "").equals("")
                        && !jTextField2.getText().replace(" ", "").equals("")) {
                    try {
                        Boolean yaEstaba = false;
                        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i,0).equals(jTextField1.getText())) {
                                yaEstaba = true;
                            }
                        }
                        if (yaEstaba==false) {
                            tableModel.addRow(new Object[] {jTextField1.getText(), Float.parseFloat(jTextField2.getText())});
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                    }
                    jTextField1.setText("");
                    jTextField2.setText("");
                }
            }
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                jTable1.getSelectedRow();
                if (jTable1.getSelectedRow() != -1) {
                    modelo.removeRow(jTable1.getSelectedRow());
                }
            }
        });
    }// </editor-fold>

    public boolean puedeComprarLibro (String name, float prize) {
        boolean quiereLibro = false;
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < modelo.getRowCount() && quiereLibro==false; i++) {
            if (modelo.getValueAt(i,0).equals(name) && (Float) modelo.getValueAt(i,1) >=
                prize) {
                quiereLibro = true;
            }
        }
        return quiereLibro;
    }

    public boolean quiereLibro (String name) {
        boolean quiereLibro = false;
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < modelo.getRowCount() && quiereLibro==false; i++) {
            if (modelo.getValueAt(i,0).equals(name)) {
                quiereLibro = true;
            }
        }
        return quiereLibro;
    }

    public void anadirLibro(String nombre, String estado, float precio) {
        DefaultTableModel modelo = (DefaultTableModel) jTable2.getModel();
        modelo.addRow(new Object[] {nombre,estado,precio});
    }

    public void borrarLibro(String nombre, boolean tabla) {
        DefaultTableModel modelo;
        if (tabla == true) {
            modelo = (DefaultTableModel) jTable2.getModel();
        } else {
            modelo = (DefaultTableModel) jTable1.getModel();
        }

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i,0).equals(nombre)) {
                modelo.removeRow(i);
                break;
            }
        }
    }

    public void cerrarVentana() {
        this.dispose();
    }
    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration
}

