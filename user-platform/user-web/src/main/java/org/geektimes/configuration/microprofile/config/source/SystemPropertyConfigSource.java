/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.geektimes.configuration.microprofile.config.source;

import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.valueOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;


public class SystemPropertyConfigSource extends BaseConfigSource {
    private static final String COPY_PROPERTY = "org.geektimes.configuration.microprofile.config.source.SystemPropertyConfigSource.copy";
    private final Map<String, String> instance;

    public SystemPropertyConfigSource() {
        this(valueOf(System.getProperty(COPY_PROPERTY, "true")));
    }

    public SystemPropertyConfigSource(boolean copy) {
        instance = copy ?
                System.getProperties().stringPropertyNames().stream().collect(toMap(identity(), System::getProperty)) :
                (Map) System.getProperties();
        initOrdinal(400);
    }


    @Override
    public Set<String> getPropertyNames() {
        return instance.keySet();
    }

    @Override
    public String getValue(String key) {
        return instance.get(key);
    }

    @Override
    public String getName() {
        return "system-properties";
    }
}
