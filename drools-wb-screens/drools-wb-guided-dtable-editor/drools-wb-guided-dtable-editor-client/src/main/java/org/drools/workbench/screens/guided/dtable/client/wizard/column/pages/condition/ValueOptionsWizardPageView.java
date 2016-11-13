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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class ValueOptionsWizardPageView extends Composite implements ValueOptionsWizardPage.View {

    @DataField("dropdownContainer")
    Div dropdownContainer = new Div();

    @Inject
    @DataField("valueList")
    TextBox valueList;

    @DataField("defaultValueContainer")
    Div defaultValueContainer = new Div();

    @DataField("limitedValueContainer")
    Div limitedValueContainer = new Div();

    @DataField("bindingContainer")
    Div bindingContainer = new Div();

    private ValueOptionsWizardPage page;

    private CEPWindowOperatorsDropdown cepOperatorsDropdown;

    private IsWidget defaultValueWidget;

    private IsWidget limitedValueWidget;

    private TextBox bindingTextBox;

    private CheckBox hideColumnCheckBox;

    @Override
    public void init( final ValueOptionsWizardPage page ) {
        this.page = page;

        setupCepOperatorsDropdown();
        setupDefaultValueContainer();
        setupLimitedValueContainer();
        setupBindingContainer();
    }

    private void setupCepOperatorsDropdown() {
        cepOperatorsDropdown = page.newCEPWindowOperatorsDropdown();

        dropdownContainer.clear();
        dropdownContainer.add( cepOperatorsDropdown );
    }

    private void setupDefaultValueContainer() {
        defaultValueWidget = page.newDefaultValueWidget();

        defaultValueContainer.clear();
        defaultValueContainer.add( defaultValueWidget );
    }

    private void setupLimitedValueContainer() {
        limitedValueWidget = page.newLimitedValueWidget();

        limitedValueContainer.clear();
        limitedValueContainer.add( limitedValueWidget );
    }

    private void setupBindingContainer() {
        bindingTextBox = page.newBindingTextBox();

        bindingContainer.clear();
        bindingContainer.add( bindingTextBox );
    }
}
