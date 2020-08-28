package org.apache.avro.myClass;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

public class task4ByJapa {

	private static final String TestDataFileMeta_FILE_PATH = "src/test/java/org/apache/avro/TestDataFileMeta.java";
	private static final String TestResolvingGrammarGenerator_FILE_PATH = "src/test/java/org/apache/avro/io/parsing/TestResolvingGrammarGenerator.java";
	private static final String TestProtocolGeneric_FILE_PATH = "avro/TestProtocolGeneric.java";

	public static void main(String[] args) throws Exception {

		CompilationUnit cu1 = StaticJavaParser.parse(new FileInputStream(TestDataFileMeta_FILE_PATH));
		CompilationUnit cu2 = StaticJavaParser.parse(new FileInputStream(TestResolvingGrammarGenerator_FILE_PATH));
		CompilationUnit cu3 = StaticJavaParser.parse(new FileInputStream(TestProtocolGeneric_FILE_PATH));


		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());

		//combinedTypeSolver.add(new JavaParserTypeSolver("src/main/java"));
		//combinedTypeSolver.add(new ReflectionTypeSolver());
		
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
		StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
		
		
		
		
		List<String> assertionStmt = new ArrayList<>();
		VoidVisitor<List<String>> assertStmtCollector = new MethodCallExprPrinter();

		assertStmtCollector.visit(cu1, assertionStmt);
		//System.out.println("Assertion Statement Printed from TestDataFileMeta.java:");
		//assertionStmt.forEach(n -> System.out.println(n + "()"));

		assertionStmt.clear();
		assertStmtCollector.visit(cu2, assertionStmt);
		//System.out.println("\nAssertion Statement Printed from TestResolvingGrammarGenerator.java:");
		//assertionStmt.forEach(n -> System.out.println(n + "()"));

		assertionStmt.clear();
		assertStmtCollector.visit(cu3, assertionStmt);
		//System.out.println("\nAssertion Statement Printed from TestProtocolGeneric.java:");
		//assertionStmt.forEach(n -> System.out.println(n + "()"));

	}

	private static class MethodCallExprPrinter extends VoidVisitorAdapter<List<String>> {
		@Override
		// public void visit(MethodCallExpr md, List<String> collector) {
		public void visit(MethodDeclaration md, List<String> collector) {
			super.visit(md, collector);
			String temp = md.getAnnotations().toString();
			Pattern p1 = Pattern.compile("\\[\\@Test(\\(*.*\\)*)\\]$");// regex model:"\[\@Test(\(*.*\)*)\]$"
			Pattern p2 = Pattern.compile("(a|A)ssert.*\\(.*\\)$");// regex model:"(a|A)ssert.*\(.*\)$"
			Matcher m1 = p1.matcher(temp);
			if (m1.matches()) {
				md.findAll(MethodCallExpr.class).forEach(mce -> {
					String temp2 = mce.toString();
					Matcher m2 = p2.matcher(temp2);
					if (m2.matches()) {
						collector.add(temp2);
						
						//System.out.println(mce.toMethodCallExpr());
						
						//System.out.println(mce.toMethodReferenceExpr());
						TypeSolver typeSolver1 = new ReflectionTypeSolver();
						showReferenceTypeDeclaration(typeSolver1.solveType(temp2));
						
						//System.out.println(resolvedType);
						// System.out.println(mce.resolve().getQualifiedName());
					}
				});
			}

			// md.findAll(MethodCallExpr.class).forEach(mce->System.out.println("mce+"+mce.toString()));
			// System.out.println("x+"+x);
			// assert.*\(.*\)$
		}
	}
	 public static void showReferenceTypeDeclaration(ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration) {
		 System.out.println(String.format("== %s ==",resolvedReferenceTypeDeclaration.getQualifiedName()));
	 }
}
