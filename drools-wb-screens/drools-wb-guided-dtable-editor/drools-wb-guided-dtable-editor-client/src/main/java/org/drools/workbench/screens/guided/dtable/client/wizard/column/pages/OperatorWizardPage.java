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
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.resources.HumanReadable;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;

@Dependent
public class OperatorWizardPage extends BaseDecisionTableColumnPage<ConditionColumnWizardPlugin> {

    @Inject
    private View view;

    @Inject
    private Event<WizardPageStatusChangeEvent> changeEvent;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.Operator();
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        final ConditionCol52 editingCol = editingCol();
        boolean hasOperator = editingCol != null && editingCol.getOperator() != null;

        callback.callback(hasOperator || isConstraintValuePredicate());
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    void forEachOperator(final BiConsumer<String, String> biConsumer) {
        for (final String option : operatorOptions()) {
            biConsumer.accept(HumanReadable.getOperatorDisplayName(option),
                              option);
        }
    }

    List<String> operatorOptions() {
        final List<String> operatorOptions = new ArrayList<>();

        if (canSetOperator()) {
            getOperatorCompletions(options -> Collections.addAll(operatorOptions,
                                                                 options));

            if (BaseSingleFieldConstraint.TYPE_LITERAL != editingCol().getConstraintValueType()) {
                operatorOptions.remove("in");
                operatorOptions.remove("not in");
            }
        }

        return operatorOptions;
    }

    void getOperatorCompletions(final Callback<String[]> callback) {
        final String factType = plugin().getEditingPattern().getFactType();
        final String factField = editingCol().getFactField();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        oracle.getOperatorCompletions(factType,
                                      factField,
                                      callback);
    }

    boolean isConstraintValuePredicate() {
        return plugin().getConstraintValue() == BaseSingleFieldConstraint.TYPE_PREDICATE;
    }

    boolean canSetOperator() {
        return editingCol() != null;
    }

    public String getOperator() {
        return canSetOperator() && editingCol().getOperator() != null ? editingCol().getOperator() : "";
    }

    public void setOperator(final String selectedValue) {
        plugin().setEditingColOperator(selectedValue);
    }

    private ConditionCol52 editingCol() {
        return plugin().getEditingCol();
    }

    public interface View extends UberView<OperatorWizardPage> {

    }
}
