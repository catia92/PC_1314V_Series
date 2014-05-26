package Peter;

import java.util.LinkedList;

/*
Implemente em Java o sincronizador exchanger Exchanger<T> que permite a troca, entre pares de 
threads, de mensagens definidas por inst�ncias do tipo T.
*/
public class Ex3_Exchanger<T> {
	
	private class MessageHolder<R>{
		public R msg = null;
		public boolean ready;
		
		private MessageHolder(R msg){
			this.msg = msg;
			ready = false;
		}
	}
	/*
	A classe que implementa o sincronizador inclui o m�todo T exchange(T myMsg, long timeout), 
	que � chamado pelas threads para oferecer uma mensagem (par�metro myMsg) e receber a mensagem 
	oferecida (valor de retorno) pela thread com que emparelham. Quando a troca de mensagens n�o 
	pode ser realizada de imediato (porque n�o existe ainda  uma thread bloqueada), a thread corrente 
	fica bloqueada at� que seja interrompida, expire o limite de tempo especificado atrav�s do par�metro 
	timeout, ou at� que outra thread invoque o m�todo exchange.
	*/
	
	LinkedList<MessageHolder<T>> messagesHolder = new LinkedList<MessageHolder<T>>(); // Inser��o � cauda, verifica��o � cabe�a
	
	public synchronized T exchange(T myMsg, long timeout) throws InterruptedException{
		if(messagesHolder.getFirst() == null || !messagesHolder.getFirst().ready){
			T retMsg = messagesHolder.getFirst().msg;
			messagesHolder.getFirst().ready = true;
			messagesHolder.getFirst().msg = myMsg;
			messagesHolder.removeFirst();
			this.notify();
			return retMsg;
		}	
		
		MessageHolder<T> myHolder = new MessageHolder<T>(myMsg);
		messagesHolder.addLast(myHolder);
		
		long lasttime = System.currentTimeMillis();
		
		do{
			try{
				this.wait(timeout);
			}catch(InterruptedException ie){
				if(myHolder.ready){ /* Fui interrompido, no entanto a minha mensagem est� no meu n�, logo retorno sucesso e regenero a interup��o */
					Thread.currentThread().interrupt();
					return myHolder.msg;
				}
				messagesHolder.remove(myHolder);
				throw ie;
			}
			
			if(myHolder.ready){ /* N�o � necess�rio notificar nenhuma thread, pois neste ponto ninguem est� bloqueado */
				T retMsg = myHolder.msg;
				return retMsg;
			}
			
			if(Utils.SyncUtils.adjustTimeout(lasttime, timeout) == 0){
				if(myHolder.ready)
					return myHolder.msg;
				else{
					messagesHolder.remove(myHolder);
					return null;
				}
			}		
		}while(true);	
	}
}