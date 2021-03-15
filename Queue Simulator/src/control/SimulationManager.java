	package control;
	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Iterator;
	import java.util.List;
	import java.util.Random;
	import model.ArrivalTimeComparator;
	import model.Client;
	import model.Queue;

	public class SimulationManager implements Runnable {
	//declaring all the input data 'stored' in the input text file;
	public int nbClients = 0;
	public int nbQueues = 0;
	public static int simulationInterval = 0;
	public static int currentTime = 0;
	public int minArrivalTime = 0;
	public int maxArrivalTime = 0;
	public int minServiceTime = 0;
	public int maxServiceTime = 0;
	//setting the SHORTEST_TIME strategy;
	public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
	
	//entity responsible with queue management and client distribution;
	public Scheduler scheduler;
	
	//clients shopping in the store;
	public List<Client> Clients = new ArrayList<Client>();
	
	
	//the constructor which will assure loading of the input data from the input path declared in the Command;
	public SimulationManager(String fileName) {	
	loadTextFile(fileName);
	}
	
	public void generateRandomClient() {
	Random random = new Random();
	int arrivalTime;
	int serviceTime;
	for (int i = 0; i < nbClients; i++) {
	arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;		//generating random numbers between the given interval;
	serviceTime = random.nextInt(maxServiceTime - minServiceTime) + minServiceTime;
	Client client = new Client(i, arrivalTime, serviceTime);
	Clients.add(client);																//adding each client generated in the Clients list;
	}
	Collections.sort(Clients, new ArrivalTimeComparator());								//all clients are sorted in the ascending order of their arrival time, using the Comparator implemented;
	}
		
	public void loadTextFile(String fileName) {											//this method is responsible for reading line by line from the input file;
	try {
	BufferedReader rd = new BufferedReader(new FileReader(fileName));
	this.nbClients = Integer.parseInt(rd.readLine());
	this.nbQueues = Integer.parseInt(rd.readLine());
	SimulationManager.simulationInterval = Integer.parseInt(rd.readLine());
	String[] arrivalTimes = rd.readLine().split(",");
	this.minArrivalTime = Integer.parseInt(arrivalTimes[0]);
	this.maxArrivalTime = Integer.parseInt(arrivalTimes[1]);
	String[] serviceTimes = rd.readLine().split(",");
	this.minServiceTime = Integer.parseInt(serviceTimes[0]);
	this.maxServiceTime = Integer.parseInt(serviceTimes[1]);
	rd.close();
	this.startSimulation();																//with the extracted data, the simulation will begin;
	}catch(Exception e) {}
	}
	
	public void startSimulation() throws Exception {
	generateRandomClient();																//the method calls the random Client generator;
	currentTime = 0;
	this.scheduler = new Scheduler(nbQueues, selectionPolicy);							//the Scheduler will be created using the extracted number of queues and the SHORTEST_TIME strategy;
	if (simulationInterval <= 0 || minArrivalTime <= 0 || maxArrivalTime <= 0 || nbQueues <= 0 || nbClients <= 0 || minServiceTime <= 0 || maxServiceTime <= 0)
	throw new Exception();																//all the wrong input-data cases are declared and an Exception will be thrown if any of them occurs;
	if (minArrivalTime > maxArrivalTime || minServiceTime > maxServiceTime)
	throw new Exception();
	Thread t = new Thread(this);
	t.start();																			//starting the thread;
	}
	
	public synchronized void waitQueue() {
	for (Queue q : this.scheduler.getServers())											//creating a for loop for each queue;
	if (!(q.getQueue().isEmpty()))
	for (Client cl : q.getQueue())
	if (cl.getFinalTime() == currentTime) {												//checking the client's final time;
	try {
	wait(1000);
	}catch (InterruptedException e) {
	e.printStackTrace();
		}
	}
	notifyAll();																		//synchronising with all the threads;
	}
	
	private void timePassed() {															//this method will update the simulation each second;
	if (currentTime < simulationInterval) {
	System.out.println("\nTime " + currentTime);										//the current time at each simulation's second will be displayed;
	}
	System.out.println("Waiting Clients:");												//the waiting Clients list is displayed;
	for (Iterator<Client> iterator = Clients.iterator(); iterator.hasNext();) {
	Client c = iterator.next();
	if (c.getArrivalTime() == currentTime) {											//in the moment in which the arrival time of a client equals the simulation's current time, it will be removed from the waiting list;
	scheduler.dispatchClient(c);														//the client will be added in the queue;	
	iterator.remove();													
	continue;
	}
	System.out.println(c.toString());													//print the Client String for each one of them;	
		}
	}
		
	@Override
	public void run() {
	while (currentTime < simulationInterval) {											//as long as the simulation takes place we will call the timePassed function;
	timePassed();
	this.waitQueue();											
	scheduler.getQueueStatus();															//the status for each queue will be displayed in the simulation;
	currentTime++;																		//the current time will be incremented until it reaches the simulation intveral declared;
	try{																				//wait an interval of 1 second;
	Thread.sleep(1000);
	}catch (InterruptedException e) {
	e.printStackTrace();
		}
	}
	scheduler.stopQueue();																//when the simulation is over the queue will be stopped;
	
	float totalTime = 0;
	float averageTime = 0;
	for (Queue q : this.scheduler.getServers()) {
	totalTime += q.getWaitingTime();													//the waiting time will be computed by diving it at the declared number of clients; 
	averageTime = totalTime / this.nbClients;				
	}
	System.out.println("Total Average Time: " + (averageTime));
		}
	}