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


public class SystemEnvConfigSource extends MapBasedConfigSource {

    public SystemEnvConfigSource() {
        super("Operation System Environment Variables", 300);
    }


    @Override
    public void prepareConfigData(Map configData){
        configData.putAll(System.getenv());
    }


    @Override
    public String getValue(String key) {
        String val = getProperties().get(key);
        if (val == null) {
            key = replaceNonPosixEnvChars(key);
            val = getProperties().get(key);
        }
        if (val == null) {
            key = key.toUpperCase();
            val = getProperties().get(key);
        }
        if (val == null) {
            val = getProperties().get(key);
        }

        return val;
    }

    private String replaceNonPosixEnvChars(String key) {
        return key.replaceAll("[^A-Za-z0-9]", "_");
    }
}
