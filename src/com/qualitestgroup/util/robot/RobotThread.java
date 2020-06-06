package com.qualitestgroup.util.robot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class RobotThread extends Thread {
	
	static boolean run = true;
	
	private static final int TIME = 5000;
	
	@Override
	public void run()
	{
		try {
			Robot robot = new Robot();
			int i = 1;
			while(run)
			{
				Point p = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(p.x+i, p.y+i);
				Thread.sleep(TIME);
				i *= -1;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	public void kill()
	{
		run = false;
	}
}
