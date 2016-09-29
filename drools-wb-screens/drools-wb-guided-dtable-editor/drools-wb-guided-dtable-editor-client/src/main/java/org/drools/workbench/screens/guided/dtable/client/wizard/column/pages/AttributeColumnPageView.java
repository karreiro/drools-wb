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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.getCurrentIndexFromList;

@Dependent
@Templated
public class AttributeColumnPageView extends Composite implements AttributeColumnPage.View {

    private AttributeColumnPage page;

    @Inject
    @DataField("attributeList")
    private ListBox attributeList;

    @Inject
    @DataField("attributeDescription")
    private Div attributeDescription;

    @Override
    public void init(final AttributeColumnPage page) {
        this.page = page;

        setup();
    }

    @EventHandler("attributeList")
    public void onSelectAttribute(ChangeEvent event) {
        page.selectItem(attributeList.getSelectedItemText());

        attributeDescription.setAttribute("data-enabled",
                                          attributeList.getSelectedValue());
    }

    private void setup() {
        attributeList.clear();

        for (String item : page.getAttributes()) {
            attributeList.addItem(item);
        }

        attributeList.setVisibleItemCount(attributeList.getItemCount());
        attributeList.setSelectedIndex(attributeIndex());
    }

    private int attributeIndex() {
        return getCurrentIndexFromList(page.selectedAttribute(),
                                       attributeList);
    }
}
