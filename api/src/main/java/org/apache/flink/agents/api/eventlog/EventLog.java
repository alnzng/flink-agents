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

package org.apache.flink.agents.api.eventlog;

import org.apache.flink.agents.api.Event;

/**
 * Interface for event logging functionality.
 *
 * <p>EventLog provides persistent storage of events for auditing, debugging, replay, and relay
 * purposes. It supports filtering events by type and custom attributes.
 */
public interface EventLog {

    /**
     * Logs an event to the configured storage backend.
     *
     * @param event The event to be logged
     * @param context Additional context information for the event
     */
    void logEvent(Event event, EventLogContext context);

    /**
     * Checks if an event should be logged based on the configured filters.
     *
     * @param event The event to check
     * @param context Additional context information
     * @return true if the event should be logged, false otherwise
     */
    boolean shouldLogEvent(Event event, EventLogContext context);

    /**
     * Flushes any buffered events to the storage backend.
     *
     * <p>This method ensures that all pending events are persisted to storage. It should be called
     * during checkpointing or shutdown to prevent data loss.
     */
    void flush();
}
