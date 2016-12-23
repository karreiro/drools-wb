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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.*;

@Dependent
@Templated
public class FieldWizardPageView extends Composite implements FieldWizardPage.View {

    private FieldWizardPage page;

    @Inject
    @DataField("fieldsList")
    private ListBox fieldsList;

    @Inject
    @DataField("warning")
    private Div warning;

    @Inject
    @DataField("info")
    private Div info;

    @Override
    public void init( final FieldWizardPage page ) {
        this.page = page;

        setupFieldList();

        toggleFields();
    }

    @EventHandler("fieldsList")
    public void onPluginSelected( ChangeEvent event ) {
        page.setEditingCol( fieldsList.getSelectedValue() );
    }

    private void toggleFields() {
        if ( page.isConstraintValuePredicate() ) {
            info.setHidden( false );
            warning.setHidden( true );
            fieldsList.setEnabled( false );
        } else {
            info.setHidden( true );
            warning.setHidden( page.hasEditingPattern() );
            fieldsList.setEnabled( page.hasEditingPattern() );
        }
    }

    private void setupFieldList() {
        fieldsList.clear();
        fieldsList.addItem( "-- Select a field --", "" );

        page.factFields().forEach( fieldName -> fieldsList.addItem( fieldName ) );

        fieldsList.setSelectedIndex( getCurrentValueIndex( getCurrentValue(), fieldsList ) );
    }

    private String getCurrentValue() {
        final ConditionCol52 editingCol = page.getEditingCol();

        return editingCol == null ? "" : editingCol.getFactField();
    }
}