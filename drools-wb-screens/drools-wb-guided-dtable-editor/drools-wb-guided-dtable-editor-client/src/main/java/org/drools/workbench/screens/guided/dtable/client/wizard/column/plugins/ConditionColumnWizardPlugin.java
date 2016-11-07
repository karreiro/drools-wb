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

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.GuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ConditionColumnPickFactTypeWizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public class ConditionColumnWizardPlugin implements DecisionTableColumnGeneratorWizardPlugin {

    //These pages contain the Widgets to define the column
    private ConditionColumnPickFactTypeWizardPage factTypePage;
//    private WizardPage fieldPage;
//    private WizardPage operatorPage;

    public ConditionColumnWizardPlugin( final GuidedDecisionTableColumnWizard wizard,
                                        final GuidedDecisionTableView.Presenter presenter ) {
        wizard.setFinishCommand( this::generateColumn );

        factTypePage = new ConditionColumnPickFactTypeWizardPage( wizard, presenter ) {{
            initialise();
        }};
//        fieldPage = new ConditionColumnPickFieldWizardPage();
//        operatorPage = new ConditionColumnPickOperatorWizardPage();
    }

    @Override
    public String getTitle() {
        return "Add new Condition column";
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( factTypePage );
//            add( fieldPage );
//            add( operatorPage );
        }};
    }

    @Override
    public Boolean generateColumn() {
        final ConditionCol52 editingCol = factTypePage.getEditingCol();
        final ConditionCol52 originalCol = factTypePage.getOriginalCol();

        if ( null == editingCol.getHeader() || "".equals( editingCol.getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
            return false;
        }
        if ( editingCol.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_PREDICATE ) {

            //Field mandatory for Literals and Formulae
            if ( null == editingCol.getFactField() || "".equals( editingCol.getFactField() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.PleaseSelectOrEnterField() );
                return false;
            }

            //Operator optional for Literals and Formulae
            if ( editingCol.getOperator() == null ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.NotifyNoSelectedOperator() );
                return false;
            }

        } else {

            //Clear operator for predicates, but leave field intact for interpolation of $param values
            editingCol.setOperator( null );
        }

        //Check for unique binding
        if ( factTypePage.isNew() ) {
            if ( editingCol.isBound() && !factTypePage.isBindingUnique( editingCol.getBinding() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern() );
                return false;
            }
        } else {
            if ( originalCol.isBound() && editingCol.isBound() ) {
                if ( !originalCol.getBinding().equals( editingCol.getBinding() ) ) {
                    if ( editingCol.isBound() && !factTypePage.isBindingUnique( editingCol.getBinding() ) ) {
                        Window.alert( GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern() );
                        return false;
                    }
                }
            }
        }

        //Check column header is unique
        if ( factTypePage.isNew() ) {
            if ( !factTypePage.unique( editingCol.getHeader() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                return false;
            }
        } else {
            if ( !originalCol.getHeader().equals( editingCol.getHeader() ) ) {
                if ( !factTypePage.unique( editingCol.getHeader() ) ) {
                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                    return false;
                }
            }
        }

        //Clear binding if column is not a literal
        if ( editingCol.getConstraintValueType() != BaseSingleFieldConstraint.TYPE_LITERAL ) {
            editingCol.setBinding( null );
        }

        // Pass new\modified column back for handling
        factTypePage.refreshGrid();
        return true;


//        hide();

    }
}
