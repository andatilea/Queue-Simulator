	package control;
	import java.util.List;
	import model.Client;
	import model.Queue;
	

	public interface Strategy {
	
	Queue addClient(List<Queue> queues, Client c);
	
	}