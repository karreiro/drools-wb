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

import com.google.gwt.core.client.GWT;
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
public class FactFieldWizardPageView extends Composite implements FactFieldWizardPage.View {

    private FactFieldWizardPage page;

    @Inject
    @DataField("fieldsList")
    private ListBox fieldsList;

    @Inject
    @DataField("warning")
    private Div warning;

    @Override
    public void init( final FactFieldWizardPage page ) {
        this.page = page;

        setupFieldList();

        enableFields( page.canSetFactField() );
    }

    @EventHandler("fieldsList")
    public void onPluginSelected( ChangeEvent event ) {
        page.setEditingCol( fieldsList.getSelectedValue() );
    }

    private void enableFields( final boolean enabled ) {
        warning.setHidden( enabled );
        fieldsList.setEnabled( enabled );
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
