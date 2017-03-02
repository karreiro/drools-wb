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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.CalculationTypePage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.OperatorPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ConditionColumnPlugin extends BaseDecisionTableColumnPlugin implements HasFieldPage,
                                                                                    HasPatternPage,
                                                                                    HasAdditionalInfoPage {

    @Inject
    private PatternPage<ConditionColumnPlugin> patternPage;

    @Inject
    private CalculationTypePage calculationTypePage;

    @Inject
    private FieldPage fieldPage;

    @Inject
    private OperatorPage operatorPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    @Inject
    private ValueOptionsPage valueOptionsPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    private int constraintValue;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_AddNewConditionSimpleColumn);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(patternPage);
            add(calculationTypePage);
            add(fieldPage);
            add(operatorPage);
            add(valueOptionsPage);
            add(additionalInfoPage());
        }};
    }

    private AdditionalInfoPage additionalInfoPage() {
        return additionalInfoPageInitializer().init(this);
    }

    private AdditionalInfoPageInitializer additionalInfoPageInitializer() {
        return new AdditionalInfoPageInitializer(additionalInfoPage);
    }

    @Override
    public Boolean generateColumn() {

        if (null == editingCol.getHeader() || "".equals(editingCol.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (editingCol.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_PREDICATE) {

            //Field mandatory for Literals and Formulae
            if (null == editingCol.getFactField() || "".equals(editingCol.getFactField())) {
                Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_PleaseSelectOrEnterField));
                return false;
            }

            //Operator optional for Literals and Formulae
            if (editingCol.getOperator() == null) {
                Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_NotifyNoSelectedOperator));
                return false;
            }
        } else {

            //Clear operator for predicates, but leave field intact for interpolation of $param values
            editingCol.setOperator(null);
        }

        if (editingCol.isBound() && !isBindingUnique(editingCol.getBinding())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern));
            return false;
        }

        if (!unique(editingCol.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        //Clear binding if column is not a literal
        if (editingCol.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_LITERAL) {
            editingCol.setBinding(null);
        }

        presenter.appendColumn(editingPattern,
                               editingCol);

        return true;
    }

    private boolean unique(String header) {
        for (CompositeColumn<?> cc : presenter.getModel().getConditions()) {
            for (int iChild = 0; iChild < cc.getChildColumns().size(); iChild++) {
                if (cc.getChildColumns().get(iChild).getHeader().equals(header)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBindingUnique(String binding) {
        final BRLRuleModel rm = new BRLRuleModel(presenter.getModel());

        return !rm.isVariableNameUsed(binding);
    }

    public Pattern52 getEditingPattern() {
        return editingPattern;
    }

    public void setEditingPattern(final Pattern52 editingPattern) {
        this.editingPattern = editingPattern;

        fireChangeEvent(patternPage);
        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorPage);
    }

    public ConditionCol52 getEditingCol() {
        return editingCol;
    }

    public void setEditingCol(final String selectedValue) {
        editingCol = newConditionColumn(selectedValue);

        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorPage);
    }

    @Override
    public void setEditingCol(final DTColumnConfig52 editingCol) {
        this.editingCol = (ConditionCol52) editingCol;
    }

    ConditionCol52 newConditionColumn(final String selectedValue) {
        if (nil(selectedValue)) {
            return null;
        }

        final ConditionCol52 conditionCol52 = newConditionColumn();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        conditionCol52.setFactField(selectedValue);
        conditionCol52.setFieldType(oracle.getFieldType(getEditingPattern().getFactType(),
                                                        conditionCol52.getFactField()));
        return conditionCol52;
    }

    private ConditionCol52 newConditionColumn() {
        switch (presenter.getModel().getTableFormat()) {
            case EXTENDED_ENTRY:
                return new ConditionCol52();
            case LIMITED_ENTRY:
                return new LimitedEntryConditionCol52();
            default:
                throw new UnsupportedOperationException("Unsupported table format: " + presenter.getModel().getTableFormat());
        }
    }

    public void setEditingColOperator(String operator) {
        if (editingCol == null) {
            return;
        }

        editingCol.setOperator(operator);

        fireChangeEvent(operatorPage);
        fireChangeEvent(valueOptionsPage);
        fireChangeEvent(additionalInfoPage);
    }

    public void setEditingColFactField(final String factField) {
        if (editingCol == null) {
            return;
        }

        editingCol.setFactField(factField);

        fireChangeEvent(operatorPage);
        fireChangeEvent(valueOptionsPage);
        fireChangeEvent(additionalInfoPage);
    }

    public int getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue(final int constraintValue) {
        this.constraintValue = constraintValue;

        fireChangeEvent(calculationTypePage);
    }

    @Override
    public String getFactField() {
        return editingCol == null ? "" : editingCol.getFactField();
    }
}
