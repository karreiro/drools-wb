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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common;

import javax.inject.Inject;

import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public abstract class BaseDecisionTableColumnPage<T extends DecisionTableColumnPlugin> implements WizardPage {

    protected NewGuidedDecisionTableColumnWizard wizard;

    protected GuidedDecisionTableView.Presenter presenter;

    protected T plugin;

    @Inject
    private TranslationService translationService;

    // TODO {karreiro} - remove this two methods (init)

    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        this.wizard = wizard;
        this.presenter = wizard.getPresenter();

        initialise();
    }

    public void init(final T plugin) {
        this.plugin = plugin;
    }

    public T plugin() {
        return plugin;
    }

    protected String translate(final String key,
                               Object... args) {
        return translationService.format(key,
                                         args);
    }
}