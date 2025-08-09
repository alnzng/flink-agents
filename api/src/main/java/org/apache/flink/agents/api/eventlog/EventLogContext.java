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

import java.time.Instant;
import java.util.Map;

/**
 * Context information associated with an event being logged.
 *
 * <p>This class provides additional metadata about the event processing context, such as timing
 * information, agent details, and custom properties.
 */
public class EventLogContext {
    private final Instant timestamp;
    private final String agentName;
    private final String actionName;
    private final Object key;
    private final Map<String, Object> customProperties;

    public EventLogContext(
            Instant timestamp,
            String agentName,
            String actionName,
            Object key,
            Map<String, Object> customProperties) {
        this.timestamp = timestamp;
        this.agentName = agentName;
        this.actionName = actionName;
        this.key = key;
        this.customProperties = customProperties;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getActionName() {
        return actionName;
    }

    public Object getKey() {
        return key;
    }

    public Map<String, Object> getCustomProperties() {
        return customProperties;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Instant timestamp = Instant.now();
        private String agentName;
        private String actionName;
        private Object key;
        private Map<String, Object> customProperties;

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder agentName(String agentName) {
            this.agentName = agentName;
            return this;
        }

        public Builder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public Builder key(Object key) {
            this.key = key;
            return this;
        }

        public Builder customProperties(Map<String, Object> customProperties) {
            this.customProperties = customProperties;
            return this;
        }

        public EventLogContext build() {
            return new EventLogContext(timestamp, agentName, actionName, key, customProperties);
        }
    }
}
