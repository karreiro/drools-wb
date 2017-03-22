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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasWorkItemPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.kie.workbench.common.widgets.client.workitems.WorkItemParametersWidget;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class WorkItemPage<T extends HasWorkItemPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.WorkItemPage_WorkItem);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(plugin().isWorkItemSet());
    }

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    Set<PortableWorkDefinition> getWorkItems() {
        return presenter.getWorkItemDefinitions();
    }

    boolean hasWorkItems() {
        return getWorkItems().size() > 0;
    }

    void selectWorkItem() {
        final String selectedWorkItem = view.getSelectedWorkItem();

        plugin().setWorkItem(selectedWorkItem);

        showParameters();
    }

    void showParameters() {
        final boolean hasWorkItemDefinition = workItemDefinition() != null;

        if (hasWorkItemDefinition) {
            view.showParameters(parametersWidget());
        } else {
            view.hideParameters();
        }
    }

    String currentWorkItem() {
        final PortableWorkDefinition workItemDefinition = editingCol().getWorkItemDefinition();

        if (workItemDefinition != null) {
            return workItemDefinition.getName();
        } else {
            return "";
        }
    }

    private WorkItemParametersWidget parametersWidget() {
        final WorkItemParametersWidget parametersWidget = new WorkItemParametersWidget(presenter,
                                                                                       false);
        parametersWidget.setParameters(workItemDefinition().getParameters());

        return parametersWidget;
    }

    private PortableWorkDefinition workItemDefinition() {
        return editingCol().getWorkItemDefinition();
    }

    private ActionWorkItemCol52 editingCol() {
        return plugin().editingCol();
    }

    public interface View extends UberView<WorkItemPage> {

        void showParameters(final WorkItemParametersWidget parametersWidget);

        void hideParameters();

        String getSelectedWorkItem();
    }
}
