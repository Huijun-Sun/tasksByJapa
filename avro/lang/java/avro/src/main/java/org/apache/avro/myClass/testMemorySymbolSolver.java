package org.apache.avro.myClass;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.CompilationUnitContext;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.resolution.typesolvers.MemoryTypeSolver;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testMemorySymbolSolver {

    private static final String FILE_PATH = "src/main/java/org/apache/avro/myClass/Foo.java";

    @Test
    public void solveTypeInSamePackage() throws Exception {
        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        ResolvedReferenceTypeDeclaration otherClass = EasyMock.createMock(ResolvedReferenceTypeDeclaration.class);
        EasyMock.expect(otherClass.getQualifiedName()).andReturn("org.apache.avro.myClass.Bar");

        /* Start of the relevant part */
        MemoryTypeSolver memoryTypeSolver = new MemoryTypeSolver();
        memoryTypeSolver.addDeclaration(
                "org.apache.avro.myClass.Bar", otherClass);
        Context context = new CompilationUnitContext(cu, memoryTypeSolver);

        /* End of the relevant part */

        EasyMock.replay(otherClass);

        SymbolReference<ResolvedTypeDeclaration> ref = context.solveType("Bar");
        
        //System.out.println(ref.getCorrespondingDeclaration().getQualifiedName());
        assertTrue(ref.isSolved());
        assertEquals("org.apache.avro.myClass.Bar", ref.getCorrespondingDeclaration().getQualifiedName());
    }
}
