/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.am.analytics.publisher.reporter;

import org.apache.log4j.Logger;
import org.wso2.am.analytics.publisher.exception.MetricCreationException;
import org.wso2.am.analytics.publisher.reporter.choreo.ChoreoAnalyticsMetricReporter;
import org.wso2.am.analytics.publisher.util.Constants;

import java.util.Map;

/**
 * Factory class to create {@link MetricReporter}. Based on the passed argument relevant concrete implementation will
 * be created and returned. Factory will behave in Singleton manner and if same type of instance is requested again
 * Factory will return earlier requested instance.
 */
public class MetricReporterFactory {
    private static final Logger log = Logger.getLogger(MetricReporterFactory.class);
    private static volatile MetricReporter reporterInstance;
    private static final MetricReporterFactory instance = new MetricReporterFactory();

    private MetricReporterFactory() {
        //private constructor
    }
    public MetricReporter createMetricReporter(String fullyQualifiedClassName, Map<String, String> properties)
            throws MetricCreationException {
        if (reporterInstance == null) {
            synchronized (this) {
                if (reporterInstance == null) {
                    if (fullyQualifiedClassName == null || fullyQualifiedClassName.isEmpty() ||
                            Constants.CHOREO_ANALYTICS_REPORTER_FQCN.equals(fullyQualifiedClassName)) {
                        reporterInstance = new ChoreoAnalyticsMetricReporter(properties);
                        return reporterInstance;
                    } else {
                        throw new MetricCreationException("Provided class name " + fullyQualifiedClassName +
                                                                  " is not supported.");
                    }
                } else {
                    return reporterInstance;
                }
            }
        } else {
            log.info("Metric Reporter of type " + reporterInstance.getClass().toString().replaceAll("[\r\n]", "") +
                             " is already created. Hence returning same instance");
            return reporterInstance;
        }
    }

    public static MetricReporterFactory getInstance() {
        return instance;
    }
}
