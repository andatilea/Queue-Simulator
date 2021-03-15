	package control;
	import java.util.List;
	import model.Client;
	import model.Queue;

	public class ConcreteStrategyTime implements Strategy {		//the concrete Strategy will be represented by adding Clients using the shortest waiting time;
	
	@Override
	public Queue addClient(List<Queue> queues, Client c) {		//override method implementing the Strategy required;
	int i = -1;
	int min = Integer.MAX_VALUE;
	boolean ok = false;
	for (int j = 0; j < queues.size(); j++) {
	int aux = queues.get(j).getWaitingTime();
	if (aux < min) {
	min = aux;
	i = j;
	ok = true;
		}
	}
	if (ok) 
	queues.get(i).addClient(c);									//adding the client in the 	queue list;
	return queues.get(i);	
		}
	}