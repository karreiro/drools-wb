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

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.AbstractDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class AdditionalInfoWizardPage extends AbstractDecisionTableColumnPage<ConditionColumnWizardPlugin> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return "Additional info";
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( false ); // TODO
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    CheckBox newHideColumnCheckBox() {
        return DTCellValueWidgetFactory.getHideColumnIndicator( plugin().getEditingCol() );
    }

    Boolean canSetupHideColumnCheckBox() {
        return plugin().getEditingCol() != null;
    }

    public interface View extends UberView<AdditionalInfoWizardPage> {

    }
}
