/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class AdditionalInfoPageTest {

    @Mock
    private ConditionColumnPlugin plugin;

    @Mock
    private ConditionCol52 editingCol;

    @Mock
    private AdditionalInfoPage.View view;

    @InjectMocks
    private AdditionalInfoPage<ConditionColumnPlugin> page = spy(new AdditionalInfoPage<>());

    @BeforeClass
    public static void setupPreferences() {
        final Map<String, String> preferences = new HashMap<String, String>() {{
            put(ApplicationPreferences.DATE_FORMAT,
                "dd/mm/yyyy");
        }};
        ApplicationPreferences.setUp(preferences);

        // Prevent runtime GWT.create() error at 'content = new SimplePanel()'
        GWTMockUtilities.disarm();
    }

    @Before
    public void setup() {
        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testIsCompleteWhenHeaderIsNotEnabled() throws Exception {
        page.isComplete(Assert::assertFalse);
    }

    @Test
    public void testIsCompleteWhenHeaderIsEnabledButNotCompleted() throws Exception {
        when(plugin.editingCol()).thenReturn(mock(ConditionCol52.class));

        page.enableHeader();

        page.isComplete(Assert::assertFalse);
    }

    @Test
    public void testIsCompleteWhenHeaderIsEnabledAndCompleted() throws Exception {
        when(plugin.getHeader()).thenReturn("header");

        page.enableHeader();

        page.isComplete(Assert::assertTrue);
    }

    @Test
    public void testGetHeader() throws Exception {
        page.getHeader();

        verify(plugin).getHeader();
    }

    @Test
    public void testSetHeader() throws Exception {
        page.setHeader("header");

        verify(plugin).setHeader(eq("header"));
    }

    @Test
    public void testNewHideColumnCheckBox() throws Exception {
        when(plugin.editingCol()).thenReturn(editingCol);
        when(editingCol.isHideColumn()).thenReturn(true);

        final CheckBox checkBox = page.newHideColumnCheckBox();

        verify(editingCol).isHideColumn();

        assertNotNull(checkBox);
    }

    @Test
    public void testSetupHeaderWhenHeaderIsEnabled() throws Exception {
        page.enableHeader();

        page.setupHeader();

        verify(view).showHeader();
    }

    @Test
    public void testSetupHeaderWhenHeaderIsNotEnabled() throws Exception {
        page.setupHeader();

        verify(view,
               never()).showHeader();
    }

    @Test
    public void testSetupHideColumnWhenHideColumnIsEnabled() throws Exception {
        when(plugin.editingCol()).thenReturn(editingCol);

        page.enableHideColumn();
        page.setupHideColumn();

        verify(view).showHideColumn(any(CheckBox.class));
    }

    @Test
    public void testSetupHideColumnWhenHideColumnIsNotEnabled() throws Exception {
        page.setupHideColumn();

        verify(view,
               never()).showHideColumn(any(CheckBox.class));
    }
}
