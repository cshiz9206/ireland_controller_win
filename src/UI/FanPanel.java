package UI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Callback.LedFanCallback;
import Callback.StateCallback;
import Connection.Serial;
import Data.PacketData;
import Data.PacketUnit;

public class FanPanel extends JPanel implements ActionListener, Runnable {
	
	private static FanPanel FFInstance;
	public static FanPanel getInstance(Serial connectionInst) { 
		connection = connectionInst;
		if(FFInstance == null) { 
			FFInstance = new FanPanel(); 
		}
		return FFInstance;
	}
	
	// for connection
	private static Serial connection;
	boolean isReceived1 = false;
	boolean isReceived2 = false;
	
	// for UI
//	Container ct;
	JButton jbSet, jbFanOn, jbFanOff;
	JTextField jtfFanPower;
	JLabel jlFanState, jlFanPower;
	
	private FanPanel() {
//		ct = getContentPane();
//		ct.setLayout(null);
//		ct.setBackground(Color.white);
		
		setLayout(null);
		setBackground(new Color(170, 200, 170));
		setSize(600, 150);
		setLocation(0, 620);
		
		// create Label
		Font font = new Font("Kristen ITC", Font.PLAIN, 14);
		jlFanState = new JLabel("?", JLabel.CENTER);
		setLabel(jlFanState, 35, 55);
		jlFanState.setFont(font);
		
		JLabel jlFan = new JLabel("Fan");
		setLabel(jlFan, 135, 55);
		
		JLabel jlFanPower = new JLabel("Power : ");
		setLabel(jlFanPower, 165, 55);
		
		// create TextField
		jtfFanPower = new JTextField();
		setTextField(jtfFanPower, 225, 55);
		
		// create Button
		jbSet = new JButton("set");
		setButton(jbSet, 325, 55);
		
		jbFanOn = new JButton("On");
		setButton(jbFanOn, 415, 55);
		jbFanOff = new JButton("Off");
		setButton(jbFanOff, 475, 55);
		

		add(jlFanState);
		add(jlFan);
		add(jlFanPower);
		add(jtfFanPower);
		add(jbSet);
		add(jbFanOn);
		add(jbFanOff);
		
//		connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 센서보드 상태체크
		stateCallback();
		
//		connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 상태체크
//		connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.FAN.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());

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
			isReceived1 = false;
			isReceived2 = false;
			
			connection.write(PacketUnit.STATE_LEDNFAN_ONOFF_STX_CMD.getPacketStr() + PacketUnit.FAN.getPacketStr() + PacketUnit.STATE_LEDNFAN_ONOFF_TAIL.getPacketStr());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			
			connection.write(PacketUnit.STATE_LEDNFAN.getPacketStr()); // 상태체크
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				break;
			}
			
			if(isReceived1 && isReceived2) {
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
		if(e.getSource().equals(jbFanOn)) {
			connection.write(PacketUnit.FAN_ON.getPacketStr());
			jbFanOff.setBackground(new Color(240, 240, 240));
			jbFanOn.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbFanOff)) {
			connection.write(PacketUnit.FAN_OFF.getPacketStr());
			jbFanOn.setBackground(new Color(240, 240, 240));
			jbFanOff.setBackground(Color.GREEN);
		}
		else if(e.getSource().equals(jbSet)) {
			connection.write(PacketUnit.FAN_SET_STX_DUTY_FIXED.getPacketStr() + String.format("%02d", Integer.parseInt(jtfFanPower.getText())) + PacketUnit.FAN_SET_TAIL.getPacketStr());
		}
	}
	
	private void stateCallback() {
		PacketData.setLedFanCallback(new LedFanCallback() {

			@Override
			public void set_power(String red, String blue, String fan) {
				// TODO Auto-generated method stub
				jlFanState.setText(fan + "%");
				repaint();
				isReceived1 = true;
			}

			@Override
			public void set_switch(boolean redOn, boolean blueOn, boolean fanOn) {
				// TODO Auto-generated method stub
				if(fanOn) {
					jbFanOn.setBackground(Color.GREEN);
					jbFanOff.setBackground(new Color(240, 240, 240));
				}
				else {
					jbFanOff.setBackground(Color.GREEN);
					jbFanOn.setBackground(new Color(240, 240, 240));
				}
				repaint();
				isReceived2 = true;
			}
		});
	}
}
