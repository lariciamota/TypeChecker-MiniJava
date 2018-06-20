package br.ufpe.cin.if688.minijava.visitor;

import java.util.Iterator;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDecl;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclList;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.Exp;
import br.ufpe.cin.if688.minijava.ast.ExpList;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.FormalList;
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
import br.ufpe.cin.if688.minijava.ast.MethodDeclList;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.Statement;
import br.ufpe.cin.if688.minijava.ast.StatementList;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.VarDeclList;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.main.AntlrParser.ClassDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.ExpressionContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.GoalContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.IdentifierContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.IntegerContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MainClassContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MethodDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.StatementContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.TypeContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.VarDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrVisitor;
public class MyVisitor implements AntlrVisitor<Object>{

	@Override
	public Object visit(ParseTree arg0) {
		return arg0.accept(this);
	}

	@Override
	public Object visitChildren(RuleNode arg0) {
		return null;
	}

	@Override
	public Object visitErrorNode(ErrorNode arg0) {
		return null;
	}

	@Override
	public Object visitTerminal(TerminalNode arg0) {
		return null;
	}

	@Override
	public Object visitIdentifier(IdentifierContext ctx) {
		String id = ctx.getText();
		return new Identifier(id);
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclarationContext ctx) {
		Type t = (Type) ctx.type(0).accept(this);
		Identifier i = (Identifier) ctx.identifier(0).accept(this);
		
		FormalList fl = new FormalList();
		for(int n = 1; n < ctx.identifier().size(); n++) {
			Type ft = (Type) ctx.type(n).accept(this);
			Identifier fi = (Identifier) ctx.identifier(n).accept(this);
			fl.addElement(new Formal(ft, fi));
		}
		
		Iterator<VarDeclarationContext> iv = ctx.varDeclaration().iterator();
		VarDeclList vl = new VarDeclList();
		while(iv.hasNext()) {
			vl.addElement((VarDecl) iv.next().accept(this));
		}
		
		Iterator<StatementContext> is = ctx.statement().iterator();
		StatementList sl = new StatementList();
		while(is.hasNext()) {
			StatementContext n = is.next();
			sl.addElement((Statement) n.accept(this));
		}
		
		
		Exp e = (Exp) ctx.expression().accept(this);
		return new MethodDecl(t, i, fl, vl, sl, e);
	}

	@Override
	public Object visitGoal(GoalContext ctx) {
		MainClass m = (MainClass) ctx.mainClass().accept(this);
		ClassDeclList cl = new ClassDeclList();
		Iterator<ClassDeclarationContext> it = ctx.classDeclaration().iterator();
		while(it.hasNext()) {
			cl.addElement((ClassDecl) it.next().accept(this));
		}
		return new Program(m, cl);
	}

	@Override
	public Object visitExpression(ExpressionContext ctx) {
		String text = ctx.getChild(0).getText();
		switch (text) {
		case ("("):
			return ctx.expression(0).accept(this);
		case ("!"):
			Exp e = (Exp) ctx.expression(0).accept(this);
			return new Not(e);
		case ("false"):
			return new False();
		case ("true"):
			return new True();
		case ("new"):
			String text2 = ctx.getChild(1).getText();
			if(text2.equals("int")) {
				Exp exp = (Exp) ctx.expression(0).accept(this);
				return new NewArray(exp);
			} else {
				Identifier id = (Identifier) ctx.identifier().accept(this);
				return new NewObject(id);
			}
		default:
			if(ctx.getChildCount() == 1) {
				if('0'<= text.charAt(0) && text.charAt(0) <= '9') {
					return ctx.integer().accept(this);
				} else {
					return new IdentifierExp(ctx.getText());
				}
			} else {
				if(ctx.getChild(0) instanceof ExpressionContext){
					String txt2 = ctx.getChild(1).getText();
					Exp e1 = (Exp) ctx.expression(0).accept(this);
					switch (txt2) {
					case "&&":
						Exp e2 = (Exp) ctx.expression(1).accept(this);
						return new And(e1, e2);
					case "<":
						Exp e3 = (Exp) ctx.expression(1).accept(this);
						return new LessThan(e1, e3);
					case "+":
						Exp e4 = (Exp) ctx.expression(1).accept(this);
						return new Plus(e1, e4);
					case "-":
						Exp e5 = (Exp) ctx.expression(1).accept(this);
						return new Minus(e1, e5);
					case "*":
						Exp e6 = (Exp) ctx.expression(1).accept(this);
						return new Times(e1, e6);
					case "[":
						Exp e7 = (Exp) ctx.expression(1).accept(this);
						return new ArrayLookup(e1, e7);
					case ".":
						String txt3 = ctx.getChild(2).getText();
						if(txt3.equals("length")) {
							return new ArrayLength(e1);
						} else {
							Exp exp1 = (Exp) ctx.expression(0).accept(this);
							Identifier i = (Identifier) ctx.identifier().accept(this);
							
							ExpList el = new ExpList();
							for(int n = 1; n < ctx.expression().size(); n++) {
								el.addElement((Exp) ctx.expression(n).accept(this));
							}
							return new Call(exp1, i, el);
						}
					default:
						return new This();
					}
				}
				
			}
		}
		
		return null;
	}
		

	@Override
	public Object visitMainClass(MainClassContext ctx) {
		Identifier im = (Identifier) ctx.identifier(0).accept(this);
		Identifier ia = (Identifier) ctx.identifier(1).accept(this);
		Statement s = (Statement) ctx.statement().accept(this);
		return new MainClass(im, ia, s);
	}

	@Override
	public Object visitStatement(StatementContext ctx) {
		String text = ctx.getChild(0).getText();
		if(text.equals("{")){
			Iterator<StatementContext> i = ctx.statement().iterator();
			StatementList sl = new StatementList();
			while(i.hasNext()) {
				sl.addElement((Statement) i.next().accept(this));
			}
			return new Block(sl);
		} else if(text.equals("if")){
			Exp e = (Exp) ctx.expression(0).accept(this);
			Statement s1 = (Statement) ctx.statement(0).accept(this);
			Statement s2 = (Statement) ctx.statement(1).accept(this);
			return new If(e, s1, s2);
		} else if(text.equals("while")){
			Exp ex = (Exp) ctx.expression(0).accept(this);
			Statement s = (Statement) ctx.statement(0).accept(this);
			return new While(ex, s);
		} else if(text.equals("System.out.println")){
			Exp exp = (Exp) ctx.expression(0).accept(this);
			return new Print(exp);
		} else {
			int size = ctx.expression().size();
			if(size == 1){
				Identifier id = (Identifier) ctx.identifier().accept(this);
				Exp expr = (Exp) ctx.expression(0).accept(this);
				return new Assign(id, expr);
			} else {
				Identifier id2 = (Identifier) ctx.identifier().accept(this);
				Exp exp1 = (Exp) ctx.expression(0).accept(this);
				Exp exp2 = (Exp) ctx.expression(1).accept(this);
				return new ArrayAssign(id2, exp1, exp2);
			}
		}		
	}
	

	@Override
	public Object visitType(TypeContext ctx) {
		String type = ctx.getText();
		if(type.contains("[")){
			return new IntArrayType();
		} else if (type.contains("boolean")){
			return new BooleanType();
		} else if (type.contains("int")){
			return new IntegerType();
		} else {
			return new IdentifierType(type);
		}
	}

	@Override
	public Object visitVarDeclaration(VarDeclarationContext ctx) {
		Type t = (Type) ctx.type().accept(this);
		Identifier i = (Identifier) ctx.identifier().accept(this);
		
		return new VarDecl(t, i);
	}

	@Override
	public Object visitClassDeclaration(ClassDeclarationContext ctx) {
		Identifier i1 = (Identifier) ctx.identifier(0).accept(this);
		
		Iterator<VarDeclarationContext> it = ctx.varDeclaration().iterator();
		VarDeclList vl = new VarDeclList();
		while(it.hasNext()) {
			vl.addElement((VarDecl) it.next().accept(this));
		}
		
		Iterator<MethodDeclarationContext> it2 = ctx.methodDeclaration().iterator();
		MethodDeclList ml = new MethodDeclList();
		while(it2.hasNext()) {
			ml.addElement((MethodDecl) it2.next().accept(this));
		}
		if( ctx.identifier().size() > 1) {
			Identifier i2 = (Identifier) ctx.identifier(1).accept(this);
			return new ClassDeclExtends(i1, i2, vl, ml);
		}
		return new ClassDeclSimple(i1, vl, ml);
	}

	@Override
	public Object visitInteger(IntegerContext ctx) {
		String text = ctx.getText();
		return new IntegerLiteral(Integer.parseInt(text));
	}

}