package net.pierreroudier.vertxpoc;

import java.security.SecureRandom;

public class RandomGeneratorService {
	public static final int DEFAULT_LENGTH = 64;
	private SecureRandom random;

	public RandomGeneratorService() {
		random = new SecureRandom();
	}

	public byte[] generate(int size) {
		byte bytes[] = new byte[size];
		random.nextBytes(bytes);
		return bytes;
	}
	
	public String foo(int length) {
		byte randomBytes[] = generate(length);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Byte b : randomBytes) {
			sb.append(b);
		}
		sb.append("}");
		return sb.toString();
	}
}
