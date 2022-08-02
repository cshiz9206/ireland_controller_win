package Data;

import Callback.AlarmCallback;
import Callback.LedFanCallback;
import Callback.StateCallback;

public class PacketData {

	static StateCallback stateCallback;
	static LedFanCallback ledfan_callback;
	static AlarmCallback alarm_callback;

	public PacketData() {}

	public static void setStateCallback(StateCallback sc) {
		stateCallback = sc;
	}

	public static void setLedFanCallback(LedFanCallback lfc) {
		ledfan_callback = lfc;
	}

	public static void setAlarmCallback(AlarmCallback ac) {
		alarm_callback = ac;
	}

	public static void divisionPacket(String packet) {
		System.out.println("[Data] final received packet : " + packet);
		String key = packet.substring(0, 8);

		if (key.equals("0202FF53")) {
			sensorBoard(packet);
		}

		else if (key.equals("0201FF53")) {
			LedOnOff(packet);
		}

		else if (key.equals("0201FF73")) {
			LedData(packet);
		}

		else if (key.equals("0201FF52")) {
			AlarmData(packet);
		}
	}

	/* 센서보드에 시간, 온습도 데이터 파싱 */
	public static void sensorBoard(String packet) {
		String[] value = new String[5];
		System.out.println("[Data] SensorBoard Packet : " + packet);
		
		if(packet.substring(14, 27).contentEquals("FFFFFFFFFFFFF")) {
			return;
		}
		
		String time, temp, humi, co2, illum, gas;

		time = ((Integer.parseInt(packet.substring(14, 16)) + 6) % 24) + ":" + packet.substring(16, 18);
		temp = packet.substring(21, 22) + packet.substring(23, 24) + "." + packet.substring(25, 26) + " °C";
		humi = packet.substring(29, 30) + packet.substring(31, 32) + "." + packet.substring(33, 34) + " %";
		co2 = packet.substring(37, 38);
		if (!co2.equals("0")) {
			co2 += packet.substring(39, 40) + packet.substring(41, 42) + packet.substring(42, 43) + " ppm";
		} else {
			co2 = packet.substring(39, 40) + packet.substring(41, 42) + packet.substring(42, 43) + " ppm";
		}

		illum = packet.substring(47, 48);
		if (!illum.equals("0")) {
			illum += packet.substring(49, 50) + packet.substring(51, 52) + packet.substring(53, 54) + " [lx]";
		} else {
			illum = packet.substring(49, 50) + packet.substring(51, 52) + packet.substring(53, 54) + " [lx]";
		}

		gas = packet.substring(55, 56);
		if (!gas.equals("0")) {
			gas += packet.substring(56, 58);
		} else {
			gas = packet.substring(56, 58);
		}
		
		value[0] = temp.substring(0,temp.length()-2);
		value[1] = humi.substring(0,humi.length()-2);
		value[2] = co2.substring(0, co2.length()-4);
		value[3] = illum.substring(0,illum.length()-5);
		value[4] = gas;
		
		stateCallback.set_state_value(time, temp, humi, co2, illum, gas);
	}
	
//	public String[] getValue() {
//		return value;
//	}

	/* LED On/Off 상태 체크 */
	public static void LedOnOff(String packet) {
		System.out.println("[Data] Led On/Off Packet : " + packet);
		
		boolean RedOn, BlueOn, FanOn; // Led,Fan On,Off State

		if (packet.substring(16, 18).equals("01")) {
			System.out.println("[Data] Red On");
			RedOn = true;
		} else {
			System.out.println("[Data] Red Off");
			RedOn = false;
		}

		if (packet.substring(18, 20).equals("01")) {
			System.out.println("[Data] Blue On");
			BlueOn = true;
		} else {
			System.out.println("[Data] Blue Off");
			BlueOn = false;
		}

		if (packet.substring(20, 22).equals("01")) {
			System.out.println("[Data] Fan On");
			FanOn = true;
		} else {
			System.out.println("[Data] Fan Off");
			FanOn = false;
		}
		
		if(ledfan_callback != null) {
			ledfan_callback.set_switch(RedOn, BlueOn, FanOn);
		}
	}

	/* LED Data 상태 체크 */
	public static void LedData(String packet) {
		System.out.println("[Data] Led Data Packet : " + packet);
		
		String Red, Blue, Fan; // Led,Fan Power Data

		Red = Integer.toString(Integer.parseInt(packet.substring(10, 12), 16));
		Blue = Integer.toString(Integer.parseInt(packet.substring(24, 26), 16));
		Fan = Integer.toString(Integer.parseInt(packet.substring(38, 40), 16));
		
		System.out.println("[Data] Red : " + Red + ", Blue&White : " + Blue + ", Fan : " + Fan);
		
		if(ledfan_callback != null) {
			ledfan_callback.set_power(Red, Blue, Fan);
		}
	}

	/* Alarm Time Data 상태 체크 */
	public static void AlarmData(String packet) {
		System.out.println("[Data] Alarm Time Data Packet : " + packet);
		
		String[] RedTime, BlueTime, FanTime; // Led,Fan Alarm Time
		
		RedTime = new String[4]; // On_H, On_M, Off_H, Off_M;
		BlueTime = new String[4];
		FanTime = new String[4];

		for (int i = 0, t = 0; i < RedTime.length; i++, t += 2) {
			RedTime[i] = packet.substring(10 + t, 12 + t);
			BlueTime[i] = packet.substring(20 + t, 22 + t);
			FanTime[i] = packet.substring(30 + t, 32 + t);
			if(i % 2 == 0) {
				RedTime[i] = String.valueOf((Integer.parseInt(RedTime[i]) + 7) % 24);
				BlueTime[i] = String.valueOf((Integer.parseInt(BlueTime[i]) + 7) % 24);
				FanTime[i] = String.valueOf((Integer.parseInt(FanTime[i]) + 7) % 24);
			}
			else {
				RedTime[i] = String.valueOf((Integer.parseInt(RedTime[i]) + 16) % 60);
				BlueTime[i] = String.valueOf((Integer.parseInt(BlueTime[i]) + 16) % 60);
				FanTime[i] = String.valueOf((Integer.parseInt(FanTime[i]) + 16) % 60);
			}
			
			if(alarm_callback != null) {
				alarm_callback.set_alarm(RedTime, BlueTime, FanTime);
			}
		}
	}
}
