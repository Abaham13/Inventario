/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unison.pagina;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class DatabaseManager {
     private static final String DB_NAME = "InventarioBD_2.db";

    // Ruta REAL donde existirá el archivo de la BD
    private static final String DB_FOLDER = "C:/InventarioBD/";
    private static String dbFilePath = null;

    public static void inicializar() {
        try {
            System.out.println("═══════════════════════════════════════");
            System.out.println(" INICIANDO DatabaseManager");
            System.out.println("═══════════════════════════════════════");

            File carpeta = new File(DB_FOLDER);

            // Crear la carpeta si no existe
            if (!carpeta.exists()) {
                System.out.println("Creando carpeta de BD: " + carpeta.getAbsolutePath());
                carpeta.mkdirs();
            }

            // Archivo real de la BD
            File archivoBD = new File(DB_FOLDER + DB_NAME);

            // Si NO existe, copiar desde resources
            if (!archivoBD.exists()) {
                System.out.println(" No existe la base de datos en:");
                System.out.println("   " + archivoBD.getAbsolutePath());
                System.out.println("? Copiando base de datos desde resources...");

                copiarBDDesdeResources(archivoBD);
            }

            // Construir ruta JDBC
            dbFilePath = "jdbc:sqlite:" + archivoBD.getAbsolutePath();
            System.out.println(" Base de datos lista: " + dbFilePath);

            // Verificar usuarios
            verificarYCrearUsuarios();

        } catch (Exception e) {
            System.err.println("\n ERROR CRÍTICO: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                "Error al inicializar la base de datos:\n\n" + e.getMessage(),
                "Error Fatal",
                JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }

        System.out.println("═══════════════════════════════════════\n");
    }

    /**
     * Copia la base de datos desde resources a la carpeta real del sistema.
     */
    private static void copiarBDDesdeResources(File destino) throws IOException {
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/" + DB_NAME)) {

            if (is == null) {
                throw new IOException(" El archivo " + DB_NAME + " no está en resources.");
            }

            try (FileOutputStream os = new FileOutputStream(destino)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }

        System.out.println("   ✅ BD copiada correctamente a: " + destino.getAbsolutePath());
    }

    /**
     * Verifica si existen usuarios en la BD, sino los crea
     */
    private static void verificarYCrearUsuarios() {
        System.out.println("\n Verificando usuarios en la BD...");

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Verificar si existe tabla usuarios
            ResultSet rs = stmt.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios'"
            );

            if (!rs.next()) {
                System.out.println("    Tabla usuarios no existe, creándola...");
                crearTablaYUsuarios(conn);
                return;
            }

            rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM usuarios");
            if (rs.next() && rs.getInt("total") == 0) {
                System.out.println("    Tabla vacía, insertando usuarios...");
                insertarUsuariosDefault(conn);
            } else {
                System.out.println("    Usuarios encontrados:");
                rs = stmt.executeQuery("SELECT nombre, rol FROM usuarios");
                while (rs.next()) {
                    System.out.println("      - " + rs.getString("nombre") +
                                       " [" + rs.getString("rol") + "]");
                }
            }

        } catch (SQLException e) {
            System.err.println(" Error verificando usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void crearTablaYUsuarios(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sqlCrearTabla = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE,
                    contrasena TEXT NOT NULL,
                    inicio_sesion TEXT,
                    rol TEXT NOT NULL DEFAULT 'ADMIN'
                );
            """;

            stmt.execute(sqlCrearTabla);
            System.out.println("   ✅ Tabla usuarios creada");

            insertarUsuariosDefault(conn);
        }
    }

    private static void insertarUsuariosDefault(Connection conn) throws SQLException {
        String sqlInsert = "INSERT INTO usuarios (nombre, contrasena, inicio_sesion, rol) VALUES (?, ?, ?, ?)";

        try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

            // Admin
            ps.setString(1, "Admin");
            ps.setString(2, crearHashMD5("admin23"));
            ps.setString(3, "");
            ps.setString(4, "ADMIN");
            ps.executeUpdate();
            System.out.println("    Usuario Admin creado");

            // Almacen
            ps.setString(1, "almacen");
            ps.setString(2, crearHashMD5("almacenll"));
            ps.setString(3, "");
            ps.setString(4, "ALMACENES");
            ps.executeUpdate();
            System.out.println("    Usuario almacen creado");

            // Producto
            ps.setString(1, "producto");
            ps.setString(2, crearHashMD5("producto19"));
            ps.setString(3, "");
            ps.setString(4, "PRODUCTOS");
            ps.executeUpdate();
            System.out.println("    Usuario producto creado");
        }
    }

    private static String crearHashMD5(String input) {
        try {
            java.security.MessageDigest md =
                java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar MD5: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dbFilePath == null) {
            throw new SQLException(" La base de datos no ha sido inicializada. Llama a inicializar() primero.");
        }

        Connection conn = DriverManager.getConnection(dbFilePath);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL;");
            stmt.execute("PRAGMA busy_timeout=5000;");
            stmt.execute("PRAGMA synchronous=NORMAL;");
        }

        return conn;
    }

    public static String getDBPath() {
        return dbFilePath;
    }
}

