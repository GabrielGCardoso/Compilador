package scanner;

public class ExecaoScanner extends Exception {
	
	private static final String mensagemPadrao = "Execao do scanner : "; 

	private String mensagePersonalized;
	private int linha;
	private int coluna;
	
	public ExecaoScanner(String msg, int lin, int col, String lexema) {
		super(mensagemPadrao);
		this.linha = lin;
		this.coluna = col;
		this.mensagePersonalized = mensagemPadrao + msg + "\nlinha:" +lin+ " Coluna:" +col +"\nLexema:"+lexema;
	}
	
	public String getMensagePersonalized() {
		return mensagePersonalized;
	}
	public void setMensagePersonalized(String mensagePersonalized) {
		this.mensagePersonalized = mensagemPadrao + " : " + mensagePersonalized;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	

}
