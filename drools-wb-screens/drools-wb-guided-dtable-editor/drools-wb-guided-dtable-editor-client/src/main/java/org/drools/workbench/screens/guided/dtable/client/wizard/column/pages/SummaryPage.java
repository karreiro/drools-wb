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

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

/**
 * A summary page for the guided Decision Table Wizard
 */
@Dependent
public class SummaryPage extends BaseDecisionTableColumnPage {

    private SimplePanel content = new SimplePanel();

    @Inject
    private Instance<BaseDecisionTableColumnPlugin> plugins;

    @Inject
    private View view;

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.SummaryPage_NewColumn);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(true);
    }

    @Override
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    void openPage(final String selectedItemText) {
        if (selectedItemText.isEmpty() || presenter.isReadOnly()) {
            return;
        }

        final BaseDecisionTableColumnPlugin plugin = findPluginByIdentifier(selectedItemText);

        wizard.start(plugin);
    }

    private BaseDecisionTableColumnPlugin findPluginByIdentifier(final String selectedItemText) {
        for (BaseDecisionTableColumnPlugin plugin : this.plugins) {
            if (plugin.getIdentifier().equals(selectedItemText)) {
                return plugin;
            }
        }

        throw new UnsupportedOperationException("The plugin " + selectedItemText + " does not have an implementation.");
    }

    public List<BaseDecisionTableColumnPlugin> pluginsSortedByTitle() {
        final List<BaseDecisionTableColumnPlugin> plugins = plugins();

        plugins.sort((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()));

        return plugins;
    }

    public List<BaseDecisionTableColumnPlugin> plugins() {
        return new ArrayList<BaseDecisionTableColumnPlugin>() {{
            plugins.forEach(this::add);
        }};
    }

    public interface View extends UberView<SummaryPage> {

    }
}
