package semantico;

import java.util.ArrayList;

public class TabelaDeSimbolos {

	ArrayList <Simbolos> tabela;
	
	public TabelaDeSimbolos(){
		tabela = new ArrayList<Simbolos>();
	}
	public void deleteEscopo(int escopo) {
		
		for(int i=0;i<tabela.size();i++) {
			
			if(tabela.get(i).getEscopo() == escopo) {
				tabela.remove(tabela.get(i));
			}
		}
	}
	public void insertSimbolo(Simbolos simbolo) {
		tabela.add(simbolo);
	}
	public boolean ExisteSimboloNoEscopo(String nome, int escopo) {
		for(int i=0;i<tabela.size();i++) {
			if(tabela.get(i).getEscopo() == escopo && nome.equals(tabela.get(i).getLexema())) {
				return true;
			}
		}
		
		return false;
	}
	public boolean SimboloJaDeclarado(String nome) {
		for(int i=0;i<tabela.size();i++) {
			if(nome.equals(tabela.get(i).getLexema())) {
				return true;
			}
		}
		
		return false;
	}
	
	public Simbolos getSimbolo(String nome, int escopo) {
		for(int j=escopo;j>=0;j--) {
			for(int i=0;i<tabela.size();i++) {
				if(nome.equals(tabela.get(i).getLexema()) && tabela.get(i).getEscopo() == j) {
					return tabela.get(i);
				}
			}
		}
		
		return null;
	}
}
