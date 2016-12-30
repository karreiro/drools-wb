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
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.ActionUpdateFactPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public class ActionUpdateFactPlugin implements DecisionTableColumnPlugin {

    private ActionUpdateFactPage page;

    public ActionUpdateFactPlugin( final NewGuidedDecisionTableColumnWizard wizard,
                                   final GuidedDecisionTableView.Presenter presenter ) {
        wizard.setFinishCommand( this::generateColumn );

        page = new ActionUpdateFactPage( wizard, presenter ) {{
            initialise();
        }};
    }

    @Override
    public String getTitle() {
        return "Add new Condition column";
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( page );
        }};
    }

    @Override
    public Boolean generateColumn() {
        if ( !page.isValidFactType() ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnFact() );
            return false;
        }
        if ( !page.isValidFactField() ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnField() );
            return false;
        }
        if ( null == page.getEditingCol().getHeader() || "".equals( page.getEditingCol().getHeader() ) ) {
            Window.alert( GuidedDecisionTableConstants.INSTANCE.YouMustEnterAColumnHeaderValueDescription() );
            return false;
        }

        if ( page.isNew() ) {
            if ( !page.unique( page.getEditingCol().getHeader() ) ) {
                Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                return false;
            }

        } else {
            if ( !page.getOriginalCol().getHeader().equals( page.getEditingCol().getHeader() ) ) {
                if ( !page.unique( page.getEditingCol().getHeader() ) ) {
                    Window.alert( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
                    return false;
                }
            }
        }

        page.refreshGrid();

        return true;
    }
}