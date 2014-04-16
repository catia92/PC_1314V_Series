package Peter;

import java.util.LinkedList;

/*
Implemente em Java o sincronizador synchronous queue, com base na classe 
SynchronousQueue<T> que define as opera��es T take()e void put(T obj). O sincronizador 
� usado para comunicar atrav�s de objetos do tipo T entre threads produtoras e threads
consumidoras. 

A opera��o put oferece um objeto e bloqueia a thread produtora at� que exista uma 
thread consumidora dispon�vel para o receber.

As threads consumidoras declaram disponibilidade para receber um objeto invocando a opera��o take, 
bloqueando-se at� que o objeto seja recebido por uma thread consumidora. 

O sincronizador implementa disciplina FIFO, ou seja, s�o sempre satisfeitos os pedidos das threads bloqueadas h� mais tempo.
*/

public class Ex2_SynchronousQueue<T> 
{
	private LinkedList<T> producers;
	private LinkedList<Boolean> consumers;
	
	public Ex2_SynchronousQueue(){
		producers = new LinkedList<T>();
		consumers = new LinkedList<Boolean>();
	}
	
	public synchronized T take() throws InterruptedException{
		if(!producers.isEmpty()){
			this.notifyAll();		
			return producers.removeFirst();
		}
		
		Boolean bool = new Boolean(false);
		consumers.addLast(bool);
		do{
			try{
				this.wait();
			}catch(InterruptedException ie){
				consumers.remove(bool);
				throw ie;
			}
			
			if(!producers.isEmpty() && consumers.getFirst() == bool){ // or consumers.getFirst().equals(bool) ?
				consumers.removeFirst();
				this.notifyAll();
				return producers.removeFirst();
			}
		}while(true);
	}
	
	public synchronized void put(T obj) throws InterruptedException{
		producers.addLast(obj);
		
		if(!consumers.isEmpty())
			this.notifyAll();
						
		do{
			try{
				this.wait();
			}catch(InterruptedException ie){
				if(!producers.contains(obj)){ // Se for interrompido e j� n�o estiver na fila, ent�o correu bem e regenero a excep��o
					Thread.currentThread().interrupt();
					return;
				}			
				producers.remove(obj);
				throw ie;
			}
			
			if(!producers.contains(obj))
				return;			
			
		}while(true);
	}
	
}