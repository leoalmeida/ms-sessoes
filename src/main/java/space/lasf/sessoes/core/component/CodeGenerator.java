package space.lasf.sessoes.core.component;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class CodeGenerator {

    private static final int CODE_LENGTH = 8;
    private static final String SALT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomCode() {
        return generateRandomCode(CODE_LENGTH);
    }

    public static String generateRandomCode(Integer size) {
        StringBuilder codeBuilder = new StringBuilder();
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < size; i++) {
                int randomIndex = random.nextInt(SALT.length());
                codeBuilder.append(SALT.charAt(randomIndex));
            }
        } catch (NoSuchAlgorithmException e) {
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                int randomIndex = random.nextInt(SALT.length());
                codeBuilder.append(SALT.charAt(randomIndex));
            }
        }
        return codeBuilder.toString();
    }
}