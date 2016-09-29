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

import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;

@Dependent
@Templated
public class SummaryPageView extends Composite implements SummaryPage.View {

    private SummaryPage page;

    @Inject
    @DataField("pluginsList")
    private ListBox pluginsList;

    @Inject
    @DataField("includeAdvanced")
    private CheckBox includeAdvanced;

    @Override
    public void init(final SummaryPage page) {
        this.page = page;

        setupPlugins();
    }

    @EventHandler("pluginsList")
    public void onPluginSelected(ChangeEvent event) {
        openSelectedPlugin();
    }

    @EventHandler("includeAdvanced")
    public void onSelectIncludeAdvanced(final ChangeEvent event) {
        page.setIncludeAdvanced(includeAdvanced.getValue());

        setupPlugins();
    }

    private void setupPlugins() {
        loadPluginList();
        setupPluginList();
    }

    private void openSelectedPlugin() {
        final String selectedValue = pluginsList.getSelectedValue();

        page.openPage(selectedValue);
    }

    private void loadPluginList() {
        final List<BaseDecisionTableColumnPlugin> plugins = page.pluginsByCategory();

        pluginsList.clear();

        for (final BaseDecisionTableColumnPlugin plugin : plugins) {
            pluginsList.addItem(plugin.getTitle(),
                                plugin.getIdentifier());
        }
    }

    private void setupPluginList() {
        pluginsList.setVisibleItemCount(pluginsList.getItemCount());
        pluginsList.setSelectedIndex(selectedPlugin());
    }

    private int selectedPlugin() {
        final DecisionTableColumnPlugin plugin = page.plugin();

        if (plugin != null) {
            return getCurrentIndexFromList(plugin.getIdentifier(),
                                           pluginsList);
        }

        return -1;
    }
}
