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
import java.util.function.BiConsumer;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasWorkItemPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.WorkItemPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ActionWorkItemPlugin extends BaseDecisionTableColumnPlugin implements HasWorkItemPage,
                                                                                   HasAdditionalInfoPage {

    @Inject
    private AdditionalInfoPage<ActionWorkItemPlugin> additionalInfoPage;

    @Inject
    private WorkItemPage workItemPage;

    private ActionWorkItemCol52 editingCol;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ActionWorkItemPlugin_ExecuteWorkItem);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            add(workItemPage());
            add(additionalInfoPage());
        }};
    }

    @Override
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);

        editingCol = new ActionWorkItemCol52();
    }

    @Override
    public String getWorkItem() {
        final boolean hasWorkItemDefinition = getWorkItemDefinition() != null;

        if (hasWorkItemDefinition) {
            return getWorkItemDefinition().getName();
        } else {
            return "";
        }
    }

    @Override
    public void setWorkItem(final String workItem) {
        if (!nil(workItem)) {
            editingCol.setWorkItemDefinition(findWorkItemDefinition(workItem));
        } else {
            editingCol.setWorkItemDefinition(null);
        }

        fireChangeEvent(workItemPage);
    }

    @Override
    public ActionWorkItemCol52 editingCol() {
        return editingCol;
    }

    @Override
    public String getHeader() {
        return editingCol().getHeader();
    }

    @Override
    public void setHeader(final String header) {
        editingCol().setHeader(header);

        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public Boolean isWorkItemSet() {
        return editingCol.getWorkItemDefinition() != null;
    }

    @Override
    public PortableWorkDefinition getWorkItemDefinition() {
        return editingCol().getWorkItemDefinition();
    }

    @Override
    public void forEachWorkItem(final BiConsumer<String, String> biConsumer) {
        presenter
                .getWorkItemDefinitions()
                .forEach(workDefinition -> biConsumer.accept(workDefinition.getDisplayName(),
                                                             workDefinition.getName()));
    }

    @Override
    public void setWorkItemPageAsCompleted() {
        // empty
    }

    @Override
    public void setInsertLogical(final Boolean value) {
        // empty
    }

    @Override
    public void setUpdate(final Boolean value) {
        // empty
    }

    @Override
    public boolean showUpdateEngineWithChanges() {
        return false;
    }

    @Override
    public boolean showLogicallyInsert() {
        return false;
    }

    PortableWorkDefinition findWorkItemDefinition(final String workItem) {
        final List<PortableWorkDefinition> workItemDefinitions = new ArrayList<>(presenter.getWorkItemDefinitions());

        return workItemDefinitions
                .stream()
                .filter(a -> a.getName().equals(workItem))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Boolean generateColumn() {
        if (!isValid()) {
            return false;
        }

        presenter.appendColumn(editingCol());

        return true;
    }

    boolean isValid() {
        if (nil(editingCol().getHeader())) {
            showError(translate(GuidedDecisionTableErraiConstants.ActionWorkItemPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (!unique(editingCol().getHeader())) {
            showError(translate(GuidedDecisionTableErraiConstants.ActionWorkItemPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        return true;
    }

    void showError(final String errorMessage) {
        Window.alert(errorMessage);
    }

    boolean unique(String header) {
        final GuidedDecisionTable52 model = presenter.getModel();

        for (ActionCol52 o : model.getActionCols()) {
            if (o.getHeader().equals(header)) {
                return false;
            }
        }
        return true;
    }

    WizardPage workItemPage() {
        workItemPage.enableParameters();

        return workItemPage;
    }

    AdditionalInfoPage additionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
    }

    @Override
    public Type getType() {
        return Type.ADVANCED;
    }
}
