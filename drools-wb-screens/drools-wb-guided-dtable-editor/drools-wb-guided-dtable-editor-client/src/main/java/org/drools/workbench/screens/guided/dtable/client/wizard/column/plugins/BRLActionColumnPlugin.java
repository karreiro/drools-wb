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
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.models.datamodel.rule.IAction;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLActionVariableColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasRuleModellerPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.RuleModellerPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerConfiguration;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class BRLActionColumnPlugin extends BaseDecisionTableColumnPlugin implements HasRuleModellerPage,
                                                                                    HasAdditionalInfoPage {

    private BRLColumn<IAction, BRLActionVariableColumn> editingCol = new BRLActionColumn();

    @Inject
    private RuleModellerPage ruleModellerPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.BRLActionColumnPlugin_AddActionBRL);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(ruleModellerPage);
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
        return true;
    }

    @Override
    public DTColumnConfig52 getEditingCol() {
        return (DTColumnConfig52) editingCol;
    }

    @Override
    public RuleModel getRuleModel() {
        final BRLRuleModel ruleModel = new BRLRuleModel(presenter.getModel());
        final List<IAction> definition = editingCol.getDefinition();

        ruleModel.rhs = definition.toArray(new IAction[definition.size()]);

        return ruleModel;
    }

    @Override
    public RuleModellerConfiguration getRuleModellerConfiguration() {
        return new RuleModellerConfiguration(true,
                                             false,
                                             true,
                                             true);
    }
}
