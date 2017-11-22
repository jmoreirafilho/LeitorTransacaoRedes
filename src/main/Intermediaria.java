package main;

import java.util.ArrayList;

import util.Resultado;
import util.Transacao;

public class Intermediaria {
	public ArrayList<Transacao> transacoes;
	public ArrayList<Resultado> resultadoFinal;
	public String ultimoAckUtilizado;
	
	public Intermediaria() {
		// TODO Auto-generated constructor stub
	}
	
	public void adicionaTransacao(char coluna, char acao, String pacote) {
		Transacao transacao = new Transacao();
		transacao.setAcao(acao);
		transacao.setColuna(coluna);
		transacao.setPacote(pacote);
		
		this.transacoes.add(transacao);
	}

	public void adicionaResultadoFinal(String ack, String pacote) {
		Resultado resultado = new Resultado();
		resultado.setAck(ack);
		resultado.setPacote(pacote);
		
		if (resultado.jaExisteEssaCombinacaoNoResultado(this.resultadoFinal, ack, pacote)) {
			resultado.setTipo("descarta");
		} else {
			resultado.setTipo("entrega");
		}
		
		this.ultimoAckUtilizado = ack;
		this.resultadoFinal.add(resultado);
	}

	public void removeDepoisDeReceberUmAckEAdicionaNoResultadoFinal(String ack) {
		// Adotar for para poder remover o elemento com maior facilidade
		for (int i = 0; i < this.transacoes.size(); i++) {
			Transacao transacao = this.transacoes.get(i);
			
			if (transacao.getAckCorrespondente().equals(ack)) {
				// Se o ack for dessa transação
				
				// Adiciona no resultadoFinal
				this.adicionaResultadoFinal(ack, transacao.getPacote());
				
				// Remove das transacoes
				this.transacoes.remove(i);
				
				// Volta indice para continuar percorrendo
				i--;
			}
		}
		
	}

	public void processaTransacoes() {
		// Verifica se tem alguma transacao pendente
		if (this.transacoes.size() > 0) {
			// Se os pacotes enviados por A NAO forem iguais
			if (!this.osPacotesEnviadosPorASaoIguais()) {
				this.removeTodasAsTransacoesPendentes();
			}
		}
	}

	private void removeTodasAsTransacoesPendentes() {
		for (int i = 0; i < this.transacoes.size(); i++) {
			Transacao transacao = this.transacoes.get(i);
			this.descartaDoResultadoFinal(transacao);
			this.transacoes.remove(i);
			i--;
		}
	}

	private void descartaDoResultadoFinal(Transacao transacao) {
		Resultado resultado = new Resultado();
		resultado.setAck(this.ultimoAckUtilizado);
		resultado.setPacote(transacao.pacote);
		resultado.setTipo("descarta");
		
		this.resultadoFinal.add(resultado);
	}

	private boolean osPacotesEnviadosPorASaoIguais() {
		String ultimoPacoteEnviadoPorA = "none";
		for (Transacao transacao : this.transacoes) {
			// Se for um pacote Enviado por A
			if (transacao.getColuna() == 'A' && transacao.getAcao() == 'E') {
				// Se ja tiver sido setado algum pacote na variavel
				if (!ultimoPacoteEnviadoPorA.equals("none")) {
					// Se o ultimo pacote for DIFERENTE do atual
					if (!ultimoPacoteEnviadoPorA.equals(transacao.getPacote())) {
						return false;
					}
				}
				
				// Redefine o ultimo pacote para o ser o pacote atual
				ultimoPacoteEnviadoPorA = transacao.getPacote();
			}
		}
		return true;
	}
	
	
}
