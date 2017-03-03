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

import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.MetaDataColumnPage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;
import org.uberfire.mocks.EventSourceMock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetaDataColumnPluginTest {

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private MetaDataColumnPage page;

    @Mock
    private TranslationService translationService;

    @Mock
    private EventSourceMock<WizardPageStatusChangeEvent> changeEvent;

    @InjectMocks
    private MetaDataColumnPlugin plugin = new MetaDataColumnPlugin();

    @Test
    public void testGetPages() throws Exception {
        assertEquals(1,
                     plugin.getPages().size());
    }

    @Test
    public void testGenerateColumnWhenMetaDataIsNotValid() throws Exception {
        final MetadataCol52 column = mock(MetadataCol52.class);

        final Boolean success = plugin.generateColumn();

        verify(presenter,
               never()).appendColumn(column);

        assertFalse(success);
    }

    @Test
    public void testGenerateColumnWhenMetaDataIsValid() throws Exception {
        final String metaData = "metaData";
        final ArgumentCaptor<MetadataCol52> colCaptor = ArgumentCaptor.forClass(MetadataCol52.class);

        plugin.setMetaData(metaData);

        when(presenter.isMetaDataUnique(eq(metaData))).thenReturn(true);

        final Boolean success = plugin.generateColumn();

        verify(presenter).appendColumn(colCaptor.capture());

        assertTrue(success);
        assertTrue(colCaptor.getValue().isHideColumn());
        assertEquals(metaData,
                     colCaptor.getValue().getMetadata());
    }

    @Test
    public void testIsValidMetadataWhenMetadataIsNull() throws Exception {
        plugin.setMetaData(null);

        final boolean isValid = plugin.isValidMetadata();

        verify(page).emptyMetadataError();

        assertFalse(isValid);
    }

    @Test
    public void testIsValidMetadataWhenMetadataIsBlank() throws Exception {
        plugin.setMetaData("");

        final boolean isValid = plugin.isValidMetadata();

        verify(page).emptyMetadataError();

        assertFalse(isValid);
    }

    @Test
    public void testIsValidMetadataWhenMetadataIsNotUnique() throws Exception {
        plugin.setMetaData("metaData");

        when(presenter.isMetaDataUnique(eq("metaData"))).thenReturn(false);

        final boolean isValid = plugin.isValidMetadata();

        verify(page).columnNameIsAlreadyInUseError();

        assertFalse(isValid);
    }

    @Test
    public void testIsValidMetadataWhenMetadataIsValid() throws Exception {
        plugin.setMetaData("metaData");

        when(presenter.isMetaDataUnique(eq("metaData"))).thenReturn(true);

        final boolean isValid = plugin.isValidMetadata();

        assertTrue(isValid);
    }

    @Test
    public void testSetMetaData() throws Exception {
        final String metaData = "metaData";

        plugin.setMetaData(metaData);

        assertEquals(metaData,
                     plugin.getMetaData());
        verify(changeEvent).fire(any(WizardPageStatusChangeEvent.class));
    }
}
