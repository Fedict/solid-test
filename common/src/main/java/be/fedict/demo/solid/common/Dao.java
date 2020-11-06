/*
 * Copyright (c) 2020, FPS BOSA DG DT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package be.fedict.demo.solid.common;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Optional;
import org.eclipse.rdf4j.model.BNode;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

/**
 * Data object
 * 
 * @author Bart Hanssens
 */
public abstract class Dao {
	private Model m;

	protected Model getModel() {
		return m;
	}

	public static IRI createIRI(String str) {
		return SimpleValueFactory.getInstance().createIRI(str);
	}

	public static BNode createBNode(String str) {
		return SimpleValueFactory.getInstance().createBNode(str);
	}

	public static Literal createLiteral(String str) {
		return SimpleValueFactory.getInstance().createLiteral(str);
	}

	/**
	 * Get only one statement
	 * 
	 * @param m model
	 * @param subj subject or null
	 * @param pred predicate
	 * @return statement
	 */
	public static Optional<Statement> getStatement(Model m, Resource subj, IRI pred) {
		Iterator<Statement> iter = m.getStatements(subj, pred, null).iterator();
		return iter.hasNext() ? Optional.of(iter.next()) : Optional.empty();
	}

	/**
	 * 
	 * @return 
	 */
	public static String toTurtle(Model m) {
		StringWriter str = new StringWriter();
		Rio.write(m, str, RDFFormat.TURTLE);
		return str.toString();
	}
}
