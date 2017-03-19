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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.addWidgetToContainer;

@Dependent
@Templated
public class OperatorPageView extends Composite implements OperatorPage.View {

    private OperatorPage page;

    @Inject
    @DataField("warning")
    private Div warning;

    @Inject
    @DataField("info")
    private Div info;

    @Inject
    @DataField("operatorsContainer")
    private Div operatorsContainer;

    @Inject
    private TranslationService translationService;

    @Override
    public void init(final OperatorPage page) {
        this.page = page;

        toggleFields();
        setupOperator();
    }

    private void toggleFields() {
        if (page.isConstraintValuePredicate()) {
            info.setHidden(false);
            warning.setHidden(true);
        } else {
            info.setHidden(true);
            warning.setHidden(page.canSetOperator());
        }
    }

    private void setupOperator() {
        page.operatorDropdown(dropdown -> addWidgetToContainer(dropdown,
                                                               operatorsContainer));
    }
}
