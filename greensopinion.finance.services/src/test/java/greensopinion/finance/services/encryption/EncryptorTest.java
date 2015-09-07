package greensopinion.finance.services.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EncryptorTest {
	private EncryptorSettings encryptorSettings = EncryptorSettings.newSettings("12345");
	private Encryptor encryptor = new Encryptor(encryptorSettings, "12345");

	@Test
	public void roundTrip() {
		String testData = "d749f39f-d0dc-4092-bf35-fa8442790a7b";
		String encrypted = encryptor.encrypt(testData);
		assertNotNull(encrypted);
		assertFalse(encrypted.equals(testData));
		String decrypted = encryptor.decrypt(encrypted);
		assertEquals(testData, decrypted);
	}

	@Test
	public void roundTripNull() {
		assertNull(encryptor.encrypt(null));
		assertNull(encryptor.decrypt(null));
	}
}