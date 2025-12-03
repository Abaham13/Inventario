/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package mx.unison.pagina;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Productos extends javax.swing.JPanel {

    private String rolesUsuario;
    private PaginaInicio ventanaPrincipal;

    /**
     * Creates new form FormularioProducto
     */
    public Productos(String rol, PaginaInicio ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.rolesUsuario = rol;
        initComponents();
        conectarBD();
        configurarBotones();
        seleccionarProductos();
        initHistorialPanel();
        asegurarColsHistorial();
        cargarHistorialReciente();
        aplicarBordesRedondeados();
    }

    private void aplicarFiltros() {
        String dep = infoDepartamento.getText().trim();
        String alm = infoAlmacen.getText().trim();
        String pMin = precioMin.getText().trim();
        String pMax = precioMax.getText().trim();
        String cMin = cantidadMin.getText().trim();
        String cMax = cantidadMax.getText().trim();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.nombre, p.precio, p.cantidad, p.departamento, a.nombre AS nombre_almacen "
                + "FROM productos p LEFT JOIN almacenes a ON p.almacen = a.id WHERE p.id != -1"
        );

        if (!dep.isEmpty()) {
            sql.append(" AND p.departamento LIKE '%").append(dep).append("%'");
        }
        if (!alm.isEmpty()) {
            sql.append(" AND a.nombre LIKE '%").append(alm).append("%'");
        }

        sql.append(validarNumero(pMin, "p.precio >=", false));
        sql.append(validarNumero(pMax, "p.precio <=", false));
        sql.append(validarNumero(cMin, "p.cantidad >=", true));
        sql.append(validarNumero(cMax, "p.cantidad <=", true));

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {

            DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("departamento"),
                    rs.getString("nombre_almacen")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar: " + e.getMessage());
        }
    }

    private String validarNumero(String txt, String sqlCond, boolean esEntero) {
        if (txt.isEmpty()) {
            return "";
        }
        try {
            double val = esEntero ? Integer.parseInt(txt) : Double.parseDouble(txt);
            return " AND " + sqlCond + " " + val;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor inválido: " + txt);
            return "";
        }
    }

    private void initHistorialPanel() {
        jPanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        jPanel3.setBackground(Color.WHITE);
        jPanel3.removeAll();

        agregarEtiquetaHistorial("Tipo:", tipoModificacion = new JLabel("Sin cambios"));
        agregarEtiquetaHistorial("Fecha / Hora:", ultimaModificacion = new JLabel("---------------"));
        agregarEtiquetaHistorial("Nombre:", usuarioModificacion = new JLabel("Ninguno"));
    }

    private void agregarEtiquetaHistorial(String titulo, JLabel valor) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Dialog", Font.BOLD, 11));
        lbl.setForeground(Color.BLACK);

        valor.setForeground(new Color(60, 60, 60));

        jPanel3.add(lbl);
        jPanel3.add(valor);
    }

    private void configurarBotones() {
        if ("PRODUCTOS".equals(rolesUsuario) || "ADMIN".equals(rolesUsuario)) {
            jButton2.setEnabled(true);
            jButton1.setEnabled(true);
            jButton1.setText("Acceder");
            jButton2.addActionListener(e -> eliminarProducto());
            jButton1.addActionListener(e -> abrirFormulario());
        } else {

            jButton2.setEnabled(false);
            jButton1.setEnabled(false);
            jButton1.setText("No Acceso");
        }
    }

    private void abrirFormulario() {
        if ("PRODUCTOS".equals(rolesUsuario) || "ADMIN".equals(rolesUsuario)) {
            FormularioProductos formulario = new FormularioProductos(ventanaPrincipal);
            ventanaPrincipal.cargarPublico(formulario);
        } else {
            JOptionPane.showMessageDialog(this, "No tienes permisos para modificar productos.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarBordesRedondeados() {
        javax.swing.border.Border borde8px = new javax.swing.border.AbstractBorder() {
            private final int radius = 8;

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                g.setColor(Color.GRAY);
                g.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(6, 6, 6, 6); // padding interno
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
        precioMax.setBorder(borde8px);
        precioMin.setBorder(borde8px);
        cantidadMax.setBorder(borde8px);
        cantidadMin.setBorder(borde8px);
        infoAlmacen.setBorder(borde8px);
        infoDepartamento.setBorder(borde8px);
    }

    private void limpiarFiltros() {
        infoDepartamento.setText("");
        infoAlmacen.setText("");
        precioMin.setText("");
        precioMax.setText("");
        cantidadMin.setText("");
        cantidadMax.setText("");
        seleccionarProductos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        ultimaModificacion = new javax.swing.JLabel();
        usuarioModificacion = new javax.swing.JLabel();
        tipoModificacion = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        precioMax = new javax.swing.JTextField();
        precioMin = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cantidadMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cantidadMax = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        infoDepartamento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        infoAlmacen = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(0, 82, 158));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ALOGO-OJO-BUHO-AMARILLO v3.png"))); // NOI18N

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(0, 82, 158));
        jTextField7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(255, 255, 255));
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setText("comunicacion@unison.mx");
        jTextField7.setBorder(null);
        jTextField7.setFocusable(false);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/correo v2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 507, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(0, 82, 158));

        tablaProductos.setBackground(new java.awt.Color(204, 204, 255));
        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "nombre", "precio", "cantidad", "departamento", "almacen"
            }
        ));
        tablaProductos.setFocusable(false);
        jScrollPane1.setViewportView(tablaProductos);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(165, 165, 165)
                .addComponent(ultimaModificacion)
                .addGap(133, 133, 133)
                .addComponent(usuarioModificacion)
                .addGap(106, 106, 106)
                .addComponent(tipoModificacion)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ultimaModificacion)
                    .addComponent(usuarioModificacion)
                    .addComponent(tipoModificacion))
                .addGap(28, 28, 28))
        );

        jPanel4.setBackground(new java.awt.Color(1, 85, 157));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255), 3));

        jLabel1.setForeground(new java.awt.Color(247, 188, 0));
        jLabel1.setText("PMIN");

        jLabel2.setForeground(new java.awt.Color(247, 188, 0));
        jLabel2.setText("PMAX");

        jLabel3.setForeground(new java.awt.Color(231, 181, 0));
        jLabel3.setText("CMIN");

        jLabel4.setForeground(new java.awt.Color(248, 180, 0));
        jLabel4.setText("CMAX");

        jLabel5.setForeground(new java.awt.Color(248, 185, 51));
        jLabel5.setText("DEPAR");

        jLabel6.setForeground(new java.awt.Color(244, 186, 0));
        jLabel6.setText("Almacen");

        jButton2.setBackground(new java.awt.Color(0, 82, 158));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Eliminar");

        jButton1.setBackground(new java.awt.Color(0, 82, 158));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("AgregarModificar");

        jButton3.setBackground(new java.awt.Color(0, 86, 157));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Limpiar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 86, 157));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("consultar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(precioMin, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(precioMax, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cantidadMin, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cantidadMax, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(infoAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addContainerGap())))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(precioMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(precioMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cantidadMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cantidadMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(infoAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(65, 65, 65))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel5.add(jPanel1);

        add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(0, 82, 158));

        jPanel8.setBackground(new java.awt.Color(248, 187, 0));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 41, Short.MAX_VALUE)
        );

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ubicacionesv.png"))); // NOI18N

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(0, 82, 158));
        jTextField8.setForeground(new java.awt.Color(255, 255, 255));
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("Blvd. Luis Encinas J, Calle Av. Rosales &, Centro, 83000 Hermosillo, Son.");
        jTextField8.setBorder(null);
        jTextField8.setFocusable(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(455, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        add(jPanel7, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        aplicarFiltros();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        limpiarFiltros();
    }//GEN-LAST:event_jButton3ActionPerformed

    private Connection conn;

    private void conectarBD() {
        try {
            conn = DatabaseManager.getConnection();
            System.out.println(" Conectado a la base de datos (Productos)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la BD: " + e.getMessage());
        }
    }

    private void asegurarColsHistorial() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("PRAGMA table_info(productos)");

            boolean colFecha = false, colUser = false, colTipo = false;

            while (rs.next()) {
                switch (rs.getString("name")) {
                    case "ultima_modificacion" ->
                        colFecha = true;
                    case "usuario_modificacion" ->
                        colUser = true;
                    case "tipo_modificacion" ->
                        colTipo = true;
                }
            }

            if (!colFecha) {
                st.execute("ALTER TABLE productos ADD COLUMN ultima_modificacion TEXT");
            }
            if (!colUser) {
                st.execute("ALTER TABLE productos ADD COLUMN usuario_modificacion TEXT");
            }
            if (!colTipo) {
                st.execute("ALTER TABLE productos ADD COLUMN tipo_modificacion TEXT");
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Error al verificar columnas: " + e.getMessage());
        }
    }

    private void cargarHistorialReciente() {
    String sql = """
        SELECT ultima_modificacion, usuario_modificacion, tipo_modificacion
        FROM productos
        WHERE ultima_modificacion IS NOT NULL
        ORDER BY ultima_modificacion DESC LIMIT 1
        """;

    try (Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        if (rs.next()) {
            String fecha = rs.getString("ultima_modificacion");
            String user  = rs.getString("usuario_modificacion");
            String tipo  = rs.getString("tipo_modificacion");

            ultimaModificacion.setText(fecha != null ? fecha : "Sin registros");
            usuarioModificacion.setText(user != null ? user : "Desconocido");
            tipoModificacion.setText(tipo != null ? tipo : "Sin cambios");

            switch (tipo) {
                case "AGREGADO"   -> tipoModificacion.setForeground(new Color(0,128,0));
                case "MODIFICADO" -> tipoModificacion.setForeground(new Color(255,140,0));
                case "ELIMINADO"  -> tipoModificacion.setForeground(new Color(178,34,34));
            }
        } else {
            ultimaModificacion.setText("--/--/---- --:--:--");
            usuarioModificacion.setText("Ninguno");
            tipoModificacion.setText("Sin cambios");
        }

    } catch (SQLException e) {
        System.err.println("Error al cargar historial: " + e.getMessage());
    }
}

    private void seleccionarProductos() {

        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT p.id, p.nombre, p.precio, p.cantidad, p.departamento, a.nombre AS nombre_almacen "
                + "FROM productos p LEFT JOIN almacenes a ON p.almacen = a.id WHERE p.id != -1";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            DefaultTableModel modelo = (DefaultTableModel) tablaProductos.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("departamento"),
                    rs.getString("nombre_almacen")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero una fila para eliminar.");
            return;
        }

        int id = (int) tablaProductos.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Quieres eliminar el producto?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PreparedStatement pstmtDelete = null;
            PreparedStatement pstmtHistorial = null;
            PreparedStatement pstmtInsert = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                String fechaHora = java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                String usuario = obtenerNombreUsuario();

                String sqlBuscar = "SELECT id FROM productos WHERE id != ? AND id != -1 LIMIT 1";
                stmt = conn.createStatement();
                pstmtDelete = conn.prepareStatement(sqlBuscar);
                pstmtDelete.setInt(1, id);
                rs = pstmtDelete.executeQuery();

                if (rs.next()) {
                    int idOtro = rs.getInt("id");

                    String sqlHistorial = "UPDATE productos SET ultima_modificacion = ?, "
                            + "usuario_modificacion = ?, tipo_modificacion = ? WHERE id = ?";
                    pstmtHistorial = conn.prepareStatement(sqlHistorial);
                    pstmtHistorial.setString(1, fechaHora);
                    pstmtHistorial.setString(2, usuario);
                    pstmtHistorial.setString(3, "ELIMINADO");
                    pstmtHistorial.setInt(4, idOtro);
                    pstmtHistorial.executeUpdate();

                    String sqlDelete = "DELETE FROM productos WHERE id = ?";
                    pstmtDelete = conn.prepareStatement(sqlDelete);
                    pstmtDelete.setInt(1, id);
                    pstmtDelete.executeUpdate();

                } else {

                    rs.close();
                    stmt.close();

                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT id FROM productos WHERE id = -1");

                    if (rs.next()) {

                        String sqlHistorial = "UPDATE productos SET ultima_modificacion = ?, "
                                + "usuario_modificacion = ?, tipo_modificacion = ? WHERE id = -1";
                        pstmtHistorial = conn.prepareStatement(sqlHistorial);
                        pstmtHistorial.setString(1, fechaHora);
                        pstmtHistorial.setString(2, usuario);
                        pstmtHistorial.setString(3, "ELIMINADO");
                        pstmtHistorial.executeUpdate();
                    } else {

                        String sqlInsert = "INSERT INTO productos (id, nombre, precio, cantidad, departamento, "
                                + "almacen, ultima_modificacion, usuario_modificacion, tipo_modificacion) "
                                + "VALUES (-1, 'HISTORIAL_TEMP', 0, 0, 'TEMP', 1, ?, ?, ?)";
                        pstmtInsert = conn.prepareStatement(sqlInsert);
                        pstmtInsert.setString(1, fechaHora);
                        pstmtInsert.setString(2, usuario);
                        pstmtInsert.setString(3, "ELIMINADO");
                        pstmtInsert.executeUpdate();
                    }

                    String sqlDelete = "DELETE FROM productos WHERE id = ?";
                    pstmtDelete = conn.prepareStatement(sqlDelete);
                    pstmtDelete.setInt(1, id);
                    pstmtDelete.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                seleccionarProductos();
                cargarHistorialReciente();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (pstmtDelete != null) {
                        pstmtDelete.close();
                    }
                    if (pstmtHistorial != null) {
                        pstmtHistorial.close();
                    }
                    if (pstmtInsert != null) {
                        pstmtInsert.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void recargarDatosYHistorial() {
        seleccionarProductos();
        cargarHistorialReciente();
    }

    private String obtenerNombreUsuario() {
        String nombreUsuario = rolesUsuario;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nombre FROM usuarios WHERE rol = ? LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setString(1, rolesUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                nombreUsuario = rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombre de usuario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nombreUsuario;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cantidadMax;
    private javax.swing.JTextField cantidadMin;
    private javax.swing.JTextField infoAlmacen;
    private javax.swing.JTextField infoDepartamento;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField precioMax;
    private javax.swing.JTextField precioMin;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JLabel tipoModificacion;
    private javax.swing.JLabel ultimaModificacion;
    private javax.swing.JLabel usuarioModificacion;
    // End of variables declaration//GEN-END:variables
}
