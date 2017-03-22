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
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.AttributeColumnPlugin;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class AttributeColumnPage extends BaseDecisionTableColumnPage<AttributeColumnPlugin> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.AttributeColumnPage_AddNewAttribute);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        final boolean hasAttribute = !nil(plugin().getAttribute());

        callback.callback(hasAttribute);
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

    List<String> getAttributes() {
        return removeAttributesAlreadyAdded(rawAttributes());
    }

    private List<String> rawAttributes() {
        return new ArrayList<String>() {{
            addAll(Arrays.asList(RuleAttributeWidget.getAttributesList()));
            add(GuidedDecisionTable52.NEGATE_RULE_ATTR);
        }};
    }

    private List<String> removeAttributesAlreadyAdded(final List<String> attributeList) {
        final List<String> attributes = new ArrayList<>(attributeList);

        attributes.removeAll(presenter.getReservedAttributeNames());

        return attributes;
    }

    void selectItem(String selectedItemText) {
        plugin().setAttribute(selectedItemText);
    }

    String selectedAttribute() {
        return plugin().getAttribute();
    }

    public interface View extends UberView<AttributeColumnPage> {

    }
}
