import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderTest {

	@Test
	void generateHashForDatabase() {
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		final String rawPassword = "admin123"; // La contraseña que quieras
		final String hash = encoder.encode(rawPassword);

		System.out.println("Contraseña: " + rawPassword);
		System.out.println("Hash para SQL: " + hash);

		assertThat(encoder.matches(rawPassword, hash)).isTrue();
	}
}