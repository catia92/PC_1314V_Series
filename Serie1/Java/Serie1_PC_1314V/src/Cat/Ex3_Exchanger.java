package Cat;

import java.util.LinkedList;

public class Ex3_Exchanger<T> {

	/*
	 * 3. 
	 * 	Implemente em Java o sincronizador exchanger Exchanger<T> que permite a troca, entre pares de 
		threads, de mensagens definidas por inst�ncias do tipo T. A classe que implementa o sincronizador 
		inclui o m�todo T exchange(T myMsg, long timeout), que � chamado pelas threads para 
		oferecer uma mensagem (par�metro myMsg) e receber a mensagem oferecida (valor de retorno) pela 
		thread com que emparelham. Quando a troca de mensagens n�o pode ser realizada de imediato 
		(porque n�o existe ainda uma thread bloqueada), a thread corrente fica bloqueada at� que seja 
		interrompida, expire o limite de tempo especificado atrav�s do par�metro timeout, ou at� que outra 
		thread invoque o m�todo exchange. 
	 */
	
	public class Message<T>{
		protected T msg;
		protected boolean consumed;
		
		public Message(T m){
			msg = m;
			consumed = false;
		}
	}
	private LinkedList<Message<T>> messages; // Cada posi��o tem a mensagem e o n�mero da thread
	
	// � chamado pelas threads para oferecer uma mensagem (myMsg) e receber uma mensagem oferecida (retorno)
	// pela thread com que emparelham
	public synchronized T exchange(T myMsg, long timeout) throws InterruptedException{

		T receivedMsg = null;
		
		if(!messages.isEmpty()){
			receivedMsg = messages.getFirst().msg;
			messages.getFirst().msg = myMsg;
			messages.getFirst().consumed = true;
			this.notifyAll();
			return receivedMsg;
		}
		
		Message<T> offeredMsg = new Message<T>(myMsg);
		do{
			if(messages.isEmpty()){ // Adicionar esta mensagem � lista
				messages.add(offeredMsg);
			}
			try{
				this.notify();	// Notificar - Para que haja thread p/emparelhar
				this.wait();
			}catch(InterruptedException iex){
				messages.remove();
				throw iex;
			}
			
			// Wait for another message to be offered for consumption 
			if(offeredMsg.consumed){
				T msg = offeredMsg.msg;
				messages.remove(offeredMsg);
				return msg; // Mensagem substitu�da! agora "msg" � a mensagem oferecida pela outra thread!
			}
			if(timeout==0){
				//timeout= SyncUtils.;
			}
		}while(true);
	}
}
