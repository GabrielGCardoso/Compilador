package scanner;

public enum TiposDeToken {
	
	
	palavraReservadaINT(1),
	palavraReservadaFLOAT(2),
	palavraReservadaCHAR(3),
	palavraReservadaFOR(4),
	palavraReservadaDO(5),
	palavraReservadaWHILE(6),
	palavraReservadaIF(7),
	palavraReservadaELSE(8),
	palavraReservadaMAIN(9),
	
	IDENTIFICADOR(10),
	NUMEROINTEIRO(11),
	NUMEROFLOAT(12),
	
	DIVISAO(13),
	DIFERENTE(14),
	MENORQUE(15),
	MENOROUIGUAL(16),
	MAIORQUE(17),
	MAIOROUIGUAL(18),
	ATRIBUICAO(19),
	OPERADORIGUAL(20),
	ABREPARENTESES(21),
	FECHAPARENTESES(22),
	CONSTANTECHAR(23),
	ABRECHAVES(24),
	FECHACHAVES(25),
	VIRGULA(26),
	PONTOEVIRGULA(27),
//	OPERADORMAIS(28),
	OPERADORMENOS(29),
	OPERADORVEZES(30),
	
	FIMDEARQUIVO(31);
	
	public final int valor;
	TiposDeToken(int valor2) {
		valor = valor2;
	}
	public int getValue() {
		return valor;
	}
	public String getNome() {
		
		switch (this.valor) {
		case 1:
			return "Palavra reservada INT";
		case 2:
			return "Palavra reservada Float";
		case 3:
			return "Palavra reservada Char";
		case 4:
			return "Palavra reservada For";
		case 5:
			return "Palavra reservada Do";
		case 6:
			return "Palavra reservada While";			
		case 7:
			return "Palavra reservada If";
		case 8:
			return "Palavra reservada else";
		case 9:
			return "Palavra reservada main";
		case 10:
			return "Identificador";
		case 11:
			return "Valor inteiro";
		case 12:
			return "Valor float";
		case 13:
			return "Operador divisao";
		case 14:
			return "Operador diferente";
		case 15:
			return "Operador menor que";
		case 16:
			return "Operador menor ou igual";
		case 17:
			return "Operador maior que";
		case 18:
			return "Operador maior ou igual";
		case 19:
			return "Operador atribuicao";
		case 20:
			return "Operador igual";
		case 21:
			return "abre parenteses";
		case 22:
			return "fecha parenteses";
		case 23:
			return "Constante char";
		case 24:
			return "Abre chaves";
		case 25:
			return "Fecha chaves";
		case 26:
			return "Virgula";
		case 27:
			return "Ponto e virgula";
//		case 28:
//			return "Operador mais";
		case 29:
			return "Operador menos";
		case 30:
			return "Operador vezes";
		case 31:
			return "Fim de arquivo";
		default:
			return "Tipo ainda nao registrado";
		}
		
	}
}
