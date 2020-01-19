/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package groovy.bugs

import groovy.transform.CompileStatic
import org.apache.groovy.parser.AbstractParser
import org.apache.groovy.parser.Antlr2Parser
import org.apache.groovy.parser.Antlr4Parser
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.ParserPluginFactory
import org.codehaus.groovy.control.Phases
import org.codehaus.groovy.tools.javac.JavaStubCompilationUnit
import org.junit.Test

@CompileStatic
final class StubGenTest {
    @Test
    @CompileStatic
    void testStubGenWithAntlr4() {
        File destdir = File.createTempDir()
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration(CompilerConfiguration.DEFAULT)
        compilerConfiguration.setPluginFactory(ParserPluginFactory.antlr4(compilerConfiguration))
        JavaStubCompilationUnit cu = new JavaStubCompilationUnit(compilerConfiguration, new GroovyClassLoader(), destdir)
        cu.addSource(StubGenTest.getResource('/stubgenerator/nextflow/ast/NextflowDSLImpl.groovy'))

        def b = System.currentTimeMillis()
        cu.compile(Phases.CONVERSION)
        def e = System.currentTimeMillis()

        println "antlr4-gen: ${(e - b) / 1000}s elapsed"
    }

    @Test
    @CompileStatic
    void testStubGenWithAntlr2() {
        File destdir = File.createTempDir()
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration(CompilerConfiguration.DEFAULT)
        compilerConfiguration.setPluginFactory(ParserPluginFactory.antlr2())
        JavaStubCompilationUnit cu = new JavaStubCompilationUnit(compilerConfiguration, new GroovyClassLoader(), destdir)
        cu.addSource(StubGenTest.getResource('/stubgenerator/nextflow/ast/NextflowDSLImpl.groovy'))

        def b = System.currentTimeMillis()
        cu.compile(Phases.CONVERSION)
        def e = System.currentTimeMillis()

        println "antlr2-gen: ${(e - b) / 1000}s elapsed"
    }

    @Test
    @CompileStatic
    void testParseWithAntlr4() {
        AbstractParser parser = new Antlr4Parser()
        File file = new File(StubGenTest.getResource('/stubgenerator/nextflow/ast/NextflowDSLImpl.groovy').toURI())

        def b = System.currentTimeMillis()
        parser.parse(file)
        def e = System.currentTimeMillis()

        println "antlr4-parse1: ${(e - b) / 1000}s elapsed"

        def b2 = System.currentTimeMillis()
        parser.parse(file)
        def e2 = System.currentTimeMillis()

        println "antlr4-parse2: ${(e2 - b2) / 1000}s elapsed"
    }

    @Test
    @CompileStatic
    void testParseWithAntlr2() {
        AbstractParser parser = new Antlr2Parser()
        File file = new File(StubGenTest.getResource('/stubgenerator/nextflow/ast/NextflowDSLImpl.groovy').toURI())

        def b = System.currentTimeMillis()
        parser.parse(file)
        def e = System.currentTimeMillis()

        println "antlr2-parse: ${(e - b) / 1000}s elapsed"
    }
}
