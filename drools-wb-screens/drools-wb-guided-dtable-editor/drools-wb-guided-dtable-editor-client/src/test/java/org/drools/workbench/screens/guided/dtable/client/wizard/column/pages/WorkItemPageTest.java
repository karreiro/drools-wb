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

import java.util.Collections;
import java.util.HashSet;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ActionWorkItemPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.workitems.WorkItemParametersWidget;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class WorkItemPageTest {

    @Mock
    private WorkItemPage.View view;

    @Mock
    private ActionWorkItemPlugin plugin;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private ActionWorkItemCol52 editingCol;

    @InjectMocks
    private WorkItemPage<ActionWorkItemPlugin> page = spy(new WorkItemPage<ActionWorkItemPlugin>());

    @BeforeClass
    public static void staticSetup() {
        // Prevent runtime GWT.create() error at 'content = new SimplePanel()'
        GWTMockUtilities.disarm();
    }

    @Test
    public void testGetWorkItems() throws Exception {
        page.getWorkItems();

        verify(presenter).getWorkItemDefinitions();
    }

    @Test
    public void testHasWorkItemsWhenItHasMoreThanZeroWorkItems() throws Exception {
        final HashSet<PortableWorkDefinition> workItemDefinitions = new HashSet<PortableWorkDefinition>() {{
            add(mock(PortableWorkDefinition.class));
        }};

        when(presenter.getWorkItemDefinitions()).thenReturn(workItemDefinitions);

        final boolean result = page.hasWorkItems();

        assertTrue(result);
    }

    @Test
    public void testHasWorkItemsWhenItHasZeroWorkItems() throws Exception {
        when(presenter.getWorkItemDefinitions()).thenReturn(Collections.emptySet());

        final boolean result = page.hasWorkItems();

        assertFalse(result);
    }

    @Test
    public void testSelectWorkItem() throws Exception {
        final String selectedValue = "selectedValue";

        when(plugin.editingCol()).thenReturn(editingCol);
        when(view.getSelectedWorkItem()).thenReturn(selectedValue);

        page.selectWorkItem();

        verify(plugin).setWorkItem(selectedValue);
        verify(page).showParameters();
    }

    @Test
    public void testShowParametersWhenItHasWorkItemDefinition() throws Exception {
        when(editingCol.getWorkItemDefinition()).thenReturn(mock(PortableWorkDefinition.class));
        when(plugin.editingCol()).thenReturn(editingCol);

        page.showParameters();

        verify(view).showParameters(any(WorkItemParametersWidget.class));
    }

    @Test
    public void testShowParametersWhenItDoesNotHaveWorkItemDefinition() throws Exception {
        when(editingCol.getWorkItemDefinition()).thenReturn(null);
        when(plugin.editingCol()).thenReturn(editingCol);

        page.showParameters();

        verify(view).hideParameters();
    }

    @Test
    public void testCurrentWorkItemWhenItHasWorkItemDefinition() throws Exception {
        final PortableWorkDefinition workDefinition = mock(PortableWorkDefinition.class);
        final String workDefinitionName = "workDefinitionName";

        when(workDefinition.getName()).thenReturn(workDefinitionName);
        when(editingCol.getWorkItemDefinition()).thenReturn(workDefinition);
        when(plugin.editingCol()).thenReturn(editingCol);

        final String currentWorkItem = page.currentWorkItem();

        assertEquals(workDefinitionName,
                     currentWorkItem);
    }

    @Test
    public void testCurrentWorkItemWhenItDoesNotHaveWorkItemDefinition() throws Exception {
        when(editingCol.getWorkItemDefinition()).thenReturn(null);
        when(plugin.editingCol()).thenReturn(editingCol);

        final String currentWorkItem = page.currentWorkItem();

        assertEquals("",
                     currentWorkItem);
    }
}
