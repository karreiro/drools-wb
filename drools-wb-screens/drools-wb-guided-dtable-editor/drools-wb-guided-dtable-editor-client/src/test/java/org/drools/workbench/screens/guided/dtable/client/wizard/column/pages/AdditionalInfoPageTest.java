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

import com.google.gwtmockito.GwtMockitoTestRunner;
import junit.framework.TestCase;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.mockito.Mock;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class AdditionalInfoPageTest {

    @Mock
    private ConditionColumnWizardPlugin plugin;

    @Mock
    private ConditionCol52 editingCol;

    private AdditionalInfoPage page;

    @BeforeClass
    public static void setupPreferences() {
        final Map<String, String> preferences = new HashMap<String, String>() {{
            put(ApplicationPreferences.DATE_FORMAT,
                "dd/mm/yyyy");
        }};
        ApplicationPreferences.setUp(preferences);
    }

    @Before
    public void setup() {
        page = spy(new AdditionalInfoPage());

        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testNewHideColumnCheckBox() throws Exception {
        when(plugin.getEditingCol()).thenReturn(editingCol);
        when(editingCol.isHideColumn()).thenReturn(true);

        final CheckBox checkBox = page.newHideColumnCheckBox();

        verify(editingCol).isHideColumn();

        TestCase.assertNotNull(checkBox);
    }

    @Test
    public void testCanSetupHideColumnCheckBoxWhenEditingColIsNull() throws Exception {
        when(plugin.getEditingCol()).thenReturn(null);

        assertFalse(page.canSetupHideColumn());
    }

    @Test
    public void testCanSetupHideColumnCheckBoxWhenEditingColIsNotNull() throws Exception {
        when(plugin.getEditingCol()).thenReturn(editingCol);

        assertTrue(page.canSetupHideColumn());
    }
}
