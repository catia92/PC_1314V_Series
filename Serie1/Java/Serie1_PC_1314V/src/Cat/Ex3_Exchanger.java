package Cat;

import java.util.LinkedList;

import Utils.SyncUtils;

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
	
	// Classe auxiliar & opcional
	private class Message<K>{
		protected K msg;
		protected boolean paired; // Indica 
		
		public Message(K m){
			msg = m;
			paired = false;
		}
	}
	
	private LinkedList<Message<T>> messages;
	
	// � chamado pelas threads para oferecer uma mensagem (myMsg) e receber uma mensagem oferecida (retorno)
	// pela thread com que emparelham
	public synchronized T exchange(T myMsg, long timeout) throws InterruptedException{

		T receivedMsg = null;
		
		// Se j� existe pelo menos 1 msg na lista e a 1� ainda � tem par... emparelha e retorna!
		// � garantida a ordem FIFO pois enquanto a 1� msg n�o tiver par, as restantes tamb�m n�o ter�o.
		if(!messages.isEmpty() && !messages.getFirst().paired){
			receivedMsg = messages.getFirst().msg;
			messages.getFirst().msg = myMsg;
			messages.getFirst().paired = true;
			this.notifyAll(); // Acorda as threads para elas verem se foi a sua msg a ser consumida
			return receivedMsg;
		}
		
		// Sen�o: Adicionar esta mensagem � lista
		Message<T> offeredMsg = new Message<T>(myMsg);
		messages.add(offeredMsg);
		
		long lastTime = System.currentTimeMillis();
		do{
			this.notify();	// Notificar - Para que haja thread p/emparelhar
			try{	
				this.wait();	// Esperar
			}catch(InterruptedException iex){
				messages.remove(); // � � necess�rio verificar se j� pertence � lista; pois j� h� certezas disso
				throw iex;
			}
			// Verificar se a msg oferecida para troca j� foi consumida
			if(offeredMsg.paired){
				receivedMsg = offeredMsg.msg;
				messages.remove(offeredMsg);
				break; // Mensagem substitu�da! agora "msg" � a mensagem oferecida pela outra thread! Retornar.
			}
			timeout = SyncUtils.adjustTimeout(lastTime, timeout);
		}while(timeout>0);
		return receivedMsg;
	}
}
