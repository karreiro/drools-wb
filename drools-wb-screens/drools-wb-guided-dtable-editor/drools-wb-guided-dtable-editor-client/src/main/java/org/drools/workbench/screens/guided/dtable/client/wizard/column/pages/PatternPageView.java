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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;

@Dependent
@Templated
public class PatternPageView extends Composite implements PatternPage.View {

    private PatternPage<? extends HasPatternPage> page;

    @Inject
    @DataField("patternList")
    private ListBox patternList;

    @Inject
    @DataField("entryPointName")
    private TextBox entryPointName;

    @Inject
    @DataField("createANewFactPattern")
    private Button createANewFactPattern;

    @Inject
    private TranslationService translationService;

    @Override
    public void init(final PatternPage page) {
        this.page = page;

        setup();
    }

    @Override
    public void setup() {
        setupPatternList();
        setupEntryPointName();
    }

    private void setupEntryPointName() {
        entryPointName.setText(page.getEntryPointName());
    }

    @EventHandler("createANewFactPattern")
    public void onCreateANewFactPattern(ClickEvent event) {
        page.showNewPatternModal();
    }

    @EventHandler("patternList")
    public void onEditingPatternSelected(ChangeEvent event) {
        page.setSelectedEditingPattern();
    }

    @EventHandler("entryPointName")
    public void onEntryPointChange(KeyUpEvent event) {
        page.setEntryPoint();
    }

    @Override
    public String getSelectedValue() {
        return patternList.getSelectedValue();
    }

    @Override
    public String getEntryPointName() {
        return entryPointName.getText();
    }

    private void setupPatternList() {
        final String selectPattern = translate(GuidedDecisionTableErraiConstants.PatternPageView_SelectPattern);

        patternList.clear();
        patternList.addItem("-- " + selectPattern + " --",
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

        patternList.setSelectedIndex(currentValueIndex);
    }

    private String translate(final String key,
                             final Object... args) {
        return translationService.format(key,
                                         args);
    }
}
