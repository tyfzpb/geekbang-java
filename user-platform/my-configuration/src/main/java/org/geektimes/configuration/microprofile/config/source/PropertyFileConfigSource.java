/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.configuration.microprofile.config.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyFileConfigSource extends MapBasedConfigSource {
    private static final Logger LOG = Logger.getLogger(PropertyFileConfigSource.class.getName());
    private final Properties properties;

    public PropertyFileConfigSource(URL propertyFileUrl) {
        super(propertyFileUrl.toExternalForm(), 100);
        properties = loadProperties(propertyFileUrl);
    }


    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(this.properties);
    }

    private Properties loadProperties(URL url) {
        Properties props = new Properties();

        InputStream inputStream = null;
        try {
            inputStream = url.openStream();

            if (inputStream != null) {
                props.load(inputStream);
            }
        } catch (IOException e) {
            // don't return null on IOException
            LOG.log(Level.WARNING, "Unable to read URL " + url, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // no worries, means that the file is already closed
            }
        }

        return props;
    }

}
