package Cat;

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

	public T exchange(T myMsg, long timeout){
		
		return null;	
	}
	
	
}
