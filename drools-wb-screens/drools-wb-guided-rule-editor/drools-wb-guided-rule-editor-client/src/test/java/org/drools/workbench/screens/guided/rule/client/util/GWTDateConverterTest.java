/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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
import java.util.HashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.kie.workbench.common.widgets.client.util.TimeZoneUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest({DateTimeFormat.class, TimeZoneUtils.class})
@RunWith(PowerMockRunner.class)
public class GWTDateConverterTest {

    @BeforeClass
    public static void setupApplicationPreferences() {
        ApplicationPreferences.setUp(new HashMap<String, String>() {{
            put(ApplicationPreferences.DATE_FORMAT, "dd-MM-yyyy");
        }});
    }

    @Test
    public void testFormat() {

        final Date date = new Date(1525122000000L);
        final DateTimeFormat format = mock(DateTimeFormat.class);
        final TimeZone timeZone = mock(TimeZone.class);

        mockStatic(DateTimeFormat.class);
        when(DateTimeFormat.getFormat(anyString())).thenReturn(format);

        mockStatic(TimeZoneUtils.class);
        when(TimeZoneUtils.getTimeZone()).thenReturn(timeZone);

        final GWTDateConverter converter = spy(GWTDateConverter.getInstance());

        converter.format(date);
        verify(format).format(date, timeZone);
    }
}
