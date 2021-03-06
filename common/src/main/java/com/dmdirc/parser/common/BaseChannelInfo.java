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

package com.dmdirc.parser.common;

import com.dmdirc.parser.interfaces.ChannelClientInfo;
import com.dmdirc.parser.interfaces.ChannelInfo;
import com.dmdirc.parser.interfaces.Parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a basic implementation of the {@link ChannelInfo} interface.
 */
public abstract class BaseChannelInfo implements ChannelInfo {

    /** The parser that owns this client. */
    private final Parser parser;

    /** A map for random data associated with the client to be stored in. */
    private final Map<Object, Object> map = new HashMap<>();

    /** The clients in this channel. */
    private final Map<String, ChannelClientInfo> clients = new HashMap<>();

    /** The name of this channel. */
    private final String name;

    public BaseChannelInfo(final Parser parser, final String name) {
        this.parser = parser;
        this.name = name;
    }

    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public Map<Object, Object> getMap() {
        return map;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Parser getParser() {
        return parser;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public void sendMessage(final String message) {
        parser.sendMessage(name, message);
    }

    @Override
    public void sendAction(final String action) {
        parser.sendAction(name, action);
    }

    @Override
    public Collection<ChannelClientInfo> getChannelClients() {
        return clients.values();
    }

    @Override
    public int getChannelClientCount() {
        return clients.size();
    }

    @Override
    public ChannelClientInfo getChannelClient(final String client) {
        return getChannelClient(client, false);
    }

    /**
     * Adds a new client to this channel's user list.
     *
     * @param key The key to identify the client by
     * @param client The client to be added
     */
    protected void addClient(final String key, final ChannelClientInfo client) {
        clients.put(key, client);
    }

    /**
     * Removes an existing client from this channel's user list.
     *
     * @param key The key that the client is identified by
     */
    protected void removeClient(final String key) {
        clients.remove(key);
    }

    /**
     * Retrieves the client identified by the specified key.
     *
     * @param key The key identifying the client to be retrieved
     * @return The client corresponding to the give key
     */
    protected ChannelClientInfo getClient(final String key) {
        return clients.get(key);
    }

}
