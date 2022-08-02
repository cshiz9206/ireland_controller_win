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

	/* 아일랜드로부터 바이트 수신 받기 */
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

	/* Byte -> 16진수 String */
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) { // bytes에서 객체를 꺼내어 b에 저장 (bytes가 없어질 때 까지 반복)
			sb.append(String.format("%02X", b & 0xff));
		}

		return sb.toString();
	}

	/* 쓰레기 값을 제거한 순수 패킷 분류 */
	public void Parsing(String pack) {
		savePacket = "";
		int start = -1;
		int end = -1;
		
		// for문 필요한가?
		for (int i = 0; i < pack.length(); i++) {
			start = pack.indexOf("02", start);
			end = pack.lastIndexOf("03");
			
			// 이런 경우가 있나?
			if (start > end || start == -1 || end == -1) {
				continue;
			}
			pack = pack.substring(start, end + 2);
			//System.out.println("팩 자름  " + pack);

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
