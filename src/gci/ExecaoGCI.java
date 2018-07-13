package gci;
import scanner.Token;
public class ExecaoGCI extends Exception {
	
	
		
	private static final String mensagemPadrao = "Execao do GCI -> "; 
	
	private String mensagePersonalized;

	public ExecaoGCI(String msg,String metodo, Token t) {
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
