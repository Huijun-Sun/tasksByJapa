package org.apache.avro.myClass;

import java.io.File;
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

public class task3ByJapa {

	private static final String TestDataFileMeta_FILE_PATH = "src/test/java/org/apache/avro/TestDataFileMeta.java";
	private static final String TestResolvingGrammarGenerator_FILE_PATH = "src/test/java/org/apache/avro/io/parsing/TestResolvingGrammarGenerator.java";
	private static final String TestProtocolGeneric_FILE_PATH = "avro/TestProtocolGeneric.java";

	public static void main(String[] args) throws Exception {

		CompilationUnit cu1 = StaticJavaParser.parse(new FileInputStream(TestDataFileMeta_FILE_PATH));
		CompilationUnit cu2 = StaticJavaParser.parse(new FileInputStream(TestResolvingGrammarGenerator_FILE_PATH));
		CompilationUnit cu3 = StaticJavaParser.parse(new FileInputStream(TestProtocolGeneric_FILE_PATH));

		List<String> assertionStmt = new ArrayList<>();
		VoidVisitor<List<String>> assertStmtCollector = new MethodCallExprPrinter();

		assertStmtCollector.visit(cu1, assertionStmt);
		System.out.println("Assertion Statement Printed from TestDataFileMeta.java:");
		assertionStmt.forEach(n -> System.out.println(n + "()"));

		assertionStmt.clear();
		assertStmtCollector.visit(cu2, assertionStmt);
		System.out.println("\nAssertion Statement Printed from TestResolvingGrammarGenerator.java:");
		assertionStmt.forEach(n -> System.out.println(n + "()"));

		assertionStmt.clear();
		assertStmtCollector.visit(cu3, assertionStmt);
		System.out.println("\nAssertion Statement Printed from TestProtocolGeneric.java:");
		assertionStmt.forEach(n -> System.out.println(n + "()"));

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
					}
				});
			}

			// md.findAll(MethodCallExpr.class).forEach(mce->System.out.println("mce+"+mce.toString()));
			// System.out.println("x+"+x);
			// assert.*\(.*\)$
		}
	}
}