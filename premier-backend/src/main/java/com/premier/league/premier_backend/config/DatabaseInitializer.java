package com.premier.league.premier_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔄 Inicializando base de datos...");

        try {
            // Conectar a MySQL sin especificar BD
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "");

            Statement stmt = conn.createStatement();

            // Crear BD si no existe
            stmt.execute("CREATE DATABASE IF NOT EXISTS premier_league_db;");
            System.out.println("✅ Base de datos 'premier_league_db' lista");

            stmt.close();
            conn.close();

            // Ahora conectar a la BD y ejecutar los scripts
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/premier_league_db?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "");
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            // Leer premier_db.sql desde classpath
            String schemaSQL = readResourceFile("/sql/premier_db.sql");
            System.out.println("📖 Leyendo premier_db.sql...");
            List<String> schemaSQLs = parseSQL(schemaSQL);
            System.out.println("   Encontrados " + schemaSQLs.size() + " statements");
            executeSQLStatements(stmt, schemaSQLs, "tablas");

            // Leer premier_seeds.sql desde classpath
            String seedsSQL = readResourceFile("/sql/premier_seeds.sql");
            System.out.println("📖 Leyendo premier_seeds.sql...");
            List<String> seedsSQLs = parseSQL(seedsSQL);
            System.out.println("   Encontrados " + seedsSQLs.size() + " statements");
            executeSQLStatements(stmt, seedsSQLs, "datos");

            // Actualizar contraseñas con BCrypt válido
            System.out.println("🔐 Actualizando hashes de contraseñas...");
            updatePasswordHashes(stmt);

            conn.commit();
            stmt.close();
            conn.close();

            System.out.println("✅ Base de datos inicializada correctamente!");

        } catch (Exception e) {
            System.out.println("❌ Error inicializando BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String readResourceFile(String resourcePath) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(resourcePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private List<String> parseSQL(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();

        String[] lines = sql.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            // Saltar comentarios vacíos y líneas vacías
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }

            // Agregar la línea al statement actual
            currentStatement.append(line).append("\n");

            // Si la línea termina con ;, es el fin del statement
            if (trimmed.endsWith(";")) {
                String statement = currentStatement.toString().trim();
                if (!statement.isEmpty()) {
                    statements.add(statement);
                }
                currentStatement = new StringBuilder();
            }
        }

        // Si hay algo restante, agregarlo
        if (currentStatement.length() > 0) {
            String statement = currentStatement.toString().trim();
            if (!statement.isEmpty()) {
                statements.add(statement);
            }
        }

        return statements;
    }

    private void executeSQLStatements(Statement stmt, List<String> statements, String tipo) {
        int count = 0;
        int errors = 0;

        for (String sql : statements) {
            try {
                stmt.execute(sql);
                count++;
                System.out.println("  ✓ Statement " + count + " ejecutado");
            } catch (Exception e) {
                String msg = e.getMessage();

                // Ignorar errores de tablas/datos duplicados
                if (msg != null && (msg.contains("already exists") || msg.contains("Duplicate"))) {
                    System.out.println("  ℹ️ Ya existe, ignorando...");
                } else {
                    errors++;
                    System.out.println("  ⚠️ Error: " + msg);
                    System.out.println("     SQL: " + sql.substring(0, Math.min(50, sql.length())) + "...");
                }
            }
        }
        System.out.println("✅ " + tipo + " completados (" + count + " OK, " + errors + " errores)");
    }

    private void updatePasswordHashes(Statement stmt) {
        try {
            // Hash BCrypt válido para la contraseña "password"
            String validHash = "$2a$12$nGs.OKzuFwjaXL4LznU/SOzIT/dWarvaeKr2qqzpt8ZatBIHAFM7i";

            // Actualizar admin_premier
            String updateAdmin = "UPDATE usuarios SET password_hash = '" + validHash
                    + "' WHERE nombre_usuario = 'admin_premier'";
            int rowsAffected1 = stmt.executeUpdate(updateAdmin);
            System.out.println("  ✓ Hash actualizado para admin_premier (" + rowsAffected1 + " fila(s))");

            // Actualizar holahola
            String updateHolahola = "UPDATE usuarios SET password_hash = '" + validHash
                    + "' WHERE nombre_usuario = 'holahola'";
            int rowsAffected2 = stmt.executeUpdate(updateHolahola);
            System.out.println("  ✓ Hash actualizado para holahola (" + rowsAffected2 + " fila(s))");

            System.out.println("✅ Contraseñas actualizadas correctamente (" + (rowsAffected1 + rowsAffected2)
                    + " usuario(s) actualizado(s))");
        } catch (Exception e) {
            System.out.println("⚠️ Error actualizando hashes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
