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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.widgets.client.workitems.WorkItemParametersWidget;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.addWidgetToContainer;
import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;

@Dependent
@Templated
public class WorkItemPageView extends Composite implements WorkItemPage.View {

    @Inject
    @DataField("workItems")
    private ListBox workItems;

    @Inject
    private TranslationService translationService;

    @Inject
    @DataField("workItemParametersContainer")
    private Div workItemParametersContainer;

    private WorkItemPage<?> page;

    @EventHandler("workItems")
    public void onSelectWorkItem(final ChangeEvent event) {
        page.selectWorkItem();
    }

    @Override
    public String getSelectedWorkItem() {
        return workItems.getSelectedValue();
    }

    @Override
    public void init(final WorkItemPage page) {
        this.page = page;

        setupWorkItemsList();
    }

    @Override
    public void showParameters(final WorkItemParametersWidget parametersWidget) {
        addWidgetToContainer(parametersWidget,
                             workItemParametersContainer);

        workItemParametersContainer.setHidden(false);
    }

    @Override
    public void hideParameters() {
        workItemParametersContainer.setHidden(true);
    }

    @Override
    public int workItemsCount() {
        return workItems.getItemCount();
    }

    private void setupWorkItemsList() {
        setupWorkItemList();

        if (!page.hasWorkItems()) {
            setupEmptyWorkItemList();
        }
    }

    private void setupEmptyWorkItemList() {
        workItems.clear();
        workItems.setEnabled(false);
        workItems.addItem(translate(GuidedDecisionTableErraiConstants.WorkItemPageView_NoWorkItemsAvailable));
    }

    private void setupWorkItemList() {
        workItems.clear();
        workItems.setEnabled(true);
        workItems.addItem(translate(GuidedDecisionTableErraiConstants.WorkItemPageView_Choose),
                          "");

        page.forEachWorkItem((displayName, name) -> this.workItems.addItem(displayName,
                                                                           name));

        selectCurrentWorkItem();
        showParameterForTheCurrentWorkItem();
    }

    private void showParameterForTheCurrentWorkItem() {
        page.showParameters();
    }

    private void selectCurrentWorkItem() {
        final String workItem = page.currentWorkItem();
        final int currentValueIndex = getCurrentIndexFromList(workItem,
                                                              workItems);

        workItems.setSelectedIndex(currentValueIndex);
    }

    private String translate(final String key,
                             final Object... args) {
        return translationService.format(key,
                                         args);
    }
}
