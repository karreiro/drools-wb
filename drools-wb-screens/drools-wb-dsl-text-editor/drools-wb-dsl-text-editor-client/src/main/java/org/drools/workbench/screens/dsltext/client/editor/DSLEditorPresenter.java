/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.dsltext.client.editor;

import java.util.List;
import java.util.function.Supplier;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.IsWidget;
import org.drools.workbench.screens.dsltext.client.type.DSLResourceType;
import org.drools.workbench.screens.dsltext.model.DSLTextEditorContent;
import org.drools.workbench.screens.dsltext.service.DSLTextEditorService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.guvnor.common.services.shared.validation.model.ValidationMessage;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.workbench.common.widgets.client.popups.validation.ValidationPopup;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.metadata.client.KieEditor;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartTitleDecoration;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.ext.editor.commons.service.support.SupportsSaveAndRename;
import org.uberfire.ext.widgets.common.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnMayClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.Menus;

/**
 * DSL Editor Presenter.
 */
@Dependent
@WorkbenchEditor(identifier = "DSLEditor", supportedTypes = { DSLResourceType.class })
public class DSLEditorPresenter
        extends KieEditor<String> {

    @Inject
    private Caller<DSLTextEditorService> dslTextEditorService;

    @Inject
    private Event<NotificationEvent> notification;

    @Inject
    private ValidationPopup validationPopup;

    private DSLEditorView view;

    @Inject
    private DSLResourceType type;

    @Inject
    public DSLEditorPresenter( final DSLEditorView baseView ) {
        super( baseView );
        view = baseView;
    }

    @OnStartup
    public void onStartup( final ObservablePath path,
                           final PlaceRequest place ) {
        this.init( path,
                   place,
                   type );
    }

    protected void loadContent() {
        view.showLoading();
        dslTextEditorService.call( getModelSuccessCallback(),
                                   getNoSuchFileExceptionErrorCallback() ).loadContent( versionRecordManager.getCurrentPath() );
    }

    @Override
    protected Supplier<String> getContentSupplier() {
        return () -> view.getContent();
    }

    @Override
    protected Caller<? extends SupportsSaveAndRename<String, Metadata>> getSaveAndRenameServiceCaller() {
        return dslTextEditorService;
    }

    private RemoteCallback<DSLTextEditorContent> getModelSuccessCallback() {
        return new RemoteCallback<DSLTextEditorContent>() {

            @Override
            public void callback( final DSLTextEditorContent content ) {
                //Path is set to null when the Editor is closed (which can happen before async calls complete).
                if ( versionRecordManager.getCurrentPath() == null ) {
                    return;
                }

                resetEditorPages( content.getOverview() );

                view.setContent( content.getModel() );
                view.setReadOnly( isReadOnly );
                view.hideBusyIndicator();

                // We need to get the hash from the widget.
                // Widget changes the String somehow -> hash changes, even though the string is the same.
                createOriginalHash( view.getContent() );
            }
        };
    }

    protected Command onValidate() {
        return new Command() {
            @Override
            public void execute() {
                dslTextEditorService.call( new RemoteCallback<List<ValidationMessage>>() {
                    @Override
                    public void callback( final List<ValidationMessage> results ) {
                        if ( results == null || results.isEmpty() ) {
                            notification.fire( new NotificationEvent( CommonConstants.INSTANCE.ItemValidatedSuccessfully(),
                                                                      NotificationEvent.NotificationType.SUCCESS ) );
                        } else {
                            validationPopup.showMessages( results );
                        }
                    }
                } ).validate( versionRecordManager.getCurrentPath(),
                              view.getContent() );
            }
        };
    }

    @Override
    protected void save( String commitMessage ) {
        dslTextEditorService.call( getSaveSuccessCallback( view.getContent().hashCode() ),
                                   new HasBusyIndicatorDefaultErrorCallback( view ) ).save( versionRecordManager.getCurrentPath(),
                                                                                            view.getContent(),
                                                                                            metadata,
                                                                                            commitMessage );
    }

    @OnClose
    public void onClose() {
        this.versionRecordManager.clear();
    }

    @OnMayClose
    public boolean mayClose() {
        return super.mayClose( view.getContent() );
    }

    @WorkbenchPartTitleDecoration
    public IsWidget getTitle() {
        return super.getTitle();
    }

    @WorkbenchPartTitle
    public String getTitleText() {
        return super.getTitleText();
    }

    @WorkbenchPartView
    public IsWidget getWidget() {
        return super.getWidget();
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

}
