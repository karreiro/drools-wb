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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals.NewPatternPresenter;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.PatternWrapper;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PatternPageTest {

    @Captor
    private ArgumentCaptor<PatternWrapper> patternWrapperArgumentCaptor;

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

    @Mock
    private SimplePanel content;

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
        when(plugin.getPatterns()).thenReturn(fakePatterns());

        final List<String> patternValues = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> patternValues.add(patternValue));

        assertEquals("factType1 boundName1 false",
                     patternValues.get(0));
        assertEquals("factType2 boundName2 true",
                     patternValues.get(1));
    }

    @Test
    public void testForEachPatternNames() throws Exception {
        when(plugin.getPatterns()).thenReturn(fakePatterns());

        final List<String> patternNames = new ArrayList<>();

        page.forEachPattern((patternName, patternValue) -> patternNames.add(patternName));

        assertEquals("factType1 [boundName1]",
                     patternNames.get(0));
        assertEquals("negatedPattern factType2 [boundName2]",
                     patternNames.get(1));
    }

    @Test
    public void testSetSelectedEditingPattern() throws Exception {
        when(view.getSelectedValue()).thenReturn("factType boundName false");

        page.setSelectedEditingPattern();

        verify(page).setEditingPattern(patternWrapperArgumentCaptor.capture());

        final PatternWrapper pattern = patternWrapperArgumentCaptor.getValue();

        assertEquals("factType",
                     pattern.getFactType());
        assertEquals("boundName",
                     pattern.getBoundName());
        assertFalse(pattern.isNegated());
    }

    @Test
    public void testSetEditingPattern() throws Exception {
        when(view.getEntryPointName()).thenReturn("entryPoint");

        final PatternWrapper pattern = spy(newPattern("factType1",
                                                      "boundName1",
                                                      false));

        page.setEditingPattern(pattern);

        verify(pattern).setEntryPointName("entryPoint");
        verify(plugin).setEditingPattern(pattern);
    }

    @Test
    public void testShowNewPatternModal() throws Exception {
        page.showNewPatternModal();

        verify(newPatternPresenter).show();
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNotNull() throws Exception {
        when(plugin.getPatterns()).thenReturn(fakePatterns());
        when(plugin.patternWrapper()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternName = page.currentPatternName();

        assertEquals("negatedPattern factType3 [boundName3]",
                     patternName);
    }

    @Test
    public void testCurrentPatternNameWhenTheCurrentPatternIsNull() throws Exception {
        when(plugin.getPatterns()).thenReturn(fakePatterns());
        when(plugin.patternWrapper()).thenReturn(null);

        final String patternName = page.currentPatternName();

        assertEquals("",
                     patternName);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNotNull() throws Exception {
        when(plugin.getPatterns()).thenReturn(fakePatterns());
        when(plugin.patternWrapper()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final String patternValue = page.currentPatternValue();

        assertEquals("factType3 boundName3 true",
                     patternValue);
    }

    @Test
    public void testCurrentPatternValueWhenTheCurrentPatternIsNull() throws Exception {
        when(plugin.patternWrapper()).thenReturn(null);

        final String patternValue = page.currentPatternValue();

        assertEquals("",
                     patternValue);
    }

    @Test
    public void testGetPatternsWhenCurrentPatternIsNull() {
        when(plugin.getPatterns()).thenReturn(fakePatterns());

        final List<PatternWrapper> patterns = page.getPatterns();

        assertEquals(2,
                     patterns.size());
    }

    @Test
    public void testGetPatternsWhenCurrentPatternIsNotNull() {
        when(plugin.getPatterns()).thenReturn(fakePatterns());
        when(plugin.patternWrapper()).thenReturn(newPattern("factType3",
                                                            "boundName3",
                                                            true));

        final List<PatternWrapper> patterns = page.getPatterns();

        assertEquals(3,
                     patterns.size());
    }

    @Test
    public void testGetPatternsWhenNegatedPatternsAreNotEnabled() {
        page.disableNegatedPatterns();

        when(plugin.getPatterns()).thenReturn(fakePatterns());

        final List<PatternWrapper> patterns = page.getPatterns();

        assertEquals(1,
                     patterns.size());
    }

    @Test
    public void testInitialise() throws Exception {
        page.initialise();

        verify(newPatternPresenter).init(page);
        verify(content).setWidget(view);
    }

    @Test
    public void testGetTitle() throws Exception {
        final String errorKey = GuidedDecisionTableErraiConstants.PatternPage_Pattern;
        final String errorMessage = "Title";

        when(translationService.format(errorKey)).thenReturn(errorMessage);

        final String title = page.getTitle();

        assertEquals(errorMessage,
                     title);
    }

    @Test
    public void testPrepareView() throws Exception {
        page.prepareView();

        verify(view).init(page);
    }

    @Test
    public void testAsWidget() {
        final Widget contentWidget = page.asWidget();

        assertEquals(contentWidget,
                     content);
    }

    @Test
    public void testIsCompleteWhenPatternIsSet() {
        when(plugin.patternWrapper()).thenReturn(newPattern("factType",
                                                            "",
                                                            false));

        page.isComplete(Assert::assertTrue);
    }

    @Test
    public void testIsCompleteWhenPatternIsNotSet() {
        when(plugin.patternWrapper()).thenReturn(newPattern("",
                                                            "",
                                                            false));

        page.isComplete(Assert::assertFalse);
    }

    @Test
    public void testPresenter() {
        assertEquals(presenter,
                     page.presenter());
    }

    @Test
    public void testDisableNegatedPatterns() {
        page.disableNegatedPatterns();

        assertFalse(page.isNegatedPatternEnabled());
    }

    @Test
    public void testDisableEntryPoint() {
        page.disableEntryPoint();

        verify(view).disableEntryPoint();
    }

    @Test
    public void testGetEntryPointName() {
        final String expectedEntryPoint = "entryPoint";

        when(plugin.getEntryPointName()).thenReturn(expectedEntryPoint);

        final String actualEntryPoint = page.getEntryPointName();

        verify(plugin).getEntryPointName();
        assertEquals(expectedEntryPoint,
                     actualEntryPoint);
    }

    @Test
    public void testSetEntryPoint() {
        final String entryPoint = "entryPoint";

        when(view.getEntryPointName()).thenReturn(entryPoint);

        page.setEntryPoint();

        verify(plugin).setEntryPointName(entryPoint);
    }

    private List<PatternWrapper> fakePatterns() {
        return new ArrayList<PatternWrapper>() {{
            add(newPattern("factType1",
                           "boundName1",
                           false));
            add(newPattern("factType2",
                           "boundName2",
                           true));
        }};
    }

    private PatternWrapper newPattern(final String factType,
                                      final String boundName,
                                      final boolean negated) {
        return new PatternWrapper(factType,
                                  boundName,
                                  negated);
    }
}
