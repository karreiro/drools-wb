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
import java.util.function.Supplier;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.SummaryPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.core.client.wizards.AbstractWizard;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;
import org.uberfire.ext.widgets.core.client.wizards.WizardView;

/**
 * Wizard for creating a Guided Decision Table
 */
@Dependent
public class NewGuidedDecisionTableColumnWizard extends AbstractWizard {

    private List<WizardPage> pages = new ArrayList<>();

    private Supplier<Boolean> finishCommand;

    @Inject
    private WizardView view;

    @Inject
    private SummaryPage summaryPage;

    private GuidedDecisionTableView.Presenter presenter;

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
        return 600;
    }

    @Override
    public int getPreferredWidth() {
        return 900;
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        //Assume complete
        callback.callback( true );

        for ( WizardPage page : this.pages ) {
            page.isComplete( result -> {
                if ( Boolean.FALSE.equals( result ) ) {
                    callback.callback( false );
                }
            } );
        }
    }

    @Override
    public void start() {
        start( new ArrayList<>() );
    }

    public void start( List<WizardPage> pages ) {
        this.pages = new ArrayList<WizardPage>() {{
            add( summaryPage );
            addAll( pages );
        }};

        for ( WizardPage page : this.pages ) {
            ( (BaseDecisionTableColumnPage) page ).init( this );
        }

        super.start();
    }

    public void start( final BaseDecisionTableColumnPlugin plugin ) {
//        this.pages = new ArrayList<WizardPage>() {{
//            add( summaryPage );
//            addAll( plugin.getPages() );
//        }};
//
//        plugin.init( this );
//
//        super.start();

        this.pages = new ArrayList<WizardPage>() {{
            add( summaryPage );
            addAll( plugin.getPages() );
        }};

        for ( WizardPage page : this.pages ) {
            ( (BaseDecisionTableColumnPage) page ).init( this );
            ( (BaseDecisionTableColumnPage) page ).init( plugin );
        }

        plugin.init( this );

        super.start();
    }

    @Override
    public void complete() {
        if ( finishCommand.get() ) {
            super.complete();
        }
    }

    private WizardView getView() {
        return view;
    }

    public void goTo( final int index ) {
        getView().selectPage( index );
    }

    public void init( final GuidedDecisionTableView.Presenter presenter ) {
        this.presenter = presenter;
    }

    public void setFinishCommand( final Supplier<Boolean> finishCommand ) {
        this.finishCommand = finishCommand;
    }

    public GuidedDecisionTableView.Presenter getPresenter() {
        return presenter;
    }

}
