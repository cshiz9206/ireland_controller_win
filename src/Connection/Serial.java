package Connection;

import java.io.InputStream;
import java.io.OutputStream;

import Data.PacketUnit;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

// Serial connect to LCS Modem
public class Serial {
	private final String PORT = "COM3";
	
	private SerialWriter sw;
	private SerialReader sr;
	
	// for singleton
	private static Serial instance = new Serial();
	
	private Serial() {}
	
	public static Serial getInstance() { return instance; }
	
	public void connect() throws Exception {
		System.loadLibrary("RXTXcomm");
		
		// 식별자(ex.COM3)로 식별자 객체 생성
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(PORT);
			
		// .open([실행 앱(클래스) 이름], [포트 열기 제한 시간]) : 포트 독점적 소유권 얻음
		CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
		
		// SerialPort > Comm(Communication)Port
		if (commPort instanceof SerialPort) {
			SerialPort serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			System.out.println("[Connection] Serial connect to PARUS success");

			InputStream in = serialPort.getInputStream();
			OutputStream out = serialPort.getOutputStream();

			sw = new SerialWriter(out);
			sr = new SerialReader(in);
			sr.start();
			
			//sw.write_byte("0201FF55FF02FF15121513FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
			
			sw.write_byte("0201FF4CFF01FF01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF4CFF02FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
			//sw.write_byte("0201FF4CFF03FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
			
			//sw.write_byte("0201FF50FF01FF000064000018FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
			//sw.write_byte("0201FF50FF02FF000064000030FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF03FF0000000000AAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF000000AA0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF0000AA0000AAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF00006400AA00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF000064AA0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF0064000000AAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF00640000AA00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF006400AA0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF6400000000AAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF64000000AA00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
//			sw.write_byte("0201FF50FF01FF640000AA0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
			
			//sw.write_byte("0201ff73ff00ffffffffffffffffffffffffffffffffffffffffffffff03");
			//sw.write_byte("0201ff53ff00ffffffffffffffffffffffffffffffffffffffffffffff03");
			//sw.write_byte("0201ff52ff00ffffffffffffffffffffffffffffffffffffffffffffff03");

		} else {
			System.out.println("[Connection] Error: Only serial ports are handled by this example.");
		}
	}
	
	public void write(String str) { sw.write_byte(str); }
}
