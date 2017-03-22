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

import java.util.ArrayList;
import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.WorkItemPage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;
import org.uberfire.mocks.EventSourceMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class ActionWorkItemPluginTest {

    @Mock
    private AdditionalInfoPage<ActionWorkItemPlugin> additionalInfoPage;

    @Mock
    private WorkItemPage workItemPage;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private TranslationService translationService;

    @Mock
    private EventSourceMock<WizardPageStatusChangeEvent> changeEvent;

    @Mock
    private ActionWorkItemCol52 editingCol;

    @Mock
    private GuidedDecisionTable52 model;

    @InjectMocks
    private ActionWorkItemPlugin plugin = spy(new ActionWorkItemPlugin());

    @Test
    public void testSetWorkItemWithABlankValue() throws Exception {
        plugin.setWorkItem("");

        verify(editingCol).setWorkItemDefinition(null);
        verify(plugin).fireChangeEvent(workItemPage);
    }

    @Test
    public void testSetWorkItemWithAValidValue() throws Exception {
        final PortableWorkDefinition workDefinition = mock(PortableWorkDefinition.class);
        final String workItem = "workItem";

        doReturn(workDefinition).when(plugin).findWorkItemDefinition(workItem);

        plugin.setWorkItem(workItem);

        verify(editingCol).setWorkItemDefinition(workDefinition);
        verify(plugin).fireChangeEvent(workItemPage);
    }

    @Test
    public void testIsWorkItemSetWhenWorkItemDefinitionIsNotNull() throws Exception {
        final PortableWorkDefinition workDefinition = mock(PortableWorkDefinition.class);

        when(editingCol.getWorkItemDefinition()).thenReturn(workDefinition);

        final Boolean isWorkItemSet = plugin.isWorkItemSet();

        assertTrue(isWorkItemSet);
    }

    @Test
    public void testIsWorkItemSetWhenWorkItemDefinitionIsNull() throws Exception {
        when(editingCol.getWorkItemDefinition()).thenReturn(null);

        final Boolean isWorkItemSet = plugin.isWorkItemSet();

        assertFalse(isWorkItemSet);
    }

    @Test
    public void testSetHeader() throws Exception {
        final String header = "Header";

        plugin.setHeader(header);

        verify(editingCol).setHeader(header);
        verify(plugin).fireChangeEvent(additionalInfoPage);
    }

    @Test
    public void testGenerateColumnWhenItIsValid() throws Exception {
        when(plugin.isValid()).thenReturn(true);

        final Boolean success = plugin.generateColumn();

        assertTrue(success);
        verify(presenter).appendColumn(editingCol);
    }

    @Test
    public void testGenerateColumnWhenItIsNotValid() throws Exception {
        when(plugin.isValid()).thenReturn(false);

        final Boolean success = plugin.generateColumn();

        assertFalse(success);
        verify(presenter,
               never()).appendColumn(editingCol);
    }

    @Test
    public void testIsValidWhenItIsValid() throws Exception {
        final String header = "header";

        doReturn(true).when(plugin).unique(header);
        doReturn(header).when(editingCol).getHeader();

        final boolean isValid = plugin.isValid();

        assertTrue(isValid);
        verify(plugin,
               never()).showError(any());
    }

    @Test
    public void testIsValidWhenHeaderIsBlank() throws Exception {
        final String errorKey = GuidedDecisionTableErraiConstants.ActionWorkItemPlugin_YouMustEnterAColumnHeaderValueDescription;
        final String errorMessage = "YouMustEnterAColumnHeaderValueDescription";

        doReturn(errorMessage).when(translationService).format(errorKey);
        doReturn("").when(editingCol).getHeader();

        final boolean isValid = plugin.isValid();

        assertFalse(isValid);
        verify(plugin).showError(errorMessage);
    }

    @Test
    public void testIsValidWhenHeaderIsNotUnique() throws Exception {
        final String header = "header";
        final String errorKey = GuidedDecisionTableErraiConstants.ActionWorkItemPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother;
        final String errorMessage = "ThatColumnNameIsAlreadyInUsePleasePickAnother";

        doReturn(false).when(plugin).unique(header);
        doReturn(errorMessage).when(translationService).format(errorKey);
        doReturn(header).when(editingCol).getHeader();

        final boolean isValid = plugin.isValid();

        assertFalse(isValid);
        verify(plugin).showError(errorMessage);
    }

    @Test
    public void testUniqueWhenHeaderIsUnique() throws Exception {
        final ActionCol52 actionCol52 = mock(ActionCol52.class);
        final List<ActionCol52> actionCol52s = new ArrayList<ActionCol52>() {{
            add(actionCol52);
        }};

        when(actionCol52.getHeader()).thenReturn("header1");
        when(model.getActionCols()).thenReturn(actionCol52s);
        when(presenter.getModel()).thenReturn(model);

        final boolean unique = plugin.unique("header2");

        assertTrue(unique);
    }

    @Test
    public void testUniqueWhenHeaderIsNotUnique() throws Exception {
        final String header = "header";
        final ActionCol52 actionCol52 = mock(ActionCol52.class);
        final List<ActionCol52> actionCol52s = new ArrayList<ActionCol52>() {{
            add(actionCol52);
        }};

        when(actionCol52.getHeader()).thenReturn(header);
        when(model.getActionCols()).thenReturn(actionCol52s);
        when(presenter.getModel()).thenReturn(model);

        final boolean unique = plugin.unique(header);

        assertFalse(unique);
    }
}
