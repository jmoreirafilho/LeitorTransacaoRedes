package main;

import java.util.ArrayList;

import util.Resultado;
import util.Transacao;

public class Intermediaria {
	public ArrayList<Transacao> transacoes = new ArrayList<Transacao>();
	public ArrayList<Resultado> resultadoFinal = new ArrayList<Resultado>();
	public String ultimoAckUtilizado;
	
	public Intermediaria() {
		// TODO Auto-generated constructor stub
	}
	
	public void adicionaTransacao(char coluna, char acao, String pacote) {
		Transacao transacao = new Transacao();
		transacao.setAcao(acao);
		transacao.setColuna(coluna);
		transacao.setPacote(pacote);
		
		// B so pode receber um pacote se A tiver enviado esse mesmo pacote
		if (coluna == 'B' && acao == 'R') {
			if (this.existeAlgumaTransacaoOndeAEnviaEssePacote(pacote)) {
				this.transacoes.add(transacao);
			}
		} else {
			this.transacoes.add(transacao);
		}
		
	}

	private boolean existeAlgumaTransacaoOndeAEnviaEssePacote(String pacote) {
		for (Transacao transacao : this.transacoes) {
			if (transacao.getColuna() == 'A' && transacao.getAcao() == 'E' && transacao.getPacote().equals(pacote)) {
				return true;
			}
		}
		return false;
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
				
				// Se vier da coluna A
				if (transacao.getColuna() == 'A') {
					// Adiciona no resultadoFinal
					this.adicionaResultadoFinal(ack, transacao.getPacote());
				}
				
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
			// Se A enviou, deve ser descartado
			if (transacao.getColuna() == 'A' && transacao.getAcao() == 'E') {
				this.descartaDoResultadoFinal(transacao);
			}
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

	public void exibeResultado() {
		for (Resultado resultado : this.resultadoFinal) {
			System.out.println(resultado.resultadoMontado());
		}
	}
	
	
}
