/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.AttributeColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.drools.workbench.screens.guided.rule.client.resources.GuidedRuleEditorResources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.preferences.ApplicationPreferences;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class AttributeColumnPageTest {

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private AttributeColumnPlugin plugin;

    private AttributeColumnPage page;

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
        page = new AttributeColumnPage() {{
            presenter = AttributeColumnPageTest.this.presenter;
            plugin = AttributeColumnPageTest.this.plugin;
        }};
    }

    @Test
    public void testGetAttributesWhenThereAreReservedAttributes() {
        reservedAttributeNamesMock(RuleAttributeWidget.SALIENCE_ATTR,
                                   RuleAttributeWidget.AGENDA_GROUP_ATTR);

        final List<String> result = page.getAttributes();
        final List<String> expected = new ArrayList<String>() {{
            add(GuidedRuleEditorResources.CONSTANTS.Choose());
            add(RuleAttributeWidget.ENABLED_ATTR);
            add(RuleAttributeWidget.DATE_EFFECTIVE_ATTR);
            add(RuleAttributeWidget.DATE_EXPIRES_ATTR);
            add(RuleAttributeWidget.NO_LOOP_ATTR);
            add(RuleAttributeWidget.ACTIVATION_GROUP_ATTR);
            add(RuleAttributeWidget.DURATION_ATTR);
            add(RuleAttributeWidget.TIMER_ATTR);
            add(RuleAttributeWidget.CALENDARS_ATTR);
            add(RuleAttributeWidget.AUTO_FOCUS_ATTR);
            add(RuleAttributeWidget.LOCK_ON_ACTIVE_ATTR);
            add(RuleAttributeWidget.RULEFLOW_GROUP_ATTR);
            add(RuleAttributeWidget.DIALECT_ATTR);
            add(GuidedDecisionTable52.NEGATE_RULE_ATTR);
        }};

        assertEquals(expected,
                     result);
    }

    @Test
    public void testGetAttributesWhenThereIsNoReservedAttribute() {
        reservedAttributeNamesMock();

        final List<String> result = page.getAttributes();
        final List<String> expected = fakeRawAttributesList();

        assertEquals(expected,
                     result);
    }

    @Test
    public void testIsCompleteWhenAttributeIsNull() {
        when(plugin.getAttribute()).thenReturn(null);

        page.isComplete(Assert::assertFalse);
    }

    @Test
    public void testIsCompleteWhenAttributeIsBlank() {
        when(plugin.getAttribute()).thenReturn("");

        page.isComplete(Assert::assertFalse);
    }

    @Test
    public void testIsCompleteWhenAttributeIsNotNull() {
        when(plugin.getAttribute()).thenReturn(RuleAttributeWidget.SALIENCE_ATTR);

        page.isComplete(Assert::assertTrue);
    }

    private void reservedAttributeNamesMock(final String... attributes) {
        final Set<String> reservedAttributeNames = new HashSet<String>() {{
            for (String attribute : attributes) {
                add(attribute);
            }
        }};

        when(presenter.getReservedAttributeNames()).thenReturn(reservedAttributeNames);
    }

    private List<String> fakeRawAttributesList() {
        return new ArrayList<String>() {{
            add(GuidedRuleEditorResources.CONSTANTS.Choose());
            add(RuleAttributeWidget.SALIENCE_ATTR);
            add(RuleAttributeWidget.ENABLED_ATTR);
            add(RuleAttributeWidget.DATE_EFFECTIVE_ATTR);
            add(RuleAttributeWidget.DATE_EXPIRES_ATTR);
            add(RuleAttributeWidget.NO_LOOP_ATTR);
            add(RuleAttributeWidget.AGENDA_GROUP_ATTR);
            add(RuleAttributeWidget.ACTIVATION_GROUP_ATTR);
            add(RuleAttributeWidget.DURATION_ATTR);
            add(RuleAttributeWidget.TIMER_ATTR);
            add(RuleAttributeWidget.CALENDARS_ATTR);
            add(RuleAttributeWidget.AUTO_FOCUS_ATTR);
            add(RuleAttributeWidget.LOCK_ON_ACTIVE_ATTR);
            add(RuleAttributeWidget.RULEFLOW_GROUP_ATTR);
            add(RuleAttributeWidget.DIALECT_ATTR);
            add(GuidedDecisionTable52.NEGATE_RULE_ATTR);
        }};
    }
}
