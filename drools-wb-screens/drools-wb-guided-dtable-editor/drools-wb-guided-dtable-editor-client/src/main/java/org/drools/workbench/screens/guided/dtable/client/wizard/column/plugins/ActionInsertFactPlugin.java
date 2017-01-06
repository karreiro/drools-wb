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
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class ActionInsertFactPlugin extends BaseDecisionTableColumnPlugin implements HasFieldPage,
                                                                                     HasPatternPage,
                                                                                     HasAdditionalInfoPage {

    @Inject
    private PatternPage patternPage;

    @Inject
    private FieldPage fieldPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    private Pattern52 editingPattern;

    private ConditionCol52 editingCol;

    private int constraintValue;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.SetTheValueOfAField();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( patternPage );
            add( fieldPage );
            add( additionalInfoPage() );
        }};
    }

    private AdditionalInfoPage additionalInfoPage() {
        return additionalInfoPageInitializer().init( this );
    }

    private AdditionalInfoPageInitializer additionalInfoPageInitializer() {
        return new AdditionalInfoPageInitializer( additionalInfoPage );
    }

    @Override
    public void init( final NewGuidedDecisionTableColumnWizard wizard ) {
        super.init( wizard );
    }

    @Override
    public Boolean generateColumn() {
        return true;
//        if ( !page.isValidFactType() ) {
//            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnPattern() );
//            return false;
//        }
//        if ( !page.isValidFactField() ) {
//            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnField() );
//            return false;
//        }
//        if ( null == page.getEditingCol().getHeader() || "".equals( page.getEditingCol().getHeader() ) ) {
//            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
//            return false;
//        }
//
//        if ( page.isNew() ) {
//            if ( !page.unique( page.getEditingCol().getHeader() ) ) {
//                Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
//                return false;
//            }
//
//        } else {
//            if ( !page.getEditingCol().getHeader().equals( page.getEditingCol().getHeader() ) ) {
//                if ( !page.unique( page.getEditingCol().getHeader() ) ) {
//                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
//                    return false;
//                }
//            }
//        }
//
//        page.refreshGrid();
//        return true;

        // Pass new\modified column back for handling
//        refreshGrid.execute( editingCol );
//        hide();
    }

    @Override
    public Pattern52 getEditingPattern() {
        return editingPattern;
    }

    @Override
    public void setEditingPattern( final Pattern52 editingPattern ) {
        this.editingPattern = editingPattern;
    }

    @Override
    public ConditionCol52 getEditingCol() {
        return editingCol;
    }

    @Override
    public void setEditingCol( final ConditionCol52 editingCol ) {
        this.editingCol = editingCol;
    }

    @Override
    public void setEditingColOperator( final String operator ) {
        if ( editingCol == null ) {
            return;
        }

        editingCol.setOperator( operator );
    }

    @Override
    public void setEditingColFactField( final String factField ) {
        if ( editingCol == null ) {
            return;
        }

        editingCol.setFactField( factField );
    }

    @Override
    public int getConstraintValue() {
        return constraintValue;
    }
}
