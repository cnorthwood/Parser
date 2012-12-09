/*
 * Copyright (c) 2006-2012 DMDirc Developers
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

package com.dmdirc.parser.irc;

import java.util.Date;

/**
 * Process CAP extension.
 * There are currently no callbacks related to this, as it is just to tell
 * the server what we support.
 *
 * For all the capabilities we support, we are always able to handle lines#
 * either with or without the capability enabled, so we don't actually need to
 * keep much/any state here, which makes this easy.
 *
 * See: http://www.leeh.co.uk/draft-mitchell-irc-capabilities-02.html
 */
public class ProcessCap extends TimestampedIRCProcessor {
    /** Have we handled the pre-connect cap request? */
    private boolean hasCapped = false;

    /**
     * Create a new instance of the IRCProcessor Object.
     *
     * @param parser IRCParser That owns this IRCProcessor
     * @param manager ProcessingManager that is in charge of this IRCProcessor
     */
    protected ProcessCap(final IRCParser parser, final ProcessingManager manager) {
        super(parser, manager);
    }

    /**
     * Process CAP responses.
     *
     * @param sParam Type of line to process ("CAP")
     * @param token IRCTokenised line to process
     */
    @Override
    public void process(final Date date, final String sParam, final String[] token) {
        // We will only automatically handle the first ever pre-001 CAP LS
        // response.
        // After that, the user may be sending stuff themselves so we do
        // nothing.
        if (!hasCapped && !parser.got001 && token.length > 4 && token[3].equalsIgnoreCase("LS")) {
            final String[] caps = token[token.length - 1].split(" ");
            for (final String cap : caps) {
                if (cap.equalsIgnoreCase("multi-prefix") || cap.equalsIgnoreCase("tsirc") || cap.equalsIgnoreCase("userhost-in-names")) {
                    // Send cap requests as individual lines, as some servers
                    // only appear to accept them one at a time.
                    parser.sendRawMessage("CAP REQ :" + cap);
                }
            }

            // If this is the last of the LS responses, set hasCapped to true
            // so that we don't try this again, and send "CAP END"
            // We will accept any of the following to be the end of the list:
            //     :DFBnc.Server CAP Dataforce LS :some caps
            //     :DFBnc.Server CAP Dataforce LS
            // but not:
            //     :DFBnc.Server CAP Dataforce LS *
            if (token.length == 4 || (token.length == 5 && !token[4].equals("*"))) {
                hasCapped = true;
                parser.sendRawMessage("CAP END");
            }
        }
    }

    /**
     * What does this IRCProcessor handle.
     *
     * @return String[] with the names of the tokens we handle.
     */
    @Override
    public String[] handles() {
        return new String[]{"CAP"};
    }
}