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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins;

import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsPage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;
import org.uberfire.mocks.EventSourceMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ActionSetFactPluginTest {

    @Mock
    private PatternPage patternPage;

    @Mock
    private FieldPage fieldPage;

    @Mock
    private ValueOptionsPage<ActionSetFactPlugin> valueOptionsPage;

    @Mock
    private AdditionalInfoPage<ActionSetFactPlugin> additionalInfoPage;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private TranslationService translationService;

    @Mock
    private EventSourceMock<WizardPageStatusChangeEvent> changeEvent;

    @InjectMocks
    private ActionSetFactPlugin plugin = spy(new ActionSetFactPlugin());

    @Test
    public void testGetPages() throws Exception {
        doReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY).when(plugin).tableFormat();

        final List<WizardPage> pages = plugin.getPages();

        assertEquals(4,
                     pages.size());
    }

    @Test
    public void testInitializedPatternPage() throws Exception {
        plugin.initializedPatternPage();

        verify(patternPage).disableEntryPoint();
    }

    @Test
    public void testInitializedAdditionalInfoPage() throws Exception {
        plugin.initializedAdditionalInfoPage();

        verify(additionalInfoPage).init(plugin);
        verify(additionalInfoPage).enableHeader();
        verify(additionalInfoPage).enableHideColumn();
        verify(additionalInfoPage).enableLogicallyInsert();
    }

    @Test
    public void testInitializedValueOptionsPageWhenTableIsAnExtendedEntry() throws Exception {
        doReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY).when(plugin).tableFormat();

        plugin.initializedValueOptionsPage();

        verify(valueOptionsPage).init(plugin);
        verify(valueOptionsPage).enableValueList();
        verify(valueOptionsPage).enableDefaultValue();
        verify(valueOptionsPage).enableBinding();
    }

    @Test
    public void testInitializedValueOptionsPageWhenTableIsALimitedEntry() throws Exception {
        doReturn(GuidedDecisionTable52.TableFormat.LIMITED_ENTRY).when(plugin).tableFormat();

        plugin.initializedValueOptionsPage();

        verify(valueOptionsPage).init(plugin);
        verify(valueOptionsPage).enableLimitedValue();
        verify(valueOptionsPage).enableBinding();
    }

    @Test
    public void testGenerateColumn() throws Exception {
//        plugin.generateColumn();
    }

    @Test
    public void testEditingPattern() throws Exception {
//        plugin.editingPattern();
    }

    @Test
    public void testSetValueOptionsPageAsCompleted() throws Exception {
//        plugin.setRuleModellerPageAsCompleted();
    }

    @Test
    public void testIsValueOptionsPageCompleted() throws Exception {
//        plugin.isValueOptionsPageCompleted();
    }

    @Test
    public void testSetEditingPattern() throws Exception {
//        plugin.setEditingPattern(null);
    }

    @Test
    public void testGetEntryPointName() throws Exception {
//        plugin.getEntryPointName();
    }

    @Test
    public void testSetEntryPointName() throws Exception {
//        plugin.setEntryPointName("");
    }

    @Test
    public void testConstraintValue() throws Exception {
//        plugin.constraintValue();
    }

    @Test
    public void testGetFactType() throws Exception {
//        plugin.getFactType();
    }

    @Test
    public void testGetAccessor() throws Exception {
//        plugin.getAccessor();
    }

    @Test
    public void testFilterEnumFields() throws Exception {
//        plugin.filterEnumFields();
    }

    @Test
    public void testGetFactField() throws Exception {
//        plugin.getFactField();
    }

    @Test
    public void testSetFactField() throws Exception {
//        plugin.setFactField("");
    }

    @Test
    public void testEditingCol() throws Exception {
//        plugin.editingCol();
    }

    @Test
    public void testGetHeader() throws Exception {
//        plugin.getHeader();
    }

    @Test
    public void testSetHeader() throws Exception {
//        plugin.setHeader("");
    }

    @Test
    public void testSetInsertLogical() throws Exception {
//        plugin.setInsertLogical(true);
    }

    @Test
    public void testSetUpdate() throws Exception {
//        plugin.setUpdate(true);
    }

    @Test
    public void testGetValueList() throws Exception {
//        plugin.getValueList();
    }

    @Test
    public void testSetValueList() throws Exception {
//        plugin.setValueList("");
    }

    @Test
    public void testGetBinding() throws Exception {
//        plugin.getBinding();
    }

    @Test
    public void testSetBinding() throws Exception {
//        plugin.setBinding("");
    }

    @Test
    public void testTableFormat() throws Exception {
//        plugin.tableFormat();
    }

    @Test
    public void testDoesOperatorNeedValue() throws Exception {
//        plugin.doesOperatorNeedValue();
    }

    @Test
    public void testDoesOperatorAcceptValueList() throws Exception {
//        plugin.doesOperatorAcceptValueList();
    }

    @Test
    public void testDefaultValueWidget() throws Exception {
//        plugin.defaultValueWidget();
    }

    @Test
    public void testLimitedValueWidget() throws Exception {
//        plugin.limitedValueWidget();
    }
}
