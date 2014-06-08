package Cat;

import java.util.LinkedList;

public class ConcurrentQueue<T> {
	/*
	 * 	Implemente em Java e C# a classe ConcurrentQueue<T> que define um contentor com disciplina 
		FIFO (First-In-First-Out) suportado numa lista simplesmente ligada. A classe disponibiliza as 
		opera��es put, tryTake e isEmpty. A opera��o put coloca no fim da fila o elemento passado 
		como argumento; a opera��o tryTake retorna o elemento presente no in�cio da fila, ou null caso 
		da file estar vazia; a opera��o isEmpty produz o valor booleano que indica se a fila cont�m 
		elementos. A implementa��o suporta acessos concorrentes e as opera��es disponibilizadas n�o 
		bloqueiam a thread invocante. 
		Nota: Para a implementa��o considere a explica��o sobre a lock-free queue, proposta por Michael e 
		Scott, que consta no Cap�tulo 15 do livro Java Concurrency in Practice. 
	 */

	private LinkedList<T> queue; // FIFO Concurrent Queue
	
	// A opera��o put coloca no fim da fila o elemento passado como argumento;
	public void put(T element){
		
	}
	
	// A opera��o tryTake retorna o elemento presente no in�cio da fila, ou null caso da file estar vazia;
	public T tryTake(){
		return null;
	}
	
	// A opera��o isEmpty produz o valor booleano que indica se a fila cont�m elementos
	public boolean isEmpty(){
		return false;//return queue.size()==0;
	}
}
