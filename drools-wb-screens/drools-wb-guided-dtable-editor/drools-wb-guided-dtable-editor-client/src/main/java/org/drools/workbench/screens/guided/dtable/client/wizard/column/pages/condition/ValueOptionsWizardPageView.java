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

import java.util.function.Supplier;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class ValueOptionsWizardPageView extends Composite implements ValueOptionsWizardPage.View {

    @Inject
    @DataField("valueList")
    private TextBox valueList;

    @DataField("cepWindowOperatorsContainer")
    private Div cepWindowOperatorsContainer = new Div();

    @DataField("defaultValueContainer")
    private Div defaultValueContainer = new Div();

    @DataField("limitedValueContainer")
    private Div limitedValueContainer = new Div();

    @DataField("bindingContainer")
    private Div bindingContainer = new Div();

    @Inject
    @DataField("cepWindowOperatorsLabel")
    private Label cepWindowOperatorsLabel;

    @Inject
    @DataField("defaultValueLabel")
    private Label defaultValueLabel;

    @Inject
    @DataField("limitedValueLabel")
    private Label limitedValueLabel;

    @Inject
    @DataField("bindingLabel")
    private Label bindingLabel;

    private CEPWindowOperatorsDropdown cepOperatorsDropdown;

    private IsWidget defaultValueWidget;

    private IsWidget limitedValueWidget;

    private TextBox bindingTextBox;

    private ValueOptionsWizardPage page;

    @Override
    public void init( final ValueOptionsWizardPage page ) {
        this.page = page;

        setupComponents();
    }

    private void setupComponents() {
        setupCepOperatorsDropdown();
        setupDefaultValue();
        setupLimitedValue();
        setupBinding();
    }

    private void setupCepOperatorsDropdown() {
        final FormFieldComponentWrapper wrapper = new FormFieldComponentWrapper( cepWindowOperatorsLabel,
                                                                                 cepWindowOperatorsContainer );

        wrapper.setup( page.canSetupCepOperators(), () -> {
            cepOperatorsDropdown = page.newCEPWindowOperatorsDropdown();
            return cepOperatorsDropdown;
        } );
    }

    private void setupDefaultValue() {
        final FormFieldComponentWrapper wrapper = new FormFieldComponentWrapper( defaultValueLabel,
                                                                                 defaultValueContainer );

        wrapper.setup( page.canSetupDefaultValue(), () -> {
            defaultValueWidget = page.newDefaultValueWidget();
            return defaultValueWidget;
        } );
    }

    private void setupLimitedValue() {
        final FormFieldComponentWrapper wrapper = new FormFieldComponentWrapper( limitedValueLabel,
                                                                                 limitedValueContainer );

        wrapper.setup( page.canSetupLimitedValue(), () -> {
            limitedValueWidget = page.newLimitedValueWidget();
            return limitedValueWidget;
        } );
    }

    private void setupBinding() {
        bindingTextBox = page.newBindingTextBox();

        addWidgetToContainer( bindingContainer, bindingTextBox );
    }

    private void addWidgetToContainer( final Div container,
                                       final IsWidget widget ) {
        container.clear();
        container.add( widget );
    }

    private class FormFieldComponentWrapper {

        final Label label;

        final Div container;

        FormFieldComponentWrapper( final Label label,
                                   final Div container ) {
            this.label = label;
            this.container = container;
        }

        void setup( final Boolean canSetup,
                    final Supplier<IsWidget> widget ) {
            formFieldToggle( canSetup );

            if ( !canSetup ) {
                return;
            }

            addWidget( widget.get() );
        }

        private void addWidget( final IsWidget widget ) {
            addWidgetToContainer( container, widget );
        }

        private void formFieldToggle( final Boolean visible ) {
            label.setVisible( visible );
            container.setVisible( visible );
        }
    }
}
