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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class Almacenes extends javax.swing.JPanel {

    private String rolUsuario;
    private PaginaInicio ventanaPrincipal;

    /**
     * Creates new form FormularioAlmacen
     */
    public Almacenes(String rol, PaginaInicio ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.rolUsuario = rol;
        initComponents();
        conectarBD();
        configurarBotones();
        seleccionarAlmacenes();
        historialUI();
        verifCols();
        cargarHist();
        aplicarBordesRedondeados();

    }

    private void aplicarfiltros() {
        String id = infoId.getText().trim();
        String nombre = infoNombre.getText().trim();

        StringBuilder sql = new StringBuilder("SELECT * FROM almacenes WHERE id != -1");

        if (!id.isEmpty()) {
            sql.append(" AND id LIKE '%").append(id).append("%'");
        }
        if (!nombre.isEmpty()) {
            sql.append(" AND nombre LIKE '%").append(nombre).append("%'");
        }

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql.toString())) {

            DefaultTableModel modelo = (DefaultTableModel) tablaAlmacenes.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar: " + e.getMessage());
        }
    }

    private void historialUI() {
        jPanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        jPanel3.setBackground(Color.WHITE);
        jPanel3.removeAll();

        addHist("Tipo:", tipoModificacion = new JLabel("Sin cambios"));
        addHist("Fecha / Hora:", ultimaModificacion = new JLabel("---------------"));
        addHist("Nombre:", usuarioModificacion = new JLabel("Ninguno"));

        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void addHist(String titulo, JLabel valor) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Dialog", Font.BOLD, 11));
        lbl.setForeground(Color.BLACK);

        valor.setForeground(new Color(60, 60, 60));

        jPanel3.add(lbl);
        jPanel3.add(valor);
    }

    private void configurarBotones() {
        if ("ALMACENES".equals(rolUsuario) || "ADMIN".equals(rolUsuario)) {
            jButton2.setEnabled(true);
            jButton1.setEnabled(true);
            jButton1.setText("Acceder");
            jButton2.addActionListener(e -> eliminarAlmacen());
            jButton1.addActionListener(e -> abrirFormulario());

        } else {
            jButton2.setEnabled(false);
            jButton1.setEnabled(false);
            jButton1.setText(" No Acceso");

        }
    }

    private void abrirFormulario() {
        if ("ALMACENES".equals(rolUsuario) || "ADMIN".equals(rolUsuario)) {
            FormularioAlmacenes formulario = new FormularioAlmacenes(ventanaPrincipal);
            ventanaPrincipal.cargarPublico(formulario);
        } else {
            JOptionPane.showMessageDialog(this, "No tienes permisos para modificar almacenes.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFiltros() {
        infoId.setText("");
        infoNombre.setText("");

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
        infoId.setBorder(borde8px);
        infoNombre.setBorder(borde8px);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        ultimaModificacion = new javax.swing.JLabel();
        usuarioModificacion = new javax.swing.JLabel();
        tipoModificacion = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAlmacenes = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        infoId = new javax.swing.JTextField();
        infoNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel3.setText("jLabel3");

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 85, 154), 3, true));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1126, 1126, 1126)
                .addComponent(ultimaModificacion)
                .addGap(133, 133, 133)
                .addComponent(usuarioModificacion)
                .addGap(106, 106, 106)
                .addComponent(tipoModificacion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ultimaModificacion)
                    .addComponent(usuarioModificacion)
                    .addComponent(tipoModificacion))
                .addGap(49, 49, 49))
        );

        jPanel6.setBackground(new java.awt.Color(248, 196, 7));

        jScrollPane1.setBackground(new java.awt.Color(0, 82, 158));

        tablaAlmacenes.setBackground(new java.awt.Color(204, 204, 255));
        tablaAlmacenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Nombre"
            }
        ));
        tablaAlmacenes.setFocusable(false);
        jScrollPane1.setViewportView(tablaAlmacenes);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 85, 157));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 86, 154), 2, true));

        jLabel1.setForeground(new java.awt.Color(242, 187, 0));
        jLabel1.setText("Id:");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(242, 187, 0));
        jLabel2.setText("Nombre:");

        jButton2.setBackground(new java.awt.Color(0, 82, 158));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Eliminar");

        jButton1.setBackground(new java.awt.Color(0, 82, 158));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("AgregarModificar");

        jButton3.setBackground(new java.awt.Color(0, 82, 158));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("consultar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 82, 158));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoId, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 33, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(infoId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(infoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(11, 11, 11)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(45, 45, 45))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(0, 82, 158));

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 82, 158));
        jTextField3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("comunicacion@unison.mx");
        jTextField3.setBorder(null);
        jTextField3.setFocusable(false);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/correo v2.png"))); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ALOGO-OJO-BUHO-AMARILLO v3.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 347, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap())))
        );

        add(jPanel2, java.awt.BorderLayout.NORTH);

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
            .addGap(0, 42, Short.MAX_VALUE)
        );

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(0, 82, 158));
        jTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("Blvd. Luis Encinas J, Calle Av. Rosales &, Centro, 83000 Hermosillo, Son.");
        jTextField4.setBorder(null);
        jTextField4.setFocusable(false);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ubicacionesv.png"))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel7, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        aplicarfiltros();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        limpiarFiltros();
    }//GEN-LAST:event_jButton4ActionPerformed

    private Connection conn;

    private void conectarBD() {
        try {
            conn = DatabaseManager.getConnection();
            System.out.println(" Conectado a la base de datos (Almacenes)");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la BD: " + e.getMessage());
        }
    }

    private void verifCols() {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("PRAGMA table_info(almacenes)")) {

            boolean f = false, u = false, t = false;

            while (rs.next()) {
                switch (rs.getString("name")) {
                    case "ultima_modificacion" ->
                        f = true;
                    case "usuario_modificacion" ->
                        u = true;
                    case "tipo_modificacion" ->
                        t = true;
                }
            }
            if (!f) {
                st.execute("ALTER TABLE almacenes ADD COLUMN ultima_modificacion TEXT");
            }
            if (!u) {
                st.execute("ALTER TABLE almacenes ADD COLUMN usuario_modificacion TEXT");
            }
            if (!t) {
                st.execute("ALTER TABLE almacenes ADD COLUMN tipo_modificacion TEXT");
            }

        } catch (SQLException e) {
            System.err.println("Error columnas: " + e.getMessage());
        }
    }

    final void cargarHist() {
        String q = """
        SELECT ultima_modificacion, usuario_modificacion, tipo_modificacion
        FROM almacenes
        WHERE ultima_modificacion IS NOT NULL
        ORDER BY ultima_modificacion DESC
        LIMIT 1
    """;

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(q)) {

            if (rs.next()) {
                String f = rs.getString(1);
                String u = rs.getString(2);
                String t = rs.getString(3);

                ultimaModificacion.setText(f != null ? f : "Sin registros");
                usuarioModificacion.setText(u != null ? u : "Desconocido");
                tipoModificacion.setText(t != null ? t : "Sin cambios");

                switch (t) {
                    case "AGREGADO" ->
                        tipoModificacion.setForeground(new Color(0, 128, 0));
                    case "MODIFICADO" ->
                        tipoModificacion.setForeground(new Color(255, 140, 0));
                    case "ELIMINADO" ->
                        tipoModificacion.setForeground(new Color(178, 34, 34));
                }

            } else {
                ultimaModificacion.setText("--/--/---- --:--:--");
                usuarioModificacion.setText("Ninguno");
                tipoModificacion.setText("Sin cambios");
            }

        } catch (SQLException e) {
            System.err.println("Error historial: " + e.getMessage());
        }
    }

    private void seleccionarAlmacenes() {
        String sql = "SELECT * FROM almacenes";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            DefaultTableModel modelo = (DefaultTableModel) tablaAlmacenes.getModel();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("nombre")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar almacenes: " + e.getMessage());
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

    private void eliminarAlmacen() {
        int filaSeleccionada = tablaAlmacenes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un almacén para eliminar.");
            return;
        }

        int id = (int) tablaAlmacenes.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tablaAlmacenes.getValueAt(filaSeleccionada, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Quieres eliminar este almacén?", "Confirmar eliminar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PreparedStatement pstmt = null;
            PreparedStatement pstmtOtro = null;
            try {
                String fechaHora = java.time.LocalDateTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                String usuario = obtenerNombreUsuario();

                
                String sqlDelete = "DELETE FROM almacenes WHERE id = ?";
                pstmt = conn.prepareStatement(sqlDelete);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();

                
                String sqlBuscar = "SELECT id FROM almacenes LIMIT 1";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlBuscar);

                if (rs.next()) {
                    
                    int idOtro = rs.getInt("id");
                    String sqlHistorial = "UPDATE almacenes SET ultima_modificacion = ?, "
                            + "usuario_modificacion = ?, tipo_modificacion = ? WHERE id = ?";
                    pstmtOtro = conn.prepareStatement(sqlHistorial);
                    pstmtOtro.setString(1, fechaHora);
                    pstmtOtro.setString(2, usuario);
                    pstmtOtro.setString(3, "ELIMINADO");
                    pstmtOtro.setInt(4, idOtro);
                    pstmtOtro.executeUpdate();
                } else {
                    
                    String sqlInsert = "INSERT INTO almacenes (id, nombre, ultima_modificacion, usuario_modificacion, tipo_modificacion) VALUES (-1, 'HISTORIAL_TEMP', ?, ?, ?)";
                    pstmtOtro = conn.prepareStatement(sqlInsert);
                    pstmtOtro.setString(1, fechaHora);
                    pstmtOtro.setString(2, usuario);
                    pstmtOtro.setString(3, "ELIMINADO");
                    pstmtOtro.executeUpdate();
                }

                rs.close();
                stmt.close();

                JOptionPane.showMessageDialog(this, "Almacén eliminado correctamente.");
                seleccionarAlmacenes();
                cargarHist();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar almacén: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (pstmtOtro != null) {
                        pstmtOtro.close();
                    }
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String obtenerNombreUsuario() {
        String nombreUsuario = rolUsuario;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nombre FROM usuarios WHERE rol = ? LIMIT 1";
            ps = conn.prepareStatement(sql);
            ps.setString(1, rolUsuario);
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
    private javax.swing.JTextField infoId;
    private javax.swing.JTextField infoNombre;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTable tablaAlmacenes;
    private javax.swing.JLabel tipoModificacion;
    private javax.swing.JLabel ultimaModificacion;
    private javax.swing.JLabel usuarioModificacion;
    // End of variables declaration//GEN-END:variables
}
