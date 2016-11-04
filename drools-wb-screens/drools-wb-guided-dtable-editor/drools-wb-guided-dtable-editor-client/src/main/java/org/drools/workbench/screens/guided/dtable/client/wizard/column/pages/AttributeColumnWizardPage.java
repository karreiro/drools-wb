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

import java.util.Arrays;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.drools.workbench.screens.guided.rule.client.resources.GuidedRuleEditorResources;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class AttributeColumnWizardPage extends AbstractDecisionTableColumnPage {

    public interface View extends UberView<AttributeColumnWizardPage> {

        String getAttributeText();

        void setup();
    }

    @Inject
    private AttributeColumnWizardPage.View view;

    private SimplePanel content = new SimplePanel();

    @Override
    public String getTitle() {
        return GuidedRuleEditorResources.CONSTANTS.AddAnOptionToTheRule();
    }

    @Override
    public void isComplete( final Callback<Boolean> callback ) {
        callback.callback( true );
    }

    @Override
    public void initialise() {
    }

    @Override
    public void prepareView() {
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public String getAttributeName() {
        return view.getAttributeText();
    }

    String[] getAttributes() {
        String[] attributes = RuleAttributeWidget.getAttributesList();

        attributes = Arrays.copyOf( attributes, attributes.length + 1 );
        attributes[ attributes.length - 1 ] = GuidedDecisionTable52.NEGATE_RULE_ATTR;

        return attributes;
    }

    String[] getDuplicates() {
        return presenter.getExistingAttributeNames().toArray( new String[ 0 ] );
    }
}
