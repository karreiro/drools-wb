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

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;

@Dependent
@Templated
public class PatternToDeletePageView extends Composite implements PatternToDeletePage.View {

    @Inject
    @DataField("patternList")
    private ListBox patternList;

    @Inject
    private TranslationService translationService;

    private PatternToDeletePage page;

    @Override
    public void init(final PatternToDeletePage page) {
        this.page = page;

        setupPatternList();
    }

    @EventHandler("patternList")
    public void onPatternSelected(ChangeEvent event) {
        page.setTheSelectedPattern();
    }

    @Override
    public String selectedPattern() {
        return patternList.getSelectedValue();
    }

    private void setupPatternList() {
        final List<String> patterns = page.loadPatterns();
        final boolean hasPatterns = !patterns.isEmpty();

        patternList.clear();
        patternList.setEnabled(hasPatterns);

        if (hasPatterns) {
            setupPatternList(patterns);
        } else {
            setupEmptyPatternList();
        }
    }

    private void setupPatternList(List<String> patterns) {
        patternList.addItem(translate(GuidedDecisionTableErraiConstants.PatternToDeletePageView_Choose));

        patterns.forEach(patternList::addItem);

        selectTheCurrentPattern();
    }

    private void setupEmptyPatternList() {
        patternList.addItem(translate(GuidedDecisionTableErraiConstants.PatternToDeletePageView_None));
    }

    private void selectTheCurrentPattern() {
        final String currentValue = page.binding();
        final int currentValueIndex = getCurrentIndexFromList(currentValue,
                                                              patternList);

        patternList.setSelectedIndex(currentValueIndex);
    }

    private String translate(final String key,
                             final Object... args) {
        return translationService.format(key,
                                         args);
    }
}
