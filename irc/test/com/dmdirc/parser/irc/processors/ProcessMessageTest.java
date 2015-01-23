/*
 * Copyright (c) 2006-2015 DMDirc Developers
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

import com.dmdirc.harness.parser.TestParser;
import com.dmdirc.parser.common.CallbackNotFoundException;
import com.dmdirc.parser.interfaces.Parser;
import com.dmdirc.parser.interfaces.callbacks.PrivateActionListener;
import com.dmdirc.parser.interfaces.callbacks.PrivateCtcpListener;
import com.dmdirc.parser.interfaces.callbacks.PrivateMessageListener;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

@Ignore
public class ProcessMessageTest {

    @Test
    public void testPrivateMessage() throws CallbackNotFoundException {
        final TestParser parser = new TestParser();
        final PrivateMessageListener ipmtest = mock(PrivateMessageListener.class);
        final PrivateActionListener ipatest = mock(PrivateActionListener.class);
        final PrivateCtcpListener ipctest = mock(PrivateCtcpListener.class);

        parser.injectConnectionStrings();

        parser.injectLine(":a!b@c PRIVMSG nick :Hello!");
        verify(ipmtest).onPrivateMessage(same(parser), (Date) anyObject(),
                eq("Hello!"), eq("a!b@c"));
        verify(ipatest, never()).onPrivateAction((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString());
        verify(ipctest, never()).onPrivateCTCP((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString(), anyString());
    }

    @Test
    public void testPrivateAction() throws CallbackNotFoundException {
        final TestParser parser = new TestParser();
        final PrivateMessageListener ipmtest = mock(PrivateMessageListener.class);
        final PrivateActionListener ipatest = mock(PrivateActionListener.class);
        final PrivateCtcpListener ipctest = mock(PrivateCtcpListener.class);

        parser.injectConnectionStrings();

        parser.injectLine(":a!b@c PRIVMSG nick :" + ((char) 1) + "ACTION meep" + ((char) 1));
        verify(ipmtest, never()).onPrivateMessage((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString());
        verify(ipatest).onPrivateAction(same(parser), (Date) anyObject(),
                eq("meep"), eq("a!b@c"));
        verify(ipctest, never()).onPrivateCTCP((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString(), anyString());
    }

    @Test
    public void testPrivateCTCP() throws CallbackNotFoundException {
        final TestParser parser = new TestParser();
        final PrivateMessageListener ipmtest = mock(PrivateMessageListener.class);
        final PrivateActionListener ipatest = mock(PrivateActionListener.class);
        final PrivateCtcpListener ipctest = mock(PrivateCtcpListener.class);

        parser.injectConnectionStrings();

        parser.injectLine(":a!b@c PRIVMSG nick :" + ((char) 1) + "FOO meep" + ((char) 1));
        verify(ipmtest, never()).onPrivateMessage((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString());
        verify(ipatest, never()).onPrivateAction((Parser) anyObject(),
                (Date) anyObject(), anyString(), anyString());
        verify(ipctest).onPrivateCTCP(same(parser),
                (Date) anyObject(), eq("FOO"), eq("meep"), eq("a!b@c"));
    }

}
