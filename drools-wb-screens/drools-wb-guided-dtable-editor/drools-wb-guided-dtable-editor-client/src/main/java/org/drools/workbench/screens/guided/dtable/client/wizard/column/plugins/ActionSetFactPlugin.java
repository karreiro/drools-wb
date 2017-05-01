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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
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
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ValueOptionsPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ActionInsertFactWrapper;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ActionSetFactWrapper;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ActionWrapper;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DefaultWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.LimitedWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.PatternWrapper;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.ValueOptionsPageInitializer;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ActionSetFactPlugin extends BaseDecisionTableColumnPlugin implements HasFieldPage,
                                                                                  HasPatternPage,
                                                                                  HasValueOptionsPage,
                                                                                  HasAdditionalInfoPage {

    private PatternPage patternPage;

    private FieldPage fieldPage;

    private ValueOptionsPage<ActionSetFactPlugin> valueOptionsPage;

    private AdditionalInfoPage<ActionSetFactPlugin> additionalInfoPage;

    private Boolean valueOptionsPageCompleted = Boolean.FALSE;

    private ActionWrapper editingWrapper;

    private PatternWrapper patternWrapper;

    private Pattern52 editingPattern = new Pattern52();

    @Inject
    public ActionSetFactPlugin(final PatternPage patternPage,
                               final FieldPage fieldPage,
                               final ValueOptionsPage<ActionSetFactPlugin> valueOptionsPage,
                               final AdditionalInfoPage<ActionSetFactPlugin> additionalInfoPage,
                               final Event<WizardPageStatusChangeEvent> changeEvent,
                               final TranslationService translationService) {
        super(changeEvent,
              translationService);

        this.patternPage = patternPage;
        this.fieldPage = fieldPage;
        this.valueOptionsPage = valueOptionsPage;
        this.additionalInfoPage = additionalInfoPage;
    }

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
    public Boolean generateColumn() {
        final ActionWrapper actionWrapper = editingWrapper();

        if (!isValid(actionWrapper)) {
            return false;
        }

        presenter.appendColumn(actionWrapper.getActionCol52());

        return true;
    }

    boolean isValid(final ActionWrapper editingWrapper) {
        final ActionCol52 actionCol52 = editingWrapper.getActionCol52();

        if (nil(getFactField())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnPattern));
            return false;
        }

        if (nil(getFactField())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnField));
            return false;
        }

        if (nil(actionCol52.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (!unique(actionCol52.getHeader())) {
            Window.alert(translate(GuidedDecisionTableErraiConstants.ActionInsertFactPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        return true;
    }

    @Override
    public void setValueOptionsPageAsCompleted() {
        if (!isValueOptionsPageCompleted()) {
            setValueOptionsPageCompleted();

            fireChangeEvent(valueOptionsPage);
        }
    }

    void setValueOptionsPageCompleted() {
        this.valueOptionsPageCompleted = Boolean.TRUE;
    }

    @Override
    public Boolean isValueOptionsPageCompleted() {
        return valueOptionsPageCompleted;
    }

    @Override
    public PatternWrapper patternWrapper() {
        return Optional.ofNullable(patternWrapper).orElse(new PatternWrapper());
    }

    @Override
    public void setEditingPattern(final PatternWrapper patternWrapper) {
        this.patternWrapper = patternWrapper;

        resetField();

        fireChangeEvent(patternPage);
        fireChangeEvent(fieldPage);
        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public String getEntryPointName() {
        return "";
    }

    @Override
    public void setEntryPointName(final String entryPointName) {
    }

    @Override
    public List<PatternWrapper> getPatterns() {
        final Set<PatternWrapper> patterns = new HashSet<>();

        for (Pattern52 pattern52 : presenter.getModel().getPatterns()) {
            patterns.add(new PatternWrapper(pattern52));
        }

        return new ArrayList<>(patterns);
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
            editingWrapper = newActionInsertFactWrapper();
        } else {
            editingWrapper = newActionSetFactWrapper();
        }

        editingWrapper.setFactField(selectedValue);
        editingWrapper.setFactType(patternWrapper().getFactType());
        editingWrapper.setBoundName(patternWrapper().getBoundName());
        editingWrapper.setType(oracle().getFieldType(editingWrapper.getFactType(),
                                                     editingWrapper.getFactField()));

        fireChangeEvent(fieldPage);
    }

    ActionSetFactWrapper newActionSetFactWrapper() {
        return new ActionSetFactWrapper(this);
    }

    ActionInsertFactWrapper newActionInsertFactWrapper() {
        return new ActionInsertFactWrapper(this);
    }

    @Override
    public Pattern52 editingPattern() {
        editingPattern.setFactType(patternWrapper().getFactType());
        editingPattern.setBoundName(patternWrapper().getBoundName());
        editingPattern.setNegated(patternWrapper().isNegated());
        editingPattern.setEntryPointName(patternWrapper().getEntryPointName());

        return editingPattern;
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
    public boolean showUpdateEngineWithChanges() {
        return !isNewFactPattern();
    }

    @Override
    public boolean showLogicallyInsert() {
        return isNewFactPattern();
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
        return patternWrapper().getBoundName();
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

    private boolean unique(final String header) {
        for (final ActionCol52 o : presenter.getModel().getActionCols()) {
            if (o.getHeader().equals(header)) {
                return false;
            }
        }

        return true;
    }

    boolean isNewFactPattern() {
        final BRLRuleModel validator = new BRLRuleModel(presenter.getModel());

        return !validator.isVariableNameUsed(getBinding());
    }

    private AsyncPackageDataModelOracle oracle() {
        return presenter.getDataModelOracle();
    }

    ActionWrapper editingWrapper() {
        return Optional.ofNullable(editingWrapper).orElse(ActionWrapper.EMPTY_COLUMN);
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

    @Override
    public Type getType() {
        return Type.BASIC;
    }

    private void resetField() {
        editingWrapper().setFactField("");
        editingWrapper().setFactType(patternWrapper().getFactType());
        editingWrapper().setBoundName(patternWrapper().getBoundName());
        editingWrapper().setType("");
    }
}
