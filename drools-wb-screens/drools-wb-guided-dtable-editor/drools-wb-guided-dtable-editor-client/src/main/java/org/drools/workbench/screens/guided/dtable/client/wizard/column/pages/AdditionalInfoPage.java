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
import org.drools.workbench.screens.guided.dtable.client.widget.DTCellValueWidgetFactory;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class AdditionalInfoPage<T extends HasAdditionalInfoPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    private SimplePanel content = new SimplePanel();

    private boolean entryPointNameEnabled = false;

    private boolean headerEnabled = false;

    private boolean logicallyInsertEnabled = false;

    private boolean updateEngineWithChangesEnabled = false;

    boolean hideColumnEnabled = false;

    @Override
    public void initialise() {
        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return "Additional info";
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(false); // TODO
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    CheckBox newHideColumnCheckBox() {
        return DTCellValueWidgetFactory.getHideColumnIndicator(plugin().getEditingCol());
    }

    boolean canSetupHideColumn() {
        return hideColumnEnabled && plugin().getEditingCol() != null;
    }

    boolean canSetupHeader() {
        return headerEnabled;
    }

    boolean canSetupEntryPoint() {
        return entryPointNameEnabled;
    }

    boolean canSetupLogicallyInsert() {
        return logicallyInsertEnabled && plugin().getEditingCol() != null;
    }

    boolean canSetupUpdateEngineWithChanges() {
        return updateEngineWithChangesEnabled && plugin().getEditingCol() != null;
    }

    public AdditionalInfoPage<T> enableEntryPointName() {
        this.entryPointNameEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableHeader() {
        this.headerEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableHideColumn() {
        this.hideColumnEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableLogicallyInsert() {
        this.logicallyInsertEnabled = true;

        return this;
    }

    public AdditionalInfoPage<T> enableUpdateEngineWithChanges() {
        this.updateEngineWithChangesEnabled = true;

        return this;
    }

    void setupHideColumn() {
        if (canSetupHideColumn()) {
            view.showHideColumn(newHideColumnCheckBox());
        }
    }

    void setupHeader() {
        if (canSetupHeader()) {
            view.showHeader();
        }
    }

    void setupEntryPoint() {
        if (canSetupEntryPoint()) {
            view.showEntryPoint();
        }
    }

    void setupLogicallyInsert() {
        if (canSetupLogicallyInsert()) {
            view.showLogicallyInsert();
        }
    }

    void setupUpdateEngineWithChanges() {
        if (canSetupUpdateEngineWithChanges()) {
            view.showUpdateEngineWithChanges();
        }
    }

    public interface View extends UberView<AdditionalInfoPage> {

        void showHideColumn(final CheckBox checkBox);

        void showHeader();

        void showEntryPoint();

        void showLogicallyInsert();

        void showUpdateEngineWithChanges();
    }
}
