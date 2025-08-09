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

package org.apache.flink.agents.api.eventlistener;

import org.apache.flink.agents.api.Event;
import org.apache.flink.agents.api.eventlog.EventFilter;
import org.apache.flink.agents.api.eventlog.EventLogContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry for managing event listeners with filtering capabilities.
 *
 * <p>This class manages a collection of event listeners and their associated filters, providing
 * methods to add listeners and notify them of processed events. It ensures that listeners are only
 * notified of events that pass their associated filters.
 */
public class EventListenerRegistry implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(EventListenerRegistry.class);

    private final List<FilteredEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Adds an event listener that will be notified of all events.
     *
     * @param listener The event listener to add
     */
    public void addListener(EventListener listener) {
        addListener(listener, EventFilter.ACCEPT_ALL);
    }

    /**
     * Adds an event listener with a specific filter.
     *
     * <p>The listener will only be notified of events that pass the provided filter.
     *
     * @param listener The event listener to add
     * @param filter The filter to determine which events this listener should receive
     */
    public void addListener(EventListener listener, EventFilter filter) {
        listeners.add(new FilteredEventListener(listener, filter));
    }

    /**
     * Removes an event listener from the registry.
     *
     * @param listener The event listener to remove
     * @return true if the listener was found and removed, false otherwise
     */
    public boolean removeListener(EventListener listener) {
        return listeners.removeIf(filtered -> filtered.listener.equals(listener));
    }

    /**
     * Notifies all registered listeners about a successfully processed event.
     *
     * <p>Only listeners whose filters accept the event will be notified. Any exceptions thrown by
     * listeners will be caught and logged to prevent disruption of the main processing flow.
     *
     * @param event The event that was processed
     * @param context The context associated with the event processing
     * @param result The result of the event processing
     */
    public void notifyEventProcessed(Event event, EventLogContext context, EventProcessingResult result) {
        for (FilteredEventListener filtered : listeners) {
            if (filtered.filter.accept(event, context)) {
                try {
                    filtered.listener.onEventProcessed(event, context, result);
                } catch (Exception e) {
                    LOG.warn("Event listener {} threw exception while processing event {}: {}", 
                            filtered.listener.getClass().getSimpleName(), event.getId(), e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners about a failed event processing.
     *
     * <p>Only listeners whose filters accept the event will be notified. Any exceptions thrown by
     * listeners will be caught and logged.
     *
     * @param event The event that failed to process
     * @param context The context associated with the event processing
     * @param exception The exception that caused the processing failure
     */
    public void notifyEventProcessingFailed(Event event, EventLogContext context, Exception exception) {
        for (FilteredEventListener filtered : listeners) {
            if (filtered.filter.accept(event, context)) {
                try {
                    filtered.listener.onEventProcessingFailed(event, context, exception);
                } catch (Exception e) {
                    LOG.warn("Event listener {} threw exception while handling processing failure for event {}: {}", 
                            filtered.listener.getClass().getSimpleName(), event.getId(), e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Gets the number of registered listeners.
     *
     * @return The number of listeners in the registry
     */
    public int getListenerCount() {
        return listeners.size();
    }

    /**
     * Gets a copy of all registered listeners (without their filters).
     *
     * @return A list of all registered event listeners
     */
    public List<EventListener> getListeners() {
        List<EventListener> result = new ArrayList<>();
        for (FilteredEventListener filtered : listeners) {
            result.add(filtered.listener);
        }
        return result;
    }

    /**
     * Clears all registered listeners.
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Internal class to pair a listener with its filter.
     */
    private static class FilteredEventListener implements Serializable {
        private static final long serialVersionUID = 1L;
        
        final EventListener listener;
        final EventFilter filter;

        FilteredEventListener(EventListener listener, EventFilter filter) {
            this.listener = listener;
            this.filter = filter;
        }
    }
}
