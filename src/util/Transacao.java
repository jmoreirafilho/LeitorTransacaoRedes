package util;

public class Transacao {
	public char coluna; // A ou B
	public char acao; // E (Envia) ou R (Recebe)
	public String pacote;
	
	public char getColuna() {
		return coluna;
	}
	public void setColuna(char coluna) {
		this.coluna = coluna;
	}
	
	public char getAcao() {
		return acao;
	}
	public void setAcao(char acao) {
		this.acao = acao;
	}
	
	public String getPacote() {
		return pacote;
	}
	public void setPacote(String pacote) {
		this.pacote = pacote;
	}
	
	public String getAckCorrespondente() {
		String corresp = "ack"+this.pacote.substring(3);
		return corresp;
	}
}
