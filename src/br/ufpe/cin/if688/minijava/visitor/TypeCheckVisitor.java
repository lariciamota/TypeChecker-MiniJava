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
import br.ufpe.cin.if688.minijava.symboltable.Class;
import br.ufpe.cin.if688.minijava.symboltable.Method;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;
	private Class currClass;
	private Class parentClass;
	private Method currMethod;


	TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		currClass = symbolTable.getClass(n.i1.toString());
		currMethod = currClass.getMethod("Main");
		
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		currClass = symbolTable.getClass(n.i.toString());
		
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			currMethod = currClass.getMethod(n.ml.elementAt(i).toString());
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		currClass = symbolTable.getClass(n.i.toString());
		parentClass = symbolTable.getClass(n.j.toString());		
		n.i.accept(this);
		n.j.accept(this);
		
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			if(currClass.containsMethod(n.ml.elementAt(i).toString())){
				currMethod = currClass.getMethod(n.ml.elementAt(i).toString());
			} else {
				currMethod = parentClass.getMethod(n.ml.elementAt(i).toString());
			}
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		Type t = n.t.accept(this);
		n.i.accept(this);
		return t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		currMethod = currClass.getMethod(n.i.toString());
		Type t = n.t.accept(this);
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
		return t;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		Type t = n.t.accept(this);
		n.i.accept(this);
		return t;
	}

	public Type visit(IntArrayType n) {
		return n;
	}

	public Type visit(BooleanType n) {
		return n;
	}

	public Type visit(IntegerType n) {
		return n;
	}

	// String s;
	public Type visit(IdentifierType n) {
		return n;
	}

	// StatementList sl;
	public Type visit(Block n) {
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
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		n.i.accept(this);
		Type exp1 = n.e1.accept(this);
		Type exp2 = n.e2.accept(this);
		Type i = new IntegerType();
		if (!(this.symbolTable.compareTypes(exp1, i)|| this.symbolTable.compareTypes(exp2, i))) {
			System.err.println("Erro: tipo inteiro não encontrado");
			System.exit(0);
		}
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
		Type ia = new IntArrayType();
		if (!this.symbolTable.compareTypes(t,ia)) {
			System.err.println("Erro: tipo array não encontrado");
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
		Type e = n.e.accept(this);
		Type i = new IntegerType();
		if(! this.symbolTable.compareTypes(e, i)) {
			System.err.println("Erro: tipo inteiro não encontrado");
			System.exit(0);
		}
		return new IntArrayType();
	}

	// Identifier i;
	public Type visit(NewObject n) {
		return n.i.accept(this);
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
		return new IdentifierType(n.s);
	}
}
