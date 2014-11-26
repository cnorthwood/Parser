/*
 * Copyright (c) 2006-2014 DMDirc Developers
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Logging using log4j if available.
 */
public class Logging {

    /** Available Log Levels. */
    public enum LogLevel {

        TRACE("trace", "isTraceEnabled"),
        DEBUG("debug", "isDebugEnabled"),
        INFO("info", "isInfoEnabled"),
        WARN("warn", "isWarnEnabled"),
        ERROR("error", "isErrorEnabled"),
        FATAL("fatal", "isFatalEnabled");
        /** Method name. */
        private final String methodName;
        /** Check Method name. */
        private final String checkMethodName;

        /**
         * Create a new LogLevel.
         *
         * @param methodName Name of method in log4j to log to
         * @param checkMethodName Name of method in log4j to sue to check logging
         */
        LogLevel(final String methodName, final String checkMethodName) {
            this.methodName = methodName;
            this.checkMethodName = checkMethodName;
        }

        /**
         * Get the Name of method in log4j to log to.
         *
         * @return Name of method in log4j to log to
         */
        public String getMethodName() {
            return methodName;
        }

        /**
         * Get the Name of the check method in log4j.
         *
         * @return Name of check method in log4j
         */
        public String getCheckMethodName() {
            return checkMethodName;
        }
    }

    /** Singleton Instance of Logging. */
    private static Logging me;
    /** Is log4j available. */
    private final boolean isAvailable;
    /** "Log" object if available. */
    private Object log;

    /** Create a new Logging. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Logging() {
        try {
            final Class<?> factory;
            // Check for classes
            Class.forName("org.apache.commons.logging.Log");
            factory = Class.forName("org.apache.commons.logging.LogFactory");

            if (factory != null) {
                final Method getLog = factory.getMethod("getLog", Class.class);
                log = getLog.invoke(null, this.getClass());
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException cnfe) {
        }

        isAvailable = log != null;
    }

    /**
     * Get an instance of Logging.
     *
     * @return The instance of Logging
     */
    public static Logging getLogging() {
        synchronized (Logging.class) {
            if (me == null) {
                me = new Logging();
            }
            return me;
        }
    }

    /**
     * Check is a log level is available.
     *
     * @param level Level to check
     *
     * @return true if the method was invoked
     */
    public boolean levelEnabled(final LogLevel level) {
        if (isAvailable) {
            try {
                final Method check = log.getClass().getMethod(level.getCheckMethodName());
                return (Boolean) check.invoke(log);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nsme) {
            }
        }

        return false;
    }

    /**
     * Log a message if log4j is available.
     *
     * @param level Level to log at
     * @param message Message to log
     */
    public void log(final LogLevel level, final String message) {
        log(level, message, null);
    }

    /**
     * Log a message if log4j is available.
     *
     * @param level Level to log at
     * @param message Message to log
     * @param throwable Throwable to log alongside message
     */
    public void log(final LogLevel level, final String message, final Throwable throwable) {
        if (!isAvailable) {
            return;
        }
        try {
            if (throwable == null) {
                final Method method = log.getClass().getMethod(level.getMethodName(), String.class);
                method.invoke(log, message);
            } else {
                final Method method = log.getClass().getMethod(level.getMethodName(), String.class, Throwable.class);
                method.invoke(log, message, throwable);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nsme) {
        }
    }
}