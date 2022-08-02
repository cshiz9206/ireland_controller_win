package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Callback.StateCallback;
import Connection.Serial;
import Data.PacketData;
import Data.PacketUnit;

public class MainFrame extends JFrame implements ActionListener, Runnable {
	
	//for UI
	Container ct;
	JLabel[] jlValue;
	JLabel jlPot, jlGas, jlIlluminance, jlCo2, jlHumidity, jlTemperature;
	JLabel jlTemperatureValue, jlHumidityValue, jlCo2Value, jlIlluminanceValue, jlGasValue, jlTimeValue;
	JButton jbFan, jbAlarm, jbLed, jbBack, jbPause, jbRestart;
	JPanel jpSouth;
	JPanel beforePanel = null;
	Thread beforeThread = null;
	
	// for func
	Serial connection;
	boolean isReceived = false;

	public MainFrame() {
		// TODO Auto-generated method stub
		connect();

		ct = getContentPane();
		ct.setLayout(null);
		ct.setBackground(new Color(255, 203, 210));
		
		jpSouth = new JPanel(null);
		jpSouth.setBackground(new Color(170, 200, 170));
		jpSouth.setSize(600, 150);
		jpSouth.setLocation(0, 620);
		
		// create Button
		jbFan = new JButton(new ImageIcon(".\\figure\\fan-icon-26(2).png"));
		setButton(jbFan, 345, 50);

		jbAlarm = new JButton(new ImageIcon(".\\figure\\alarma-icon-496777(2).png"));
		setButton(jbAlarm, 275, 50);
		
		jbLed = new JButton(new ImageIcon(".\\figure\\led-icon(2).png"));
		setButton(jbLed, 205, 50);
		
		jbPause = new JButton(new ImageIcon(".\\figure\\pause (2).png"));
		setButton(jbPause, 470, 574);
		
		jbRestart = new JButton(new ImageIcon(".\\figure\\restart (2).png"));
		setButton(jbRestart, 513, 582);
		
		jbBack = new JButton(new ImageIcon(".\\figure\\back (2).png"));
		setButton(jbBack, 543, 581);
		
		jlPot = new JLabel(new ImageIcon(".\\figure\\giphy.gif")); //https://www.primogif.com/p/3oEjHOUcNRKgpqTHiM
		jlPot.setSize(jlPot.getIcon().getIconWidth(), jlPot.getIcon().getIconHeight());
		//jlPot.setSize(100, 100);
		//jlPot.setLocation(205, 380);
		jlPot.setLocation(45, 120);
		
		Font font = new Font("Kristen ITC", Font.PLAIN, 14);
		
		jlTemperature = new JLabel(new ImageIcon(".\\figure\\Temperature.png"));
		jlTemperature.setSize(jlTemperature.getIcon().getIconWidth(), jlTemperature.getIcon().getIconHeight());
		jlTemperature.setLocation(70, 190);
		jlTemperatureValue = new JLabel("?", JLabel.CENTER);
		jlTemperatureValue.setLocation(69, 234);
		jlTemperatureValue.setFont(font);
		jlTemperatureValue.setSize(50, 50);
		
		jlHumidity = new JLabel(new ImageIcon(".\\figure\\Humidity.png"));
		jlHumidity.setSize(jlHumidity.getIcon().getIconWidth(), jlHumidity.getIcon().getIconHeight());
		jlHumidity.setLocation(160, 115);
		jlHumidityValue = new JLabel("?", JLabel.CENTER);
		jlHumidityValue.setLocation(160, 160);
		jlHumidityValue.setFont(font);
		jlHumidityValue.setSize(50, 50);
		
		jlCo2 = new JLabel(new ImageIcon(".\\figure\\Co2.png"));
		jlCo2.setSize(jlCo2.getIcon().getIconWidth(), jlCo2.getIcon().getIconHeight());
		jlCo2.setLocation(250, 60);
		jlCo2Value = new JLabel("?", JLabel.CENTER);
		jlCo2Value.setLocation(265, 133);
		jlCo2Value.setFont(font);
		jlCo2Value.setSize(70, 50);
		
		jlIlluminance = new JLabel(new ImageIcon(".\\figure\\Illuminance (1).png"));
		jlIlluminance.setSize(jlIlluminance.getIcon().getIconWidth(), jlIlluminance.getIcon().getIconHeight());
		jlIlluminance.setLocation(390, 110);
		jlIlluminanceValue = new JLabel("?", JLabel.CENTER);
		jlIlluminanceValue.setLocation(380, 160);
		jlIlluminanceValue.setFont(font);
		jlIlluminanceValue.setSize(80, 50);
		
		jlGas = new JLabel(new ImageIcon(".\\figure\\Gas.png"));
		jlGas.setSize(jlGas.getIcon().getIconWidth(), jlGas.getIcon().getIconHeight());
		jlGas.setLocation(480, 190);
		jlGasValue = new JLabel("?", JLabel.CENTER);
		jlGasValue.setLocation(480, 228);
		jlGasValue.setFont(font);
		jlGasValue.setSize(50, 50);
		
		Font fontTime = new Font("Kristen ITC", Font.PLAIN, 18);
		jlTimeValue = new JLabel("?", JLabel.CENTER);
		jlTimeValue.setLocation(275, 520);
		jlTimeValue.setFont(fontTime);
		jlTimeValue.setSize(50, 50);
		
		// create State Table
//		String[] value = { "Time", "Temperature", "Humidity", "Co2", "Illuminance", "Gas" };
//		JPanel jpGrid = createStateTable(value);
//
//		ct.add(jpGrid);
		jpSouth.add(jbFan);
		jpSouth.add(jbAlarm);
		jpSouth.add(jbLed);
		
		ct.add(jlTemperature);
		ct.add(jlHumidity);
		ct.add(jlCo2);
		ct.add(jlIlluminance);
		ct.add(jlGas);
		ct.add(jlTemperatureValue);
		ct.add(jlHumidityValue);
		ct.add(jlCo2Value);
		ct.add(jlIlluminanceValue);
		ct.add(jlGasValue);
		ct.add(jlTimeValue);
		ct.add(jbRestart);
		ct.add(jbPause);
		ct.add(jbBack);
		ct.add(jpSouth);
		
		ct.add(jlPot); // 무 조 건 맨 마 지 막 에

		connection.write(PacketUnit.STATE_SENSORBOARD.getPacketStr()); // 센서보드 상태체크
		stateCallback();

		setTitle("Ireland Controller");
		setSize(600, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void connect() {
		try {
			connection = Serial.getInstance();
			connection.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setButton(JButton jb, int x, int y) {
		jb.setSize(jb.getIcon().getIconWidth(), jb.getIcon().getIconHeight());
		//jb.setSize(200, 30);
		jb.setLocation(x, y);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
		jb.setFocusPainted(false);
		jb.addActionListener(this);
	}
	
	private JPanel createStateTable(String[] value) {
		JPanel jpGrid = new JPanel();
		jpGrid.setLayout(new GridLayout(6, 2));
		jpGrid.setLocation(150, 70);
		jpGrid.setSize(300, 380);
		jpGrid.setBackground(new Color(0, 0, 0, 0));

		jlValue = new JLabel[6];
		JLabel[] jlTitle = new JLabel[6];
		
		Font font = new Font("맑은고딕", Font.PLAIN, 20);
		for (int i = 0; i < value.length; i++) {
			jlTitle[i] = new JLabel(value[i], JLabel.CENTER);
			jlTitle[i].setFont(font);
			//jlTitle[i].setForeground(Color.white);
			jpGrid.add(jlTitle[i]);

			jlValue[i] = new JLabel("?", JLabel.CENTER);
			jlValue[i].setFont(font);
			//jlValue[i].setForeground(Color.white);
			jpGrid.add(jlValue[i]);
		}
		
		return jpGrid;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(jbLed)) {
			ct.remove(jpSouth);
			JPanel jpnLedControl = LedPanel.getInstance(connection);
			beforePanel = jpnLedControl;
			ct.add(jpnLedControl);
			ct.repaint();
			Thread ledStateCheck = new Thread((Runnable) jpnLedControl);
			ledStateCheck.start();
			beforeThread = ledStateCheck;
		}
		else if(e.getSource().equals(jbAlarm)) {
			ct.remove(jpSouth);
			JPanel jpnAlarmSet = AlarmPanel.getInstance(connection);
			beforePanel = jpnAlarmSet;
			ct.add(jpnAlarmSet);
			ct.repaint();
			Thread alarmStateCheck = new Thread((Runnable) jpnAlarmSet);
			alarmStateCheck.start();
			beforeThread = alarmStateCheck;
		}
		else if(e.getSource().equals(jbFan)) {
			ct.remove(jpSouth);
			JPanel jpnFanControl = FanPanel.getInstance(connection);
			beforePanel = jpnFanControl;
			ct.add(jpnFanControl);
			ct.repaint();
			Thread fanStateCheck = new Thread((Runnable) jpnFanControl);
			fanStateCheck.start();
			beforeThread = fanStateCheck;
		}
		else if(e.getSource().equals(jbBack)) {
			beforeThread.interrupt();
			ct.remove(beforePanel);
			ct.add(jpSouth);
			ct.repaint();
		}
		else if(e.getSource().equals(jbPause)) {
			beforeThread.interrupt();
		}
		else if(e.getSource().equals(jbRestart)) {
			beforeThread = new Thread((Runnable) beforePanel);
			beforeThread.start();
		}
	}
	
	private void stateCallback() {
		PacketData.setStateCallback(new StateCallback() {

			@Override
			public void set_state_value(String time, String temp, String humi, String co2, String illum, String gas) {
				jlTimeValue.setText(time);
				jlTemperatureValue.setText(temp);
				jlHumidityValue.setText(humi);
				jlCo2Value.setText(co2);
				jlIlluminanceValue.setText(illum);
				jlGasValue.setText(gas);

				repaint();
				
				isReceived = true;

//				if (!first_create_thread) {
//					sensor_data.start(); // 1분마다 DB에 센서보드 데이터 저장
//					first_create_thread = true;
//				}
			}
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
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
			
			connection.write(PacketUnit.STATE_SENSORBOARD.getPacketStr()); // 센서보드 상태체크
			isReceived = false;
		}
	}
}
