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
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.CalculationTypeWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ConditionColumnWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.OperatorWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ConditionColumnWizardPlugin extends BaseDecisionTableColumnPlugin implements HasFieldPage,
                                                                                          HasPatternPage,
                                                                                          HasAdditionalInfoPage {

    @Inject
    private ConditionColumnWizardPage conditionColumnWizardPage;

    @Inject
    private PatternPage<ConditionColumnWizardPlugin> patternPage;

    @Inject
    private CalculationTypeWizardPage calculationTypeWizardPage;

    @Inject
    private FieldPage fieldPage;

    @Inject
    private OperatorWizardPage operatorWizardPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    @Inject
    private ValueOptionsWizardPage valueOptionsWizardPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    private int constraintValue;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewConditionSimpleColumn();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(patternPage);
            add(calculationTypeWizardPage);
            add(fieldPage);
            add(operatorWizardPage);
            add(valueOptionsWizardPage);
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
            Window.alert(GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription());
            return false;
        }

        if (editingCol.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_PREDICATE) {

            //Field mandatory for Literals and Formulae
            if (null == editingCol.getFactField() || "".equals(editingCol.getFactField())) {
                Window.alert(GuidedDecisionTableConstants.INSTANCE.PleaseSelectOrEnterField());
                return false;
            }

            //Operator optional for Literals and Formulae
            if (editingCol.getOperator() == null) {
                Window.alert(GuidedDecisionTableConstants.INSTANCE.NotifyNoSelectedOperator());
                return false;
            }
        } else {

            //Clear operator for predicates, but leave field intact for interpolation of $param values
            editingCol.setOperator(null);
        }

        if (editingCol.isBound() && !isBindingUnique(editingCol.getBinding())) {
            Window.alert(GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern());
            return false;
        }

        if (!unique(editingCol.getHeader())) {
            Window.alert(GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother());
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
        fireChangeEvent(operatorWizardPage);
    }

    public ConditionCol52 getEditingCol() {
        return editingCol;
    }

    @Override
    public void setEditingCol(final DTColumnConfig52 editingCol) {
        this.editingCol = (ConditionCol52) editingCol;
    }

    public void setEditingCol(final String selectedValue) {
        editingCol = newConditionColumn(selectedValue);

        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorWizardPage);
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

        fireChangeEvent(operatorWizardPage);
        fireChangeEvent(valueOptionsWizardPage);
        fireChangeEvent(additionalInfoPage);
    }

    public void setEditingColFactField(final String factField) {
        if (editingCol == null) {
            return;
        }

        editingCol.setFactField(factField);

        fireChangeEvent(operatorWizardPage);
        fireChangeEvent(valueOptionsWizardPage);
        fireChangeEvent(additionalInfoPage);
    }

    public int getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue(final int constraintValue) {
        this.constraintValue = constraintValue;

        fireChangeEvent(calculationTypeWizardPage);
    }

    @Override
    public String getFactField() {
        return editingCol == null ? "" : editingCol.getFactField();
    }
}
