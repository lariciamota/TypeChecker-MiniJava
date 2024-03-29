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
	private boolean isVar;
	private boolean isMethod;
	

	public TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
		this.isVar = false;
		this.isMethod = false;
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
		this.currClass = this.symbolTable.getClass(n.i1.toString());
		this.currMethod = this.symbolTable.getMethod("Main", this.currClass.getId());
		n.i1.accept(this);
		this.isVar = true;
		n.i2.accept(this);
		this.isVar = false;
		n.s.accept(this);
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		this.currClass = this.symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			this.currMethod = this.currClass.getMethod(n.ml.elementAt(i).toString());
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		this.currClass = this.symbolTable.getClass(n.i.toString());
		this.parentClass = this.symbolTable.getClass(n.j.s);
		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			if(this.currClass.containsMethod(n.ml.elementAt(i).toString())){
				this.currMethod = this.currClass.getMethod(n.ml.elementAt(i).toString());
			} else {
				this.currMethod = this.parentClass.getMethod(n.ml.elementAt(i).toString());
			}
			n.ml.elementAt(i).accept(this);
		}
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		Type t = n.t.accept(this);
		this.isVar = true;
		n.i.accept(this);
		this.isVar = false;
		return t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		this.currMethod = this.currClass.getMethod(n.i.toString());
		Type t = n.t.accept(this);
		this.isMethod = true;
		n.i.accept(this);
		this.isMethod = false;
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
		this.isVar = true;
		n.i.accept(this);
		this.isVar = false;
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
		if(!this.symbolTable.containsClass(n.s)){
			System.err.println("Error IDType: doesnt exist " + n.s);
		}
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
			System.err.println("Error IF: op not boolean");
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
			System.err.println("Error WHILE: op not boolean");
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
		this.isVar = true;
		Type ti = n.i.accept(this);
		this.isVar = false;
		Type te = n.e.accept(this);
		if(!this.symbolTable.compareTypes(ti, te)) {
			System.err.println("Error Assign: diferent types");
			System.exit(0);
		}
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		this.isVar = true;
		Type id = n.i.accept(this);
		this.isVar = false;
		Type exp1 = n.e1.accept(this);
		Type exp2 = n.e2.accept(this);
		Type i = new IntegerType();
		Type ia = new IntArrayType();
		if(!this.symbolTable.compareTypes(ia, id)){
			System.err.println("Error ArrayAssign: IntArrayType not found");
			System.exit(0);
		}
		if (!(this.symbolTable.compareTypes(exp1, i)|| this.symbolTable.compareTypes(exp2, i))) {
			System.err.println("Error ArrayAssign: IntType not found");
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
			System.err.println("Error AND: op not boolean");
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
			System.err.println("Error LessThan: op not int");
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
			System.err.println("Error PLUS: op not int");
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
			System.err.println("Error MINUS: op not int");
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
			System.err.println("Error TIMES: op not int");
			System.exit(0);
		}
		return new IntegerType();	
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type t = n.e1.accept(this);
		Type ia = new IntArrayType();
		if (!this.symbolTable.compareTypes(t,ia)) {
			System.err.println("Error ArrayLookup: ArrayType not found");
			System.exit(0);
		}
		Type ty = n.e2.accept(this);
		Type i = new IntegerType();
		if (!this.symbolTable.compareTypes(ty,i)) {
			System.err.println("Error ArrayLookup: IntType not found");
			System.exit(0);
		}
		return new IntegerType();
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type t = n.e.accept(this);
		Type i = new IntArrayType();
		if (!this.symbolTable.compareTypes(t,i)) {
			System.err.println("Error ArrayLength: IntArrayType not found");
			System.exit(0);
		}
		return new IntegerType();
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		Type tvar = n.e.accept(this);
		if(tvar instanceof IdentifierType) {
			Class c = this.symbolTable.getClass(((IdentifierType) tvar).s);
			Method m = this.symbolTable.getMethod(n.i.toString(), c.getId());
			Class cAux = this.currClass;
			this.currClass = c;
			this.isMethod = true;
			Type tmet = n.i.accept(this);
			this.isMethod = false;
			this.currClass = cAux;
			boolean hasMethod;
			if(c.parent() != null){
				Class parent = this.symbolTable.getClass(c.parent());
				hasMethod = c.containsMethod(n.i.s) || parent.containsMethod(n.i.s);
			} else {
				hasMethod = c.containsMethod(n.i.s);
			}
			
			if(!hasMethod) {
				System.err.println("Error Call: method " + n.i.s + " not in class " + c.getId());
				System.exit(0);
			}
			int index = 0;
			Type parC = null;
			Type parM = null;
			while(index < n.el.size()) {
				parC = n.el.elementAt(index).accept(this);
				if(m.getParamAt(index) == null) {
					System.err.println("Error Call: method has more params than expected");
					System.exit(0);
				}
				parM = m.getParamAt(index).type();
				if(! this.symbolTable.compareTypes(parM, parC)) {
					System.err.println("Error Call: param type different than expected");
					System.exit(0);
				}
				index++;
			}
			if(! (m.getParamAt(index) == null)) {
				System.err.println("Error Call: method has less params than expected");
				System.exit(0);
			}
			return tmet;
		} else {
			System.err.println("Error Call: id not found");
			System.exit(0);
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
		return this.symbolTable.getVarType(this.currMethod, this.currClass, n.s);
	}

	public Type visit(This n) {
		return currClass.type();
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type e = n.e.accept(this);
		Type i = new IntegerType();
		if(! this.symbolTable.compareTypes(e, i)) {
			System.err.println("Error NewArray: IntType not found");
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
			System.err.println("Error NOT : op not boolean");
			System.exit(0);
		}
		return b;
	}

	// String s;
	public Type visit(Identifier n) {
		if(this.isVar) {
			return this.symbolTable.getVarType(this.currMethod, this.currClass, n.s);
		} else if (this.isMethod) {
			return this.symbolTable.getMethodType(n.s, this.currClass.getId());
		} else {
			Class c = this.symbolTable.getClass(n.toString());
			if(c == null) {
				System.err.println("Error ID : var not found");
				System.exit(0);
			}
			return c.type();
		}
	}

}
