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
package be.fedict.demo.solid.inboxwriter;

import be.fedict.demo.solid.common.DaoMessage;
import be.fedict.demo.solid.common.DaoProfile;
import be.fedict.demo.solid.common.Solid;

import java.net.URI;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 *
 * @author Bart Hanssens
 */
@Command(name = "inboxwriter", version = "1.0", description = "Write to SOLID inbox.")
public class Main implements Runnable {
	@Option(names = "-p", arity = "1", description = "The URL of person.")
    private String profile;

	@Option(names = "-t", arity = "1", description = "Title of the message")
    private String title;

	@Option(names = "-d", arity = "1", description = "Description of the message")
    private String desc;
		
	@Override
	public void run()  {
		URI uri = URI.create(profile + "/profile/card");
		
		DaoProfile dao = Solid.get(uri, DaoProfile.class);
		URI inbox = URI.create(dao.getInbox().toString());
		
		DaoMessage msg = new DaoMessage(title, desc);
		Solid.post(inbox, msg.toModel());
	}

	public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);		
	}
}
