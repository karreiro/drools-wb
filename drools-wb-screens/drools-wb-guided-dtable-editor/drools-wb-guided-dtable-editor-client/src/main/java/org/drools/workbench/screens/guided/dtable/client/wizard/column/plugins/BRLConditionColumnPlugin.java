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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.IPattern;
import org.drools.workbench.models.datamodel.rule.InterpolationVariable;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.models.datamodel.rule.visitors.RuleModelVisitor;
import org.drools.workbench.models.guided.dtable.shared.model.BRLColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLConditionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLConditionVariableColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryBRLConditionColumn;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasRuleModellerPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.RuleModellerPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerConfiguration;
import org.drools.workbench.screens.guided.rule.client.editor.events.TemplateVariablesChangedEvent;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class BRLConditionColumnPlugin extends BaseDecisionTableColumnPlugin implements HasRuleModellerPage,
                                                                                       HasAdditionalInfoPage,
                                                                                       TemplateVariablesChangedEvent.Handler {

    private BRLColumn<IPattern, BRLConditionVariableColumn> editingCol;

    @Inject
    private RuleModellerPage ruleModellerPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    private Boolean ruleModellerPageCompleted = Boolean.FALSE;

    private HandlerRegistration registration;

    private RuleModel ruleModel = null;

    @Override
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);

        editingCol = newBRLCondition();
        registration = presenter.getEventBus().addHandler(TemplateVariablesChangedEvent.TYPE,
                                                          this);
    }

    @Override
    public void onClose() {
        super.onClose();

        registration.removeHandler();
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.BRLConditionColumnPlugin_AddConditionBRL);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(ruleModellerPage);
            add(additionalInfoPage());
        }};
    }

    @Override
    public Boolean generateColumn() {

        if (nil(editingCol.getHeader())) {
            Window.alert(GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription());
            return false;
        }

        if (!isHeaderUnique(editingCol.getHeader())) {
            Window.alert(GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother());
            return false;
        }

        //Ensure variables reflect (name) changes made in RuleModeller
        getDefinedVariables(this.getRuleModel());

        this.editingCol.setDefinition(Arrays.asList(this.getRuleModel().lhs));
        presenter.appendColumn((BRLConditionColumn) this.editingCol);

        return true;
    }

    protected boolean isHeaderUnique(String header) {
        for (CompositeColumn<?> cc : presenter.getModel().getConditions()) {
            for (int iChild = 0; iChild < cc.getChildColumns().size(); iChild++) {
                if (cc.getChildColumns().get(iChild).getHeader().equals(header)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean getDefinedVariables(RuleModel ruleModel) {
        Map<InterpolationVariable, Integer> ivs = new HashMap<InterpolationVariable, Integer>();
        RuleModelVisitor rmv = new RuleModelVisitor(ivs);
        rmv.visit(ruleModel);

        //Update column and UI
        editingCol.setChildColumns(convertInterpolationVariables(ivs));

        return ivs.size() > 0;
    }

    private List<BRLConditionVariableColumn> convertInterpolationVariables(Map<InterpolationVariable, Integer> ivs) {

        //If there are no variables add a boolean column to specify whether the fragment should apply
        if (ivs.isEmpty()) {
            BRLConditionVariableColumn variable = new BRLConditionVariableColumn("",
                                                                                 DataType.TYPE_BOOLEAN);
            variable.setHeader(editingCol.getHeader());
            variable.setHideColumn(editingCol.isHideColumn());
            List<BRLConditionVariableColumn> variables = new ArrayList<BRLConditionVariableColumn>();
            variables.add(variable);
            return variables;
        }

        //Convert to columns for use in the Decision Table
        BRLConditionVariableColumn[] variables = new BRLConditionVariableColumn[ivs.size()];
        for (Map.Entry<InterpolationVariable, Integer> me : ivs.entrySet()) {
            InterpolationVariable iv = me.getKey();
            int index = me.getValue();
            BRLConditionVariableColumn variable = new BRLConditionVariableColumn(iv.getVarName(),
                                                                                 iv.getDataType(),
                                                                                 iv.getFactType(),
                                                                                 iv.getFactField());
            variable.setHeader(editingCol.getHeader());
            variable.setHideColumn(editingCol.isHideColumn());
            variables[index] = variable;
        }

        //Convert the array into a mutable list (Arrays.toList provides an immutable list)
        List<BRLConditionVariableColumn> variableList = new ArrayList<BRLConditionVariableColumn>();
        for (BRLConditionVariableColumn variable : variables) {
            variableList.add(variable);
        }
        return variableList;
    }

    @Override
    public DTColumnConfig52 editingCol() {
        return (DTColumnConfig52) editingCol;
    }

    @Override
    public String getHeader() {
        return editingCol.getHeader();
    }

    @Override
    public void setHeader(String header) {
        editingCol.setHeader(header);

        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public void setInsertLogical(Boolean value) {
        // empty
    }

    @Override
    public void setUpdate(Boolean value) {
        // empty
    }

    @Override
    public RuleModel getRuleModel() {
        ruleModel = Optional.ofNullable(ruleModel).orElse(newRuleModel());

        return ruleModel;
    }

    private RuleModel newRuleModel() {
        final BRLRuleModel ruleModel = new BRLRuleModel(presenter.getModel());
        final List<IPattern> definition = editingCol.getDefinition();

        ruleModel.lhs = definition.toArray(new IPattern[definition.size()]);

        return ruleModel;
    }

    @Override
    public RuleModellerConfiguration getRuleModellerConfiguration() {
        return new RuleModellerConfiguration(false,
                                             true,
                                             true,
                                             true);
    }

    @Override
    public void setRuleModellerPageAsCompleted() {
        if (!isRuleModellerPageCompleted()) {
            setRuleModellerPageCompleted();

            fireChangeEvent(ruleModellerPage);
        }
    }

    private void setRuleModellerPageCompleted() {
        this.ruleModellerPageCompleted = Boolean.TRUE;
    }

    @Override
    public Boolean isRuleModellerPageCompleted() {
        return ruleModellerPageCompleted;
    }

    private BRLColumn<IPattern, BRLConditionVariableColumn> newBRLCondition() {
        switch (tableFormat()) {
            case EXTENDED_ENTRY:
                return new BRLConditionColumn();
            case LIMITED_ENTRY:
                return new LimitedEntryBRLConditionColumn();
            default:
                throw new UnsupportedOperationException("Unsupported table format: " + tableFormat());
        }
    }

    @Override
    public GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
    }

    private AdditionalInfoPage additionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
    }

    @Override
    public void onTemplateVariablesChanged(TemplateVariablesChangedEvent event) {
        if (event.getSource() == getRuleModel()) {
            getDefinedVariables(event.getModel());
        }
    }
}