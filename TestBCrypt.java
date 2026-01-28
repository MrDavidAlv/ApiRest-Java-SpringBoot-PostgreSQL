import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "Admin123!";
        String existingHash = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYKKMQzzzm6";

        // Test if existing hash matches
        boolean matches = encoder.matches(password, existingHash);
        System.out.println("Existing hash matches 'Admin123!': " + matches);

        // Generate new hash
        String newHash = encoder.encode(password);
        System.out.println("New hash for 'Admin123!': " + newHash);
        System.out.println("New hash matches 'Admin123!': " + encoder.matches(password, newHash));
    }
}
