package util;

import java.util.ArrayList;

public class Resultado {
	public char coluna = 'B';
	public char status = 'E';
	public String ack;
	public String tipo;
	public String pacote;

	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack.toUpperCase();
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getPacote() {
		return pacote;
	}
	public void setPacote(String pacote) {
		this.pacote = pacote;
	}
	
	public boolean jaExisteEssaCombinacaoNoResultado(ArrayList<Resultado> resultadoFinal, String ack, String pacote) {
		for (Resultado resultado : resultadoFinal) {
			if (resultado.getAck().toLowerCase().equals(ack) && resultado.getPacote().equals(pacote)) {
				return true;
			}
		}
		return false;
	}
	public String resultadoMontado() {
		String result = this.coluna+" "+this.status+" "+this.getAck()+" "+this.getTipo()+" "+this.getPacote();
		return result;
	}
}
