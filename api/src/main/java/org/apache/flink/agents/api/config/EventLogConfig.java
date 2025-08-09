/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.agents.api.config;

import org.apache.flink.agents.api.eventlog.EventFilter;
import org.apache.flink.agents.api.eventlog.storage.EventStorage;
import org.apache.flink.agents.api.eventlog.storage.InMemoryEventStorage;

import java.io.Serializable;

/**
 * Configuration for event logging capabilities.
 *
 * <p>This class provides a fluent API for configuring event logging behavior, including storage
 * backend selection, filtering rules, and performance settings.
 */
public class EventLogConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private EventStorage storage = new InMemoryEventStorage();
    private EventFilter filter = EventFilter.ACCEPT_ALL;
    private boolean enabled = true;
    private int bufferSize = 1000;
    private long flushIntervalMs = 5000;

    /**
     * Sets the storage backend for event logs.
     *
     * @param storage The storage backend to use
     * @return This config instance for method chaining
     */
    public EventLogConfig storage(EventStorage storage) {
        this.storage = storage;
        return this;
    }

    /**
     * Sets the filter for determining which events to log.
     *
     * @param filter The event filter to apply
     * @return This config instance for method chaining
     */
    public EventLogConfig filter(EventFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Enables or disables event logging.
     *
     * @param enabled true to enable event logging, false to disable
     * @return This config instance for method chaining
     */
    public EventLogConfig enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Sets the buffer size for batching events before writing to storage.
     *
     * <p>A larger buffer size can improve performance by reducing the frequency of storage
     * operations, but may increase memory usage and potential data loss on failure.
     *
     * @param bufferSize The buffer size (number of events)
     * @return This config instance for method chaining
     */
    public EventLogConfig bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * Sets the flush interval for periodically writing buffered events to storage.
     *
     * @param flushIntervalMs The flush interval in milliseconds
     * @return This config instance for method chaining
     */
    public EventLogConfig flushInterval(long flushIntervalMs) {
        this.flushIntervalMs = flushIntervalMs;
        return this;
    }

    // Getters
    public EventStorage getStorage() {
        return storage;
    }

    public EventFilter getFilter() {
        return filter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public long getFlushIntervalMs() {
        return flushIntervalMs;
    }

    /**
     * Creates a new EventLogConfig with default settings.
     *
     * @return A new EventLogConfig instance
     */
    public static EventLogConfig create() {
        return new EventLogConfig();
    }

    /**
     * Creates a disabled EventLogConfig.
     *
     * @return A disabled EventLogConfig instance
     */
    public static EventLogConfig disabled() {
        return new EventLogConfig().enabled(false);
    }
}
