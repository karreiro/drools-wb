/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasFieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.FieldPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.WorkItemPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

@Dependent
public class ActionWorkItemSetFieldPlugin extends BaseDecisionTableColumnPlugin implements HasPatternPage,
                                                                                           HasFieldPage,
                                                                                           HasAdditionalInfoPage {

    @Inject
    private PatternPage factPatternsPage;

    @Inject
    private FieldPage fieldPage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    @Inject
    private WorkItemPage workItemPage;

    @Override
    public DTColumnConfig52 editingCol() {
        //TMP
        return new ActionRetractFactCol52();
    }

    @Override
    public void setHeader(String header) {

    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ActionWorkItemSetFieldPlugin_SetValue);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(factPatternsPage);
            add(fieldPage);
            add(workItemPage);
            add(additionalInfoPage());
        }};
    }

    private AdditionalInfoPage additionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
    }

    @Override
    public Boolean generateColumn() {
        return true;
    }

    @Override
    public Pattern52 editingPattern() {
        return null;
    }

    @Override
    public void setEditingPattern(final Pattern52 editingPattern) {

    }

    @Override
    public String getEntryPointName() {
        return null;
    }

    @Override
    public void setEntryPointName(String entryPointName) {

    }

    @Override
    public int constraintValue() {
        return 0;
    }

    @Override
    public String getFactField() {
        return null;
    }

    @Override
    public void setFactField(final String selectedValue) {

    }
}
