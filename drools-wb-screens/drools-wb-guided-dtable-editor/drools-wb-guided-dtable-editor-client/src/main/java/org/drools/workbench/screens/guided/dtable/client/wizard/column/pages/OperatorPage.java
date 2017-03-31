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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.CEPOperatorsDropdown;
import org.gwtbootstrap3.client.ui.ListBox;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class OperatorPage extends BaseDecisionTableColumnPage<ConditionColumnPlugin> {

    private View view;

    private Event<WizardPageStatusChangeEvent> changeEvent;

    @Inject
    public OperatorPage(final View view,
                        final Event<WizardPageStatusChangeEvent> changeEvent,
                        final TranslationService translationService) {
        super(translationService);

        this.view = view;
        this.changeEvent = changeEvent;
    }

    @Override
    protected UberElement<?> getView() {
        return view;
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.OperatorPage_Operator);
    }

    @Override
    public void prepareView() {
        view.init(this);

        setupWarningMessages();
        setupOperator();
    }

    private void setupOperator() {
        operatorDropdown(dropdown -> view.setupOperator(dropdown));
    }

    private void setupWarningMessages() {
        if (isConstraintValuePredicate()) {
            view.showPredicateWarning();
        } else {
            view.showFactFieldWarningWhenItIsNotDefined(hasFactField());
        }
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        boolean hasOperator = !nil(plugin().getFactField()) && !nil(editingCol().getOperator());

        callback.callback(hasOperator || isConstraintValuePredicate());
    }

    public String getOperator() {
        return editingCol().getOperator();
    }

    void setOperator(final String operator) {
        plugin().setOperator(operator);
    }

    void operatorDropdown(final Consumer<IsWidget> widgetSupplier) {
        if (hasFactField()) {
            cepOperatorsDropdown(widgetSupplier);
        } else {
            emptyOperatorsDropdown(widgetSupplier);
        }
    }

    boolean isConstraintValuePredicate() {
        return plugin().constraintValue() == BaseSingleFieldConstraint.TYPE_PREDICATE;
    }

    boolean hasFactField() {
        return !nil(plugin().getFactField());
    }

    void getOperatorCompletions(final Callback<String[]> callback) {
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        oracle.getOperatorCompletions(plugin().getFactType(),
                                      plugin().getFactField(),
                                      callback);
    }

    private void emptyOperatorsDropdown(final Consumer<IsWidget> widgetSupplier) {
        final ListBox listBox = newListBox();

        listBox.addItem(translate(GuidedDecisionTableErraiConstants.OperatorPage_PleaseChoose));
        listBox.getElement().setAttribute("disabled",
                                          "disabled");

        widgetSupplier.accept(listBox);
    }

    ListBox newListBox() {
        return new ListBox();
    }

    private void cepOperatorsDropdown(final Consumer<IsWidget> widgetSupplier) {
        getOperatorCompletions(options -> {
            final String[] operatorsArray = filterOptionsForConstraintTypeLiteral(options);
            final CEPOperatorsDropdown dropdown = newCepOperatorsDropdown(operatorsArray);

            dropdown.insertItem(translate(GuidedDecisionTableErraiConstants.OperatorPage_NoOperator),
                                "",
                                1);

            dropdown.addValueChangeHandler(valueChangeEvent -> {
                setOperator(valueChangeEvent.getValue().getValue());
            });

            widgetSupplier.accept(dropdown);
        });
    }

    CEPOperatorsDropdown newCepOperatorsDropdown(final String[] operatorsArray) {
        return new CEPOperatorsDropdown(operatorsArray,
                                        editingCol());
    }

    String[] filterOptionsForConstraintTypeLiteral(final String[] options) {
        final List<String> operatorOptions = new ArrayList<>();

        Collections.addAll(operatorOptions,
                           options);

        if (getConstraintValueType() != BaseSingleFieldConstraint.TYPE_LITERAL) {
            operatorOptions.remove("in");
            operatorOptions.remove("not in");
        }

        return operatorOptions.toArray(new String[operatorOptions.size()]);
    }

    private ConditionCol52 editingCol() {
        return plugin().editingCol();
    }

    private int getConstraintValueType() {
        return plugin().constraintValue();
    }

    public interface View extends UberElement<OperatorPage> {

        void showFactFieldWarningWhenItIsNotDefined(boolean hasOperator);

        void showPredicateWarning();

        void setupOperator(IsWidget dropdown);
    }
}
