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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals.NewPatternPresenter;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PatternPageTest {

    @Mock
    private PatternPage.View view;

    @Mock
    private NewPatternPresenter newPatternPresenter;

    @Mock
    private GuidedDecisionTable52 model;

    @Mock
    private ConditionColumnPlugin plugin;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @InjectMocks
    private PatternPage<ConditionColumnPlugin> page = spy(new PatternPage());

    @BeforeClass
    public static void staticSetup() {
        // Prevent runtime GWT.create() error at 'content = new SimplePanel()'
        GWTMockUtilities.disarm();
    }

    @Before
    public void setup() {
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(presenter.getModel()).thenReturn(model);
        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testUpdateNewPatternLabel() throws Exception {
        page.updateNewPatternLabel();

        verify(view).setup();
    }

    @Test
    public void testForEachPatternValues() throws Exception {
        final List<String> patternValues = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> {
            patternValues.add(patternValue);
        });

        assertEquals("factType1 boundName1 false",
                     patternValues.get(0));
        assertEquals("factType2 boundName2 true",
                     patternValues.get(1));
    }

    @Test
    public void testForEachPatternNames() throws Exception {
        final List<String> patternNames = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> patternNames.add(patternName));

        assertEquals("factType1 [boundName1]",
                     patternNames.get(0));
        assertEquals("negatedPattern factType2 [boundName2]",
                     patternNames.get(1));
    }

    @Test
    public void testCanSetNewFactPatternLabelWhenEditingPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        assertFalse(page.canSetNewFactPatternLabel());
    }

    @Test
    public void testCanSetNewFactPatternLabelWhenPatternIsNotANewFactPattern() throws Exception {
        final Pattern52 pattern = pattern("factType2",
                                          "boundName2",
                                          true);

        when(plugin.getEditingPattern()).thenReturn(pattern);

        assertFalse(page.canSetNewFactPatternLabel());
    }

    @Test
    public void testCanSetNewFactPatternLabelWhenPatternIsANewFactPattern() throws Exception {
        final Pattern52 pattern = pattern("factType3",
                                          "boundName3",
                                          true);

        when(plugin.getEditingPattern()).thenReturn(pattern);

        assertTrue(page.canSetNewFactPatternLabel());
    }

    @Test
    public void testSetEditingPattern() throws Exception {
        final Pattern52 pattern = pattern("factType1",
                                          "boundName1",
                                          false);

        when(model.getConditionPattern(eq("boundName1"))).thenReturn(pattern);

        page.setEditingPattern("factType1 boundName1 false");

        verify(plugin).setEditingPattern(pattern);
    }

    @Test
    public void testShowNewPatternModal() throws Exception {
        page.showNewPatternModal();

        verify(newPatternPresenter).show();
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNotNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(pattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternName = page.currentPatternName();

        assertEquals("negatedPattern factType3 [boundName3]",
                     patternName);
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        final String patternName = page.currentPatternName();

        assertEquals("",
                     patternName);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNotNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(pattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternValue = page.currentPatternValue();

        assertEquals("factType3 boundName3 true",
                     patternValue);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNull() throws Exception {
        when(plugin.getEditingPattern()).thenReturn(null);

        final String patternValue = page.currentPatternValue();

        assertEquals("",
                     patternValue);
    }

    private List<Pattern52> fakePatterns() {
        return new ArrayList<Pattern52>() {{
            add(pattern("factType1",
                        "boundName1",
                        false));
            add(pattern("factType2",
                        "boundName2",
                        true));
        }};
    }

    private Pattern52 pattern(final String factType,
                              final String boundName,
                              final boolean negated) {
        return new Pattern52() {{
            setFactType(factType);
            setBoundName(boundName);
            setNegated(negated);
        }};
    }
}
