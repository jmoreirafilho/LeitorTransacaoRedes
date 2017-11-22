package main;

public class GUI {

	public static void main(String[] args) {
		Intermediaria intermediaria = new Intermediaria();
		
		// PERCORRE AS LINHAS
			// SE A LINHA NAO FOR UM TIMEOUT
				// SE A LINHA NAO FOR UM ACK
					intermediaria.adicionaTransacao('A', 'E', "pkt0");
				// SE A LINHA FOR UM ACK
					intermediaria.removeDepoisDeReceberUmAckEAdicionaNoResultadoFinal("ack0");
			// SE A LINHA FOR UM TIMEOUT
				intermediaria.processaTransacoes();
		
		// QUANDO TERMINAR DE PERCORRER AS LINHAS
		intermediaria.processaTransacoes();
	}

}
