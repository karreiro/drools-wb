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

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.workitems.PortableWorkDefinition;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ConditionColumnPlugin;
import org.kie.workbench.common.widgets.client.workitems.WorkItemParametersWidget;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class WorkItemPage extends BaseDecisionTableColumnPage<ConditionColumnPlugin> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.WorkItemPage_WorkItem);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(false);
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

    public DTColumnConfig52 getEditingCol() {
        return plugin().editingCol();
    }

    Set<PortableWorkDefinition> getWorkItems() {
        return presenter.getWorkItemDefinitions();
    }

    boolean hasWorkItems() {
        return getWorkItems().size() > 0;
    }

    void showParameters() {
        view.showParameters(parametersWidget());
    }

    private WorkItemParametersWidget parametersWidget() {
        return new WorkItemParametersWidget(presenter,
                                            false);
    }

    boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    public interface View extends UberView<WorkItemPage> {

        void showParameters(final WorkItemParametersWidget parametersWidget);
    }
}
