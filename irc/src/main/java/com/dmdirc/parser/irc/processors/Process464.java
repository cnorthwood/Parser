/*
 * Copyright (c) 2006-2017 DMDirc Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.dmdirc.parser.irc.processors;

import com.dmdirc.parser.events.PasswordRequiredEvent;
import com.dmdirc.parser.irc.IRCParser;

import java.time.LocalDateTime;

import javax.inject.Inject;

/**
 * Process a 464 message.
 */
public class Process464 extends IRCProcessor {

    /**
     * Create a new instance of the IRCProcessor Object.
     *
     * @param parser IRCParser That owns this IRCProcessor
     */
    @Inject
    public Process464(final IRCParser parser) {
        super(parser, "464");
    }

    /**
     * Process a 464 message.
     *
     * @param sParam Type of line to process ("464")
     * @param token IRCTokenised line to process
     */
    @Override
    public void process(final LocalDateTime time, final String sParam, final String... token) {
        callPasswordRequired(time);
    }

    /**
     * Callback to all objects implementing the PasswordRequired Callback.
     */
    protected void callPasswordRequired(final LocalDateTime time) {
        getCallbackManager().publish(new PasswordRequiredEvent(parser, time));
    }
}
