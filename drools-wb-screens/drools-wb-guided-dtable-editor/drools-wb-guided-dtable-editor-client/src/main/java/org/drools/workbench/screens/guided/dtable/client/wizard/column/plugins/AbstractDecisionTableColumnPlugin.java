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

import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.GuidedDecisionTableColumnWizard;

public abstract class AbstractDecisionTableColumnPlugin implements DecisionTableColumnGeneratorWizardPlugin {

    protected GuidedDecisionTableColumnWizard wizard;

    protected GuidedDecisionTableView.Presenter presenter;

    public void init( final GuidedDecisionTableColumnWizard wizard ) {
        this.wizard = wizard;
        this.presenter = wizard.getPresenter();

        setupFinishCommand();
    }

    private void setupFinishCommand() {
        wizard.setFinishCommand( this::generateColumn );
    }
}
