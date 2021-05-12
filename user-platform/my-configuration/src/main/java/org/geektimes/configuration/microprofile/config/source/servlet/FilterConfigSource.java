/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.Map;

import static java.lang.String.format;

/**
 * The {@link ConfigSource} implementation based on {@link FilterConfig}
 *
 * @since 1.0.0
 * Date : 2021-04-28
 */
public class FilterConfigSource extends MapBasedConfigSource {

    private Map<String,String> parameters;

    public FilterConfigSource(FilterConfig filterConfig) {
        super(format("Filter[name:%s] Init Parameters", filterConfig.getFilterName()), 550);
        getParameters(filterConfig);
    }

    private void getParameters(FilterConfig filterConfig){
        Enumeration<String> parameterNames = filterConfig.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, filterConfig.getInitParameter(parameterName));
        }
    }


    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        this.parameters = configData;
    }
}
