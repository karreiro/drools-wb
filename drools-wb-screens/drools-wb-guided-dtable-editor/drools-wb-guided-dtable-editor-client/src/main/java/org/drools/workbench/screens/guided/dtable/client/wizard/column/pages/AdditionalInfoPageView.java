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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.addWidgetToContainer;

@Dependent
@Templated
public class AdditionalInfoPageView extends Composite implements AdditionalInfoPage.View {

    @Inject
    @DataField("headerFormItem")
    private Div headerFormItem;

    @Inject
    @DataField("hideColumnFormItem")
    private Div hideColumnFormItem;

    @Inject
    @DataField("updateEngineWithChangesFormItem")
    private Div updateEngineWithChangesFormItem;

    @Inject
    @DataField("logicallyInsertFormItem")
    private Div logicallyInsertFormItem;

    @Inject
    @DataField("header")
    private TextBox header;

    @Inject
    @DataField("hideColumnContainer")
    private Div hideColumnContainer;

    @Inject
    @DataField("updateEngineWithChanges")
    private CheckBox updateEngineWithChanges;

    @Inject
    @DataField("logicallyInsert")
    private CheckBox logicallyInsert;

    private AdditionalInfoPage page;

    @EventHandler("header")
    public void onSelectHeader(final KeyUpEvent event) {
        page.setHeader(header.getText());
    }

    @EventHandler("logicallyInsert")
    public void onSelectLogicallyInsert(final ChangeEvent event) {
        page.setInsertLogical(logicallyInsert.getValue());
    }

    @EventHandler("updateEngineWithChanges")
    public void onSelectUpdateEngineWithChanges(final ChangeEvent event) {
        page.setUpdate(updateEngineWithChanges.getValue());
    }

    @Override
    public void init(final AdditionalInfoPage page) {
        this.page = page;

        clear();
        setup();
    }

    @Override
    public void showHideColumn(final CheckBox checkBox) {
        addWidgetToContainer(checkBox,
                             hideColumnContainer);

        hideColumnFormItem.setHidden(false);
    }

    @Override
    public void showHeader() {
        header.setText(page.getHeader());

        headerFormItem.setHidden(false);
    }

    @Override
    public void showLogicallyInsert() {
        logicallyInsertFormItem.setHidden(false);
    }

    @Override
    public void showUpdateEngineWithChanges() {
        updateEngineWithChangesFormItem.setHidden(false);
    }

    private void setup() {
        page.setupHeader();
        page.setupHideColumn();
        page.setupLogicallyInsert();
        page.setupUpdateEngineWithChanges();
    }

    private void clear() {
        headerFormItem.setHidden(true);
        hideColumnFormItem.setHidden(true);
        logicallyInsertFormItem.setHidden(true);
        updateEngineWithChangesFormItem.setHidden(true);
    }
}
