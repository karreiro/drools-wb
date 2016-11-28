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
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.*;

@Dependent
@Templated
public class PatternWizardPageView extends Composite implements PatternWizardPage.View {

    private PatternWizardPage page;

    @Inject
    @DataField("patternsList")
    private ListBox patternsList;

    @Override
    public void init( final PatternWizardPage page ) {
        this.page = page;

        setupPatternList( page );
    }

    @EventHandler("patternsList")
    public void onPluginSelected( ChangeEvent event ) {
        page.setEditingPattern( patternsList.getSelectedValue() );
    }

    private void setupPatternList( final PatternWizardPage page ) {
        patternsList.clear();
        patternsList.addItem( "-- Select a pattern --", "" );

        page.forEachPattern( ( item, value ) -> patternsList.addItem( item, value ) );

        patternsList.setSelectedIndex( getCurrentValueIndex( currentPatternValue(), patternsList ) );
    }

    private String currentPatternValue() {
        final Pattern52 currentPattern = page.getEditingPattern();

        return currentPattern == null ? "" : page.patternToValue( currentPattern );
    }
}
