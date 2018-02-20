/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.editor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.editor.command.GDTCommandHistoryHandler;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.EditMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.InsertMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.RadarMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.menu.ViewMenuBuilder;
import org.drools.workbench.screens.guided.dtable.client.editor.page.ColumnsPage;
import org.drools.workbench.screens.guided.dtable.client.type.GuidedDTableResourceType;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.model.GuidedDecisionTableEditorContent;
import org.drools.workbench.screens.guided.dtable.service.GuidedDecisionTableEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.metadata.model.Overview;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilder;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UpdatedLockStatusEvent;
import org.uberfire.ext.editor.commons.client.menu.common.SaveAndRenameCommandBuilder;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnFocus;
import org.uberfire.lifecycle.OnMayClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;

import static org.uberfire.client.annotations.WorkbenchEditor.LockingStrategy.EDITOR_PROVIDED;

/**
 * Guided Decision Table Editor Presenter
 */
@Dependent
@WorkbenchEditor(identifier = "GuidedDecisionTableEditor", supportedTypes = {GuidedDTableResourceType.class}, lockingStrategy = EDITOR_PROVIDED)
public class GuidedDecisionTableEditorPresenter extends BaseGuidedDecisionTableEditorPresenter {

    private final SaveAndRenameCommandBuilder<GuidedDecisionTable52, Metadata> saveAndRenameCommandBuilder;

    @Inject
    private GDTCommandHistoryHandler commandHistoryHandler;

    @Inject
    public GuidedDecisionTableEditorPresenter(final View view,
                                              final Caller<GuidedDecisionTableEditorService> service,
                                              final Event<NotificationEvent> notification,
                                              final Event<DecisionTableSelectedEvent> decisionTableSelectedEvent,
                                              final ValidationPopup validationPopup,
                                              final GuidedDTableResourceType resourceType,
                                              final EditMenuBuilder editMenuBuilder,
                                              final ViewMenuBuilder viewMenuBuilder,
                                              final InsertMenuBuilder insertMenuBuilder,
                                              final RadarMenuBuilder radarMenuBuilder,
                                              final GuidedDecisionTableModellerView.Presenter modeller,
                                              final SyncBeanManager beanManager,
                                              final PlaceManager placeManager,
                                              final ColumnsPage columnsPage,
                                              final SaveAndRenameCommandBuilder<GuidedDecisionTable52, Metadata> saveAndRenameCommandBuilder) {
        super(view,
              service,
              notification,
              decisionTableSelectedEvent,
              validationPopup,
              resourceType,
              editMenuBuilder,
              viewMenuBuilder,
              insertMenuBuilder,
              radarMenuBuilder,
              modeller,
              beanManager,
              placeManager,
              columnsPage);

        modeller.setCommandHistoryHandler(commandHistoryHandler);

        this.saveAndRenameCommandBuilder = saveAndRenameCommandBuilder;
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
    }

    @Override
    @OnStartup
    public void onStartup(final ObservablePath path,
                          final PlaceRequest placeRequest) {
        super.onStartup(path,
                        placeRequest);

        loadDocument(path,
                     placeRequest);
    }

    @Override
    @OnFocus
    public void onFocus() {
        super.onFocus();
    }

    @Override
    public void loadDocument(final ObservablePath path,
                             final PlaceRequest placeRequest) {
        view.showLoading();
        service.call(getLoadContentSuccessCallback(path,
                                                   placeRequest),
                     getNoSuchFileExceptionErrorCallback()).loadContent(path);
    }

    protected RemoteCallback<GuidedDecisionTableEditorContent> getLoadContentSuccessCallback(final ObservablePath path,
                                                                                             final PlaceRequest placeRequest) {
        return (content) -> {
            //Path is set to null when the Editor is closed (which can happen before async calls complete).
            if (path == null) {
                return;
            }

            //Add Decision Table to modeller
            final GuidedDecisionTableView.Presenter dtPresenter = modeller.addDecisionTable(path,
                                                                                            placeRequest,
                                                                                            content,
                                                                                            placeRequest.getParameter("readOnly",
                                                                                                                      null) != null,
                                                                                            null,
                                                                                            null);
            registerDocument(dtPresenter);

            decisionTableSelectedEvent.fire(new DecisionTableSelectedEvent(dtPresenter));

            modeller.getView().getGridPanel().setFocus(true);

            view.hideBusyIndicator();
        };
    }

    @Override
    @WorkbenchPartTitle
    public String getTitleText() {
        return super.getTitleText();
    }

    @Override
    @WorkbenchPartView
    public IsWidget getWidget() {
        return super.getWidget();
    }

    @Override
    @WorkbenchMenu
    public Menus getMenus() {
        return super.getMenus();
    }

    @Override
    @OnMayClose
    public boolean mayClose() {
        return super.mayClose();
    }

    @Override
    @OnClose
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void onDecisionTableSelected(final @Observes DecisionTableSelectedEvent event) {
        super.onDecisionTableSelected(event);
    }

    @Override
    public void makeMenuBar() {
        if (canUpdateProject()) {
            getFileMenuBuilder()
                    .addSave(getSaveMenuItem())
                    .addCopy(() -> getActiveDocument().getCurrentPath(),
                             assetUpdateValidator)
                    .addRename(getSaveAndRenameCommand())
                    .addDelete(() -> getActiveDocument().getLatestPath(),
                               assetUpdateValidator);
        }

        this.menus = getFileMenuBuilder()
                .addNewTopLevelMenu(makeMenuItem("⟲ Undo", commandHistoryHandler::undo))
                .addNewTopLevelMenu(makeMenuItem("Redo ⟳", commandHistoryHandler::redo))
                .addValidate(() -> onValidate(getActiveDocument()))
                .addNewTopLevelMenu(getEditMenuItem())
                .addNewTopLevelMenu(getViewMenuItem())
                .addNewTopLevelMenu(getInsertMenuItem())
                .addNewTopLevelMenu(getRadarMenuItem())
                .addNewTopLevelMenu(getVersionManagerMenuItem())
                .build();
    }

    private MenuItem makeMenuItem(final String identifier,
                                  final Command command) {

        return MenuFactory.newTopLevelMenu(identifier)
                .respondsWith(command)
                .endMenu()
                .build()
                .getItems()
                .get(0);
    }

    protected Command getSaveAndRenameCommand() {

        return saveAndRenameCommandBuilder
                .addPathSupplier(getPathSupplier())
                .addValidator(assetUpdateValidator)
                .addRenameService(service)
                .addMetadataSupplier(getMetadataSupplier())
                .addContentSupplier(getContentSupplier())
                .addIsDirtySupplier(getIsDirtySupplier())
                .build();
    }

    private Supplier<Path> getPathSupplier() {
        return () -> versionRecordManager.getPathToLatest();
    }

    Supplier<GuidedDecisionTable52> getContentSupplier() {
        return () -> getActiveDocument().getModel();
    }

    Supplier<Metadata> getMetadataSupplier() {

        return () -> {

            final Overview overview = getActiveDocument().getOverview();

            return overview.getMetadata();
        };
    }

    Supplier<Boolean> getIsDirtySupplier() {
        return () -> {

            final Integer originalHashCode = originalHashCode(getActiveDocument());
            final Integer currentHashCode = currentHashCode(getActiveDocument());

            return isDirty(originalHashCode, currentHashCode);
        };
    }

    int originalHashCode(final GuidedDecisionTableView.Presenter dtPresenter) {
        return dtPresenter.getOriginalHashCode();
    }

    int currentHashCode(final GuidedDecisionTableView.Presenter dtPresenter) {
        return dtPresenter.getModel().hashCode();
    }

    Set<GuidedDecisionTableView.Presenter> getAvailableDecisionTables() {
        return modeller.getAvailableDecisionTables();
    }

    FileMenuBuilder getFileMenuBuilder() {
        return fileMenuBuilder;
    }

    @Override
    public void onOpenDocumentsInEditor(final List<Path> selectedDocumentPaths) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getAvailableDocumentPaths(final Callback<List<Path>> callback) {
        callback.callback(Collections.emptyList());
    }

    @Override
    public void removeDocument(GuidedDecisionTableView.Presenter dtPresenter) {
        super.removeDocument(dtPresenter);
        scheduleClosure(() -> placeManager.forceClosePlace(editorPlaceRequest));
    }

    void onUpdatedLockStatusEvent(final @Observes UpdatedLockStatusEvent event) {
        super.onUpdatedLockStatusEvent(event);
    }

    void scheduleClosure(final Scheduler.ScheduledCommand command) {
        Scheduler.get().scheduleDeferred(command);
    }
}
