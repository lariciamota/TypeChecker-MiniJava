package br.ufpe.cin.if688.minijava.main;

import br.ufpe.cin.if688.minijava.main.AntlrLexer;
import br.ufpe.cin.if688.minijava.main.AntlrParser;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.visitor.*;

import java.io.IOException;

import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;


public class Main {

	public static void main(String[] args) throws IOException {
		
		InputStream stream = new FileInputStream("testes/QuickSort.java"); 
		ANTLRInputStream input = new ANTLRInputStream(stream);
		AntlrLexer lexer = new AntlrLexer(input);
		CommonTokenStream token = new CommonTokenStream(lexer);
		
		Program prog = (Program) new MyVisitor().visit(new AntlrParser(token).goal());

		BuildSymbolTableVisitor bst = new BuildSymbolTableVisitor();
		bst.visit(prog);
		SymbolTable st = bst.getSymbolTable();
		
		TypeCheckVisitor tc = new TypeCheckVisitor(st);
		tc.visit(prog);
		
	}

}