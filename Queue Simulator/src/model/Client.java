	package model;


public class Client{
		
	private int id = 0;							//it is unique from client to client;
	private static int idClient = 0;
	private int arrivalTime;					//the time when the client “finished shopping”;
	private int serviceTime;					//the time needed for the cashier to serve the client;
	private int waitTime;						//the remaining waiting time for each client;
	private int finalTime;						//the moment in which the client will be ‘extracted’ from the queue;
	
	
	//it creates a new client with the given integer parameters, also initialising the times;
	public Client(int id, int arrivalTime, int serviceTime) {
	idClient++;
	this.id = idClient;
	this.arrivalTime = arrivalTime;
	this.serviceTime = serviceTime;
	this.finalTime = 0;	
	this.waitTime = 0;		
	}
	
	public int getArrivalTime() {					//return the value of the arriving time;
	return this.arrivalTime;						//client will enter queue when the time reaches this value;
	}
	
	public int getFinalTime() {						//return the value of the final time;
	return this.finalTime;							//client leaves the queue when the time reaches this value;
	}
		
	public int getServiceTime() {					//return the value of the service time;
	return this.serviceTime;						//client stays in queue as long as this period of time;
	}
	
	public void setFinalTime() {					//setter for the value of the final time, using a formula;
	this.finalTime = arrivalTime + serviceTime;
	}
	
	public String toString() {						//displaying the list of Waiting Clients at each step;
	return "(" + this.id + ", " + this.arrivalTime + " , " + this.serviceTime  + ")";
	}
	
	public String toString(boolean fromQueue) { 	//displaying the clients from the queues, along with their time evolution;		
	this.waitTime = this.serviceTime--;
	String  s = ("( " + id + " , " + arrivalTime + " , " + this.waitTime + ")");
	return s;
		}
	}