package parser;

import gci.ExecaoGCI;
import scanner.ExecaoScanner;
import scanner.Lexema;
import scanner.TiposDeToken;
import scanner.Token;
import semantico.ExecaoSemantico;
import semantico.Simbolos;
import semantico.TabelaDeSimbolos;

public class Parser {
	private static Simbolos simboloAtual = new Simbolos();
	private static int ln=0;
	private static int tn=0;
	private static int escopoAtual = 0;
	private static Token tokenAtual = null;
	private static TabelaDeSimbolos table = new TabelaDeSimbolos();

	private static void nextTK() throws ExecaoScanner {
		tokenAtual = Lexema.GetNextToken();
	}
	
	static Simbolos gCI (Simbolos op1, char op, Simbolos op2){
		System.out.print("T"+tn+" = ");
		System.out.print (op1.getLexema());
		if (op1.getContador() >= 0)
		{
			System.out.print (op1.getContador());
		}	
		System.out.print (op);
		if (op == '!')
		{
			System.out.print ("=");
		}
		System.out.print (op2.getLexema());
		if (op2.getContador() >= 0)
		{
			System.out.print (op2.getContador());
		}
		System.out.print ("\n");
		op1.setLexema("T");
		op1.setContador(tn);
		tn++;
		return op1;
	}


	private static Simbolos fator() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		Simbolos simb_ret1 = null;
		

		if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES) {
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES
					|| tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
					|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT
					|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
					|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR) { // fi(expr_art)
				
				simb_ret1 = expr_arit();
				if (tokenAtual.getTipo() == TiposDeToken.FECHAPARENTESES) {
					nextTK();
					return simb_ret1;
					
				} else {
					ExecaoParser ex = new ExecaoParser("Esperado ')' apos expr_arit ", "fator", tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Esperado First de <expr_art> ", "fator", tokenAtual);
				throw ex;
			}
		} else if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR) {
			if (!table.SimboloJaDeclarado(tokenAtual.getLexema())) {
				ExecaoSemantico ex = new ExecaoSemantico("Erro variavel nao declarada", "fator", tokenAtual);
				throw ex;
			}
			simb_ret1 = table.getSimbolo(tokenAtual.getLexema(),escopoAtual).clone();
			nextTK();
			return simb_ret1;
			
		} else if (tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
				|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR) {
			simb_ret1 = new Simbolos(tokenAtual);
			nextTK();
			return simb_ret1;
		} else {
			ExecaoParser ex = new ExecaoParser("Esperado First de <fator> ", "fator", tokenAtual);
			throw ex;
		}

	}

	private static Simbolos termo() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		Simbolos simb_ret1 = null,simb_ret2 = null;
		char operacao = 0;
		int flag_ocorreu_divisao = 0;
		if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES || tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
				|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR) { // fi(fator)
			simb_ret1 = fator();
		} else {
			ExecaoParser ex = new ExecaoParser("Esperado First de <fator> ", "termo", tokenAtual);
			throw ex;
		}
		while (tokenAtual.getTipo() == TiposDeToken.DIVISAO || tokenAtual.getTipo() == TiposDeToken.OPERADORVEZES) {
			operacao = tokenAtual.getLexema().charAt(0);
			if (tokenAtual.getTipo() == TiposDeToken.DIVISAO) { 
				flag_ocorreu_divisao = 1;
			}
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES || tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
					|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT
					|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
					|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR) {// TIPOS DE DADOS INT FLOAT OU CHAR
				simb_ret2 = fator();
				
				//VERIFICACOES SEMANTICO
				if(simb_ret2 != null && simb_ret1 != null) {
					if ((simb_ret1.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret1.getTipo() == TiposDeToken.palavraReservadaCHAR) 
							&& (simb_ret2.getTipo() != TiposDeToken.CONSTANTECHAR && simb_ret2.getTipo() != TiposDeToken.palavraReservadaCHAR)) {
						ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: caracter so recebe caracter", "termo", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}
					else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) 
							&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
						ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Tipo de variavel inteira eh nao incopativel com valores caracte", "termo", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}
					else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)
							&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
						ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Erro de incopatibilidade: Tipo de variavel de ponto flutuante so pode receber variavel ponto flutuante", "termo", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}else if (flag_ocorreu_divisao == 1 && (simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) &&
							(simb_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret2.getTipo() == TiposDeToken.palavraReservadaINT)) {
						
						flag_ocorreu_divisao = 0;
						simb_ret1.setTipo(TiposDeToken.palavraReservadaFLOAT);
						
					} else if((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) &&
							(simb_ret2.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret2.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
						System.out.println ("T"+tn+" = (float) "+ simb_ret1.getLexema());
						simb_ret1.setLexema("T");
						simb_ret1.setContador(tn);
						tn++;

						
						simb_ret1.setTipo(TiposDeToken.palavraReservadaFLOAT);
						
					}else if((simb_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret2.getTipo() == TiposDeToken.palavraReservadaINT) &&
							(simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
						System.out.println ("T"+tn+" = (float) "+ simb_ret2.getLexema());
						simb_ret2.setLexema("T");
						simb_ret2.setContador(tn);
						tn++;

						
						simb_ret2.setTipo(TiposDeToken.palavraReservadaFLOAT);
						
					}	
					simb_ret1 = gCI (simb_ret1, operacao, simb_ret2);
				}
				//VERIFICACOES SEMANTICO
			} else {
				ExecaoParser ex = new ExecaoParser("Esperado First de <fator> ", "termo", tokenAtual);
				throw ex;
			}
		}
		return simb_ret1;
	}

	private static Simbolos expr_arit_() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		Simbolos simb_ret1 = null,simb_ret2;
		char operacao;
		if (tokenAtual.getTipo() == TiposDeToken.ATRIBUICAO || tokenAtual.getTipo() == TiposDeToken.OPERADORMENOS) {
			operacao = tokenAtual.getLexema().charAt(0);
			nextTK();
			simb_ret1 = termo();
			simb_ret2 = expr_arit_();
			if(simb_ret2 != null) {
				if ((simb_ret1.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret1.getTipo() == TiposDeToken.palavraReservadaCHAR) 
						&& !(simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: caracter so recebe caracter", "expr_arit_", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}
				else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) 
						&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Tipo de variavel inteira eh nao incopativel com valores caracte", "expr_arit_", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}
				else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)
						&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Erro de incopatibilidade: Tipo de variavel de ponto flutuante so pode receber variavel ponto flutuante", "expr_arit_", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) &&
						(simb_ret2.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret2.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
					System.out.print("T"+tn+" = (float) "+ simb_ret1.getLexema());
					simb_ret1.setLexema("T");
					simb_ret1.setContador(tn);
					tn++;

					simb_ret1.setTipo(TiposDeToken.palavraReservadaFLOAT);
//					simb_ret1.setLexema(simb_ret1.getLexema() + simb_ret2.getLexema());
				}
				else if ((simb_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret2.getTipo() == TiposDeToken.palavraReservadaINT) &&
						(simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
					System.out.print("T"+tn+" = (float) "+ simb_ret2.getLexema());
					simb_ret2.setLexema("T");
					simb_ret2.setContador(tn);
					tn++;
	
					simb_ret2.setTipo(TiposDeToken.palavraReservadaFLOAT);
	//				simb_ret1.setLexema(simb_ret1.getLexema() + simb_ret2.getLexema());
				}
				simb_ret1 = gCI (simb_ret1, operacao, simb_ret2);
				
			}
			
		
		}
		return simb_ret1;
	}

	private static Simbolos expr_arit() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		Simbolos simb_ret1 = null,simb_ret2 = null;
		char operacao;
		if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES || tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
				|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT) { // fi(termo)
			simb_ret1 = termo();
			operacao = tokenAtual.getLexema().charAt(0);
			simb_ret2 = expr_arit_();
			
			//VERIFICACAO DE TIPOS DO SEMANTICO
			if(simb_ret2 != null && simb_ret1 != null) {
				if ((simb_ret1.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret1.getTipo() == TiposDeToken.palavraReservadaCHAR) 
						&& (simb_ret2.getTipo() != TiposDeToken.CONSTANTECHAR && simb_ret2.getTipo() != TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: caracter so recebe caracter", "expr_arit", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}
				else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) 
						&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Tipo de variavel inteira eh nao incopativel com valores caracte", "expr_arit", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}
				
				else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)
						&& (simb_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
					ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis: Erro de incopatibilidade: Tipo de variavel de ponto flutuante so pode receber variavel ponto flutuante", "expr_arit", tokenAtual,simb_ret1,simb_ret2);
					throw ex;
				}else if ((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT) &&
							(simb_ret2.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret2.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
//					simb_ret1.setLexema(simb_ret1.getLexema() + simb_ret2.getLexema());	
					simb_ret1.setTipo(TiposDeToken.palavraReservadaFLOAT);					
					System.out.println ("T"+tn+" = (float) " + simb_ret1.getLexema());
					simb_ret1.setLexema("T");
					simb_ret1.setContador(tn);
					tn++;
				}else if ((simb_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret2.getTipo() == TiposDeToken.palavraReservadaINT) &&
							(simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)) {
		//			simb_ret1.setLexema(simb_ret1.getLexema() + simb_ret2.getLexema());	
					simb_ret2.setTipo(TiposDeToken.palavraReservadaFLOAT);					
					System.out.println ("T"+tn+" = (float) " + simb_ret2.getLexema());
					simb_ret2.setLexema("T");
					simb_ret2.setContador(tn);
					tn++;
				}
				simb_ret1 = gCI(simb_ret1,operacao,simb_ret2);
			}	
			
			return simb_ret1;

			//FIM VERIFICACAO DE TIPOS DO SEMANTICO
		} else {
			ExecaoParser ex = new ExecaoParser("Erro esperado First de <termo> ", "expr_arit", tokenAtual);
			throw ex;
		}

	}

	private static void declVar() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		simboloAtual.setTipo(tokenAtual.getTipo());
		simboloAtual.setEscopo(escopoAtual);
		nextTK();
		if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR) {
			simboloAtual.setLexema(tokenAtual.getLexema());

			if (table.ExisteSimboloNoEscopo(simboloAtual.getLexema(), escopoAtual)) {
				ExecaoSemantico ex = new ExecaoSemantico("Simbolo ja existe no escopo atual", "declVar", tokenAtual);
				throw ex;
			} else {
				table.insertSimbolo(simboloAtual.clone());
			}
			nextTK();
			while (tokenAtual.getTipo() == TiposDeToken.VIRGULA) {
				nextTK();
				if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR) {

					simboloAtual.setLexema(tokenAtual.getLexema());
					if (table.ExisteSimboloNoEscopo(simboloAtual.getLexema(), escopoAtual)) {
						ExecaoSemantico ex = new ExecaoSemantico("Simbolo ja existe no escopo atual", "declVar", tokenAtual);
						throw ex;
					} else {

						table.insertSimbolo(simboloAtual.clone());
					}
					nextTK();
				} else {
					ExecaoParser ex = new ExecaoParser("Esperado Token Identificador", "declVar", tokenAtual);
					throw ex;
				}
			}
			if (tokenAtual.getTipo() == TiposDeToken.PONTOEVIRGULA) {
				nextTK();
			} else {
				ExecaoParser ex = new ExecaoParser("Esperado Token ponto e virgula", "declVar", tokenAtual);
				throw ex;
			}
		} else {
			ExecaoParser ex = new ExecaoParser("Esperado Token Identificador", "declVar", tokenAtual);
			throw ex;
		}

	}

	private static void interacao() throws ExecaoScanner, ExecaoParser, ExecaoSemantico, ExecaoGCI {
		Simbolos s1;
		System.out.println("L"+ln);
		if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaWHILE) {
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES) {
				nextTK();
				s1 = expr_relacional();
				if (tokenAtual.getTipo() == TiposDeToken.FECHAPARENTESES) {
					nextTK();
					// GCI do Token IF
					System.out.print("if ");
					System.out.print(s1.getLexema());
					if (s1.getContador() >= 0)
					{
						System.out.print(s1.getContador());
					}		
					ln++;
					System.out.println(" == 0 goto L"+ln);
					comando();
					System.out.println ("goto L"+(ln-1));
					System.out.println ("L"+ln);
					ln++;

				} else {
					ExecaoParser ex = new ExecaoParser("Erro esperado token fecha parenteses apos expr_relacional",
							"interacao", tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro esperado token abre parenteses apos token While", "interacao",
						tokenAtual);
				throw ex;
			}
		} else if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaDO) {
			nextTK();
			comando();
			if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaWHILE) {
				nextTK();
				if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES) {
					nextTK();
					s1 = expr_relacional();
					if (tokenAtual.getTipo() == TiposDeToken.FECHAPARENTESES) {
						nextTK();
						if (tokenAtual.getTipo() == TiposDeToken.PONTOEVIRGULA) {
							nextTK();
							// GCI do Token IF
							System.out.print ("if ");
							System.out.print (s1.getLexema());
							if (s1.getContador() >= 0)
							{
								System.out.print (s1.getContador());
							}		
							System.out.println (" != 0 goto L"+ln);
							ln++;

						} else {
							ExecaoParser ex = new ExecaoParser("Erro esperado token ; apos Token While",
									"interacao", tokenAtual);
							throw ex;
						}
					} else {
						ExecaoParser ex = new ExecaoParser("Erro esperado token fecha parenteses apos expr_relacional",
								"interacao", tokenAtual);
						throw ex;
					}
				} else {
					ExecaoParser ex = new ExecaoParser("Erro esperado token abre parenteses", "interacao", tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro esperado token while", "interacao", tokenAtual);
				throw ex;
			}
		} else {
			ExecaoParser ex = new ExecaoParser("Erro esperado token do ou while", "interacao", tokenAtual);
			throw ex;
		}
	}

//	private static Simbolos verificacao_de_tipos(Simbolos s1, Simbolos s2, String metodoAtual) throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
//		return s1;
//	}
	private static Simbolos expr_relacional() throws ExecaoScanner, ExecaoParser, ExecaoSemantico {
		Simbolos simbolo_ret1, simbolo_ret2= null;
		char operacao;
		if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES || tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
				|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR
				|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT) { // fi(expr_arit)==fi(termo)
			simbolo_ret1 = expr_arit();
			operacao = tokenAtual.getLexema().charAt(0);
			if (tokenAtual.getTipo() == TiposDeToken.OPERADORIGUAL || tokenAtual.getTipo() == TiposDeToken.MAIORQUE
					|| tokenAtual.getTipo() == TiposDeToken.MENORQUE
					|| tokenAtual.getTipo() == TiposDeToken.MENOROUIGUAL
					|| tokenAtual.getTipo() == TiposDeToken.MAIOROUIGUAL
					|| tokenAtual.getTipo() == TiposDeToken.DIFERENTE) { // operador relacional
				nextTK();
				if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES
						|| tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
						|| tokenAtual.getTipo() == TiposDeToken.NUMEROINTEIRO
						|| tokenAtual.getTipo() == TiposDeToken.CONSTANTECHAR
						|| tokenAtual.getTipo() == TiposDeToken.NUMEROFLOAT) {// fi(expr_arit)==fi(termo)
					simbolo_ret2 = expr_arit();
					
					//VERIFICACOES DE TIPOS NO SEMANTICO
					if(simbolo_ret2 != null) {						
						if ((simbolo_ret1.getTipo() == TiposDeToken.CONSTANTECHAR || simbolo_ret1.getTipo() == TiposDeToken.palavraReservadaCHAR) 
								&& (simbolo_ret2.getTipo() != TiposDeToken.CONSTANTECHAR && simbolo_ret2.getTipo() != TiposDeToken.palavraReservadaCHAR)) {
							ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis char somente com char", "expr_relacional", tokenAtual,simbolo_ret1,simbolo_ret2);
							throw ex;						
						}
						else if ((simbolo_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simbolo_ret1.getTipo() == TiposDeToken.palavraReservadaINT) 
								&& (simbolo_ret2.getTipo() == TiposDeToken.CONSTANTECHAR || simbolo_ret2.getTipo() == TiposDeToken.palavraReservadaCHAR)) {
							ExecaoSemantico ex = new ExecaoSemantico("Erro tipos incompativeis int nao relaciona com char", "expr_relacional", tokenAtual,simbolo_ret1,simbolo_ret2);
							throw ex;
						}
						else if ((simbolo_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simbolo_ret1.getTipo() == TiposDeToken.palavraReservadaINT) 
								&& (simbolo_ret2.getTipo() == TiposDeToken.NUMEROFLOAT || simbolo_ret2.getTipo() == TiposDeToken.palavraReservadaFLOAT)) 	{
							simbolo_ret1.setTipo(TiposDeToken.palavraReservadaFLOAT);
							//GCI TRANSFORMACAO DE TIPO
							System.out.print ("T"+tn+" = (float) "+simbolo_ret1.getLexema());
							if (simbolo_ret1.getContador() >= 0)
							{
								System.out.print(simbolo_ret1.getContador());
							}
							System.out.print("\n");
							simbolo_ret1.setLexema("T");
							simbolo_ret1.setContador(tn);
							tn++;
						}
						else if ((simbolo_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simbolo_ret2.getTipo() == TiposDeToken.palavraReservadaINT) 
								&& (simbolo_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simbolo_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)) 	{
							simbolo_ret2.setTipo(TiposDeToken.palavraReservadaFLOAT);
							//GCI TRANSFORMACAO DE TIPO
							System.out.print ("T"+tn+" = (float) "+simbolo_ret2.getLexema());
							if (simbolo_ret2.getContador() >= 0)
							{
								System.out.print(simbolo_ret2.getContador());
							}
							System.out.print("\n");
							simbolo_ret2.setLexema("T");
							simbolo_ret2.setContador(tn);
							tn++;						
						}
					}
					simbolo_ret1 = gCI(simbolo_ret1, operacao, simbolo_ret2);
					return simbolo_ret1;

					//FIM VERIFICACOES DE TIPOS NO SEMANTICO

				} else {
					ExecaoParser ex = new ExecaoParser("Erro esperado First de <expr_arit>", "expr_relacional",
							tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro esperado operador relacional apos <expr_arit>",
						"expr_relacional", tokenAtual);
				throw ex;
			}
		} else {
			ExecaoParser ex = new ExecaoParser("Erro esperado First de <expr_arit>", "expr_relacional", tokenAtual);
			throw ex;
		}
	}

	private static Simbolos atribuicao() throws ExecaoScanner, ExecaoParser, ExecaoSemantico{
		Simbolos simb_ret1 = null;
		if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR) {
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.ATRIBUICAO) {
				nextTK();
				simb_ret1 = expr_arit();
				if (tokenAtual.getTipo() == TiposDeToken.PONTOEVIRGULA) {
					nextTK();
					return simb_ret1;
				} else {
					ExecaoParser ex = new ExecaoParser("Erro esperado token ; apos <expr_arit>", "atribuicao",
							tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro esperado token de atribuicao", "atribuicao",
						tokenAtual);
				throw ex;
			}
		} else {
			ExecaoParser ex = new ExecaoParser("Erro esperado token Identificador", "atribuicao",
					tokenAtual);
			throw ex;
		} 	
	}

	private static void comando() throws ExecaoParser, ExecaoScanner, ExecaoSemantico, ExecaoGCI {
		Simbolos s1;
		if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR || tokenAtual.getTipo() == TiposDeToken.ABRECHAVES) { // FIRTS(comando
																														// basico)
			comando_basico();
		} else if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaWHILE
				|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaDO) { // FIRST(iteracaoo)
			interacao();
		} else if(tokenAtual.getTipo() == TiposDeToken.palavraReservadaINT
				|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaCHAR
				|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaFLOAT) {
			ExecaoGCI ex = new ExecaoGCI("Nao se pode declarar variavel fora de bloco!",
					"comando", tokenAtual);
			throw ex;			
		} 
		else if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaIF) {
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES) {
				nextTK();
				s1 = expr_relacional();

				if (tokenAtual.getTipo() == TiposDeToken.FECHAPARENTESES) {
					nextTK();
					// GCI do Token IF
					System.out.print("if " + s1.getLexema());
					if (s1.getContador() >= 0){
						System.out.print(s1.getContador());
					}		
					System.out.println(" == 0 goto L" + ln);
					ln++;

				} else {
					ExecaoParser ex = new ExecaoParser("Erro esperado token fecha parenteses apos expr_relacional",
							"comando", tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro token esperado abre parenteses apos token if", "comando",
						tokenAtual);
				throw ex;
			}
			if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaIF
					|| tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
					|| tokenAtual.getTipo() == TiposDeToken.ABRECHAVES
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaWHILE
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaDO) { // first de comando
				comando();
				if(tokenAtual.getTipo() == TiposDeToken.palavraReservadaELSE) {
					// GCI TOKEN ELSE
					System.out.println("goto L" + ln);	
					System.out.println("L" + (ln-1));
					nextTK();
					comando();
					System.out.println("L"+ln);
				}
				else {// SE TOKEN NAO FOR ELSE GCI
					System.out.println("L"+(ln-1));
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Erro esperado token inicial de comando (first de comando)",
						"comando", tokenAtual);
				throw ex;
			}
		} // FIM DE CASO IF
		else { // ERRO DE COMANDO NAO RECEBIDO FIRST DE COMANDO
			ExecaoParser ex = new ExecaoParser(
					"Erro esperado First de <comando_basico>" + "ou \nFirst de <iteracao> ou \nToken if", "comando",
					tokenAtual);
			throw ex;
		}

	}

	private static void bloco() throws ExecaoScanner, ExecaoParser, ExecaoSemantico, ExecaoGCI {
		if (tokenAtual.getTipo() == TiposDeToken.ABRECHAVES) {
			nextTK();
			escopoAtual++;

			while (tokenAtual.getTipo() == TiposDeToken.palavraReservadaINT
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaFLOAT
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaCHAR) {// FIRST DECLARACAO DE VARIAVEL
				declVar();
			}
			while (tokenAtual.getTipo() == TiposDeToken.ABRECHAVES || tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaIF
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaWHILE
					|| tokenAtual.getTipo() == TiposDeToken.palavraReservadaDO) {// FIRST COMANDO
				comando();
			}
			if (tokenAtual.getTipo() == TiposDeToken.FECHACHAVES) {
				nextTK();

				table.deleteEscopo(escopoAtual);// TABELA DE SIMBOLOS
				escopoAtual--;
			} else {
				ExecaoParser ex = new ExecaoParser("Esperado Token fecha chaves", "bloco", tokenAtual);
				throw ex;
			}

		} else {
			ExecaoParser ex = new ExecaoParser("Esperado token Abre chaves", "bloco", tokenAtual);
			throw ex;
		}
	}

	private static void comando_basico() throws ExecaoParser, ExecaoScanner, ExecaoSemantico, ExecaoGCI {
		Simbolos simb_ret1, simb_ret2;
		if (tokenAtual.getTipo() == TiposDeToken.IDENTIFICADOR) {
			if (!(table.SimboloJaDeclarado(tokenAtual.getLexema()))) {
				ExecaoSemantico ex = new ExecaoSemantico("Erro variavel nao foi declarada", "comando_basico", tokenAtual);
				throw ex;
			} else {
				simb_ret1 = table.getSimbolo(tokenAtual.getLexema(),escopoAtual).clone();
				simb_ret2 = atribuicao();
				//VERIFICACAO SEMANTICO
				if(simb_ret2 != null) {
					if((simb_ret1.getTipo() == TiposDeToken.CONSTANTECHAR || simb_ret1.getTipo() == TiposDeToken.palavraReservadaCHAR)
							&& (simb_ret2.getTipo() != TiposDeToken.CONSTANTECHAR && simb_ret2.getTipo() != TiposDeToken.palavraReservadaCHAR)){
						ExecaoSemantico ex = new ExecaoSemantico("Tipo incompativel para atribuicao char so recebe char", "comando_basico", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}
					else if((simb_ret1.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT)
							&& (simb_ret2.getTipo() != TiposDeToken.NUMEROINTEIRO && simb_ret2.getTipo() != TiposDeToken.palavraReservadaINT)){
						ExecaoSemantico ex = new ExecaoSemantico("Tipo incompativel para atribuicao", "comando_basico", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}
					else if((simb_ret2.getTipo() == TiposDeToken.NUMEROINTEIRO || simb_ret1.getTipo() == TiposDeToken.palavraReservadaINT)
							&& (simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)){
						simb_ret2.setTipo(TiposDeToken.palavraReservadaFLOAT);
						
						System.out.print("T"+tn+" = (float) " + simb_ret2.getLexema());
						if (simb_ret2.getContador() >= 0)
						{
							System.out.print (simb_ret2.getContador());
						}
						System.out.print ("\n");
						simb_ret2.setLexema("T");
						simb_ret2.setContador(tn);
						tn++;
					}

					else if((simb_ret1.getTipo() == TiposDeToken.NUMEROFLOAT || simb_ret1.getTipo() == TiposDeToken.palavraReservadaFLOAT)
							&& (simb_ret2.getTipo() != TiposDeToken.NUMEROINTEIRO && simb_ret2.getTipo() != TiposDeToken.palavraReservadaINT &&
									simb_ret2.getTipo() != TiposDeToken.NUMEROFLOAT && simb_ret2.getTipo() != TiposDeToken.palavraReservadaFLOAT)){
						ExecaoSemantico ex = new ExecaoSemantico("Tipo incompativel para atribuicao float nao recebe char", "comando_basico", tokenAtual,simb_ret1,simb_ret2);
						throw ex;
					}
					
					System.out.print (simb_ret1.getLexema()+"="+ simb_ret2.getLexema());
					if (simb_ret2.getContador() >= 0)
					{
						System.out.print(simb_ret2.getContador());
					}
					System.out.print("\n");
				}
				//FIM VERIFICACAO SEMANTICO
			}

		} else {
			bloco();
		}
	}

	public static void programa() throws ExecaoScanner, ExecaoParser, ExecaoSemantico, ExecaoGCI {
		nextTK();
		if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaINT) {
			nextTK();
			if (tokenAtual.getTipo() == TiposDeToken.palavraReservadaMAIN) {
				nextTK();
				if (tokenAtual.getTipo() == TiposDeToken.ABREPARENTESES) {
					nextTK();
					if (tokenAtual.getTipo() == TiposDeToken.FECHAPARENTESES) {
						nextTK();
						bloco();
						if (tokenAtual.getTipo() != TiposDeToken.FIMDEARQUIVO) {
							ExecaoParser ex = new ExecaoParser("Ultimo Token nao eh EOF", "programa", tokenAtual);
							throw ex;
						} else {
							System.out.println("\n*** Programa Verificado com sucesso! ***");
						}
					} else {
						ExecaoParser ex = new ExecaoParser("Esperado Token ')' ", "programa", tokenAtual);
						throw ex;
					}
				} else {
					ExecaoParser ex = new ExecaoParser("Esperado Token '(' ", "programa", tokenAtual);
					throw ex;
				}
			} else {
				ExecaoParser ex = new ExecaoParser("Esperado Token main", "programa", tokenAtual);
				throw ex;
			}
		} else {
			ExecaoParser ex = new ExecaoParser("Esperado Token int", "programa", tokenAtual);
			throw ex;
		}
	}
}
