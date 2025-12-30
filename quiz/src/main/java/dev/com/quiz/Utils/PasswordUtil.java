package dev.com.quiz.Utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    // Hash password
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // Verify password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}

