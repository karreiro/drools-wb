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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.RuleModellerPage;
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
@WithClassesToStub(BRLRuleModel.class)
public class BRLActionColumnPluginTest {

    @Mock
    private RuleModellerPage ruleModellerPage;

    @Mock
    private AdditionalInfoPage<BRLActionColumnPlugin> additionalInfoPage;

    @Mock
    private EventSourceMock<WizardPageStatusChangeEvent> changeEvent;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private GuidedDecisionTable52 model;

    @Mock
    private TranslationService translationService;

    @Mock
    private HandlerRegistration registration;

    @Mock
    private NewGuidedDecisionTableColumnWizard wizard;

    @Mock
    private BRLActionColumn editingCol;

    @InjectMocks
    private BRLActionColumnPlugin plugin = spy(new BRLActionColumnPlugin(ruleModellerPage,
                                                                         additionalInfoPage,
                                                                         changeEvent,
                                                                         translationService));

    @Test
    public void testGetTitle() throws Exception {
        final String errorKey = GuidedDecisionTableErraiConstants.BRLActionColumnPlugin_AddActionBRL;
        final String errorMessage = "Title";

        when(translationService.format(errorKey)).thenReturn(errorMessage);

        final String title = plugin.getTitle();

        assertEquals(errorMessage,
                     title);
    }

    @Test
    public void testInit() throws Exception {
        doReturn(GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY).when(plugin).tableFormat();
        doReturn(mock(EventBus.class)).when(presenter).getEventBus();
        doReturn(presenter).when(wizard).getPresenter();

        plugin.init(wizard);

        verify(plugin).setupEditingCol();
        verify(plugin).setupRuleModellerEvents();
    }

    @Test
    public void testGetPages() throws Exception {
        final List<WizardPage> pages = plugin.getPages();

        assertEquals(2,
                     pages.size());
    }

    @Test
    public void testOnClose() throws Exception {
        plugin.onClose();

        verify(plugin).teardownRuleModellerEvents();
    }

    @Test
    public void testGenerateColumnWhenHeaderIsBlank() throws Exception {
        doReturn("").when(editingCol).getHeader();

        final Boolean success = plugin.generateColumn();

        assertFalse(success);

        verify(translationService).format(GuidedDecisionTableErraiConstants.BRLActionColumnPlugin_YouMustEnterAColumnHeaderValueDescription);
    }

    @Test
    public void testGenerateColumnWhenHeaderIsNotUnique() throws Exception {
        final String header = "header";
        final ActionCol52 oldActionCol52 = mock(ActionCol52.class);
        final GuidedDecisionTable52 model = mock(GuidedDecisionTable52.class);
        final ArrayList<ActionCol52> actionCol52s = new ArrayList<ActionCol52>() {{
            add(oldActionCol52);
        }};

        when(oldActionCol52.getHeader()).thenReturn(header);
        when(model.getActionCols()).thenReturn(actionCol52s);
        when(presenter.getModel()).thenReturn(model);
        when(editingCol.getHeader()).thenReturn(header);

        final Boolean success = plugin.generateColumn();

        assertFalse(success);

        verify(translationService).format(GuidedDecisionTableErraiConstants.BRLActionColumnPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother);
    }

    @Test
    public void testGenerateColumnWhenHeaderIsValid() throws Exception {
        final String header = "header";
        final GuidedDecisionTable52 model = mock(GuidedDecisionTable52.class);
        final ArrayList<ActionCol52> actionCol52s = new ArrayList<>();

        when(model.getActionCols()).thenReturn(actionCol52s);
        when(presenter.getModel()).thenReturn(model);
        when(editingCol.getHeader()).thenReturn(header);

        final Boolean success = plugin.generateColumn();

        assertTrue(success);

        verify(plugin).getDefinedVariables(any());
        verify(editingCol).setDefinition(any());
        verify(presenter).appendColumn(editingCol);
        verify(translationService,
               never()).format(any());
    }

    @Test
    public void testGetHeader() throws Exception {
        plugin.getHeader();

        verify(editingCol).getHeader();
    }

    @Test
    public void testSetHeader() throws Exception {
        final String header = "header";

        plugin.setHeader(header);

        verify(editingCol).setHeader(header);
        verify(plugin).fireChangeEvent(additionalInfoPage);
    }

    @Test
    public void testGetRuleModel() throws Exception {
        assertNotNull(plugin.getRuleModel());
    }

    @Test
    public void testSetRuleModellerPageAsCompletedWhenItIsCompleted() throws Exception {
        doReturn(true).when(plugin).isRuleModellerPageCompleted();

        plugin.setRuleModellerPageAsCompleted();

        verify(plugin,
               never()).setRuleModellerPageCompleted();
        verify(plugin,
               never()).fireChangeEvent(ruleModellerPage);
    }

    @Test
    public void testSetRuleModellerPageAsCompletedWhenItIsNotCompleted() throws Exception {
        doReturn(false).when(plugin).isRuleModellerPageCompleted();

        plugin.setRuleModellerPageAsCompleted();

        verify(plugin).setRuleModellerPageCompleted();
        verify(plugin).fireChangeEvent(ruleModellerPage);
    }

    @Test
    public void testTableFormat() throws Exception {
        final GuidedDecisionTable52.TableFormat expectedTableFormat = GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY;

        doReturn(expectedTableFormat).when(model).getTableFormat();
        doReturn(model).when(presenter).getModel();

        final GuidedDecisionTable52.TableFormat actualTableFormat = plugin.tableFormat();

        assertEquals(expectedTableFormat,
                     actualTableFormat);
    }
}
