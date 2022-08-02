package Connection;

import java.io.IOException;
import java.io.InputStream;

import Data.PacketData;

public class SerialReader extends Thread {
	InputStream in;
	String savePacket;

	public SerialReader(InputStream in) {
		this.in = in;
	}

	/* ���Ϸ���κ��� ����Ʈ ���� �ޱ� */
	public void run() {
		try {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte[] buffer = new byte[50];
				in.read(buffer);
				
				String packet = "";
				packet = byteArrayToHexString(buffer);
				System.out.println("[Connection] Receive Data : " + packet);
				
				if(!packet.substring(0, 4).equals("0000")) {
					Parsing(packet);
				}
			}

		} catch (IOException /*| InterruptedException*/ e) {
			e.printStackTrace();
		}
	}

	/* Byte -> 16���� String */
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) { // bytes���� ��ü�� ������ b�� ���� (bytes�� ������ �� ���� �ݺ�)
			sb.append(String.format("%02X", b & 0xff));
		}

		return sb.toString();
	}

	/* ������ ���� ������ ���� ��Ŷ �з� */
	public void Parsing(String pack) {
		savePacket = "";
		int start = -1;
		int end = -1;
		
		// for�� �ʿ��Ѱ�?
		for (int i = 0; i < pack.length(); i++) {
			start = pack.indexOf("02", start);
			end = pack.lastIndexOf("03");
			
			// �̷� ��찡 �ֳ�?
			if (start > end || start == -1 || end == -1) {
				continue;
			}
			pack = pack.substring(start, end + 2);
			//System.out.println("�� �ڸ�  " + pack);

			if (start < end) {
				if (pack.length() == 60) {
					savePacket = pack;
					PacketData.divisionPacket(savePacket);
					break;
				}
			}
		}

	}
}
