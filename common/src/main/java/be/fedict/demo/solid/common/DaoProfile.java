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

import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.VCARD4;

/**
 * Data object
 * 
 * @author Bart Hanssens
 */
public class DaoProfile extends Dao {
	private IRI iri;
	private String name;
	private String email;
	
	public IRI getIRI() {
		return iri;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return email;
	}

	
	@Override
	public DaoProfile fromModel(Model m) {
		Optional<Statement> stmt; 
		stmt = Dao.getStatement(m, null, FOAF.PRIMARY_TOPIC);
		iri = stmt.isPresent() ? (IRI) stmt.get().getObject() : null;
		
		stmt = Dao.getStatement(m, iri, FOAF.NAME);
		name = stmt.get().getObject().toString();
		
		stmt = Dao.getStatement(m, iri, VCARD4.HAS_EMAIL);
		Value val = stmt.get().getObject();
		if (val instanceof Resource) {
			String str = val.toString();

			if (!str.startsWith("mailto:")) {
				stmt = Dao.getStatement(m, (Resource) val, VCARD4.VALUE);
				str = stmt.get().getObject().toString();
			}
			email = str.substring("mailto:".length());
		}
				
		return this;
	}
}
