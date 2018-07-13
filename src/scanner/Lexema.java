package scanner;

import java.util.Scanner;

public class Lexema {

	private static String lexema = "";
	private static int linha = 1;
	private static int coluna;
	private static Scanner scan;
	private static char carcterAtual = 32;
	private static boolean EOF = false;

	/* Getters and setters defaults begin */
	public char getCarcater() {
		return carcterAtual;
	}

	public void setCarcater(char carcater) {
		Lexema.carcterAtual = carcater;
	}

	public static String getLexema() {
		return lexema;
	}

	public static void setLexema(String lexema) {
		Lexema.lexema = lexema;
	}

	public static int getLinha() {
		return linha;
	}

	public static void setLinha(int linha) {
		Lexema.linha = linha;
	}

	public static int getColuna() {
		return coluna;
	}

	public static void setColuna(int coluna) {
		Lexema.coluna = coluna;
	}

	public static Scanner getScan() {
		return scan;
	}

	public static void setScan(Scanner scan) {
		Lexema.scan = scan;
		Lexema.scan.useDelimiter("");
	}
	/* Getters and setters defaults end */

	/**
	 * scanners functions
	 */

	public static char nextCaracter() {
		if (Lexema.scan.hasNext()) {
			Lexema.carcterAtual = scan.next().toCharArray()[0];
			coluna++;
		} else {
			Lexema.carcterAtual = '\0';
			EOF = true;
		}

		return carcterAtual;
	}

	protected static boolean is_blanck() {
		if (carcterAtual == ' ' || carcterAtual == '\n' || carcterAtual == '\t'|| carcterAtual == '\r')
			return true;
		return false;
	}

	protected static Token instaciar_token(int linha, int coluna, String lexema, TiposDeToken tipo) {
		Token tk = null;
		tk = new Token(linha, coluna, lexema, tipo);
		Lexema.lexema = "";
		return tk;
	}

	private static void ingnore_whitespace() {
		while (is_blanck()) {
			if (carcterAtual == '\n') {
				linha++;
				coluna = 0;
			}
			else if (carcterAtual == '\t') {
				coluna += 3;
			} else if (carcterAtual == ' ') {
				coluna++;
			}
			nextCaracter();
		}
	}
	
	private static TiposDeToken lookup(String lex) {
		switch (lex) {
		case "main":
			return TiposDeToken.palavraReservadaMAIN;
		case "if":
			return TiposDeToken.palavraReservadaIF;
		case "else":
			return TiposDeToken.palavraReservadaELSE;
		case "while":
			return TiposDeToken.palavraReservadaWHILE;
		case "do":
			return TiposDeToken.palavraReservadaDO;
		case "for":
			return TiposDeToken.palavraReservadaFOR;
		case "int":
			return TiposDeToken.palavraReservadaINT;
		case "float":
			return TiposDeToken.palavraReservadaFLOAT;
		case "char":
			return TiposDeToken.palavraReservadaCHAR;
		default:
			return TiposDeToken.IDENTIFICADOR;
		}
	}

	public static boolean isAlpha(char name) {
		String valor;
		valor = String.valueOf(name);
		return valor.matches("[a-zA-Z]+");
	}
	
	public static Token GetNextToken() throws ExecaoScanner {

		Token tk = null;

		while (!EOF) { //inicio do while

			if (is_blanck()) {// IGNORA WHITESPACE
				ingnore_whitespace();
			}
			
			if(carcterAtual == '.') { // CASO FLOAT
				Lexema.lexema += carcterAtual;
				nextCaracter();
				if (Character.isDigit(carcterAtual)) {
					while (Character.isDigit(carcterAtual)) {
						Lexema.lexema += carcterAtual;
						nextCaracter();
						if (carcterAtual == '.') {
							Lexema.lexema += carcterAtual;
							ExecaoScanner ex = new ExecaoScanner("float mal formado", Lexema.linha, Lexema.coluna,
									Lexema.lexema);
							throw ex;
						}
					}
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.NUMEROFLOAT));
				}else {
					ExecaoScanner ex = new ExecaoScanner("float mal formado", Lexema.linha, Lexema.coluna,
							Lexema.lexema);
					throw ex;
				}
			}
			
			if (Character.isDigit(carcterAtual)) { // CASO INT OU FLOAT
				while (Character.isDigit(carcterAtual)) {
					Lexema.lexema += carcterAtual;
					nextCaracter();
				}
				if (carcterAtual == '.') {// float
					Lexema.lexema += carcterAtual;
					nextCaracter();
					if (Character.isDigit(carcterAtual)) {
						while (Character.isDigit(carcterAtual)) {
							Lexema.lexema += carcterAtual;
							nextCaracter();
							if (carcterAtual == '.') {
								Lexema.lexema += carcterAtual;
								ExecaoScanner ex = new ExecaoScanner("float mal formado", Lexema.linha, Lexema.coluna,
										Lexema.lexema);
								throw ex;
							}
						}
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.NUMEROFLOAT));
					} else {
						ExecaoScanner ex = new ExecaoScanner("float mal formado", Lexema.linha, Lexema.coluna,
								Lexema.lexema);
						throw ex;
					}

				} else {
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.NUMEROINTEIRO));
				}//FIM DO INT
				
			}//FIM CASO INT FLOAT
			
			else if (carcterAtual == '_' || isAlpha(carcterAtual)) {//IDENTIFICADOR OU PALAVRA RESERVADA
				Lexema.lexema += carcterAtual;
				nextCaracter();
				while (isAlpha(carcterAtual) || (Character.isDigit(carcterAtual))) {	
					Lexema.lexema += carcterAtual;
					nextCaracter();
				}
				return (tk = instaciar_token(linha, coluna, lexema, lookup(Lexema.lexema)));
				
			}
			
			else if (carcterAtual == '<' || carcterAtual == '>' || carcterAtual == '=' || carcterAtual == '!'){ // CASO OPERADORES RELACIONAIS E IGUAL
				Lexema.lexema += carcterAtual;
				
				switch (carcterAtual) {				
				case '<':
					nextCaracter();
					if(carcterAtual == '=') {
						Lexema.lexema += carcterAtual;
						nextCaracter();
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.MENOROUIGUAL));
					}else {
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.MENORQUE));
					}
				case '>':
					nextCaracter();
					if(carcterAtual == '=') {
						Lexema.lexema += carcterAtual;
						nextCaracter();
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.MAIOROUIGUAL));
					}else {
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.MAIORQUE));
					}
				case '=':
					nextCaracter();
					if(carcterAtual == '=') {
						Lexema.lexema += carcterAtual;
						nextCaracter();
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.OPERADORIGUAL));
					}else {
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.ATRIBUICAO));
					}
				case '!':
					nextCaracter();
					if(carcterAtual == '=') {
						Lexema.lexema += carcterAtual;
						nextCaracter();
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.DIFERENTE));
					}else {
						ExecaoScanner ex = new ExecaoScanner("Operador diferente mal formado", Lexema.linha, Lexema.coluna, Lexema.lexema);
						throw ex;
					}						
				}
				
			}//FIM CASO OPERADORES RELACIONAIS
			
			else if (carcterAtual == '+' || carcterAtual == '-' || carcterAtual == '*' || carcterAtual == '/')  {// OPERADORES ARITIMETICOS
				
				Lexema.lexema += carcterAtual;
				switch (carcterAtual) {
				case '+':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.ATRIBUICAO));
				case '-':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.OPERADORMENOS));
				case '*':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.OPERADORVEZES));
				case '/':
					nextCaracter();
					if(carcterAtual == '/' || carcterAtual == '*') {
						Lexema.lexema = "";
						if(carcterAtual == '/') {
							while( carcterAtual != '\n') {
								nextCaracter();
							}
						}
						else {//COMENTARIO MULTI LINHA
							nextCaracter();//IGNORA PRIMEIRO *
							while(true) {
								
								if (is_blanck()) {// IGNORA WHITESPACE
									ingnore_whitespace();
								}
								
								if(carcterAtual == '*') {
									nextCaracter();
									if(carcterAtual == '/') {
										nextCaracter();
										break;
									}
								}
								else if (EOF) {
									ExecaoScanner ex = new ExecaoScanner("Comentario multi linha nao fechado", Lexema.linha, Lexema.coluna, Lexema.lexema);
									throw ex;
								}
								else{
									nextCaracter();
								}
								
							}// FIM DO WHILE
						}// CASO COMENTARIO MULTI LINHA
					}else 
					{
						return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.DIVISAO));
					}
				}
			}//FIM OPERADORES ARITIMETICOS
			
			else if(carcterAtual == ')' || carcterAtual ==  '(' || carcterAtual ==  '{' || carcterAtual == '}' || carcterAtual == ',' || carcterAtual == ';'){
				Lexema.lexema += carcterAtual;
				switch (carcterAtual) {
				case ')':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.FECHAPARENTESES));
				case '(':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.ABREPARENTESES));
				case '{':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.ABRECHAVES));
				case '}':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.FECHACHAVES));
				case ',':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.VIRGULA));
				case ';':
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.PONTOEVIRGULA));
				}
			}
			else if (carcterAtual == '\''){
				nextCaracter();
				Lexema.lexema += carcterAtual;
				nextCaracter();
				if(carcterAtual == '\''){
					nextCaracter();
					return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.CONSTANTECHAR));
				}else{
					ExecaoScanner ex = new ExecaoScanner("Valor char mal formado", Lexema.linha, Lexema.coluna, Lexema.lexema);
					throw ex;
				}

			}			
			else if (EOF) {// FIM DE ARQUIVO
				return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.FIMDEARQUIVO));
				
			} else {// TOKEN INVALIDO
				Lexema.lexema += carcterAtual;
				ExecaoScanner ex = new ExecaoScanner("Caracter invalido para linguagem", Lexema.linha, Lexema.coluna, Lexema.lexema);
				throw ex;
				
			}

		}// FIM DO WHILE

		return (tk = instaciar_token(linha, coluna, lexema, TiposDeToken.FIMDEARQUIVO));
	}//FIM DO SCANNER
}
