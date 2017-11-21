package main;

import java.util.ArrayList;

import util.Transacao;

public class Intermediaria {
	public ArrayList<Transacao> transacoes;
	
	public Intermediaria(char acao, char coluna, String pacote) {
		Transacao transacao = new Transacao();
		transacao.setAcao(acao);
		transacao.setColuna(coluna);
		transacao.setPacote(pacote);
		
		transacoes.add(transacao);
	}
	
	
}
