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

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionInsertFactCol52;
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
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

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

    private ActionInsertFactCol52 editingCol;

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
        if ( !isValidFactType() ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnPattern() );
            return false;
        }
        if ( !isValidFactField() ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnField() );
            return false;
        }
        if ( null == editingCol.getHeader() || "".equals( editingCol.getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
            return false;
        }

        if ( !unique( editingCol.getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
            return false;
        }

        presenter.appendColumn( editingCol );

        return true;
    }

    private boolean unique( String header ) {
        for ( ActionCol52 o : presenter.getModel().getActionCols() ) {
            if ( o.getHeader().equals( header ) ) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFactType() {
        return !( editingCol.getFactType() == null || "".equals( editingCol.getFactType() ) );
    }

    private boolean isValidFactField() {
        return !( editingCol.getFactField() == null || "".equals( editingCol.getFactField() ) );
    }

    @Override
    public Pattern52 getEditingPattern() {
        return editingPattern;
    }

    @Override
    public void setEditingPattern( final Pattern52 editingPattern ) {
        this.editingPattern = editingPattern;

        fireChangeEvent( patternPage );
        fireChangeEvent( fieldPage );
    }

    @Override
    public DTColumnConfig52 getEditingCol() {
        return editingCol;
    }

    @Override
    public void setEditingCol( final String selectedValue ) {
        editingCol = newActionInsertColumn( selectedValue );

        fireChangeEvent( fieldPage );
    }

    private ActionInsertFactCol52 newActionInsertColumn( String selectedValue ) {
        if ( nil( selectedValue ) ) {
            return null;
        }

        final ActionInsertFactCol52 insertFactCol52 = newActionInsertFact();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();

        insertFactCol52.setFactField( selectedValue );
        insertFactCol52.setType( oracle.getFieldType( getEditingPattern().getFactType(),
                                                     insertFactCol52.getFactField() ) );
        return insertFactCol52;
    }

    private ActionInsertFactCol52 newActionInsertFact() {
        switch ( presenter.getModel().getTableFormat() ) {
            case EXTENDED_ENTRY:
                return new ActionInsertFactCol52();
            case LIMITED_ENTRY:
                return new LimitedEntryActionInsertFactCol52();
            default:
                throw new UnsupportedOperationException( "Unsupported table format: " + presenter.getModel().getTableFormat() );
        }
    }

    @Override
    public void setEditingCol( final DTColumnConfig52 editingCol ) {
        this.editingCol = (ActionInsertFactCol52) editingCol;
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

    @Override
    public String getFactField() {
        return editingCol == null ? "" : editingCol.getFactField();
    }
}
