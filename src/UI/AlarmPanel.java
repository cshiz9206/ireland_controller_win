package UI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Callback.AlarmCallback;
import Callback.LedFanCallback;
import Callback.StateCallback;
import Connection.Serial;
import Data.PacketData;
import Data.PacketUnit;

public class AlarmPanel extends JPanel implements ActionListener, Runnable {
	
	private static AlarmPanel FFInstance;
	public static AlarmPanel getInstance(Serial connectionInst) { 
		connection = connectionInst;
		if(FFInstance == null) { 
			FFInstance = new AlarmPanel(); 
		}
		return FFInstance;
	}
	
	// for connection
	private static Serial connection;
	boolean isReceived = false;
	
	// for UI
//	Container ct;
	JButton jbSetLedR, jbSetLedB, jbSetFan, jbFanOn, jbFanOff;
	JTextField jtfOnHourB, jtfOnMinuteB, jtfOffHourB, jtfOffMinuteB, jtfOnHourR, jtfOnMinuteR, jtfOffHourR, jtfOffMinuteR, jtfOnHourF, jtfOffHourF, jtfOnMinuteF, jtfOffMinuteF;
	JLabel jlAlarmStateSR, jlAlarmStateER, jlAlarmStateSB, jlAlarmStateEB, jlAlarmStateSF, jlAlarmStateEF;
	
	private AlarmPanel() {
//		ct = getContentPane();
//		ct.setLayout(null);
//		ct.setBackground(Color.white);
		
		setLayout(null);
		setBackground(new Color(170, 200, 170));
		setSize(600, 150);
		setLocation(0, 620);
		
		// create Label
		jlAlarmStateSR = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateSR, 10, 12);
		jlAlarmStateSR.setSize(50, 30);
		jlAlarmStateER = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateER, 10, 24);
		jlAlarmStateER.setSize(50, 30);
		jlAlarmStateSB = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateSB, 10, 52);
		jlAlarmStateSB.setSize(50, 30);
		jlAlarmStateEB = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateEB, 10, 64);
		jlAlarmStateEB.setSize(50, 30);
		jlAlarmStateSF = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateSF, 10, 90);
		jlAlarmStateSF.setSize(50, 30);
		jlAlarmStateEF = new JLabel("?", JLabel.CENTER);
		setLabel(jlAlarmStateEF, 10, 102);
		jlAlarmStateEF.setSize(50, 30);
		
		JLabel jlAlarmR = new JLabel("Red LED Alarm");
		setLabel(jlAlarmR, 75, 17);
		JLabel jlAlarmB = new JLabel("B&W LED Alarm");
		setLabel(jlAlarmB, 75, 57);
		JLabel jlAlarmF = new JLabel("Fan Alarm");
		setLabel(jlAlarmF, 75, 95);
		
		JLabel jlTimeR = new JLabel("Time : ");
		setLabel(jlTimeR, 190, 17);
		JLabel jlTimeB = new JLabel("Time : ");
		setLabel(jlTimeB, 190, 57);
		JLabel jlTimeF = new JLabel("Time : ");
		setLabel(jlTimeF, 190, 95);
		
		JLabel jlColon1 = new JLabel(":");
		setLabel(jlColon1, 290, 17);
		jlColon1.setSize(20, 30);
		JLabel jlColon2 = new JLabel(":");
		setLabel(jlColon2, 290, 57);
		jlColon2.setSize(20, 30);
		JLabel jlColon3 = new JLabel(":");
		setLabel(jlColon3, 290, 95);
		jlColon3.setSize(20, 30);
		
		// create TextField
		jtfOnHourR = new JTextField("hh");
		setTextField(jtfOnHourR, 235, 17);
		jtfOnMinuteR = new JTextField("mm");
		setTextField(jtfOnMinuteR, 300, 17);
		
		jtfOnHourB = new JTextField("hh");
		setTextField(jtfOnHourB, 235, 57);
		jtfOnMinuteB = new JTextField("mm");
		setTextField(jtfOnMinuteB, 300, 57);
		
		jtfOnHourF = new JTextField("hh");
		setTextField(jtfOnHourF, 235, 95);
		jtfOnMinuteF = new JTextField("mm");
		setTextField(jtfOnMinuteF, 300, 95);
		
		JLabel jlWave1 = new JLabel("~");
		setLabel(jlWave1, 355, 17);
		jlWave1.setSize(30, 30);
		JLabel jlWave2 = new JLabel("~");
		setLabel(jlWave2, 355, 57);
		jlWave2.setSize(30, 30);
		JLabel jlWave3 = new JLabel("~");
		setLabel(jlWave3, 355, 95);
		jlWave3.setSize(30, 30);
		
		JLabel jlColon4 = new JLabel(":");
		setLabel(jlColon4, 430, 17);
		jlColon4.setSize(20, 30);
		JLabel jlColon5 = new JLabel(":");
		setLabel(jlColon5, 430, 57);
		jlColon5.setSize(20, 30);
		JLabel jlColon6 = new JLabel(":");
		setLabel(jlColon6, 430, 95);
		jlColon6.setSize(20, 30);
		
		jtfOffHourR = new JTextField("hh");
		setTextField(jtfOffHourR, 375, 17);
		jtfOffMinuteR = new JTextField("mm");
		setTextField(jtfOffMinuteR, 440, 17);
		
		jtfOffHourB = new JTextField("hh");
		setTextField(jtfOffHourB, 375, 57);
		jtfOffMinuteB = new JTextField("mm");
		setTextField(jtfOffMinuteB, 440, 57);
		
		jtfOffHourF = new JTextField("hh");
		setTextField(jtfOffHourF, 375, 95);
		jtfOffMinuteF = new JTextField("mm");
		setTextField(jtfOffMinuteF, 440, 95);
		
		// create Button
		jbSetLedR = new JButton("set");
		setButton(jbSetLedR, 510, 17);
		
		jbSetLedB = new JButton("set");
		setButton(jbSetLedB, 510, 57);
		
		jbSetFan = new JButton("set");
		setButton(jbSetFan, 510, 95);
		
//		jbFanOn = new JButton("On");
//		setButton(jbFanOn, 430, 55);
//		jbFanOff = new JButton("Off");
//		setButton(jbFanOff, 490, 55);
		

		add(jlAlarmStateSR);
		add(jlAlarmStateER);
		add(jlAlarmStateSB);
		add(jlAlarmStateEB);
		add(jlAlarmStateSF);
		add(jlAlarmStateEF);
		add(jlAlarmR);
		add(jlAlarmB);
		add(jlAlarmF);
		add(jlTimeR);
		add(jlTimeB);
		add(jlTimeF);
		add(jtfOnHourR);
		add(jtfOnHourB);
		add(jtfOnHourF);
		add(jtfOffMinuteR);
		add(jtfOffMinuteB);
		add(jtfOffMinuteF);
		add(jtfOffHourR);
		add(jtfOffHourB);
		add(jtfOffHourF);
		add(jtfOnMinuteR);
		add(jtfOnMinuteB);
		add(jtfOnMinuteF);
		add(jlColon1);
		add(jlColon2);
		add(jlColon3);
		add(jlColon4);
		add(jlColon5);
		add(jlColon6);
		add(jlWave1);
		add(jlWave2);
		add(jlWave3);
		add(jbSetLedR);
		add(jbSetLedB);
		add(jbSetFan);
		
//		connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 센서보드 상태체크
		alarmCallback();

//		setTitle("Ireland Controller - Fan Control");
//		setSize(600, 800);
//		setVisible(true);
//		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
	}
	
	private void setLabel(JLabel jlbObj, int x, int y) {
		Font font = new Font("Kristen ITC", Font.PLAIN, 12);
		jlbObj.setLocation(x, y);
		jlbObj.setFont(font);
		jlbObj.setSize(100, 30);
	}
	
	private void setTextField(JTextField tfObj, int x, int y) {
		Font font = new Font("Kristen ITC", Font.PLAIN, 12);
		tfObj.setLocation(x, y);
		tfObj.setSize(50, 30);
		tfObj.setEditable(true);
		tfObj.setVisible(true);
		tfObj.setForeground(Color.white);
		tfObj.setBackground(new Color(90, 120, 90));
		tfObj.setBorder(null);
		tfObj.setFont(font);
	}
	
	private void setButton(JButton btnObj, int x, int y) {
		Font font = new Font("Kristen ITC", Font.PLAIN, 12);
		btnObj.setLocation(x, y);
		btnObj.setSize(60, 30);
		btnObj.setBorderPainted(true);
		btnObj.setContentAreaFilled(false);
		btnObj.setFocusPainted(false);
		btnObj.addActionListener(this);
		btnObj.setOpaque(true);
		btnObj.setFont(font);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			connection.write(PacketUnit.STATE_ALARM_STX_CMD.getPacketStr() + PacketUnit.LED_RED.getPacketStr() + PacketUnit.STATE_ALARM_TAIL.getPacketStr()); // 상태체크
			connection.write(PacketUnit.STATE_ALARM_STX_CMD.getPacketStr() + PacketUnit.LED_BLUEWHITE.getPacketStr() + PacketUnit.STATE_ALARM_TAIL.getPacketStr());
			connection.write(PacketUnit.STATE_ALARM_STX_CMD.getPacketStr() + PacketUnit.FAN.getPacketStr() + PacketUnit.STATE_ALARM_TAIL.getPacketStr());
			isReceived = false;
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			
			if(isReceived) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(jbSetLedR)) {
			connection.write(PacketUnit.ALARM_SET_STX_CMD.getPacketStr() + PacketUnit.LED_RED.getPacketStr() + "FF" + String.format("%02d", (Integer.parseInt(jtfOnHourR.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOnMinuteR.getText()) - 16 + 60) % 60) + String.format("%02d", (Integer.parseInt(jtfOffHourR.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOffMinuteR.getText()) - 16 + 60) % 60) + PacketUnit.ALARM_SET_TAIL.getPacketStr());
		}
		else if(e.getSource().equals(jbSetLedB)) {
			connection.write(PacketUnit.ALARM_SET_STX_CMD.getPacketStr() + PacketUnit.LED_BLUEWHITE.getPacketStr() + "FF" + String.format("%02d", (Integer.parseInt(jtfOnHourB.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOnMinuteB.getText()) - 16 + 60) % 60) + String.format("%02d", (Integer.parseInt(jtfOffHourB.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOffMinuteB.getText()) - 16 + 60) % 60) + PacketUnit.ALARM_SET_TAIL.getPacketStr());
		}
		else if(e.getSource().equals(jbSetFan)) {
			connection.write(PacketUnit.ALARM_SET_STX_CMD.getPacketStr() + PacketUnit.FAN.getPacketStr() + "FF" + String.format("%02d", (Integer.parseInt(jtfOnHourF.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOnMinuteF.getText()) - 16 + 60) % 60) + String.format("%02d", (Integer.parseInt(jtfOffHourF.getText()) - 7 + 24) % 24) + String.format("%02d", (Integer.parseInt(jtfOffMinuteF.getText()) - 16 + 60) % 60) + PacketUnit.ALARM_SET_TAIL.getPacketStr());
		}
	}
	
	private void alarmCallback() {
		PacketData.setAlarmCallback(new AlarmCallback() {

			@Override
			public void set_alarm(String[] red, String[] blue, String[] fan) {
				// TODO Auto-generated method stub
				jlAlarmStateSR.setText(red[0] + " : " + red[1]);
				jlAlarmStateER.setText(red[2] + " : " + red[3]);
				jlAlarmStateSB.setText(blue[0] + " : " + blue[1]);
				jlAlarmStateEB.setText(blue[2] + " : " + blue[3]);
				jlAlarmStateSF.setText(fan[0] + " : " + fan[1]);
				jlAlarmStateEF.setText(fan[2] + " : " + fan[3]);
//				jtfOnHourR.setText(red[0]);
//				jtfOnMinuteR.setText(red[1]);
//				jtfOffHourR.setText(red[2]);
//				jtfOffMinuteR.setText(red[3]);
//				jtfOnHourB.setText(blue[0]);
//				jtfOnMinuteB.setText(blue[1]);
//				jtfOffHourB.setText(blue[2]);
//				jtfOffMinuteB.setText(blue[3]);
//				jtfOnHourF.setText(fan[0]);
//				jtfOnMinuteF.setText(fan[1]);
//				jtfOffHourF.setText(fan[2]);
//				jtfOffMinuteF.setText(fan[3]);
				repaint();
				isReceived = true;
			}
		});
	}
}
