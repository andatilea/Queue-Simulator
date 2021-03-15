package start;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import control.SimulationManager;

public class Main {
	
	public static void main(String[] args) {
		
	new SimulationManager(args[0]);
	String outTextFile = args[1];
	System.out.println("Taking data from file: " + args[0]);
	try {
	PrintStream console = new PrintStream(new File(outTextFile));
	System.setOut(console);
	}catch(FileNotFoundException e) {
	e.printStackTrace();
	}
		}
	}