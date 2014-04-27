package Peter;

/*
Implemente em Java o sincronizador exchanger Exchanger<T> que permite a troca, entre pares de 
threads, de mensagens definidas por inst�ncias do tipo T.
*/
public class Ex3_Exchanger<T> {
	/*
	A classe que implementa o sincronizador inclui o m�todo T exchange(T myMsg, long timeout), 
	que � chamado pelas threads para oferecer uma mensagem (par�metro myMsg) e receber a mensagem 
	oferecida (valor de retorno) pela thread com que emparelham. Quando a troca de mensagens n�o 
	pode ser realizada de imediato (porque n�o existe ainda  uma thread bloqueada), a thread corrente 
	fica bloqueada at� que seja interrompida, expire o limite de tempo especificado atrav�s do par�metro 
	timeout, ou at� que outra thread invoque o m�todo exchange.
	*/
	
	T messageHolder = null;
	
	public synchronized T exchange(T myMsg, long timeout) throws InterruptedException{
		if(messageHolder != null){
			T retMsg = messageHolder;
			messageHolder = myMsg;
			this.notify();
			return retMsg;
		}
		
		messageHolder = myMsg;
		
		long lasttime = System.currentTimeMillis();
		
		do{
			try{
				this.wait(timeout);
			}catch(InterruptedException ie){
				if(!messageHolder.equals(myMsg)){ /* Fui interrompido, no entanto a minha mensagem j� n�o se encontra no holder, logo retorno sucesso e regenero a interup��o*/
					T retMsg = messageHolder;
					messageHolder = null;
					return retMsg;
				}			
				throw ie;
			}
			
			if(!messageHolder.equals(myMsg)){ /* N�o � necess�rio notificar nenhuma thread, pois neste ponto ninguem est� bloqueado */
				T retMsg = messageHolder;
				messageHolder = null;
				return retMsg;
			}
			
			if(SyncUtils.adjustTimeout(lasttime, timeout) == 0){
				messageHolder = null;
				return null;
			}
			
		}while(true);
		
	}
}