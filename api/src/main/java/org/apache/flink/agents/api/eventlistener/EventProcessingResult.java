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

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Represents the result of processing an event through the agent system.
 *
 * <p>This class contains information about what happened when an event was processed, including
 * any output events that were generated, timing information, and whether the processing was
 * successful.
 */
public class EventProcessingResult {
    private final List<Event> outputEvents;
    private final boolean success;
    private final Duration processingDuration;
    private final Instant startTime;
    private final Instant endTime;
    private final String actionName;

    private EventProcessingResult(
            List<Event> outputEvents,
            boolean success,
            Duration processingDuration,
            Instant startTime,
            Instant endTime,
            String actionName) {
        this.outputEvents = outputEvents != null ? outputEvents : Collections.emptyList();
        this.success = success;
        this.processingDuration = processingDuration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.actionName = actionName;
    }

    /**
     * Gets the events that were generated as a result of processing the input event.
     *
     * @return A list of output events, or empty list if no events were generated
     */
    public List<Event> getOutputEvents() {
        return outputEvents;
    }

    /**
     * Indicates whether the event processing was successful.
     *
     * @return true if processing was successful, false if an error occurred
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the duration of event processing.
     *
     * @return The time taken to process the event
     */
    public Duration getProcessingDuration() {
        return processingDuration;
    }

    /**
     * Gets the start time of event processing.
     *
     * @return The instant when event processing started
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of event processing.
     *
     * @return The instant when event processing completed
     */
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * Gets the name of the action that processed the event.
     *
     * @return The action name, or null if not available
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Creates a builder for constructing EventProcessingResult instances.
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a successful processing result.
     *
     * @param outputEvents The events generated during processing
     * @param processingDuration The time taken to process the event
     * @param actionName The name of the action that processed the event
     * @return A successful EventProcessingResult
     */
    public static EventProcessingResult success(
            List<Event> outputEvents, Duration processingDuration, String actionName) {
        Instant now = Instant.now();
        return new EventProcessingResult(
                outputEvents, true, processingDuration, now.minus(processingDuration), now, actionName);
    }

    /**
     * Creates a failed processing result.
     *
     * @param processingDuration The time taken before the failure occurred
     * @param actionName The name of the action that was processing the event
     * @return A failed EventProcessingResult
     */
    public static EventProcessingResult failure(Duration processingDuration, String actionName) {
        Instant now = Instant.now();
        return new EventProcessingResult(
                Collections.emptyList(), false, processingDuration, now.minus(processingDuration), now, actionName);
    }

    /**
     * Builder for creating EventProcessingResult instances.
     */
    public static class Builder {
        private List<Event> outputEvents;
        private boolean success = true;
        private Duration processingDuration;
        private Instant startTime;
        private Instant endTime;
        private String actionName;

        public Builder outputEvents(List<Event> outputEvents) {
            this.outputEvents = outputEvents;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder processingDuration(Duration processingDuration) {
            this.processingDuration = processingDuration;
            return this;
        }

        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public EventProcessingResult build() {
            if (processingDuration == null && startTime != null && endTime != null) {
                processingDuration = Duration.between(startTime, endTime);
            }
            return new EventProcessingResult(
                    outputEvents, success, processingDuration, startTime, endTime, actionName);
        }
    }
}
