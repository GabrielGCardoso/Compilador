package semantico;

import scanner.TiposDeToken;
import scanner.Token;

public class Simbolos extends Token{
	private int escopo;
	//MODIFICACAO PARA O GCI
	private int contador = -1;
	
	public Simbolos() {
		super();
		this.escopo = 0;
	}
	public Simbolos(int escopo) {
		super();
		this.escopo = escopo;
	}
	public Simbolos(Token token) {
		super(token.getLinha(),token.getColuna(),token.getLexema(),token.getTipo());
	}
	public Simbolos(int lin,int col,String lex, TiposDeToken tipo,int escopo) {
		super(lin,col,lex,tipo);
		this.escopo =  escopo;
	}
	public int getEscopo() {
		return escopo;
	}
	public void setEscopo(int escopo) {
		this.escopo = escopo;
	}
	//PARA O GCI INICIO
	public void setContador(int contador) {
		this.contador = contador;
	}
	public int getContador() {
		return this.contador;
	}
	//PARA O GCI FIM 
	@Override
	public Simbolos clone(){
		return new Simbolos(this.getLinha(),this.getColuna(),this.getLexema(), this.getTipo(),this.getEscopo());
	}
	
}
