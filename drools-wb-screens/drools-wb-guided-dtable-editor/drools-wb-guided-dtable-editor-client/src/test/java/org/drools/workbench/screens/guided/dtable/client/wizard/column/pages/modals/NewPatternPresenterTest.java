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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals;

import java.util.Arrays;
import java.util.List;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class NewPatternPresenterTest {

    @Mock
    private NewPatternPresenter.View view;

    @Mock
    private PatternPage patternPage;

    @Mock
    private GuidedDecisionTableView.Presenter decisionTablePresenter;

    @Mock
    private AsyncPackageDataModelOracle oracle;

    @Mock
    private ConditionColumnWizardPlugin plugin;

    private NewPatternPresenter presenter;

    @Before
    public void setup() {
        when(decisionTablePresenter.getDataModelOracle()).thenReturn(oracle);
        when(patternPage.presenter()).thenReturn(decisionTablePresenter);
        when(patternPage.plugin()).thenReturn(plugin);

        presenter = new NewPatternPresenter(view) {{
            init(patternPage);
        }};
    }

    @Test
    public void testGetFactTypes() throws Exception {
        final String[] fakeFactTypesArray = new String[]{"factType1", "factType2", "factType3"};
        final List<String> expectedFactTypes = Arrays.asList(fakeFactTypesArray);

        when(oracle.getFactTypes()).thenReturn(fakeFactTypesArray);

        final List<String> factTypes = presenter.getFactTypes();

        assertEquals(Arrays.asList(fakeFactTypesArray),
                     factTypes);
    }

    @Test
    public void testCancel() throws Exception {
        presenter.cancel();

        verify(view).hide();
    }

    @Test
    public void testAddPattern() throws Exception {
        when(view.isNegatePatternMatch()).thenReturn(true);

        presenter.addPattern();

        verify(plugin).setEditingPattern(any(Pattern52.class));
        verify(plugin).setEditingColFactField(null);
        verify(plugin).setEditingColOperator(null);
        verify(patternPage).updateNewPatternLabel();
        verify(view).hide();
    }
}
