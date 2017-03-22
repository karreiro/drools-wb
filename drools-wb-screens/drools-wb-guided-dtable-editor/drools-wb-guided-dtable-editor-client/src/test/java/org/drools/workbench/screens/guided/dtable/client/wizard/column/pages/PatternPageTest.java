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
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals.NewPatternPresenter;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.jboss.errai.ui.client.local.spi.TranslationService;
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
    private ConditionColumnPlugin plugin;

    @Mock
    private GuidedDecisionTableView.Presenter presenter;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private PatternPage<ConditionColumnPlugin> page = spy(new PatternPage<ConditionColumnPlugin>());

    private GuidedDecisionTable52 model;

    @BeforeClass
    public static void staticSetup() {
        // Prevent runtime GWT.create() error at 'content = new SimplePanel()'
        GWTMockUtilities.disarm();
    }

    @Before
    public void setup() {
        model = spy(new GuidedDecisionTable52());

        when(presenter.getModel()).thenReturn(model);
        when(page.plugin()).thenReturn(plugin);
    }

    @Test
    public void testForEachPatternValues() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());

        final List<String> patternValues = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> patternValues.add(patternValue));

        assertEquals("factType1 boundName1 false",
                     patternValues.get(0));
        assertEquals("factType2 boundName2 true",
                     patternValues.get(1));
    }

    @Test
    public void testForEachPatternNames() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(translationService.format(GuidedDecisionTableErraiConstants.PatternPage_NegatedPattern)).thenReturn("Not");

        final List<String> patternNames = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> patternNames.add(patternName));

        assertEquals("factType1 [boundName1]",
                     patternNames.get(0));
        assertEquals("Not factType2 [boundName2]",
                     patternNames.get(1));
    }

    @Test
    public void testSetSelectedEditingPattern() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());

        final Pattern52 pattern = newPattern("factType1",
                                             "boundName1",
                                             false);

        when(model.getConditionPattern(eq("boundName1"))).thenReturn(pattern);
        when(view.getSelectedValue()).thenReturn("factType1 boundName1 false");

        page.setSelectedEditingPattern();

        verify(page).setEditingPattern(pattern);
    }

    @Test
    public void testSetEditingPattern() throws Exception {
        final Pattern52 pattern = newPattern("factType1",
                                             "boundName1",
                                             false);

        page.setEditingPattern(pattern);

        verify(plugin).setEditingPattern(pattern);
    }

    @Test
    public void testShowNewPatternModal() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());

        page.showNewPatternModal();

        verify(newPatternPresenter).show();
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNotNull() throws Exception {
        when(translationService.format(GuidedDecisionTableErraiConstants.PatternPage_NegatedPattern)).thenReturn("Not");
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(plugin.editingPattern()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternName = page.currentPatternName();

        assertEquals("Not factType3 [boundName3]",
                     patternName);
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNull() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(plugin.editingPattern()).thenReturn(null);

        final String patternName = page.currentPatternName();

        assertEquals("",
                     patternName);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNotNull() throws Exception {
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(plugin.editingPattern()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternValue = page.currentPatternValue();

        assertEquals("factType3 boundName3 true",
                     patternValue);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNull() throws Exception {
        when(plugin.editingPattern()).thenReturn(null);

        final String patternValue = page.currentPatternValue();

        assertEquals("",
                     patternValue);
    }

    @Test
    public void testExtractEditingPatternWithBoundPatterns() {
        final String[] metadata = new String[]{"Applicant", "$a", "false"};
        final Pattern52 expectedPattern = newPattern("Applicant",
                                                     "$a",
                                                     false);

        model.getConditions().add(expectedPattern);

        final Pattern52 pattern = page.extractEditingPattern(metadata);

        assertNotNull(pattern);
        assertEquals(expectedPattern,
                     pattern);
    }

    @Test
    public void testExtractEditingPatternWithoutBoundPatterns() {
        final String[] metadata = new String[]{"Applicant", "$a", "false"};

        final Pattern52 pattern52 = page.extractEditingPattern(metadata);

        assertNull(pattern52);
    }

    @Test
    public void testExtractEditingPatternWithNotPatterns() {
        final String[] metadata = new String[]{"Applicant", "", "true"};
        final Pattern52 pattern52 = newPattern("Applicant",
                                               "",
                                               true);

        model.getConditions().add(pattern52);

        final Pattern52 pattern = page.extractEditingPattern(metadata);

        assertNotNull(pattern);
        assertEquals(pattern52,
                     pattern);
    }

    @Test(expected = IllegalStateException.class)
    public void testExtractEditingPatternWithoutNotPatterns() {
        final String[] metadata = new String[]{"Applicant", "", "true"};

        page.extractEditingPattern(metadata);
    }

    @Test
    public void testGetPatternsWhenCurrentPatternIsNull() {
        when(model.getPatterns()).thenReturn(fakePatterns());

        final List<Pattern52> patterns = page.getPatterns();

        assertEquals(2,
                     patterns.size());
    }

    @Test
    public void testGetPatternsWhenCurrentPatternIsNotNull() {
        when(model.getPatterns()).thenReturn(fakePatterns());
        when(plugin.editingPattern()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final List<Pattern52> patterns = page.getPatterns();

        assertEquals(3,
                     patterns.size());
    }

    private List<Pattern52> fakePatterns() {
        return new ArrayList<Pattern52>() {{
            add(newPattern("factType1",
                           "boundName1",
                           false));
            add(newPattern("factType2",
                           "boundName2",
                           true));
        }};
    }

    private Pattern52 newPattern(final String factType,
                                 final String boundName,
                                 final boolean negated) {
        return new Pattern52() {{
            setFactType(factType);
            setBoundName(boundName);
            setNegated(negated);
        }};
    }
}
