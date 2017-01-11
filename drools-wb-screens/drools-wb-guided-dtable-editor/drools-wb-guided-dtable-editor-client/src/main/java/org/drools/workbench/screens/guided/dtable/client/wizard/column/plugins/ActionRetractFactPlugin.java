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
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class ActionRetractFactPlugin extends BaseDecisionTableColumnPlugin implements HasAdditionalInfoPage {

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    private ActionRetractFactCol52 editingCol;

    @Override
    public void init( final NewGuidedDecisionTableColumnWizard wizard ) {
        super.init( wizard );

        ActionRetractFactCol52 column = null;

        switch ( presenter.getModel().getTableFormat() ) {
            case EXTENDED_ENTRY:
                column = new ActionRetractFactCol52();
                break;
            case LIMITED_ENTRY:
                column = new LimitedEntryActionRetractFactCol52();
                ( (LimitedEntryActionRetractFactCol52) column ).setValue( new DTCellValue52( "" ) );
                break;
        }

        editingCol = cloneActionRetractColumn( column );

    }

    private ActionRetractFactCol52 cloneActionRetractColumn( ActionRetractFactCol52 col ) {
        final ActionRetractFactCol52 clone;

        if ( col instanceof LimitedEntryCol ) {
            clone = new LimitedEntryActionRetractFactCol52();
            DTCellValue52 dcv = new DTCellValue52( ( (LimitedEntryCol) col ).getValue().getStringValue() );
            ( (LimitedEntryCol) clone ).setValue( dcv );
        } else {
            clone = new ActionRetractFactCol52();
        }

        clone.setHeader( col.getHeader() );
        clone.setHideColumn( col.isHideColumn() );

        return clone;
    }

    @Override
    public String getTitle() {
        return "Delete an existing fact";
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
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
    public Boolean generateColumn() {
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

    public boolean unique( String header ) {
        for ( ActionCol52 o : presenter.getModel().getActionCols() ) {
            if ( o.getHeader().equals( header ) ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public DTColumnConfig52 getEditingCol() {
        return editingCol;
    }
}
