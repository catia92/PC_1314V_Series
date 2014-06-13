package Cat;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentQueue<T> {
	
	private class CNode<K> {
		final K value;
		final AtomicReference<CNode<K>> nextNode;

		public CNode(K val, CNode<K> next) {
			value = val;
			nextNode = new AtomicReference<CNode<K>>(next);
		}
	}
	
	private CNode<T>dummy = new CNode<T>(null, null);
	private AtomicReference<CNode<T>> queueHead= new AtomicReference<>(dummy); // Aponta para a cabe�a da queue
	private AtomicReference<CNode<T>> queueTail= new AtomicReference<>(dummy);
	
	// A opera��o put coloca no fim da fila o elemento passado como argumento;
	public void put(T element){
		
		CNode<T> newNode = new CNode<T>(element, null);
		
		// Tentar adicionar o novo node � queue...
		do{
			CNode<T> currTail = queueTail.get();
			CNode<T> next = currTail.nextNode.get();
			
			// Se a informa��o da tail � consistente
			if(queueTail.get() == currTail){
				if(next == null){ // Se a tail � realmente o �ltimo node
					// Tentar adicionar o novo node no fim da queue
					if(queueTail.get().nextNode.compareAndSet(null, newNode)){
						queueTail.compareAndSet(currTail, newNode);
						break; //Inser��o feita com sucesso
					}
				}else{
					// A tail n�o estava a apontar para o ultimo node
					// tentar mudar a referencia para o pr�ximo node
					queueTail.compareAndSet(currTail, next);
				}
			}
		}while(true);
	}
	
	// A opera��o tryTake retorna o elemento presente no in�cio da fila, ou null caso da file estar vazia;
	public T tryTake(){
		do{
			if(isEmpty()) // Retorna null caso a fila esteja vazia
				return null;
			
			CNode<T>headPtr = queueHead.get();
			CNode<T>currHead = headPtr.nextNode.get();//headPtr.nextNode.get(); 
			CNode<T>nextHead = currHead.nextNode.get();
			
			// Actualiza a head, se a informa��o lida estiver consistente, sen�o tenta novamente
			if(queueHead.compareAndSet(queueHead.get(), nextHead)){
				return currHead.value;
			}
		}while(true);
	}
	
	// A opera��o isEmpty produz o valor booleano que indica se a fila cont�m elementos
	public boolean isEmpty(){
		return queueHead.get().nextNode.get()==null;
	}
}