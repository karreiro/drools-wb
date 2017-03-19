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

    public AdditionalInfoPage<T> enableHideColumn() {
        hideColumnEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableLogicallyInsert() {
        logicallyInsertEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableUpdateEngineWithChanges() {
        updateEngineWithChangesEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableHeader() {
        this.headerEnabled = true;

        return this;
    }

    public String getHeader() {
        return plugin().editingCol().getHeader();
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
        final boolean canSetupHideColumn = hideColumnEnabled;

        if (canSetupHideColumn) {
            view.showHideColumn(newHideColumnCheckBox());
        }
    }

    void setupLogicallyInsert() {
        final boolean canSetupLogicallyInsert = logicallyInsertEnabled;

        if (canSetupLogicallyInsert) {
            view.showLogicallyInsert();
        }
    }

    void setupUpdateEngineWithChanges() {
        final boolean canSetupUpdateEngine = updateEngineWithChangesEnabled;

        if (canSetupUpdateEngine) {
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

    public interface View extends UberView<AdditionalInfoPage> {

        void showHideColumn(final CheckBox checkBox);

        void showHeader();

        void showLogicallyInsert();

        void showUpdateEngineWithChanges();
    }
}
