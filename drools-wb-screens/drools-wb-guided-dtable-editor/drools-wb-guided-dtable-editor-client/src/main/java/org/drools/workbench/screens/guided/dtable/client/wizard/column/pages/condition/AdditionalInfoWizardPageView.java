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
import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.screens.guided.rule.client.editor.CEPWindowOperatorsDropdown;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.html.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class AdditionalInfoWizardPageView extends Composite implements AdditionalInfoWizardPage.View {

    @Inject
    @DataField("entryPointName")
    TextBox entryPointName;

    @Inject
    @DataField("header")
    TextBox header;

    @DataField("hideColumnContainer")
    Div hideColumnContainer = new Div();

    private AdditionalInfoWizardPage page;

    private CheckBox hideColumnCheckBox;

    @Override
    public void init( final AdditionalInfoWizardPage page ) {
        this.page = page;

        setupHideColumnContainer();
    }

    private void setupHideColumnContainer() {
        hideColumnCheckBox = page.newHideColumnCheckBox();

        hideColumnContainer.clear();
        hideColumnContainer.add( hideColumnCheckBox );
    }
}
