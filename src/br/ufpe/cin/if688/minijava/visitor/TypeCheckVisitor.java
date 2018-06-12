package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierExp;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;

	TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		// #TODO
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		// #TODO
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		// #TODO
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		// #TODO
		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		// #TODO
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		// #TODO
		n.t.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		// #TODO
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	public Type visit(IntArrayType n) {
		// #TODO
		return null;
	}

	public Type visit(BooleanType n) {
		// #TODO
		return null;
	}

	public Type visit(IntegerType n) {
		// #TODO
		return null;
	}

	// String s;
	public Type visit(IdentifierType n) {
		// #TODO
		return null;
	}

	// StatementList sl;
	public Type visit(Block n) {
		// #TODO
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		Type t = n.e.accept(this);
		Type b = new BooleanType();
		if (!this.symbolTable.compareTypes(t, b)) {
			System.err.println("Erro: operando não booleano");
			System.exit(0);
		}
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		Type t = n.e.accept(this);
		Type b = new BooleanType();
		if (!this.symbolTable.compareTypes(t, b)) {
			System.err.println("Erro: operando não booleano");
			System.exit(0);
		}
		n.s.accept(this);
		return b;
	}

	// Exp e;
	public Type visit(Print n) {
		// #TODO
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		// #TODO
		n.i.accept(this);
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		// #TODO
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		Type b = new BooleanType();
		if (!(this.symbolTable.compareTypes(t1, b) && this.symbolTable.compareTypes(t2, b))) {
			System.err.println("Erro: operando não booleano");
			System.exit(0);
		}
		return b;
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		Type i = new IntegerType();
		if (!(this.symbolTable.compareTypes(t1, i) && this.symbolTable.compareTypes(t2, i))) {	
			System.err.println("Erro: operando não inteiro");
			System.exit(0);
		}
		return new BooleanType();			
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		Type i = new IntegerType();
		if (!(this.symbolTable.compareTypes(t1, i) && this.symbolTable.compareTypes(t2, i))) {	
			System.err.println("Erro: operando não inteiro");
			System.exit(0);
		}
		return new IntegerType();	
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		Type i = new IntegerType();
		if (!(this.symbolTable.compareTypes(t1, i) && this.symbolTable.compareTypes(t2, i))) {	
			System.err.println("Erro: operando não inteiro");
			System.exit(0);
		}
		return new IntegerType();	
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type t1 = n.e1.accept(this);
		Type t2 = n.e2.accept(this);
		Type i = new IntegerType();
		if (!(this.symbolTable.compareTypes(t1, i) && this.symbolTable.compareTypes(t2, i))) {	
			System.err.println("Erro: operando não inteiro");
			System.exit(0);
		}
		return new IntegerType();	
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type t = n.e1.accept(this);
		Type id = new IdentifierType();
		if (!this.symbolTable.compareTypes(t,id)) {
			System.err.println("Erro: tipo identificador não encontrado");
			System.exit(0);
		}
		Type ty = n.e2.accept(this);
		Type i = new IntegerType();
		if (!this.symbolTable.compareTypes(ty,i)) {
			System.err.println("Erro: tipo inteiro não encontrado");
			System.exit(0);
		}
		return new IntegerType();
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type t = n.e.accept(this);
		Type i = new IntegerType();
		if (!this.symbolTable.compareTypes(t,i)) {
			System.err.println("Erro: tipo inteiro não encontrado");
			System.exit(0);
		}
		return i;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		// #TODO
		n.e.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}
		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
		// #TODO
		return null;
	}

	public Type visit(This n) {
		// #TODO
		return null;
	}

	// Exp e;
	public Type visit(NewArray n) {
		// #TODO
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	public Type visit(NewObject n) {
		// #TODO
		return null;
	}

	// Exp e;
	public Type visit(Not n) {
		Type t = n.e.accept(this);
		Type b = new BooleanType();
		if(! this.symbolTable.compareTypes(t, b)) {
			System.err.println("Erro: operando não booleano");
			System.exit(0);
		}
		return b;
	}

	// String s;
	public Type visit(Identifier n) {
		// #TODO
		return null;
	}
}
