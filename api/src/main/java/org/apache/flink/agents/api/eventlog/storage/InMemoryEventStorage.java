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

package org.apache.flink.agents.api.eventlog.storage;

import org.apache.flink.agents.api.Event;
import org.apache.flink.agents.api.eventlog.EventLogContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory implementation of EventStorage for testing and development.
 *
 * <p>This storage keeps events in memory and provides access to them for inspection. It's suitable
 * for development, testing, and scenarios where events don't need to persist beyond the application
 * lifecycle.
 */
public class InMemoryEventStorage implements EventStorage {

    private static final long serialVersionUID = 1L;

    private final List<EventRecord> events = Collections.synchronizedList(new ArrayList<>());
    private final int maxEvents;

    public InMemoryEventStorage() {
        this(10000); // Default max events
    }

    public InMemoryEventStorage(int maxEvents) {
        this.maxEvents = maxEvents;
    }

    @Override
    public void store(Event event, EventLogContext context) {
        EventRecord record = new EventRecord(event, context);
        events.add(record);
        
        // Simple circular buffer implementation
        if (events.size() > maxEvents) {
            events.remove(0);
        }
    }

    @Override
    public void flush() {
        // No-op for in-memory storage
    }

    @Override
    public void open() {
        // No initialization needed for in-memory storage
    }

    @Override
    public void close() {
        events.clear();
    }

    /**
     * Gets all stored events. This method is primarily for testing and debugging.
     *
     * @return A copy of all stored event records
     */
    public List<EventRecord> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Gets the number of stored events.
     *
     * @return The number of events currently stored
     */
    public int getEventCount() {
        return events.size();
    }

    /**
     * Clears all stored events.
     */
    public void clear() {
        events.clear();
    }

    /**
     * Record containing an event and its associated context.
     */
    public static class EventRecord {
        private final Event event;
        private final EventLogContext context;

        public EventRecord(Event event, EventLogContext context) {
            this.event = event;
            this.context = context;
        }

        public Event getEvent() {
            return event;
        }

        public EventLogContext getContext() {
            return context;
        }
    }
}
