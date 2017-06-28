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

package org.drools.workbench.screens.guided.dtable.client.widget.table;

import java.util.Map;
import java.util.Set;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GuidedDecisionTablePresenterAttributesAndMetadataTest extends BaseGuidedDecisionTablePresenterTest {

    @Captor
    private ArgumentCaptor<Set<String>> reservedAttributesCaptor;

    @Captor
    private ArgumentCaptor<Map<String, String>> callbackValueCaptor;

    @Before
    public void setup() {
        super.setup();

        dtPresenter.onAppendRow();
        dtPresenter.onAppendRow();
        dtPresenter.onAppendRow();
    }

    @Test
    public void isMetaDataUnique() {
        final MetadataCol52 metadata = new MetadataCol52();

        metadata.setMetadata("metadata");
        dtPresenter.getModel()
                .getMetadataCols()
                .add(metadata);

        assertFalse(dtPresenter.isMetaDataUnique("metadata"));
        assertTrue(dtPresenter.isMetaDataUnique("cheese"));
    }

    @Test
    public void appendAttributeColumn() throws ModelSynchronizer.MoveColumnVetoException {
        reset(modellerPresenter);

        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);

        dtPresenter.appendColumn(column);

        verify(synchronizer,
               times(1)).appendColumn(eq(column));
        verify(refreshAttributesPanelEvent,
               times(1)).fire(any(RefreshAttributesPanelEvent.class));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void appendMetadataColumn() throws ModelSynchronizer.MoveColumnVetoException {
        reset(modellerPresenter);

        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");

        dtPresenter.appendColumn(column);

        verify(synchronizer,
               times(1)).appendColumn(eq(column));
        verify(refreshMetaDataPanelEvent,
               times(1)).fire(any(RefreshMetaDataPanelEvent.class));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void deleteAttributeColumn() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        dtPresenter.deleteColumn(column);

        verify(synchronizer,
               times(1)).deleteColumn(eq(column));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void deleteMetadataColumn() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        dtPresenter.deleteColumn(column);

        verify(synchronizer,
               times(1)).deleteColumn(eq(column));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void updateAttributeColumn() throws ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        final AttributeCol52 update = new AttributeCol52();
        update.setAttribute(RuleAttributeWidget.ENABLED_ATTR);

        dtPresenter.updateColumn(column,
                                 update);

        verify(synchronizer,
               times(1)).updateColumn(eq(column),
                                      eq(update));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void updateMetadataColumn() throws ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        final MetadataCol52 update = new MetadataCol52();
        column.setMetadata("update");

        dtPresenter.updateColumn(column,
                                 update);

        verify(synchronizer,
               times(1)).updateColumn(eq(column),
                                      eq(update));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }
}
