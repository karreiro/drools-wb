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

import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.CalculationTypePage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.OperatorPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsPage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;
import org.uberfire.mocks.EventSourceMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConditionColumnPluginTest {

    @Mock
    private PatternPage<ConditionColumnPlugin> patternPage;

    @Mock
    private CalculationTypePage calculationTypePage;

    @Mock
    private FieldPage fieldPage;

    @Mock
    private OperatorPage operatorPage;

    @Mock
    private AdditionalInfoPage<ConditionColumnPlugin> additionalInfoPage;

    @Mock
    private ValueOptionsPage valueOptionsPage;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private TranslationService translationService;

    @Mock
    private EventSourceMock<WizardPageStatusChangeEvent> changeEvent;

    @InjectMocks
    private ConditionColumnPlugin plugin = spy(new ConditionColumnPlugin());

    @Test
    public void testGetPagesWhenItIsAnExtendedEntryTable() throws Exception {
        final GuidedDecisionTable52 model = mock(GuidedDecisionTable52.class);

        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY);
        when(presenter.getModel()).thenReturn(model);

        final List<WizardPage> pages = plugin.getPages();

        assertTrue(pages.stream().anyMatch(a -> a instanceof CalculationTypePage));
        assertEquals(6,
                     pages.size());
    }

    @Test
    public void testGetPagesWhenItIsALimitedEntryTable() throws Exception {
        final GuidedDecisionTable52 model = mock(GuidedDecisionTable52.class);

        when(model.getTableFormat()).thenReturn(GuidedDecisionTable52.TableFormat.LIMITED_ENTRY);
        when(presenter.getModel()).thenReturn(model);

        final List<WizardPage> pages = plugin.getPages();

        assertFalse(pages.stream().anyMatch(a -> a instanceof CalculationTypePage));
        assertEquals(5,
                     pages.size());
    }

    @Test
    public void generateColumn() throws Exception {

    }

    @Test
    public void editingPattern() throws Exception {

    }

    @Test
    public void setEditingPattern() throws Exception {

    }

    @Test
    public void getEntryPointName() throws Exception {

    }

    @Test
    public void setEntryPointName() throws Exception {

    }

    @Test
    public void editingCol() throws Exception {

    }

    @Test
    public void setHeader() throws Exception {

    }

    @Test
    public void getFactField() throws Exception {

    }

    @Test
    public void setFactField() throws Exception {

    }

    @Test
    public void setOperator() throws Exception {

    }

    @Test
    public void constraintValue() throws Exception {

    }

    @Test
    public void setConstraintValue() throws Exception {

    }

    @Test
    public void getFactType() throws Exception {

    }

    @Test
    public void setValueOptionsPageAsCompleted() throws Exception {

    }

    @Test
    public void isValueOptionsPageCompleted() throws Exception {

    }

    @Test
    public void setValueList() throws Exception {

    }
}
