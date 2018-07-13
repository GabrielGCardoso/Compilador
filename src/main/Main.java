package main;
import java.io.FileNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import gci.ExecaoGCI;
import scanner.ExecaoScanner;
import scanner.Lexema;
import scanner.Token;
import semantico.ExecaoSemantico;
import parser.ExecaoParser;
import parser.Parser;

public class Main {
	static Token t;
	
	public static void main(String[] args) {
		
		Path caminhoDoArquivo;
		
		if(args.length == 0) {
			System.out.println("Passe o caminho do arquivo pelo argumento!");
			System.out.println("EX: java scanner arquivo.txt ");
		}else{
			
			caminhoDoArquivo = Paths.get(args[0]);
			try 
			{	
				Scanner scan = new Scanner(caminhoDoArquivo.toFile());
				Lexema.setScan(scan);
				
				Parser.programa();
			} catch (FileNotFoundException e) {
				System.out.println("Problema com o camminho do arquivo passado!");
				System.out.println("Tente passar outro caminho");
			}catch (ExecaoScanner e){
				System.out.println(e.getMensagePersonalized());
			} catch (ExecaoParser e) {
				System.out.println(e.getMensagePersonalized());
			} catch (ExecaoSemantico e) {
				System.out.println(e.getMensagePersonalized());
			} catch (ExecaoGCI e) {
				System.out.println(e.getMensagePersonalized());
			}
		}
	}
}
