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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.GuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.AbstractDecisionTableColumnPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

/**
 * A summary page for the guided Decision Table Wizard
 */
@Dependent
public class SummaryPage extends AbstractDecisionTableColumnPage {

    public interface View extends UberView<SummaryPage> {

    }

    @Inject
    private Instance<AbstractDecisionTableColumnPlugin> plugins;

    @Inject
    private View view;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewColumn();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void init( final GuidedDecisionTableColumnWizard wizard ) {
        super.init( wizard );
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    void openPage( final String selectedItemText ) {
        final AbstractDecisionTableColumnPlugin plugin = findPluginByIdentifier( selectedItemText );

        plugin.init( wizard );

        wizard.start( plugin.getPages() );
        wizard.goTo( 1 );
    }

    private AbstractDecisionTableColumnPlugin findPluginByIdentifier( final String selectedItemText ) {
        for ( AbstractDecisionTableColumnPlugin plugin : this.plugins ) {
            if ( plugin.getIdentifier().equals( selectedItemText ) ) {
                return plugin;
            }
        }

        throw new UnsupportedOperationException( "The plugin " + selectedItemText + " does not have an implementation." );
    }

    public Instance<AbstractDecisionTableColumnPlugin> getPlugins() {
        return plugins;
    }
}
