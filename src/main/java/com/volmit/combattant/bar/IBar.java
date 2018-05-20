package com.volmit.combattant.bar;

public interface IBar
{
	public void setProgress(double progress);

	public void setProgress(double v, double of);

	public void setProgress(int v, int of);

	public void stop();

	public void update();

	public double get();
}
