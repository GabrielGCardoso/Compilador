package parser;

import scanner.Token;

public class ExecaoParser extends Exception {
	
	private static final String mensagemPadrao = "Execao do Parser -> "; 
	
	private String mensagePersonalized;

	public ExecaoParser(String msg,String metodo, Token t) {
		super(mensagemPadrao);
		
		this.mensagePersonalized = mensagemPadrao + "Metodo: " + metodo+
				"\nMenssagem Erro : "+ msg + 
				"\nRecebido Token{"+
				"\n Tipo:"+ t.getTipo().getNome() +
				"\n linha:" +t.getLinha()+ " Coluna:" +t.getColuna() +"\n Lexema:"+t.getLexema()+"\n}";
				
	}

	public String getMensagePersonalized() {
		return mensagePersonalized;
	}
	public void setMensagePersonalized(String mensagePersonalized) {
		this.mensagePersonalized = mensagemPadrao + " : " + mensagePersonalized;
	}

}
