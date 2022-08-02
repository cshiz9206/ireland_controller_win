package UI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Callback.LedFanCallback;
import Callback.StateCallback;
import Connection.Serial;
import Data.PacketData;
import Data.PacketUnit;

public class LedPanel extends JPanel implements ActionListener, Runnable {
	
	private static LedPanel LFInstance;
	public static LedPanel getInstance(Serial connectionInst) { 
		connection = connectionInst;
		if(LFInstance == null) { 
			LFInstance = new LedPanel(); 
		}
		return LFInstance;
	}
	
	// for connection
	private static Serial connection;
	
	// for UI
//	Container ct;
	JButton jbSetR, jbSetB, jbLedOnR, jbLedOffR, jbLedOnB, jbLedOffB;
	JTextField jtfLedLuxR, jtfLedLuxB;
	JLabel jlLedStateR, jlLedStateB;
	
	boolean isReceived1 = false;
	boolean isReceived2 = false;
	
	private LedPanel() {
//		ct = getContentPane();
//		ct.setLayout(null);
//		ct.setBackground(Color.white);
		
		setLayout(null);
		setBackground(new Color(170, 200, 170));
		setSize(600, 150);
		setLocation(0, 620);
		
		// create Label
		Font font = new Font("Kristen ITC", Font.PLAIN, 14);
		jlLedStateR = new JLabel("?", JLabel.CENTER);
		setLabel(jlLedStateR, 10, 30);
		jlLedStateR.setFont(font);
		jlLedStateR.setSize(60, 30);
		jlLedStateB = new JLabel("?", JLabel.CENTER);
		setLabel(jlLedStateB, 10, 80);
		jlLedStateB.setSize(60, 30);
		jlLedStateB.setFont(font);
		
		JLabel jlLedColorR = new JLabel("RED", JLabel.CENTER);
		setLabel(jlLedColorR, 70, 30);
		JLabel jlLedColorB = new JLabel("BLUE&WHITE", JLabel.CENTER);
		setLabel(jlLedColorB, 70, 80);
		
		JLabel jlLedLuxR = new JLabel("Brightness : ", JLabel.CENTER);
		setLabel(jlLedLuxR, 170, 30);
		JLabel jlLedLuxB = new JLabel("Brightness : ", JLabel.CENTER);
		setLabel(jlLedLuxB, 170, 80);
		
		// create TextField
		jtfLedLuxR = new JTextField();
		setTextField(jtfLedLuxR, 260, 30);
		jtfLedLuxB = new JTextField();
		setTextField(jtfLedLuxB, 260, 80);
		
		// create Button
		jbSetR = new JButton("set");
		setButton(jbSetR, 360, 30);
		jbSetR.setHorizontalAlignment(SwingConstants.CENTER);
		jbSetB = new JButton("set");
		setButton(jbSetB, 360, 80);
		jbSetB.setHorizontalAlignment(SwingConstants.CENTER);
		
		jbLedOnR = new JButton("On");
		setButton(jbLedOnR, 440, 30);
		jbLedOnR.setHorizontalAlignment(SwingConstants.CENTER);
		jbLedOffR = new JButton("Off");
		setButton(jbLedOffR, 500, 30);
		jbLedOffR.setHorizontalAlignment(SwingConstants.CENTER);
		
		jbLedOnB = new JButton("On");
		setButton(jbLedOnB, 440, 80);
		jbLedOnB.setHorizontalAlignment(SwingConstants.CENTER);
		jbLedOffB = new JButton("Off");
		setButton(jbLedOffB, 500, 80);
		jbLedOffB.setHorizontalAlignment(SwingConstants.CENTER);
		

		add(jlLedStateR);
		add(jlLedStateB);
		add(jlLedColorR);
		add(jlLedColorB);
		add(jlLedLuxR);
		add(jlLedLuxB);
		add(jtfLedLuxR);
		add(jtfLedLuxB);
		add(jbSetR);
		add(jbSetB);
		add(jbLedOnR);
		add(jbLedOffR);
		add(jbLedOnB);
		add(jbLedOffB);
		
//		connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 상태체크
//		
//		
//		connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.LED_RED.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());
//		connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.LED_BLUEWHITE.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());
		stateCallback();

//		setTitle("Ireland Controller - LED Control");
//		setSize(600, 800);
//		setVisible(true);
//		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLocationRelativeTo(null);
	}
	
	private void setLabel(JLabel jlbObj, int x, int y) {
		Font font = new Font("Kristen ITC", Font.PLAIN, 12); //Bradley Hand ITC
		jlbObj.setLocation(x, y);
		jlbObj.setFont(font);
		jlbObj.setSize(100, 30);
	}
	
	private void setTextField(JTextField tfObj, int x, int y) {
		Font font = new Font("Kristen ITC", Font.PLAIN, 12);
		tfObj.setFont(font);
		tfObj.setLocation(x, y);
		tfObj.setSize(100, 30);
		tfObj.setEditable(true);
		tfObj.setVisible(true);
		tfObj.setForeground(Color.white);
		tfObj.setBackground(new Color(90, 120, 90));
		tfObj.setBorder(null);
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
			connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 센서보드 상태체크
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.LED_RED.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.LED_BLUEWHITE.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());
//			isReceived1 = false;
//			isReceived2 = false;
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			
//			if(isReceived1 && isReceived2) {
//				break;
//			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(jbLedOnR)) {
			connection.write(PacketUnit.LED_ON_RED.getPacketStr());
			jbLedOffR.setBackground(new Color(240, 240, 240));
			jbLedOnR.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbLedOffR)) {
			connection.write(PacketUnit.LED_OFF_RED.getPacketStr());
			jbLedOnR.setBackground(new Color(240, 240, 240));
			jbLedOffR.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbLedOnB)) {
			connection.write(PacketUnit.LED_ON_BLUEWHITE.getPacketStr());
			jbLedOffB.setBackground(new Color(240, 240, 240));
			jbLedOnB.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbLedOffB)) {
			connection.write(PacketUnit.LED_OFF_BLUEWHITE.getPacketStr());
			jbLedOnB.setBackground(new Color(240, 240, 240));
			jbLedOffB.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbSetR)) {
			connection.write(PacketUnit.LED_SET_STX_CMD.getPacketStr() + PacketUnit.LED_RED.getPacketStr() + PacketUnit.LED_SET_PWM_DUTY_FIXED.getPacketStr() + String.format("%02d", Integer.parseInt(jtfLedLuxR.getText())) + PacketUnit.LED_SET_TAIL.getPacketStr());
			//connection.write(PacketUnit.SET_RED_POWER_HEAD.getPacketStr() + String.format("%02d", Integer.parseInt(jtfLedLuxR.getText())) + PacketUnit.SET_POWER_TAIL.getPacketStr());
		}
		else if(e.getSource().equals(jbSetB)) {
			connection.write(PacketUnit.LED_SET_STX_CMD.getPacketStr() + PacketUnit.LED_BLUEWHITE.getPacketStr() + PacketUnit.LED_SET_PWM_DUTY_FIXED.getPacketStr() + String.format("%02d", Integer.parseInt(jtfLedLuxB.getText())) + PacketUnit.LED_SET_TAIL.getPacketStr());
		}
		repaint();
	}
	
	private void stateCallback() {
		PacketData.setLedFanCallback(new LedFanCallback() {

			@Override
			public void set_power(String red, String blue, String fan) {
				// TODO Auto-generated method stub
				jlLedStateR.setText(red + "%");
				jlLedStateB.setText(blue + "%");
				repaint();
				isReceived1 = true;
			}

			@Override
			public void set_switch(boolean redOn, boolean blueOn, boolean fanOn) {
				// TODO Auto-generated method stub
				if(redOn) {
					jbLedOnR.setBackground(Color.GREEN);
					jbLedOffR.setBackground(new Color(240, 240, 240));
				}
				else {
					jbLedOffR.setBackground(Color.GREEN);
					jbLedOnR.setBackground(new Color(240, 240, 240));
				}
				if(blueOn) {
					jbLedOnB.setBackground(Color.GREEN);
					jbLedOffB.setBackground(new Color(240, 240, 240));
				}
				else {
					jbLedOffB.setBackground(Color.GREEN);
					jbLedOnB.setBackground(new Color(240, 240, 240));
				}
				repaint();
				isReceived2 = true;
			}
		});
	}
}
