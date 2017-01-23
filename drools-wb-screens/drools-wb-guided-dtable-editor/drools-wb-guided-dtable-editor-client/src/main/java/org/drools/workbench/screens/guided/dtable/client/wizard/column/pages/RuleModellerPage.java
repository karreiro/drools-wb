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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.datamodel.rule.RuleModel;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.BRLConditionColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.ModellerWidgetFactory;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModellerConfiguration;
import org.drools.workbench.screens.guided.template.client.editor.TemplateModellerWidgetFactory;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class RuleModellerPage extends BaseDecisionTableColumnPage<BRLConditionColumnPlugin> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public String getTitle() {
        return "Rule Modeller";
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( false );
    }

    @Override
    public void initialise() {
        content.setWidget( view );
    }

    @Override
    public void prepareView() {
        view.init( this );
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public RuleModeller ruleModeller() {
        final ModellerWidgetFactory widgetFactory = new TemplateModellerWidgetFactory();
        final RuleModellerConfiguration ruleModellerConfiguration = plugin().getRuleModellerConfiguration();
        final RuleModel ruleModel = plugin().getRuleModel();
        final AsyncPackageDataModelOracle oracle = presenter.getDataModelOracle();
        final EventBus eventBus = presenter.getEventBus();
        final boolean readOnly = presenter.isReadOnly();

        return new RuleModeller( ruleModel,
                                 oracle,
                                 widgetFactory,
                                 ruleModellerConfiguration,
                                 eventBus,
                                 readOnly );
    }

    public interface View extends UberView<RuleModellerPage> {

    }
}
