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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Level;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

/**
 *
 * @author Bart Hanssens
 */
public class Solid {
	private static final Logger LOG = LoggerFactory.getLogger(Solid.class);

	private static final HttpClient client;
	
	static {
		client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS)
			.sslContext(SSLChecker.allowAll())
			.build();
	}
	
	/**
	 * Get a series of triples as an RDF4J model from Solid
	 * 
	 * @param uri location to retrieve the data from
	 * @return RDF model
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static Model getModel(URI uri) throws IOException, InterruptedException {
		LOG.info("GET {}", uri);

		HttpRequest req = HttpRequest.newBuilder().timeout(Duration.ofMinutes(1))
			.uri(uri).header("Accept", RDFFormat.TURTLE.getDefaultMIMEType())
			.GET()
			.build();

		HttpResponse<InputStream> resp = client.send(req, BodyHandlers.ofInputStream());
		LOG.info("  Code {}", resp.statusCode());
		
		return Rio.parse(resp.body(), uri.toString(), RDFFormat.TURTLE);
	}
	
	/**
	 * Post a series of triples to Solid
	 * 
	 * @param uri
	 * @param m
	 * @return HTTP status code 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static int postModel(URI uri, Model m) throws IOException, InterruptedException {
		LOG.info("POST {}", uri);

		HttpRequest req = HttpRequest.newBuilder().timeout(Duration.ofMinutes(1))
			.uri(uri).header("Content-Type", RDFFormat.TURTLE.getDefaultMIMEType())
			.POST(BodyPublishers.ofString(Dao.toTurtle(m)))
			.build();
		
		HttpResponse<String> resp = client.send(req, BodyHandlers.ofString());
		LOG.info("  Code {}", resp.statusCode());
		
		return resp.statusCode();
	}

	/**
	 * Get a DAO based on RDF model
	 * 
	 * @param <T>
	 * @param uri location to retrieve the data from
	 * @param dao DAO
	 * @return 
	 */
	public static <T extends Dao> T get(URI uri, Class<T> dao) {
		try {
			Model m = getModel(uri);
			return dao.getConstructor(Model.class).newInstance(m);
		} catch (IOException | IllegalAccessException | IllegalArgumentException | InstantiationException | 
				InterruptedException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
			LOG.warn(ex.getMessage());
		}
		return null;
	}
	
	public static void post(URI uri, Model m) {
		try {
			postModel(uri, m);
		} catch (IOException | InterruptedException ex) {
			LOG.warn(ex.getMessage());
		}
	}
}
