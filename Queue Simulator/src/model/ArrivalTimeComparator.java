	package model;
	import java.util.Comparator;
	
	
	public class ArrivalTimeComparator implements Comparator<Client> {
		
	public int compare(Client o1, Client o2) {
	return (o1.getArrivalTime() - o2.getArrivalTime());			//ordering the Clients in an ascending order, using their generated arrival times;
		}
	}