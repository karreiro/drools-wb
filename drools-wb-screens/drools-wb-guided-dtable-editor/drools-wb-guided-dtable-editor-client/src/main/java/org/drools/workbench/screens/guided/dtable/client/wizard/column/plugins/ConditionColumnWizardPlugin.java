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

import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.AdditionalInfoWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.CalculationTypeWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.ConditionColumnWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.FactFieldWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.OperatorWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.PatternWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.ValueOptionsWizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class ConditionColumnWizardPlugin extends AbstractDecisionTableColumnPlugin {

    @Inject
    private ConditionColumnWizardPage conditionColumnWizardPage;

    @Inject
    private PatternWizardPage patternWizardPage;

    @Inject
    private CalculationTypeWizardPage calculationTypeWizardPage;

    @Inject
    private FactFieldWizardPage factFieldWizardPage;

    @Inject
    private OperatorWizardPage operatorWizardPage;

    @Inject
    private AdditionalInfoWizardPage additionalInfoWizardPage;

    @Inject
    private ValueOptionsWizardPage valueOptionsWizardPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.NewConditionColumn();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( conditionColumnWizardPage );
            add( patternWizardPage );
            add( calculationTypeWizardPage );
            add( factFieldWizardPage );
            add( operatorWizardPage );
            add( valueOptionsWizardPage );
            add( additionalInfoWizardPage );
        }};
    }

    @Override
    public Boolean generateColumn() {
        return true;
    }

    public Pattern52 getEditingPattern() {
        return editingPattern;
    }

    public void setEditingPattern( final Pattern52 editingPattern ) {
        this.editingPattern = editingPattern;
    }

    public ConditionCol52 getEditingCol() {
        return editingCol;
    }

    public void setEditingCol( final ConditionCol52 editingCol ) {
        this.editingCol = editingCol;
    }
}
