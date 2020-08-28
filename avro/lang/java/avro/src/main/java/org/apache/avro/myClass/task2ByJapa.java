package org.apache.avro.myClass;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import com.javaparser.avro.task2ByJapa.MethodNameCollector;

public class task2ByJapa {

	private static final String TestDataFileMeta_FILE_PATH = "src/test/java/org/apache/avro/TestDataFileMeta.java";
	private static final String TestResolvingGrammarGenerator_FILE_PATH = "src/test/java/org/apache/avro/io/parsing/TestResolvingGrammarGenerator.java";
	private static final String TestProtocolGeneric_FILE_PATH = "avro/TestProtocolGeneric.java";

	public static void main(String[] args) throws Exception {

		CompilationUnit cu1 = StaticJavaParser.parse(new FileInputStream(TestDataFileMeta_FILE_PATH));
		CompilationUnit cu2 = StaticJavaParser.parse(new FileInputStream(TestResolvingGrammarGenerator_FILE_PATH));
		CompilationUnit cu3 = StaticJavaParser.parse(new FileInputStream(TestProtocolGeneric_FILE_PATH));

		List<String> methodNames = new ArrayList<>();
		VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();

		methodNameCollector.visit(cu1, methodNames);
		System.out.println("Method Name Printed from TestDataFileMeta.java:");
		methodNames.forEach(n -> System.out.println(n + "()"));
		
		methodNames.clear();
		methodNameCollector.visit(cu2, methodNames);
		System.out.println("\nMethod Name Printed from TestResolvingGrammarGenerator.java:");
		methodNames.forEach(n -> System.out.println(n + "()"));
		
		methodNames.clear();
		methodNameCollector.visit(cu3, methodNames);
		System.out.println("\nMethod Name Printed from TestProtocolGeneric.java:");
		methodNames.forEach(n -> System.out.println(n + "()"));
	}

	private static class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

		@Override
		public void visit(MethodDeclaration md, List<String> collector) {
			super.visit(md, collector);

			/*
			 * String temp="[@Test]"; System.out.println(md.getAnnotations().toString());
			 * if(md.getAnnotations().toString().equals(temp)) {
			 * collector.add(md.getNameAsString()); }
			 */
			String temp = md.getAnnotations().toString();
			Pattern p = Pattern.compile("\\[\\@Test(\\(*.*\\)*)\\]$");// regex model:"\[\@Test(\(*.*\)*)\]$"
			Matcher m = p.matcher(temp);
			if (m.matches()) {
				collector.add(md.getNameAsString());
			}

		}
	}

}