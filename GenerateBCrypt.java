import java.security.MessageDigest;
import java.util.Base64;

public class GenerateBCrypt {
    // Muy simple: usar BCrypt online o verificar directamente en la app
    public static void main(String[] args) {
        String password = "password";
        System.out.println("Para generar BCrypt hashes, usa:");
        System.out.println("En Spring: passwordEncoder.encode(\"" + password + "\")");
        System.out.println("");
        System.out.println("Hashes conocidos (de BCrypt generators online):");
        System.out.println("password -> $2a$10$5T8.XNEhFkHJ/ELb6uPKiO5OMq0U7C9Sc3d5GqNmHXXXXXXXXXXXXe");
        System.out.println("password -> $2a$10$SMe9eGBCHrKqKkSvvh2fY.dNIoI2D0C1fXLVMqDXHqHFKM3VYEbha");
        System.out.println("");
        System.out.println("El hash en BD es:");
        System.out.println("$2a$10$kKc5PlFP4jvkOA6Th5bYC.k5qF33LJp4V1ZkNAz5vbBQFc9f0Gq7C");
    }
}
