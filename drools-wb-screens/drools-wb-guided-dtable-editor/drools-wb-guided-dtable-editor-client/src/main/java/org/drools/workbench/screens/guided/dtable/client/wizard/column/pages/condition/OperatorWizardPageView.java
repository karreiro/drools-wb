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
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.*;

@Dependent
@Templated
public class OperatorWizardPageView extends Composite implements OperatorWizardPage.View {

    private OperatorWizardPage page;

    @Inject
    @DataField("warning")
    private Div warning;

    @Inject
    @DataField("info")
    private Div info;

    @Inject
    @DataField("operatorsList")
    private ListBox operatorsList;

    @EventHandler("operatorsList")
    public void onPluginSelected( ChangeEvent event ) {
        page.setOperator( operatorsList.getSelectedValue() );
    }

    @Override
    public void init( final OperatorWizardPage page ) {
        this.page = page;

        setupPatternList();

        toggleFields();
    }

    private void toggleFields() {
        if ( page.isConstraintValuePredicate() ) {
            info.setHidden( false );
            warning.setHidden( true );
            operatorsList.setEnabled( false );
        } else {
            info.setHidden( true );
            warning.setHidden( page.canSetOperator() );
            operatorsList.setEnabled( page.canSetOperator() );
        }
    }

    private void setupPatternList() {
        operatorsList.clear();
        operatorsList.addItem( "-- Select an operator --", "" );

        page.forEachOperator( ( item, value ) -> operatorsList.addItem( item, value ) );

        operatorsList.setSelectedIndex( getCurrentValueIndex( page.getOperator(), operatorsList ) );
    }
}
