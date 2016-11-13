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

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.AbstractDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnWizardPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class PatternWizardPage extends AbstractDecisionTableColumnPage {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.Pattern();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    void forEachPattern( final BiConsumer<String, String> biConsumer ) {
        final Set<String> addedBounds = new HashSet<>();

        presenter.getModel().getPatterns().forEach( pattern52 -> {
            if ( !addedBounds.contains( pattern52.getBoundName() ) ) {
                final String item = patternName( pattern52 );
                final String value = patternValue( pattern52 );

                biConsumer.accept( item, value );

                addedBounds.add( pattern52.getBoundName() );
            }
        } );
    }

    private String patternValue( final Pattern52 pattern52 ) {
        return pattern52.getFactType() + " " + pattern52.getBoundName() + " " + pattern52.isNegated();
    }

    private String patternName( final Pattern52 pattern52 ) {
        final String prefix = pattern52.isNegated() ? GuidedDecisionTableConstants.INSTANCE.negatedPattern() + " " : "";

        return prefix + pattern52.getFactType() + " [" + pattern52.getBoundName() + "]";
    }

    public void setEditingPattern( final String selectedValue ) {
        final ConditionColumnWizardPlugin conditionColumnWizardPlugin = (ConditionColumnWizardPlugin) plugin;
        final String boundName = selectedValue.split( "\\s" )[ 1 ];

        conditionColumnWizardPlugin.setEditingPattern( presenter.getModel().getConditionPattern( boundName ) );
    }

    public interface View extends UberView<PatternWizardPage> {

    }
}
