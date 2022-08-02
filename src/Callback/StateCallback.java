package Callback;

public interface StateCallback {
	
	public void set_state_value(String time, String temp, String humi, String co2, String illum, String gas);
}
