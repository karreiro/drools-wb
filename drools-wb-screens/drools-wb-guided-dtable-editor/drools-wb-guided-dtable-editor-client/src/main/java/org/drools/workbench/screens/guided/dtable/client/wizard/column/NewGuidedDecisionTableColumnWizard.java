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

package org.drools.workbench.screens.guided.dtable.client.wizard.column;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.SummaryPage;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.core.client.wizards.AbstractWizard;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

/**
 * Wizard for creating a Guided Decision Table
 */
@Dependent
public class NewGuidedDecisionTableColumnWizard extends AbstractWizard {

    private static final int HEIGHT = 550;
    private static final int WIDTH = 950;

    private GuidedDecisionTableView.Presenter presenter;

    private List<WizardPage> pages = new ArrayList<>();

    private Command finishCommand;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewColumn();
    }

    @Override
    public List<WizardPage> getPages() {
        return this.pages;
    }

    public void setPages( final List<WizardPage> pages ) {
        this.pages = pages;
    }

    @Override
    public Widget getPageWidget( final int pageNumber ) {
        final WizardPage wizardPage = this.pages.get( pageNumber );
        final Widget widget = wizardPage.asWidget();

        wizardPage.prepareView();

        return widget;
    }

    @Override
    public int getPreferredHeight() {
        return HEIGHT;
    }

    @Override
    public int getPreferredWidth() {
        return WIDTH;
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void start() {
        start( defaultPages() );
    }

    @Override
    public void complete() {
        super.complete();

        finishCommand.execute();
    }

    public void start( List<WizardPage> pages ) {
        this.pages = pages;

        super.start();
    }

    public ArrayList<WizardPage> defaultPages() {
        return new ArrayList<WizardPage>() {{
            add( summaryPage() );
        }};
    }

    private SummaryPage summaryPage() {
        return new SummaryPage( this, presenter ) {{
            initialise();
        }};
    }

    public void addPage( final WizardPage wizardPage ) {
        this.pages.add( wizardPage );
    }

    public void goTo( final int index ) {
        view.selectPage( index );
    }

    public void init( final GuidedDecisionTableView.Presenter presenter ) {
        this.presenter = presenter;
    }

    public void setFinishCommand( final Command finishCommand ) {
        this.finishCommand = finishCommand;
    }
}
