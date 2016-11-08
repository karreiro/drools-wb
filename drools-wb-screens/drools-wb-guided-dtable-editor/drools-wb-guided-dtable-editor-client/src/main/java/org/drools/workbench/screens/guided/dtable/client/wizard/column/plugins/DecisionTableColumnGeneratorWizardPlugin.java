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

import java.util.List;

import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

public interface DecisionTableColumnGeneratorWizardPlugin {

    //Shown as the text to Users in the "type selection list" on the first page of the Wizard
    String getTitle();

    //Return a list of subsequent pages required for the "type" selected in the first page list
    List<WizardPage> getPages();

    //Responsible for creation of the column itself when the Wizard completes
    Boolean generateColumn();
}