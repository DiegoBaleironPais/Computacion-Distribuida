package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SellerGUI extends javax.swing.JFrame {
    private Seller vendedor;
    /**
     * Creates new form SellerGUI
     */
    public SellerGUI(Seller vendedor) {
        this.vendedor = vendedor;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Titulo:");

        jButton1.setText("Agregar");

        jLabel2.setText("Precio de salida");

        jLabel3.setText("Incremento");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "Titulo", "Estado", "Precio Actual", "Numero subastadores"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] { "Titulo", "Precio de salida", "Incremento"}));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!jTextField1.getText().replace(" ", "").equals("")
                        && !jTextField2.getText().replace(" ", "").equals("")
                        && !jTextField3.getText().replace(" ", "").equals("")) {
                    try {
                        Boolean yaEstaba = false;
                        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i,0).equals(jTextField1.getText())) {
                                yaEstaba = true;
                            }
                        }
                        if (yaEstaba==false) {
                            tableModel.addRow(new Object[] {jTextField1.getText(), Float.parseFloat(jTextField2.getText()),
                                    Float.parseFloat(jTextField3.getText())});
                            vendedor.anadirLibro(new SellerBooks(jTextField1.getText(), Float.parseFloat(jTextField2.getText()),
                                    Float.parseFloat(jTextField3.getText())));
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                    }
                    jTextField1.setText("");
                    jTextField2.setText("");
                    jTextField3.setText("");
                }
            }
        });
    }// </editor-fold>

    public void anhadirLibro (String title, String estado, String precioActual, String numeroInteresados) {
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        boolean yaEstaba = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (title.equals(tableModel.getValueAt(i,0))) {
                tableModel.setValueAt(estado, i,1);
                tableModel.setValueAt(precioActual, i,2);
                tableModel.setValueAt(numeroInteresados, i,3);
                yaEstaba = true;
            }
        }
        if (yaEstaba == false) {
            tableModel.addRow(new Object[]{title,estado,precioActual,numeroInteresados});
        }
    }
    public ArrayList<SellerBooks> getBooks () {
        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
        ArrayList<SellerBooks> toReturn = new ArrayList<SellerBooks>();
        SellerBooks aux;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            aux = new SellerBooks( (String) tableModel.getValueAt(i,0), (float) tableModel.getValueAt(i,1),
                    (float) tableModel.getValueAt(i,2));
            toReturn.add(aux);
        }
        return toReturn;
    }

    public void borrarLibro (String nombre) {
        DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i,0).equals(nombre)) {
                tableModel.removeRow(i);
                break;
            }
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration
}
