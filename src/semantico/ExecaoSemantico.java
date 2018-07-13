package semantico;
import scanner.Token;

public class ExecaoSemantico extends Exception {

	private static final String mensagemPadrao = "Execao do Semantico -> "; 
	
	private String mensagePersonalized;

	public ExecaoSemantico(String msg,String metodo, Token t) {
		super(mensagemPadrao);
		
		this.mensagePersonalized = mensagemPadrao + "Metodo: " + metodo+
				"\nMenssagem Erro : "+ msg + 
				"\nRecebido Token{"+
				"\n Tipo:"+ t.getTipo().getNome() +
				"\n linha:" +t.getLinha()+ " Coluna:" +t.getColuna() +"\n Lexema:"+t.getLexema()+"\n}";
				
	}
	public ExecaoSemantico(String msg,String metodo, Token t, Simbolos s1, Simbolos s2) {
		
		super(mensagemPadrao);
		
		
		this.mensagePersonalized = mensagemPadrao + "Metodo: " + metodo+
				"\nMenssagem Erro : "+ msg +
				"\nSimbolo 1 : {"+
				"\nTipo : "+ s1.getTipo().getNome()+ 
				"\nLexema:" +s1.getLexema()+
				"\n}"+
				"\nSimbolo 2 : {"+ 
				"\nTipo : "+s2.getTipo().getNome() +
				"\nLexema:" +s2.getLexema()+
				"\n}"+
				"\nUltimo Token Lido{"+
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
