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

import com.google.gwt.user.client.ui.IsWidget;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.addWidgetToContainer;

@Dependent
@Templated
public class OperatorPageView implements IsElement,
                                         OperatorPage.View {

    private OperatorPage page;

    @DataField("warning")
    private Div warning;

    @DataField("info")
    private Div info;

    @DataField("operatorsContainer")
    private Div operatorsContainer;

    private TranslationService translationService;

    @Inject
    public OperatorPageView(final Div warning,
                            final Div info,
                            final Div operatorsContainer,
                            final TranslationService translationService) {
        this.warning = warning;
        this.info = info;
        this.operatorsContainer = operatorsContainer;
        this.translationService = translationService;
    }

    @Override
    public void init(final OperatorPage page) {
        this.page = page;
    }

    @Override
    public void showFactFieldWarningWhenItIsNotDefined(final boolean hasOperator) {
        info.setHidden(true);
        warning.setHidden(hasOperator);
    }

    @Override
    public void showPredicateWarning() {
        info.setHidden(false);
        warning.setHidden(true);
    }

    @Override
    public void setupOperator(final IsWidget dropdown) {
        addWidgetToContainer(dropdown,
                             operatorsContainer);
    }
}
