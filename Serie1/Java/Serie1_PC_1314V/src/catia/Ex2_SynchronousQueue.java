package catia;
import java.util.LinkedList;

/*
 * 2. 
 * 	Implemente em Java o sincronizador synchronous queue, com base na classe 
	SynchronousQueue<T> que define as opera��es T take() e void put(T obj). 
	
	O sincronizador 
	� usado para comunicar atrav�s de objetos do tipo T entre threads produtoras e threads 
	consumidoras. 
	
	A opera��o put oferece um objeto e bloqueia a thread produtora at� que exista uma 
	thread consumidora dispon�vel para o receber. 
	
	As threads consumidoras declaram disponibilidade 
	para receber um objeto invocando a opera��o take, bloqueando-se at� que o objeto seja recebido por 
	uma thread consumidora. 
	
	O sincronizador implementa disciplina FIFO, ou seja, s�o sempre 
	satisfeitos os pedidos das threads bloqueadas h� mais tempo. 

 */
public class Ex2_SynchronousQueue <T> {

	// FIFO Queues:
	private LinkedList<Long> consumerThreads;
	private LinkedList<T> objectsProduced;
	
	public Ex2_SynchronousQueue(){
		objectsProduced = new LinkedList<T>();
		consumerThreads = new LinkedList<Long>();
	}
	
	// As threads consumidoras declaram disponibilidade 
	// para receber um objeto invocando a opera��o take, bloqueando-se at� que o objeto seja recebido por 
	// uma thread consumidora.
	public synchronized T take() throws InterruptedException{
		T takenObject = null;
		long tId = Thread.currentThread().getId();
		consumerThreads.addLast(tId);
		
		if(!objectsProduced.isEmpty() && consumerThreads.getFirst().equals(tId)){	
			takenObject = objectsProduced.removeFirst();
			consumerThreads.removeFirst();
			notifyAll();
			return takenObject;
		}
		
		do{
			if(objectsProduced.isEmpty()){

				notifyAll();
				try {
					wait();
				} catch (InterruptedException e) {
					consumerThreads.remove(tId);
					throw e;
				}
			}			
			if(!objectsProduced.isEmpty()){
				if(consumerThreads.getFirst().equals(tId))
				{
					consumerThreads.removeFirst();
					takenObject = objectsProduced.removeFirst();
					notifyAll();
					break;
				}
			}
		}while(true);
		
		return takenObject;
	}
	
	// A opera��o put oferece um objeto e bloqueia a thread produtora at� que exista uma 
	// thread consumidora dispon�vel para o receber.
	
	public synchronized void put(T obj) throws InterruptedException{
		objectsProduced.addLast(obj);
		do{
			if(consumerThreads.isEmpty()){
				try{
					wait();
				}catch(InterruptedException iex){
					if(objectsProduced.contains(obj))
						objectsProduced.remove(obj);
					throw iex;
				}
			}
			if(!consumerThreads.isEmpty()){
				if(objectsProduced.getFirst().equals(obj)){
					break;
				}
			}
		}while(true);
		notifyAll();
	}

	public synchronized int getNumberOfElements() {
		return objectsProduced.size();
	}
}
