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

import org.apache.flink.agents.api.eventlistener.EventListener;
import org.apache.flink.agents.api.eventlistener.EventListenerRegistry;

import java.io.Serializable;

/**
 * Configuration for event listener capabilities.
 *
 * <p>This class provides a fluent API for configuring event listeners that will be notified when
 * events are processed by the agent system.
 */
public class EventListenerConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EventListenerRegistry registry = new EventListenerRegistry();
    private boolean enabled = true;

    /**
     * Adds an event listener that will be notified of all events.
     *
     * @param listener The event listener to add
     * @return This config instance for method chaining
     */
    public EventListenerConfig addListener(EventListener listener) {
        registry.addListener(listener);
        return this;
    }

    /**
     * Enables or disables event listeners.
     *
     * @param enabled true to enable event listeners, false to disable
     * @return This config instance for method chaining
     */
    public EventListenerConfig enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Gets the event listener registry.
     *
     * @return The registry containing all configured listeners
     */
    public EventListenerRegistry getRegistry() {
        return registry;
    }

    /**
     * Checks if event listeners are enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Creates a new EventListenerConfig with default settings.
     *
     * @return A new EventListenerConfig instance
     */
    public static EventListenerConfig create() {
        return new EventListenerConfig();
    }

    /**
     * Creates a disabled EventListenerConfig.
     *
     * @return A disabled EventListenerConfig instance
     */
    public static EventListenerConfig disabled() {
        return new EventListenerConfig().enabled(false);
    }
}
