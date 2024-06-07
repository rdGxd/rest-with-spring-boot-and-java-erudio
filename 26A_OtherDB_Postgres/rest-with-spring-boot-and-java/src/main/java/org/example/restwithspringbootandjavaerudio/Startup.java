package org.example.restwithspringbootandjavaerudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
    @PropertySource(value = "classpath:application.yml")
})
public class Startup {

  public static void main(String[] args) {
    SpringApplication.run(Startup.class, args);

    /*
     * Map<String, PasswordEncoder> encoders = new HashMap<>();
     * 
     * Pbkdf2PasswordEncoder pbkdf2Encoder =
     * new Pbkdf2PasswordEncoder(
     * "", 8, 185000,
     * Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
     * 
     * encoders.put("pbkdf2", pbkdf2Encoder);
     * DelegatingPasswordEncoder passwordEncoder = new
     * DelegatingPasswordEncoder("pbkdf2", encoders);
     * passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
     * 
     * String result1 = passwordEncoder.encode("admin123");
     * String result2 = passwordEncoder.encode("admin456");
     * System.out.println("My hash result1 " + result1);
     * System.out.println("My hash result2 " + result2);
     * }
     */

    /*
     * PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
     * String result1 = passwordEncoder.encode("admin123");
     * String result2 = passwordEncoder.encode("admin456");
     * System.out.println("My hash result1 " + result1);
     * System.out.println("My hash result2 " + result2);
     */
  }
}
