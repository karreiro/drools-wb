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
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.ConditionColumnWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.PatternWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.CalculationTypeWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.FieldWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.OperatorWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.ValueOptionsWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.AdditionalInfoWizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPageStatusChangeEvent;

@Dependent
public class ConditionColumnWizardPlugin extends AbstractDecisionTableColumnPlugin {

    @Inject
    private ConditionColumnWizardPage conditionColumnWizardPage;

    @Inject
    private PatternWizardPage patternWizardPage;

    @Inject
    private CalculationTypeWizardPage calculationTypeWizardPage;

    @Inject
    private FieldWizardPage fieldWizardPage;

    @Inject
    private OperatorWizardPage operatorWizardPage;

    @Inject
    private AdditionalInfoWizardPage additionalInfoWizardPage;

    @Inject
    private ValueOptionsWizardPage valueOptionsWizardPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    private int constraintValue;
    @Inject
    private Event<WizardPageStatusChangeEvent> changeEvent;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.NewConditionColumn();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( patternWizardPage );
            add( calculationTypeWizardPage );
            add( fieldWizardPage );
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

        WizardPage patternWizardPage = this.patternWizardPage;
        fireChangeEvent( patternWizardPage );
        fireChangeEvent( fieldWizardPage );
        fireChangeEvent( operatorWizardPage );
    }

    public ConditionCol52 getEditingCol() {
        return editingCol;
    }

    public void setEditingCol( final ConditionCol52 editingCol ) {
        this.editingCol = editingCol;

        fireChangeEvent( fieldWizardPage );
        fireChangeEvent( operatorWizardPage );
    }

    public void setEditingColOperator( String operator ) {
        if (editingCol == null) {
            return;
        }

        editingCol.setOperator( operator );

        fireChangeEvent( operatorWizardPage );
        fireChangeEvent( valueOptionsWizardPage );
        fireChangeEvent( additionalInfoWizardPage );
    }

    public void setEditingColFactField( final String factField ) {
        if (editingCol == null) {
            return;
        }

        editingCol.setFactField( factField );

        fireChangeEvent( operatorWizardPage );
        fireChangeEvent( valueOptionsWizardPage );
        fireChangeEvent( additionalInfoWizardPage );
    }

    public int getConstraintValue() {
        return constraintValue;
    }

    public void setConstraintValue( final int constraintValue ) {
        this.constraintValue = constraintValue;

        fireChangeEvent( calculationTypeWizardPage );
    }

    private void fireChangeEvent( final WizardPage wizardPage ) {
        changeEvent.fire( new WizardPageStatusChangeEvent( wizardPage ) );
    }
}
