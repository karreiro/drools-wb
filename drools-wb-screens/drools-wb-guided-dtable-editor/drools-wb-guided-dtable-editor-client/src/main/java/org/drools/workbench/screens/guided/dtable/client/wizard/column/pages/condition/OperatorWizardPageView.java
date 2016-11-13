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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class OperatorWizardPageView extends Composite implements OperatorWizardPage.View {

    private OperatorWizardPage page;

    @Inject
    @DataField("operatorsList")
    private ListBox operatorsList;

    @Override
    public void init( final OperatorWizardPage page ) {
        this.page = page;

        setupPatternList();
    }

    private void setupPatternList() {
        operatorsList.clear();
        operatorsList.addItem( "-- Select an operator --", "" );

        page.forEachOperator( ( item, value ) -> operatorsList.addItem( item, value ) );
    }
}
