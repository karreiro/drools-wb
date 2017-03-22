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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.datamodel.rule.CEPWindow;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.BindingTextBox;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ValueOptionsPage<T extends HasValueOptionsPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    private boolean valueListEnabled = false;

    private boolean cepOperatorsEnabled = false;

    private boolean defaultValueEnabled = false;

    private boolean limitedValueEnabled = false;

    private boolean bindingEnabled = false;

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ValueOptionsPage_ValueOptions);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(plugin().isValueOptionsPageCompleted());
    }

    @Override
    public void prepareView() {
        view.init(this);

        markAsViewed();
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    boolean isValueListEnabled() {
        return valueListEnabled;
    }

    boolean isCepOperatorsEnabled() {
        return cepOperatorsEnabled;
    }

    boolean isDefaultValueEnabled() {
        return defaultValueEnabled;
    }

    boolean isLimitedValueEnabled() {
        return limitedValueEnabled;
    }

    boolean isBindingEnabled() {
        return bindingEnabled;
    }

    public void enableValueList() {
        valueListEnabled = true;
    }

    public void enableCepOperators() {
        cepOperatorsEnabled = true;
    }

    public void enableDefaultValue() {
        defaultValueEnabled = true;
    }

    public void enableLimitedValue() {
        limitedValueEnabled = true;
    }

    public void enableBinding() {
        bindingEnabled = true;
    }

    IsWidget newDefaultValueWidget() {
        return plugin().defaultValueWidget();
    }

    IsWidget newLimitedValueWidget() {
        return plugin().limitedValueWidget();
    }

    CEPWindowOperatorsDropdown newCEPWindowOperatorsDropdown() {
        final Pattern52 editingPattern = editingPattern();

        return new CEPWindowOperatorsDropdown(editingPattern,
                                              false) {{
            addValueChangeHandler(event -> {
                final CEPWindow cepWindow = editingPattern.getWindow();
                final String operator = event.getValue().getValue();

                cepWindow.setOperator(operator);
            });
        }};
    }

    TextBox newBindingTextBox() {
        final BindingTextBox bindingTextBox = new BindingTextBox();

        bindingTextBox.setText(plugin().getBinding());
        bindingTextBox.addChangeHandler(event -> {
            plugin().setBinding(bindingTextBox.getText());
        });

        return bindingTextBox;
    }

    boolean canSetupCepOperators() {
        return isCepOperatorsEnabled() && editingPattern() != null;
    }

    boolean canSetupDefaultValue() {
        if (!isDefaultValueEnabled()) {
            return false;
        }

        if (tableFormat() != GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY) {
            return false;
        }

        if (doesNotHaveFactTypeOrFactField()) {
            return false;
        }

        if (!plugin().doesOperatorNeedValue()) {
            return false;
        }

        return true;
    }

    boolean canSetupLimitedValue() {
        if (!isLimitedValueEnabled()) {
            return false;
        }

        if (tableFormat() != GuidedDecisionTable52.TableFormat.LIMITED_ENTRY) {
            return false;
        }

        if (doesNotHaveFactTypeOrFactField()) {
            return false;
        }

        if (!plugin().doesOperatorNeedValue()) {
            return false;
        }

        return true;
    }

    boolean canSetupBinding() {
        return isBindingEnabled() && plugin().constraintValue() == BaseSingleFieldConstraint.TYPE_LITERAL;
    }

    boolean canSetupValueList() {
        if (!isValueListEnabled()) {
            return false;
        }

        if (tableFormat() != GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY) {
            return false;
        }

        if (doesNotHaveFactTypeOrFactField()) {
            return false;
        }

        if (!doesOperatorAcceptValueList()) {
            return false;
        }

        if (presenter.getDataModelOracle().hasEnums(editingPattern().getFactType(),
                                                    plugin().getFactField())) {
            return false;
        }

        return true;
    }

    private boolean doesOperatorAcceptValueList() {
        return plugin().doesOperatorAcceptValueList();
    }

    private boolean doesNotHaveFactTypeOrFactField() {
        return nil(plugin().getFactType()) || nil(plugin().getFactField());
    }

    void isFactTypeAnEvent(final Callback<Boolean> callback) {
        if (canSetupCepOperators()) {
            presenter.getDataModelOracle().isFactTypeAnEvent(editingPattern().getFactType(),
                                                             callback);
        } else {
            callback.callback(false);
        }
    }

    private GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
    }

    private Pattern52 editingPattern() {
        return plugin().editingPattern();
    }

    public String getValueList() {
        return plugin().getValueList();
    }

    public void setValueList(final String valueList) {
        plugin().setValueList(valueList);
    }

    private void markAsViewed() {
        plugin().setValueOptionsPageAsCompleted();
    }

    public interface View extends UberView<ValueOptionsPage> {

    }
}
