// Generated from Antlr.g4 by ANTLR 4.4

	package br.ufpe.cin.if688.minijava.main;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AntlrParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AntlrVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AntlrParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(@NotNull AntlrParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(@NotNull AntlrParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#goal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoal(@NotNull AntlrParser.GoalContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull AntlrParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#mainClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainClass(@NotNull AntlrParser.MainClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull AntlrParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(@NotNull AntlrParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(@NotNull AntlrParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#varDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaration(@NotNull AntlrParser.VarDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AntlrParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(@NotNull AntlrParser.ClassDeclarationContext ctx);
}