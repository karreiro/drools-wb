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
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ActionColumnWrapper;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DefaultWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.LimitedWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ValueOptionsPageInitializer;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ActionSetFactPlugin extends BaseDecisionTableColumnPlugin implements HasFieldPage,
                                                                                  HasPatternPage,
                                                                                  HasValueOptionsPage,
                                                                                  HasAdditionalInfoPage {

    @Inject
    private PatternPage patternPage;

    @Inject
    private FieldPage fieldPage;

    @Inject
    private ValueOptionsPage<ActionSetFactPlugin> valueOptionsPage;

    @Inject
    private AdditionalInfoPage<ActionSetFactPlugin> additionalInfoPage;

    private Boolean valueOptionsPageCompleted = Boolean.FALSE;

    private Pattern52 editingPattern;

    private ActionColumnWrapper editingWrapper;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_SetTheValueOfAField);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(initializedPatternPage());
            add(fieldPage);
            add(initializedValueOptionsPage());
            add(initializedAdditionalInfoPage());
        }};
    }

    @Override
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);
    }

    @Override
    public Boolean generateColumn() {
        final ActionCol52 editingCol = editingWrapper().getActionCol52();

        if (!isValidFactType()) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnPattern));
            return false;
        }
        if (!isValidFactField()) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnField));
            return false;
        }
        if (nil(editingCol.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (!unique(editingCol.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        presenter.appendColumn(editingCol);

        return true;
    }

    @Override
    public Pattern52 editingPattern() {
        return editingPattern;
    }

    @Override
    public void setValueOptionsPageAsCompleted() {
        if (!isValueOptionsPageCompleted()) {
            setValueOptionsPageCompleted();

            fireChangeEvent(valueOptionsPage);
        }
    }

    private void setValueOptionsPageCompleted() {
        this.valueOptionsPageCompleted = Boolean.TRUE;
    }

    @Override
    public Boolean isValueOptionsPageCompleted() {
        return valueOptionsPageCompleted;
    }

    @Override
    public void setEditingPattern(final Pattern52 editingPattern) {
        this.editingPattern = editingPattern;

        editingWrapper().setFactField(null);
        editingWrapper().setFactType(null);
        editingWrapper().setBoundName(null);
        editingWrapper().setType(null);

        fireChangeEvent(patternPage);
        fireChangeEvent(fieldPage);
        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public String getEntryPointName() {
        return ""; // TODO
    }

    @Override
    public void setEntryPointName(final String entryPointName) {
        // TODO
    }

    @Override
    public int constraintValue() {
        return BaseSingleFieldConstraint.TYPE_UNDEFINED;
    }

    @Override
    public String getFactType() {
        return editingWrapper().getFactType();
    }

    @Override
    public FieldAccessorsAndMutators getAccessor() {
        return FieldAccessorsAndMutators.MUTATOR;
    }

    @Override
    public boolean filterEnumFields() {
        return false;
    }

    @Override
    public String getFactField() {
        return editingWrapper().getFactField();
    }

    @Override
    public void setFactField(final String selectedValue) {

        if (isNewFactPattern()) {
            editingWrapper = new ActionColumnWrapper.ActionInsertFact(this);
        } else {
            editingWrapper = new ActionColumnWrapper.ActionSetField(this);
        }

        editingWrapper.setFactField(selectedValue);
        editingWrapper.setFactType(editingPattern.getFactType());
        editingWrapper.setBoundName(editingPattern.getFactType());
        editingWrapper.setType(oracle().getFieldType(editingWrapper.getFactType(),
                                                     editingWrapper.getFactField()));

        fireChangeEvent(fieldPage);
    }

    @Override
    public ActionCol52 editingCol() {
        return editingWrapper().getActionCol52();
    }

    @Override
    public String getHeader() {
        return editingWrapper().getHeader();
    }

    @Override
    public void setHeader(final String header) {
        editingWrapper().setHeader(header);

        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public void setInsertLogical(final Boolean value) {
        editingWrapper().setInsertLogical(value);
    }

    @Override
    public void setUpdate(final Boolean value) {
        editingWrapper().setUpdate(value);
    }

    @Override
    public String getValueList() {
        return editingWrapper().getValueList();
    }

    @Override
    public void setValueList(final String valueList) {
        editingWrapper().setValueList(valueList);
    }

    @Override
    public String getBinding() {
        return editingWrapper().getBoundName();
    }

    @Override
    public void setBinding(final String binding) {
        editingWrapper().setBoundName(binding);
    }

    @Override
    public GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
    }

    @Override
    public boolean doesOperatorNeedValue() {
        return true;
    }

    @Override
    public boolean doesOperatorAcceptValueList() {
        return true;
    }

    @Override
    public IsWidget defaultValueWidget() {
        return new DefaultWidgetFactory<>(this).create();
    }

    @Override
    public IsWidget limitedValueWidget() {
        return new LimitedWidgetFactory<>(this).create();
    }

    private ActionColumnWrapper editingWrapper() {
        return Optional.ofNullable(editingWrapper).orElse(ActionColumnWrapper.emptyColumn());
    }

    private boolean unique(final String header) {
        for (final ActionCol52 o : presenter.getModel().getActionCols()) {
            if (o.getHeader().equals(header)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidFactType() {
        return !nil(getFactType());
    }

    private boolean isValidFactField() {
        return !nil(getFactField());
    }

    private boolean isNewFactPattern() {
        final BRLRuleModel validator = new BRLRuleModel(presenter.getModel());

        return !validator.isVariableNameUsed(getBinding());
    }

    private AsyncPackageDataModelOracle oracle() {
        return presenter.getDataModelOracle();
    }

    PatternPage initializedPatternPage() {
        patternPage.disableEntryPoint();

        return patternPage;
    }

    AdditionalInfoPage<ActionSetFactPlugin> initializedAdditionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
    }

    ValueOptionsPage<ActionSetFactPlugin> initializedValueOptionsPage() {
        return ValueOptionsPageInitializer.init(valueOptionsPage,
                                                this);
    }
}
