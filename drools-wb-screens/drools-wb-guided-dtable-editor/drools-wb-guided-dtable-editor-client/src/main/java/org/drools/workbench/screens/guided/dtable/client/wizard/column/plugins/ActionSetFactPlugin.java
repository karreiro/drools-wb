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
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
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
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);

        setupValues();
    }

    private void setupValues() {
        if (!isNewColumn()) {
            DTColumnConfig52 originalCol = getOriginalColumnConfig52();

            // TODO: remote ALL instanceof
            if (originalCol instanceof ActionInsertFactCol52) {
                ActionInsertFactCol52 col = (ActionInsertFactCol52) originalCol;
                String boundName = col.getBoundName();
                Optional<PatternWrapper> first = getPatterns()
                        .stream()
                        .filter(p -> p.getBoundName().equals(boundName))
                        .findFirst();

                if (first.isPresent()) {
                    patternWrapper = first.get();
                } else {
                    patternWrapper = new PatternWrapper(col.getFactType(),
                                                        col.getBoundName(),
                                                        false);
                }
                setEditingPattern(patternWrapper);

                editingWrapper = newActionInsertFactWrapper();
                editingWrapper.setFactField(col.getFactField());
                editingWrapper.setFactType(patternWrapper().getFactType());
                editingWrapper.setBoundName(patternWrapper().getBoundName());
                editingWrapper.setType(oracle().getFieldType(editingWrapper.getFactType(),
                                                             editingWrapper.getFactField()));

                setBinding(col.getBoundName());
                setValueList(col.getValueList());
                setHeader(col.getHeader());
                setInsertLogical(col.isInsertLogical());
                editingWrapper.setDefaultValue(col.getDefaultValue());
                editingWrapper.setInsertLogical(col.isInsertLogical());

                if (col instanceof LimitedEntryActionInsertFactCol52) {
                    final LimitedEntryActionInsertFactCol52 limitedO = (LimitedEntryActionInsertFactCol52) originalCol;
                    final LimitedEntryActionInsertFactCol52 limitedE = (LimitedEntryActionInsertFactCol52) editingWrapper.getActionCol52();

                    limitedE.setValue(limitedO.getValue());
                }
            } else if (originalCol instanceof ActionSetFieldCol52) {

                ActionSetFieldCol52 col = (ActionSetFieldCol52) originalCol;
                String boundName = col.getBoundName();
                Optional<PatternWrapper> first = getPatterns()
                        .stream()
                        .filter(p -> p.getBoundName().equals(boundName))
                        .findFirst();

                if (first.isPresent()) {
                    patternWrapper = first.get();
                } else {
                    patternWrapper = new PatternWrapper(col.getFactField(),
                                                        col.getBoundName(),
                                                        false);
                }
                setEditingPattern(patternWrapper);

//                new ActionSetFactWrapper(this).clone(col);

                editingWrapper = newActionSetFactWrapper();
//                editingWrapper.setFactField(col.getFactField());
                editingWrapper.setFactType(patternWrapper().getFactType());
                editingWrapper.setBoundName(patternWrapper().getBoundName());
                editingWrapper.setType(oracle().getFieldType(editingWrapper.getFactType(),
                                                             editingWrapper.getFactField()));

//                setBinding(col.getBoundName());
//                setValueList(col.getValueList());
//                setHeader(col.getHeader());
//                setUpdate(col.isUpdate());
                editingWrapper.setDefaultValue(col.getDefaultValue());
                editingWrapper.setUpdate(col.isUpdate());

//                if (col instanceof LimitedEntryCol) {
//                    final LimitedEntryActionSetFieldCol52 limitedO = (LimitedEntryActionSetFieldCol52) originalCol;
//                    final LimitedEntryActionSetFieldCol52 limitedE = (LimitedEntryActionSetFieldCol52) editingWrapper.getActionCol52();
//
//                    limitedE.setValue(limitedO.getValue());
//                }
            }

            editingWrapper.getActionCol52().setHideColumn(originalCol.isHideColumn());

            setValueOptionsPageAsCompleted();

            fireChangeEvent(patternPage);
            fireChangeEvent(fieldPage);
            fireChangeEvent(valueOptionsPage);
            fireChangeEvent(additionalInfoPage);
        }
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

        if (isNewColumn()) {
            presenter.appendColumn(actionWrapper.getActionCol52());
        } else {
            presenter.updateColumn((ActionCol52) getOriginalColumnConfig52(),
                                   editingCol());
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

        for (Object o : presenter.getModel().getActionCols()) {
            ActionCol52 col = (ActionCol52) o;
            if (col instanceof ActionInsertFactCol52) {
                ActionInsertFactCol52 c = (ActionInsertFactCol52) col;

                patterns.add(new PatternWrapper(c));
            }
        }

        return new ArrayList<>(patterns);
    }

    @Override
    public int constraintValue() {
        return BaseSingleFieldConstraint.TYPE_UNDEFINED;
    }

    @Override
    public String getFactType() {
        return patternWrapper().getFactType();
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
        editingWrapper = newActionWrapper();

        editingWrapper.setFactField(selectedValue);
        editingWrapper.setFactType(patternWrapper().getFactType());
        editingWrapper.setBoundName(patternWrapper().getBoundName());
        editingWrapper.setType(oracle().getFieldType(editingWrapper.getFactType(),
                                                     editingWrapper.getFactField()));
        fireChangeEvent(fieldPage);
    }

    private ActionWrapper newActionWrapper() {
        if (isNewFactPattern()) {
            return newActionInsertFactWrapper();
        } else {
            return newActionSetFactWrapper();
        }
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
    public Set<String> getAlreadyUsedColumnHeaders() {
        return presenter
                .getModel()
                .getActionCols()
                .stream()
                .map(DTColumnConfig52::getHeader)
                .collect(Collectors.toSet());
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
        return editingWrapper() instanceof ActionSetFactWrapper;
    }

    @Override
    public boolean showLogicallyInsert() {
        return editingWrapper() instanceof ActionInsertFactWrapper;
    }

    @Override
    public boolean isLogicallyInsert() {
        return editingWrapper.isInsertLogical();
    }

    @Override
    public boolean isUpdateEngine() {
        return editingWrapper.isUpdateEngine();
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
    public boolean isBindable() {
        return false;
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
        patternPage.disableNegatedPatterns();

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
