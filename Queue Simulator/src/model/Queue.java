	package model;
	import java.util.concurrent.BlockingQueue;
	import java.util.concurrent.LinkedBlockingQueue;
	import java.util.concurrent.atomic.AtomicInteger;
	import control.SimulationManager;


	public class Queue implements Runnable {
	
	private LinkedBlockingQueue<Client> clients;		//will store the clients added;
	private AtomicInteger waitingPeriod;				//the waiting time in a queue;
	private static int idQueue = 0;
	private int id = 0;									//an unique identifier for each queue(starting from 1);
	private boolean open;								//determining if the queue is functional;
	
	
	public Queue() {									//constructor which will create a new queue with the given values, setting its id;
	idQueue++;
	this.id = idQueue;
	this.clients = new LinkedBlockingQueue<Client>();	//initialise the queue;
	waitingPeriod = new AtomicInteger(0);				//initialise the waiting period;
	open = true;
	}
	
	public void addClient(Client c) {
	c.setFinalTime();									//calculate its final time;
	clients.add(c);										//adding a specific client;
	waitingPeriod.addAndGet(c.getServiceTime());		//getting its specific waiting period;
	}
	
	public synchronized void removeClient() {
	Client c = null;
	if (!this.clients.isEmpty()) {						//the application makes sure that it is not empty;
	try {
	c = clients.peek();									//the client c will be the head of the queue;
	while (c.getFinalTime() != SimulationManager.currentTime) {
	wait(1000);											//if it is not the right time to leave, then a waiting time is set;
	}
	}catch (InterruptedException e) {
	e.printStackTrace();
	}
	if (c.getFinalTime() == SimulationManager.currentTime) {
	this.clients.remove();								//if the time is right, remove the client;
	}
	notifyAll();										//synchronising between all threads;
		}
	}
	
	@Override
	public void run() {									//override method, due to the Runnable implementation;
	while (open) {
	while(!(clients.isEmpty())) {
	Client c;
	c = clients.peek();
	if(c!= null) {
	int serviceTime = c.getServiceTime();
	if(c.getServiceTime() <= 0) {
		clients.remove();
	}
	else {
	try {
	Thread.sleep(serviceTime*1000);						//wait the specific time established for each client;
	}catch(InterruptedException e) {
	e.printStackTrace();
		}
	}
	this.removeClient();								//after it passed, call the method created for the removal of clients;
				}	
			}
		}
	}
	
	public BlockingQueue<Client> getQueue() {
	return this.clients;								//getter for the current queue;
	}

	public void setOpen(boolean open) {
	this.open = open;									//setter for the 'open' queue;
	}
	
	public String toString() {							//this method returns the string associated with each queue at every second of the simulation;
	String s="Queue " + this.id +":";
	if(!this.clients.isEmpty()) {
	for(Client c: this.clients) { 
	if(c.getServiceTime() <=0) {						//condition to make sure that no Client stays longer that the given Service Time;					
	clients.remove();
	}
	else if(c == clients.peek()) {
	s += c.toString(true) + ";";						//for the Client in the front of the queue, the service time will be decremented, the Client String will be called and displayed;
	}else if (!(c==clients.peek())){
	s+= c.toString() + ";";								//the other Clients will enter the queue, but the Service Time will be decremented only in the moment in which they arrive at the "cashier";
	}}
	}else	s+= "closed";								//if there are no clients in a queue, it will be, by default, 'closed';
	return s;		
	}
	
	public int getWaitingTime() {	
	return this.waitingPeriod.get();					//getter for obtaining the waiting period in order to calculate the Average Time;
		}
	}