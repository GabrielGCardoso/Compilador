package scanner;

public class Token {
	private String lexema;
	private int coluna;
	private int linha;
	private TiposDeToken tipo;
	
	public Token() {
		this.linha = 0;
		this.coluna = 0;
	}
	public Token(int lin, int col, String lex, TiposDeToken tip) {
		this.linha = lin;
		this.coluna = col;
		this.lexema = lex;
		this.tipo = tip;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public TiposDeToken getTipo() {
		return tipo;
	}
	public void setTipo(TiposDeToken tipo) {
		this.tipo = tipo;
	}
	@Override
	public Token clone(){
		return new Token(this.linha, this.coluna, this.lexema, this.tipo);
	}
	public void exibirInformacoes() {
		System.out.println("Lexema: "+getLexema()+"\nLinha: " +getLinha()+"\nColuna: "+getColuna() + "\nTipo: "+getTipo().getValue() + "\nDescricao: "+getTipo().getNome());
	}
}
