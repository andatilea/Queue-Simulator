	package control;
	import java.util.ArrayList;
	import java.util.List;
	import model.Client;
	import model.Queue;

	public class Scheduler {
	public List<Queue> queues;						//queues list;
	private int nbQueues;							//the number of queues involved;
	private Strategy strategy;						//the strategy chosen for the implementation;

	public Scheduler(int nbQueues, SelectionPolicy policy) {
	this.nbQueues = nbQueues;
	queues = new ArrayList<Queue>();				//initialise the list;
	changeStrategy(policy);							//establishing the strategy;		
	this.startQueue();								//create thread with the object;
	}
	
	public void changeStrategy(SelectionPolicy policy) {
	strategy = new ConcreteStrategyTime();			//concrete strategy corresponding to the policy;
	}
	
	public Queue dispatchClient(Client c) {
	return strategy.addClient(queues, c);			//call the strategy addClient method
	}
	
	public void startQueue() {						//this method will create each queue;
	int i=0;
	for (i = 0; i < nbQueues; i++) {
	Queue queue = new Queue();
	queues.add(queue);
	Thread thread = new Thread(queue);
	queue.setOpen(true);							//setting the functionality to 'open';
	thread.start();
		}
	}
	
	public void getQueueStatus() {
	for(Queue q : this.getServers()) {
	System.out.println(q.toString());				//this method will be responsible with the displaying of each queue (either having client or being closed);
		}
	}
	
	public void stopQueue() {						//the queue will become 'closed';
	for (Queue c : queues)
	c.setOpen(false);
	}
	
	public List<Queue> getServers() {				//getter for receiving the list of queues;
	return this.queues;
		}
	}
