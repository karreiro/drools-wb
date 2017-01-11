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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.MetaDataColumnWizardPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class MetaDataColumnWizardPlugin extends BaseDecisionTableColumnPlugin {

    @Inject
    private MetaDataColumnWizardPage page;

    @Override
    public String getTitle() {
        return GuidedDecisionTableConstants.INSTANCE.AddNewMetadata();
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add( page );
        }};
    }

    @Override
    public Boolean generateColumn() {
        if ( !page.isValidMetadata() ) {
            return false;
        }

//        final MetadataCol52 column = new MetadataCol52() {{
//            setMetadata( page.getMetadataValue() );
//            setHideColumn( true );
//        }};

        final MetadataCol52 column = new MetadataCol52();

        column.setMetadata( page.getMetadataValue() );
        column.setHideColumn( true );

        presenter.appendColumn( column );

        return true;
    }
}
