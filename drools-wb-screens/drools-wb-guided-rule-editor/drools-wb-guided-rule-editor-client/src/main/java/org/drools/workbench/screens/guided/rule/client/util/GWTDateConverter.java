/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.guided.rule.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import org.drools.workbench.models.datamodel.oracle.DateConverter;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.kie.workbench.common.widgets.client.util.TimeZoneUtils;

/**
 * Convenience class to handle date conversion when running under GWT.
 */
public class GWTDateConverter
        implements
        DateConverter {

    private final static String DATE_FORMAT = ApplicationPreferences.getDroolsDateFormat();

    private final static DateTimeFormat FORMATTER = DateTimeFormat.getFormat( DATE_FORMAT );

    //Singleton
    private static GWTDateConverter INSTANCE;

    public static synchronized GWTDateConverter getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new GWTDateConverter();
        }
        return INSTANCE;
    }

    private GWTDateConverter() {
    }

    public String format( Date date ) {
        return FORMATTER.format( date, TimeZoneUtils.getTimeZone() );
    }

    public Date inTheCurrentTimeZone(Date date) {
        return FORMATTER.parse(format(date));
    }

    public Date parse( String text ) {
        return FORMATTER.parse( text );
    }

}
