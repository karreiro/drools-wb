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

import java.util.function.Supplier;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class ValueOptionsPageView extends Composite implements ValueOptionsPage.View {

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

    private ValueOptionsPage page;

    @Override
    public void init(final ValueOptionsPage page) {
        this.page = page;

        setupComponents();
    }

    private void setupComponents() {
        setupValueList();
        setupCepOperators();
        setupDefaultValue();
        setupLimitedValue();
        setupBinding();
    }

    @EventHandler("valueList")
    public void onSelectValueList(final KeyUpEvent event) {
        page.setValueList(valueList.getText());

        setupDefaultValue();
    }

    private void setupValueList() {
        if (!page.canSetupValueList()) {
            disableElement(valueList.getElement());
        }

        valueList.setText(page.getValueList());
    }

    private void setupDefaultValue() {
        setupElement(page.canSetupDefaultValue(),
                     page::newDefaultValueWidget,
                     defaultValueContainer);
    }

    private void setupLimitedValue() {
        setupElement(page.canSetupLimitedValue(),
                     page::newLimitedValueWidget,
                     limitedValueContainer);
    }

    private void setupBinding() {
        setupElement(page.canSetupBinding(),
                     page::newBindingTextBox,
                     bindingContainer);
    }

    private void setupCepOperators() {
        page.isFactTypeAnEvent((isEvent) -> {
            setupElement(page.canSetupCepOperators() && isEvent,
                         page::newCEPWindowOperatorsDropdown,
                         cepWindowOperatorsContainer);
        });
    }

    private void setupElement(final boolean canSetup,
                              final Supplier<IsWidget> widgetSupplier,
                              final Div container) {
        final IsWidget widget;

        if (canSetup) {
            widget = widgetSupplier.get();
        } else {
            widget = disabledTextBox();
        }

        addWidgetToContainer(widget,
                             container);
    }

    private void addWidgetToContainer(final IsWidget widget,
                                      final Div container) {
        container.clear();
        container.add(widget);
    }

    private TextBox disabledTextBox() {
        return new TextBox() {{
            disableElement(getElement());
        }};
    }

    private void disableElement(final Element element) {
        element.setAttribute("disabled",
                             "disabled");
    }
}
