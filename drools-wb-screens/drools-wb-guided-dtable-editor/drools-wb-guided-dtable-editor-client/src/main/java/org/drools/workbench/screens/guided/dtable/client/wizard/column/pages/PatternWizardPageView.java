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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.jboss.errai.common.client.dom.Label;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;
import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
@Templated
public class PatternWizardPageView extends Composite implements PatternPage.View {

    private PatternPage<? extends HasPatternPage> page;

    @Inject
    @DataField("patternList")
    private ListBox patternList;

    @Inject
    @DataField("newPattern")
    private Label newPattern;

    @Inject
    @DataField("createANewFactPattern")
    private Button createANewFactPattern;

    @Override
    public void init(final PatternPage page) {
        this.page = page;

        setup();
    }

    @Override
    public void setup() {
        setupPatternList();
        setupNewPatternLabel();
    }

    @EventHandler("createANewFactPattern")
    public void onCreateANewFactPattern(ClickEvent event) {
        page.showNewPatternModal();
    }

    @EventHandler("patternList")
    public void onPluginSelected(ChangeEvent event) {
        setEditingPattern();
        setupNewPatternLabel();
    }

    @Override
    public void setNewPatternLabel(final String patternName) {
        newPattern.setTextContent(patternName);
    }

    private void setEditingPattern() {
        page.setEditingPattern(patternList.getSelectedValue());
    }

    private void setupNewPatternLabel() {
        final boolean canSetNewFactPatternLabel = page.canSetNewFactPatternLabel();

        if (canSetNewFactPatternLabel) {
            newPattern.setTextContent(page.currentPatternName());
        }

        newPattern.setHidden(!canSetNewFactPatternLabel);
    }

    private void setupPatternList() {
        patternList.clear();
        patternList.addItem("-- Select a pattern --",
                            "");

        page.forEachPattern((item, value) -> patternList.addItem(item,
                                                                 value));

        selectTheCurrentPattern();
        hidePatternListWhenItIsEmpty();
    }

    private void hidePatternListWhenItIsEmpty() {
        patternList.setVisible(patternList.getItemCount() > 1);
    }

    private void selectTheCurrentPattern() {
        final String currentValue = page.currentPatternValue();
        final int currentValueIndex = getCurrentIndexFromList(currentValue,
                                                              patternList);
        final boolean patternListContainsCurrentValue = currentValueIndex != 0;

        if (!nil(currentValue) && !patternListContainsCurrentValue) {
            newPattern.setTextContent(currentValue);
        }

        patternList.setSelectedIndex(currentValueIndex);
    }
}
