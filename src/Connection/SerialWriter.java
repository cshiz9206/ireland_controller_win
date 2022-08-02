package Connection;

import java.io.IOException;
import java.io.OutputStream;

public class SerialWriter {
	OutputStream out;

	public SerialWriter(OutputStream out) {
		this.out = out;
	}

	/* String형 패킷 byte로 변환하여 전송 */
	public void write_byte(String str) {
		byte[] packet = hexStringToByteArray(str);
		try {
			out.write(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[Connection] Send Packet : " + str);
	}

	/* String -> 16진수 Byte */
	private byte[] hexStringToByteArray(String str) {
		int len = str.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
		}
		return data;
	}
}
