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
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.CellUtilities;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
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
    private AdditionalInfoPage<ConditionColumnPlugin> additionalInfoPage;

    @Inject
    private ValueOptionsPage valueOptionsPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    private int constraintValue;

    private Boolean valueOptionsPageCompleted;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_AddNewConditionSimpleColumn);
    }

    @Override
    public void init(NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);

        setupDefaultValues();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(patternPage);

            if (isExtendedEntryTable()) {
                add(calculationTypePage);
            }

            add(fieldPage);
            add(operatorPage);
            add(valueOptionsPage);
            add(additionalInfoPage());
        }};
    }

    @Override
    public Boolean generateColumn() {
        if (nil(editingCol().getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (constraintValue() != BaseSingleFieldConstraint.TYPE_PREDICATE) {
            if (nil(getFactField())) {
                Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_PleaseSelectOrEnterField));
                return false;
            }

            if (nil(getOperator())) {
                Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_NotifyNoSelectedOperator));
                return false;
            }
        } else {
            editingCol().setOperator(null);
        }

        if (editingCol().isBound() && !isBindingUnique(editingCol().getBinding())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern));
            return false;
        }

        if (!unique(editingCol().getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ConditionColumnPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        if (editingCol().getConstraintValueType() != BaseSingleFieldConstraint.TYPE_LITERAL) {
            editingCol().setBinding(null);
        }

        presenter.appendColumn(editingPattern,
                               editingCol);

        return true;
    }

    @Override
    public Pattern52 editingPattern() {
        return editingPattern;
    }

    @Override
    public void setEditingPattern(final Pattern52 editingPattern) {
        this.editingPattern = editingPattern;

        setupDefaultValues();

        fireChangeEvent(patternPage);
        fireChangeEvent(calculationTypePage);
        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorPage);
        fireChangeEvent(valueOptionsPage);
        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public String getEntryPointName() {
        if (editingPattern() != null) {
            return editingPattern().getEntryPointName();
        }

        return "";
    }

    @Override
    public void setEntryPointName(final String entryPointName) {
        if (editingPattern() != null) {
            editingPattern().setEntryPointName(entryPointName);
        }
    }

    @Override
    public ConditionCol52 editingCol() {
        if (editingPattern() == null) {
            resetFieldAndOperator();
        }

        return editingCol;
    }

    @Override
    public void setHeader(final String header) {
        editingCol().setHeader(header);

        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public String getFactField() {
        return editingCol() == null ? "" : editingCol().getFactField();
    }

    @Override
    public void setFactField(final String selectedValue) {
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        editingCol().setFactField(selectedValue);
        editingCol().setFieldType(oracle.getFieldType(getFactType(),
                                                      getFactField()));

        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorPage);
        fireChangeEvent(additionalInfoPage);
        fireChangeEvent(valueOptionsPage);
    }

    public int constraintValue() {
        final boolean factHasEnums = presenter.getDataModelOracle().hasEnums(getFactType(),
                                                                             getFactField());

        if (factHasEnums) {
            setConstraintValueFieldAndUpdateEditingCol(BaseSingleFieldConstraint.TYPE_LITERAL);
        }

        return constraintValue;
    }

    public void setConstraintValue(final int constraintValue) {
        setConstraintValueFieldAndUpdateEditingCol(constraintValue);

        resetFieldAndOperator();

        fireChangeEvent(calculationTypePage);
        fireChangeEvent(fieldPage);
        fireChangeEvent(operatorPage);
    }

    public void setValueOptionsPageAsCompleted() {
        if (valueOptionsPageCompleted.equals(Boolean.FALSE)) {
            valueOptionsPageCompleted = Boolean.TRUE;

            fireChangeEvent(valueOptionsPage);
        }
    }

    public Boolean isValueOptionsPageCompleted() {
        return valueOptionsPageCompleted;
    }

    public void setValueList(final String valueList) {
        editingCol().setValueList(valueList);

        assertDefaultValue();

        fireChangeEvent(valueOptionsPage);
    }

    public String getFactType() {
        return editingPattern() == null ? "" : editingPattern().getFactType();
    }

    private String getOperator() {
        return editingCol().getOperator();
    }

    public void setOperator(String operator) {
        editingCol().setOperator(operator);

        fireChangeEvent(operatorPage);
        fireChangeEvent(additionalInfoPage);
        fireChangeEvent(valueOptionsPage);
    }

    private AdditionalInfoPage additionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
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

    private void setConstraintValueFieldAndUpdateEditingCol(int constraintValue) {
        this.constraintValue = constraintValue;

        editingCol().setConstraintValueType(constraintValue);
    }

    private ConditionCol52 newConditionColumn() {
        switch (tableFormat()) {
            case EXTENDED_ENTRY:
                return new ConditionCol52();
            case LIMITED_ENTRY:
                return new LimitedEntryConditionCol52();
            default:
                throw new UnsupportedOperationException("Unsupported table format: " + tableFormat());
        }
    }

    private boolean isExtendedEntryTable() {
        return tableFormat() == GuidedDecisionTable52.TableFormat.EXTENDED_ENTRY;
    }

    private GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
    }

    private void assertDefaultValue() {
        final CellUtilities cellUtilities = new CellUtilities();
        final GuidedDecisionTable52 model = presenter.getModel();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();
        final ColumnUtilities columnUtilities = new ColumnUtilities(model,
                                                                    oracle);
        final List<String> valueList = Arrays.asList(columnUtilities.getValueList(editingCol));

        if (valueList.size() > 0) {
            final String defaultValue = cellUtilities.asString(editingCol().getDefaultValue());
            if (!valueList.contains(defaultValue)) {
                editingCol.getDefaultValue().clearValues();
            }
        } else {
            //Ensure the Default Value has been updated to represent the column's data-type.
            final DTCellValue52 defaultValue = editingCol().getDefaultValue();
            final DataType.DataTypes dataType = columnUtilities.getDataType(editingPattern(),
                                                                            editingCol());
            cellUtilities.convertDTCellValueType(dataType,
                                                 defaultValue);
        }
    }

    private void setupDefaultValues() {
        editingCol = newConditionColumn();
        constraintValue = BaseSingleFieldConstraint.TYPE_UNDEFINED;
        valueOptionsPageCompleted = Boolean.FALSE;
    }

    private void resetFieldAndOperator() {
        editingCol.setFactField("");
        editingCol.setFieldType("");
        editingCol.setOperator("");
    }
}
