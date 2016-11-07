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
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.rule.client.resources.GuidedRuleEditorResources;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class MetaDataColumnWizardPage extends AbstractDecisionTableColumnPage {

    public interface View extends UberView<MetaDataColumnWizardPage> {

        String getMetadataText();

        void showError( String errorMessage );
    }

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.NewMetadata();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    public boolean isValidMetadata() {

        final String metaData = getMetadataValue().trim();

        if ( metaData.isEmpty() ) {
            view.showError( GuidedRuleEditorResources.CONSTANTS.MetadataNameEmpty() );
            return false;
        }

        if ( !presenter.isMetaDataUnique( metaData ) ) {
            view.showError( GuidedDecisionTableConstants.INSTANCE.ThatColumnNameIsAlreadyInUsePleasePickAnother() );
            return false;
        }

        return true;
    }

    public String getMetadataValue() {
        return view.getMetadataText();
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }
}
