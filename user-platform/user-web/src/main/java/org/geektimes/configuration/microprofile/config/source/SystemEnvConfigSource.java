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


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SystemEnvConfigSource extends BaseConfigSource {
    private final Map<String, String> configValues;
    private final Map<String, String> uppercasePosixValues;

    public SystemEnvConfigSource() {
        uppercasePosixValues = new HashMap<>();
        configValues = System.getenv();
        initOrdinal(300);

        for (Map.Entry<String, String> e : configValues.entrySet()) {
            String originalKey = e.getKey();
            String posixKey = replaceNonPosixEnvChars(originalKey).toUpperCase();
            if (!originalKey.equals(posixKey)) {
                uppercasePosixValues.put(posixKey, e.getValue());
            }
        }
    }

    @Override
    public String getName() {
        return "system_env";
    }


    @Override
    public Set<String> getPropertyNames() {
        return configValues.keySet();
    }

    @Override
    public String getValue(String key) {
        String val = configValues.get(key);
        if (val == null) {
            key = replaceNonPosixEnvChars(key);
            val = configValues.get(key);
        }
        if (val == null) {
            key = key.toUpperCase();
            val = configValues.get(key);
        }
        if (val == null) {
            val = uppercasePosixValues.get(key);
        }

        return val;
    }

    private String replaceNonPosixEnvChars(String key) {
        return key.replaceAll("[^A-Za-z0-9]", "_");
    }
}
