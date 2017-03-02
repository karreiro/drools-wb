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

import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AttributeColumnPage;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AttributeColumnPluginTest {

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private AttributeColumnPage page;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private AttributeColumnPlugin plugin = new AttributeColumnPlugin();

    @Test
    public void testGetTitle() throws Exception {
        plugin.getTitle();

        verify(translationService).format(GuidedDecisionTableErraiConstants.AttributeColumnPlugin_AddNewAttributeColumn);
    }

    @Test
    public void testGetPages() throws Exception {
        assertEquals(1,
                     plugin.getPages().size());
    }

    @Test
    public void testGenerateColumn() throws Exception {
        final String attributeName = RuleAttributeWidget.DIALECT_ATTR;
        final ArgumentCaptor<AttributeCol52> colCaptor = ArgumentCaptor.forClass(AttributeCol52.class);

        when(page.getAttributeName()).thenReturn(attributeName);

        plugin.generateColumn();

        verify(presenter).appendColumn(colCaptor.capture());

        assertEquals(attributeName,
                     colCaptor.getValue().getAttribute());
    }
}
