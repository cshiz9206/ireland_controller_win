package System;

import Connection.Serial;
import UI.MainFrame;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			Serial.getInstance().connect();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		new Thread(new MainFrame()).start();
	}

}
