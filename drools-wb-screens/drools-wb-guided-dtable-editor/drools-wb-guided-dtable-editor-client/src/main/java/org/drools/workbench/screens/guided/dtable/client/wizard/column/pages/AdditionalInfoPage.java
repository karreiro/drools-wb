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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class AdditionalInfoPage<T extends HasAdditionalInfoPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    private boolean headerEnabled = false;

    private boolean hideColumnEnabled = false;

    private boolean logicallyInsertEnabled = false;

    private boolean updateEngineWithChangesEnabled = false;

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.AdditionalInfoPage_AdditionalInfo);
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        final boolean result = isHeaderCompleted() && isLogicallyInsertCompleted() && isUpdateEngineWithChangesCompleted();

        callback.callback(result);
    }

    public void enableHideColumn() {
        hideColumnEnabled = true;
    }

    public void enableLogicallyInsert() {
        logicallyInsertEnabled = true;
    }

    public void enableUpdateEngineWithChanges() {
        updateEngineWithChangesEnabled = true;
    }

    public void enableHeader() {
        headerEnabled = true;
    }

    public String getHeader() {
        return plugin().getHeader();
    }

    void setHeader(final String header) {
        plugin().setHeader(header);
    }

    CheckBox newHideColumnCheckBox() {
        return DTCellValueWidgetFactory.getHideColumnIndicator(plugin().editingCol());
    }

    void setupHeader() {
        if (headerEnabled) {
            view.showHeader();
        }
    }

    void setupHideColumn() {
        if (hideColumnEnabled) {
            view.showHideColumn(newHideColumnCheckBox());
        }
    }

    void setupLogicallyInsert() {
        if (logicallyInsertEnabled) {
            view.showLogicallyInsert();
        }
    }

    void setupUpdateEngineWithChanges() {
        if (updateEngineWithChangesEnabled) {
            view.showUpdateEngineWithChanges();
        }
    }

    private boolean isUpdateEngineWithChangesCompleted() {
        return true;
    }

    private boolean isLogicallyInsertCompleted() {
        return true;
    }

    private boolean isHeaderCompleted() {
        return headerEnabled && !nil(getHeader());
    }

    void setInsertLogical(final Boolean value) {
        plugin().setInsertLogical(value);
    }

    public void setUpdate(final Boolean value) {
        plugin().setUpdate(value);
    }

    public interface View extends UberView<AdditionalInfoPage> {

        void showHideColumn(final CheckBox checkBox);

        void showHeader();

        void showLogicallyInsert();

        void showUpdateEngineWithChanges();
    }
}
