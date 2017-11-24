package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GUI {

	public static void main(String[] args) throws IOException {
		Intermediaria intermediaria = new Intermediaria();
		
		BufferedReader br = new BufferedReader(new FileReader("/home/airton/Downloads/Trabalho/entrada04"));
		String linha;
		while((linha = br.readLine()) != null) {
			// PERCORRE AS LINHAS
			if (!linha.equals("timeout")) {
				// SE A LINHA NAO FOR UM TIMEOUT
				if (linha.toLowerCase().indexOf("ack") < 0) {
					// SE A LINHA NAO FOR UM ACK
					intermediaria.adicionaTransacao(linha.charAt(0), linha.charAt(2), linha.substring(4));
				} else {
					// SE A LINHA FOR UM ACK
					String ack = linha.substring(4).toLowerCase();
					intermediaria.removeDepoisDeReceberUmAckEAdicionaNoResultadoFinal(ack);
				}
			} else {
				// SE A LINHA FOR UM TIMEOUT
				intermediaria.processaTransacoes();
			}
		}
		br.close();
		
		// QUANDO TERMINAR DE PERCORRER AS LINHAS
		intermediaria.processaTransacoes();
		
		intermediaria.exibeResultado();
	}

}
